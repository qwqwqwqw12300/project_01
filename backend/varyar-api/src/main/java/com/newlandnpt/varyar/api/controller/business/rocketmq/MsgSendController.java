package com.newlandnpt.varyar.api.controller.business.rocketmq;

import com.newlandnpt.varyar.system.domain.req.MsgSendReq;
import com.newlandnpt.varyar.common.annotation.Anonymous;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

/**
 * @author lisd
 * @date 2022/12/24
 **/
@RestController
@RequestMapping("/api/msgApp")
@Slf4j
@SuppressWarnings({"ALL", "pmd:LowerCamelCaseVariableNamingRule"})
public class MsgSendController extends BaseController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 断网事件
     * 定时任务 从激活的设备中循环判断在redis是否有在线信息，不在线则新增断网事件。
     * 设备模块重新接收到信息，mq推送，新增断网恢复事件
     */
    @Value("${rocketmq.topic.net}")
    private String netTopic;

    /**
     * 跌倒事件
     * 设备模块接收到信息，mq推送，新增跌倒事件
     */
    @Value("${rocketmq.topic.fall}")
    private String fallTopic;

    /**
     * 进出房间事件
     * 设备模块接收到信息，mq推送，新增进房间事件
     * 增加定时任务，规定时间内，没有出房间事件，则新增失联事件
     */
    @Value("${rocketmq.topic.access}")
    private String accessTopic;

    /**
     * 地理围栏事件
     * 设备模块接收到信息，上送经纬度
     * app模块调用地图api，超出，则新增地理围栏事件
     */
    @Value("${rocketmq.topic.location}")
    private String locationTopic;

    /**
     * 发送消息
     *
     * @param msg
     */
    @PostMapping("/sendMsg")
    @Anonymous
    public void sendMsg(@RequestBody MsgSendReq msgSendReq) throws Exception {
        String msg = msgSendReq.getMsg();
        int code = msgSendReq.getCode();
        SendResult result;
        switch (code) {
            case 1:
                result = rocketMQTemplate.syncSend(netTopic, MessageBuilder.withPayload(msg).build());
                break;
            case 2:
                result = rocketMQTemplate.syncSend(fallTopic, MessageBuilder.withPayload(msg).build());
                break;
            case 3:
                result = rocketMQTemplate.syncSend(accessTopic, MessageBuilder.withPayload(msg).build());
                break;
            default:
                result = rocketMQTemplate.syncSend(locationTopic, MessageBuilder.withPayload(msg).build());
        }
        log.debug(" 发送消息结果： " + result);
        log.debug(" 发送时间： " + System.currentTimeMillis());
        if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
            throw new Exception("发送失败");
        }
    }
}
