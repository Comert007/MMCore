package ww.com.core.utils;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by fighter on 2016/8/5.
 */
public class ImageLoderUtils {
    /**
     * 将图片保存到ImageLoader 缓存中, 减少网络开销.
     *
     * @param context
     * @param bmPath  本地图片
     * @param url     对应的网络地址
     * @return
     */
    public static String saveToImgCache(Context context, String bmPath, String url) {
        Md5FileNameGenerator md5FileNameGenerator = new Md5FileNameGenerator();
        File f1 = new File(bmPath);
        File file2 = new File(StorageUtils.getIndividualCacheDirectory(context),
                md5FileNameGenerator.generate(url)
        );
        try {
            FileUtils.forJava(f1, file2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (file2.exists())
            return file2.getAbsolutePath();
        else {
            return null;
        }
    }
}
