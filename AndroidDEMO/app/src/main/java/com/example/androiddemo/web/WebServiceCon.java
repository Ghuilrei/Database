package com.example.androiddemo.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebServiceCon {
    public static String executeHttpGet(String address){
        HttpURLConnection connection = null;
        InputStream in = null;

        try{
            String Url = "http://192.168.0.116:8080/ServerDEMO/" + address;
            String path = Url;
            System.out.println("111111111111");
            try {
                URL url = new URL(path);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);//建立连接超时
                connection.setReadTimeout(8000);//传递数据超时

                in = connection.getInputStream();
                return parseInfo(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //意外退出时，连接关闭保护
            if(connection != null){
                connection.disconnect();
            }
            if(in != null){
                try{
                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //得到字节输入流，将字节输入流转化为String类型
    public static String parseInfo(InputStream inputStream){
        BufferedReader reader = null;
        String line = "";
        StringBuilder response = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while((line = reader.readLine()) != null){
                response.append(line);
            }
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(reader != null){
                try{
                    reader.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
