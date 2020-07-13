package com.test;

import com.service.Service;
import com.tools.SQLInjection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @description 测试函数
 * @author Ghuilrei
 * @date 2020/7/9 17:38
 * @version V1.0
 */

public class test {

    public static void main(String[] args) throws UnsupportedEncodingException {
//        System.out.println("out:"+SQLInjection.SQLInjectionTest("123 and 1 = 1--", "123123"));
        System.out.println(URLEncoder.encode("男","UTF-8"));
    }


}
