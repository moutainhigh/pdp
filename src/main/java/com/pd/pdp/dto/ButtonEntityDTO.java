package com.pd.pdp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author pdp
 * @Description
 * @Date 2020-10-15 4:45 下午
 **/

@Data
@ApiModel("按钮返回对象")
public class ButtonEntityDTO {

    @ApiModelProperty("按钮ID")
    private Integer id;

    @ApiModelProperty("文本")
    private String text;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("图标")
    private String icon;
}
