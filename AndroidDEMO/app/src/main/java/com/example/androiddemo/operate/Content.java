package com.example.androiddemo.operate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.R;
import com.example.androiddemo.tool.Person;
import com.example.androiddemo.web.WebServicePost;
import com.example.androiddemo.work.History;
//import com.example.androiddemo.work.Information;

public class Content extends AppCompatActivity implements View.OnClickListener{

//    // 个人信息类
//    Person person = new Person();
//
//    // 意图
//    Intent intent;
//
//    // POST 发送的数据
//    String data;
//
//    private ImageView img;
//    private TextView Sno;
//    private TextView Sname;
//    private Button check;
//    private Button appointment;
//    private Button history;
//    private Button renew;
//    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

//
//        if (person.getSession().equals("NULL")) {
//            // 登陆信息失效：跳转到登陆界面
//            Intent intent = new Intent(Content.this, Login.class);
//            startActivityForResult(intent, 0);
//        } else {
//            // 新线程 尝试登陆
//            data = "Session";
//            new Thread(new Content.MyThread()).start();
//        }
//
//
//        //初始化信息
//        img = findViewById(R.id.imageView8);
//        Sno = findViewById(R.id.Sno);
//        Sname = findViewById(R.id.Sname);
//        check = findViewById(R.id.check);
//        appointment = findViewById(R.id.appointment);
//        history = findViewById(R.id.history);
//        renew = findViewById(R.id.renew);
//        exit = findViewById(R.id.exit);
//
//        // 监听器
//        img.setOnClickListener(this);
//        Sno.setOnClickListener(this);
//        Sname.setOnClickListener(this);
//        check.setOnClickListener(this);
//        appointment.setOnClickListener(this);
//        history.setOnClickListener(this);
//        renew.setOnClickListener(this);
//        exit.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 0) {
//            if (resultCode == 1) {
//                // 显示姓名、学号
//                Sname.setText(person.getSname());
//                Sno.setText(person.getSno());
//            }
//        }
    }

    @Override
    public void onClick(View view) {
//
//        switch (view.getId()) {
//            // 点击头像、姓名、学号 跳转信息界面
//            case R.id.imageView8:
//            case R.id.Sno:
//            case R.id.Sname:
//                intent = new Intent(Content.this, Information.class);
//                intent.putExtra("Person", person);
//                startActivity(intent);
//                break;
//            // 历史记录
//            case R.id.history:
//                intent = new Intent(Content.this, History.class);
//                intent.putExtra("Person", person);
//                startActivity(intent);
//                break;
//        }
    }
//
//    public class MyThread implements Runnable{
//        @Override
//        public void run() {
//            //获取服务器返回的数据
//            String infoString = WebServicePost.executeHttpPost(data, "ConLet");
//
//            //数据处理，使用runOnUiThread()方法
//            showResponse(infoString);
//        }
//    }
//
//
//    private void showResponse(final String response){
//        runOnUiThread(new Runnable() {
//            //更新UI
//            @Override
//            public void run() {
//                if (response.equals("false")) {
//                    Intent intent = new Intent(Content.this, Login.class);
//                    startActivity(intent);
//                } else {
//                    // TODO response格式要求："{[Session:666666],[Sno:666666],[Sname:admin]}"
//                    // 接受并存入数据
//                    person.setALL(response);
//                    // 显示姓名、学号
//                    Sname.setText(person.getSname());
//                    Sno.setText(person.getSno());
//                }
//            }
//        });
//    }
}
