package com.example.androiddemo.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.R;
import com.example.androiddemo.operate.Content;
import com.example.androiddemo.tool.Person;
import com.example.androiddemo.tool.Response;
import com.example.androiddemo.web.WebServicePost;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class CheckBook extends AppCompatActivity implements View.OnClickListener{

    // 发送到数据
    String data;

    // 接收到信息
    HashMap<String, String>[] info;

    // 个人信息
    Person person;

    // 桌面部件
    private EditText editText_book_id;
    private EditText editText_category_name;
    private EditText editText_book_name;
    private EditText editText_author;
    private EditText editText_press;
    private EditText editText_pulic_date;
    private Button button_check_book;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbook);

        // 获取个人信息
        person = (Person)getIntent().getSerializableExtra("Person");

        // 添加桌面内容
        editText_book_id = findViewById(R.id.eT_book_id);
        editText_category_name = findViewById(R.id.eT_category_name);
        editText_book_name = findViewById(R.id.eT_book_name);
        editText_author = findViewById(R.id.eT_author);
        editText_press = findViewById(R.id.eT_press);
        editText_pulic_date = findViewById(R.id.eT_public_date);
        button_check_book = findViewById(R.id.bT_check_book);

        // 添加监听器
        button_check_book.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        try {
            data = "user_id="+ URLEncoder.encode(person.getUser_id(), "UTF-8") +
                    "&session="+ URLEncoder.encode(person.getSession(), "UTF-8") +
                    "&book_id="+ URLEncoder.encode(editText_book_id.getText().toString(), "UTF-8") +
                    "&category_name="+ URLEncoder.encode(editText_category_name.getText().toString(), "UTF-8") +
                    "&book_name="+ URLEncoder.encode(editText_book_name.getText().toString(), "UTF-8") +
                    "&author="+ URLEncoder.encode(editText_author.getText().toString(), "UTF-8") +
                    "&press="+ URLEncoder.encode(editText_press.getText().toString(), "UTF-8") +
                    "&public_date="+ URLEncoder.encode(editText_pulic_date.getText().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        new Thread(new CheckBook.MyThread()).start();
    }


    public class MyThread implements Runnable{
        @Override
        public void run() {
            String infoString = WebServicePost.executeHttpPost(data, "CheckBook");//获取服务器返回的数据

            //更新UI，使用runOnUiThread()方法
            showResponse(infoString);
        }
    }


    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            //更新UI
            @Override
            public void run() {

                Response response1 = new Response(response);

                if ("B04A0300A0800".equals(response1.getRecode())) {
                    info = response1.getInformation();
                    Intent intent = new Intent(CheckBook.this, CheckBookRes.class);
                    Response.bian(info);
                    intent.putExtra("respond", response1);
                    intent.putExtra("Person", person);
                    startActivity(intent);
//                    finish();
                } else {
                    Toast.makeText(CheckBook.this, "未知错误", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(Content.RESULT_CANCELED, intent);
                    finish();
                }

            }
        });
    }

}
