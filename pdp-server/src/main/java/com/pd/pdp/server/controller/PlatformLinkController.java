package com.pd.pdp.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.server.base.Request;
import com.pd.pdp.server.base.Result;
import com.pd.pdp.server.base.ResultGenerator;
import com.pd.pdp.server.entity.PlatformInfo;
import com.pd.pdp.server.service.PlatformLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

/**
 * Description:
 *
 * @author lly
 * @date 2021/10/22
 */

@RestController
@RequestMapping("/platformLink")
@Api(tags = "平台链接管理模块")
public class PlatformLinkController {
    @Autowired
    PlatformLinkService platformLinkService;

    @ApiOperation(value = "获取大数据平台url")
    @PostMapping(value = "/list")
    public Result<List<PlatformInfo>> list(@RequestBody Request<JSONObject> request) {
        PlatformInfo platformInfo = JSONObject.parseObject(request.getData().getJSONObject("data").toString(), PlatformInfo.class);
        List<PlatformInfo> platformLinkList = null;
        String name = platformInfo.getName();
        if (name == null) {
            platformLinkList = platformLinkService.findAllByStatus();
        } else {
            platformLinkList = platformLinkService.findByName(name);
        }
        return ResultGenerator.getSuccessResult(platformLinkList);
    }

    @ApiOperation(value = "获取报表看板url")
    @PostMapping(value = "/report_board_list")
    public Result<List<PlatformInfo>> reportBoardList(@RequestBody Request<JSONObject> request) {
        List<PlatformInfo> platformLinkList = platformLinkService.findAllReportBoardByStatus();
        return ResultGenerator.getSuccessResult(platformLinkList);
    }

    @ApiOperation(value = "新增url")
    @PostMapping(value = "/add")
    public Result add(@RequestBody Request<JSONObject> request) {
        PlatformInfo platformInfo = JSONObject.parseObject(request.getData().toString(), PlatformInfo.class);
        platformInfo.setCreateUserId(request.getUserId());
        platformInfo.setUpdateUserId(request.getUserId());
        platformInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        platformInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        platformInfo.setStatus(1);
        platformLinkService.insert(platformInfo);

        return ResultGenerator.getSuccessResult();
    }

    @ApiOperation(value = "修改url")
    @PostMapping(value = "/update")
    public Result update(@RequestBody Request<JSONObject> request) {
        PlatformInfo platformInfo = JSONObject.parseObject(request.getData().toString(), PlatformInfo.class);
        platformInfo.setUpdateUserId(request.getUserId());
        platformInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        platformLinkService.update(platformInfo);

        return ResultGenerator.getSuccessResult();
    }
}
