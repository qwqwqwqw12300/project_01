package com.newlandnpt.varyar.tcp.base;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 频道缓存
 * @author lin.ju
 * @date 2023/2/22
 */
public class ChannelCache {

    private static Map<String, Channel> channelMap = new HashMap<>();

    public static void setCache(String deviceNo,Channel channel){
        if(channelMap.containsKey(deviceNo)&& channel!= channelMap.get(deviceNo)){
            channelMap.get(deviceNo).close();
        }
        channelMap.put(deviceNo,channel);
    }

    public static Channel getChannelByDeviceNo(String deviceNo){
        Channel channel = channelMap.get(deviceNo);

        if(channel!=null && channel.isOpen()){
            return channel;
        }

        return null;
    }

}
