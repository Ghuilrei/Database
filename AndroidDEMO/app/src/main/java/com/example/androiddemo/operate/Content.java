package com.example.androiddemo.operate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.R;
import com.example.androiddemo.manager.AddBook;
import com.example.androiddemo.manager.BorrowBook;
import com.example.androiddemo.manager.DelBook;
import com.example.androiddemo.manager.ReturnBook;
import com.example.androiddemo.tool.Person;
import com.example.androiddemo.user.CheckBook;
import com.example.androiddemo.user.History;
import com.example.androiddemo.user.Information;
@SuppressWarnings("ResourceType")

public class Content extends AppCompatActivity implements View.OnClickListener{

    public final int REQUEST_CODE_INFOMATION = 0;

    // 个人信息类
    Person person;

    // 意图
    Intent intent;

    private ImageView img;
    private TextView Sno;
    private TextView Sname;
    private Button check;
    private Button history;
    private Button exit;
    private Button addbook;
    private Button delbook;
    private Button borrowbook;
    private Button returnbook;

    private TextView textView_is_ban;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // 获取登陆信息
        person = (Person)getIntent().getSerializableExtra("person");

        // 添加基础内容

        // 初始化信息
        img = findViewById(R.id.imageView8);
        Sno = findViewById(R.id.Sno);
        Sname = findViewById(R.id.Sname);
        textView_is_ban = findViewById(R.id.Tv_is_ban);

        // 监听器
        img.setOnClickListener(this);
        Sno.setOnClickListener(this);
        Sname.setOnClickListener(this);

        // 设置姓名和uid
        Sname.setText(person.getUser_name());
        Sno.setText("uid:"+person.getUser_id());

        // 获取用户状态
        System.out.println("123123123"+person.getIs_ban());
        if ("1".equals(person.getIs_ban())) {
            textView_is_ban.setText("该账户正在封禁中...");
        }


        if ("1".equals(person.getIs_manager())) {
            // 添加管理员用户内容
            LinearLayout table = findViewById(R.id.linearlayout);

            // 查询图书
            check = new Button(this);
            check.setText("查询图书");
            check.setId(1);
            table.addView(check);

            table.addView(new TextView(this));

            // 查询历史借阅记录
            history = new Button(this);
            history.setText("查询历史借阅记录");
            history.setId(3);
            table.addView(history);

            table.addView(new TextView(this));

            // 书籍上架
            addbook = new Button(this);
            addbook.setText("书籍上架");
            addbook.setId(5);
            table.addView(addbook);

            table.addView(new TextView(this));

            // 书籍下架
            delbook = new Button(this);
            delbook.setText("书籍下架");
            delbook.setId(6);
            table.addView(delbook);

            table.addView(new TextView(this));

            // 借阅登记
            borrowbook = new Button(this);
            borrowbook.setText("借阅登记");
            borrowbook.setId(7);
            table.addView(borrowbook);

            table.addView(new TextView(this));

            // 归还登记
            returnbook = new Button(this);
            returnbook.setText("归还登记");
            returnbook.setId(8);
            table.addView(returnbook);

            table.addView(new TextView(this));

            // 推出登陆
            exit = new Button(this);
            exit.setText("退出登陆");
            exit.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            exit.setId(0);
            table.addView(exit);

            table.addView(new TextView(this));

            table.addView(new TextView(this));

            table.addView(new TextView(this));


            check.setOnClickListener(this);
            history.setOnClickListener(this);
            exit.setOnClickListener(this);
            addbook.setOnClickListener(this);
            delbook.setOnClickListener(this);
            borrowbook.setOnClickListener(this);
            returnbook.setOnClickListener(this);
        } else {
            // 添加普通用户内容
            LinearLayout table = findViewById(R.id.linearlayout);

            // 查询图书
            check = new Button(this);
            check.setText("查询图书");
            check.setId(1);
            table.addView(check);

            table.addView(new TextView(this));

            // 查询历史借阅记录
            history = new Button(this);
            history.setText("查询历史借阅记录");
            history.setId(3);
            table.addView(history);

            table.addView(new TextView(this));

            // 推出登陆
            exit = new Button(this);
            exit.setText("退出登陆");
            exit.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            exit.setId(0);
            table.addView(exit);


            table.addView(new TextView(this));

            table.addView(new TextView(this));

            table.addView(new TextView(this));


            check.setOnClickListener(this);
            history.setOnClickListener(this);
            exit.setOnClickListener(this);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == this.RESULT_OK) {
                assert data != null;
                person = (Person)data.getSerializableExtra("person");
                // 显示姓名、学号
                assert person != null;
                Sname.setText(person.getUser_name());
                Sno.setText("uid:"+person.getUser_id());
                System.out.println(person.getPhone());

            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            // 点击头像、姓名、学号 跳转信息界面
            case R.id.imageView8:
            case R.id.Sno:
            case R.id.Sname:
                intent = new Intent(Content.this, Information.class);
                intent.putExtra("Person", person);
                startActivityForResult(intent, REQUEST_CODE_INFOMATION);
                break;
            // 推出登陆
            case 0:
                intent = new Intent(Content.this, Login.class);
                new Logoff(person, this);
                startActivity(intent);
                finish();
                break;
            // 查询图书
            case 1:
                intent = new Intent(Content.this, CheckBook.class);
                intent.putExtra("Person", person);
                startActivityForResult(intent, 1);
                break;
            // 历史记录
            case 3:
                intent = new Intent(Content.this, History.class);
                intent.putExtra("Person", person);
                startActivityForResult(intent, 1);
                break;
            // 上架书籍
            case 5:
                intent = new Intent(Content.this, AddBook.class);
                intent.putExtra("Person", person);
                startActivityForResult(intent, 1);
                break;
            // 下架书籍
            case 6:
                intent = new Intent(Content.this, DelBook.class);
                intent.putExtra("Person", person);
                startActivityForResult(intent, 1);
                break;
            // 借阅登记
            case 7:
                intent = new Intent(Content.this, BorrowBook.class);
                intent.putExtra("Person", person);
                startActivityForResult(intent, 1);
                break;
            // 还书登记
            case 8:
                intent = new Intent(Content.this, ReturnBook.class);
                intent.putExtra("Person", person);
                startActivityForResult(intent, 1);
                break;
        }
    }
}
