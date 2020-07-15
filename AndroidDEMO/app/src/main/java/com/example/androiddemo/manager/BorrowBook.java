package com.example.androiddemo.manager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.R;
import com.example.androiddemo.tool.Person;
import com.example.androiddemo.tool.Response;
import com.example.androiddemo.web.WebServicePost;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BorrowBook extends AppCompatActivity implements View.OnClickListener {

    private EditText et_book_id;
    private EditText et_borrow_id;
    private Button bt_com;

    // 用户信息类
    Person person;

    String data = "";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borebook);

        // 接收用户数据
        person = (Person) getIntent().getSerializableExtra("Person");

        //初始化
        et_book_id = findViewById(R.id.et_book_id);
        et_borrow_id = findViewById(R.id.et_borrow_id);
        bt_com = findViewById(R.id.bt_com);

        bt_com.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.bt_com){
            try {
                data = "user_id="+ URLEncoder.encode(person.getUser_id(),"UTF-8")+
                        "&session="+URLEncoder.encode(person.getSession(),"UTF-8")+
                        "&book_id="+URLEncoder.encode(et_book_id.getText().toString(),"UTF-8")+
                        "&borrow_id="+URLEncoder.encode(et_borrow_id.getText().toString(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // 新建连接线程
            new Thread(new BorrowBook.MyThread()).start();
        }
    }

    public class MyThread implements Runnable {
        // 服务器返回的数据
        String infoString;

        @Override
        public void run() {
            //获取服务器返回的数据
            infoString = WebServicePost.executeHttpPost(data, "BorrowBook");
            //更新UI，使用runOnUiThread()方法
            showResponse(infoString);
        }

        private void showResponse(final String response) {
            runOnUiThread(new Runnable() {
                //更新UI
                @Override
                public void run() {

                    // 返回数据处理类
                    Response data = new Response(response);

                    // 返回码
                    String recode = data.getRecode();

                    // 注册成功
                    if ("B11A0300A0500A1600".equals(recode)) {
                        Toast.makeText(getApplicationContext(), "借书成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "借书失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}