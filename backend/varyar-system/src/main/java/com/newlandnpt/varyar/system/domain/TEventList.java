package com.newlandnpt.varyar.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 事件 t_event list
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class TEventList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Size(min = 0, max = 20, message = "唯一标识不能超过20个字符")
    private String familyId;

    /** 级别 */
    private String level;

    /**
     *  设备类型
     * */
    private String type;

    /** 内容 */
    private String content;

    /** 操作标志（0未处理 1已处理） */
    private String operateFlag;

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOperateFlag() {
        return operateFlag;
    }

    public void setOperateFlag(String operateFlag) {
        this.operateFlag = operateFlag;
    }

    @Override
    public String toString() {
        return "TEventList{" +
                "familyId='" + familyId + '\'' +
                ", level='" + level + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", operateFlag='" + operateFlag + '\'' +
                '}';
    }
}
