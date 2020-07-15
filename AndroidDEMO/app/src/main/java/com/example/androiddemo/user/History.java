package com.example.androiddemo.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.ArrayList;
import java.util.HashMap;

public class History extends AppCompatActivity implements View.OnClickListener{

    String data;

    HashMap<String, String>[] info;

    private Button bT_renew;

    CheckBox cB_choose[];

    Person person;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        person = (Person)getIntent().getSerializableExtra("Person");

        try {
            data = "user_id="+URLEncoder.encode(person.getUser_id(), "UTF-8")+ "&session="+URLEncoder.encode(person.getSession(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(data);

        new Thread(new History.MyThread()).start();

        setContentView(R.layout.activity_history);

        bT_renew = findViewById(R.id.bT_renew);

        bT_renew.setOnClickListener(this);

    }

    public void addWegit() {


        TableLayout table = findViewById(R.id.tableLayout);
        table.setStretchAllColumns(true);

        TableRow tablerow = new TableRow(History.this);
        tablerow.setBackgroundResource(R.drawable.tv_shape);

        TextView testview1 = new TextView(History.this);
        testview1.setText("  ");
        tablerow.addView(testview1);

        TextView testview2 = new TextView(History.this);
        testview2.setText("书名");
//        testview2.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview2);

        TextView testview4 = new TextView(History.this);
        testview4.setText("借书日期");
//        testview4.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview4);

        TextView testview3 = new TextView(History.this);
        testview3.setText("应还日期");
//        testview3.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview3);

        TextView testview5 = new TextView(History.this);
        testview5.setText("续借");
//        testview5.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview5);

        TextView testview6 = new TextView(History.this);
        testview6.setText("归还日期");
//        testview6.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview6);

        table.addView(tablerow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        try {
            cB_choose = new CheckBox[info.length];
            for (int i = 0; i < info.length; i++) {
                TableRow tablerow1 = new TableRow(History.this);
                tablerow1.setBackgroundResource(R.drawable.tv_shape);


                cB_choose[i] = new CheckBox(History.this);
                cB_choose[i].setBackgroundResource(R.drawable.tv_shape);
//                cB_choose[i].setVisibility(View.INVISIBLE);
                tablerow1.addView(cB_choose[i]);

                if ("1".equals(info[i].get("is_return")) && "0".equals(info[i].get("is_renew"))) {
                    cB_choose[i].setClickable(false);
                }

                TextView et_book_name = new TextView(History.this);
                et_book_name.setText(info[i].get("book_name"));
//                et_book_name.setBackgroundResource(R.drawable.tv_shape);
//                et_book_name.setHeight(60);
                tablerow1.addView(et_book_name);

                TextView et_borrow_date = new TextView(History.this);
                et_borrow_date.setText(info[i].get("borrow_date").substring(0, 10));
//                et_borrow_date.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_borrow_date);

                TextView et_return_date = new TextView(History.this);
                et_return_date.setText(info[i].get("return_date"));
//                et_return_date.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_return_date);

                TextView et_is_renew = new TextView(History.this);
                if ("1".equals(info[i].get("is_renew"))) {
                    et_is_renew.setText("是");
                } else {
                    et_is_renew.setText("否");
                }
//                et_is_renew.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_is_renew);

                TextView et_is_return = new TextView(History.this);
                if ("1".equals(info[i].get("is_return"))) {
                    et_is_return.setText(info[i].get("real_date"));
                } else {
                    et_is_return.setText("未还");
                }
//                et_is_return.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_is_return);

                table.addView(tablerow1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();

            Toast.makeText(History.this, "未查询到相关数据", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            setResult(Content.RESULT_CANCELED, intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        ArrayList<String> bookID = new ArrayList<>();
        for (int i = 0; i < cB_choose.length; ++i) {
            if (cB_choose[i].isChecked()) {
                bookID.add(info[i].get("book_id"));
            }
        }
        if (bookID.size() > 0) {
            Intent intent = new Intent(History.this, RenewBook.class);
            intent.putExtra("book_id", bookID);
            intent.putExtra("Person", person);
            finish();
            startActivity(intent);
        }
    }

    public class MyThread implements Runnable{
        @Override
        public void run() {
            String infoString = WebServicePost.executeHttpPost(data, "CheckHistory");//获取服务器返回的数据

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

                if ("B06A0300A1000".equals(response1.getRecode())) {
                    info = response1.getInformation();
                    addWegit();
                } else {
                    Toast.makeText(History.this, "未知错误", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(Content.RESULT_CANCELED, intent);
                    finish();
                }

            }
        });
    }


}
