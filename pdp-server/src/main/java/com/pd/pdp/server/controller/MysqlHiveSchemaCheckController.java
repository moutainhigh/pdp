package com.pd.pdp.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.server.base.Request;
import com.pd.pdp.server.base.Result;
import com.pd.pdp.server.base.ResultGenerator;
import com.pd.pdp.server.entity.MysqlHiveSchemaCheck;
import com.pd.pdp.server.service.MysqlHiveSchemaCheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 *
 * @author lly
 * @date 2021/10/23
 */

@RestController
@RequestMapping("/metaData/change")
@Api(tags = "元数据变更报告模块")
public class MysqlHiveSchemaCheckController {
    @Autowired
    MysqlHiveSchemaCheckService mysqlHiveSchemaCheckService;

    @ApiOperation(value = "获取今日元数据变更")
    @PostMapping(value = "/list")
    public Result<List<MysqlHiveSchemaCheck>> list(@RequestBody Request request) {
        MysqlHiveSchemaCheck mysqlHiveSchemaCheck = JSONObject.parseObject(request.getData().toString(), MysqlHiveSchemaCheck.class);
        List<MysqlHiveSchemaCheck> MysqlHiveSchemaCheckToday = null;
        MysqlHiveSchemaCheckToday = mysqlHiveSchemaCheckService.findToday();
        return ResultGenerator.getSuccessResult(MysqlHiveSchemaCheckToday);
    }
}
