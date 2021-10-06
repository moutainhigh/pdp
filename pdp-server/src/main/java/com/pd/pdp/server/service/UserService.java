package com.pd.pdp.server.service;

import com.pd.pdp.server.base.Result;
import com.pd.pdp.server.base.Service;
import com.pd.pdp.server.vo.PageVO;
import com.pd.pdp.server.entity.UserEntity;
import com.pd.pdp.server.vo.UserPageVO;

/**
 * @Author pdp
 * @Description 用户服务接口
 **/

public interface UserService extends Service<UserEntity, PageVO<UserPageVO>> {
    
    /**
     * @Author pdp
     * @Description 修改密码
     * @Param [userId, oldPassword, newPassword, repPassword]
     * @return com.pd.pdp.base.Request<java.lang.Object>
     **/
    Result<Object> updatePassword(Integer userId, String repPassword);

    /**
     * @Author pdp
     * @Description 根据用户ID查询密码
     * @Param [userId]
     * @return java.lang.String
     **/
    String selectPasswordByUserId(Integer userId);

    /**
     * @Author pdp
     * @Description 校验账号是否已经存在
     * @Param [account]
     * @return boolean
     **/
    boolean checkAccount(String account, Integer userId);

    /**
     * @Author pdp
     * @Description 校验手机号是否重复
     * @Param [phone]
     * @return boolean
     **/
    boolean checkPhone(String phone, Integer userId);
}
