package com.ss.video.rtc.demo.quickstart;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ss.rtc.demo.quickstart.R;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private String usrId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button createroomButton = (Button) this.findViewById(R.id.button2);

        EditText roomId = findViewById(R.id.roomId);

        usrId = (String) getIntent().getExtras().get(Constants.USER_ID_EXTRA);

        TextView tv = findViewById(R.id.id_main);
        tv.setText(usrId);
        createroomButton.setOnClickListener((v) -> {
            String roomIdContent = roomId.getText().toString();
            joinRoom(roomIdContent);
        });
    }

    private void joinRoom(String roomId) {
        if (TextUtils.isEmpty(roomId)) {
            Toast.makeText(this, "Please input your room ID", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Pattern.matches(Constants.INPUT_REGEX, roomId)) {
            Toast.makeText(this, "Illegal room ID", Toast.LENGTH_SHORT).show();
            return;

        }


        Intent j = new Intent(MainActivity.this, Room.class);

        j.putExtra(Constants.ROOM_ID_EXTRA, roomId);
        j.putExtra(Constants.USER_ID_EXTRA, usrId);

        startActivity(j);
    }

    @Override
    protected void onResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }
}
