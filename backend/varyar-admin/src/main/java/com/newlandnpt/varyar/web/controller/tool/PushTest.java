package com.newlandnpt.varyar.web.controller.tool;

import com.newlandnpt.varyar.system.domain.TMsg;
import com.newlandnpt.varyar.system.service.IJiGuangSendService;
import com.newlandnpt.varyar.system.service.IMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.newlandnpt.varyar.common.constant.MsgConstants.*;

@RestController
@RequestMapping("/jgPushTest")
public  class PushTest {



        @Autowired
        private IJiGuangSendService jiGuangSendService;
        @Autowired
        private IMsgService iMsgService;
        private static final Logger logger = LoggerFactory.getLogger(PushTest.class);

        @PostMapping("/push")
        void myJPushClientTest() {
                TMsg  msg = new TMsg();
//                List<TMsg> msgList = iMsgService.selectTMsgList(msg);
//                System.out.println(msgList.toString());
                msg.setMsgType(MSG_TYPE_APP);
                msg.setContent("有人在卧室移动");
                msg.setMemberId(153L);
                msg.setOperator("系统");
                msg.setSendStatus(SEND_STATUS_NOT_SEND);
                msg.setOperateFlag(OPERATE_FLAG_NOT_HANDLE);
//                Map map = new HashMap();
//                map.put("memberId",101L);
//                map.put("familyId",103L);
//                System.out.println(map);
//                System.out.println(new ArrayList(map.keySet()));
                jiGuangSendService.jiGuangSend(msg);
//                try{
//                        jiGuangSendService.jiGuangSend(Arrays.asList(msg));
//                }catch (Exception e){
//                        logger.error(">>>> 极光推送发送失败",e);
//                }
//                jiGuangSendService.jiGuangSend(msgList);
        }

}
