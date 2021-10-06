package com.pd.pdp.server.dto;

import com.pd.pdp.server.utils.PageUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author pdp
 * @Description 分页返回类
 **/

@Data
@ApiModel("分页返回对象")
public class PageDTO<T> {

    @ApiModelProperty("分页参数")
    private PageUtil pageUtil;

    @ApiModelProperty("分页数据")
    private List<T> list;
}
