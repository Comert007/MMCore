package com.android.ww.mmcore;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import ww.com.core.Debug;
import ww.com.core.exception.StorageSpaceException;
import ww.com.core.pick.ImagePick;
import ww.com.core.utils.FileUtils;
import ww.com.core.utils.PermissionDispose;
import ww.com.core.utils.PhotoUtils;

public class MainActivity extends AppCompatActivity implements ImagePick.OnImageListener {
    ImageView imgView;
    ImagePick pick;

    TextView textView;
    Button btnMedia;

    private ArrayList<String> multiImages;
    private PermissionDispose dispose;

    private final int CAMERA_REQUEST_CODE = 0x23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        btnMedia = (Button) findViewById(R.id.btn_media);

        pick = ImagePick.init(this, this);

        // 初始化permissionDispose
        dispose = PermissionDispose.init(this, new PermissionDispose.OnPermissionListener() {
            @Override
            public void onSuccess(int requestCode, Map<String, Integer> successPermissions) {
                // successPermissions 成功时返回的所以权限
//                showToast("获取权限成功");
                VideoRecorderActivity.start(MainActivity.this);
            }

            @Override
            public void onFinal(int requestCode, Map<String, Integer> failPermissions) {
                // failPermissions 失败时返回的失败权限
                showToast("获取权限失败");
            }
        });

        testPermissionDispose();

        Debug.setTag("Test");
        testExcMethod();

        showCacheFreeSize();
    }

    private static final int DEFAULT_MAX_BITMAP_DIMENSION = 2048;

    public void testExcMethod() {
//        StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
//        if (traceElements != null) {
//            for (int i = 0; i < traceElements.length; i++) {
//                String clsName = traceElements[i].getClassName();
//                if (MainActivity.class.getSimpleName().equals(clsName)) {
//                    Debug.d(String.format("trance method name [%1$d] : %2$s", i,
// traceElements[i].getMethodName()));
//                }
//            }
//        }
    }

    //测试权限申请
    private void testPermissionDispose() {
        btnMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispose.requestPermission(CAMERA_REQUEST_CODE, Manifest.permission.CAMERA);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        dispose.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        pick.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
        Debug.d("onSaveInstanceState()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        pick.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
        Debug.d("onRestoreInstanceState()");
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            pick.onActivityResult(requestCode, resultCode, data);
        } catch (StorageSpaceException e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }

    public void onSdCardFreeSize(View v) {
        onAlbumMulti(v);
//        long size = FileUtils.getFreeDiskSpace();
//        String sizeName = FileUtils.formatFileSize(size);
//        Debug.d(String.format("SdCard free disk apace > free %1$s  : sizeName : %2$s ", size +
// "", sizeName));
    }

    public void onLocalPhoto(View v) {
        pick.setCrop(true);
        pick.startAlbumSingle();
    }


    public void onRecord(View v){
        dispose.requestPermission(CAMERA_REQUEST_CODE, Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO);
    }

    public void onMedia(View v) {
        try {
            pick.setCrop(true);
            pick.startCamera();
        } catch (StorageSpaceException e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }

    public void onAlbumMulti(View v) {
        pick.startAlbumMulti(9, multiImages);
    }

    @Override
    public void onSinglePath(String path) {
        Debug.d("path : " + path);
        imgView.setImageBitmap(PhotoUtils.getBitmapFromFile(path));

        showCacheFreeSize();
    }

    @Override
    public void onMultiPaths(ArrayList<String> paths) {
        Debug.d("path : " + paths.size());
        multiImages = paths;

        showCacheFreeSize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ImagePick.clearImgCache(this);
    }

    private void showCacheFreeSize() {
        long size = ImagePick.getImageCacheFreeSize(this);
        String sizeName = FileUtils.formatFileSize(size);
        textView.setText(String.format("SdCard free disk apace > free %1$s  : sizeName : %2$s ",
                size + "", sizeName));
    }
}


