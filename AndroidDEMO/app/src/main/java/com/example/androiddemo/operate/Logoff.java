package com.example.androiddemo.operate;
import android.content.Context;
import android.widget.Toast;

import com.example.androiddemo.tool.Person;
import com.example.androiddemo.tool.Response;
import com.example.androiddemo.web.WebServicePost;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Logoff {

    Person person;

    Context context;

    String data;

    public Logoff (Person person, Context context) {
        this.person = person;
        this.context = context;
        try {
            data = "user_id=" + URLEncoder.encode(person.getUser_id(),"UTF-8") +
                    "&session=" + URLEncoder.encode(person.getSession(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        person.delAll(context);
        // 设置子线程，登陆
        new Thread(new Logoff.MyThread()).start();
    }

    public class MyThread implements Runnable{

        @Override
        public void run() {
            WebServicePost.executeHttpPost(data, "LogOffLet");
        }
    }
}
