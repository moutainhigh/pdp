package com.pd.pdp.controller;

import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.base.Request;
import com.pd.pdp.base.Result;
import com.pd.pdp.base.ResultGenerator;
import com.pd.pdp.dto.PageDTO;
import com.pd.pdp.entity.UserEntity;
import com.pd.pdp.service.UserService;
import com.pd.pdp.utils.StringUtil;
import com.pd.pdp.vo.PageVO;
import com.pd.pdp.vo.UserPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @Author pdp
 * @Description
 * @Date 2020-10-16 4:24 下午
 **/
@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/insertUser")
    @ApiOperation(value = "新增用户")
    public Result insertUser(@RequestBody Request<UserEntity> request) {
        request.getData().setCreateUserId(request.getUserId());
        userService.save(request.getData());
        return ResultGenerator.getSuccessResult();
    }

    @PostMapping("/updateUser")
    @ApiOperation(value = "修改用户")
    public Result updateUser(@RequestBody Request<UserEntity> request) {
        request.getData().setUpdateUserId(request.getUserId());
        userService.update(request.getData());
        return ResultGenerator.getSuccessResult();
    }

    @PostMapping("/updatePassword")
    @ApiOperation(value = "修改密码")
    public Result<Object> updatePassword(@RequestBody Request<JSONObject> request) {
        String repPassword = request.getData().getString("repPassword");
        Result<Object> response = userService.updatePassword(Integer.valueOf(request.getUserId()), repPassword);
        return response;
    }

    @PostMapping("/selectUserList")
    @ApiOperation(value = "分页查询用户")
    public Result<PageDTO<UserEntity>> selectUserList(@RequestBody Request<PageVO<UserPageVO>> request) {
        PageDTO<UserEntity> pageDTO = userService.findByCondition(request.getData());
        return ResultGenerator.getSuccessResult(pageDTO);
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    @ApiOperation(value = "删除用户")
    public Result deleteUser(@RequestBody Request<JSONObject> request) {
        List<String> list;
        if (!StringUtil.isEmpty(request.getData().get("ids"), true)) {
            list = Arrays.asList(request.getData().get("ids").toString().split(","));
        } else {
            return ResultGenerator.getFailResult("用户删除失败，请检查参数");
        }
        if (list == null || list.size() == 0) {
            return ResultGenerator.getFailResult("用户删除失败，请检查参数");
        }
        userService.deleteByIds(list);
        return ResultGenerator.getSuccessResult();
    }
}
