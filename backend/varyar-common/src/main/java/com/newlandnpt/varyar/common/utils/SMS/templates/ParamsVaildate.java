package com.newlandnpt.varyar.common.utils.SMS.templates;

import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 对于邮件模板的参数校验
 * @author ljx
 * @date 2023/2/22
 */
public class ParamsVaildate {

    /**
     * 中文+字母+数字
     */
    public static String CHAR_NUMBER = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$";

    /**
     * 中文+字母+*
     */
    public static String CHAR_STAR = "^[\\u4e00-\\u9fa5*a-zA-Z0-9]+$";

    /**
     * 判断不为空值
     * @param param
     * @return
     */
    public static Boolean isNotNull(String param){
        return !StringUtils.isBlank(param);
    }

    /**
     * 是否为限制长度
     * @param param
     * @param limitSize
     * @return
     */
    public static Boolean isRanger(String param, Integer limitSize){
        if(StringUtils.isBlank(param) || param.length()==0 || param.length()>limitSize){
            return false;
        }
        return true;
    }

    /**
     * 判断是否满足字符串的组合要求
     * @param param
     * @param regexp
     * @return
     */
    public static Boolean isPattern(String param, String regexp){
        return Pattern.matches(regexp,param);
    }
}
