package ww.com.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2016/11/24.
 * 6.0以上权限申请
 */

public class PermissionDispose {

    private Activity activity;
    private Fragment fragment;
    private Context context;

    private OnPermissionListener permissionListener;
//    private Map<String, Integer> resultPermissions; //权限以及请求状态
    private Map<String, Integer> successMap; //成功的权限
    private Map<String, Integer> failMap; //失败的权限

    private int REQUEST_CODE_PERMISSION = 0x00099; //请求code


    public static final PermissionDispose init(@NonNull Object obj, @NonNull OnPermissionListener
            permissionListener) {

        return new PermissionDispose(obj, permissionListener);
    }


    private PermissionDispose(Object obj, OnPermissionListener permissionListener) {
        if (obj instanceof Activity) {
            init((Activity) obj, permissionListener);
        } else if (obj instanceof Fragment) {
            init((Fragment) obj, permissionListener);
        } else
            throw new RuntimeException("obj should is fragment or activity,but now, the obj is " +
                    "not one between them");
    }

    private void init(Activity activity, OnPermissionListener permissionListener) {
        init(activity, null, permissionListener);
    }

    private void init(Fragment fragment, OnPermissionListener permissionListener) {
        init(fragment.getActivity(), fragment, permissionListener);
    }

    private void init(Activity activity, Fragment fragment, OnPermissionListener
            permissionListener) {
        this.activity = activity;
        this.fragment = fragment;
        this.context = this.activity;
        this.permissionListener = permissionListener;
        successMap = new HashMap<>();
        failMap = new HashMap<>();
    }


    /**
     * 请求权限
     *
     * @param requestCode 请求权限的请求码
     * @param permissions 请求的权限
     */
    public void requestPermission(int requestCode, @NonNull String... permissions) {

        this.REQUEST_CODE_PERMISSION = requestCode;
        if (checkPermissions(permissions)) {
            // 如果全部都已经获取了权限直接返回成功
            for (String permission : permissions) {
                successMap.put(permission, 0);
            }
            permissionListener.onSuccess(requestCode, successMap);
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(activity, needPermissions.toArray(new
                    String[needPermissions.size()]), REQUEST_CODE_PERMISSION);
        }
    }


    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }


    /**
     * 系统请求权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // 将权限申请存入map， granted : 0:成功 -1：失败
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == -1) { //失败
                failMap.put(permissions[i], grantResults[i]);
            } else if (grantResults[i] == 0) { //成功
                successMap.put(permissions[i], grantResults[i]);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requestCode == REQUEST_CODE_PERMISSION) {
                if (verifyPermissions(grantResults)) {
                    permissionListener.onSuccess(REQUEST_CODE_PERMISSION, successMap);
                } else {
                    permissionListener.onFinal(REQUEST_CODE_PERMISSION, failMap);
                    showTipsDialog();
                }
            }
        }
    }

    /**
     * 确认所有的权限是否都已授权
     *
     * @param grantResults
     * @return
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示提示对话框
     */
    private void showTipsDialog() {
        new AlertDialog.Builder(activity)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                }).show();
    }

    /**
     * 启动当前应用设置页面
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }


    public static interface OnPermissionListener {
        void onSuccess(int requestCode, Map<String, Integer> successPermissions);

        void onFinal(int requestCode, Map<String, Integer> failPermissions);

    }
}
