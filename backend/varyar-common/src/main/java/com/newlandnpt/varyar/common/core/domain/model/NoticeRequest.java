package com.newlandnpt.varyar.common.core.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@ApiModel(description = "获取公告请求")
public class NoticeRequest {

    /**
     *  未读标识 0:未读 1:已读
     * */
    @ApiModelProperty("未读标识 0:未读 1:已读")
    @Size(min = 0, max = 1, message = "未读标识不能超过1个字符")
    private String readFlag;
    /**
     *  公告id
     * */
    @ApiModelProperty("公告id")
    @NotBlank(message = "公告id不能为空")
    @Size(min = 0, max = 20, message = "公告id不能超过20个字符")
    private String noticeId;

    public String getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }
}
