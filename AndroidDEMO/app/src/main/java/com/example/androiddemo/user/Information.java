package com.example.androiddemo.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.R;
import com.example.androiddemo.operate.Content;
import com.example.androiddemo.tool.Person;
import com.example.androiddemo.tool.Response;
import com.example.androiddemo.web.WebServicePost;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class Information extends AppCompatActivity implements View.OnClickListener{

    // 初始化
    private EditText user_id;
    private EditText user_name;
    private EditText sex;
    private EditText age;
    private EditText phone;
    private EditText num;
    private EditText is_manager;
    private EditText is_ban;
    private Button ifom_save;

    // 目录
    private String catalogue;

    Person person;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        // 获取从Content传过来的数据
        Intent intent = getIntent();
        person = (Person)intent.getSerializableExtra("Person");

        //初始化信息
        user_id = findViewById(R.id.user_id);
        user_name = findViewById(R.id.user_name);
        sex = findViewById(R.id.sex);
        age = findViewById(R.id.age);
        phone = findViewById(R.id.phone);
        num = findViewById(R.id.num);
        is_manager = findViewById(R.id.is_manager);
        is_ban = findViewById(R.id.is_ban);
        ifom_save = findViewById(R.id.ifom_save);

        // 设置姓名学号
        user_name.setText(person.getUser_name());
        user_id.setText(person.getUser_id());

        // 监听器
        ifom_save.setOnClickListener(this);

        // 关键信息不能修改
        user_id.setEnabled(false);
        is_ban.setEnabled(false);
        is_manager.setEnabled(false);
        num.setEnabled(false);


        showInformation();
    }

    @Override
    public void onClick(View view) {
        // 新线程 保存数据
        try {
            data = "session="+URLEncoder.encode(person.getSession(),"UTF-8")+
                    "&user_name="+ URLEncoder.encode(user_name.getText().toString(),"UTF-8")+
                    "&user_id="+ URLEncoder.encode(person.getUser_id(),"UTF-8")+
                    "&sex="+ URLEncoder.encode(sex.getText().toString(),"UTF-8")+
                    "&age="+ URLEncoder.encode(age.getText().toString(),"UTF-8")+
                    "&phone="+ URLEncoder.encode(phone.getText().toString(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catalogue = "UpdateUser";

        person.setUser_name(user_name.getText().toString());
        person.setPhone(phone.getText().toString());

        System.out.println(data);
        new Thread(new Information.MyThread()).start();
    }


    private void showInformation() {
        // 新线程 请求信息
        data = "session="+person.getSession() +
                "&user_id="+person.getUser_id();
        catalogue = "CheckUser";
        new Thread(new Information.MyThread()).start();
    }

    public class MyThread implements Runnable{
        @Override
        public void run() {
            String infoString = WebServicePost.executeHttpPost(data, catalogue);//获取服务器返回的数据

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

                if ("B09A0300A1300".equals(response1.getRecode())) {
                    // 提交信息保存成功
                    Toast.makeText(Information.this, "保存成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("person", person);
                    setResult(Content.RESULT_OK, intent);
                    finish();
                } else if ("B08A0300".equals(response1.getRecode())) {
                    // 提交信息保存失败
                    HashMap<String, String> data = response1.getInformation()[0];
                    user_name.setText(data.get("user_name"));
                    user_id.setText(person.getUser_id());
                    age.setText(data.get("age"));
                    phone.setText(data.get("phone"));
                    sex.setText(data.get("sex"));
                    num.setText(data.get("num"));
                    if ("1".equals(data.get("is_manager"))) {
                        is_manager.setText("管理员");
                    } else {
                        is_manager.setText("普通用户");
                    }
                    if (data.get("is_ban").equals("0")) {
                        is_ban.setText("正常");
                    } else {
                        is_ban.setText("正在封禁");
                    }
                } else {
                    // 请求个人信息
                    Toast.makeText(Information.this, "保存失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}