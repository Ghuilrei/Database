package com.example.androiddemo.manager;

import android.content.Intent;
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

public class DelBook extends AppCompatActivity implements View.OnClickListener {
    

    String data;

    HashMap<String, String>[] info;

    private Button bt_delbook;

    CheckBox cB_choose[];

    Person person;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delbook);

        person = (Person)getIntent().getSerializableExtra("Person");

        try {
            data = "user_id="+ URLEncoder.encode(person.getUser_id(), "UTF-8")+
                    "&session="+URLEncoder.encode(person.getSession(), "UTF-8")+
                    "&book_id=&category_name=&book_name=&author=&press=&public_date=";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(data);

        new Thread(new DelBook.MyThread()).start();

        bt_delbook = findViewById(R.id.bt_delbook);

        bt_delbook.setOnClickListener(this);

    }

    public void addWegit() {

        TableLayout table = findViewById(R.id.tableLayout_delbook);
        table.setStretchAllColumns(true);

        TableRow tablerow = new TableRow(DelBook.this);
        tablerow.setBackgroundResource(R.drawable.tv_shape);

        TextView testview7 = new TextView(DelBook.this);
        testview7.setText("");
//        testview7.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview7);

        TextView testview2 = new TextView(DelBook.this);
        testview2.setText("书籍ID");
//        testview2.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview2);

        TextView testview4 = new TextView(DelBook.this);
        testview4.setText("所属分类");
//        testview4.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview4);

        TextView testview3 = new TextView(DelBook.this);
        testview3.setText("书名");
//        testview3.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview3);

        TextView testview5 = new TextView(DelBook.this);
        testview5.setText("作者");
//        testview5.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview5);

        TextView testview6 = new TextView(DelBook.this);
        testview6.setText("出版社");
//        testview6.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview6);

        TextView testview1 = new TextView(DelBook.this);
        testview1.setText("出版日期");
//        testview1.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview1);

        table.addView(tablerow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        try {
            cB_choose = new CheckBox[info.length];

            for (int i = 0; i < info.length; i++) {
                TableRow tablerow1 = new TableRow(DelBook.this);
                tablerow1.setBackgroundResource(R.drawable.tv_shape);

                cB_choose[i] = new CheckBox(DelBook.this);
                tablerow1.addView(cB_choose[i]);

                TextView et_book_id = new TextView(DelBook.this);
                et_book_id.setText(info[i].get("book_id"));
//                et_book_id.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_book_id);

                TextView et_category_name = new TextView(DelBook.this);
                et_category_name.setText(info[i].get("category_name"));
//                et_category_name.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_category_name);

                TextView et_book_name = new TextView(DelBook.this);
                et_book_name.setText(info[i].get("book_name"));
//                et_book_name.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_book_name);

                TextView et_author = new TextView(DelBook.this);
                et_author.setText(info[i].get("author"));
//                et_author.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_author);

                TextView et_press = new TextView(DelBook.this);
                et_press.setText(info[i].get("press"));
//                et_press.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_press);

                TextView et_public_date = new TextView(DelBook.this);
                et_public_date.setText(info[i].get("public_date"));
//                et_public_date.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_public_date);

                table.addView(tablerow1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();

            Toast.makeText(DelBook.this, "未查询到相关数据", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(DelBook.this, DelBook2.class);
            intent.putExtra("book_id", bookID);
            intent.putExtra("Person", person);
            finish();
            startActivity(intent);
        }
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
                    addWegit();
                } else {
                    Toast.makeText(DelBook.this, "未知错误", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(Content.RESULT_CANCELED, intent);
                    finish();
                }

            }
        });
    }
}
