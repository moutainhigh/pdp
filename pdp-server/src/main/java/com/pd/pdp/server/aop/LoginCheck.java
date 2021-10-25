package com.pd.pdp.server.aop;

import com.pd.pdp.server.base.Request;
import com.pd.pdp.server.base.ResultGenerator;
import com.pd.pdp.server.utils.RedisUtil;
import com.pd.pdp.server.utils.StringUtil;
import com.pd.pdp.server.vo.LoginVO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author pdp
 * @Description 登陆前校验
 * @Date 2020-10-15 2:29 下午
 **/

@Aspect
@ComponentScan(basePackages = {"com.pd.pdp.common.utils"})
public class LoginCheck {

    @Autowired
    RedisUtil redisUtil;

    @Pointcut("execution(public * com.pd.pdp.server.controller.LoginController.login(..))")
    public void login(){}
    @Around("login()")
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Request<LoginVO> request = (Request<LoginVO>) args[0];
        LoginVO loginVO = request.getData();

        // 检验验证码错误次数
        String errorpw = "ERRORPW" + loginVO.getAccount();
        Integer count = 0;
        if (redisUtil.hasKey(errorpw)) {
            count = (Integer) redisUtil.get(errorpw);
            if (count >= 3) {
                // 密码错误3次
                return ResultGenerator.getFailResult("密码在30分钟内错误超过3次，请30分钟后重试");
            }
        }
        // 检验参数是否正确
        if (StringUtil.isEmpty(loginVO.getAccount(), true)) {
            // 账号不能为空
            return ResultGenerator.getFailResult("账号不能为空");
        }
        if (StringUtil.isEmpty(loginVO.getPassword(), true)) {
            // 密码不能为空
            return ResultGenerator.getFailResult("密码不能为空");
        }
        //关闭验证码
//        if (StringUtil.isEmpty(loginVO.getCode(), true) || StringUtil.isEmpty(loginVO.getPCode(), true)) {
//            // 验证信息不能为空
//            return ResultGenerator.getFailResult("验证信息不能为空");
//        }
//        if (redisUtil.get(loginVO.getPCode()) == null) {
//            // 验证码过期
//            return ResultGenerator.getFailResult("验证码过期，请重新获取");
//        }
//        if (!loginVO.getCode().toUpperCase().equals(redisUtil.get(loginVO.getPCode()).toString().toUpperCase())) {
//            // 验证码强制过期
//            redisUtil.del(loginVO.getPCode());
//            // 验证码错误
//            return ResultGenerator.getFailResult("验证码错误，请重新输入");
//        }

        //验证码强制过期
//        redisUtil.del(loginVO.getPCode());
        return pjp.proceed();
    }
}
