package com.example.androiddemo.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.R;
import com.example.androiddemo.tool.Person;
import com.example.androiddemo.web.WebServicePost;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Information extends AppCompatActivity implements View.OnClickListener{

    private EditText eTSno;
    private EditText eTSname;
    private EditText eTSsex;
    private EditText eTSdept;
    private EditText eTSgrade;
    private EditText eTSxy;
    private EditText eTSphone;
    private EditText eTSis_ban;
    private Button ifom_save;
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
        eTSno = findViewById(R.id.eTSno);
        eTSname = findViewById(R.id.eTSname);
        eTSsex = findViewById(R.id.eTSsex);
        eTSdept = findViewById(R.id.eTSdept);
        eTSgrade = findViewById(R.id.eTSgrade);
        eTSxy = findViewById(R.id.eTSxy);
        eTSphone = findViewById(R.id.eTSphone);
        eTSis_ban = findViewById(R.id.eTSis_ban);
        ifom_save = findViewById(R.id.ifom_save);

        // 设置姓名学号
        eTSname.setText(person.getSname());
        eTSno.setText(person.getSno());

        // 监听器
        ifom_save.setOnClickListener(this);

        // 关键信息不能修改
        eTSno.setEnabled(false);
        eTSname.setEnabled(false);
        eTSis_ban.setEnabled(false);

        // 新线程 请求信息
        data = "";
        new Thread(new Information.MyThread()).start();
    }

    @Override
    public void onClick(View view) {

        // 新线程 保存数据
        data = "";
        new Thread(new Information.MyThread()).start();
    }

    public String readInfo(){
        File file = new File(person.getFilePath() + "SessionID.txt");
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String result = br.readLine();
            Toast.makeText(Information.this, result, Toast.LENGTH_LONG).show();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Information.this, "读取文件失败", Toast.LENGTH_LONG).show();
            return "NULL";
        }
    }

    public class MyThread implements Runnable{
        @Override
        public void run() {
            String infoString = WebServicePost.executeHttpPost(data, "Information");//获取服务器返回的数据

            //更新UI，使用runOnUiThread()方法
            showResponse(infoString);
        }
    }


    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            //更新UI
            @Override
            public void run() {
                if (response.equals("success")) {

                } else if (response.equals("failure")) {

                } else {
                    eTSname.setText(response);
                }
            }
        });

    }
}