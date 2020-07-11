package com.example.androiddemo.web;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @description 使用Post方法获取Http服务器数据
 * @author Ghuilrei
 * @date 2020/7/9 17:38
 * @version V1.0
 */

public class WebServicePost {

    public static String executeHttpPost(String postData, String catalogue){
        HttpURLConnection connection = null;
        InputStream in = null;

        try{
            String address = "http://192.168.0.116:8080/ServerDEMO/" + catalogue;
            try {
                URL url = new URL(address);
                connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                //传递数据超时
                connection.setReadTimeout(8000);
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                connection.connect();
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(postData);
                out.flush();
                out.close();
                int resultCode = connection.getResponseCode();
                if(HttpURLConnection.HTTP_OK == resultCode) {
                    in = connection.getInputStream();
                    return parseInfo(in);
                }
                return null;
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

    /**
     * @description 得到字节输入流，将字节输入流转化为String类型
     * @param inputStream 字节输入流
     * @return String
     */
    public static String parseInfo(InputStream inputStream){
        BufferedReader reader = null;
        String line;
        StringBuilder response = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while((line = reader.readLine()) != null){
                Log.d("RegisterActivity",line);
                response.append(line);
            }
            Log.d("RegisterActivity","response.toString():"+response.toString());
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