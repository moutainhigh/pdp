package com.pd.pdp.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.server.base.Result;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.service.RoleService;
import com.pd.pdp.server.base.Request;
import com.pd.pdp.server.base.ResultGenerator;
import com.pd.pdp.server.entity.RoleEntity;
import com.pd.pdp.server.utils.StringUtil;
import com.pd.pdp.server.vo.PageVO;
import com.pd.pdp.server.vo.RolePageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author pdp
 * @Description 角色模块
 **/


@RestController
@RequestMapping("/role")
@Api(tags = "角色模块")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "新增角色接口")
    @RequestMapping(value = "/insertRole", method = RequestMethod.POST)
    public Result insertRole(@RequestBody Request<RoleEntity> request) {
        request.getData().setCreateUserId(request.getUserId());
        request.getData().setCreateTime(new Date());
        roleService.save(request.getData());
        return ResultGenerator.getSuccessResult();
    }

    @ApiOperation(value = "修改角色接口")
    @RequestMapping(value = "/updateRole", method = RequestMethod.POST)
    public Result updateRole(@RequestBody Request<RoleEntity> request) {
        request.getData().setUpdateUserId(request.getUserId());
        request.getData().setUpdateTime(new Date());
        roleService.update(request.getData());
        return ResultGenerator.getSuccessResult();
    }

    @ApiOperation(value = "角色绑定权限接口")
    @RequestMapping(value = "/insertRoleAndAuth", method = RequestMethod.POST)
    public Result insertRoleAndAuth(@RequestBody Request<JSONObject> request) {
        // 权限id
        Integer roleId = request.getData().getInteger("roleId");
        // 权限id
        List<Integer> authIds = (List<Integer>) request.getData().get("authIds");
        roleService.insertRoleAndAuth(roleId, authIds);
        return ResultGenerator.getSuccessResult();
    }

    @ApiOperation(value = "修改权限状态接口")
    @RequestMapping(value = "/updateRoleStatus", method = RequestMethod.POST)
    public Result updateRoleStatus(@RequestBody Request<JSONObject> request) {
        List<String> list;
        if (!StringUtil.isEmpty(request.getData().get("ids"), true)) {
            list = Arrays.asList(request.getData().get("ids").toString().split(","));
        } else {
            return ResultGenerator.getFailResult("角色状态更新失败，请检查参数");
        }
        if (list == null || list.size() == 0) {
            return ResultGenerator.getFailResult("角色状态更新失败，请检查参数");
        }
        int count = roleService.updateRoleStatus(list, request.getData().getInteger("status"));
        if (count > 0) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("角色已绑定权限，请解绑后重试");
        }
    }

    @ApiOperation(value = "根据用户查询角色id接口")
    @RequestMapping(value = "/selectRoleIdsByUserId", method = RequestMethod.POST)
    public List<Integer> selectRoleIdsByUserId(@RequestBody Request<Integer> request) {
        return roleService.selectRoleIdsByUserId(request.getData());
    }

    @ApiOperation(value = "查询角色列表")
    @RequestMapping(value = "/selectRoleList", method = RequestMethod.POST)
    public Result<PageDTO<RoleEntity>> selectRoleList(@RequestBody Request<PageVO<RolePageVO>> request) {
        PageDTO<RoleEntity> roleEntities = roleService.selectRoleList(request.getData());
        return ResultGenerator.getSuccessResult(roleEntities);
    }

    @ApiOperation(value = "查询所有角色接口")
    @RequestMapping(value = "/selectAllRole", method = RequestMethod.POST)
    public Result<List<RoleEntity>> selectAllRole(@RequestBody Request request) {
        List<RoleEntity> roleEntities = roleService.selectAllRole();
        return ResultGenerator.getSuccessResult(roleEntities);
    }

    @ApiOperation(value = "根据角色id查询所有权限接口")
    @RequestMapping(value = "/selectAllAuthIdByRoleId", method = RequestMethod.POST)
    public Result<List<Integer>> selectAllAuthIdByRoleId(@RequestBody Request<JSONObject> request) {
        List<Integer> list = roleService.selectAllAuthIdByRoleId(request.getData().getInteger("roleId"));
        return ResultGenerator.getSuccessResult(list);
    }
}
