package com.pd.pdp.server.controller;

import com.pd.pdp.server.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Controller
@Api(tags = "调度中心报表")
public class IndexController {

	@Resource
	private XxlJobService xxlJobService;


	@GetMapping("/")
	@ApiOperation(value = "跳转报表页面")
	public String index(Model model) {

		Map<String, Object> dashboardMap = xxlJobService.dashboardInfo();
		model.addAllAttributes(dashboardMap);

		return "index";
	}

    @PostMapping("/chartInfo")
	@ApiOperation(value = "获取报表统计数据")
	@ResponseBody
	public ReturnT<Map<String, Object>> chartInfo(Date startDate, Date endDate) {
        ReturnT<Map<String, Object>> chartInfo = xxlJobService.chartInfo(startDate, endDate);
        return chartInfo;
    }


	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
}
