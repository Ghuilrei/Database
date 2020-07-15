package com.example.androiddemo.user;

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

import java.util.ArrayList;
import java.util.HashMap;

public class CheckBookRes extends AppCompatActivity implements View.OnClickListener {

    // 接收到信息
    HashMap<String, String>[] info;

    Person person;

    Button bT_orderbook;

    CheckBox cB_choose[];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbook_res);

        person = (Person)getIntent().getSerializableExtra("Person");

        bT_orderbook = findViewById(R.id.bT_orderbook);
        bT_orderbook.setOnClickListener(this);

        info = ((Response)getIntent().getSerializableExtra("respond")).getInformation();

        addWegit();


    }
    public void addWegit() {

        TableLayout table = findViewById(R.id.check_book_res_tablelayout);
        table.setStretchAllColumns(true);

        TableRow tablerow = new TableRow(CheckBookRes.this);
        tablerow.setBackgroundResource(R.drawable.tv_shape);

        TextView testview7 = new TextView(CheckBookRes.this);
        testview7.setText("");
//        testview7.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview7);

        TextView testview2 = new TextView(CheckBookRes.this);
        testview2.setText("书籍ID");
//        testview2.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview2);

        TextView testview4 = new TextView(CheckBookRes.this);
        testview4.setText("所属分类");
//        testview4.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview4);

        TextView testview3 = new TextView(CheckBookRes.this);
        testview3.setText("书名");
//        testview3.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview3);

        TextView testview5 = new TextView(CheckBookRes.this);
        testview5.setText("作者");
//        testview5.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview5);

        TextView testview6 = new TextView(CheckBookRes.this);
        testview6.setText("出版社");
//        testview6.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview6);

        TextView testview1 = new TextView(CheckBookRes.this);
        testview1.setText("剩余数量");
//        testview1.setBackgroundResource(R.drawable.tv_shape);
        tablerow.addView(testview1);

        table.addView(tablerow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        try {
                cB_choose = new CheckBox[info.length];

            for (int i = 0; i < info.length; i++) {
                TableRow tablerow1 = new TableRow(CheckBookRes.this);
                tablerow1.setBackgroundResource(R.drawable.tv_shape);

                cB_choose[i] = new CheckBox(CheckBookRes.this);
                tablerow1.addView(cB_choose[i]);

                TextView et_book_id = new TextView(CheckBookRes.this);
                et_book_id.setText(info[i].get("book_id"));
//                et_book_id.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_book_id);

                TextView et_category_name = new TextView(CheckBookRes.this);
                et_category_name.setText(info[i].get("category_name"));
//                et_category_name.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_category_name);

                TextView et_book_name = new TextView(CheckBookRes.this);
                et_book_name.setText(info[i].get("book_name"));
//                et_book_name.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_book_name);

                TextView et_author = new TextView(CheckBookRes.this);
                et_author.setText(info[i].get("author"));
//                et_author.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_author);

                TextView et_press = new TextView(CheckBookRes.this);
                et_press.setText(info[i].get("press"));
//                et_press.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_press);

                TextView et_public_date = new TextView(CheckBookRes.this);
                et_public_date.setText(info[i].get("remain"));
//                et_public_date.setBackgroundResource(R.drawable.tv_shape);
                tablerow1.addView(et_public_date);

                table.addView(tablerow1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();

            Toast.makeText(CheckBookRes.this, "未查询到相关数据", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(CheckBookRes.this, OrderBook.class);
        intent.putExtra("book_id", bookID);
        intent.putExtra("person", person);
        startActivityForResult(intent, 1);
    }
}
