package com.pd.pdp.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author pdp
 * @Description 角色分页接收类
 **/

@Data
@ApiModel("角色分页接收类对象")
public class RolePageVO {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("状态")
    private Integer status;
}
