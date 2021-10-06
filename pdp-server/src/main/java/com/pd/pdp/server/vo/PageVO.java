package com.pd.pdp.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author pdp
 * @Description 分页接收参数类
 **/

@Data
@ApiModel("分页接收参数类对象")
public class PageVO<T> {

    @ApiModelProperty(value = "当前页")
    private int currentPage;

    @ApiModelProperty(value = "每页数量")
    private int pageNumber;

    @ApiModelProperty("数据库开始页")
    private int dbIndex;

    @ApiModelProperty("数据库大小")
    private int dbNumber;

    @ApiModelProperty(value = "查询参数对象")
    private T data;
}
