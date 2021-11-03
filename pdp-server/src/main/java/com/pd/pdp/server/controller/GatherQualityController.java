package com.pd.pdp.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.server.base.Request;
import com.pd.pdp.server.base.Result;
import com.pd.pdp.server.base.ResultGenerator;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.*;
import com.pd.pdp.server.service.DataSourcesService;
import com.pd.pdp.server.service.GatherQualityService;

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
import java.util.UUID;

/**
 * Description:
 *
 * @author zz
 * @date 2021/9/28
 */

@RestController
@RequestMapping("/gather_quality")
@Api(tags = "数据校验模块")
public class GatherQualityController {

    @Autowired
    DataSourcesService dataSourcesServiceImpl;

    @Autowired
    GatherQualityService gatherQualityServiceImpl;


    @ApiOperation(value = "获取校验任务信息")
    @PostMapping(value = "/quality_job_list")
    public Result<PageDTO<GatherQualityInfo>> qualityJobList(@RequestBody Request<PageVO<GatherQualityPageVo>> request) {

        PageDTO<GatherQualityInfo> gatherQualityInfos = gatherQualityServiceImpl.findByCondition(request.getData(), request.getUserId());
        return ResultGenerator.getSuccessResult(gatherQualityInfos);
    }

    @ApiOperation(value = "新增校验任务配置信息")
    @PostMapping(value = "/quality_job_add")
    public Result qualityJobAdd(@RequestBody Request<JSONObject> request) {


        GatherQualityInfo gatherQualityInfo = JSONObject.parseObject(request.getData().toString(), GatherQualityInfo.class);
        GatherDolphinInfo gatherDolphinInfo = gatherQualityServiceImpl.selectGatherJobId(gatherQualityInfo);


        if (gatherDolphinInfo == null){
            return ResultGenerator.getFailResult("新增校验任务失败，请检查是否已经添加同步任务");
        }
        String gatherJobInfo = gatherDolphinInfo.getGatherJobInfo();
        int datasourceOutputId = gatherDolphinInfo.getDatasourceOutput();
        String rules = gatherQualityInfo.getRulesId();
        String rulesId = rules.substring(1, rules.length() - 1);

        gatherQualityInfo.setRulesId(rulesId);
        gatherQualityInfo.setGatherJobId(gatherJobInfo);
        gatherQualityInfo.setDatasourceOutputId(datasourceOutputId);
        gatherQualityInfo.setCreateUserId(request.getUserId());
        gatherQualityInfo.setUpdateUserId(request.getUserId());
        gatherQualityInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        gatherQualityInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        gatherQualityServiceImpl.insert(gatherQualityInfo);

        return ResultGenerator.getSuccessResult();
    }

    @ApiOperation(value = "批量删除dolphin任务配置信息")
    @PostMapping(value = "/quality_job_delete")
    public Result deleteIds(@RequestBody Request<JSONObject> request) {

        List<String> ids;
        if (!StringUtil.isEmpty(request.getData().get("ids"), true)) {
            ids = Arrays.asList(request.getData().get("ids").toString().split(","));
        } else {
            return ResultGenerator.getFailResult("用户删除失败，请检查参数");
        }
        if (ids == null || ids.size() == 0) {
            return ResultGenerator.getFailResult("用户删除失败，请检查参数");
        }

        gatherQualityServiceImpl.deleteBatchById(ids);

        return ResultGenerator.getSuccessResult();
    }

    @ApiOperation(value = "更新dolphin任务配置信息")
    @PostMapping("/quality_job_update")
    public Result dataSourcesUpdate(@RequestBody Request<GatherQualityInfo> request) {

        GatherQualityInfo gatherQualityInfo = request.getData();
        GatherDolphinInfo gatherDolphinInfo = gatherQualityServiceImpl.selectGatherJobId(gatherQualityInfo);
        if (gatherDolphinInfo == null){
            return ResultGenerator.getFailResult("新增校验任务失败，请检查是否已经添加同步任务");
        }

        gatherQualityInfo.setGatherJobId(gatherDolphinInfo.getGatherJobId());
        gatherQualityInfo.setDatasourceOutputId(gatherDolphinInfo.getDatasourceOutput());
        gatherQualityInfo.setUpdateUserId(request.getUserId());
        gatherQualityInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        gatherQualityServiceImpl.update(gatherQualityInfo);

        return ResultGenerator.getSuccessResult();
    }

    @ApiOperation(value = "获取所有校验规则")
    @PostMapping(value = "/check_rules")
    public Result<List<QualityRulesInfo>> checkRules() {
        List<QualityRulesInfo> result = gatherQualityServiceImpl.checkRules();

        return ResultGenerator.getSuccessResult(result);
    }



}
