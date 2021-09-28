package com.pd.pdp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author pdp
 * @Description 角色分页接收类
 * @Date 2020-10-17 10:25 上午
 **/

@Data
@ApiModel("角色分页接收类对象")
public class RolePageVO {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("状态")
    private Integer status;
}
