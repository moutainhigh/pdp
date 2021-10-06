package com.pd.pdp.server.aop;

import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.server.base.Request;
import com.pd.pdp.server.base.ResultGenerator;
import com.pd.pdp.server.service.UserService;
import com.pd.pdp.server.enums.Constant;
import com.pd.pdp.server.utils.MD5Util;
import com.pd.pdp.server.utils.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author pdp
 * @Description 修改密码校验参数
 **/

@Aspect
@Component
public class UpdatePasswordCheck {

    @Autowired
    UserService userService;

    @Pointcut("execution(public * com.pd.pdp.server.controller.UserController.updatePassword(..))")
    public void updatePassword(){}
    @Around("updatePassword()")
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Request<JSONObject> request = (Request<JSONObject>) args[0];
        String password = userService.selectPasswordByUserId(request.getData().getInteger("userId"));

        String oldPassword = request.getData().getString("oldPassword");
        String newPassword = request.getData().getString("newPassword");
        String repPassword = request.getData().getString("repPassword");
        // 验证老密码是否正确
        if (StringUtil.isEmpty(oldPassword, true) || password == null || !password.equals(MD5Util.getMD5Code(Constant.PARENT_ID.getValue() + oldPassword))) {
            return ResultGenerator.getFailResult("旧密码不正确，请重新输入");
        }
        // 验证新密码是否正确
        if (StringUtil.isEmpty(newPassword, true) || StringUtil.isEmpty(repPassword, true)) {
            return ResultGenerator.getFailResult("新密码不能为空，请重新输入");
        }
        // 验证新密码和重复密码是否一致
        if (!newPassword.equals(repPassword)) {
            return ResultGenerator.getFailResult("两次密码输入不一致，请重新输入");
        }

        // 业务系统修改密码
        return pjp.proceed();
    }
}
