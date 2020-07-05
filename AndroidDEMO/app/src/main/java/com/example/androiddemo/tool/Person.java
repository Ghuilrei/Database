package com.example.androiddemo.tool;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

public class Person implements Serializable {

    // 文件路径
    static String filename = "/external_sd/JavaWeb/";
    static final String FILE_PATH = Environment.getExternalStorageDirectory().getPath()+ File.separator+filename;

    // Session
    private String session;
    // 姓名
    private String Sname;
    // 学号
    private String Sno;

    public Person() {
        setSession("NULL");
        setSno("NULL");
        setSname("NULL");

        File file = new File(FILE_PATH + "SessionID.txt");
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String result = br.readLine();
//            Toast.makeText(Content.this, result, Toast.LENGTH_LONG).show();
            setSession(result);
        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(Content.this, "登陆信息失效，请重新登陆。", Toast.LENGTH_LONG).show();
        }
    }

    public void setALL (String request) {
        // request格式要求：Sname=`admin`&Sno=`66666`

    }

    public void setSession (String session) {
        this.session = session;
        try {
            setSession(session);
            // FIXME System.out.print
            System.out.println("Login:121:filepath:" + getFilePath() + "SessionID.txt");

            Operatefile.writeTxtToFile(session, getFilePath(), "SessionID.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSno (String Sno) {
        this.Sno = Sno;
    }

    public void setSname(String sname) {
        Sname = sname;
    }

    public String getSession () {
        return session;
    }

    public String getSno () {
        return Sno;
    }

    public String getSname() {
        return Sname;
    }

    public static String getFilePath() {
        return FILE_PATH;
    }
}