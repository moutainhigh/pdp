package com.pd.pdp.server.service;

import com.pd.pdp.server.base.Result;
import com.pd.pdp.server.dto.LoginDTO;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.LoginLogEntity;
import com.pd.pdp.server.vo.LoginVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author pdp
 * @Description 登陆服务接口
 **/

public interface LoginService {
    
    /**
     * @Author pdp
     * @Description 用户登陆
     * @Param [loginVO]
     * @return com.pd.pdp.base.Result<com.pd.pdp.dto.LoginDTO>
     **/
    Result<LoginDTO> login(LoginVO loginVO, HttpServletRequest request);
    
    /**
     * @Author pdp
     * @Description 查询登陆日志
     * @Param []
     * @return com.pd.pdp.dto.PageDTO<com.pd.pdp.entity.LoginLogEntity>
     **/
    
    PageDTO<LoginLogEntity> selectLoginLogList();
}
