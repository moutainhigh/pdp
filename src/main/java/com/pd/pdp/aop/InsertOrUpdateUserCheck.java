package com.pd.pdp.aop;

import com.pd.pdp.base.Request;
import com.pd.pdp.base.ResultGenerator;
import com.pd.pdp.entity.UserEntity;
import com.pd.pdp.service.UserService;
import com.pd.pdp.utils.RedisUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author pdp
 * @Description 新增和修改用户校验参数
 * @Date 2020-10-15 2:29 下午
 **/

@Aspect
@Component
public class InsertOrUpdateUserCheck {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserService userService;

    @Pointcut("execution(public * com.pd.pdp.controller.UserController.insertUser(..))" +
            "|| execution(public * com.pd.pdp.controller.UserController.updateUser(..))")
    public void insertOrUpdateUser() {
    }

    @Around("insertOrUpdateUser()")
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Request<UserEntity> request = (Request<UserEntity>) args[0];

        // 校验账号
        if (userService.checkAccount(request.getData().getAccount(), request.getData().getId())) {
            return ResultGenerator.getFailResult("账号已经存在，请修改后重试");
        }

        // 校验手机号
        if (userService.checkPhone(request.getData().getPhone(), request.getData().getId())) {
            return ResultGenerator.getFailResult("手机号已经存在，请修改后重试");
        }
        return pjp.proceed();
    }
}
