package com.example.androiddemo.tool;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Response {

    public HashMap[] data;

    public Response(String response) {
        String[] a = response.split(";");
        data = new HashMap[a.length];
        for (int i = 0; i < a.length; ++i) {
            data[i] = Extract(a[i]);
        }
    }

    public static HashMap<String, String> Extract(String str) {
        String cp = "\\[(\\w+?)\\:((\\w|[\u4E00-\u9FA5])+?)\\],?";
        Pattern compile = Pattern.compile(cp);
        Matcher matcher = compile.matcher(str);
        HashMap<String, String> map = new HashMap<String, String>();
        while (matcher.find()) {
            map.put(matcher.group(1), matcher.group(2));
        }
        return map;
    }

    public HashMap<String, String>[] getInformation() {
        HashMap<String, String>[] a = Arrays.copyOfRange(data, 1, data.length);
        return a;
    }

    public String getRecode() {
        return (String) data[0].get("recode");
    }

}
