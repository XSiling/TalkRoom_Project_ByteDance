package com.ss.video.rtc.demo.quickstart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ss.bytertc.engine.RTCEngine;
import com.ss.bytertc.engine.RTCRoom;
import com.ss.bytertc.engine.RTCRoomConfig;
import com.ss.bytertc.engine.RTCVideo;
import com.ss.bytertc.engine.UserInfo;
import com.ss.bytertc.engine.VideoCanvas;
import com.ss.bytertc.engine.VideoEncoderConfig;
import com.ss.bytertc.engine.data.AudioRoute;
import com.ss.bytertc.engine.data.CameraId;
import com.ss.bytertc.engine.data.RemoteStreamKey;
import com.ss.bytertc.engine.data.ScreenMediaType;
import com.ss.bytertc.engine.data.StreamIndex;
import com.ss.bytertc.engine.data.VideoFrameInfo;
import com.ss.bytertc.engine.data.VideoSourceType;
import com.ss.bytertc.engine.handler.IRTCEngineEventHandler;
import com.ss.bytertc.engine.handler.IRTCVideoEventHandler;
import com.ss.bytertc.engine.type.ChannelProfile;
import com.ss.bytertc.engine.type.LocalVideoStreamError;
import com.ss.bytertc.engine.type.LocalVideoStreamState;
import com.ss.bytertc.engine.type.MediaDeviceState;
import com.ss.bytertc.engine.type.MediaStreamType;
import com.ss.bytertc.engine.type.RemoteVideoState;
import com.ss.bytertc.engine.type.RemoteVideoStateChangeReason;
import com.ss.bytertc.engine.type.StreamRemoveReason;
import com.ss.bytertc.engine.type.VideoDeviceType;
import com.ss.rtc.demo.quickstart.R;

import org.webrtc.RXScreenCaptureService;

