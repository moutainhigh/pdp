package com.pd.pdp.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author zhousp
 * @date 2021/10/25
 */
@Data
@ApiModel("工作流实例分页接收类对象")
public class ProcessInstancePageVO {

    @ApiModelProperty("工作流实例编号")
    private String dsId;

    @ApiModelProperty("工作流名称")
    private String processName;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("工作流实例状态")
    private String state;

    @ApiModelProperty("调度时间")
    private String schedulingTime;

    @ApiModelProperty("执行用户")
    private String executor;
}
