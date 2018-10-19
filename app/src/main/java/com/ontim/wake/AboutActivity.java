package com.ontim.wake;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by wangzhen on 18-3-2.
 */

@SuppressLint("Registered")
public class AboutActivity extends AppCompatActivity {
    TextView tv_version;
    Button checkUpdate;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        setTitle("关于");
        tv_version = findViewById(R.id.app_version);
        checkUpdate = findViewById(R.id.check_update);
        tv_version.setText("版本:"+getAppVersion());
        checkUpdate.setOnClickListener(v -> Toast.makeText(AboutActivity.this,"已是最新版本",Toast.LENGTH_SHORT).show());

    }
    public String getAppVersion() {
        String versionName=null;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
             versionName = packageInfo.versionName;//获得版本名称
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
