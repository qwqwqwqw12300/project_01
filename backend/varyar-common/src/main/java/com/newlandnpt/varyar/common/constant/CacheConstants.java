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

    /** 是否开启记录目标坐标信息的开关，值为true or false **/
    public static final String TARGET_LOCATION_SWITCH_KEY = "cloud:presence:targetLocationSwitch:";
    /** 进出区域坐标信息 **/
    public static final String TARGET_LOCATION_PRESENCE_KEY = "cloud:presence:targetLocation:";
    /** 跌倒坐标信息 **/
    public static final String TARGET_LOCATION_FALL_KEY = "cloud:fall:targetLocation:";
}
