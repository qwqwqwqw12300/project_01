package com.newlandnpt.varyar.cloudBase.constant;

/**
 * 缓存的key 常量
 * @author Bean
 */
public class CacheConstants {
    
	 /**
     * 状态缓存时间
     */
    public static final Integer STATE_EXPIRATION = 1;

    /**
     * 设备状态
     */
    public static final String DEVICE_STATE_KEY = "cloud:deviceState:";
    
    /** 进出区域 **/
    public static final String PRESENCE_ROOM_KEY = "cloud:presence:room:";
    
    public static final String PRESENCE_REGION0_KEY = "cloud:presence:region0:";
    public static final String PRESENCE_REGION1_KEY = "cloud:presence:region1:";
    public static final String PRESENCE_REGION2_KEY = "cloud:presence:region2:";
    public static final String PRESENCE_REGION3_KEY = "cloud:presence:region3:";
    
    public static final String VAYYAR_TOKEN_KEY = "cloud:token";
    
    public static final String VAYYAR_EMAIL = "elinker@elinker.cc";
    
    public static final String VAYYAR_PWD = "1234567890";
}
