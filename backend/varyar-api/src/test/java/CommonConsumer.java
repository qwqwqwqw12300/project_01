import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.*;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import java.util.*;
public class CommonConsumer {


    public static void main(String[] args) throws Exception {
        String accessKey = "a1184af46e8544a8af861cca2b711bdf";
        String secretkey = "99f1cf73e4354f06ba88750fc6cce6a6";

        String instanceID = "MQ_INST_1675403579920_yJyKHLuz";
        String GroupID = "GID_group_deviceActive";

        String namesrv = "http://rocketmq-ingress-huadong-4.cmecloud.cn:8081/";

        String topic = "deviceActiveTopic";


        // accesskey，secretkey鉴权，填写移动云上申请的accesskey与secretkey
        SessionCredentials credentials = new SessionCredentials(accessKey,secretkey);
        AclClientRPCHook rpcHook= new AclClientRPCHook(credentials);
        //构建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(instanceID, GroupID, rpcHook, new AllocateMessageQueueAveragely(), true, "");
        //namesrv地址
        consumer.setNamesrvAddr(namesrv);
        consumer.setConsumeTimeout(30000);
        //消费位点设置
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setMaxReconsumeTimes(2);
        //订阅Topic和Tag
        consumer.subscribe(topic, "*");
        //集群订阅
        consumer.setMessageModel(MessageModel.CLUSTERING);
        //广播订阅
        // consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgList, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for(MessageExt msg:msgList){
                    System.out.println(new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //启动消费者Consumer
        consumer.start();
        System.out.println("Consumer Started");
    }
}