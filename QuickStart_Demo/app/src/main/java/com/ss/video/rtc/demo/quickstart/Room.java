package com.ss.video.rtc.demo.quickstart;



import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ss.bytertc.engine.RTCRoom;
import com.ss.bytertc.engine.RTCRoomConfig;
import com.ss.bytertc.engine.RTCVideo;
import com.ss.bytertc.engine.UserInfo;
import com.ss.bytertc.engine.VideoCanvas;

import com.ss.bytertc.engine.data.CameraId;
import com.ss.bytertc.engine.data.RemoteStreamKey;
import com.ss.bytertc.engine.data.StreamIndex;
import com.ss.bytertc.engine.data.VideoFrameInfo;
import com.ss.bytertc.engine.handler.IRTCVideoEventHandler;
import com.ss.bytertc.engine.type.ChannelProfile;
import com.ss.bytertc.engine.type.MediaStreamType;
import com.ss.bytertc.engine.type.RTCRoomStats;
import com.ss.bytertc.engine.type.StreamRemoveReason;
import com.ss.rtc.demo.quickstart.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Room extends AppCompatActivity {
//    private RecyclerView mUserRV;
//    private List<User> mUserList = new ArrayList<>();
//    private UserAdapter mUserAdapter;

    private FrameLayout mSelfContainer;
    private TextView mSelfUserId;
    private final FrameLayout[] mRemoteContainerArray = new FrameLayout[7];
    private final TextView[] mUserIdArray = new TextView[7];
    private final String[] mShowUidArray = new String[7];

    private Button videoButton;
    private Button micButton;

    //some parameters related to audio/video switch
    private boolean mIsMuteAudio = false;
    private boolean mIsMuteVideo = false;

    private RTCVideo mRTCVideo;
    private RTCRoom mRTCRoom;

    private CameraId mCameraID = CameraId.CAMERA_ID_FRONT;



    //////

    private Timer timer;
    private boolean Start;
    private int cnt;

    private RecyclerView mVCChatRv;
    private final List<String> itemList = new ArrayList<>();
    private VCChatAdapter mVCChatAdapter;
    //////

/////////////////////////////
////////////////////////////////////
void setInvisible() {
    AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
    disappearAnimation.setDuration(500);
    mVCChatRv.startAnimation(disappearAnimation);
    disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            mVCChatRv.setVisibility(View.INVISIBLE);
        }
    });
}
///////////////////////////////////
    final Handler mHandler = new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
                mVCChatRv.setVisibility(View.VISIBLE);
                cnt = 10;
                if(Start == true) {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if(cnt <= 0) {
                                if(mVCChatRv.getVisibility() == View.VISIBLE) setInvisible();
                            } else cnt--;
                            Log.d("lfycnt", String.valueOf(cnt));
                        }
                    }, 0, 1000);
                } else {
                    Start = true;
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if(cnt <= 0) {
                                if(mVCChatRv.getVisibility() == View.VISIBLE) setInvisible();
                            } else cnt--;
                            Log.d("lfycnt", String.valueOf(cnt));
                        }
                    }, 0, 1000);
                }
            }
        }
    };
    ////////////////////////////////
    /////////////////////////////////

    private void initGetMessage(String userId){

        mVCChatAdapter = new VCChatAdapter();
        mVCChatRv = (RecyclerView) findViewById(R.id.voice_chat_demo_main_chat_rv);
        mVCChatRv.setLayoutManager(new LinearLayoutManager(
                Room.this, RecyclerView.VERTICAL, false));
        mVCChatRv.setAdapter(mVCChatAdapter);
        cnt = 10;

        View view = findViewById(R.id.review);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.sendMessage(Message.obtain(mHandler, 1));
            }
        });

        TextView textViewButton = findViewById(R.id.voice_chat_demo_main_input_send);
        EditText textView = findViewById(R.id.voice_chat_demo_main_input_et);

        textViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String MESSAGE = textView.getText().toString();
                mRTCRoom.sendRoomMessage(MESSAGE);
                mVCChatAdapter.addChatMsg(userId + ": " + MESSAGE);
                textView.setText("");
                if(mVCChatAdapter.getItemCount() > 6) mVCChatRv.smoothScrollToPosition(mVCChatAdapter.getItemCount()-1);
            }
        });
    }


    public void OutSendMessage(){
        mHandler.sendMessage(Message.obtain(mHandler, 1));
    }


    ////////////////////////////////


