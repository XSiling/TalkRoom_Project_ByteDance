package com.ss.video.rtc.demo.quickstart;

/**
 * VolcEngineRTC 常量定义
 */
public class Constants {

    //APPID 使用SDK前需要为自己的应用申请一个AppId，详情参见{https://www.volcengine.com/docs/6348/69865}
    public static final String APPID = "634a63b0ab430d0158d27c85";

    //TOKEN 加入房间的时候需要使用token完成鉴权，详情参见{https://www.volcengine.com/docs/6348/70121}
    public static final String TOKEN_1 = "001634a63b0ab430d0158d27c85OwCXXFIFOddhY7kRa2MEADEyMzQBADEGAAAAuRFrYwEAuRFrYwIAuRFrYwMAuRFrYwQAuRFrYwUAuRFrYyAAloyOrHRIdelpApwuv131cpx+u0A+wH0V2zSvreu1Fpk=";


    public static final String TOKEN_2 = "001634a63b0ab430d0158d27c85OwBk6hIEYNdhY+ARa2MEADEyMzQBADIGAAAA4BFrYwEA4BFrYwIA4BFrYwMA4BFrYwQA4BFrYwUA4BFrYyAAiZgXp2A6MuoKc2Tf1AlAZ7hg72lt9QX5OK04445VRd8=";

    public static final String TOKEN_3 = "001634a63b0ab430d0158d27c85OwDXloYBbNdhY+wRa2MEADEyMzQBADMGAAAA7BFrYwEA7BFrYwIA7BFrYwMA7BFrYwQA7BFrYwUA7BFrYyAAYGAoOKruueVEgk0RMYLkqJGzq9owYXRYwE4GR+C56jU=";

    //INPUT_REGEX SDK 对房间名、用户名的限制是：非空且最大长度不超过128位的数字、大小写字母、@ . _ -
    public static final String INPUT_REGEX = "^[a-zA-Z0-9@._-]{1,128}$";

    public static final String ROOM_ID_EXTRA = "extra_room_id";

    public static final String USER_ID_EXTRA = "extra_user_id";
}
