package com.newlandnpt.varyar.common.utils.SMS;

import com.alibaba.fastjson2.JSON;
import com.newlandnpt.varyar.common.core.domain.model.SMS.SMSSendReq;
import com.newlandnpt.varyar.common.core.domain.model.SMS.SMSSendRes;
import com.newlandnpt.varyar.common.utils.Md5.Md5Util;
import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class SMSUtils {

    /**账号*/
    private static String apId = "";
    /**密码*/
    private static String secretKey = "";
    /**集团名称*/
    private static String ecName = "";
    /**网关签名编码*/
    private static String sign = "";
    /**拓展码（可以为空）*/
    private static String addSerial = "";
    /**url*/
    private static String url = "http://112.35.1.155:1992/sms/norsubmit";

    /**
     * 多用户发送短信信息
     *
     * @param mobiles 手机号码逗号分隔
     * @param content 短信内容
     * @return 返回1表示成功，0表示失败
     * @throws IOException
     */
    public static boolean sendMsg(String mobiles, String content) throws IOException {
        SMSSendReq sendReq = new SMSSendReq();
        sendReq.setApId(apId);
        sendReq.setEcName(ecName);
        sendReq.setSecretKey(secretKey);
        sendReq.setContent(content);
        sendReq.setMobiles(mobiles);
        sendReq.setAddSerial(addSerial);
        sendReq.setSign(sign);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(sendReq.getEcName());
        stringBuffer.append(sendReq.getApId());
        stringBuffer.append(sendReq.getSecretKey());
        stringBuffer.append(sendReq.getMobiles());
        stringBuffer.append(sendReq.getContent());
        stringBuffer.append(sendReq.getSign());
        stringBuffer.append(sendReq.getAddSerial());

        sendReq.setMac(Md5Util.MD5(stringBuffer.toString()).toLowerCase());

        String reqText = JSON.toJSONString(sendReq);

        // BASE64编码
        String encode = Base64.encodeBase64String(reqText.getBytes("UTF-8"));
        System.out.println(encode);

        String resStr = sendPost(url, encode);
        System.out.println("发送短信结果：" + resStr);

        SMSSendRes sendRes = JSON.parseObject(resStr, SMSSendRes.class);

        if (sendRes.isSuccess() && !"".equals(sendRes.getMsgGroup()) && "success".equals(sendRes.getRspcod())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * main方法测试发送短信，返回1表示成功，0表示失败
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String msg = "这是发送短信的内容！";
        sendMsg("186****7603", msg);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数
     * @return 所代表远程资源的响应结果
     */
    private static String sendPost(String url, String param) {
        OutputStreamWriter out = null;

        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            out = new OutputStreamWriter(conn.getOutputStream());
            out.write(param);
            out.flush();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