import java.io.Closeable;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class RTCRoomActivity extends AppCompatActivity {

    private ImageView mscrenIv;
    private ImageView mclose;

    private FrameLayout mSelfContainer;
    private FrameLayout viewvideo;
    private final FrameLayout[] mRemoteContainerArray = new FrameLayout[3];
    private final TextView[] mUserIdTvArray = new TextView[3];
    private final String[] mShowUidArray = new String[3];

    private RTCVideo mRTCVideo;
    private RTCRoom mRTCRoom;
    private  String userId;

    private RTCRoomEventHandlerAdapter mIRtcRoomEventHandler = new RTCRoomEventHandlerAdapter() {

        /**
         * 远端主播角色用户加入房间回调。
         */
        @Override
        public void onUserJoined(UserInfo userInfo, int elapsed) {
            super.onUserJoined(userInfo, elapsed);
            Log.d("IRTCRoomEventHandler", "onUserJoined: " + userInfo.getUid());
        }

        @Override
        public void onUserPublishScreen(String uid, MediaStreamType type) {
            super.onUserPublishScreen(uid, type);
            runOnUiThread(()->setRemoteRenderView1(uid,viewvideo));
        }

        /**
         * 远端用户离开房间回调。
         */
        @Override
        public void onUserLeave(String uid, int reason) {
            super.onUserLeave(uid, reason);
            Log.d("IRTCRoomEventHandler", "onUserLeave: " + uid);
            runOnUiThread(() -> removeRemoteView(uid));
        }

        @Override
        public void onUserUnpublishScreen(String uid, MediaStreamType type, StreamRemoveReason reason) {
            super.onUserUnpublishScreen(uid, type, reason);
            viewvideo.setVisibility(View.INVISIBLE);
            runOnUiThread(() -> removeRemoteView(uid));
            mclose.setVisibility(View.INVISIBLE);
        }
    };

    private IRTCVideoEventHandler mIRtcVideoEventHandler = new IRTCVideoEventHandler() {
        /**
         * SDK收到第一帧远端视频解码数据后，用户收到此回调。
         */
        @Override
        public void onFirstRemoteVideoFrameDecoded(RemoteStreamKey remoteStreamKey, VideoFrameInfo frameInfo) {
            super.onFirstRemoteVideoFrameDecoded(remoteStreamKey, frameInfo);
            Log.d("IRTCVideoEventHandler", "onFirstRemoteVideoFrame: " + remoteStreamKey.toString());
            runOnUiThread(() -> setRemoteView(remoteStreamKey.getRoomId(), remoteStreamKey.getUserId()));

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Intent intent = getIntent();
        String roomId = intent.getStringExtra(Constants.ROOM_ID_EXTRA);
        userId = intent.getStringExtra(Constants.USER_ID_EXTRA);
        initUI(roomId, userId);
        initEngineAndJoinRoom(roomId, userId);
    }

    private void initUI(String roomId, String userId) {
        mSelfContainer = findViewById(R.id.self_video_container);
        mRemoteContainerArray[0] = findViewById(R.id.remote_video_0_container);
        mRemoteContainerArray[1] = findViewById(R.id.remote_video_1_container);
        mRemoteContainerArray[2] = findViewById(R.id.remote_video_2_container);
        viewvideo=findViewById(R.id.videoview);
        mUserIdTvArray[0] = findViewById(R.id.remote_video_0_user_id_tv);
        mUserIdTvArray[1] = findViewById(R.id.remote_video_1_user_id_tv);
        mUserIdTvArray[2] = findViewById(R.id.remote_video_2_user_id_tv);
        mscrenIv=findViewById(R.id.screen_share_off);

        mclose=findViewById(R.id.close);
        mclose.setVisibility(View.INVISIBLE);

        findViewById(R.id.hang_up).setOnClickListener((v) -> onBackPressed());
        TextView roomIDTV = findViewById(R.id.room_id_text);
        TextView userIDTV = findViewById(R.id.self_video_user_id_tv);
        roomIDTV.setText(String.format("RoomID:%s", roomId));
        userIDTV.setText(String.format("UserID:%s", userId));
        mclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRTCVideo.stopScreenCapture();
                viewvideo.setVisibility(View.INVISIBLE);
                mclose.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initEngineAndJoinRoom(String roomId, String userId) {
        // 创建引擎
        mRTCVideo = RTCVideo.createRTCVideo(getApplicationContext(), Constants.APPID, mIRtcVideoEventHandler, null, null);
        // 设置视频发布参数
        VideoEncoderConfig videoEncoderConfig = new VideoEncoderConfig(360, 640, 15, 800);
        mRTCVideo.setVideoEncoderConfig(videoEncoderConfig);
        setLocalRenderView(userId);
        // 开启本地视频采集
        mRTCVideo.startVideoCapture();
        // 开启本地音频采集
        mRTCVideo.startAudioCapture();
        // 加入房间
        mRTCRoom = mRTCVideo.createRTCRoom(roomId);
        mRTCRoom.setRTCRoomEventHandler(mIRtcRoomEventHandler);
        RTCRoomConfig roomConfig = new RTCRoomConfig(ChannelProfile.CHANNEL_PROFILE_COMMUNICATION,
                true, true, true);
        int joinRoomRes;

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
        requestForScreenSharing();
    }

    public static final int REQUEST_CODE_OF_SCREEN_SHARING = 101;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_OF_SCREEN_SHARING:
                mscrenIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startScreenShare(data);
                        mRTCRoom.publishScreen(MediaStreamType.RTC_MEDIA_STREAM_TYPE_BOTH);
                        mclose.setVisibility(View.VISIBLE);
                        mRTCVideo.setVideoSourceType(
                                StreamIndex.STREAM_INDEX_SCREEN,
                                VideoSourceType.VIDEO_SOURCE_TYPE_INTERNAL);
                    }
                });

        }
    }

    private void setLocalRenderView(String uid) {
        VideoCanvas videoCanvas = new VideoCanvas();
        TextureView renderView = new TextureView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mSelfContainer.removeAllViews();
        mSelfContainer.addView(renderView, params);
        videoCanvas.renderView = renderView;
        videoCanvas.uid = uid;
        videoCanvas.isScreen = false;
        videoCanvas.renderMode = VideoCanvas.RENDER_MODE_HIDDEN;
        // 设置本地视频渲染视图
        mRTCVideo.setLocalVideoCanvas(StreamIndex.STREAM_INDEX_MAIN, videoCanvas);
    }

    private void setRemoteRenderView(String roomId, String uid, FrameLayout container) {
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
        // 设置远端用户视频渲染视图
        mRTCVideo.setRemoteVideoCanvas(uid, StreamIndex.STREAM_INDEX_MAIN, videoCanvas);
    }

    private void setRemoteView(String roomId, String uid) {
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
        mUserIdTvArray[emptyInx].setText(String.format("UserId:%s", uid));
        setRemoteRenderView(roomId, uid, mRemoteContainerArray[emptyInx]);
    }

    private void setRemoteRenderView1(String uid, FrameLayout container) {
        TextureView renderView = new TextureView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        container.removeAllViews();
        container.addView(renderView, params);
        VideoCanvas videoCanvas = new VideoCanvas();
        videoCanvas.renderView = renderView;
        videoCanvas.roomId = getIntent().getStringExtra(Constants.ROOM_ID_EXTRA);
        videoCanvas.isScreen=true;
        videoCanvas.uid =uid;
        videoCanvas.renderMode = VideoCanvas.RENDER_MODE_Fill;
        // 设置远端用户视频渲染视图
        mRTCVideo.setRemoteVideoCanvas(uid, StreamIndex.STREAM_INDEX_SCREEN, videoCanvas);
    }

    private void removeRemoteView(String uid) {
        for (int i = 0; i < mShowUidArray.length; i++) {
            if (TextUtils.equals(uid, mShowUidArray[i])) {
                mShowUidArray[i] = null;
                mUserIdTvArray[i].setText(null);
                mRemoteContainerArray[i].removeAllViews();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        // 离开房间
        if (mRTCRoom != null) {
            mRTCRoom.leaveRoom();
            mRTCRoom.destroy();
        }
        mRTCRoom = null;
        // 销毁引擎
        RTCVideo.destroyRTCVideo();
        mIRtcVideoEventHandler = null;
        mIRtcRoomEventHandler = null;
        mRTCVideo = null;
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
}