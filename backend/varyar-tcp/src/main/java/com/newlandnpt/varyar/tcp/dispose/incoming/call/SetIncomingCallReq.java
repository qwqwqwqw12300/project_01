package com.newlandnpt.varyar.tcp.dispose.incoming.call;

import com.newlandnpt.varyar.tcp.base.MessageHead;
import com.newlandnpt.varyar.tcp.base.Req;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设置呼入号码
 * @author lin.ju
 * @date 2023/2/22
 */
public class SetIncomingCallReq extends MessageHead implements Req {

    /**
     * 删除的号码
     */
    private List<String> deleteNumbers;

    /**
     * 添加的号码
     */
    private List<IncomingCallPhone> addPhones;

    /**
     * 呼入限制：1、无限制 2、限制白名单以外的号码呼入 3、限制所有号码呼入
     */
    private String callInLimit = "2";

    /**
     * 周期 周一至周六用1-6表示，周日用0表示，如 1+2+3+4+表示周一至周四
     */
    private String period = "1+2+3+4+5+6+0";

    public SetIncomingCallReq() {
        setApiType("SET_INCOMING_CALL");
        setMsgType("1");
    }

    public List<String> getDeleteNumbers() {
        return deleteNumbers;
    }

    public void setDeleteNumbers(List<String> deleteNumbers) {
        this.deleteNumbers = deleteNumbers;
    }

    public List<IncomingCallPhone> getAddPhones() {
        return addPhones;
    }

    public void setAddPhones(List<IncomingCallPhone> addPhones) {
        this.addPhones = addPhones;
    }

    public String getCallInLimit() {
        return callInLimit;
    }

    public void setCallInLimit(String callInLimit) {
        this.callInLimit = callInLimit;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @Override
    public List<String> getRequests() {
        return Arrays.asList(
                CollectionUtils.isEmpty(deleteNumbers)?"0":deleteNumbers.stream().collect(Collectors.joining("!","","!")),
                CollectionUtils.isEmpty(addPhones)?"0":addPhones.stream().map(p->p.generateMessage()).collect(Collectors.joining("!","","!")),
                callInLimit,period
        );
    }

    public static class IncomingCallPhone{

        /**
         * 手机号码
         */
        private String phone;

        /**
         * 时间段0600-0800
         */
        private List<String> timePeriods = new ArrayList<>();

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
                    .collect(Collectors.joining("+")))
                    .stream().collect(Collectors.joining("="));
        }
    }

}
