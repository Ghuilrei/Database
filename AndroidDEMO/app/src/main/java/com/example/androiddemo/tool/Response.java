package com.example.androiddemo.tool;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Response implements Serializable {

    public HashMap[] data;

    public Response(String response) {
        String[] a = response.split(";");
        data = new HashMap[a.length];
        for (int i = 0; i < a.length; ++i) {
            data[i] = Extract(a[i]);
        }
    }

    public static HashMap<String, String> Extract(String str) {
        String cp = "\\[(\\w+?)\\:((\\w|[\u4E00-\u9FA5]|-|:|\\s)+?)\\],?";
        Pattern compile = Pattern.compile(cp);
        Matcher matcher = compile.matcher(str);
        HashMap<String, String> map = new HashMap<String, String>();
        while (matcher.find()) {
            map.put(matcher.group(1), matcher.group(2));
        }
        return map;
    }

    public HashMap<String, String>[] getInformation() {
        int begin = 1;
        int end = data.length;
        HashMap<String, String>[] a = Arrays.copyOfRange(data, begin, end);
        return a;
    }

    public static void bian(HashMap<String, String>[] info) {
        for (HashMap<String, String> map : info) {
            for(String key:map.keySet())
            {
                System.out.println("Key: "+key+" Value: "+map.get(key));
            }
        }
    }

    public String getRecode() {
        return (String) data[0].get("recode");
    }

    public static void main(String[] args) {
        String a = "{[recode:B06A0300A1000]};{[book_id:1000],[book_name:Android权威指南],[user_id:10012],[borrow_date:2020-07-14 23:23:07],[return_date:2020-08-14],[is_renew:0],[is_return:0],[real_date:null]}";
        Response response = new Response(a);
        response.bian(response.getInformation());
    }

}
