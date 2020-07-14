package com.example.androiddemo.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.R;
import com.example.androiddemo.operate.Content;
import com.example.androiddemo.operate.Login;
import com.example.androiddemo.tool.Person;
import com.example.androiddemo.tool.Response;
import com.example.androiddemo.web.WebServicePost;

public class AddBook extends AppCompatActivity implements View.OnClickListener{

    // 桌面内容
    private EditText editText_category_id;

    // 用户信息类
    Person person;

    String data = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_addbook);

        // 接收用户数据
        person = (Person)getIntent().getSerializableExtra("person");

        // 初始化
        editText_category_id = findViewById(R.id.et_category_id);

    }

    @Override
    public void onClick(View v) {
        // 要发送的数据
        data = "user_id="+person.getUser_id()+"&session="+person.getSession()+"operate=up&category_id="+editText_category_id.getText().toString();

        // 新建连接线程
        new Thread(new AddBook.MyThread()).start();

    }

    public class MyThread implements Runnable{
        // 服务器返回的数据
        String infoString;

        @Override
        public void run() {
            //获取服务器返回的数据
            infoString = WebServicePost.executeHttpPost(data, "Book");
            //更新UI，使用runOnUiThread()方法
            showResponse(infoString);
        }
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            //更新UI
            @Override
            public void run() {

                // 返回数据处理类
                Response data = new Response(response);

                // 返回码
                String recode = data.getRecode();

                // 注册成功
                if ("B10A0300A0500A1400".equals(recode)) {
                    Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "登陆失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
