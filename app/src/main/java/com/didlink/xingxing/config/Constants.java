package com.didlink.xingxing.config;

public class Constants {
    public static final String CHAT_SERVER_URL = "http://192.168.1.103:3000";
    public static final String HTTP_BASE_URL = "http://10.0.2.2:5946";
    public static final String PP_SERVICE_HOST = "10.0.2.2";
    public static final int PP_SERVICE_PORT = 7366;
    public static final int PP_HEARTBEAT_TIMER = 10000;
    public static final int PP_HEARTBEAT_FAILED_RETRY_TIMER = 120000;

    public static final int SPOTCODE_DEFAULT = 0;
    public static final int SPOTCODE_STATION = 1;

    //SharedPreferences key
    public static final String SHARED_PREFERENCE_NAME = "shared_p";
    public static final String SHARED_PREFERENCE_KEY_IFLOGIN = "if_login";
    public static final String SHARED_PREFERENCE_KEY_AVATAR_STATUS = "avatar_status";
    public static final String SHARED_PREFERENCE_KEY_AVATAR_FILE = "avatar_file";

    //DB KEY
    public static final String DBKEY_RECEIVEFREEMSG = "shdisney_revmsg";
    public static final String DBKEY_LOGIN = "login";
    public static final String DBKEY_USERLOGIN = "userlogin";
    public static final String DBKEY_USERID = "uid";
    public static final String DBKEY_GRAVATARPICTURE = "avatarurl";
    public static final String DBKEY_AVATARSTATUS = "avatarstat";
    public static final String DBKEY_NICKNAME = "nickname";
    public static final String DBKEY_CHANNELPREFIX = "channel:";

    public static final int ACTIVITY_CHANNELADDGROUP_RESULTOK = 101;
    public static final int ACTIVITY_CHANNELADDGROUP_RESULTCANCEL = 102;
    public static final int ACTIVITY_CHANNELADDCONTACT_RESULTOK = 103;
    public static final int ACTIVITY_CHANNELADDCONTACT_RESULTCANCEL = 104;

    public static final String PRAMARY_CHANNEL_MY = "1";
    public static final String PRAMARY_CHANNEL_PUBLIC = "2";

    public static final int CHANNEL_TYPE_DEFAULT = 0;
    public static final int CHANNEL_TYPE_SOCIAL = 1;
    public static final int CHANNEL_TYPE_PLACE = 2;
}
