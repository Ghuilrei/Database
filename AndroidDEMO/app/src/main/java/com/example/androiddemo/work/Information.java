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
import com.example.androiddemo.tool.StaticTool;
import com.example.androiddemo.web.WebServicePost;

import java.util.HashMap;

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

        showInformation();
    }

    @Override
    public void onClick(View view) {

        // 新线程 保存数据
        data = "Session="+person.getSession()+
                "&Ssex"+eTSsex.getText().toString()+
                "&Sdept"+eTSdept.getText().toString()+
                "&SGrade"+eTSgrade.getText().toString()+
                "&Sphone"+eTSphone.getText().toString()+
                "&Sxy"+eTSxy.getText().toString();
        new Thread(new Information.MyThread()).start();
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
                    // 提交信息保存成功
                    Toast.makeText(Information.this, "保存成功！", Toast.LENGTH_SHORT).show();
                } else if (response.equals("failure")) {
                    // 提交信息保存失败
                    Toast.makeText(Information.this, "保存失败！", Toast.LENGTH_SHORT).show();
                } else {
                    // 请求个人信息
                    // TODO response格式要求："{[Session:666666],[Sno:666666],[Sname:admin]}"
                    HashMap<String, String> data = StaticTool.Regular_Expression(response);
                    eTSname.setText(data.get("Sno"));
                    eTSno.setText(data.get("Sname"));
                    eTSdept.setText(data.get("Sdept"));
                    eTSgrade.setText(data.get("SGrade"));
                    eTSphone.setText(data.get("Sphone"));
                    eTSsex.setText(data.get("Ssex"));
                    eTSxy.setText(data.get("Sxy"));
                    if (data.get("Sis_ban").equals("false")) {
                        eTSis_ban.setText("正常");
                    } else {
                        eTSis_ban.setText("正在封禁");
                    }
                }
            }
        });
    }

    private void showInformation() {
        // 新线程 请求信息
        data = "Session="+person.getSession();
        new Thread(new Information.MyThread()).start();
    }
}