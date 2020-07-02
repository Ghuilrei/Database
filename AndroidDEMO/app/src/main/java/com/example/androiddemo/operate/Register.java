package com.example.androiddemo.operate;


import android.os.Bundle;

import com.example.androiddemo.R;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.androiddemo.web.WebServicePost;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Register extends AppCompatActivity implements View.OnClickListener{

    // 部件
    private EditText regSno;
    private EditText regSname;
    private EditText regPassWord;
    private EditText regPassWord2;

    // 提示框
    ProgressDialog dialog;

    // 发送到服务器的数据
    private String data;

    // 检查两次密码是否一致
    protected boolean onCheck() {
        return regPassWord.getText().toString().equals(regPassWord2.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        //修改标题栏title
//        ActionBar ac = getSupportActionBar();
//        ac.setTitle("注册");

        // 部件
        Button btn_reg;

        //初始化
        regSno = findViewById(R.id.regUserID);
        regSname = findViewById(R.id.regUserName);
        regPassWord = findViewById(R.id.regPassWord);
        regPassWord2 = findViewById(R.id.regPassWord2);
        btn_reg = findViewById(R.id.btn_reg);

        // 注册监听器
        btn_reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // 检查两次密码是否一致
        if (onCheck()) {
            dialog = new ProgressDialog(Register.this);
            dialog.setTitle("正在注册");
            dialog.setMessage("请稍后");
            dialog.show();
            // 制作请求信息
            try {
                data = "Sno=" + URLEncoder.encode(regSno.getText().toString(),"UTF-8") +
                        "&Sname=" + URLEncoder.encode(regSname.getText().toString(),"UTF-8") +
                        "&Spassword=" + URLEncoder.encode(regPassWord.getText().toString(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //启动线程
            new Thread(new RegThread()).start();

        } else {
            // 弹窗提醒
            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
            builder.setTitle("错误");
            builder.setMessage("两次密码输入不一致");
            builder.setPositiveButton("是", null);
            builder.show();
        }
    }

    public class RegThread implements Runnable{
        @Override
        public void run() {
            //获取服务器返回数据
            String RegRet = WebServicePost.executeHttpPost(data,"RegLet");
            if (RegRet != null) {
                //更新UI，界面处理
                showReq(RegRet);
            }
        }
    }
    private void showReq(final String RegRet){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(RegRet.equals("true")){
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setTitle("注册信息");
                    builder.setMessage("注册成功");
                    builder.setCancelable(false);
                    // 跳转到登陆界面
                    builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Register.this,Login.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }else{
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setTitle("注册信息");
                    builder.setMessage("注册失败");
                    builder.setCancelable(false);
//                    builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent intent = new Intent(Register.this,Register.class);
//                            startActivity(intent);
//                        }
//                    });
                    builder.show();
                }
            }
        });
    }
}