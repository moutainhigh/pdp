package com.pd.pdp.service.impl;

import com.pd.pdp.base.Result;
import com.pd.pdp.base.ResultGenerator;
import com.pd.pdp.dto.PageDTO;
import com.pd.pdp.entity.UserEntity;
import com.pd.pdp.entity.UserRoleEntity;
import com.pd.pdp.enums.Constant;
import com.pd.pdp.mapper.UserEntityMapper;
import com.pd.pdp.mapper.UserRoleEntityMapper;
import com.pd.pdp.service.UserService;
import com.pd.pdp.utils.MD5Util;
import com.pd.pdp.utils.PageUtil;
import com.pd.pdp.vo.PageVO;
import com.pd.pdp.vo.UserPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author pdp
 * @Description 用户服务接口实现类
 * @Date 2020-10-16 4:27 下午
 **/

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserEntityMapper userEntityMapper;

    @Autowired
    UserRoleEntityMapper userRoleEntityMapper;

    @Override
    public Result<Object> updatePassword(Integer userId, String repPassword) {
        // 业务系统修改密码
        int count = userEntityMapper.updatePasswordByUserId(userId, MD5Util.getMD5Code(Constant.PARENT_ID.getValue() + repPassword));
        if (count == 0) {
            return ResultGenerator.getFailResult("修改密码失败，请稍后重试");
        }
        return ResultGenerator.getSuccessResult();
    }

    @Override
    public String selectPasswordByUserId(Integer userId) {
        return userEntityMapper.selectPasswordByUserId(userId);
    }

    @Override
    public boolean checkAccount(String account, Integer userId) {
        int count = userEntityMapper.checkAccountRepeat(account, userId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkPhone(String phone, Integer userId) {
        int count = userEntityMapper.checkPhoneRepeat(phone, userId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void save(UserEntity userEntity) {
        // 新增用户表
        userEntity.setCreateTime(new Date());
        userEntity.setPassword(MD5Util.getMD5Code(Constant.PARENT_ID.getValue() + MD5Util.getMD5Code(Constant.PASSWORD_ID.getValue())));
        int count = userEntityMapper.insertSelective(userEntity);
        if (count > 0) {
            // 新增权限表
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setUserId(userEntity.getId());
            userRoleEntity.setRoleId(userEntity.getRoleId());
            userRoleEntity.setCreateUserId(userEntity.getCreateUserId());
            userRoleEntity.setCreateTime(new Date());
            userRoleEntityMapper.insertSelective(userRoleEntity);
        }
    }

    @Override
    public void save(List<UserEntity> models) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void deleteByIds(List<String> list) {
        userEntityMapper.deleteUser(list);
        userRoleEntityMapper.deleteAllByUserId(list);
    }

    @Override
    public void update(UserEntity userEntity) {
        userEntity.setUpdateTime(new Date());
        int count = userEntityMapper.updateByPrimaryKeySelective(userEntity);
        if (count > 0) {
            // 删除原来角色
            userRoleEntityMapper.deleteByUserId(userEntity.getId());
            // 新增权限表
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setUserId(userEntity.getId());
            userRoleEntity.setRoleId(userEntity.getRoleId());
            userRoleEntity.setCreateUserId(userEntity.getCreateUserId());
            userRoleEntity.setCreateTime(new Date());
            userRoleEntityMapper.insertSelective(userRoleEntity);
        }
    }

    @Override
    public UserEntity findById(Integer id) {
        return null;
    }

    @Override
    public List<UserEntity> findByIds(String ids) {
        return null;
    }

    @Override
    public PageDTO<UserEntity> findByCondition(PageVO<UserPageVO> pageVO) {
        PageDTO<UserEntity> pageDTO = new PageDTO<>();
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNumber(pageVO.getPageNumber());
        pageUtil.setTotalNumber(userEntityMapper.selectUserListCount(pageVO));
        pageUtil.setCurrentPage(pageVO.getCurrentPage());
        pageVO.setDbIndex(pageUtil.getDbIndex());
        pageVO.setDbNumber(pageUtil.getDbNumber());
        List<UserEntity> userEntities = userEntityMapper.selectUserList(pageVO);
        pageDTO.setPageUtil(pageUtil);
        pageDTO.setList(userEntities);
        return pageDTO;
    }

    @Override
    public List<UserEntity> findAll() {
        return null;
    }
}
