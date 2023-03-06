package com.newlandnpt.varyar.common.constant;

/**
 * 缓存的key 常量
 *
 * @author ruoyi
 */
public class CacheConstants
{
    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 短信码 redis key
     */
    public static final String SMS_CODE_KEY = "sms_codes:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 终端管理 cache key
     */
    public static final String T_DEVICE_KEY = "t_device:";

    /**
     * vayyar终端访问控制  cache key
     */
    public static final String T_DEVICE_VAYYAR_ACCESS_KEY = "t_device_vayyar_access:";
    /**
     * vayyar终端访问控制 24小时无人检测  cache key
     */
    public static final String T_DEVICE_VAYYAR_ACCESS_24HOURS_KEY = T_DEVICE_VAYYAR_ACCESS_KEY+"24Hours:";

    /**
     * vayyar终端访问控制 24小时无人检测 进房间 cache key
     */
    public static final String T_DEVICE_VAYYAR_ACCESS_24HOURS_IN_ROOM_KEY = T_DEVICE_VAYYAR_ACCESS_24HOURS_KEY+"inRoom:";

    /**
     * vayyar终端访问控制 24小时无人检测 离开房间 cache key
     */
    public static final String T_DEVICE_VAYYAR_ACCESS_24HOURS_OUT_ROOM_KEY = T_DEVICE_VAYYAR_ACCESS_24HOURS_KEY+"outRoom:";

    /**
     * 终端离线列表 cache key
     */
    public static final String DEVICE_DISCONNECTION = "device_disconnection";

    /**
     * 设备状态
     */
    public static final String DEVICE_STATE_KEY = "cloud:deviceState:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 手机号更改认证key
     */
    public static final String PHONE_TOKEN_KEY = "phone_token_key:";

    /** 进出区域 **/
    public static final String PRESENCE_ROOM_KEY = "cloud:presence:room:";
    /** 是否开启记录目标坐标信息的开关，值为true or false **/
    public static final String TARGET_LOCATION_SWITCH_KEY = "cloud:presence:targetLocationSwitch:";
    /** 进出区域坐标信息 **/
    public static final String TARGET_LOCATION_PRESENCE_KEY = "cloud:presence:targetLocation:";
    /** 跌倒坐标信息 **/
    public static final String TARGET_LOCATION_FALL_KEY = "cloud:fall:targetLocation:";

    /** 电子牵挂卡 tcp响应 */
    public static final String TCP_WATCH_TRADE_NO = "t_device_watch_tcp_response:";
    /** 秒级 4位循环数 */
    public static final String SECOND_4_CYCLE_NUMBER = "second_4_cycle_number:";
    /** 设备在线信息**/
    public static final String DEVICE_ONLINE_INFO = "tcp:deviceOnlineNo:";
    /** 设备心跳数据**/
    public static final String DEVICE_HEART_BEAT = "tcp:deviceHeartBeat:";
    /** 设备位置数据**/
    public static  final String DEVICE_LOCATION = "tcp:deviceLocation:";
    /** 设备号**/
    public static final String DEVICE_NO = "cloud:deviceNo:";
    /** SIM 卡卡号**/
    public static final String ICCID = "cloud:iccid:";
    /** 交易流水号**/
    public static final String TRAN_NO = "cloud:tranNo:";
    /** 报文时间**/
    public static final String MSG_TIME = "cloud:msgTime:";
    /** 设备当前电量**/
    public static final String DEVICE_CURRENT_POWER = "deviceCurrentPower:";
    /** 设备当前步数**/
    public static final String DEVICE_CURRENT_STEPS = "currentSteps:";
    /** 设备GPS信息**/
    public static final String DEVICE_GPS = "gps:";
    /** 设备LBS信息**/
    public static final String DEVICE_LBS = "lbs:";

    /** 高德地图猎鹰api 终端设备id */
    public static final String GAODE_TID = "gaode:tid:";
}
