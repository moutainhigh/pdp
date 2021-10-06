package com.pd.pdp.server.service.impl;

import com.pd.pdp.server.mapper.LoginLogEntityMapper;
import com.pd.pdp.server.mapper.RoleEntityMapper;
import com.pd.pdp.server.mapper.UserEntityMapper;
import com.pd.pdp.server.service.LoginService;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.LoginLogEntity;
import com.pd.pdp.server.enums.Constant;
import com.pd.pdp.server.base.Result;
import com.pd.pdp.server.base.ResultGenerator;
import com.pd.pdp.server.dto.AuthEntityDTO;
import com.pd.pdp.server.dto.ButtonEntityDTO;
import com.pd.pdp.server.dto.ButtonMapDTO;
import com.pd.pdp.server.dto.LoginDTO;
import com.pd.pdp.server.entity.AuthEntity;
import com.pd.pdp.server.entity.RoleEntity;
import com.pd.pdp.server.entity.UserEntity;
import com.pd.pdp.server.mapper.AuthEntityMapper;
import com.pd.pdp.server.utils.MD5Util;
import com.pd.pdp.server.utils.RedisUtil;
import com.pd.pdp.server.utils.GetIPAddrUtil;
import com.pd.pdp.server.utils.IpToAddressUtil;
import com.pd.pdp.server.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @Author pdp
 * @Description 登陆服务接口实现类
 * @Date 2020-10-15 8:49 上午
 **/

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private RoleEntityMapper roleEntityMapper;

    @Autowired
    private AuthEntityMapper authEntityMapper;

    @Autowired
    private LoginLogEntityMapper loginLogEntityMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Result<LoginDTO> login(LoginVO loginVO, HttpServletRequest request) {
        String errorpw = "ERRORPW" + loginVO.getAccount();
        Integer count = (Integer) redisUtil.get(errorpw);
        if (count == null) {
            count = 0;
        }

        // 查询用户
        UserEntity userEntity = userEntityMapper.login(loginVO.getAccount());
        List<RoleEntity> roleEntityList;
        if (userEntity != null) {
            String password = MD5Util.getMD5Code(Constant.PARENT_ID.getValue() + loginVO.getPassword());
            if (!userEntity.getPassword().equals(password)) {
                //计算验证码错误次数
                count++;
                if (count == 1) {
                    redisUtil.set(errorpw, count, 30 * 60);
                    return ResultGenerator.getFailResult("密码错误，在错误两次账号将被锁定");
                } else if (count == 2) {
                    redisUtil.set(errorpw, count, 30 * 60);
                    return ResultGenerator.getFailResult("密码错误，在错误一次账号将被锁定");
                } else if (count >= 3) {
                    redisUtil.set(errorpw, count, 3 * 60 * 60);
                    return ResultGenerator.getFailResult("密码在30分钟内错误超过3次，请30分钟后重试");
                }
                return ResultGenerator.getErrorResult("系统错误，请联系系统管理员");
            }

            if (!userEntity.getStatus().equals(1)) {
                // 用户状态不可用
                return ResultGenerator.getFailResult("用户已被锁定，请联系系统管理员");
            }
            // 查询角色
            roleEntityList = roleEntityMapper.selectRoleByUserId(userEntity.getId());
            List<Integer> ids = new ArrayList<>();
            for (RoleEntity role : roleEntityList) {
                ids.add(role.getId());
            }
            //查询权限
            List<AuthEntity> authEntityList = authEntityMapper.selectAuthByRoleIds(ids);
            List<AuthEntityDTO> authEntityDTOS = getAuth(0, authEntityList);

            // 查询按钮
            List<ButtonMapDTO> buttonMapDTOS = new ArrayList<>();
            for (AuthEntity authEntity : authEntityList) {
                List<ButtonEntityDTO> buttonEntityDTOS = new ArrayList<>();
                List<AuthEntity> buttonEntitis = authEntityMapper.selectAuthByParentId(ids, authEntity.getId());
                for (AuthEntity auth : buttonEntitis) {
                    ButtonEntityDTO buttonEntityDTO = new ButtonEntityDTO();
                    buttonEntityDTO.setId(auth.getId());
                    buttonEntityDTO.setIcon(auth.getClazz());
                    buttonEntityDTO.setText(auth.getName());
                    buttonEntityDTO.setType(auth.getUrl());
                    buttonEntityDTOS.add(buttonEntityDTO);
                }
                ButtonMapDTO buttonMapDTO = new ButtonMapDTO();
                buttonMapDTO.setUrl(authEntity.getUrl());
                buttonMapDTO.setButtonEntityDTO(buttonEntityDTOS);
                buttonMapDTOS.add(buttonMapDTO);
            }

            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUserEntity(userEntity);
            loginDTO.setRoleEntities(roleEntityList);
            loginDTO.setAuthEntityDTOS(authEntityDTOS);
            loginDTO.setButtonMapDTOS(buttonMapDTOS);
            loginDTO.setTime(userEntity.getCreateTime());
            loginDTO.setAddress(IpToAddressUtil.getCityInfo(GetIPAddrUtil.getIpAddr(request)));

            // 生成token
            String token = MD5Util.getMD5Code(String.valueOf(System.currentTimeMillis() + new Random().nextInt(999999999)));
            redisUtil.set(userEntity.getId().toString(), token, 24 * 60 * 60);
            loginDTO.setToken(token);
            // 新增登录日志
            insertLoginLog(userEntity, request);
            return ResultGenerator.getSuccessResult(loginDTO);
        } else {
            // 账号错误
            return ResultGenerator.getFailResult("账号不存在，请检查后重试");
        }
    }

    @Override
    public PageDTO<LoginLogEntity> selectLoginLogList() {
        PageDTO<LoginLogEntity> loginLogEntityPageDTO = new PageDTO<>();
        loginLogEntityPageDTO.setList(loginLogEntityMapper.selectLoginLogList());
        for (LoginLogEntity loginLogEntity : loginLogEntityPageDTO.getList()) {
            if (redisUtil.hasKey("onLine"+loginLogEntity.getUserId()) && redisUtil.get("onLine"+loginLogEntity.getUserId()).equals(loginLogEntity.getId())){
                loginLogEntity.setIsLine("在线");
            } else {
                loginLogEntity.setIsLine("离线");
            }
        }
        return loginLogEntityPageDTO;
    }

    /**
     * @return void
     * @Author pdp
     * @Description 记录登陆日志
     * @Date 1:50 下午 2020/10/16
     * @Param [userEntity, request]
     **/
    private void insertLoginLog(UserEntity userEntity, HttpServletRequest request) {
        LoginLogEntity loginLogEntity = new LoginLogEntity();
        loginLogEntity.setUserId(userEntity.getId());
        loginLogEntity.setUserName(userEntity.getUserName());
        loginLogEntity.setSysId(Integer.valueOf(Constant.SYS_ID.getValue()));
        loginLogEntity.setIp(GetIPAddrUtil.getIpAddr(request));
        loginLogEntity.setPlatform(1);
        loginLogEntity.setTime(new Date());
        loginLogEntityMapper.insert(loginLogEntity);
        redisUtil.set("onLine"+loginLogEntity.getUserId(), loginLogEntity.getId());
    }

    /**
     * @return java.util.List<com.pd.pdp.dto.AuthEntityDTO>
     * @Author pdp
     * @Description 递归获取权限
     * @Date 4:50 下午 2020/10/15
     * @Param [authId, list]
     **/
    private static List<AuthEntityDTO> getAuth(Integer authId, List<AuthEntity> list) {
        List<AuthEntityDTO> authEntities = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (authId.equals(list.get(i).getParentId())) {
                AuthEntityDTO authEntityDTO = new AuthEntityDTO();
                authEntityDTO.setId(list.get(i).getId());
                authEntityDTO.setIcon(list.get(i).getClazz());
                authEntityDTO.setName(list.get(i).getTarget());
                authEntityDTO.setTitle(list.get(i).getName());
                authEntityDTO.setChildren(getAuth(list.get(i).getId(), list));
                authEntities.add(authEntityDTO);
            }
        }
        return authEntities;
    }


}
