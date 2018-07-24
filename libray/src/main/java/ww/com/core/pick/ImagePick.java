package ww.com.core.pick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import ww.com.core.exception.StorageSpaceException;
import ww.com.core.utils.FileUtils;
import ww.com.core.utils.PhotoUtils;

@Deprecated
public class ImagePick {
    private static final long SMALL_SIZE = 20 * 1024 * 1024;  // 20MB
    private static final int NORMAL_WIDTH = 720;   // 图片默认保留的宽
    private static final int NORMAL_HEIGHT = 1280; // 图片默认保留的高
    private static final int NORMAL_OUTPUT_X = 250;  // 图片默认剪切的宽
    private static final int NORMAL_OUTPUT_Y = 250;  // 图片默认剪切的高
    private static final int NORMAL_ASPECT_X = 1;   // 图片剪切横向比例
    private static final int NORMAL_ASPECT_Y = 1;   // 图片剪切纵向比例

    public static final int INTENT_REQUEST_CODE_SINGLE_ALBUM = 8501; // 相册单选
    public static final int INTENT_REQUEST_CODE_MULTI_ALBUM = 8504; // 相册多选
    public static final int INTENT_REQUEST_CODE_CAMERA = 8502; // 相机
    public static final int INTENT_REQUEST_CODE_CROP = 8503; // 剪切

    private String mCurrImgFile; // 当前图片的文件路径

    // 图片的宽高
    private int outputX = NORMAL_OUTPUT_X;
    private int outputY = NORMAL_OUTPUT_Y;
    // 图片的比例
    private int aspectX = NORMAL_ASPECT_X;
    private int aspectY = NORMAL_ASPECT_Y;
    // 是否需要剪切
    private boolean mCrop = true;

    private OnImageListener imageListener;
    private Context context;
    private Fragment fragment;
    protected Activity activity;

    private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            if (null == imageListener)
                return;

