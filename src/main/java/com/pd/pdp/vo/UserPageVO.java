package com.pd.pdp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author pdp
 * @Description 用户分页接收类
 * @Date 2020-10-17 9:06 上午
 **/

@Data
@ApiModel("用户分页接收类对象")
public class UserPageVO {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("账号")
    private String account;
}
