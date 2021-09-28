package com.pd.pdp.controller;

import com.pd.pdp.base.Request;
import com.pd.pdp.base.Result;
import com.pd.pdp.base.ResultGenerator;
import com.pd.pdp.dto.LoginDTO;
import com.pd.pdp.dto.PageDTO;
import com.pd.pdp.entity.LoginLogEntity;
import com.pd.pdp.service.LoginService;
import com.pd.pdp.utils.RedisUtil;
import com.pd.pdp.utils.VerifyUtil;
import com.pd.pdp.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @Date 2020-10-14 4:19 下午
 **/

@Log4j2
@RestController
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
        try {
            response.setContentType("image/jpeg"); //设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache"); //设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            VerifyUtil randomValidateCode = new VerifyUtil();
            String randomString = randomValidateCode.getRandcode(response); //输出验证码图片
            //将生成的随机验证码存放到redis中
            redisUtil.set(request.getParameter("pCode"), randomString, 30 * 60);
        } catch (Exception e) {
            log.error("获取验证码异常：", e);
        }
    }

    @ApiOperation(value = "查询登陆日志接口")
    @PostMapping("/selectLoginLogList")
    public Result<PageDTO<LoginLogEntity>> selectLoginLogList(@RequestBody Request request){
        return ResultGenerator.getSuccessResult(loginService.selectLoginLogList());
    }
}
