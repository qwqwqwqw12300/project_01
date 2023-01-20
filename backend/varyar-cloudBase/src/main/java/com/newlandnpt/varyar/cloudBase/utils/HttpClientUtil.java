package com.newlandnpt.varyar.cloudBase.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

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
import com.newlandnpt.varyar.common.constant.Constants;
import com.newlandnpt.varyar.common.utils.StringUtils;

public class HttpClientUtil {

	private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
	
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
			httpPost.setHeader("accept", "*/*");
			httpPost.setHeader("connection", "Keep-Alive");
			httpPost.setHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            httpPost.setHeader("Accept-Charset", "utf-8");
            httpPost.setHeader("contentType", "utf-8");
            httpPost.setHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiNjMxODQ1NWFiZTM2ZTlhOTU1MDY2ZTFmIiwiZW1haWwiOiJlbGlua2VyQGVsaW5rZXIuY2MiLCJpYXQiOjE2NzQxMTczMDcsImV4cCI6MTY3NDExNzMwN30.MC-EBlLay7GUvDOSAZx5mN3ToJR4dNDDh7HTZk6k3bI");
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
			log.error("httpclient post请求api错误,url=["+url+"]", e);
			return null;
		} finally {
			// 关闭连接,释放资源
			try {
				httpClient.close();
			} catch (Exception e) {
				log.error("关闭httpclient错误,url=["+url+"]", e);
			} 
		}
    }

   /**
    * 向指定 URL 发送GET方法的请求<br>
    * vayyar专用
    * @param url 发送请求的 URL
    * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
    * @param authorization Authorization报文头的值
    * @return 所代表远程资源的响应结果
    */
   public static String sendGet(String url, String param, String authorization)
   {
       StringBuilder result = new StringBuilder();
       BufferedReader in = null;
       try
       {
           String urlNameString = StringUtils.isNotBlank(param) ? url + "?" + param : url;
           log.info("sendGet - {}", urlNameString);
           URL realUrl = new URL(urlNameString);
           URLConnection connection = realUrl.openConnection();
           connection.setRequestProperty("accept", "*/*");
           connection.setRequestProperty("connection", "Keep-Alive");
           connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
           connection.setRequestProperty("Authorization", authorization);
           connection.connect();
           in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Constants.UTF8));
           String line;
           while ((line = in.readLine()) != null)
           {
               result.append(line);
           }
           log.info("recv - {}", result);
       }
       catch (ConnectException e)
       {
           log.error("调用HttpUtils.sendGet ConnectException, url=" + url + ",param=" + param, e);
       }
       catch (SocketTimeoutException e)
       {
           log.error("调用HttpUtils.sendGet SocketTimeoutException, url=" + url + ",param=" + param, e);
       }
       catch (IOException e)
       {
           log.error("调用HttpUtils.sendGet IOException, url=" + url + ",param=" + param, e);
       }
       catch (Exception e)
       {
           log.error("调用HttpsUtil.sendGet Exception, url=" + url + ",param=" + param, e);
       }
       finally
       {
           try
           {
               if (in != null)
               {
                   in.close();
               }
           }
           catch (Exception ex)
           {
               log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
           }
       }
       return result.toString();
   }
   
}
