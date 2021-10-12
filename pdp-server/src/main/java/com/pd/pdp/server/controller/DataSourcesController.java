package com.pd.pdp.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.server.base.Request;
import com.pd.pdp.server.base.Result;
import com.pd.pdp.server.base.ResultGenerator;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.DataSourcesInfo;
import com.pd.pdp.server.entity.DataSourcesTypeInfo;
import com.pd.pdp.server.service.DataSourcesService;
import com.pd.pdp.server.utils.StringUtil;
import com.pd.pdp.server.vo.DatasourcePageVO;
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
@RequestMapping("/datasource")
@Api(tags = "数据源模块")
public class DataSourcesController {

    @Autowired
    DataSourcesService dataSourcesServiceImpl;


    @ApiOperation(value = "分页获取当前用户下的数据源")
    @PostMapping(value = "/data_sources_list")
    public Result<PageDTO<DataSourcesInfo>> dataSourcesList(@RequestBody Request<PageVO<DatasourcePageVO>> request) {

        PageDTO<DataSourcesInfo> dataSourcesInfos = dataSourcesServiceImpl.findByCondition(request.getData(), request.getUserId());
        return ResultGenerator.getSuccessResult(dataSourcesInfos);
    }

    @ApiOperation(value = "获取所有数据源")
    @PostMapping(value = "/data_sources_all")
    public Result<List<DataSourcesInfo>> dataSourcesAll(@RequestBody Request request) {

        List<DataSourcesInfo> dataSourcesAll = dataSourcesServiceImpl.selectAll();
        return ResultGenerator.getSuccessResult(dataSourcesAll);
    }


    @ApiOperation(value = "批量删除数据源")
    @PostMapping(value = "/data_sources_delete")
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

        dataSourcesServiceImpl.deleteBatchById(ids);

        return ResultGenerator.getSuccessResult();
    }


    @ApiOperation(value = "新增数据源")
    @PostMapping(value = "/add_data_sources")
    public Result addDataSources(@RequestBody Request<JSONObject> request) {
        DataSourcesInfo dataSourcesInfo = JSONObject.parseObject(request.getData().toString(), DataSourcesInfo.class);
        dataSourcesInfo.setCreateUserId(request.getUserId());
        dataSourcesInfo.setUpdateUserId(request.getUserId());
        dataSourcesInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        dataSourcesInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        dataSourcesServiceImpl.insert(dataSourcesInfo);

        return ResultGenerator.getSuccessResult();
    }


    @ApiOperation(value = "更新数据源")
    @PostMapping("/data_sources_update")
    public Result dataSourcesUpdate(@RequestBody Request<DataSourcesInfo> request) {
        DataSourcesInfo dataSourcesInfo = request.getData();
        dataSourcesInfo.setCreateUserId(request.getUserId());
        dataSourcesInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        dataSourcesServiceImpl.update(dataSourcesInfo);

        return ResultGenerator.getSuccessResult();
    }


    @ApiOperation(value = "获取所有的数据源类型")
    @PostMapping(value = "/data_sources_type")
    public Result<List<DataSourcesTypeInfo>> dataSourcesType() {
        List<DataSourcesTypeInfo> result = dataSourcesServiceImpl.selectDataSourcesType();

        return ResultGenerator.getSuccessResult(result);
    }

}
