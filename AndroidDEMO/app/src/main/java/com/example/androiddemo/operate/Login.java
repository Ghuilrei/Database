package com.example.androiddemo.operate;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddemo.R;
import com.example.androiddemo.tool.Person;
import com.example.androiddemo.web.WebServicePost;

import java.net.URLEncoder;

import com.facebook.stetho.Stetho;

import static com.example.androiddemo.tool.Error.handleError;
import static com.example.androiddemo.tool.StaticTool.GetMD5;

public class Login extends AppCompatActivity implements View.OnClickListener{

    Intent intent;

    /** 个人信息类 **/
    Person person;

    /** 部件 **/
    private EditText userid;
    private EditText password;
    private CheckBox ismanager;

    /** 提示框 **/
    ProgressDialog dialog;

    /** 发送到服务器的数据 **/
    private String data;

    /** 登陆返回值 **/
    private static String error = "Error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 数据库连接初始化
        Stetho.initializeWithDefaults(this);

        person = new Person(this);

        // 数据库中存在Session
        if (!"NULL".equals(person.getUserId())) {
            // 尝试使用Session登陆
            try {
                data = "kind=session"+
                        "&userid=" + URLEncoder.encode(person.getUserId(),"UTF-8") +
                        "&session=" + URLEncoder.encode(person.getSession(),"UTF-8") +
                        "&ismanager=" + URLEncoder.encode(person.getIsManager(), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // TODO Session 登陆 发送数据：
            System.out.println(" Session 登陆 发送数据：" + data);

            // 设置子线程，登陆
            new Thread(new MyThread()).start();
        }

        // TODO Session 登录失败
        System.out.println("Session 登录失败");

        // 不存在或登陆失败
        setContentView(R.layout.activity_login);

        // 部件
        Button login;
        TextView register;

        //初始化信息
        userid = findViewById(R.id.regUserID);
        password = findViewById(R.id.regPassWord);
        ismanager = findViewById(R.id.isManager);
        login = findViewById(R.id.btn_reg);
        register = findViewById(R.id.register);

        //设置按钮监听器
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            // 登陆按钮
            case R.id.btn_reg:
                // 设置提示框
                dialog = new ProgressDialog(Login.this);
                dialog.setTitle("正在登陆");
                dialog.setMessage("请稍后");
                //设置可以通过back键取消
                dialog.setCancelable(false);
                dialog.show();

                // 设置发送的数据
                try {
                    data = "kind=password"+
                            "&userid=" + URLEncoder.encode(userid.getText().toString(),"UTF-8") +
                            "&password=" + URLEncoder.encode(GetMD5(password.getText().toString()),"UTF-8") +
                            "&ismanager=" + URLEncoder.encode(ismanager.isChecked() ? "true" : "false", "UTF-8");
                    // TODO Password 登陆 发送数据：
                    System.out.println(" Password 登陆 发送数据：" + data);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 设置子线程，登陆
                new Thread(new MyThread()).start();
                break;

            // 注册按钮
            case R.id.register:
                //跳转注册页面
                intent = new Intent(Login.this,Register.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    public class MyThread implements Runnable{
        // 服务器返回的数据
        String infoString;

        @Override
        public void run() {
            //获取服务器返回的数据
            infoString = WebServicePost.executeHttpPost(data, "LogLet");
            //更新UI，使用runOnUiThread()方法
            showResponse(infoString);
        }
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            //更新UI
            @Override
            public void run() {

                try {
                    dialog.dismiss();
                } catch (NullPointerException e) {
                    // TODO Session登陆过程中
                    System.out.println("Session登陆过程中");
                }

                // TODO response:
                System.out.println("response:"+response);

                // 处理错误
                String re = handleError(Login.this, response);
                // 没发生错误
                if (!error.equals(re)) {

                    // TODO 没发生错误：re:
                    System.out.println("没发生错误：re:"+re);

                    Intent intent = new Intent(Login.this, Content.class);
                    // TODO response格式要求："{[session:666666],[userid:666666],[name:admin],[ismanager:true]}"
                    person.setAll(response, Login.this);
                    intent.putExtra("person",person);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }
}