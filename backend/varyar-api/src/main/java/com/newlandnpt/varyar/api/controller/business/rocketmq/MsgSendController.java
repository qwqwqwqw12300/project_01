package com.newlandnpt.varyar.api.controller.business.rocketmq;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newlandnpt.varyar.common.annotation.Anonymous;
import com.newlandnpt.varyar.common.core.controller.BaseController;
import com.newlandnpt.varyar.system.domain.req.MsgSendReq;

/**
 * @author lisd
 * @date 2022/12/24
 **/
@RestController
@RequestMapping("/api/msgApp")
@SuppressWarnings({"ALL", "pmd:LowerCamelCaseVariableNamingRule"})
public class MsgSendController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(MsgSendController.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

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
