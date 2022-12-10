package com.ss.video.rtc.demo.quickstart;

/**
 * VolcEngineRTC 常量定义
 */
public class Constants {
    //APPID 使用SDK前需要为自己的应用申请一个AppId，详情参见{https://www.volcengine.com/docs/6348/69865}
    public static final String APPID = "6365ea3c10a4d2015a1e07ab";
    //TOKEN 加入房间的时候需要使用token完成鉴权，详情参见{https://www.volcengine.com/docs/6348/70121}
    public static final String TOKEN_1 = "0016365ea3c10a4d2015a1e07abOwAEo/kDr6GSYy/cm2MEADEyMzQBADEGAAAAL9ybYwEAL9ybYwIAL9ybYwMAL9ybYwQAL9ybYwUAL9ybYyAABnfkzE10lhz/aRM9nbJe+fIx2G5Ft7cPD4Suk2eeCFQ=";

    public static final String TOKEN_2 = "0016365ea3c10a4d2015a1e07abOwDpjmkByaGSY0ncm2MEADEyMzQBADIGAAAASdybYwEASdybYwIASdybYwMASdybYwQASdybYwUASdybYyAAHMIvrlzFPBgWZxy9RfaQ3SixnEAjWd4sWtDxumlvRzY=";

    public static final String TOKEN_3 = "0016365ea3c10a4d2015a1e07abOwAOZCMC36GSY1/cm2MEADEyMzQBADMGAAAAX9ybYwEAX9ybYwIAX9ybYwMAX9ybYwQAX9ybYwUAX9ybYyAAnmpno2UzPfcFze6AUztRCF/eo4u4Z+PsoE9QU3CsbIM=";

    public static final String TOKEN_4 = "0016365ea3c10a4d2015a1e07abOwCgIt0A86GSY3Pcm2MEADEyMzQBADQGAAAAc9ybYwEAc9ybYwIAc9ybYwMAc9ybYwQAc9ybYwUAc9ybYyAA74YckV6+cOuMVMF5oCAt5CbV4mgFn1VTMOaJoowXzKk=";

    public static final String TOKEN_5 = "0016365ea3c10a4d2015a1e07abOwBU3XMBHqKSY57cm2MEADEyMzQBADUGAAAAntybYwEAntybYwIAntybYwMAntybYwQAntybYwUAntybYyAAc2yXNferdeCmh6dfj+IhKYXclSwIIWKeT0rKrlIrnRA=";

    public static final String TOKEN_6 = "0016365ea3c10a4d2015a1e07abOwD95YsDQKKSY8Dcm2MEADEyMzQBADYGAAAAwNybYwEAwNybYwIAwNybYwMAwNybYwQAwNybYwUAwNybYyAAfcW6fRspJ+9CeeHZmHwOju3Z7bcMU2UXi32vGe37TCA=";

    //INPUT_REGEX SDK 对房间名、用户名的限制是：非空且最大长度不超过128位的数字、大小写字母、@ . _ -
    public static final String INPUT_REGEX = "^[a-zA-Z0-9@._-]{1,128}$";

    public static final String ROOM_ID_EXTRA = "extra_room_id";

    public static final String USER_ID_EXTRA = "extra_user_id";
}
