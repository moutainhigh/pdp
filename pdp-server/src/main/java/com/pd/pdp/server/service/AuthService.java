package com.pd.pdp.server.service;

import com.pd.pdp.server.base.Service;
import com.pd.pdp.server.vo.PageVO;
import com.pd.pdp.server.dto.AuthEntityDTO;
import com.pd.pdp.server.entity.AuthEntity;
import com.pd.pdp.server.vo.AuthPageVO;

import java.util.List;

/**
 * @Author pdp
 * @Description 权限服务接口
 **/

public interface AuthService extends Service<AuthEntity, PageVO<AuthPageVO>> {

    /**
     * @Author pdp
     * @Description 更改权限状态
     * @Param [ids, status]
     * @return int
     **/
    int updateAuthStatus(List<String> ids, Integer status);

    /**
     * @Author pdp
     * @Description 查询权限树
     * @Param []
     * @return java.util.List<com.pd.pdp.dto.AuthEntityDTO>
     **/
    List<AuthEntityDTO> selectAuthTree();
}
