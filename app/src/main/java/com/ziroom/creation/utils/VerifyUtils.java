package com.ziroom.creation.utils;

import android.text.TextUtils;

/**
 * 验证工具
 * Created by lmnrenbc on 2017/11/26.
 */

public class VerifyUtils {

    private static VerifyUtils instance;

    private VerifyUtils() {
    }

    public static VerifyUtils getInstance() {
        if (instance == null) {
            synchronized (VerifyUtils.class) {
                instance = new VerifyUtils();
            }
        }
        return instance;
    }

    /**
     * 是否为网络文件路径
     *
     * @param s
     * @return
     */
    public boolean fromNet(String s) {
        if (TextUtils.isEmpty(s)) {
            return false;
        }
        return s.contains("http://") || s.contains("https://");
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public boolean equalsIgnoreCase(String a, String b) {
        return (a == b) || (b != null) && (a.length() == b.length()) && a.regionMatches(true, 0, b, 0, b.length());
    }

}
