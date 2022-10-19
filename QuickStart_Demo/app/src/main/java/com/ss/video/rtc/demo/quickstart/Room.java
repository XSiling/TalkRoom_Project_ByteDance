package com.ss.video.rtc.demo.quickstart;


import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ss.bytertc.engine.RTCRoom;
import com.ss.bytertc.engine.RTCRoomConfig;
import com.ss.bytertc.engine.RTCVideo;
import com.ss.bytertc.engine.UserInfo;
import com.ss.bytertc.engine.VideoCanvas;
import com.ss.bytertc.engine.VideoEncoderConfig;
import com.ss.bytertc.engine.data.RemoteStreamKey;
import com.ss.bytertc.engine.data.StreamIndex;
import com.ss.bytertc.engine.data.VideoFrameInfo;
import com.ss.bytertc.engine.handler.IRTCVideoEventHandler;
import com.ss.bytertc.engine.type.ChannelProfile;
import com.ss.rtc.demo.quickstart.R;

public class Room extends AppCompatActivity {


    private int roomUserNum = 0;//房间内的人数，根据此调整布局


    private FrameLayout mSelfContainer;
    private final FrameLayout[] mRemoteContainerArray = new FrameLayout[7];
    private final TextView[] mUserIdTvArray = new TextView[7];
    private final String [] mShowUidArray = new String[7];


    private RTCVideo mRTCVideo;
    private RTCRoom mRTCRoom;

    private RTCRoomEventHandlerAdapter mIRtcRoomEventHandler = new RTCRoomEventHandlerAdapter(){

        /**
         * 新用户加入本房间
         * @param userInfo
         * @param elapsed
         */
        @Override
        public void onUserJoined(UserInfo userInfo, int elapsed){
            super.onUserJoined(userInfo, elapsed);
            roomUserNum += 1;
            Log.d("IRTCRoomEventHandler", "onUserJoined: " + userInfo.getUid());
        }

        @Override
        public void onUserLeave(String uid, int reason){
            super.onUserLeave(uid, reason);
            Log.d("IRTCRoomEventHandler", "onUserLeave: " + uid);
            runOnUiThread(() -> removeRemoteView(uid));
        }
    };

    private IRTCVideoEventHandler mIRtcVideoEventHandler = new IRTCVideoEventHandler(){
        @Override
        public void onFirstRemoteVideoFrameDecoded(RemoteStreamKey remoteStreamKey, VideoFrameInfo frameInfo) {
            super.onFirstRemoteVideoFrameDecoded(remoteStreamKey, frameInfo);
            Log.d("IRTCVideoEventHandler", "onFirstRemoteVideoFrame: " + remoteStreamKey.toString());
            runOnUiThread(() -> setRemoteView(remoteStreamKey.getRoomId(), remoteStreamKey.getUserId()));
        }
    };

    private void setRemoteRenderView(String roomId, String uid, FrameLayout container){
        TextureView renderView = new TextureView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        container.removeAllViews();
        container.addView(renderView, params);

        VideoCanvas videoCanvas = new VideoCanvas();
        videoCanvas.renderView = renderView;
        videoCanvas.roomId = roomId;
        videoCanvas.uid = uid;
        videoCanvas.isScreen = false;
        videoCanvas.renderMode = VideoCanvas.RENDER_MODE_HIDDEN;

        mRTCVideo.setRemoteVideoCanvas(uid, StreamIndex.STREAM_INDEX_MAIN, videoCanvas);
    }

    private void setRemoteView(String roomId, String uid){
        int emptyInx = -1;
        for(int i=0; i < mShowUidArray.length; i++){
            if (TextUtils.isEmpty(mShowUidArray[i]) && emptyInx == -1){
                emptyInx = i;
            }else if (TextUtils.equals(uid, mShowUidArray[i])){
                return;
            }
        }
        if (emptyInx<0){
            return;
        }
        mShowUidArray[emptyInx] = uid;
        mUserIdTvArray[emptyInx].setText(String.format("UserId:%s", uid));
        setRemoteRenderView(roomId, uid, mRemoteContainerArray[emptyInx]);
    }


