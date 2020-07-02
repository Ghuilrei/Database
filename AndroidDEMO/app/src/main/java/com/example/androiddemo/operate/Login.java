package com.example.androiddemo.operate;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddemo.R;
import com.example.androiddemo.test.test;
import com.example.androiddemo.web.WebServicePost;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Login extends AppCompatActivity implements View.OnClickListener{

    // 部件
    private EditText userid;
    private EditText password;
    private CheckBox ismanager;

    // 文件路径
    static String filename = "/external_sd/JavaWeb/";
    static final String FILE_PATH = Environment.getExternalStorageDirectory().getPath()+ File.separator+filename;

    // 提示框
    ProgressDialog dialog;

    // 发送到服务器的数据
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    data = "Sno=" + URLEncoder.encode(userid.getText().toString(),"UTF-8") +
                            "&Spassword=" + URLEncoder.encode(password.getText().toString(),"UTF-8") +
                            "&ismanager=" + ismanager.isChecked();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // 设置子线程，分别进行Get和Post传输数据
                new Thread(new MyThread()).start();
                break;

            // 注册按钮
            case R.id.register:
                //跳转注册页面
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
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
                dialog.dismiss();
                if (response.equals("false")) {
                    Toast.makeText(Login.this, "登陆失败！", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Login.this, Content.class);
                    try {

                        // FIXME System.out.print
                        System.out.println("Login:121:filepath:" + FILE_PATH + "SessionID.txt");

                        test.writeTxtToFile(response, FILE_PATH, "SessionID.txt");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }

            }
        });
    }
}