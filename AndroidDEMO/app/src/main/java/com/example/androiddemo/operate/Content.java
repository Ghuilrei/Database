package com.example.androiddemo.operate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.R;
import com.example.androiddemo.tool.Person;
import com.example.androiddemo.web.WebServicePost;
import com.example.androiddemo.work.Information;

public class Content extends AppCompatActivity implements View.OnClickListener{

    // 个人信息类
    Person person = new Person();

    private ImageView img;
    private TextView Sno;
    private TextView Sname;
    private Button check;
    private Button appointment;
    private Button history;
    private Button renew;
    private Button exit;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        if (person.getSession().equals("NULL")) {
            // 登陆信息失效：跳转到登陆界面
            Intent intent = new Intent(Content.this, Login.class);
            startActivityForResult(intent, 1);
        } else {
//            // 新线程 尝试登陆
//            data = "";
//            new Thread(new Content.MyThread()).start();
        }


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

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            // 点击头像、姓名、学号 跳转信息界面
            case R.id.imageView8:
            case R.id.Sno:
            case R.id.Sname:
                Intent intent = new Intent(Content.this, Information.class);
                intent.putExtra("Person", person);
                startActivity(intent);
                break;

        }
    }

    public class MyThread implements Runnable{
        @Override
        public void run() {
            //获取服务器返回的数据
            String infoString = WebServicePost.executeHttpPost(data, "ConLet");

            //数据处理，使用runOnUiThread()方法
            showResponse(infoString);
        }
    }


    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            //更新UI
            @Override
            public void run() {
                if (response.equals("false")) {
                    Intent intent = new Intent(Content.this, Login.class);
                    startActivity(intent);
                } else {
//                    Sname.setText(response);
//                    Sno.setText(response);
                }
            }
        });
    }
}
