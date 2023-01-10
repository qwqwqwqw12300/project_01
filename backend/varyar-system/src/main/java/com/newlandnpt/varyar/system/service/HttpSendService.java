package com.newlandnpt.varyar.system.service;

import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.utils.http.HttpUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HTTP请求服务
 * @author lisd
 * @date 2023/1/3
 **/
public class HttpSendService {

    /**
     * 发送post请求
     * @param paramsMap
     * @param url
     * @return
     */
    public static String sendPost(Map<String, String> paramsMap, String url) {
        String params = setParams(paramsMap);
        return HttpUtils.sendPost(url, params);
    }

    /**
     * 发送get请求
     * @param paramsMap
     * @param url
     * @return
     */
    public static String sendRequest(Map<String, String> paramsMap, String url) {
        String params = setParams(paramsMap);
        return HttpUtils.sendGet(url, params);
    }

    /**
     * 组装请求参数
     * @param paramsMap
     * @return
     */
    private static String setParams(Map<String, String> paramsMap) {
        List<NameValuePair> paramList = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            paramList.add(new BasicNameValuePair(key, value));
        }
        String params = "";
        try {
            params = EntityUtils.toString(new UrlEncodedFormEntity(paramList, Constants.UTF8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return params;
    }

}
