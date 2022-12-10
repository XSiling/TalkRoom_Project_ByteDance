package com.ss.video.rtc.demo.quickstart.TabSwitcher;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.ss.rtc.demo.quickstart.R;

public class WebPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        WebView page=(WebView) findViewById(R.id.webpage);
        page.loadUrl(getIntent().getStringExtra("url"));
    }
}
