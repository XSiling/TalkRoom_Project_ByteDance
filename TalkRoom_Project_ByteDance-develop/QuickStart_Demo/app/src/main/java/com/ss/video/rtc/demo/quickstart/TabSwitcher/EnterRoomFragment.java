package com.ss.video.rtc.demo.quickstart.TabSwitcher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ss.rtc.demo.quickstart.R;
import com.ss.video.rtc.demo.quickstart.Constants;
import com.ss.video.rtc.demo.quickstart.MainActivity;
import com.ss.video.rtc.demo.quickstart.Room;
import com.ss.video.rtc.demo.quickstart.ViewModel.UserIdViewModel;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnterRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterRoomFragment extends BaseFragment {

    private static final String TAG = "EnterRoomFragment";
    private UserIdViewModel userIdViewModel;
    private String userId;
    @Override
    protected String getLogTag(){
        return TAG;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EnterRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnterRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnterRoomFragment newInstance(String param1, String param2) {
        EnterRoomFragment fragment = new EnterRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userIdViewModel = new ViewModelProvider(getActivity()).get(
            UserIdViewModel.class
        );
        Log.i("Print :)", "userId:\t " + userIdViewModel.getUserID() );

        userIdViewModel.getUserID().observe(
                this, new Observer<String>() {
                    @Override
                    public void onChanged(String mUserID) {
                        Log.i("Print :))", mUserID);
                        userId = mUserID;
                    }
                }
        );


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_enter_room, container, false);

        Button createroomButton = view.findViewById(R.id.button2);
        EditText roomId = view.findViewById(R.id.roomId);

        TextView tv = view.findViewById(R.id.id_main);
        tv.setText(userId);
        createroomButton.setOnClickListener((v) -> {
            String roomIdContent = roomId.getText().toString();
            joinRoom(roomIdContent);
        });
        return view;
    }

    private void joinRoom(String roomId) {
        if (TextUtils.isEmpty(roomId)) {
            Toast.makeText(getActivity(), "Please input your room ID", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Pattern.matches(Constants.INPUT_REGEX, roomId)) {
            Toast.makeText(getActivity(), "Illegal room ID", Toast.LENGTH_SHORT).show();
            return;

        }

        // Unsure
        Intent j = new Intent(getActivity(), Room.class);

        j.putExtra(Constants.ROOM_ID_EXTRA, roomId);
        j.putExtra(Constants.USER_ID_EXTRA, userId);

        startActivity(j);
    }

    @Override
    public void onResume() {
        if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }
}