package ww.com.core.utils;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * 字符串操作工具包
 *
 * @version 1.0
 * @created 2012-3-21
 */
public class StringUtils {

    private final static ThreadLocal<DecimalFormat> priceFormat = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("###,###,##0.00");
        }
    };

    /**
     * 得到Paint 画出的文本高度.
     *
     * @param paint
     * @return
     */
    public static int getTextHeight(Paint paint) {
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int height = Math.abs(fontMetrics.bottom - fontMetrics.top);
        return height;
    }

    /**
     * @param obj 数字类型
     * @return 默认: #, ###.#
     */
    public static String formatNum(Object obj) {
        return formatNum(obj, "#,###.#");
    }

    /**
     * @param obj     数字类型
     * @param pattern #,###.#
     * @return
     */
    public static String formatNum(Object obj, String pattern) {
        try {
            DecimalFormat df = new DecimalFormat(pattern);
            return df.format(obj);
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 格式化 价格
     *
     * @param price
     * @return #, ###.##
     */
    public static String formatPrice(Double price) {
        return priceFormat.get().format(price);
    }

    /**
     * @see #formatPrice(Double)
     */
    public static String formatPrice(String price) {
        try {
            Double price_double = Double.parseDouble(price);
            return formatPrice(price_double);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "0.00";
        }
    }

    /**
     * @see #getGraphicString(String, Drawable)
     */
    public static SpannableString getGraphicString(Context context,
                                                   String content, int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        return getGraphicString(content, drawable);
    }

    /**
     * @param content  文本信息
     * @param drawable 图像信息
     * @return 图文信息(图片在文字的前面)
     */
    public static SpannableString getGraphicString(String content, Drawable drawable) {
        SpannableString spannableString =
                new SpannableString("1" + content); // 1 为占位符
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(drawable);
        spannableString.setSpan(span, 0, 1,
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    public static double toDouble(String price) {
        double d = 0.0d;
        try {
            d = Double.parseDouble(price);
        } catch (Exception e) {
        }
        return d;
    }

    /**
     * @param phone
     * @return 格式化手机号, 中间四位为* 手机号必须为11 位
     */
    public static String formatPhone(String phone) {
        if (phone.length() == 11) {
            phone = String.format("%s****%s", phone.substring(0, 3),
                    phone.substring(7));
        }
        return phone;
    }

    public static float toFloat(String input) {
        try {
            return Float.parseFloat(input);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 判断数组是否相邻
     *
     * @param string
     * @return 全部相邻 true isUpon("5,4,6,0")
     */
    public static boolean isUpon(String string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        String[] strs = string.split(",");
        if (strs.length <= 1) {
            return false;
        }
        Arrays.sort(strs);
        int[] array = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {
            array[i] = Integer.parseInt(strs[i]);
            if (i > 0 && Math.abs(array[i] - array[i - 1]) != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 复制到剪贴板管理器
     */
    @SuppressLint("NewApi")
    public static void copy(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 从剪贴板管理器粘贴
     */
    @SuppressLint("NewApi")
    public static String paste(Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }
}
