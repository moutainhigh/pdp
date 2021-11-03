package com.pd.pdp.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Description:
 *
 * @author ckw
 * @date 2021/10/27
 */

@Data
@ApiModel("qualityReport分页接收类对象")
public class QualityReportPageVo {


    @ApiModelProperty("输入起始日期")
    private Timestamp startTime;

    @ApiModelProperty("输入截至日期")
    private Timestamp endTime;
}
