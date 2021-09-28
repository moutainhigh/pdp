package com.pd.pdp.service.impl;

import com.pd.pdp.dto.PageDTO;
import com.pd.pdp.entity.RoleAuthEntity;
import com.pd.pdp.entity.RoleEntity;
import com.pd.pdp.mapper.RoleAuthEntityMapper;
import com.pd.pdp.mapper.RoleEntityMapper;
import com.pd.pdp.mapper.UserRoleEntityMapper;
import com.pd.pdp.service.RoleService;
import com.pd.pdp.utils.PageUtil;
import com.pd.pdp.vo.PageVO;
import com.pd.pdp.vo.RolePageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author pdp
 * @Description 角色服务接口实现类
 * @Date 2020-10-17 10:26 上午
 **/

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleEntityMapper roleEntityMapper;

    @Autowired
    RoleAuthEntityMapper roleAuthEntityMapper;

    @Autowired
    UserRoleEntityMapper userRoleEntityMapper;

    @Override
    public void save(RoleEntity model) {
        roleEntityMapper.insert(model);
    }

    @Override
    public void save(List<RoleEntity> models) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void deleteByIds(List<String> ids) {

    }

    @Override
    public void update(RoleEntity model) {
        roleEntityMapper.updateByPrimaryKey(model);
    }

    @Override
    public RoleEntity findById(Integer id) {
        return null;
    }

    @Override
    public List<RoleEntity> findByIds(String ids) {
        return null;
    }

    @Override
    public PageDTO<RoleEntity> findByCondition(PageVO<RolePageVO> pageVO) {
        return null;
    }

    @Override
    public List<RoleEntity> findAll() {
        return null;
    }

    @Override
    public void insertRoleAndAuth(Integer roleId, List<Integer> authIds) {
        // 先删除角色绑定的权限
        roleEntityMapper.deleteRoleAuthByRoleId(roleId);
        // 角色绑定权限
        for (Integer authId : authIds) {
            RoleAuthEntity roleAuthEntity = new RoleAuthEntity();
            roleAuthEntity.setAuthId(authId);
            roleAuthEntity.setRoleId(roleId);
            roleAuthEntityMapper.insert(roleAuthEntity);
        }
    }

    @Override
    public int updateRoleStatus(List<String> ids, Integer status) {
        Integer count = userRoleEntityMapper.checkRoleByRoleIds(ids);
        if (count>0){
            return -1;
        }
        return roleEntityMapper.updateRoleStatus(ids, status);
    }

    @Override
    public List<Integer> selectRoleIdsByUserId(Integer userId) {
        return roleEntityMapper.selectRoleIdsByUserId(userId);
    }

    @Override
    public List<RoleEntity> selectAllRole() {
        return roleEntityMapper.selectAllRole();
    }

    @Override
    public PageDTO<RoleEntity> selectRoleList(PageVO<RolePageVO> pageVO) {
        PageDTO<RoleEntity> pageDTO = new PageDTO<>();
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNumber(pageVO.getPageNumber());
        pageUtil.setTotalNumber(roleEntityMapper.selectRoleListCount(pageVO));
        pageUtil.setCurrentPage(pageVO.getCurrentPage());
        pageVO.setDbIndex(pageUtil.getDbIndex());
        pageVO.setDbNumber(pageUtil.getDbNumber());
        List<RoleEntity> roleEntities = roleEntityMapper.selectRoleList(pageVO);
        pageDTO.setPageUtil(pageUtil);
        pageDTO.setList(roleEntities);
        return pageDTO;
    }

    @Override
    public List<Integer> selectAllAuthIdByRoleId(Integer roleId) {
        return roleAuthEntityMapper.selectAllAuthIdByRoleId(roleId);
    }
}
