package com.pd.pdp.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author ckw
 * @date 2021/10/27
 */

@Data
@ApiModel("gatherQuality分页接收类对象")
public class GatherQualityPageVo {

    @ApiModelProperty("系统名称")
    private String systemName;

    @ApiModelProperty("输入数据源库名")
    private String sourceDb;

    @ApiModelProperty("输入数据源表名")
    private String sourceTable;
}
