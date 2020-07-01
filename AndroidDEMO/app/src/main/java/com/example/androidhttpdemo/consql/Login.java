package com.example.androidhttpdemo.consql;

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

import com.example.androidhttpdemo.test.test;
import com.example.androidhttpdemo.web.WebServiceGet;

import com.example.androidhttpdemo.R;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText userid;
    private EditText password;
    private CheckBox ismanager;
    private Button login;
    private TextView info;
    private TextView register;
    //提示框
    ProgressDialog dialog;
    //服务器返回的数据
    private String infoString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化信息
        userid = (EditText)findViewById(R.id.regUserID);
        password = (EditText)findViewById(R.id.regPassWord);
        ismanager = (CheckBox)findViewById(R.id.isManager);
        login = (Button)findViewById(R.id.btn_reg);
        info = (TextView)findViewById(R.id.info);
        register = (TextView)findViewById(R.id.register);

        //设置按钮监听器
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_reg:
                //设置提示框
                dialog = new ProgressDialog(Login.this);
                dialog.setTitle("正在登陆");
                dialog.setMessage("请稍后");
                dialog.setCancelable(false);//设置可以通过back键取消
                dialog.show();

                //设置子线程，分别进行Get和Post传输数据
                new Thread(new MyThread()).start();

                break;
            case R.id.register:
                //跳转注册页面
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
                break;
        }
    }

    public class MyThread implements Runnable{
        @Override
        public void run() {
            System.out.println(userid.getText().toString()+' '+password.getText()+toString());
            infoString = WebServiceGet.executeHttpGet(userid.getText().toString(),password.getText().toString(),ismanager.isChecked(),"LogLet");//获取服务器返回的数据

            //更新UI，使用runOnUiThread()方法
            showResponse(infoString);
        }
    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            //更新UI
            @Override
            public void run() {
                if (response.equals("false")) {
                    Toast.makeText(Login.this, "登陆失败！", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                Intent intent = new Intent(Login.this,Content.class);
//                test.writeTxtToFile(response, "/sdcard/JavaWeb/", "SessionID");
                startActivity(intent);
            }
        });
    }
}