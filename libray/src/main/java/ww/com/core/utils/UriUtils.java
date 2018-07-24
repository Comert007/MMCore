package ww.com.core.utils;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;

import java.io.File;


/**
 * @author feng
 * @Date 2018/7/24.
 */
public final class UriUtils {

    private UriUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * @deprecated Use {@link UriUtils#file2Uri(File)} instead.
     */
    @Deprecated
    public static Uri getUriForFile(final File file) {
        return file2Uri(file);
    }

    /**
     * File to uri.
     *
     * @param file The file.
     * @return uri
     */
    public static Uri file2Uri(final File file) {
        if (file == null) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = Utils.getApp().getPackageName() + ".utilcode.provider";
            return FileProvider.getUriForFile(Utils.getApp(), authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * Uri to file.
     *
     * @param uri        The uri.
     * @param columnName The name of the target column.
     * @return file
     */
    public static File uri2File(final Uri uri, final String columnName) {
        if (uri == null) return null;
        CursorLoader cl = new CursorLoader(Utils.getApp());
        cl.setUri(uri);
        cl.setProjection(new String[]{columnName});
        Cursor cursor = cl.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        cursor.moveToFirst();
        return new File(cursor.getString(columnIndex));
    }
}