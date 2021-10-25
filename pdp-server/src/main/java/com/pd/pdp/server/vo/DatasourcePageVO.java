package com.pd.pdp.server.vo;

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

    @ApiModelProperty("系统名称")
    private String systemName;

    @ApiModelProperty("数据源")
    private String dataSourceType;
}
