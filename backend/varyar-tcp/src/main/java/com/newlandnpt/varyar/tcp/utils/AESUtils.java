package com.newlandnpt.varyar.tcp.utils;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author lin.ju
 * @date 2023/2/27
 */
public class AESUtils {

    /** 电子牵挂卡厂商提供 */
    private static final String ENCODE_KEY = "AjDxy202109AbcTd";
    /** 电子牵挂卡厂商提供 */
    private static final String IV_KEY = "A1B2C3D4E5F6G7H8";

    public static void main(String[] args) throws IOException {
        String encryptData = encryptFromStringForResponse("[867597011508550,89860321245910333529,202302170933320000,DEVICE_LOGIN,3,20230217093332,12,3@1@1@0@1@21]", Mode.CBC, Padding.PKCS5Padding);
        System.out.println("加密：" + encryptData+"#morefun#170");
        encryptData = "aJStPeBZ6Qt8mTFWxGR0XA+tWH8s4K+XGHmpxa0IK6LZZFBUAX8kPTTtEMXtBH2AyotLFx5y3OQXRVhVvlVLuXy26oClvB8EB0rs46MBe5KRU9l1RVEV8xIlSzWXFAVI";
        String decryptData = decryptFromString(encryptData, Mode.CBC, Padding.PKCS5Padding);
        System.out.println("解密：" + decryptData);
    }

    public static String encryptFromString(String data, Mode mode, Padding padding) {
        AES aes;
        if (Mode.CBC == mode) {
            aes = new AES(mode, padding,
                    new SecretKeySpec(ENCODE_KEY.getBytes(), "AES"),
                    new IvParameterSpec(IV_KEY.getBytes()));
        } else {
            aes = new AES(mode, padding,
                    new SecretKeySpec(ENCODE_KEY.getBytes(), "AES"));
        }
        return aes.encryptBase64(data, StandardCharsets.UTF_8);
    }

    public static String encryptFromStringForResponse(String data, Mode mode, Padding padding) {
        return encryptFromString(data,mode,padding)+"\n";
    }

    public static String decryptFromString(String data, Mode mode, Padding padding) {
        AES aes;
        if (Mode.CBC == mode) {
            aes = new AES(mode, padding,
                    new SecretKeySpec(ENCODE_KEY.getBytes(), "AES"),
                    new IvParameterSpec(IV_KEY.getBytes()));
        } else {
            aes = new AES(mode, padding,
                    new SecretKeySpec(ENCODE_KEY.getBytes(), "AES"));
        }
        data = data.split("#morefun")[0];
        byte[] decryptDataBase64 = aes.decrypt(data);
        return new String(decryptDataBase64, StandardCharsets.UTF_8).replace("\n","");
    }

}
