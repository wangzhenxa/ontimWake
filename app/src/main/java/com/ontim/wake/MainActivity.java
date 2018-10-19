package com.ontim.wake;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import java.util.Timer;
import java.util.TimerTask;

/**
 * wake main activity
 */

public class MainActivity extends AppCompatActivity {
    //    private String
    private WebView webview;
    private Boolean isFirstpage = false;
    private Boolean isErrorpage = false;
    private String initUrl = "http://wake.ontim.cn:9000/login_page.php";
    private ProgressBar pg1;
    private String logedInUrl = "http://wake.ontim.cn:9000/view_all_bug_page.php";
    private RefreshLayout mRefreshLayout;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webview = findViewById(R.id.viewWeb);
        pg1 = findViewById(R.id.pb_progress);
        WebSettings webSettings = webview.getSettings();
        webview.getSettings().setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webview.loadUrl(initUrl);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        checkNetworkInfo();
        webview.setWebViewClient(new MyWebViewClient() {
        });
        webview.setWebChromeClient(new myChromeClient() {

        });
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setPrimaryColorsId(R.color.colorPrimaryDark);//全局设置主题颜色
        mRefreshLayout.setOnRefreshListener(refreshlayout -> {
            if (isErrorpage) {
                webview.loadUrl(initUrl);
            } else {
                webview.reload();
            }
        });
    }


    //重写onCreateOptionMenu(Menu menu)方法，当菜单第一次被加载时调用
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //填充选项菜单（读取XML文件、解析、加载到Menu组件上）
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //重写OptionsItemSelected(MenuItem item)来响应菜单项(MenuItem)的点击事件（根据id来区分是哪个item）
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.about:
//                getappVersion();
//                Toast.makeText(this, getappVersion(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this,AboutActivity.class);
                startActivity(i);
                break;

            case R.id.contact_us:
                Intent j = new Intent(this,ContactUsActivity.class);
                startActivity(j);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    /**
     * 是否显示Title
     *
     * @return
     */


    public void IfshowTitle() {
        if (webview.getUrl().equals(initUrl)) {
            this.getSupportActionBar().show();

        } else if (webview.getUrl().equals(logedInUrl)) {
            this.getSupportActionBar().hide();
        }
    }

    /**
     * 检测网络状态
     */
    public void checkNetworkInfo() {
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // mobile 3G Data Network
        NetworkInfo.State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        // wifi
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();

        // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接

        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING)
            return;
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING)
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("网络未打开，请确保连接网络后重试...")
                .setTitle("提示")
                .setCancelable(false)
                .setPositiveButton("配置", (dialog, id) -> {
                    // 进入无线网络配置界面
                    startActivity(new Intent(
                            Settings.ACTION_SETTINGS));
                    webview.loadUrl(initUrl);
                })
                .setNegativeButton("退出", (dialog, id) -> {
                    finish();
                    System.exit(0);
                });
        builder.show();

    }


    //销毁Webview

    /**
     * 双击返回键响应关闭app单击返回上一页面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (webview.getUrl().equals(initUrl) || webview.getUrl().equals(logedInUrl)) {
            isFirstpage = true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isFirstpage) {
                webview.goBack();
            } else {
                exitBy2Click(); //调用双击退出函数
            }
        }
        if (isErrorpage) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (!isFirstpage) {
                    webview.goBack();
                } else {
                    exitBy2Click(); //调用双击退出函数
                }
            }
        }
        return true;
    }


    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit;
        if (!isExit) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class MyWebViewClient extends WebViewClient {
        String TAG = "wangzhen";

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(TAG, "-MyWebViewClient->shouldOverrideUrlLoading()--");
            view.loadUrl(url);
            return true;
        }


        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            isErrorpage = true;
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.i(TAG, "-MyWebViewClient->onReceivedError()--\n errorCode=" + errorCode + " \ndescription=" + description + " \nfailingUrl=" + failingUrl);
            //这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
            view.loadUrl("file:///android_asset/errorPage.html");
            Log.d("wangzhen", "in the error page");
//            exitBy2Click();
        }
    }

    public class myChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            MainActivity.this.setTitle("Loading...");
            MainActivity.this.setProgress(progress * 100);
            if (progress == 100) {
                MainActivity.this.setTitle(R.string.app_name);
                pg1.setVisibility(View.GONE);//加载完网页进度条消失
                mRefreshLayout.finishRefresh();
                if (webview.getUrl().equals(initUrl)) {
                    MainActivity.this.setTitle("登录");
                }
                IfshowTitle();
            } else {
                pg1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                pg1.setProgress(progress);//设置进度值
            }
        }
    }
}






