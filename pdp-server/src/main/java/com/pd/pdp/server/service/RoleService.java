package com.pd.pdp.server.service;

import com.pd.pdp.server.base.Service;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.vo.PageVO;
import com.pd.pdp.server.entity.RoleEntity;
import com.pd.pdp.server.vo.RolePageVO;

import java.util.List;

/**
 * @Author pdp
 * @Description 角色服务接口
 **/

public interface RoleService extends Service<RoleEntity, PageVO<RolePageVO>> {

    /**
     * @Author pdp
     * @Description 角色绑定权限
     * @Param [roleId, authIds]
     * @return void
     **/
    void insertRoleAndAuth(Integer roleId, List<Integer> authIds);

    /**
     * @Author pdp
     * @Description 修改权限状态
     * @Param [ids, status]
     * @return int
     **/
    int updateRoleStatus(List<String> ids, Integer status);

    /**
     * @Author pdp
     * @Description 根据用户查询角色ID
     * @Param [userId]
     * @return java.util.List<java.lang.Integer>
     **/
    List<Integer> selectRoleIdsByUserId(Integer userId);

    /**
     * @Author pdp
     * @Description 查询角色
     * @Param []
     * @return java.util.List<com.pd.pdp.entity.RoleEntity>
     **/
    List<RoleEntity> selectAllRole();

    /**
     * @Author pdp
     * @Description 查询所有角色
     * @Param [pageVO]
     * @return com.pd.pdp.dto.PageDTO<com.pd.pdp.entity.RoleEntity>
     **/
    PageDTO<RoleEntity> selectRoleList(PageVO<RolePageVO> pageVO);

    /**
     * @Author pdp
     * @Description 根据角色ID查询所有权限
     * @Param [roleId]
     * @return java.util.List<java.lang.Integer>
     **/
    List<Integer> selectAllAuthIdByRoleId(Integer roleId);
}
