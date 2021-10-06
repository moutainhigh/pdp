package com.pd.pdp.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author pdp
 * @Description
 **/

@Data
@ApiModel("按钮集合返回对象")
public class ButtonMapDTO {

    @ApiModelProperty("页面路径")
    private String url;

    @ApiModelProperty("按钮集合")
    private List<ButtonEntityDTO> buttonEntityDTO;
}
