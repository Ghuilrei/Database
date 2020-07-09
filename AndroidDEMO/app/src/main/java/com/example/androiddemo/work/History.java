package com.example.androiddemo.work;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.R;

public class History extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        //获取控件
        webView = findViewById(R.id.webView);
        //装载URL
//        webView.loadUrl("http://192.168.0.116:8080/ServerDEMO/History");
        webView.loadUrl("https://baidu.com");
        //获取焦点
        webView.requestFocus();

        // 重写WebViewClient的shouldOverrideUrlLoading(WebView view, String url)
        // 使用view.loadUrl(url)来加载url
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
}
