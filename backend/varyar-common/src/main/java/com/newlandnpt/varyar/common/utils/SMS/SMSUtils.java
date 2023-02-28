package com.newlandnpt.varyar.common.utils.SMS;

import com.alibaba.fastjson2.JSON;
import com.newlandnpt.varyar.common.core.domain.model.SMS.SMSSendReq;
import com.newlandnpt.varyar.common.core.domain.model.SMS.SMSSendRes;
import com.newlandnpt.varyar.common.utils.Md5.Md5Util;
import com.newlandnpt.varyar.common.utils.SMS.templates.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

public class SMSUtils {

    /**账号*/
    private static String apId = "Pdev";
    /**密码*/
    private static String secretKey = "$Admin123!";
    /**集团名称*/
    private static String ecName = "福建易联智慧科技有限公司";
    /**网关签名编码*/
    private static String sign = "BeoPYV83W";
    /**拓展码（可以为空）*/
    private static String addSerial = "";
    /**url*/
    private static String url = "http://112.35.10.201:5992/sms/tmpsubmit";

    /**
     * 多用户发送短信信息
     *
     * @param mobiles 手机号码逗号分隔
     * @param template 短信模板
     * @return 返回1表示成功，0表示失败
     * @throws IOException
     */
    public static boolean sendMsg(String mobiles, SMSTemplate template) throws IOException {
        // todo 验证模板短信
        SMSSendReq sendReq = new SMSSendReq();
        sendReq.setEcName(ecName);
        sendReq.setApId(apId);
        sendReq.setTemplateId(template.getTemplateId());
        sendReq.setMobiles(mobiles);
        sendReq.setParams(template.getParams());
        sendReq.setSign(sign);
        sendReq.setAddSerial(addSerial);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(sendReq.getEcName());
        stringBuffer.append(sendReq.getApId());
        stringBuffer.append(secretKey);
        stringBuffer.append(sendReq.getTemplateId());
        stringBuffer.append(sendReq.getMobiles());
        stringBuffer.append(sendReq.getParams());
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
//        sendMsg("15060665801", new Test("易连科"));
        try {
//            DeviceOutLineWarn warn = new DeviceOutLineWarn( "", "雷达波1号");
            SMSVerificate sms1 = new SMSVerificate("123456","60分钟");
            FamilyShare sms2 = new FamilyShare("135*1234","http://r.aj.com");
            DeviceOutLineWarn sms3 = new DeviceOutLineWarn("1234","雷达波一号");
            FallWarn sms4 = new FallWarn("1234","雷达波1号");
            PassOutWarn sms5 = new PassOutWarn("1234","雷达波1号","大卧室","离开","50");



//            if(sms1.isCorrectParams()){
//                sendMsg("15859819572",sms1);
//            }
//            if(sms2.isCorrectParams()){
//                sendMsg("15859819572",sms2);
//            }
//            if(sms3.isCorrectParams()){
//                sendMsg("15859819572",sms3);
//            }
//            if(sms4.isCorrectParams()){
//                sendMsg("15859819572",sms4);
//            }
//            sendMsg("15859819572",sms5);
//            if(sms5.isCorrectParams()){
//                sendMsg("15859819572",sms5);
//            }


        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
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

    private static class Test implements SMSTemplate{

        private String name;

        public Test(String name) {
            this.name = name;
        }

        @Override
        public String getTemplateId() {
            return "aa2be779a4f34ff0a97ce0d1df6965f9";
        }

        @Override
        public List<String> getParams() {
            return Arrays.asList(name);
        }

        @Override
        public Boolean isCorrectParams() {
            return true;
        }
    }

    @Component
    public static class SMSUtilsInjector{

        @Value("${sms.apId:-}")
        public void setApId(String apId){
            SMSUtils.apId = apId;
        }

        @Value("${sms.secretKey:-}")
        public void setSecretKey(String secretKey){
            SMSUtils.secretKey = secretKey;
        }

    }

}
