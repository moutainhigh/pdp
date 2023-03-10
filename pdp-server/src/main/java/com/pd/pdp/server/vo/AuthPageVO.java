package com.pd.pdp.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author pdp
 * @Description
 **/

@Data
@ApiModel("权限分页接收参数对象")
public class AuthPageVO {

    @ApiModelProperty("名称")
     private String name;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("状态")
    private Integer status;
}
