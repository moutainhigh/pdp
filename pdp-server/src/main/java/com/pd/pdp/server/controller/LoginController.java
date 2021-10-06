package com.pd.pdp.server.controller;

import com.pd.pdp.server.service.LoginService;
import com.pd.pdp.server.base.Request;
import com.pd.pdp.server.base.Result;
import com.pd.pdp.server.base.ResultGenerator;
import com.pd.pdp.server.dto.LoginDTO;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.LoginLogEntity;
import com.pd.pdp.server.utils.RedisUtil;
import com.pd.pdp.server.utils.VerifyUtil;
import com.pd.pdp.server.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author pdp
 * @Description
 **/

@RestController
@ComponentScan(basePackages = {"com.pd.pdp"})
@RequestMapping("/login")
@Api(tags = "登陆模块")
public class LoginController {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    LoginService loginService;

    @ApiOperation(value = "登陆接口")
    @PostMapping("/login")
    public Result<LoginDTO> login(@RequestBody Request<LoginVO> loginVORequest, HttpServletRequest request) {
        return loginService.login(loginVORequest.getData(), request);
    }

    @ApiOperation(value = "验证码接口")
    @GetMapping("/createImg")
    public void createImg(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg"); //设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache"); //设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        VerifyUtil randomValidateCode = new VerifyUtil();
        String randomString = randomValidateCode.getRandcode(response); //输出验证码图片
        //将生成的随机验证码存放到redis中
        redisUtil.set(request.getParameter("pCode"), randomString, 30 * 60);
    }

    @ApiOperation(value = "查询登陆日志接口")
    @PostMapping("/selectLoginLogList")
    public Result<PageDTO<LoginLogEntity>> selectLoginLogList(@RequestBody Request request) {
        return ResultGenerator.getSuccessResult(loginService.selectLoginLogList());
    }
}
