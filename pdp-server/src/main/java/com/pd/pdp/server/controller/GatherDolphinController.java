package com.pd.pdp.server.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.gather.constant.Constant;
import com.pd.pdp.server.base.Request;
import com.pd.pdp.server.base.Result;
import com.pd.pdp.server.base.ResultGenerator;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.DataSourcesInfo;
import com.pd.pdp.server.entity.GatherDolphinInfo;
import com.pd.pdp.server.entity.GatherTypeInfo;
import com.pd.pdp.server.service.DataSourcesService;
import com.pd.pdp.server.service.GatherDolphinService;
import com.pd.pdp.server.utils.StringUtil;
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
import java.util.Arrays;
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
    @PostMapping("/job_list")
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

        //user
        gatherDolphinInfo.setCreateUserId(request.getUserId());
        gatherDolphinInfo.setUpdateUserId(request.getUserId());
        //time
        gatherDolphinInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        gatherDolphinInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        gatherDolphinServiceImpl.insert(gatherDolphinInfo);

        return ResultGenerator.getSuccessResult();
    }

    @ApiOperation(value = "批量删除dolphin任务配置信息")
    @PostMapping(value = "/job_delete")
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

        gatherDolphinServiceImpl.deleteBatchById(ids);

        return ResultGenerator.getSuccessResult();
    }

    @ApiOperation(value = "更新dolphin任务配置信息")
    @PostMapping("/job_update")
    public Result dataSourcesUpdate(@RequestBody Request<GatherDolphinInfo> request) {
        GatherDolphinInfo gatherDolphinInfo = request.getData();
        gatherDolphinInfo.setUpdateUserId(request.getUserId());
        gatherDolphinInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        gatherDolphinServiceImpl.update(gatherDolphinInfo);

        return ResultGenerator.getSuccessResult();
    }

    @ApiOperation(value = "获取数据源下所有db")
    @PostMapping("/db")
    public Result<List<Map<String, String>>> selectDBByDatasource(@RequestBody Request<JSONObject> request) {
        String id = request.getData().getString("id");
        DataSourcesInfo dataSourcesInfo = dataSourcesServiceImpl.selectById(Integer.valueOf(id));
        List<Map<String, String>> dbs = gatherDolphinServiceImpl.selectDBByDatasource(dataSourcesInfo);
        return ResultGenerator.getSuccessResult(dbs);
    }

    @ApiOperation(value = "获取数据源db下的所有table")
    @PostMapping("/table")
    public Result<List<Map<String, String>>> selectTableByDB(@RequestBody Request<JSONObject> request) {
        String id = request.getData().getString("id");
        DataSourcesInfo dataSourcesInfo = dataSourcesServiceImpl.selectById(Integer.valueOf(id));


        String db = request.getData().getString("db");
        List<Map<String, String>> tables = gatherDolphinServiceImpl.selectTablesByDB(db, dataSourcesInfo);
        return ResultGenerator.getSuccessResult(tables);
    }

    // @PostMapping("/dolphin_project")
    // public Result<List<Map<String, String>>> getDolphinProject(@RequestBody Request<JSONObject> request) {
    //     String id = request.getData().getString("id");
    //     DataSourcesInfo dataSourcesInfo = dataSourcesServiceImpl.selectById(Integer.valueOf(id));
    //     if (!dataSourcesInfo.getDataSourceType().equalsIgnoreCase("http")) {
    //         return ResultGenerator.getFailResult("it's not a http data source!");
    //     }
    //     List<Map<String, String>> tables = gatherDolphinServiceImpl.getDolphinProject(dataSourcesInfo);
    //     return ResultGenerator.getSuccessResult(tables);
    // }

    @ApiOperation(value = "获取同步方式")
    @PostMapping("/sync_type")
    public Result<List<GatherTypeInfo>> selectSyncType(@RequestBody Request<JSONObject> request) {
        List<GatherTypeInfo> gatherTypeInfos = gatherDolphinServiceImpl.selectSyncType();
        return ResultGenerator.getSuccessResult(gatherTypeInfos);
    }

    @ApiOperation(value = "初始化任务")
    @PostMapping("/init_job")
    public Result initJob(@RequestBody Request<JSONObject> request) {
        List<String> ids;
        if (!StringUtil.isEmpty(request.getData().get("ids"), true)) {
            ids = Arrays.asList(request.getData().get("ids").toString().split(","));
        } else {
            return ResultGenerator.getFailResult("初始化失败，请检查参数");
        }
        if (ids == null || ids.size() == 0) {
            return ResultGenerator.getFailResult("初始化失败，请检查参数");
        }

        String response = gatherDolphinServiceImpl.initJob(ids);
        if (response != null && JSONObject.parseObject(response).getInteger(Constant.CODE) == 0) {
            return ResultGenerator.getSuccessResult("初始化完成，请手动执行，完成后重新创建定时任务");
        }

        return ResultGenerator.getFailResult(response);
    }

    @ApiOperation(value = "使用模板创建dolphin任务")
    @PostMapping("/create_job_by_template")
    public Result createJobByTemplate(@RequestBody Request<JSONObject> request) {
        List<String> ids;
        if (!StringUtil.isEmpty(request.getData().get("ids"), true)) {
            ids = Arrays.asList(request.getData().get("ids").toString().split(","));
        } else {
            return ResultGenerator.getFailResult("创建失败，请检查参数");
        }
        if (ids == null || ids.size() == 0) {
            return ResultGenerator.getFailResult("创建失败，请检查参数");
        }

        String response = gatherDolphinServiceImpl.createJobByTemplate(ids);
        if (response != null && JSONObject.parseObject(response).getInteger(Constant.CODE) == 0) {
            return ResultGenerator.getSuccessResult();
        }

        return ResultGenerator.getFailResult(response);
    }

    @ApiOperation(value = "上线dolphin任务")
    @PostMapping("/online")
    public Result online(@RequestBody Request<JSONObject> request) {
        List<String> ids;
        if (!StringUtil.isEmpty(request.getData().get("ids"), true)) {
            ids = Arrays.asList(request.getData().get("ids").toString().split(","));
        } else {
            return ResultGenerator.getFailResult("上线失败，请检查参数");
        }
        if (ids == null || ids.size() == 0) {
            return ResultGenerator.getFailResult("上线失败，请检查参数");
        }

        String onlineResponse = gatherDolphinServiceImpl.online(ids);
        if (JSONObject.parseObject(onlineResponse).getInteger(Constant.CODE) == 0) {
            return ResultGenerator.getSuccessResult();
        }

        return ResultGenerator.getFailResult(onlineResponse);
    }

    @ApiOperation(value = "下线dolphin任务")
    @PostMapping("/offline")
    public Result offline(@RequestBody Request<JSONObject> request) {
        List<String> ids;
        if (!StringUtil.isEmpty(request.getData().get("ids"), true)) {
            ids = Arrays.asList(request.getData().get("ids").toString().split(","));
        } else {
            return ResultGenerator.getFailResult("下线失败，请检查参数");
        }
        if (ids == null || ids.size() == 0) {
            return ResultGenerator.getFailResult("下线失败，请检查参数");
        }

        String response = gatherDolphinServiceImpl.offline(ids);
        if (response != null && JSONObject.parseObject(response).getInteger(Constant.CODE) == 0) {
            return ResultGenerator.getSuccessResult();
        }

        return ResultGenerator.getFailResult("下线失败，请检查参数及任务");
    }

    @ApiOperation(value = "删除dolphin上的任务")
    @PostMapping("/del_dolphin_Job")
    public Result delDolphinJob(@RequestBody Request<JSONObject> request) {
        List<String> ids;
        if (!StringUtil.isEmpty(request.getData().get("ids"), true)) {
            ids = Arrays.asList(request.getData().get("ids").toString().split(","));
        } else {
            return ResultGenerator.getFailResult("删除失败，请检查参数");
        }
        if (ids == null || ids.size() == 0) {
            return ResultGenerator.getFailResult("删除失败，请检查参数");
        }

        String onlineResponse = gatherDolphinServiceImpl.delDolphinJob(ids);
        if (onlineResponse != null && JSONObject.parseObject(onlineResponse).getInteger(Constant.CODE) == 0) {
            return ResultGenerator.getSuccessResult();
        }

        return ResultGenerator.getFailResult("删除失败，请检查参数及任务");
    }

    @ApiOperation(value = "同步dolphin上的任务信息")
    @PostMapping("/sync_dolphin_Job_info")
    public Result syncDolphinJobJson(@RequestBody Request<JSONObject> request) {
        List<String> ids;
        if (!StringUtil.isEmpty(request.getData().get("ids"), true)) {
            ids = Arrays.asList(request.getData().get("ids").toString().split(","));
        } else {
            return ResultGenerator.getFailResult("获取失败，请检查参数");
        }
        if (ids == null || ids.size() == 0) {
            return ResultGenerator.getFailResult("获取失败，请检查参数");
        }

        JSONArray jsonArray = gatherDolphinServiceImpl.syncDolphinJobJson(ids);
        if (jsonArray != null) {
            return ResultGenerator.getSuccessResult();
        }

        return ResultGenerator.getFailResult("获取失败，请检查参数");
    }

    @ApiOperation(value = "使用同步的dolphin任务json创建任务")
    @PostMapping("/create_job_by_json")
    public Result createJobByJson(@RequestBody Request<JSONObject> request) {
        List<String> ids;
        if (!StringUtil.isEmpty(request.getData().get("ids"), true)) {
            ids = Arrays.asList(request.getData().get("ids").toString().split(","));
        } else {
            return ResultGenerator.getFailResult("获取失败，请检查参数");
        }
        if (ids == null || ids.size() == 0) {
            return ResultGenerator.getFailResult("获取失败，请检查参数");
        }

        String response = gatherDolphinServiceImpl.createJobByJson(ids);
        if (JSONObject.parseObject(response).getInteger(Constant.CODE) == 0) {
            return ResultGenerator.getSuccessResult();
        }

        return ResultGenerator.getFailResult("获取失败，请检查参数");
    }
}
