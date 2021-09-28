package com.pd.pdp.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author pdp
 * @Description
 * @Date 2020-10-15 2:42 下午
 **/

@Data
@ApiModel("公共请求类对象")
public class Request<T> {
    @ApiModelProperty(notes = "签名")
    private String sign;

    @ApiModelProperty(notes = "系统ID")
    private String sysId;

    @ApiModelProperty(notes = "时间")
    private String time;

    @ApiModelProperty(notes = "token")
    private String token;

    @ApiModelProperty(notes = "用户ID")
    private Integer userId;

    @ApiModelProperty(notes = "请求数据")
    private T data;

    public Request setData(T data) {
        this.data = data;
        return this;
    }
}
