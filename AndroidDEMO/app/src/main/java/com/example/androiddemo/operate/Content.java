package com.example.androiddemo.operate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.R;
import com.example.androiddemo.web.WebServicePost;
import com.example.androiddemo.work.Information;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Content extends AppCompatActivity implements View.OnClickListener{

    private ImageView img;
    private TextView Sno;
    private TextView Sname;
    private Button check;
    private Button appointment;
    private Button history;
    private Button renew;
    private Button exit;
    String SessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //初始化信息
        img = findViewById(R.id.imageView8);
        Sno = findViewById(R.id.Sno);
        Sname = findViewById(R.id.Sname);
        check = findViewById(R.id.check);
        appointment = findViewById(R.id.appointment);
        history = findViewById(R.id.history);
        renew = findViewById(R.id.renew);
        exit = findViewById(R.id.exit);

        // 监听器
        img.setOnClickListener(this);
        Sno.setOnClickListener(this);
        Sname.setOnClickListener(this);
        check.setOnClickListener(this);
        appointment.setOnClickListener(this);
        history.setOnClickListener(this);
        renew.setOnClickListener(this);
        exit.setOnClickListener(this);

        // TODO 请求session判断是否登录
        // 新线程
//        new Thread(new Content.MyThread()).start();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imageView8:
            case R.id.Sno:
            case R.id.Sname:
                Intent intent = new Intent(Content.this, Information.class);
        }

        //设置子线程，分别进行Get和Post传输数据
        new Thread(new Content.MyThread()).start();
    }

    public String readInfo(){
        File file = new File(Login.FILE_PATH + "SessionID.txt");
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String result = br.readLine();
            Toast.makeText(Content.this, result, Toast.LENGTH_LONG).show();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Content.this, "读取文件失败", Toast.LENGTH_LONG).show();
            return "NULL";
        }
    }

    public class MyThread implements Runnable{
        @Override
        public void run() {
            String infoString = WebServicePost.executeHttpPost(SessionID, "ConLet");//获取服务器返回的数据

            //更新UI，使用runOnUiThread()方法
            showResponse(infoString);
        }
    }


    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            //更新UI
            @Override
            public void run() {
                Sname.setText(response);
            }
        });
    }
}
