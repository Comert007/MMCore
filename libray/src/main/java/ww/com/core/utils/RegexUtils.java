package ww.com.core.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * 正则表达式匹配
 * Created by fighter on 2016/8/5.
 */
public class RegexUtils {

    private RegexUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 验证简单手机
     */
    private static final String REGEX_PHONE = "^(1)\\d{10}$";
    /**
     * 验证邮箱
     */
    private static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 验证URL
     */
    private static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";

    /**
     * 验证汉字
     */
    private static final String REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$";
    /**
     * 验证用户名,取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
     */
    private static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";

    /**
     * 验证IP地址
     */
    private static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

    /**
     * 验证身份证
     */
    private static final String REGEX_CD_CARD = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";

    /**
     * @param string 待验证文本
     * @return 是否符合手机号（简单）格式
     */
    public static boolean validateMobileSimple(String string) {
        return validateMatch(REGEX_PHONE, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合邮箱格式
     */
    public static boolean validateEmail(String string) {
        return validateMatch(REGEX_EMAIL, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合网址格式
     */
    public static boolean validateURL(String string) {
        return validateMatch(REGEX_URL, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合汉字
     */
    public static boolean validateChz(String string) {
        return validateMatch(REGEX_CHZ, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合用户名(取值范围为a-z, A-Z, 0-9,"_", 汉字，不能以"_"结尾, 用户名必须是6-20位)
     */
    public static boolean validateUsername(String string) {
        return validateMatch(REGEX_USERNAME, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合IPV4格式
     */
    public static boolean validateIpV4(String string) {
        return validateMatch(REGEX_IP, string);
    }


    /**
     * @param string 待验证文本
     * @return 是否符合 身份证号码格式
     */
    public static boolean validateCardNo(String string) {
        return validateMatch(REGEX_CD_CARD, string);
    }

    /**
     * @param regex  正则表达式字符串
     * @param string 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    public static boolean validateMatch(String regex, String string) {
        return !TextUtils.isEmpty(string) && Pattern.matches(regex, string);
    }

}
