package com.example.androiddemo.operate;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddemo.R;
import com.example.androiddemo.tool.Person;
import com.example.androiddemo.tool.Response;
import com.example.androiddemo.web.WebServicePost;

import java.net.URLEncoder;

import com.facebook.stetho.Stetho;

import static com.example.androiddemo.tool.StaticTool.GetMD5;

public class Login extends AppCompatActivity implements View.OnClickListener{

    Intent intent;

    /** 个人信息类 **/
    Person person;

    /** 部件 **/
    private EditText userid;
    private EditText password;

    /** 提示框 **/
    ProgressDialog dialog;

    /** 发送到服务器的数据 **/
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 数据库连接初始化
        Stetho.initializeWithDefaults(this);

        person = new Person(this);

        // 数据库中存在Session
        if (!"NULL".equals(person.getSession())) {
            // 尝试使用Session登陆
            try {
                data = "kind=session"+
                        "&user_id=" + URLEncoder.encode(person.getUser_id(),"UTF-8") +
                        "&session=" + URLEncoder.encode(person.getSession(),"UTF-8");
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
                            "&phone=" + URLEncoder.encode(userid.getText().toString(),"UTF-8") +
                            "&password=" + URLEncoder.encode(GetMD5(password.getText().toString()),"UTF-8");
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
                    e.printStackTrace();
                    // TODO Session登陆过程中
                    System.out.println("Session登陆过程中");
                }

                Response data = new Response(response);

                String recode = data.getRecode();

                // 注册成功
                if ("B0101".equals(recode) || "B0102".equals(recode)) {

                    // TODO 没发生错误：recode:
                    System.out.println("没发生错误：recode:"+recode);

                    // 往person中填入信息
                    person.setAll(data.getInformation()[0], Login.this);

                    // 传信息
                    Intent intent = new Intent(Login.this, Content.class);
                    intent.putExtra("person", person);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "登陆失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}