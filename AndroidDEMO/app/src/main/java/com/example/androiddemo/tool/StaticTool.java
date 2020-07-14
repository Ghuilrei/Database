package com.example.androiddemo.tool;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version     V1.0
 * @author      Ghuilrei
 * @description 静态类合集
 * @date        2019-07-05 10:53
 **/

public class StaticTool {

    /**
     * @author      Ghuilrei
     * @description 添加一个用户
     * @param       str
     * @return      String
     * @date        2019-07-05 10:53
     */
    public static String GetMD5(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest  = md5.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }
}
