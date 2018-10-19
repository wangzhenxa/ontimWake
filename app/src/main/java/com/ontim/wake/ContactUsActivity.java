package com.ontim.wake;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by wangzhen on 18-3-2.
 */

public class ContactUsActivity extends AppCompatActivity {
    private TextView mAboutcompany;
    private TextView mAboutmainpage;
    private TextView mAboutrecruit;
    @SuppressLint("ShowToast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        setTitle("联系我们");
        mAboutcompany = findViewById(R.id.about_company);
        mAboutmainpage = findViewById(R.id.about_mainpage);
        mAboutrecruit = findViewById(R.id.about_recruit);
        mAboutcompany.setOnClickListener(v -> {
//            WebView company = new WebView(ContactUsActivity.this);
//            company.loadUrl("www.baidu.com");
            Toast.makeText(ContactUsActivity.this,"clicked 公司", Toast.LENGTH_SHORT).show();
        });
        mAboutmainpage.setOnClickListener(v -> {
//            WebView company = new WebView(ContactUsActivity.this);
//            company.loadUrl("www.baidu.com");
            Toast.makeText(ContactUsActivity.this,"clicked 主页", Toast.LENGTH_SHORT).show();
        });
        mAboutrecruit.setOnClickListener(v -> {
//            WebView company = new WebView(ContactUsActivity.this);
//            company.loadUrl("www.baidu.com");
            Toast.makeText(ContactUsActivity.this,"clicked 招聘", Toast.LENGTH_SHORT).show();
        });
    }
}
