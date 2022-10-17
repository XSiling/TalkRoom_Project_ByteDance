package com.example.framework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button createroomButton = (Button)this.findViewById(R.id.button2);
        Button joinroomButton = (Button)this.findViewById(R.id.button3);

        createroomButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent j = new Intent(MainActivity.this, Room.class);
                startActivity(j);
            }
        });


        joinroomButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent j = new Intent(MainActivity.this, Room.class);
                startActivity(j);
            }
        });



    }



    @Override
    protected void onResume() {
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }
}
