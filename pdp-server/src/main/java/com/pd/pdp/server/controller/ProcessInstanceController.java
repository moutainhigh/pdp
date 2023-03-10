package com.pd.pdp.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.server.base.Request;
import com.pd.pdp.server.base.Result;
import com.pd.pdp.server.base.ResultGenerator;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.ProcessInstanceInfo;
import com.pd.pdp.server.entity.ProcessInstanceRunStatusStatistics;
import com.pd.pdp.server.service.ProcessInstanceService;
import com.pd.pdp.server.vo.PageVO;
import com.pd.pdp.server.vo.ProcessInstancePageVO;
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
 * @author zhousp
 * @date 2021/10/25
 */

@RestController
@RequestMapping("/process_instance")
@Api(tags = "工作流实例模块")
public class ProcessInstanceController {

    @Autowired
    ProcessInstanceService processInstanceServiceImpl;


    @ApiOperation(value = "分页获取工作流实例")
    @PostMapping(value = "/process_instance_list")
    public Result<PageDTO<ProcessInstanceInfo>> processInstanceList(@RequestBody Request<PageVO<ProcessInstancePageVO>> request) {

        PageDTO<ProcessInstanceInfo> processInstanceInfos = processInstanceServiceImpl.findByCondition(request.getData());
        return ResultGenerator.getSuccessResult(processInstanceInfos);
    }

    @ApiOperation(value = "统计时间段内每日任务调度成功失败数量")
    @PostMapping(value = "/every_day_run_status_statistics")
    public Result<List<ProcessInstanceRunStatusStatistics>> everyDayRunStatusStatistics(@RequestBody Request request) {
        ProcessInstanceRunStatusStatistics processInstanceRunStatus = JSONObject.parseObject(request.getData().toString(), ProcessInstanceRunStatusStatistics.class);
        int startUnixTime = processInstanceRunStatus.getStartUnixTime();
        int endUnixTime = processInstanceRunStatus.getEndUnixTime();
        List<ProcessInstanceRunStatusStatistics> processInstanceSuccessList = processInstanceServiceImpl.statisticsEveryDayRunStatus(startUnixTime, endUnixTime, "SUCCESS");
        List<ProcessInstanceRunStatusStatistics> processInstanceFailList = processInstanceServiceImpl.statisticsEveryDayRunStatus(startUnixTime, endUnixTime, "FAILURE");
        List resultList = new ArrayList<List>();
        resultList.add(processInstanceSuccessList);
        resultList.add(processInstanceFailList);
        return ResultGenerator.getSuccessResult(resultList);
    }

}
