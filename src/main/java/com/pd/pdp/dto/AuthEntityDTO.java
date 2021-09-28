package com.pd.pdp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author pdp
 * @Description
 * @Date 2020-10-15 4:45 下午
 **/

@Data
@ApiModel("权限返回对象")
public class AuthEntityDTO {
    @ApiModelProperty("权限ID")
    private Integer id;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("子级")
    private List<AuthEntityDTO> children;
}
