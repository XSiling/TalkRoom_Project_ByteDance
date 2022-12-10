package com.ss.video.rtc.demo.quickstart.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserIdViewModel extends ViewModel {
    // User ID, to be shared by TabSwitcherActivity & EnterRoomFragment
    private MutableLiveData<String> userID = new MutableLiveData<>();

    public MutableLiveData<String> getUserID(){
        return userID;
    }
}