    private void removeRemoteView(String uid){
        for (int i=0; i < mShowUidArray.length; i++){
            if (TextUtils.equals(uid, mShowUidArray[i])){
                mShowUidArray[i] = null;
                mUserIdTvArray[i].setText(null);
                mRemoteContainerArray[i].removeAllViews();
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.room);

        Button btn_LeaveRoom = (Button) findViewById(R.id.btn_leaveroom);
        btn_LeaveRoom.setOnClickListener((v)->{
            finish();
        });

        Intent intent = getIntent();
        String roomId = intent.getStringExtra(Constants.ROOM_ID_EXTRA);
        String userId = intent.getStringExtra(Constants.USER_ID_EXTRA);

        initUI(roomId, userId);
        initEngineAndJoinRoom(roomId, userId);



    }

    private void initUI(String roomId, String userId){
        mSelfContainer = findViewById(R.id.usr1);

        mRemoteContainerArray[0] = findViewById(R.id.usr2);
        mRemoteContainerArray[1] = findViewById(R.id.usr3);
        mRemoteContainerArray[2] = findViewById(R.id.usr4);
        mRemoteContainerArray[3] = findViewById(R.id.usr5);
        mRemoteContainerArray[4] = findViewById(R.id.usr6);
        mRemoteContainerArray[5] = findViewById(R.id.usr7);
        mRemoteContainerArray[6] = findViewById(R.id.usr8);

        mUserIdTvArray[0] = findViewById(R.id.usr2_id);
        mUserIdTvArray[1] = findViewById(R.id.usr3_id);
        mUserIdTvArray[2] = findViewById(R.id.usr4_id);
        mUserIdTvArray[3] = findViewById(R.id.usr5_id);
        mUserIdTvArray[4] = findViewById(R.id.usr6_id);
        mUserIdTvArray[5] = findViewById(R.id.usr7_id);
        mUserIdTvArray[6] = findViewById(R.id.usr8_id);

        TextView usrID = findViewById(R.id.usr1_id);
        usrID.setText(userId);
        Log.d("e","Here the userId ought to be: " + userId);
    }



    private void initEngineAndJoinRoom(String roomId, String userId){
        mRTCVideo = RTCVideo.createRTCVideo(getApplicationContext(), Constants.APPID, mIRtcVideoEventHandler, null, null);

        VideoEncoderConfig videoEncoderConfig = new VideoEncoderConfig(182,180,15,800);
        mRTCVideo.setVideoEncoderConfig(videoEncoderConfig);
        setLocalRenderView(userId);

        mRTCVideo.startVideoCapture();
        mRTCVideo.startAudioCapture();
        mRTCRoom = mRTCVideo.createRTCRoom(roomId);
        mRTCRoom.setRTCRoomEventHandler(mIRtcRoomEventHandler);

        RTCRoomConfig roomConfig = new RTCRoomConfig(ChannelProfile.CHANNEL_PROFILE_COMMUNICATION,
                true, true, true);
        int joinRoomRes = mRTCRoom.joinRoom(Constants.TOKEN, UserInfo.create(userId,""),roomConfig);

    }

    private void setLocalRenderView(String uid){
        VideoCanvas videoCanvas = new VideoCanvas();
        TextureView renderView = new TextureView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                250,
                250);
        mSelfContainer.removeAllViews();
        mSelfContainer.addView(renderView, params);
        videoCanvas.renderView = renderView;
        videoCanvas.uid = uid;
        videoCanvas.isScreen = false;
        videoCanvas.renderMode = VideoCanvas.RENDER_MODE_HIDDEN;

        mRTCVideo.setLocalVideoCanvas(StreamIndex.STREAM_INDEX_MAIN, videoCanvas);

    }

    @Override
    protected void onResume() {
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    @Override
    public void finish(){
        super.finish();
        if (mRTCRoom != null){
            mRTCRoom.leaveRoom();
            mRTCRoom.destroy();
        }

        mRTCRoom = null;
        RTCVideo.destroyRTCVideo();
        mIRtcVideoEventHandler = null;
        mIRtcRoomEventHandler = null;
        mRTCVideo = null;
    }
}
