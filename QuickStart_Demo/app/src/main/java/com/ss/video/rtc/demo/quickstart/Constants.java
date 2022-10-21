package com.ss.video.rtc.demo.quickstart;

/**
 * VolcEngineRTC 常量定义
 */
public class Constants {

    //APPID 使用SDK前需要为自己的应用申请一个AppId，详情参见{https://www.volcengine.com/docs/6348/69865}
    public static final String APPID = "634a63b0ab430d0158d27c85";

    //TOKEN 加入房间的时候需要使用token完成鉴权，详情参见{https://www.volcengine.com/docs/6348/70121}
    public static final String TOKEN = "001634a63b0ab430d0158d27c85OwAmlvwEw6VSY0PgW2MEADEyMzQBADEGAAAAQ+BbYwEAQ+BbYwIAQ+BbYwMAQ+BbYwQAQ+BbYwUAQ+BbYyAA1zRK96gjxdOmI1pSYTZspqW1wW9X1s0oq4NNDBtoNok=";


    public static final String TOKEN_BACK = "001634a63b0ab430d0158d27c85OwACd+AC46VSY2PgW2MEADEyMzQBADIGAAAAY+BbYwEAY+BbYwIAY+BbYwMAY+BbYwQAY+BbYwUAY+BbYyAAGVkdzS/reChvHQ3r7yNlQEVxy/Kcl8dkN8CNZ32E0IU=";
    //INPUT_REGEX SDK 对房间名、用户名的限制是：非空且最大长度不超过128位的数字、大小写字母、@ . _ -
    public static final String INPUT_REGEX = "^[a-zA-Z0-9@._-]{1,128}$";

    public static final String ROOM_ID_EXTRA = "extra_room_id";

    public static final String USER_ID_EXTRA = "extra_user_id";
}
