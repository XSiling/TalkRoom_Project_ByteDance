package com.ss.video.rtc.demo.quickstart;

/**
 * VolcEngineRTC 常量定义
 */
public class Constants {
    //APPID 使用SDK前需要为自己的应用申请一个AppId，详情参见{https://www.volcengine.com/docs/6348/69865}
    public static final String APPID = "6365ea3c10a4d2015a1e07ab";
    //TOKEN 加入房间的时候需要使用token完成鉴权，详情参见{https://www.volcengine.com/docs/6348/70121}
    public static final String TOKEN_1 = "0016365ea3c10a4d2015a1e07abOwCQnCABLIiAY6zCiWMEADEyMzQBADEGAAAArMKJYwEArMKJYwIArMKJYwMArMKJYwQArMKJYwUArMKJYyAATpdSh563hPI9c5xJVZUFsixIWwS2d+ygE7DEVArUIVM=";

    public static final String TOKEN_2 = "0016365ea3c10a4d2015a1e07abOwDA1O4BQoiAY8LCiWMEADEyMzQBADIGAAAAwsKJYwEAwsKJYwIAwsKJYwMAwsKJYwQAwsKJYwUAwsKJYyAACTmD/54HxWbt4apB3MkNsFJsjcAhy+zyTkSuvB8KRXs=";

    public static final String TOKEN_3 = "0016365ea3c10a4d2015a1e07abOwBWcVgDUYiAY9HCiWMEADEyMzQBADMGAAAA0cKJYwEA0cKJYwIA0cKJYwMA0cKJYwQA0cKJYwUA0cKJYyAArHe5vNARffh5a/8u0gvbWnUrUBVl0pCsxQkk4P0Ov3A=";

    public static final String TOKEN_4 = "0016365ea3c10a4d2015a1e07abOwCzj2sFGrqAY5r0iWMEADEyMzQBADQGAAAAmvSJYwEAmvSJYwIAmvSJYwMAmvSJYwQAmvSJYwUAmvSJYyAAsKoAgXHenAxwdD7FE2U+zeV/XezUdtR3NlH7U0o66Kw=";

    public static final String TOKEN_5 = "0016365ea3c10a4d2015a1e07abOwDifA0CQrqAY8L0iWMEADEyMzQBADUGAAAAwvSJYwEAwvSJYwIAwvSJYwMAwvSJYwQAwvSJYwUAwvSJYyAAS8827cyKTWsxJENqWsw2oDKwjyyn6co8fjoBKJ9mFQo=";

    public static final String TOKEN_6 = "0016365ea3c10a4d2015a1e07abOwBwiMcCabqAY+n0iWMEADEyMzQBADYGAAAA6fSJYwEA6fSJYwIA6fSJYwMA6fSJYwQA6fSJYwUA6fSJYyAA0CBzVcJP/RrxNWyCgVbEwc+u9vBCw8jEKfoKr8ZjaEw=";
    //INPUT_REGEX SDK 对房间名、用户名的限制是：非空且最大长度不超过128位的数字、大小写字母、@ . _ -
    public static final String INPUT_REGEX = "^[a-zA-Z0-9@._-]{1,128}$";

    public static final String ROOM_ID_EXTRA = "extra_room_id";

    public static final String USER_ID_EXTRA = "extra_user_id";
}
