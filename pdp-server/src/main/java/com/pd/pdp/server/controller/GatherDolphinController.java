package com.pd.pdp.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.server.base.Request;
import com.pd.pdp.server.base.Result;
import com.pd.pdp.server.base.ResultGenerator;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.DataSourcesInfo;
import com.pd.pdp.server.entity.GatherDolphinInfo;
import com.pd.pdp.server.entity.GatherTypeInfo;
import com.pd.pdp.server.service.DataSourcesService;
import com.pd.pdp.server.service.GatherDolphinService;
import com.pd.pdp.server.vo.GatherDolphinPageVo;
import com.pd.pdp.server.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/8
 */

@RestController
@RequestMapping("/gather_dolphin")
@Api(tags = "dolphin采集任务信息模块")
public class GatherDolphinController {

    @Autowired
    GatherDolphinService gatherDolphinServiceImpl;

    @Autowired
    DataSourcesService dataSourcesServiceImpl;

    @ApiOperation(value = "获取dolphin任务配置信息")
    @RequestMapping("/job_list")
    public Result<PageDTO<GatherDolphinInfo>> jobList(@RequestBody Request<PageVO<GatherDolphinPageVo>> request) {
        PageDTO<GatherDolphinInfo> gatherDolphinInfoInfos = gatherDolphinServiceImpl.findByCondition(request.getData(), request.getUserId());
        return ResultGenerator.getSuccessResult(gatherDolphinInfoInfos);
    }

    @ApiOperation(value = "新增dolphin任务配置信息")
    @PostMapping(value = "/add_job")
    public Result addjob(@RequestBody Request<JSONObject> request) {


        GatherDolphinInfo gatherDolphinInfo = JSONObject.parseObject(request.getData().toString(), GatherDolphinInfo.class);
        //init job id.
        UUID jobId = UUID.randomUUID();
        gatherDolphinInfo.setGatherJobId(jobId.toString());

        //datasource type
        DataSourcesInfo dataSourcesInfoInput = dataSourcesServiceImpl.selectById(gatherDolphinInfo.getDatasourceInput());
        DataSourcesInfo dataSourcesInfoOutput = dataSourcesServiceImpl.selectById(gatherDolphinInfo.getDatasourceOutput());
        gatherDolphinInfo.setDatasourceTypeInput(dataSourcesInfoInput.getDataSourceType());
        gatherDolphinInfo.setDatasourceTypeOutput(dataSourcesInfoOutput.getDataSourceType());

        //user
        gatherDolphinInfo.setCreateUserId(request.getUserId());
        gatherDolphinInfo.setUpdateUserId(request.getUserId());
        //time
        gatherDolphinInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        gatherDolphinInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        gatherDolphinServiceImpl.insert(gatherDolphinInfo);

        return ResultGenerator.getSuccessResult();
    }

    @RequestMapping("/db")
    public Result<List<Map<String, String>>> selectDBByDatasource(@RequestBody Request<JSONObject> request) {
        String id = request.getData().getString("id");
        DataSourcesInfo dataSourcesInfo = dataSourcesServiceImpl.selectById(Integer.valueOf(id));
        List<Map<String, String>> dbs = gatherDolphinServiceImpl.selectDBByDatasource(dataSourcesInfo);
        return ResultGenerator.getSuccessResult(dbs);
    }

    @RequestMapping("/table")
    public Result<List<Map<String, String>>> selectTableByDB(@RequestBody Request<JSONObject> request) {
        String id = request.getData().getString("id");
        DataSourcesInfo dataSourcesInfo = dataSourcesServiceImpl.selectById(Integer.valueOf(id));


        String db = request.getData().getString("db");
        List<Map<String, String>> tables = gatherDolphinServiceImpl.selectTablesByDB(db, dataSourcesInfo);
        return ResultGenerator.getSuccessResult(tables);
    }

    // @RequestMapping("/dolphin_project")
    // public Result<List<Map<String, String>>> getDolphinProject(@RequestBody Request<JSONObject> request) {
    //     String id = request.getData().getString("id");
    //     DataSourcesInfo dataSourcesInfo = dataSourcesServiceImpl.selectById(Integer.valueOf(id));
    //     if (!dataSourcesInfo.getDataSourceType().equalsIgnoreCase("http")) {
    //         return ResultGenerator.getFailResult("it's not a http data source!");
    //     }
    //     List<Map<String, String>> tables = gatherDolphinServiceImpl.getDolphinProject(dataSourcesInfo);
    //     return ResultGenerator.getSuccessResult(tables);
    // }

    @RequestMapping("/sync_type")
    public Result<List<GatherTypeInfo>> selectSyncType(@RequestBody Request request) {
        List<GatherTypeInfo> gatherTypeInfos = gatherDolphinServiceImpl.selectSyncType();
        return ResultGenerator.getSuccessResult(gatherTypeInfos);
    }
}
