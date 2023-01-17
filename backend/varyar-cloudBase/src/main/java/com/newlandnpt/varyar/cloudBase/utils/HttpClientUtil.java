package com.newlandnpt.varyar.cloudBase.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;

public class HttpClientUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	public static String sendPost(String url, Object obj) {
		
		Assert.notNull(url, "url不可为空");
		// 创建默认的httpClient实例.    
		CloseableHttpClient httpClient = HttpClients.createDefault();  
		// 创建httppost    
		HttpPost httpPost = new HttpPost(url);
		try {
			String jsonDataStr = JSON.toJSONString(obj);
			HttpEntity sendEntity = new StringEntity(jsonDataStr,"UTF-8");
			httpPost.setEntity(sendEntity);
			httpPost.setHeader("Content-type", "application/json");
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {  
					String result = EntityUtils.toString(entity, "UTF-8");
					return result;
				}else {
					return null;
				}
			}finally {
				response.close();
			}
		} catch (Exception e) {
			logger.error("httpclient post请求api错误,url=["+url+"]", e);
			return null;
		} finally {
			// 关闭连接,释放资源
			try {
				httpClient.close();
			} catch (Exception e) {
				logger.error("关闭httpclient错误,url=["+url+"]", e);
			} 
		}
    }
	
}
