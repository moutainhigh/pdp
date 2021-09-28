package com.pd.pdp.controller;

import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.base.Request;
import com.pd.pdp.base.Result;
import com.pd.pdp.base.ResultGenerator;
import com.pd.pdp.dto.PageDTO;
import com.pd.pdp.entity.DataSourcesInfo;
import com.pd.pdp.entity.DataSourcesTypeInfo;
import com.pd.pdp.service.DataSourcesService;
import com.pd.pdp.utils.StringUtil;
import com.pd.pdp.vo.DatasourcePageVO;
import com.pd.pdp.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@RestController
@RequestMapping("/datasource")
@Api(tags = "数据源模块")
public class DataSourcesController {

    @Autowired
    DataSourcesService dataSourcesServiceImpl;


    @ApiOperation(value = "获取当前用户下的所有数据源")
    @PostMapping(value = "/data_sources_list")
    public Result<PageDTO<DataSourcesInfo>> data_sources_list(@RequestBody Request<PageVO<DatasourcePageVO>> request) {

        PageDTO<DataSourcesInfo> dataSourcesInfos = dataSourcesServiceImpl.findByCondition(request.getData(), request.getUserId());
        return ResultGenerator.getSuccessResult(dataSourcesInfos);
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
    public Result add_data_sources(@RequestBody Request<JSONObject> request) {
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
    public Result data_sources_update(@RequestBody Request<DataSourcesInfo> request) {
        DataSourcesInfo dataSourcesInfo = request.getData();
        dataSourcesInfo.setCreateUserId(request.getUserId());
        dataSourcesInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        dataSourcesServiceImpl.update(dataSourcesInfo);

        return ResultGenerator.getSuccessResult();
    }


    @ApiOperation(value = "获取所有的数据源类型")
    @PostMapping(value = "/data_sources_type")
    public Result<List<DataSourcesTypeInfo>> data_sources_type() {
        List<DataSourcesTypeInfo> result = dataSourcesServiceImpl.selectDataSourcesType();

        return ResultGenerator.getSuccessResult(result);
    }

}
