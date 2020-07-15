package com.example.androiddemo.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.tool.Person;
import com.example.androiddemo.tool.Response;
import com.example.androiddemo.web.WebServicePost;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class OrderBook extends AppCompatActivity {

    // 用户信息类
    Person person;

    String data = "";

    String bookid;

    ArrayList<String> bookID;

    String info = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookID = getIntent().getStringArrayListExtra("book_id");
        person = (Person)getIntent().getSerializableExtra("person");

        for (int o = 0; o < bookID.size(); ++o){

            bookid = bookID.get(o);
            if (o == 0) {
                info += "- " + bookid;
            } else {
                info += "\n- " + bookid;
            }
        }

        ShowDialog();
    }

    //弹窗内容
    private void ShowDialog() {
//        final EditText et_book_id = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("要预约的书籍编号为:")
                .setMessage(info)
                .setPositiveButton("确定",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            for (int o = 0; o < bookID.size(); ++o){

                                bookid = bookID.get(o);

                                data =  "user_id="+ URLEncoder.encode(person.getUser_id(),"UTF-8")+
                                        "&session="+URLEncoder.encode(person.getSession(),"UTF-8")+
                                        "&book_id="+URLEncoder.encode(bookid,"UTF-8");

                                // 新建连接线程
                                OrderBook.MyThread myThread = new OrderBook.MyThread();
                                myThread.setBook_id(data);
                                new Thread(myThread).start();
                            }

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                })
                .setNegativeButton("取消",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }

                })
                .show();

    }

    public class MyThread implements Runnable {
        // 服务器返回的数据
        String infoString;

        String book_id;

        public void setBook_id(String book_id) {
            this.book_id = book_id;
        }

        @Override
        public void run() {
            //获取服务器返回的数据
            infoString = WebServicePost.executeHttpPost(book_id, "OrderBook");
            //更新UI，使用runOnUiThread()方法
            showResponse(infoString);
        }

        private void showResponse(final String response){
            runOnUiThread(new Runnable() {
                //更新UI
                @Override
                public void run() {

                    try {
                        // 返回数据处理类
                        Response data = new Response(response);

                        // 返回码
                        String recode = data.getRecode();

                        // 注册成功
                        if ("B05A0300A0900".equals(recode)) {
                            Toast.makeText(getApplicationContext(), "预约成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "预约失败", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }
}