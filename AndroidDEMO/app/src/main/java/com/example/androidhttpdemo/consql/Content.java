package com.example.androidhttpdemo.consql;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidhttpdemo.R;
import com.example.androidhttpdemo.web.WebServiceCon;

public class Content extends AppCompatActivity implements View.OnClickListener{

    private TextView info;
    String SessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        //初始化信息
        info = (TextView)findViewById(R.id.textView13);

        new Thread(new Content.MyThread()).start();
    }

    @Override
    public void onClick(View v) {
        //设置子线程，分别进行Get和Post传输数据
        new Thread(new Content.MyThread()).start();
    }

    public class MyThread implements Runnable{
        @Override
        public void run() {
            String infoString = WebServiceCon.executeHttpGet("ConLet");//获取服务器返回的数据

            //更新UI，使用runOnUiThread()方法
            showResponse(infoString);
        }
    }


    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            //更新UI
            @Override
            public void run() {
                info.setText(response);
            }
        });
    }
}
