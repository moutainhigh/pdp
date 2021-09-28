package com.pd.pdp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author zz
 * @date 2021/9/28
 */
@Data
@ApiModel("数据源分页接收类对象")
public class DatasourcePageVO {

    @ApiModelProperty("数据源说明")
    private String dataSourceContext;

    @ApiModelProperty("连接串")
    private String url;

    @ApiModelProperty("数据源")
    private String dataSourceType;
}
