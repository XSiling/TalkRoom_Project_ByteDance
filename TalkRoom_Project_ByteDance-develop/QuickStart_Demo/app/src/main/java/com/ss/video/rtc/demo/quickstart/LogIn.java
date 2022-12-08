package com.ss.video.rtc.demo.quickstart;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.ss.bytertc.engine.RTCEngine;
import com.ss.rtc.demo.quickstart.R;

import java.util.regex.Pattern;

public class LogIn extends AppCompatActivity {
    private LottieAnimationView LottieView;
    private ImageView imageView;
    private EditText usrName;
    private EditText usrPasswd;
    private Button button;
    private TextView version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        String SDKVersion = RTCEngine.getSdkVersion();

        LottieView = findViewById(R.id.loading_view);
        imageView = findViewById(R.id.imageView);
        usrName = findViewById(R.id.usrName);
        usrPasswd = findViewById(R.id.usrPasswd);
        button = findViewById(R.id.button);
        version = findViewById(R.id.version_tv1);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                1000);

        button.setOnClickListener((v)->{
            String usrInputContent = usrName.getText().toString();
            String passwordContent = usrPasswd.getText().toString();
            logIn(usrInputContent, passwordContent);
        });

        imageView.setVisibility(View.GONE);
        usrName.setVisibility(View.GONE);
        usrPasswd.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        version.setVisibility(View.GONE);
        LottieView.setVisibility(View.VISIBLE);

        LottieView.playAnimation();
        LottieView.addAnimatorListener(new Animator.AnimatorListener(){
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                imageView.setVisibility(View.VISIBLE);
                usrName.setVisibility(View.VISIBLE);
                usrPasswd.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                version.setVisibility(View.VISIBLE);

                LottieView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
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
        Log.d("tag","hello!!!!!!!!!!!" + Constants.USER_ID_EXTRA);
        startActivity(i);
    }
}
