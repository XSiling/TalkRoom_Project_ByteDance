package com.ss.video.rtc.demo.quickstart;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Camera;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.ss.bytertc.engine.VideoEncoderConfig;
import com.ss.bytertc.engine.data.CameraId;
import com.ss.bytertc.engine.data.EffectBeautyMode;
import com.ss.bytertc.engine.data.RemoteStreamKey;
import com.ss.bytertc.engine.data.ScreenMediaType;
import com.ss.bytertc.engine.data.StreamIndex;
import com.ss.bytertc.engine.data.VideoFrameInfo;
import com.ss.bytertc.engine.data.VideoSourceType;
import com.ss.bytertc.engine.handler.IRTCVideoEventHandler;
import com.ss.bytertc.engine.type.ChannelProfile;
import com.ss.bytertc.engine.type.MediaDeviceState;
import com.ss.bytertc.engine.type.MediaStreamType;
import com.ss.bytertc.engine.type.RTCRoomStats;
import com.ss.bytertc.engine.type.StreamRemoveReason;
import com.ss.bytertc.engine.type.VideoDeviceType;
import com.ss.rtc.demo.quickstart.R;

import org.webrtc.RXScreenCaptureService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Room extends AppCompatActivity {


    public static final int REQUEST_CODE_OF_SCREEN_SHARING = 101;

    private RecyclerView mUserRV;
    private List<User> mUserList = new ArrayList<>();
    private UserAdapter mUserAdapter;

    private FrameLayout mSelfContainer;
    private FrameLayout shareScreenContainer;
    private TextView mSelfUserId;
    private final FrameLayout[] mRemoteContainerArray = new FrameLayout[7];
    private final TextView[] mUserIdArray = new TextView[7];
    private final String[] mShowUidArray = new String[7];

    private ImageButton videoButton;
    private ImageButton micButton;
    private ImageButton chatButton;
    private ImageButton setButton;
    private Button setCheck;
    private Button setCancel;
    private ImageButton shareScreenBtn;

    private float whiteValue = 0;
    private float smoothValue = 0;
    //private boolean share = false;


    private SeekBar whiteBar;
    private SeekBar smoothBar;

    //some parameters related to audio/video switch
    private boolean mIsMuteAudio = false;
    private boolean mIsMuteVideo = false;
    private boolean mIsMuteChat = true;
    private boolean mIsSharingScreen = false;
    private static String StopShareScreenFlagString = "#!Stop Sharing Screen!#";
    private static String StartShareScreenFlagString = "#!Start Sharing Screen!#";

    private RTCVideo mRTCVideo;
    private RTCRoom mRTCRoom;

    private CameraId mCameraID = CameraId.CAMERA_ID_FRONT;

    private FrameLayout setBox;

    private String userId;

    private Timer timer;
    private boolean Start;
    private int cnt;

    private RecyclerView mVCChatRv;
    private final List<String> itemList = new ArrayList<>();
    private VCChatAdapter mVCChatAdapter;

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


    private void setWH(int position, int width, int height){
        mUserList.get(position).setmWidth(width);
        mUserList.get(position).setmHeight(height);
    }

    private void FreshWidthHeight(boolean mIsSharingScreen){
        //不在这个函数里notify change
        int totalWidth;
        int totalHeight;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Room.this, 4);

        if (mIsSharingScreen){
            totalWidth = this.getWindowManager().getDefaultDisplay().getWidth()/2;
            totalHeight = this.getWindowManager().getDefaultDisplay().getHeight() * 4 / 5;
        }else{
            totalWidth = this.getWindowManager().getDefaultDisplay().getWidth();
            totalHeight = this.getWindowManager().getDefaultDisplay().getHeight() * 4 / 5;
        }

        int userNum = mUserList.size();
        switch (userNum){
            case 1:
                setWH(0, totalWidth, totalHeight);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return 4;
                    }
                });
                break;
            case 2:
                setWH(0, totalWidth/2, totalHeight);
                setWH(1, totalWidth/2, totalHeight);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return 2;
                    }
                });
                break;
            case 3:
                setWH(0, totalWidth, totalHeight/2);
                setWH(1, totalWidth/2, totalHeight/2);
                setWH(2, totalWidth/2, totalHeight/2);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        switch(position){
                            case 0:
                                return 4;
                            default:
                                return 2;
                        }
                    }
                });
                break;
            case 4:
                setWH(0, totalWidth/2, totalHeight/2);
                setWH(1, totalWidth/2, totalHeight/2);
                setWH(2, totalWidth/2, totalHeight/2);
                setWH(3, totalWidth/2, totalHeight/2);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return 2;
                    }
                });
                break;
            case 5:
                setWH(0, totalWidth, totalHeight/2);
                setWH(1, totalWidth/4, totalHeight/2);
                setWH(2, totalWidth/4, totalHeight/2);
                setWH(3, totalWidth/4, totalHeight/2);
                setWH(4, totalWidth/4, totalHeight/2);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        switch(position){
                            case 0:
                                return 4;
                            default:
                                return 1;
                        }
                    }
                });
                break;
            case 6:
                setWH(0, totalWidth/2, totalHeight/2);
                setWH(1, totalWidth/2, totalHeight/2);
                setWH(2, totalWidth/4, totalHeight/2);
                setWH(3, totalWidth/4, totalHeight/2);
                setWH(4, totalWidth/4, totalHeight/2);
                setWH(5, totalWidth/4, totalHeight/2);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        switch(position){
                            case 0:
                            case 1:
                                return 2;
                            default:
                                return 1;
                        }
                    }
                });
                break;

            case 7:
                setWH(0, totalWidth/4, totalHeight/2);
                setWH(1, totalWidth/4, totalHeight/2);
                setWH(2, totalWidth/4, totalHeight/2);
                setWH(3, totalWidth/4, totalHeight/2);
                setWH(4, totalWidth/4, totalHeight/2);
                setWH(5, totalWidth/4, totalHeight/2);
                setWH(6, totalWidth/4, totalHeight/2);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return 1;
                    }
                });
                break;
            case 8:
                setWH(0, totalWidth/4, totalHeight/2);
                setWH(1, totalWidth/4, totalHeight/2);
                setWH(2, totalWidth/4, totalHeight/2);
                setWH(3, totalWidth/4, totalHeight/2);
                setWH(4, totalWidth/4, totalHeight/2);
                setWH(5, totalWidth/4, totalHeight/2);
                setWH(6, totalWidth/4, totalHeight/2);
                setWH(7, totalWidth/4, totalHeight/2);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return 1;
                    }
                });
                break;
        }
        mUserRV.setLayoutManager(gridLayoutManager);
    }

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


        @Override
        public void onUserPublishScreen(String uid, MediaStreamType type) {
            Log.i("Hello," , "onUserPublishScreen");
            super.onUserPublishScreen(uid, type);
            runOnUiThread(()->setRemoteRenderView1(uid,shareScreenContainer));
        }


        @Override
        public void onUserUnpublishScreen(String uid, MediaStreamType type, StreamRemoveReason reason) {
            super.onUserUnpublishScreen(uid, type, reason);
            runOnUiThread(() -> removeRemoteView(uid));
        }

        @Override
        public void onRoomMessageSendResult(long msgid, int error) {
            super.onRoomMessageSendResult(msgid, error);
            mHandler.sendMessage(Message.obtain(mHandler, 1));
            mVCChatAdapter.notifyDataSetChanged();
            mVCChatRv.smoothScrollToPosition(mVCChatAdapter.getItemCount()- 1);
        }

        @Override
        public void onRoomMessageReceived(String uid, String message) {
            super.onUserMessageReceived(uid, message);
            mHandler.sendMessage(Message.obtain(mHandler, 1));
            if (message.equals(StopShareScreenFlagString)){
                Log.i("jy Trace L419", "Stop Sharing");
                mIsSharingScreen = false;
                updateShareScreenContainer();

                runOnUiThread(()->setRemoteRenderView1(uid,shareScreenContainer));

                /*
                if(shareScreenContainer.getVisibility() == View.VISIBLE)
                    shareScreenContainer.setVisibility(View.GONE);
                    */

                /*
                FreshWidthHeight(mIsSharingScreen);
                mUserAdapter.notifyDataSetChanged();*/
            }else if(message.equals(StartShareScreenFlagString)){
                Log.i("jy Trace L431","Start Sharing");
                mIsSharingScreen = true;
                updateShareScreenContainer();

                runOnUiThread(()->setRemoteRenderView1(uid,shareScreenContainer));
                /*
                if(shareScreenContainer.getVisibility() == View.GONE)
                    shareScreenContainer.setVisibility(View.VISIBLE);*/
                /*
                FreshWidthHeight(mIsSharingScreen);
                mUserAdapter.notifyDataSetChanged();*/
            }else {
                mVCChatAdapter.addChatMsg(uid + ": " + message);
                mVCChatAdapter.notifyDataSetChanged();
                mVCChatRv.smoothScrollToPosition(mVCChatAdapter.getItemCount() - 1);
            }
        }
    };

    private IRTCVideoEventHandler mIRtcVideoEventHandler = new IRTCVideoEventHandler(){
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

    private void setRemoteRenderView(String roomId, String uid){
        TextureView renderView = new TextureView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        for(int i=0;i<mUserList.size();++i){
            if (mUserList.get(i).mUid.equals(uid))
            {
                Log.d("tag","an user already in");
                return;
            }
        }
        mUserList.add(new User(renderView, params, uid));
        FreshWidthHeight(mIsSharingScreen);
        //if(mIsSharingScreen)

        mUserAdapter.notifyDataSetChanged();

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
        for(int i=0; i < mUserList.size(); ++i){
            if (mUserList.get(i).mUid == uid){
                if (i == 0){
                    mRTCRoom.leaveRoom();//离开的操作
                }
                else{
                    mUserList.remove(i);
                    FreshWidthHeight(mIsSharingScreen);
                    mUserAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
        setRemoteRenderView(roomId, uid);
    }


    private void removeRemoteView(String uid){
        for (int i=0; i < mUserList.size(); ++i){
            if (Objects.equals(mUserList.get(i).mUid, uid)){
                mUserList.remove(i);
                FreshWidthHeight(mIsSharingScreen);
                mUserAdapter.notifyDataSetChanged();
                break;
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_room);

        mUserRV = findViewById(R.id.user_rv);
        mUserAdapter = new UserAdapter();
        mUserRV.setAdapter(mUserAdapter);
        mUserRV.setLayoutManager(new GridLayoutManager(Room.this,1));

        Intent intent = getIntent();
        String roomId = intent.getStringExtra(Constants.ROOM_ID_EXTRA);
        userId = intent.getStringExtra(Constants.USER_ID_EXTRA);

        Log.d("tag","hello!!!!!!!!!!!!" + userId);
        initUI(roomId, userId);
        initEngineAndJoinRoom(roomId, userId);

        initGetMessage(userId);
    }


    private void makeSet(){
        setBox.setVisibility(View.VISIBLE);
    }

    private void checkSet(){
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        Log.d("tag", String.valueOf(rg.getCheckedRadioButtonId()));
        if (rg.getCheckedRadioButtonId()==R.id.radioButton1){
            if (mCameraID == CameraId.CAMERA_ID_BACK){
                onSwitchCameraClick();
            }
        }
        else{
            if (mCameraID == CameraId.CAMERA_ID_FRONT){
                onSwitchCameraClick();
            }
        }

        whiteValue = (float)whiteBar.getProgress()/100;
        smoothValue = (float)smoothBar.getProgress()/100;

        int r1 = mRTCVideo.setBeautyIntensity(EffectBeautyMode.EFFECT_WHITE_MODE, whiteValue);
        int r2 = mRTCVideo.setBeautyIntensity(EffectBeautyMode.EFFECT_SMOOTH_MODE, smoothValue);
        Log.d("tag", String.valueOf(r1));
        Log.d("tag", String.valueOf(r2));
//        mRTCVideo.enableEffectBeauty(true);

        setBox.setVisibility(View.GONE);
    }

//    @SuppressLint("ResourceType")
    private void cancelSet(){
        float white;
        float smooth;
        setBox.setVisibility(View.GONE);
        RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup);
        if (mCameraID == CameraId.CAMERA_ID_FRONT){
            rg.check(R.id.radioButton1);
        }
        if (mCameraID == CameraId.CAMERA_ID_BACK){
            rg.check(R.id.radioButton2);
        }

        white = whiteValue * 100;
        smooth = smoothValue * 100;

        whiteBar.setProgress((int)white);
        smoothBar.setProgress((int)smooth);
//        rg.check(0);
    }
    class UserAdapter extends RecyclerView.Adapter<UserHolder>{
        @NonNull
        @Override
        public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view = View.inflate(Room.this, R.layout.item_user, null);
            UserHolder myUserHolder = new UserHolder(view);
            return myUserHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull UserHolder holder, int position){
            User user = mUserList.get(position);
//            Log.i("tag", "the width is:" + String.valueOf(holder.avatar_layout.getWidth()));
//            Log.i("tag", "the height is :" + String.valueOf(holder.avatar_layout.getHeight()));
//            Log.i("tag","the width of mView(video) is:" + String.valueOf(user.mView.getWidth()));
//            Log.i("tag","the width of mView(video) is:" + String.valueOf(user.mView.getHeight()));
            Log.d("tag","0");
            holder.avatar_layout.removeView(user.mView);
            Log.d("tag","1");
            holder.avatar_layout.removeAllViews();
            Log.d("tag","2");

            if ((FrameLayout)user.mView.getParent()!=null){
                Log.d("tag","here");
                ((FrameLayout)user.mView.getParent()).removeView(user.mView);
            }



            holder.avatar_layout.addView(user.mView, user.mWidth, user.mHeight);
            Log.d("tag","3");
            holder.id_layout.setText(user.mUid);
        }

        @Override
        public int getItemCount(){
            return mUserList.size();
        }

    }

    class UserHolder extends RecyclerView.ViewHolder{
        FrameLayout avatar_layout;
        TextView id_layout;

        public UserHolder(@NonNull View itemView){
            super(itemView);
            avatar_layout = itemView.findViewById(R.id.user_layout);
            id_layout = itemView.findViewById(R.id.user_id);
        }
    }


    // 向系统发起屏幕共享的权限请求
    public void requestForScreenSharing() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        MediaProjectionManager projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (projectionManager != null) {
            startActivityForResult(projectionManager.createScreenCaptureIntent(), REQUEST_CODE_OF_SCREEN_SHARING);
        } else {
            Log.e("ShareScreen","当前系统版本过低，无法支持屏幕共享");
        }
    }

    private void initUI(String roomId, String userId){
        micButton = (ImageButton)this.findViewById(R.id.btn_mic);
        videoButton = (ImageButton)this.findViewById(R.id.btn_video);
        chatButton = (ImageButton)this.findViewById(R.id.btn_chat);
        shareScreenContainer = findViewById(R.id.shareScreenView);
        shareScreenBtn = findViewById(R.id.btn_share_screen);
//        mclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(share) {
//                    share = false;
//                    mRTCVideo.stopScreenCapture();
//                  //  shareScreenContainer.setVisibility(View.INVISIBLE);
//                    mclose.setVisibility(View.INVISIBLE);
//                }
//            }
//        });


        micButton.setOnClickListener((v)-> updateLocalMicStatus());

        videoButton.setOnClickListener((v)-> updateLocalVideoStatus());

        chatButton.setOnClickListener((v)->updateChatStatus());

        ImageButton btn_LeaveRoom = (ImageButton) findViewById(R.id.btn_leaveroom);
        setButton = findViewById(R.id.btn_set);
        setBox = findViewById(R.id.setBox);
        setBox.setVisibility(View.GONE);
        setCheck = findViewById(R.id.btn_set_check);
        setCancel = findViewById(R.id.btn_set_cancel);
        whiteBar = findViewById(R.id.beautySeekWhite);
        smoothBar = findViewById(R.id.beautySeekSmooth);

        setButton.setOnClickListener((v)->makeSet());
        setCheck.setOnClickListener((v)->checkSet());
        setCancel.setOnClickListener((v)->cancelSet());
        btn_LeaveRoom.setOnClickListener((v)->{onBackPressed();
        });
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
            mUserList.get(0).setmUid(userId + "（静音中）");
            mUserAdapter.notifyDataSetChanged();
        }else{
            mRTCRoom.publishStream(MediaStreamType.RTC_MEDIA_STREAM_TYPE_VIDEO);
            mUserList.get(0).setmUid(userId);
            mUserAdapter.notifyDataSetChanged();

        }
    }

    private void updateChatStatus(){
        mIsMuteChat = !mIsMuteChat;
        if (mIsMuteChat){
            this.findViewById(R.id.voice_chat_demo_main_input_layout).setVisibility(View.GONE);
        }else{
            this.findViewById(R.id.voice_chat_demo_main_input_layout).setVisibility(View.VISIBLE);
        }
    }
    private void updateShareScreenContainer() {
        if(mIsSharingScreen){
            Log.i("jy Trace L757","Set Share Visible");
            //this.findViewById(R.id.shareScreenView).setVisibility(View.VISIBLE);
        }else{

            Log.i("jy Trace L757","Set Share Gone");
            //this.findViewById(R.id.shareScreenView).setVisibility(View.GONE);
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

        VideoEncoderConfig videoEncoderConfig = new VideoEncoderConfig(360,640,15,800);
        mRTCVideo.setVideoEncoderConfig(videoEncoderConfig);

//        mUserList.clear();
        setLocalRenderView(userId);

        mRTCVideo.setBeautyIntensity(EffectBeautyMode.EFFECT_WHITE_MODE,0);//美白EFFECT_WHITE_MODE(0)	美白
        mRTCVideo.setBeautyIntensity(EffectBeautyMode.EFFECT_SMOOTH_MODE,0);
        mRTCVideo.setBeautyIntensity(EffectBeautyMode.EFFECT_SHARPEN_MODE, 0);
        mRTCVideo.enableEffectBeauty(true);


        mRTCVideo.startVideoCapture();
        mRTCVideo.startAudioCapture();
        mRTCRoom = mRTCVideo.createRTCRoom(roomId);
        mRTCRoom.setRTCRoomEventHandler(mIRtcRoomEventHandler);

        RTCRoomConfig roomConfig = new RTCRoomConfig(ChannelProfile.CHANNEL_PROFILE_COMMUNICATION,
                true, true, true);

        // here for the test!
        switch(userId){
            case "1":
                joinRoomRes = mRTCRoom.joinRoom(Constants.TOKEN_1, UserInfo.create(userId, ""), roomConfig);
                break;
            case "2":
                joinRoomRes = mRTCRoom.joinRoom(Constants.TOKEN_2, UserInfo.create(userId, ""), roomConfig);
                break;
            case "3":
                joinRoomRes = mRTCRoom.joinRoom(Constants.TOKEN_3, UserInfo.create(userId, ""), roomConfig);
                break;
            case "4":
                joinRoomRes = mRTCRoom.joinRoom(Constants.TOKEN_4, UserInfo.create(userId, ""), roomConfig);
                break;
            case "5":
                joinRoomRes = mRTCRoom.joinRoom(Constants.TOKEN_5, UserInfo.create(userId, ""), roomConfig);
                break;
            default:
                joinRoomRes = mRTCRoom.joinRoom(Constants.TOKEN_6, UserInfo.create(userId, ""), roomConfig);
                break;
        }
//        joinRoomRes = mRTCRoom.joinRoom(Constants.TOKEN_3, UserInfo.create(userId, ""), roomConfig);
        Log.i("tag","initEngineAndJoinRoom:" + joinRoomRes);
        requestForScreenSharing();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_OF_SCREEN_SHARING:
                shareScreenBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mIsSharingScreen) {
                            //关闭分享
                            mIsSharingScreen = false;
                            mRTCRoom.sendRoomMessage(StopShareScreenFlagString);
                            mRTCVideo.stopScreenCapture();
                        }else{
                            mIsSharingScreen = true;
                            startScreenShare(data);
                            mRTCRoom.sendRoomMessage(StartShareScreenFlagString);
                            mRTCRoom.publishScreen(MediaStreamType.RTC_MEDIA_STREAM_TYPE_BOTH);
                            mRTCVideo.setVideoSourceType(
                                    StreamIndex.STREAM_INDEX_SCREEN,
                                    VideoSourceType.VIDEO_SOURCE_TYPE_INTERNAL);
                        }


                    }
                });

        }
    }

    private void startScreenShare(Intent data) {
        startRXScreenCaptureService(data);
        //编码参数
        VideoEncoderConfig config = new VideoEncoderConfig();
        config.width = 720;
        config.height = 640;
        config.frameRate = 15;
        config.maxBitrate = 1600;
        mRTCVideo.setScreenVideoEncoderConfig(config);

        // 开启屏幕视频数据采集
        mRTCVideo.startScreenCapture(ScreenMediaType.SCREEN_MEDIA_TYPE_VIDEO_AND_AUDIO, data);
    }
    private void startRXScreenCaptureService(@NonNull Intent data) {
        Context context = getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent intent = new Intent();
            intent.putExtra(RXScreenCaptureService.KEY_LARGE_ICON, R.drawable.launcher_quick_start);
            intent.putExtra(RXScreenCaptureService.KEY_SMALL_ICON, R.drawable.launcher_quick_start);
            intent.putExtra(RXScreenCaptureService.KEY_LAUNCH_ACTIVITY, this.getClass().getCanonicalName());
            intent.putExtra(RXScreenCaptureService.KEY_CONTENT_TEXT, "正在录制/投射您的屏幕");
            intent.putExtra(RXScreenCaptureService.KEY_RESULT_DATA, data);
            context.startForegroundService(RXScreenCaptureService.getServiceIntent(context, RXScreenCaptureService.COMMAND_LAUNCH, intent));
        }
    }

    private void setRemoteRenderView1(String uid, FrameLayout container) {
        TextureView renderView = new TextureView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        container.removeAllViews();
        container.addView(renderView, params);
        if(mIsSharingScreen) {
            Log.i("jy Trace:", "RenderRemote Share Visible");
            container.setVisibility(View.VISIBLE);
        }
        else {
            Log.i("jy Trace:", "RenderRemote Share Gone");
            container.setVisibility(View.GONE);
        }
        VideoCanvas videoCanvas = new VideoCanvas();
        videoCanvas.renderView = renderView;
        videoCanvas.roomId = getIntent().getStringExtra(Constants.ROOM_ID_EXTRA);
        videoCanvas.isScreen=true;
        videoCanvas.uid =uid;
        videoCanvas.renderMode = VideoCanvas.RENDER_MODE_Fill;
        // 设置远端用户视频渲染视图
        mRTCVideo.setRemoteVideoCanvas(uid, StreamIndex.STREAM_INDEX_SCREEN, videoCanvas);
    }


    private void setRemoteRenderView_Unpublish(String uid, FrameLayout container) {
        TextureView renderView = new TextureView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        container.removeAllViews();
        container.addView(renderView, params);
        container.setVisibility(View.INVISIBLE);

        VideoCanvas videoCanvas = new VideoCanvas();
        videoCanvas.renderView = renderView;
        videoCanvas.roomId = getIntent().getStringExtra(Constants.ROOM_ID_EXTRA);
        videoCanvas.isScreen=true;
        videoCanvas.uid =uid;
        videoCanvas.renderMode = VideoCanvas.RENDER_MODE_Fill;
        // 设置远端用户视频渲染视图
        mRTCVideo.setRemoteVideoCanvas(uid, StreamIndex.STREAM_INDEX_SCREEN, videoCanvas);

    }

    private void setLocalRenderView(String uid){

        VideoCanvas videoCanvas = new VideoCanvas();
        TextureView renderView = new TextureView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mUserList.add(new User(renderView, params, uid));
        FreshWidthHeight(mIsSharingScreen);
        mUserAdapter.notifyDataSetChanged();

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
