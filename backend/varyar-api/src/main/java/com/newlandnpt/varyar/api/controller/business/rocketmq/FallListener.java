package com.newlandnpt.varyar.api.controller.business.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.newlandnpt.varyar.cloudBase.domain.Fall;

/**
 * MQ监听 - 摔倒事件
 * @author Bean
 * @date 2023-01-11
 */
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic.fall}", consumerGroup = "${rocketmq.group.fall}")
public class FallListener  implements RocketMQListener<Fall> {

	private static final Logger log = LoggerFactory.getLogger(FallListener.class);
	
    @Override
    public void onMessage(Fall fall) {
    	log.debug("监听到[{}], 报文对象：{} " ,"跌倒事件消息"  ,JSON.toJSONString(fall));
    }
}
