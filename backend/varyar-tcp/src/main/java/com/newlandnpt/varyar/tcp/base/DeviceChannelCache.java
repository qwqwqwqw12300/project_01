package com.newlandnpt.varyar.tcp.base;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备频道缓存
 * @author lin.ju
 * @date 2023/2/22
 */
public class DeviceChannelCache {

    private static Map<String, DeviceChannel> channelMap = new HashMap<>();

    public static void setCache(String deviceNo,String iccid,String connectTime,Channel channel){
        if(channelMap.containsKey(deviceNo)&& channel!= channelMap.get(deviceNo)){
            channelMap.get(deviceNo).getChannel().close();
        }
        DeviceChannel deviceChannel = new DeviceChannel();
        deviceChannel.setDeviceNo(deviceNo);
        deviceChannel.setIccid(iccid);
        deviceChannel.setConnectTime(connectTime);
        deviceChannel.setChannel(channel);

        channelMap.put(deviceNo,deviceChannel);
    }

    public static Channel getChannelByDeviceNo(String deviceNo){
        DeviceChannel channel = channelMap.get(deviceNo);

        if(channel!=null&&channel.getChannel()!=null && channel.getChannel().isOpen()){
            return channel.getChannel();
        }

        return null;
    }

    public static String getIccIdByDeviceNo(String deviceNo){
        DeviceChannel channel = channelMap.get(deviceNo);

        if(channel!=null){
            return channel.getIccid();
        }

        return null;
    }

    public static class DeviceChannel{
        /**
         * 设备编号
         */
        String deviceNo;
        /**
         * SIM 卡卡号
         */
        private String iccid;
        /**
         * 连接时间
         * 时间格式为： yyyyMMddHHmmss
         */
        private String connectTime;
        /**
         * 通道
         */
        Channel channel;

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

        public String getIccid() {
            return iccid;
        }

        public void setIccid(String iccid) {
            this.iccid = iccid;
        }

        public String getConnectTime() {
            return connectTime;
        }

        public void setConnectTime(String connectTime) {
            this.connectTime = connectTime;
        }

        public Channel getChannel() {
            return channel;
        }

        public void setChannel(Channel channel) {
            this.channel = channel;
        }
    }

}
