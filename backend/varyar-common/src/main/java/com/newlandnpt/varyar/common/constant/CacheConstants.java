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
     * 终端在线标记 cache key
     */
    public static final String DEVICE_ONLINE_FLAG_KEY = "device_online_flag:";

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
}
