package com.pd.pdp.server.controller;
import com.pd.pdp.server.core.model.XxlJobGroup;
import com.pd.pdp.server.core.model.XxlJobInfo;
import com.pd.pdp.server.core.route.ExecutorRouteStrategyEnum;
import com.pd.pdp.server.core.scheduler.MisfireStrategyEnum;
import com.pd.pdp.server.core.scheduler.ScheduleTypeEnum;
import com.pd.pdp.server.core.thread.JobScheduleHelper;
import com.pd.pdp.server.core.thread.JobTriggerPoolHelper;
import com.pd.pdp.server.core.trigger.TriggerTypeEnum;
import com.pd.pdp.server.core.util.I18nUtil;
import com.pd.pdp.server.mapper.XxlJobGroupDao;
import com.pd.pdp.server.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.job.core.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Controller
@RequestMapping("/jobinfo")
@Api(tags = "调度任务信息")
public class JobInfoController {
	private static Logger logger = LoggerFactory.getLogger(JobInfoController.class);

	@Resource
	private XxlJobGroupDao xxlJobGroupDao;
	@Resource
	private XxlJobService xxlJobService;
	
	@GetMapping
	@ApiOperation(value = "调度任务页面")
	public String index(HttpServletRequest request, Model model, @RequestParam(required = false, defaultValue = "-1") int jobGroup) {

		// 枚举-字典
		model.addAttribute("ExecutorRouteStrategyEnum", ExecutorRouteStrategyEnum.values());	    // 路由策略-列表
		model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());								// Glue类型-字典
		model.addAttribute("ExecutorBlockStrategyEnum", ExecutorBlockStrategyEnum.values());	    // 阻塞处理策略-字典
		model.addAttribute("ScheduleTypeEnum", ScheduleTypeEnum.values());	    				// 调度类型
		model.addAttribute("MisfireStrategyEnum", MisfireStrategyEnum.values());	    			// 调度过期策略

		// 执行器列表
		List<XxlJobGroup> jobGroupList_all =  xxlJobGroupDao.findAll();


		model.addAttribute("JobGroupList", jobGroupList_all);
		model.addAttribute("jobGroup", jobGroup);

		return "jobinfo/jobinfo.index";
	}

	
	@PostMapping("/pageList")
	@ResponseBody
	@ApiOperation(value = "调度任务列表")
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
			@RequestParam(required = false, defaultValue = "10") int length,
			int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {
		
		return xxlJobService.pageList(start, length, jobGroup, triggerStatus, jobDesc, executorHandler, author);
	}
	
	@PostMapping("/add")
	@ResponseBody
	@ApiOperation(value = "新增调度任务")
	public ReturnT<String> add(XxlJobInfo jobInfo) {
		return xxlJobService.add(jobInfo);
	}
	
	@PostMapping("/update")
	@ResponseBody
	@ApiOperation(value = "更新调度任务")
	public ReturnT<String> update(XxlJobInfo jobInfo) {
		return xxlJobService.update(jobInfo);
	}
	
	@PostMapping("/remove")
	@ResponseBody
	@ApiOperation(value = "删除调度任务")
	public ReturnT<String> remove(int id) {
		return xxlJobService.remove(id);
	}
	
	@PostMapping("/stop")
	@ResponseBody
	@ApiOperation(value = "停止调度任务")
	public ReturnT<String> pause(int id) {
		return xxlJobService.stop(id);
	}
	
	@PostMapping("/start")
	@ResponseBody
	@ApiOperation(value = "启动调度任务")
	public ReturnT<String> start(int id) {
		return xxlJobService.start(id);
	}
	
	@PostMapping("/trigger")
	@ResponseBody
	@ApiOperation(value = "触发调度任务")
	//@PermissionLimit(limit = false)
	public ReturnT<String> triggerJob(int id, String executorParam, String addressList) {
		// force cover job param
		if (executorParam == null) {
			executorParam = "";
		}

		JobTriggerPoolHelper.trigger(id, TriggerTypeEnum.MANUAL, -1, null, executorParam, addressList);
		return ReturnT.SUCCESS;
	}

	@PostMapping("/nextTriggerTime")
	@ResponseBody
	@ApiOperation(value = "调度任务下次执行时间")
	public ReturnT<List<String>> nextTriggerTime(String scheduleType, String scheduleConf) {

		XxlJobInfo paramXxlJobInfo = new XxlJobInfo();
		paramXxlJobInfo.setScheduleType(scheduleType);
		paramXxlJobInfo.setScheduleConf(scheduleConf);

		List<String> result = new ArrayList<>();
		try {
			Date lastTime = new Date();
			for (int i = 0; i < 5; i++) {
				lastTime = JobScheduleHelper.generateNextValidTime(paramXxlJobInfo, lastTime);
				if (lastTime != null) {
					result.add(DateUtil.formatDateTime(lastTime));
				} else {
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ReturnT<List<String>>(ReturnT.FAIL_CODE, (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_unvalid")) + e.getMessage());
		}
		return new ReturnT<List<String>>(result);

	}
	
}
