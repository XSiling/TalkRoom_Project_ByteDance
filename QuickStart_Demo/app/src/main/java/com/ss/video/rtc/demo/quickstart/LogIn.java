package com.ss.video.rtc.demo.quickstart;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ss.bytertc.engine.RTCEngine;
import com.ss.rtc.demo.quickstart.R;

import java.util.regex.Pattern;

public class LogIn extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        String SDKVersion = RTCEngine.getSdkVersion();

        Log.d("tag","i'm requesting permissions!!!!!!!!!!");

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                1000);

        EditText usrInput = findViewById(R.id.usrName);
        EditText password = findViewById(R.id.usrPasswd);


        Button btn1 = (Button) findViewById(R.id.button);
        btn1.setOnClickListener((v)->{
            String usrInputContent = usrInput.getText().toString();
            String passwordContent = password.getText().toString();
            logIn(usrInputContent, passwordContent);
        });


    }
    /*
    在这里进行LogIn的检测，可能会有一些和数据库的连接或者其他的，
    暂时以只要非空即可进入下一页面实现
     */
    private void logIn(String usrId, String pw){
        if (TextUtils.isEmpty(usrId)){
            Toast.makeText(this, "Please input your ID", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Pattern.matches(Constants.INPUT_REGEX, usrId)){
            Toast.makeText(this,"Illegal ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(LogIn.this, MainActivity.class);
        i.putExtra(Constants.USER_ID_EXTRA, usrId);
        startActivity(i);
    }



}
