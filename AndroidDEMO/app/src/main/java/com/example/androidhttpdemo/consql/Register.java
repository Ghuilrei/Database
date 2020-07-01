package com.example.androidhttpdemo.consql;


import android.os.Bundle;

import com.example.androidhttpdemo.R;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.DialogPreference;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.androidhttpdemo.web.WebServiceGet;
import com.example.androidhttpdemo.web.WebServicePost;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText regUserID;
    private EditText regUserName;
    private EditText regPassWord;
    private EditText regPassWord2;
    private Button btn_reg;

    ProgressDialog dialog;

    protected boolean onCheck() {
        return regPassWord.getText().toString().equals(regPassWord2.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //修改标题栏title
        ActionBar ac = getSupportActionBar();
        ac.setTitle("注册");

        //初始化
        regUserID = (EditText)findViewById(R.id.regUserID);
        regUserName = (EditText)findViewById(R.id.regUserName);
        regPassWord = (EditText)findViewById(R.id.regPassWord);
        regPassWord2 = (EditText)findViewById(R.id.regPassWord2);
        btn_reg = (Button)findViewById(R.id.btn_reg);

        btn_reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_reg:
                if (onCheck()) {
                    dialog = new ProgressDialog(Register.this);
                    dialog.setTitle("正在注册");
                    dialog.setMessage("请稍后");
                    dialog.show();

                    new Thread(new RegThread()).start();
                    break;
                } else {
                    AlertDialog.Builder builder  = new AlertDialog.Builder(Register.this);
                    builder.setTitle("错误" ) ;
                    builder.setMessage("两次密码输入不一致" ) ;
                    builder.setPositiveButton("是" ,  null );
                    builder.show();
                }
        }
    }

    public class RegThread implements Runnable{
        @Override
        public void run() {

            //获取服务器返回数据
//            String RegRet = WebServiceGet.executeHttpGet(regUserID.getText().toString(), regUserName.getText().toString(),regPassWord.getText().toString(),"RegLet");
            String RegRet = WebServicePost.executeHttpPost(regUserID.getText().toString(), regUserName.getText().toString(),regPassWord.getText().toString(),"RegLet");
            if (RegRet != null) {
                //更新UI，界面处理
                showReq(RegRet);
            }
        }
    }
    private void showReq(final String RegRet){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(RegRet.equals("true")){
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setTitle("注册信息");
                    builder.setMessage("注册成功");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Register.this,Login.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }else{
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setTitle("注册信息");
                    builder.setMessage("注册失败");
                    builder.setCancelable(false);
//                    builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent intent = new Intent(Register.this,Register.class);
//                            startActivity(intent);
//                        }
//                    });
                    builder.show();
                }
            }
        });
    }
}