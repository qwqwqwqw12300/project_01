package com.newlandnpt.varyar.tcp.gateway.handler.contacts;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Response;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 设备通讯录响应
 * @author chenxw1
 * @date 2023/2/22
 *
 *
 * 报文体格式：
 * 应答报文体：
 * 删除号码@添加号码和时段@呼入限制@周期
 * 字段说明：
 * 无删除号码或者无添加号码可用0代替
 * 电子牵挂卡通讯接口协议
 * 44
 * 例如
 * 0@13900000004=0600-2230+!@呼入限制@周期
 * 13900000004@0@呼入限制@周期
 * 最多允许20个白名单用户; 每个白名单号码允许设
 * 置两个呼入时段
 * 删除号码和时段(呼入号码)
 * 删除的时候，会将2个时段内的数据全部删除
 * 添加号码和时段(呼入号码=开始时间-结束时间+开
 * 始时间-结束时间+)
 * 13900000000!13900000001!13900000002!1390000000
 * 3!@13900000004=0600-2230+!13900000005=0900-1
 * 130+!
 * 呼入限制：1、无限制 2、限制白名单以外的号码呼
 * 入 3、限制所有号码呼入
 * 周期：周一至周六用1-6表示，周日用0表示，如
 * 1+2+3+4+表示周一至周四
 * 异常应答：0，无设置 1非平台用户2其他异常
 * 报文示例
 * [35380100360174,898600MFSSYYGXXXXXXP,201805082000001000,GET_NORM
 * AL_BUTTON,4,20180528111820,112,13900000000!13900000001!13900000002!139
 * 00000003!@13900000004=0600-2230+!13900000005=0300-0600+0900-1130+!@2
 * @0+1+2+3+4+5+6]
 *
 */
public class GetIncomingCallResponse extends MessageHead implements Response {


    /**
     * 删除号码和时段(呼入号码)
     */
    private List<String> delPhoneNumbers;

    /**
     * 添加号码和时段(呼入号码=开始时间-结束时间+开
     * 始时间-结束时间+) 最多2个时段
     */
    private List<AddPhoneNumbers> addPhoneNumbers;

    /**
     * 呼入限制 1、无限制 2、限制白名单以外的号码呼
     * 入 3、限制所有号码呼入
     */
    private String incomingCallLimit="2";

    /**
     * 周期：周一至周六用1-6表示，周日用0表示，如
     * 1+2+3+4+表示周一至周四
     */
    private String cycle = "0+1+2+3+4+5+6";

    public List<String> getDelPhoneNumbers() {
        return delPhoneNumbers;
    }

    public void setDelPhoneNumbers(List<String> delPhoneNumbers) {
        this.delPhoneNumbers = delPhoneNumbers;
    }

    public List<AddPhoneNumbers> getAddPhoneNumbers() {
        return addPhoneNumbers;
    }

    public void setAddPhoneNumbers(List<AddPhoneNumbers> addPhoneNumbers) {
        this.addPhoneNumbers = addPhoneNumbers;
    }

    public String getIncomingCallLimit() {
        return incomingCallLimit;
    }

    public void setIncomingCallLimit(String incomingCallLimit) {
        this.incomingCallLimit = incomingCallLimit;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public static class AddPhoneNumbers {

        /**
         * 手机号码
         */
        private String phone;

        /**
         * 时间段固定0000 - 2359
         */
        private List<String> timePeriods = Arrays.asList("0000","2359");

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public List<String> getTimePeriods() {
            return timePeriods;
        }

        public void setTimePeriods(List<String> timePeriods) {
            this.timePeriods = timePeriods;
        }

        public String generateMessage(){
            return Arrays.asList(phone,timePeriods.stream()
                            .collect(Collectors.joining("-","","+")))
                    .stream().collect(Collectors.joining("="));
        }
    }

    @Override
    public List<String> getResponses() {
        return Arrays.asList(
                CollectionUtils.isEmpty(delPhoneNumbers)?"0":delPhoneNumbers.stream().collect(Collectors.joining("!","","!")),
                CollectionUtils.isEmpty(addPhoneNumbers)?"0":addPhoneNumbers.stream().map(p->p.generateMessage()).collect(Collectors.joining("!","","!")),
                incomingCallLimit,cycle
        );
    }

}
