package ww.com.core.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebViewDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @fileName FileUtils.java
 * @description 文件工具类
 */
public class FileUtils {

    /*** 检查 **/
    /**
     * 判断SD是否可以
     *
     * @return
     */
    public static boolean isSdcardExist() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }

        return false;
    }

    /**
     * 获取程序外部的缓存目录
     *
     * @param context
     * @return
     */
    public static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }

    /**
     * 获取文件的Uri
     *
     * @param path 文件的路径
     * @return
     */
    public static Uri getUriFromFile(String path) {
        File file = new File(path);
        return Uri.fromFile(file);
    }

    /********** 添加 ************/

    /**
     * 创建根目录
     *
     * @param path 目录路径(创建失败为 NULL)
     */
    public static File createDir(String path) {
        File dir = new File(path);
        if (createDir(dir)) {
            return dir;
        } else {
            return null;
        }
    }

    /**
     * 创建目录
     *
     * @param dir
     * @return
     */
    public static boolean createDir(File dir) {
        if (dir.exists()) {
            if (dir.isFile())
                dir.delete();
        }
        if (!dir.exists()) {
            return dir.mkdirs();
        } else {
            return true;
        }
    }

    /**
     * 创建文件
     *
     * @param filePath 文件路径
     * @return 创建的文件
     * 创建失败为null
     */
    public static File createNewFile(String filePath) {
        File file = new File(filePath);

        if (createNewFile(file)) {
            return file;
        } else {
            return null;
        }
    }

    public static boolean createNewFile(File file) {
        File dir = file.getParentFile();
        if (createDir(dir)) {
            if (!file.exists()) {
                try {
                    return file.createNewFile();
                } catch (IOException e) {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /********** 修改 ************/
    /**
     * forJava(复制文件)
     * 复制 文件f1 到 f2
     *
     * @param f1
     * @param f2
     * @return void
     * @throws Exception
     * @Title: forJava
     * @Description: TODO
     */
    public static boolean forJava(File f1, File f2) throws Exception {
        if (!f1.exists()) {
            return false;
        }
        if (createNewFile(f2)) {
            int length = 524288;
            FileInputStream in = new FileInputStream(f1);
            FileOutputStream out = new FileOutputStream(f2);
            byte[] buffer = new byte[length];
            while (true) {
                int ins = in.read(buffer);
                if (ins < 0) {
                    in.close();
                    out.flush();
                    out.close();
                    break;
                } else {
                    out.write(buffer, 0, ins);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    /********** 删除 ************/

    /**
     * 删除应用缓存（/data/data/包名/databases或者webview）<br/>
     *
     * @param context
     * @return
     * @author fighter <br />
     * 创建时间:2013-8-15<br />
     * 修改时间:<br />
     */
    public static void deleteCache(Context context) {
        // 清除 webview中 的缓存用户名和密码
        try {
            WebViewDatabase database = WebViewDatabase.getInstance(context);
            database.clearUsernamePassword();
            database.clearHttpAuthUsernamePassword();
            database.clearFormData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获取/data/data/包名/cache目录
        File cFile = context.getCacheDir();
        // 得到 /data/data/包名/目录
        File cFilePrant = cFile.getParentFile();
        File fileData = new File(cFilePrant, "databases");
        if (fileData.exists() && fileData.isDirectory()) {
            // System.out.println(fileData.getPath());
            File[] files = fileData.listFiles();
            if (files != null) {
                for (File file : files) {
                    String name = file.getName();
                    if (name != null && name.startsWith("webview")) {
                        file.delete();
                    }
                }
            }
        }
        deleteDir(cFile);
    }

    public static void deleteDir(String path) {
        deleteDir(new File(path));
    }

    /**
     * 删除指定目录下的所有文件.
     *
     * @param dir 作者:fighter <br />
     *            创建时间:2013-4-25<br />
     *            修改时间:<br />
     */
    public static void deleteDir(File dir) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files == null) {
                dir.delete();
                return;
            }
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteDir(f);
                } else if (f.isFile()) {
                    f.delete();
                }
            }

//            if (dir.exists() && dir.isDirectory()) {
//                dir.delete();
//            }
        }
    }

    /**
     * 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     *
     * @param context
     */
    public static boolean write(Context context, String fileName, String content) {
        if (content == null)
            content = "";

        try {
            FileOutputStream fos = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 读取文本文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String read(Context context, String fileName) {
        try {
            FileInputStream in = context.openFileInput(fileName);
            return readInStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String readInStream(FileInputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }

            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            Log.i("FileTest", e.getMessage());
        }
        return null;
    }

    /**
     * 向手机写图片
     *
     * @param buffer
     * @param folder
     * @param fileName
     * @return
     */
    public static boolean writeFile(byte[] buffer, String folder,
                                    String fileName) {
        boolean writeSucc = false;

        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

        String folderPath = "";
        if (sdCardExist) {
            folderPath = Environment.getExternalStorageDirectory()
                    + File.separator + folder + File.separator;
        } else {
            writeSucc = false;
        }

        File fileDir = new File(folderPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File file = new File(folderPath + fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(buffer);
            writeSucc = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return writeSucc;
    }

    /**
     * 根据文件绝对路径获取文件名
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return "";
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * 根据文件的绝对路径获取文件名但不包含扩展名
     *
     * @param filePath
     * @return
     */
    public static String getFileNameNoFormat(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        int point = filePath.lastIndexOf('.');
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1,
                point);
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileExtension(String fileName) {
        if (TextUtils.isEmpty(fileName))
            return "";

        int point = fileName.lastIndexOf('.');
        return fileName.substring(point + 1);
    }

    /**
     * 获取文件大小
     *
     * @param filePath
     * @return
     */
    public static long getFileSize(String filePath) {
        long size = 0;

        File file = new File(filePath);
        if (file != null && file.exists()) {
            size = file.length();
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 获取目录文件个数
     *
     * @param dir
     * @return
     */
    public long getFileList(File dir) {
        long count = 0;
        File[] files = dir.listFiles();
        count = files.length;
        for (File file : files) {
            if (file.isDirectory()) {
                count = count + getFileList(file);// 递归
                count--;
            }
        }
        return count;
    }

    /**
     * 流转换为二进制
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] toBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            out.write(ch);
        }
        byte buffer[] = out.toByteArray();
        out.close();
        return buffer;
    }

    /**
     * 计算SD卡的剩余空间
     *
     * @return 返回-1，说明没有安装sd卡
     */
    public static long getFreeDiskSpace() {
        long freeSpace = 0;
        if (isSdcardExist()) {
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = 0;
                long availableBlocks = 0;
                if (Build.VERSION.SDK_INT >= 18) {
                    blockSize = stat.getBlockSizeLong();
                    availableBlocks = stat.getAvailableBlocksLong();
                } else {
                    blockSize = stat.getBlockSize();
                    availableBlocks = stat.getAvailableBlocks();
                }

                freeSpace = availableBlocks * blockSize;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return -1;
        }
        return (freeSpace);
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return true 成功
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 重命名
     *
     * @param oldFile
     * @param newFile
     * @return
     */
    public static boolean reNameFile(String oldFile, String newFile) {
        File f = new File(oldFile);
        return f.renameTo(new File(newFile));
    }

    /**
     * 获取SD卡的根目录，末尾带\
     *
     * @return
     */
    public static String getSDRoot() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 列出root目录下所有子目录
     *
     * @param root
     * @return 绝对路径
     */
    public static List<String> listPath(String root) {
        List<String> allDir = new ArrayList<String>();
        SecurityManager checker = new SecurityManager();
        File path = new File(root);
        checker.checkRead(root);
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                if (f.isDirectory()) {
                    allDir.add(f.getAbsolutePath());
                }
            }
        }
        return allDir;
    }

    /**
     * 更新指定路径的文件到媒体库中
     *
     * @param context
     * @param paths
     */
    public static void scanFile(Context context, String... paths) {
        try {
            MediaScannerConnection.scanFile(context, paths, null, null);
        } catch (Exception e) {
        }

    }

}
