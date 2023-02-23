package com.newlandnpt.varyar.common.utils.SMS.templates;

import java.util.List;

/**
 * 短信模板
 * @author lin.ju
 * @date 2023/2/21
 */
public interface SMSTemplate {

    /**
     * 获取模板id
     * @return
     */
    String getTemplateId();


    /**
     * 获取模板参数
     * @return
     */
    List<String> getParams();

    /**
     * 判断是否为正确的参数
     * @return
     */
    Boolean isCorrectParams();
}
