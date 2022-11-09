package com.ss.video.rtc.demo.quickstart;

/**
 * VolcEngineRTC 常量定义
 */
public class Constants {

    //APPID 使用SDK前需要为自己的应用申请一个AppId，详情参见{https://www.volcengine.com/docs/6348/69865}
    public static final String APPID = "634a63b0ab430d0158d27c85";

    //TOKEN 加入房间的时候需要使用token完成鉴权，详情参见{https://www.volcengine.com/docs/6348/70121}
    public static final String TOKEN_1 = "001634a63b0ab430d0158d27c85OwDN4ZEDNpBrY7bKdGMEADEyMzQBADEGAAAAtsp0YwEAtsp0YwIAtsp0YwMAtsp0YwQAtsp0YwUAtsp0YyAAr66AG7jWaU9cPuwdHVOk5EtkqEjz5oISO5LWmReROYY=";


    public static final String TOKEN_2 = "001634a63b0ab430d0158d27c85OwAe+/wBS5BrY8vKdGMEADEyMzQBADIGAAAAy8p0YwEAy8p0YwIAy8p0YwMAy8p0YwQAy8p0YwUAy8p0YyAA0tNbxosBxhlRYybxJsg382oSWswyNBPEU1/kOqk08oE=";

    public static final String TOKEN_3 = "001634a63b0ab430d0158d27c85OwCO/7sEVpBrY9bKdGMEADEyMzQBADMGAAAA1sp0YwEA1sp0YwIA1sp0YwMA1sp0YwQA1sp0YwUA1sp0YyAAbRj5XnVBJCp5MnFsXOXch+zMCCC3fJQNyiZspiKERc0=";

    //INPUT_REGEX SDK 对房间名、用户名的限制是：非空且最大长度不超过128位的数字、大小写字母、@ . _ -
    public static final String INPUT_REGEX = "^[a-zA-Z0-9@._-]{1,128}$";

    public static final String ROOM_ID_EXTRA = "extra_room_id";

    public static final String USER_ID_EXTRA = "extra_user_id";
}
