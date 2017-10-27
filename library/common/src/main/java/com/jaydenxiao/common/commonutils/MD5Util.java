package com.jaydenxiao.common.commonutils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * MD5加密
 * </p>
 * Created by Administrator on 2017/10/13 0013.
 */
public class MD5Util {

    /**
     * MD5加密
     *
     * @param text    待加密文本
     * @param bit     加密位数，16或32
     * @param isUpper 是否是大写
     * @return 加密后文本，如果加密失败，返回null
     */
    public static String getMD5String(String text, int bit, boolean isUpper) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(text.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assert messageDigest != null;
        byte[] byteArray = messageDigest.digest();
        StringBuilder md5StrBuff = new StringBuilder();
        for (byte aByteArray : byteArray) {
            if (Integer.toHexString(0xFF & aByteArray).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & aByteArray));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & aByteArray));
        }
        String result;
        //16位加密，从第9位到25位
        if (bit == 16) {
            result = md5StrBuff.substring(8, 24);
        } else {
            result = md5StrBuff.toString();
        }
        if (isUpper) {
            result = result.toUpperCase();
        } else {
            result = result.toLowerCase();
        }
        return result;
    }
}
