package ww.com.core.media;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by feng on 2017/1/25.
 * 视频回调
 */

public class MediaCallback implements SurfaceHolder.Callback {
    private static final String TAG = "MediaCallback";
    public static final int MEDIA_AUDIO = 0;
    public static final int MEDIA_VIDEO = 1;

    private Activity activity;
    private MediaRecorder mediaRecorder;
    private CamcorderProfile profile;
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private File targetDir;                   //存储目录
    private String targetName;                //存储文件名
    private File targetFile;                  //存储文件
    private int previewWidth, previewHeight;  //预览宽高
    private int recorderType;
    private boolean isRecording;
    private GestureDetector detector;
    private boolean isZoomIn = false;
    private int or = 90;


    public MediaCallback(Activity activity) {
        this.activity = activity;
    }

    public void setTargetDir(File targetDir) {
        this.targetDir = targetDir;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public void setRecorderType(int recorderType) {
        this.recorderType = recorderType;
    }

    public String getSaveFilePath() {
        return targetFile.getPath();
    }

    public boolean deleteSaveFile() {
        if (targetFile.exists()) {
            return targetFile.delete();
        } else {
            return false;
        }
    }


    public void setSurfaceView(SurfaceView view) {
        this.surfaceView = view;
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setFixedSize(previewWidth, previewHeight);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);
        detector = new GestureDetector(activity, new ZoomGestureListener());

        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }


    public int getPreviewWidth() {
        return previewWidth;
    }

    public int getPreviewHeight() {
        return previewHeight;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void record() {
        if (isRecording) {
            try {
                mediaRecorder.stop();
            } catch (RuntimeException e) {

                targetFile.delete();
                e.printStackTrace();
            }
            releaseMediaRecorder();
            camera.lock();
            isRecording = false;

        } else {
            startRecordThread();
        }
    }

    public boolean prepareRecord() {
        try {
            mediaRecorder = new MediaRecorder();
            if (recorderType == MEDIA_VIDEO) {
                camera.unlock();
                mediaRecorder.setCamera(camera);
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                mediaRecorder.setProfile(profile);
                mediaRecorder.setOrientationHint(or);
            } else if (recorderType == MEDIA_AUDIO) {
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            }
            targetFile = new File(targetDir, targetName);
            mediaRecorder.setOutputFile(targetFile.getPath());

        } catch (Exception e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }
        return true;
    }


    public void stopRecordSave() {
        if (isRecording) {
            isRecording = false;
            try {
                mediaRecorder.stop();
            } catch (RuntimeException r) {
                r.printStackTrace();
            } finally {
                releaseMediaRecorder();
            }
        }
    }

    public void stopRecordUnSave() {
        if (isRecording) {
            isRecording = false;
            try {
                mediaRecorder.stop();
            } catch (RuntimeException r) {
                if (targetFile.exists()) {
                    targetFile.delete();
                }
            } finally {
                releaseMediaRecorder();
            }

            if (targetFile.exists()) {
                targetFile.delete();
            }
        }
    }


    private void startPreView(SurfaceHolder holder) {
        if (camera == null) {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }

        if (camera != null) {
            camera.setDisplayOrientation(or);

            try {
                camera.setPreviewDisplay(holder);
                Camera.Parameters parameters = camera.getParameters();
                List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
                List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
                Camera.Size optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
                        mSupportedPreviewSizes, surfaceView.getWidth(), surfaceView.getHeight());
                // Use the same size for recording profile.

                previewWidth = optimalSize.width;
                previewHeight = optimalSize.height;

                parameters.setPreviewSize(previewWidth, previewHeight);

                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
                // 这里是重点，分辨率和比特率
                // 分辨率越大视频大小越大，比特率越大视频越清晰
                // 清晰度由比特率决定，视频尺寸和像素量由分辨率决定
                // 比特率越高越清晰（前提是分辨率保持不变），分辨率越大视频尺寸越大。
                profile.videoFrameWidth = optimalSize.width;
                profile.videoFrameHeight = optimalSize.height;
                // 这样设置 1080p的视频 大小在5M , 可根据自己需求调节
                profile.videoBitRate = 2 * optimalSize.width * optimalSize.height;
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes != null) {
                    for (String mode : focusModes) {
                        mode.contains("continuous-video");
                        parameters.setFocusMode("continuous-video");
                    }
                }

                camera.setParameters(parameters);
                camera.startPreview();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            // clear recorder configuration
            mediaRecorder.reset();
            // release the recorder object
            mediaRecorder.release();
            mediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            Log.d(TAG, "release Recorder");
        }
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
            Log.d(TAG, "release Camera");
        }
    }



    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        startPreView(this.surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (camera!=null){
            releaseCamera();
        }

        if (mediaRecorder!=null){
            releaseMediaRecorder();
        }
    }



    private void startRecordThread() {
        if (prepareRecord()) {
            try {
                mediaRecorder.start();
                isRecording = true;
            } catch (RuntimeException r) {
                r.printStackTrace();
                releaseMediaRecorder();
            }
        }
    }

    private class ZoomGestureListener extends GestureDetector.SimpleOnGestureListener {
        //双击手势事件
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            super.onDoubleTap(e);
            Log.d(TAG, "onDoubleTap: 双击事件");
            if (!isZoomIn) {
                setZoom(20);
                isZoomIn = true;
            } else {
                setZoom(0);
                isZoomIn = false;
            }
            return true;
        }
    }

    private void setZoom(int zoomValue) {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            if (parameters.isZoomSupported()) {
                int maxZoom = parameters.getMaxZoom();
                if (maxZoom == 0) {
                    return;
                }
                if (zoomValue > maxZoom) {
                    zoomValue = maxZoom;
                }
                parameters.setZoom(zoomValue);
                camera.setParameters(parameters);
            }
        }
    }

    private  String getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return bitmap2File(media.getFrameAtTime());
    }

    private  String bitmap2File(Bitmap bitmap) {
        File thumbFile = new File(targetDir,
                targetName);
        if (thumbFile.exists()) thumbFile.delete();
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(thumbFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            return null;
        }
        return thumbFile.getAbsolutePath();
    }

}
