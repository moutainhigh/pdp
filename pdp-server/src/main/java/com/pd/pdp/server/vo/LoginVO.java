package com.pd.pdp.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author pdp
 * @Description 登陆接收参数
 **/

@Data
@ApiModel("登陆接收参数对象")
public class LoginVO {

    @ApiModelProperty(notes = "账号")
    private String account;

    @ApiModelProperty(notes = "密码")
    private String password;

    @ApiModelProperty(notes = "验证码")
    private String code;

    @ApiModelProperty(notes = "验证值")
    private String pCode;
}
