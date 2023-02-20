package com.newlandnpt.varyar.common.core.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
/**
 * 分页Page基类
 *
 * @author chenxw1
 */
@ApiModel
public class PageRequest implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("分页数")
//    @NotBlank(message = "页数不能为空")
//    @Size(min = 0, max = 20, message = "设备编号不能超过20个字符")
    private int pageNum = 1;

    @ApiModelProperty("每页记录数")
//    @NotBlank(message = "每页记录数不能为空")
    private int pageSize = 10;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;//    @Size(min = 0, max = 20, message = "设备编号不能超过20个字符")

    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