//    private void setWH(int position, int width, int height){
//        mUserList.get(position).setmWidth(width);
//        mUserList.get(position).setmHeight(height);
//    }
//
//    private void FreshWidthHeight(){
//        //不在这个函数里notify change
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(Room.this, 4);
//        int totalWidth = this.getWindowManager().getDefaultDisplay().getWidth();
//        int totalHeight = this.getWindowManager().getDefaultDisplay().getHeight() * 4 / 5;
//        int userNum = mUserList.size();
//        switch (userNum){
//            case 1:
//                setWH(0, totalWidth, totalHeight);
//                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        return 4;
//                    }
//                });
//                break;
//            case 2:
//                setWH(0, totalWidth/2, totalHeight);
//                setWH(1, totalWidth/2, totalHeight);
//                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        return 2;
//                    }
//                });
//                break;
//            case 3:
//                setWH(0, totalWidth, totalHeight/2);
//                setWH(1, totalWidth/2, totalHeight/2);
//                setWH(2, totalWidth/2, totalHeight/2);
//                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        switch(position){
//                            case 0:
//                                return 4;
//                            default:
//                                return 2;
//                        }
//                    }
//                });
//                break;
//            case 4:
//                setWH(0, totalWidth/2, totalHeight/2);
//                setWH(1, totalWidth/2, totalHeight/2);
//                setWH(2, totalWidth/2, totalHeight/2);
//                setWH(3, totalWidth/2, totalHeight/2);
//                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        return 2;
//                    }
//                });
//                break;
//            case 5:
//                setWH(0, totalWidth, totalHeight/2);
//                setWH(1, totalWidth/4, totalHeight/2);
//                setWH(2, totalWidth/4, totalHeight/2);
//                setWH(3, totalWidth/4, totalHeight/2);
//                setWH(4, totalWidth/4, totalHeight/2);
//                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        switch(position){
//                            case 0:
//                                return 4;
//                            default:
//                                return 1;
//                        }
//                    }
//                });
//                break;
//            case 6:
//                setWH(0, totalWidth/2, totalHeight/2);
//                setWH(1, totalWidth/2, totalHeight/2);
//                setWH(2, totalWidth/4, totalHeight/2);
//                setWH(3, totalWidth/4, totalHeight/2);
//                setWH(4, totalWidth/4, totalHeight/2);
//                setWH(5, totalWidth/4, totalHeight/2);
//                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        switch(position){
//                            case 0:
//                            case 1:
//                                return 2;
//                            default:
//                                return 1;
//                        }
//                    }
//                });
//                break;
//
//            case 7:
//                setWH(0, totalWidth/4, totalHeight/2);
//                setWH(1, totalWidth/4, totalHeight/2);
//                setWH(2, totalWidth/4, totalHeight/2);
//                setWH(3, totalWidth/4, totalHeight/2);
//                setWH(4, totalWidth/4, totalHeight/2);
//                setWH(5, totalWidth/4, totalHeight/2);
//                setWH(6, totalWidth/4, totalHeight/2);
//                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        return 1;
//                    }
//                });
//                break;
//            case 8:
//                setWH(0, totalWidth/4, totalHeight/2);
//                setWH(1, totalWidth/4, totalHeight/2);
//                setWH(2, totalWidth/4, totalHeight/2);
//                setWH(3, totalWidth/4, totalHeight/2);
//                setWH(4, totalWidth/4, totalHeight/2);
//                setWH(5, totalWidth/4, totalHeight/2);
//                setWH(6, totalWidth/4, totalHeight/2);
//                setWH(7, totalWidth/4, totalHeight/2);
//                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        return 1;
//                    }
//                });
//                break;
//        }
//        mUserRV.setLayoutManager(gridLayoutManager);
//    }

    private RTCRoomEventHandlerAdapter mIRtcRoomEventHandler = new RTCRoomEventHandlerAdapter(){

        /**
         * 新用户加入本房间
         * @param userInfo
         * @param elapsed
         */
        @Override
        public void onUserJoined(UserInfo userInfo, int elapsed){
            super.onUserJoined(userInfo, elapsed);
            Log.d("IRTCRoomEventHandler", "onUserJoined: " + userInfo.getUid());
        }

        @Override
        public void onUserLeave(String uid, int reason){
            super.onUserLeave(uid, reason);
            Log.d("IRTCRoomEventHandler", "onUserLeave: " + uid);
            runOnUiThread(() -> removeRemoteView(uid));
        }

        //接受消息回调
        @Override
        public void onRoomMessageSendResult(long msgid, int error) {
            super.onRoomMessageSendResult(msgid, error);
            mHandler.sendMessage(Message.obtain(mHandler, 1));
            mVCChatAdapter.notifyDataSetChanged();
            mVCChatRv.smoothScrollToPosition(mVCChatAdapter.getItemCount()- 1);
            Log.d("lfysendMessageID", String.valueOf(msgid));
            Log.d("lfySendMessageResult", String.valueOf(error));
        }

        @Override
        public void onRoomMessageReceived(String uid, String message) {
            super.onUserMessageReceived(uid, message);
            mHandler.sendMessage(Message.obtain(mHandler, 1));
            mVCChatAdapter.addChatMsg(uid + ": " + message);
            mVCChatAdapter.notifyDataSetChanged();
            mVCChatRv.smoothScrollToPosition(mVCChatAdapter.getItemCount()- 1);
            Log.d("lfyUserMessageRecevied", uid + " " + message);
        }


    };




    private IRTCVideoEventHandler mIRtcVideoEventHandler = new IRTCVideoEventHandler(){
        /**
         *
         * SDK收到第一帧远端视频解码数据之后，用户收到此回调
         */

        @Override
        public void onFirstRemoteVideoFrameDecoded(RemoteStreamKey remoteStreamKey, VideoFrameInfo frameInfo) {
            super.onFirstRemoteVideoFrameDecoded(remoteStreamKey, frameInfo);
            Log.d("IRTCVideoEventHandler", "onFirstRemoteVideoFrame: " + remoteStreamKey.toString());
            runOnUiThread(() -> setRemoteView(remoteStreamKey.getRoomId(), remoteStreamKey.getUserId()));
        }

        @Override
        public void onWarning(int warn){
            super.onWarning(warn);
            Log.d("IRTCVideoEventHandler","onWarning:" + warn);
        }

        @Override
        public void onError(int err){
            super.onError(err);
            Log.d("IRTCVideoEventHandler","onError:" + err);
            showAlertDialog(String.format(Locale.US, "error: %d",err));
        }

    };


    private void showAlertDialog(String message){
        runOnUiThread(()->{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message);
            builder.setPositiveButton("Got That", (dialog, which) ->dialog.dismiss());
            builder.create().show();
        });
    }

    private void setRemoteRenderView(String roomId, String uid, FrameLayout container){
        TextureView renderView = new TextureView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

//        mUserList.add(new User(renderView, params, uid));
//        FreshWidthHeight();
//        mUserAdapter.notifyDataSetChanged();
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
        //如果现在的list里有一个相同的人，把它踢掉
        //提 还是不提 这是一个问题
//        for(int i=0; i < mUserList.size(); ++i){
//            if (mUserList.get(i).mUid == uid){
//                if (i == 0){
//                    mRTCRoom.leaveRoom();//离开的操作
//                }
//                else{
//                    mUserList.remove(i);
//                    FreshWidthHeight();
//                    mUserAdapter.notifyDataSetChanged();
//                    break;
//                }
//            }
//        }

        //如果没有的话，可以加新用户了
        int emptyInx = -1;
        for (int i = 0; i < mShowUidArray.length; i++) {
            if (TextUtils.isEmpty(mShowUidArray[i]) && emptyInx == -1) {
                emptyInx = i;
            } else if (TextUtils.equals(uid, mShowUidArray[i])) {
                return;
            }
        }
        if (emptyInx < 0) {
            return;
        }
        mShowUidArray[emptyInx] = uid;
        mUserIdArray[emptyInx].setText(uid);
        setRemoteRenderView(roomId, uid, mRemoteContainerArray[emptyInx]);
        //setRemoteRenderView(roomId, uid);
    }


    private void removeRemoteView(String uid){
        for (int i=0; i < mShowUidArray.length; i++){
            if (TextUtils.equals(uid, mShowUidArray[i])){
                mShowUidArray[i] = null;
                mUserIdArray[i].setText(null);
                mRemoteContainerArray[i].removeAllViews();
            }
        }
//        for (int i=0; i < mUserList.size(); ++i){
//            if (Objects.equals(mUserList.get(i).mUid, uid)){
//                mUserList.remove(i);
//                FreshWidthHeight();
//                mUserAdapter.notifyDataSetChanged();
//                break;
//            }
//        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_room);

//        mUserRV = findViewById(R.id.user_rv);
//        mUserAdapter = new UserAdapter();
//        mUserRV.setAdapter(mUserAdapter);
//        mUserRV.setLayoutManager(new GridLayoutManager(Room.this,1));


        Button btn_LeaveRoom = (Button) findViewById(R.id.btn_leaveroom);
        btn_LeaveRoom.setOnClickListener((v)->{onBackPressed();
        });



        String roomId = (String)getIntent().getExtras().get(Constants.ROOM_ID_EXTRA);
        String userId = (String)getIntent().getExtras().get(Constants.USER_ID_EXTRA);
        Log.d("tag","hello!!!!!!!!!!!!" + userId);
        initUI(roomId, userId);
        initEngineAndJoinRoom(roomId, userId);

        Intent intent = getIntent();

        Boolean sxt = intent.getBooleanExtra("sxt", false);
        Boolean mkf = intent.getBooleanExtra("mkf", false);

        initGetMessage(userId);


    }

