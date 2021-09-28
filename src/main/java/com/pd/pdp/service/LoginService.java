package com.pd.pdp.service;

import com.pd.pdp.base.Result;
import com.pd.pdp.dto.LoginDTO;
import com.pd.pdp.dto.PageDTO;
import com.pd.pdp.entity.LoginLogEntity;
import com.pd.pdp.vo.LoginVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author pdp
 * @Description 登陆服务接口
 * @Date 2020-10-15 8:49 上午
 **/

public interface LoginService {
    
    /**
     * @Author pdp
     * @Description 用户登陆
     * @Date 10:02 上午 2020/10/16
     * @Param [loginVO]
     * @return com.pd.pdp.base.Result<com.pd.pdp.dto.LoginDTO>
     **/
    Result<LoginDTO> login(LoginVO loginVO, HttpServletRequest request);
    
    /**
     * @Author pdp
     * @Description 查询登陆日志
     * @Date 2:04 下午 2020/10/16
     * @Param []
     * @return com.pd.pdp.dto.PageDTO<com.pd.pdp.entity.LoginLogEntity>
     **/
    
    PageDTO<LoginLogEntity> selectLoginLogList();
}
