package com.pd.pdp.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.server.base.Request;
import com.pd.pdp.server.base.Result;
import com.pd.pdp.server.base.ResultGenerator;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.*;
import com.pd.pdp.server.service.DataSourcesService;
import com.pd.pdp.server.service.GatherQualityService;
import com.pd.pdp.server.service.QualityReportService;
import com.pd.pdp.server.utils.StringUtil;
import com.pd.pdp.server.vo.GatherQualityPageVo;
import com.pd.pdp.server.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Arrays;
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



}
