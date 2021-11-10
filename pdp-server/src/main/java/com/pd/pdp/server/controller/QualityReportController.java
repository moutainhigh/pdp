package com.pd.pdp.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.server.base.Request;
import com.pd.pdp.server.base.Result;
import com.pd.pdp.server.base.ResultGenerator;
import com.pd.pdp.server.entity.*;
import com.pd.pdp.server.service.QualityReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author zz
 * @date 2021/9/28
 */

@RestController
@RequestMapping("/quality_report")
@Api(tags = "数据校验报告")
public class QualityReportController {

    @Autowired
    QualityReportService qualityReportService;




    @ApiOperation(value = "获取当日数据校验结果校验信息")
    @PostMapping(value = "/check_result_list")
    public Result<List<CountCheckResult>> list(@RequestBody Request request) {
        List<CountCheckResult> countCheckResult = null;
        countCheckResult = qualityReportService.findToday();
        return ResultGenerator.getSuccessResult(countCheckResult);
    }

    @ApiOperation(value = "统计时间段内每天工作流数据校验结果成功失败数量")
    @PostMapping(value = "/every_day_check_result_statistic")
    public Result<List<DataCheckResultStatistics>> statisticsEveryDayDataQuality(@RequestBody Request request) {
        DataCheckResultStatistics dataCheckResult = JSONObject.parseObject(request.getData().toString(), DataCheckResultStatistics.class);
        int startUnixTime = dataCheckResult.getStartUnixTime();
        int endUnixTime = dataCheckResult.getEndUnixTime();
        List<DataCheckResultStatistics> dataCheckSuccessStatistics = qualityReportService.statisticsEveryDayDataQuality(startUnixTime, endUnixTime, 1);
        List<DataCheckResultStatistics> dataCheckFailStatistics = qualityReportService.statisticsEveryDayDataQuality(startUnixTime, endUnixTime, 0);
        List resultList = new ArrayList<List>();
        resultList.add(dataCheckSuccessStatistics);
        resultList.add(dataCheckFailStatistics);
        return ResultGenerator.getSuccessResult(resultList);
    }

}
