package com.example.androiddemo.manager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class DelBook2 extends AppCompatActivity {

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
        person = (Person)getIntent().getSerializableExtra("Person");

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
                .setTitle("要续借的书籍编号为:")
                .setMessage(info)
                .setPositiveButton("确定",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            for (int o = 0; o < bookID.size(); ++o){

                                bookid = bookID.get(o);

                                data =  "user_id="+ URLEncoder.encode(person.getUser_id(),"UTF-8")+
                                        "&session="+URLEncoder.encode(person.getSession(),"UTF-8")+
                                        "&operate=down&book_id="+URLEncoder.encode(bookid,"UTF-8");

                                // 新建连接线程
                                MyThread myThread = new DelBook2.MyThread();
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
                        Intent intent = new Intent(DelBook2.this, DelBook.class);
                        intent.putExtra("Person", person);
                        finish();
                        startActivity(intent);
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
            infoString = WebServicePost.executeHttpPost(book_id, "Book");
            //更新UI，使用runOnUiThread()方法
            showResponse(infoString);
        }

        private void showResponse(final String response){
            runOnUiThread(new Runnable() {
                //更新UI
                @Override
                public void run() {

                    // 返回数据处理类
                    Response data = new Response(response);

                    // 返回码
                    String recode = data.getRecode();

                    // 注册成功
                    if ("B10A0300A0500A1500".equals(recode)) {
                        Toast.makeText(getApplicationContext(), "下架成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "下架失败", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(DelBook2.this, DelBook.class);
                    intent.putExtra("Person", person);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}