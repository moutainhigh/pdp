package com.pd.pdp.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/9
 */

@Data
@ApiModel("dolphin任务分页接收类对象")
public class GatherDolphinPageVo {

    @ApiModelProperty("任务说明")
    private String gatherContext;

    @ApiModelProperty("输入数据源库名")
    private String databaseNameInput;

    @ApiModelProperty("同步方式")
    private String syncType;
}