//    class UserAdapter extends RecyclerView.Adapter<UserHolder>{
//        @NonNull
//        @Override
//        public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
//            View view = View.inflate(Room.this, R.layout.item_user, null);
//            UserHolder myUserHolder = new UserHolder(view);
//            return myUserHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull UserHolder holder, int position){
//            User user = mUserList.get(position);
////            Log.i("tag", "the width is:" + String.valueOf(holder.avatar_layout.getWidth()));
////            Log.i("tag", "the height is :" + String.valueOf(holder.avatar_layout.getHeight()));
////            Log.i("tag","the width of mView(video) is:" + String.valueOf(user.mView.getWidth()));
////            Log.i("tag","the width of mView(video) is:" + String.valueOf(user.mView.getHeight()));
//
//            holder.avatar_layout.removeView(user.mView);
//            holder.avatar_layout.removeAllViews();
//            holder.avatar_layout.addView(user.mView, user.mWidth, user.mHeight);
//            holder.id_layout.setText(user.mUid);
//        }
//
//        @Override
//        public int getItemCount(){
//            return mUserList.size();
//        }
//
//    }

    class UserHolder extends RecyclerView.ViewHolder{
        FrameLayout avatar_layout;
        TextView id_layout;

        public UserHolder(@NonNull View itemView){
            super(itemView);
            avatar_layout = itemView.findViewById(R.id.user_layout);
            id_layout = itemView.findViewById(R.id.user_id);
        }
    }

    private void initUI(String roomId, String userId){
        micButton = (Button)this.findViewById(R.id.btn_mic);
        videoButton = (Button)this.findViewById(R.id.btn_video);

        micButton.setOnClickListener((v)-> updateLocalMicStatus());

        videoButton.setOnClickListener((v)-> updateLocalVideoStatus());

        findViewById(R.id.btn_switch_camera).setOnClickListener((v)->onSwitchCameraClick());

        mSelfContainer = findViewById(R.id.usr1_layout);
        mSelfUserId = findViewById(R.id.usr1_id);
        mRemoteContainerArray[0] = findViewById(R.id.usr2_layout);
        mRemoteContainerArray[1] = findViewById(R.id.usr3_layout);
        mRemoteContainerArray[2] = findViewById(R.id.usr4_layout);
        mRemoteContainerArray[3] = findViewById(R.id.usr5_layout);
        mRemoteContainerArray[4] = findViewById(R.id.usr6_layout);
        mRemoteContainerArray[5] = findViewById(R.id.usr7_layout);
        mRemoteContainerArray[6] = findViewById(R.id.usr8_layout);

        mUserIdArray[0] = findViewById(R.id.usr2_id);
        mUserIdArray[1] = findViewById(R.id.usr3_id);
        mUserIdArray[2] = findViewById(R.id.usr4_id);
        mUserIdArray[3] = findViewById(R.id.usr5_id);
        mUserIdArray[4] = findViewById(R.id.usr6_id);
        mUserIdArray[5] = findViewById(R.id.usr7_id);
        mUserIdArray[6] = findViewById(R.id.usr8_id);


        //add one user
        Log.d("e","Here the userId ought to be: " + userId);
    }



    private void onSwitchCameraClick(){
        if (mCameraID.equals(CameraId.CAMERA_ID_FRONT)) {
            mCameraID = CameraId.CAMERA_ID_BACK;
        } else {
            mCameraID = CameraId.CAMERA_ID_FRONT;
        }
        mRTCVideo.switchCamera(mCameraID);
    }


    private void updateLocalMicStatus(){
        mIsMuteAudio = !mIsMuteAudio;
        if (mIsMuteAudio){
            mRTCRoom.unpublishStream(MediaStreamType.RTC_MEDIA_STREAM_TYPE_AUDIO);
        }else{
            mRTCRoom.publishStream(MediaStreamType.RTC_MEDIA_STREAM_TYPE_VIDEO);
        }
    }


    private void updateLocalVideoStatus(){
        mIsMuteVideo = !mIsMuteVideo;
        if(mIsMuteVideo){
            mRTCVideo.stopVideoCapture();
        }else{
            mRTCVideo.startVideoCapture();
        }
    }

    private void initEngineAndJoinRoom(String roomId, String userId){
        int joinRoomRes;
        mRTCVideo = RTCVideo.createRTCVideo(getApplicationContext(), Constants.APPID, mIRtcVideoEventHandler, null, null);

//      VideoEncoderConfig videoEncoderConfig = new VideoEncoderConfig(182,180,15,800);
//      mRTCVideo.setVideoEncoderConfig(videoEncoderConfig);

//        mUserList.clear();
        setLocalRenderView(userId);

        mRTCVideo.startVideoCapture();
        mRTCVideo.startAudioCapture();
        mRTCRoom = mRTCVideo.createRTCRoom(roomId);
        mRTCRoom.setRTCRoomEventHandler(mIRtcRoomEventHandler);

        RTCRoomConfig roomConfig = new RTCRoomConfig(ChannelProfile.CHANNEL_PROFILE_COMMUNICATION,
                true, true, true);

        // here for the test!
        switch(userId){
            case "2":
                joinRoomRes = mRTCRoom.joinRoom(Constants.TOKEN_2, UserInfo.create(userId, ""), roomConfig);
                break;
            case "3":
                joinRoomRes = mRTCRoom.joinRoom(Constants.TOKEN_3, UserInfo.create(userId, ""), roomConfig);
                break;
            default:
                joinRoomRes = mRTCRoom.joinRoom(Constants.TOKEN_1, UserInfo.create(userId, ""), roomConfig);
                break;
        }
        Log.i("tag","initEngineAndJoinRoom:" + joinRoomRes);

    }

    private void setLocalRenderView(String uid){

        VideoCanvas videoCanvas = new VideoCanvas();
        TextureView renderView = new TextureView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(500,500);
//        mUserList.add(new User(renderView, params, uid));
//        FreshWidthHeight();
//        mUserAdapter.notifyDataSetChanged();

        mSelfContainer.removeAllViews();
        mSelfContainer.addView(renderView, params);
        mSelfUserId.setText(uid);

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
