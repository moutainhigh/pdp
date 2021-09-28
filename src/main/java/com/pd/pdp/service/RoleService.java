package com.pd.pdp.service;

import com.pd.pdp.base.Service;
import com.pd.pdp.dto.PageDTO;
import com.pd.pdp.entity.RoleEntity;
import com.pd.pdp.vo.PageVO;
import com.pd.pdp.vo.RolePageVO;

import java.util.List;

/**
 * @Author pdp
 * @Description 角色服务接口
 * @Date 2020-10-17 10:24 上午
 **/

public interface RoleService extends Service<RoleEntity, PageVO<RolePageVO>> {

    /**
     * @Author pdp
     * @Description 角色绑定权限
     * @Date 10:20 下午 2020/10/17
     * @Param [roleId, authIds]
     * @return void
     **/
    void insertRoleAndAuth(Integer roleId, List<Integer> authIds);

    /**
     * @Author pdp
     * @Description 修改权限状态
     * @Date 10:20 下午 2020/10/17
     * @Param [ids, status]
     * @return int
     **/
    int updateRoleStatus(List<String> ids, Integer status);

    /**
     * @Author pdp
     * @Description 根据用户查询角色ID
     * @Date 10:20 下午 2020/10/17
     * @Param [userId]
     * @return java.util.List<java.lang.Integer>
     **/
    List<Integer> selectRoleIdsByUserId(Integer userId);

    /**
     * @Author pdp
     * @Description 查询角色
     * @Date 10:21 下午 2020/10/17
     * @Param []
     * @return java.util.List<com.pd.pdp.entity.RoleEntity>
     **/
    List<RoleEntity> selectAllRole();

    /**
     * @Author pdp
     * @Description 查询所有角色
     * @Date 10:21 下午 2020/10/17
     * @Param [pageVO]
     * @return com.pd.pdp.dto.PageDTO<com.pd.pdp.entity.RoleEntity>
     **/
    PageDTO<RoleEntity> selectRoleList(PageVO<RolePageVO> pageVO);

    /**
     * @Author pdp
     * @Description 根据角色ID查询所有权限
     * @Date 10:25 下午 2020/10/17
     * @Param [roleId]
     * @return java.util.List<java.lang.Integer>
     **/
    List<Integer> selectAllAuthIdByRoleId(Integer roleId);
}