            String resPath = (String) msg.obj;
            if (!mCrop) {
                imageListener.onSinglePath(resPath);
//                String imagePath = getImgFile();
//                Bitmap bitmap = ImageUtil.getBitmapFromFile(resPath, NORMAL_WIDTH, NORMAL_HEIGHT);
//                // 压缩...
//                PhotoUtils.saveBmpToSdCard(bitmap, new File(imagePath));
//                if (!bitmap.isRecycled()) {
//                    bitmap.recycle();
//                }
//                imageListener.onSinglePath(imagePath);
            } else {
                imageListener.onSinglePath(resPath);
            }
        }

    };


    public static final ImagePick init(Object obj, OnImageListener listener) {
        return new ImagePick(obj, listener);
    }

    /**
     * @param obj      (activity 或者是 fragment)
     * @param listener
     * @throws Exception 不知道的对象
     */
    private ImagePick(Object obj, OnImageListener listener) {
        if (obj instanceof Activity) {
            init((Activity) obj, listener);
        } else if (obj instanceof Fragment) {
            init((Fragment) obj, listener);
        } else {
            throw new RuntimeException("obj is activity or fragment ");
        }
    }

    private void init(Activity activity, OnImageListener listener) {
        init(activity, listener, null);
    }

    private void init(Fragment fragment, OnImageListener listener) {
        init(fragment.getActivity(), listener, fragment);
    }

    /**
     * @param activity
     * @param listener
     */
    private void init(Activity activity, OnImageListener listener, Fragment fragment) {
        this.activity = activity;
        context = this.activity;
        this.imageListener = listener;
        this.fragment = fragment;
    }

    /**
     * 检查SD卡是否存在
     * </br>
     * 检查SD卡存储空间是否不足(低于20MB)
     *
     * @throws StorageSpaceException
     */
    private void checkSdcard() throws StorageSpaceException {
        if (!FileUtils.isSdcardExist()) {
            throw new StorageSpaceException("Please check whether the Sdcard is unplugged");
        }

        if (FileUtils.getFreeDiskSpace() < SMALL_SIZE) {
            throw new StorageSpaceException("Check that the storage space is insufficient");
        }
    }

    /**
     * 保存状态
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("outputX", outputX);
        outState.putInt("outputY", outputY);
        outState.putInt("aspectX", aspectX);
        outState.putInt("aspectY", aspectY);
        outState.putBoolean("mCrop", mCrop);
        outState.putString("mCurrImgFile", mCurrImgFile);
    }

    /**
     * 还原状态
     *
     * @param savedInstanceState
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        outputX = savedInstanceState.getInt("outputX", NORMAL_OUTPUT_X);
        outputY = savedInstanceState.getInt("outputY", NORMAL_OUTPUT_Y);
        aspectX = savedInstanceState.getInt("aspectX", NORMAL_ASPECT_X);
        aspectY = savedInstanceState.getInt("aspectY", NORMAL_ASPECT_Y);
        mCrop = savedInstanceState.getBoolean("mCrop", true);
        mCurrImgFile = savedInstanceState.getString("mCurrImgFile", "");
    }

    /**
     * 打开相册
     */
    public void startAlbumSingle() {
        startAlbumSingle(false);
    }

    public void startAlbumSingle(boolean showCamera) {
        MultiImageSelector selector = MultiImageSelector.create()
                .showCamera(showCamera) // show camera or not. true by default
                .single(); // single mode

//        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                "image/jpeg");
        if (fragment != null) {
//            fragment.startActivityForResult(intent, INTENT_REQUEST_CODE_SINGLE_ALBUM);
            selector.start(fragment, INTENT_REQUEST_CODE_SINGLE_ALBUM);
            return;
        }
//        activity.startActivityForResult(intent, INTENT_REQUEST_CODE_SINGLE_ALBUM);
        selector.start(activity, INTENT_REQUEST_CODE_SINGLE_ALBUM);
    }

    /**
     * 开启多张选择
     *
     * @param count
     * @param images
     */
    public void startAlbumMulti(int count, ArrayList<String> images) {
        startAlbumMulti(false, count, images);
    }

    public void startAlbumMulti(boolean showCamera, int count, ArrayList<String> images) {
        MultiImageSelector selector = MultiImageSelector.create()
                .showCamera(showCamera)
                .count(count)
                .multi();
        if (images != null) {
            selector.origin(images);
        }

        if (fragment != null) {
            selector.start(fragment, INTENT_REQUEST_CODE_MULTI_ALBUM);
            return;
        }
        selector.start(activity, INTENT_REQUEST_CODE_MULTI_ALBUM);
    }

    /**
     * 开启相机
     *
     * @throws StorageSpaceException
     */
    public void startCamera() throws StorageSpaceException {
        checkSdcard();
        String path = getImgFile();
        File file = FileUtils.createNewFile(path);
        if (file == null) {
            throw new StorageSpaceException("Failed to create file");
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (file != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        mCurrImgFile = path;
        if (this.fragment != null) {
            fragment.startActivityForResult(intent, INTENT_REQUEST_CODE_CAMERA);
            return;
        }
        activity.startActivityForResult(intent, INTENT_REQUEST_CODE_CAMERA);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) throws StorageSpaceException {
        if (!FileUtils.isSdcardExist()) {
            Toast.makeText(activity, "SD卡不可用!", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode) {
            // 选择相册返回
            case INTENT_REQUEST_CODE_MULTI_ALBUM: {
                if (data == null) {
                    return;
                }
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    imageListener.onMultiPaths(path);
                }
            }
            break;
            case INTENT_REQUEST_CODE_SINGLE_ALBUM:
                if (data == null) {
                    return;
                }
                if (resultCode == Activity.RESULT_OK) {
                    List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    String resPath = "";
                    if (path != null && path.size() == 1) {
                        resPath = path.get(0);
                    }

                    if (TextUtils.isEmpty(resPath)) {
                        return;
                    }

                    if (!mCrop) {
                        handler.sendMessage(handler.obtainMessage(
                                INTENT_REQUEST_CODE_SINGLE_ALBUM, resPath));
                        return;
                    }
                    mCurrImgFile = getImgFile();
                    if (TextUtils.isEmpty(resPath)) {
                        return;
                    }
                    cropPhoto(resPath, mCurrImgFile);
                }
                break;

            case INTENT_REQUEST_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    if (!TextUtils.isEmpty(mCurrImgFile)) {
                        if (!mCrop) {
                            handler.sendMessage(handler.obtainMessage(
                                    INTENT_REQUEST_CODE_CAMERA, mCurrImgFile));
                            return;
                        }
                        String resPath = mCurrImgFile;
                        mCurrImgFile = getImgFile();
                        cropPhoto(resPath, mCurrImgFile);
                    }
                }
                break;

            case INTENT_REQUEST_CODE_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    handler.sendMessage(handler.obtainMessage(
                            INTENT_REQUEST_CODE_CROP, mCurrImgFile));
                }
                break;
        }

    }


    /**
     * @param resPath 剪切图片的地址
     * @param outPath 剪切成功后的保存的目标地址
     */
    private void cropPhoto(String resPath, String outPath) {
        Uri uri = FileUtils.getUriFromFile(resPath);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                FileUtils.getUriFromFile(outPath));
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true); // no face detection
        if (fragment != null) {
            fragment.startActivityForResult(intent, INTENT_REQUEST_CODE_CROP);
            return;
        }
        activity.startActivityForResult(intent, INTENT_REQUEST_CODE_CROP);
    }

    private String getImgPath(Intent data) {
        Uri uri = data.getData();
        Scheme scheme = Scheme.ofUri(uri.toString());
        switch (scheme) {
            case CONTENT: {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = activity.getContentResolver().query(uri, proj,
                        null, null, null);
                try {
                    if (cursor != null) {
                        int column_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                            String path = cursor.getString(column_index);
                            return path;
                        }
                    }
                    return null;
                } catch (Exception e) {
                } finally {
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Exception e2) {
                        }
                    }
                }
            }
            break;
            case FILE:
                return scheme.crop(uri.toString());
        }

        return null;
    }

    private String getImgFile() throws StorageSpaceException {
        checkSdcard();
        String pName = getPictureName();
        File cachePath = getCachePath(context);
        if (cachePath == null) {
            throw new StorageSpaceException("Failed to cache path ");
        }
        return new File(getCachePath(context), pName).getAbsolutePath();
    }

    public ImagePick setOutputX(int outputX) {
        this.outputX = outputX;
        return this;
    }

    public ImagePick setOutputY(int outputY) {
        this.outputY = outputY;
        return this;
    }

    public ImagePick setAspectX(int aspectX) {
        this.aspectX = aspectX;
        return this;
    }

    public ImagePick setAspectY(int aspectY) {
        this.aspectY = aspectY;
        return this;
    }

    public ImagePick setCrop(boolean crop) {
        this.mCrop = crop;
        return this;
    }


    /**
     * 清理图片缓存
     *
     * @param context
     */
    public static void clearImgCache(Context context) {
        ImageCache.clearCache(context);
    }

    /**
     * 获取图片缓存目录
     *
     * @param context
     * @return file  (如果等于 null， 可能是sd卡 硬盘空间不足，或者是sd 卡被拔出)
     */
    public static File getCachePath(Context context) {
        return ImageCache.getIndividualCacheDirectory(context);
    }


    public static long getImageCacheFreeSize(Context context) {
        return ImageCache.getCacheFreeSize(context);
    }

    public static String getPictureName() {
        return PhotoUtils.getPictureName();
    }

    public interface OnImageListener {
        void onSinglePath(String path);

        void onMultiPaths(ArrayList<String> paths);
    }

    public enum Scheme {
        HTTP("http"), HTTPS("https"), FILE("file"), CONTENT("content"), ASSETS(
                "assets"), DRAWABLE("drawable"), UNKNOWN("");

        private String scheme;
        private String uriPrefix;

        Scheme(String scheme) {
            this.scheme = scheme;
            uriPrefix = scheme + "://";
        }

        /**
         * Defines scheme of incoming URI
         *
         * @param uri URI for scheme detection
         * @return Scheme of incoming URI
         */
        public static Scheme ofUri(String uri) {
            if (uri != null) {
                for (Scheme s : values()) {
                    if (s.belongsTo(uri)) {
                        return s;
                    }
                }
            }
            return UNKNOWN;
        }

        private boolean belongsTo(String uri) {
            return uri.toLowerCase(Locale.US).startsWith(uriPrefix);
        }

        /**
         * Appends scheme to incoming path
         */
        public String wrap(String path) {
            return uriPrefix + path;
        }

        /**
         * Removed scheme part ("scheme://") from incoming URI
         */
        public String crop(String uri) {
            if (!belongsTo(uri)) {
                throw new IllegalArgumentException(String.format(
                        "URI [%1$s] doesn't have expected scheme [%2$s]", uri,
                        scheme));
            }
            return uri.substring(uriPrefix.length());
        }
    }
}
