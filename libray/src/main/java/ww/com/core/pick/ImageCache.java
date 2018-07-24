package ww.com.core.pick;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.nostra13.universalimageloader.utils.L;

import java.io.File;

import ww.com.core.utils.FileUtils;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by fighter on 2016/9/19.
 */
@Deprecated
public class ImageCache {

    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String INDIVIDUAL_DIR_NAME = "pick_temp";

    private ImageCache() {
        throw new RuntimeException();
    }

    public static File getIndividualCacheDirectory(Context context) {
        File cacheDir = getCacheDirectory(context);
        File individualCacheDir = new File(cacheDir, INDIVIDUAL_DIR_NAME);
        if (!individualCacheDir.exists()) {
            if (!individualCacheDir.mkdir()) {
                individualCacheDir = cacheDir;
            }
        }
        return individualCacheDir;
    }

    public static final void clearCache(Context context) {
        File cacheDir = getIndividualCacheDirectory(context);
        if (cacheDir != null) {
            FileUtils.deleteDir(cacheDir);
        }
    }

    /**
     *
     * @param context
     * @return -1 未获取成功
     */
    public static long getCacheFreeSize(Context context) {
        File cacheDir = getIndividualCacheDirectory(context);
        if (cacheDir != null) {
            return FileUtils.getDirSize(cacheDir);
        }
        return -1;
    }

    private static File getCacheDirectory(Context context) {
        File appCacheDir = null;
        if (MEDIA_MOUNTED
                .equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                L.w("Unable to create external cache directory");
                return null;
            }
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }
}
