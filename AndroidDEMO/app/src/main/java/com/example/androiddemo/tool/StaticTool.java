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

    // 测试用主函数
    public static void main(String[] args) {
        String a = "{[recode:A0300];{[user_name:小明],[sex:男],[age:29],[phone:15787223423],[num:4],[is_ban:0],[is_manager:1],[phone:15787223423]";
        String [] arr2 = a.split(";");
        for(String ss : arr2){
            System.out.println(ss);
            Pattern compile = Pattern.compile("\\[(\\w+?)\\:((\\w|[\u4E00-\u9FA5])+?)\\],?");
            Matcher matcher = compile.matcher(ss);
            while (matcher.find()) {
                System.out.println(matcher.group(1)+" : "+matcher.group(2));
            }
        }
    }

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

    /**
     * @author      Ghuilrei
     * @description 添加一个用户
     * @param       str
     * @return      HashMap<String, String>
     * @date        2019-07-05 10:53
     */
    public static HashMap<String, String> Regular_Expression(String str) {
        Pattern compile = Pattern.compile("\\[(\\w+?)\\:(\\w+?)\\],?");
        Matcher matcher = compile.matcher(str);
        HashMap<String, String> map = new HashMap<String, String>();
        while (matcher.find()) {
            map.put(matcher.group(1), matcher.group(2));
        }
        return map;
    }
}
