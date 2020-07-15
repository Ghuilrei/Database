package com.example.androiddemo.manager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.R;
import com.example.androiddemo.tool.Person;
import com.example.androiddemo.tool.Response;
import com.example.androiddemo.web.WebServicePost;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

public class AddBook extends AppCompatActivity implements View.OnClickListener {

    // 桌面内容
    private Spinner categoty;
    private EditText et_bookname;
    private EditText et_author;
    private EditText et_price;
    private EditText et_press;
    private EditText et_public_date;
    private EditText et_remain;
    private Button bt_confirm;
    // 用户信息类
    Person person;

    String data = "";
    private String category_id[];
    private String categpry_name="";
    private String categoty_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);

        // 接收用户数据
        person = (Person)getIntent().getSerializableExtra("Person");

        // 初始化
        categoty = findViewById(R.id.category);
        et_bookname = findViewById(R.id.et_bookname);
        et_author = findViewById(R.id.et_author);
        et_price = findViewById(R.id.et_price);
        et_press = findViewById(R.id.et_press);
        et_public_date = findViewById(R.id.et_public_date);
        et_remain = findViewById(R.id.et_remain);
        bt_confirm = findViewById(R.id.bt_confirm);

        category_id = getResources().getStringArray(R.array.category);

        //设置监听器并获取书籍类别
        categoty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoty_name = category_id[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        et_public_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    //将timeText传入用于显示所选择的时间
                    showDialogPick((EditText) v);
                } else {
                    // 此处为失去焦点时的处理内容
                }
            }
        });

        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.bt_confirm){
            // 要发送的数据
            try {
                data = "user_id="+ URLEncoder.encode(person.getUser_id(),"UTF-8")+
                        "&session="+URLEncoder.encode(person.getSession(),"UTF-8")+
                        "&operate=up&category_id="+URLEncoder.encode(categoty_name.substring(0, 1),"UTF-8")+
                        "&price="+URLEncoder.encode(et_price.getText().toString(), "UTF-8")+
                        "&book_name="+URLEncoder.encode(et_bookname.getText().toString(),"UTF-8")+
                        "&author="+URLEncoder.encode(et_author.getText().toString(),"UTF-8")+
                        "&press="+URLEncoder.encode(et_press.getText().toString(),"UTF-8")+
                        "&public_date="+URLEncoder.encode(et_public_date.getText().toString(),"UTF-8")+
                        "&remain="+URLEncoder.encode(et_remain.getText().toString(),"UTF-8");

                System.out.println(data);

            } catch (Exception e) {
                e.printStackTrace();
            }
            // 新建连接线程
            new Thread(new AddBook.MyThread()).start();
        }
    }

    public class MyThread implements Runnable{
        // 服务器返回的数据
        String infoString;

        @Override
        public void run() {
            //获取服务器返回的数据
            infoString = WebServicePost.executeHttpPost(data, "Book");
            //更新UI，使用runOnUiThread()方法
            showResponse(infoString);
        }
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
                if ("B10A0300A0500A1400".equals(recode)) {
                    Toast.makeText(getApplicationContext(), "上架成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "上架失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDialogPick(final EditText timeText) {
        final StringBuffer time = new StringBuffer();
        //获取Calendar对象，用于获取当前时间
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //实例化TimePickerDialog对象
//        final TimePickerDialog timePickerDialog = new TimePickerDialog(AddBook.this, new TimePickerDialog.OnTimeSetListener() {
//            //选择完时间后会调用该回调函数
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                time.append(" "  + hourOfDay + ":" + minute);
//                //设置TextView显示最终选择的时间
//                timeText.setText(time);
//            }
//        }, hour, minute, true);
        //实例化DatePickerDialog对象
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddBook.this, new DatePickerDialog.OnDateSetListener() {
            //选择完日期后会调用该回调函数
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //因为monthOfYear会比实际月份少一月所以这边要加1
                time.append(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                //设置TextView显示最终选择的时间
                timeText.setText(time);
//                //选择完日期后弹出选择时间对话框
//                timePickerDialog.show();
            }
        }, year, month, day);
        //弹出选择日期对话框
        datePickerDialog.show();
    }
}