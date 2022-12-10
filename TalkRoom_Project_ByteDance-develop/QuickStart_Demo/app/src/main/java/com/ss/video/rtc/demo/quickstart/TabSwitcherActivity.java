package com.ss.video.rtc.demo.quickstart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.ss.rtc.demo.quickstart.R;
import com.ss.video.rtc.demo.quickstart.TabSwitcher.TabSwitcherFragment;
import com.ss.video.rtc.demo.quickstart.ViewModel.UserIdViewModel;

public class TabSwitcherActivity extends AppCompatActivity {
    private String userId;
    private UserIdViewModel userIdViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_switcher);

        // pass userId to EnterRoomFragment by ViewModel
        userId = (String) getIntent().getExtras().get(Constants.USER_ID_EXTRA);
        userIdViewModel = new ViewModelProvider(this).get(
                UserIdViewModel.class);
        userIdViewModel.getUserID().setValue(userId);

        // Add fragment_container, contains all things
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.fragment_container, new TabSwitcherFragment())
                //.addToBackStack(null)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}