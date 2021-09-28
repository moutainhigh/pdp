package com.pd.pdp.service;

import com.pd.pdp.base.Service;
import com.pd.pdp.dto.AuthEntityDTO;
import com.pd.pdp.entity.AuthEntity;
import com.pd.pdp.vo.AuthPageVO;
import com.pd.pdp.vo.PageVO;

import java.util.List;

/**
 * @Author pdp
 * @Description 权限服务接口
 * @Date 2020-10-17 10:27 下午
 **/

public interface AuthService extends Service<AuthEntity, PageVO<AuthPageVO>> {

    /**
     * @Author pdp
     * @Description 更改权限状态
     * @Date 10:29 下午 2020/10/17
     * @Param [ids, status]
     * @return int
     **/
    int updateAuthStatus(List<String> ids, Integer status);

    /**
     * @Author pdp
     * @Description 查询权限树
     * @Date 10:29 下午 2020/10/17
     * @Param []
     * @return java.util.List<com.pd.pdp.dto.AuthEntityDTO>
     **/
    List<AuthEntityDTO> selectAuthTree();
}
