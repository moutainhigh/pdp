package com.pd.pdp.controller;

import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.base.Request;
import com.pd.pdp.base.Result;
import com.pd.pdp.base.ResultGenerator;
import com.pd.pdp.dto.AuthEntityDTO;
import com.pd.pdp.dto.PageDTO;
import com.pd.pdp.entity.AuthEntity;
import com.pd.pdp.service.AuthService;
import com.pd.pdp.utils.StringUtil;
import com.pd.pdp.vo.AuthPageVO;
import com.pd.pdp.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @Author pdp
 * @Description
 * @Date 2020-10-17 10:22 下午
 **/

@RestController
@RequestMapping("/auth")
@Api(tags = "权限模块")
public class AuthController {
    @Autowired
    AuthService authService;

    @ApiOperation(value = "新增权限接口")
    @RequestMapping("/insertAuth")
    public Result insertAuth(@RequestBody Request<AuthEntity> request){
        request.getData().setCreateUserId(request.getUserId());
        authService.save(request.getData());
        return ResultGenerator.getSuccessResult();
    }

    @ApiOperation(value = "修改权限接口")
    @RequestMapping(value = "/updateAuth", method = RequestMethod.POST)
    public Result updateAuth(@RequestBody Request<AuthEntity> request) {
        request.getData().setUpdateUserId(request.getUserId());
        authService.update(request.getData());
        return ResultGenerator.getSuccessResult();
    }

    @ApiOperation(value = "修改权限状态")
    @RequestMapping(value = "/updateAuthStatus", method = RequestMethod.POST)
    public Result updateAuthStatus(@RequestBody Request<JSONObject> request) {
        List<String> list;
        if (!StringUtil.isEmpty(request.getData().get("ids"), true)) {
            list = Arrays.asList(request.getData().get("ids").toString().split(","));
        } else {
            return ResultGenerator.getFailResult("角色状态跟新失败，请检查参数");
        }
        if (list == null || list.size() == 0) {
            return ResultGenerator.getFailResult("角色状态跟新失败，请检查参数");
        }
        int count = authService.updateAuthStatus(list, request.getData().getInteger("status"));
        if (count > 0) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("权限已绑定角色，请解绑后重试");
        }
    }

    @ApiOperation(value = "查询权限列表")
    @RequestMapping(value = "/selectAuthList", method = RequestMethod.POST)
    public Result<PageDTO<AuthEntity>> selectAuthList(@RequestBody Request<PageVO<AuthPageVO>> request) {
        PageDTO<AuthEntity> pageVO = authService.findByCondition(request.getData());
        return ResultGenerator.getSuccessResult(pageVO);
    }

    @ApiOperation(value = "查询权限树")
    @RequestMapping(value = "/selectAuthTree", method = RequestMethod.POST)
    public Result<List<AuthEntityDTO>> selectAuthTree(@RequestBody Request request) {
       List<AuthEntityDTO> authEntityDTOS = authService.selectAuthTree();
        return ResultGenerator.getSuccessResult(authEntityDTOS);
    }

    @ApiOperation(value = "查询所有权限接口")
    @RequestMapping(value = "/selectAllAuth", method = RequestMethod.POST)
    public Result<List<AuthEntity>> selectAllAuth(@RequestBody Request request) {
        List<AuthEntity> authEntities = authService.findAll();
        return ResultGenerator.getSuccessResult(authEntities);
    }
}
