package com.feng.utils;
import java.security.MessageDigest;

public class MD5 {
    static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    static final char hexDigitsLower[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    /**
     * MD5加密后生成32位(小写字母+数字)字符串 同 MD5Lower() 一样
     */
    public final static String md5(String plainText) {
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");

            mdTemp.update(plainText.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigitsLower[byte0 >>> 4 & 0xf];
                str[k++] = hexDigitsLower[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 校验MD5码
     *
     * @param text 要校验的字符串
     * @param md5  md5值
     * @return 校验结果
     */
    public static boolean valid(String text, String md5) {
        return md5.equals(md5(text)) || md5.equals(md5(text).toUpperCase());
    }


}
