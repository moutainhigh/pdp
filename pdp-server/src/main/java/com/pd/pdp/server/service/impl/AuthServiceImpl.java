package com.pd.pdp.server.service.impl;

import com.pd.pdp.server.mapper.RoleAuthEntityMapper;
import com.pd.pdp.server.service.AuthService;
import com.pd.pdp.server.dto.AuthEntityDTO;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.AuthEntity;
import com.pd.pdp.server.enums.Constant;
import com.pd.pdp.server.mapper.AuthEntityMapper;
import com.pd.pdp.server.utils.PageUtil;
import com.pd.pdp.server.vo.AuthPageVO;
import com.pd.pdp.server.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author pdp
 * @Description 权限服务接口实现类
 * @Date 2020-10-17 10:30 下午
 **/

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthEntityMapper authEntityMapper;

    @Autowired
    RoleAuthEntityMapper roleAuthEntityMapper;

    @Override
    public int updateAuthStatus(List<String> ids, Integer status) {
        int count = roleAuthEntityMapper.checkAuthByAuthIds(ids);
        if (count>0){
            return -1;
        }
        return authEntityMapper.updateAuthStatus(ids, status);
    }

    @Override
    public List<AuthEntityDTO> selectAuthTree() {
        List<AuthEntity> authEntities = authEntityMapper.selectAuthTree();
        return getAuth(0, authEntities);
    }

    @Override
    public void save(AuthEntity model) {
        model.setSysId(Integer.parseInt(Constant.SYS_ID.getValue()));
        model.setCreateTime(new Date());
        authEntityMapper.insertSelective(model);
    }

    @Override
    public void save(List<AuthEntity> models) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void deleteByIds(List<String> ids) {

    }

    @Override
    public void update(AuthEntity model) {
        model.setUpdateTime(new Date());
        authEntityMapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public AuthEntity findById(Integer id) {
        return null;
    }

    @Override
    public List<AuthEntity> findByIds(String ids) {
        return null;
    }

    @Override
    public PageDTO<AuthEntity> findByCondition(PageVO<AuthPageVO> pageVO) {
        PageDTO<AuthEntity> pageDTO = new PageDTO<>();
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNumber(pageVO.getPageNumber());
        pageUtil.setTotalNumber(authEntityMapper.selectAuthListCount(pageVO));
        pageUtil.setCurrentPage(pageVO.getCurrentPage());
        pageVO.setDbIndex(pageUtil.getDbIndex());
        pageVO.setDbNumber(pageUtil.getDbNumber());
        List<AuthEntity> authEntities = authEntityMapper.selectAuthList(pageVO);
        pageDTO.setList(authEntities);
        pageDTO.setPageUtil(pageUtil);
        return pageDTO;
    }

    @Override
    public List<AuthEntity> findAll() {
        return authEntityMapper.selectAuthTree();
    }

    /**
     * @Author pdp
     * @Description 递归获取权限
     * @Date 1:40 下午 2020/10/18
     * @Param [authId, list]
     * @return java.util.List<com.pd.pdp.dto.AuthEntityDTO>
     **/
    public static List<AuthEntityDTO> getAuth(Integer authId, List<AuthEntity> list) {
        List<AuthEntityDTO> authEntities = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (authId.equals(list.get(i).getParentId())) {
                AuthEntityDTO authEntityDTO = new AuthEntityDTO();
                authEntityDTO.setId(list.get(i).getId());
                authEntityDTO.setIcon(list.get(i).getClazz());
                authEntityDTO.setTitle(list.get(i).getName());
                authEntityDTO.setName(list.get(i).getTarget());
                authEntityDTO.setChildren(getAuth(list.get(i).getId(), list));
                authEntities.add(authEntityDTO);
            }
        }
        return authEntities;
    }
}
