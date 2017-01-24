package ww.com.core;

import ww.com.core.log.Logger;

public class Debug {
    private static boolean isDebug = true;

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    public static void setTag(String tag) {
        Logger.init(tag);
    }

    public static void v(String msg) {
        if (isDebug)
            Logger.v(msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Logger.d(msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Logger.e(msg);
    }

    public static void i(String tag, String msg) {
        if (isDebug)
            Logger.i(msg);
    }


    public static void w(String msg) {
        if (isDebug)
            Logger.w(msg);
    }

    public static void json(String json) {
        if (isDebug)
            Logger.json(json);
    }

}
