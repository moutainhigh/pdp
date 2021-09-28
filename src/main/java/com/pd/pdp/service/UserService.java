package com.pd.pdp.service;

import com.pd.pdp.base.Result;
import com.pd.pdp.base.Service;
import com.pd.pdp.entity.UserEntity;
import com.pd.pdp.vo.PageVO;
import com.pd.pdp.vo.UserPageVO;

/**
 * @Author pdp
 * @Description 用户服务接口
 * @Date 2020-10-16 4:25 下午
 **/

public interface UserService extends Service<UserEntity, PageVO<UserPageVO>> {
    
    /**
     * @Author pdp
     * @Description 修改密码
     * @Date 4:29 下午 2020/10/16
     * @Param [userId, oldPassword, newPassword, repPassword]
     * @return com.pd.pdp.base.Request<java.lang.Object>
     **/
    Result<Object> updatePassword(Integer userId, String repPassword);

    /**
     * @Author pdp
     * @Description 根据用户ID查询密码
     * @Date 8:45 下午 2020/10/17
     * @Param [userId]
     * @return java.lang.String
     **/
    String selectPasswordByUserId(Integer userId);

    /**
     * @Author pdp
     * @Description 校验账号是否已经存在
     * @Date 4:28 下午 2020/10/21
     * @Param [account]
     * @return boolean
     **/
    boolean checkAccount(String account, Integer userId);

    /**
     * @Author pdp
     * @Description 校验手机号是否重复
     * @Date 4:28 下午 2020/10/21
     * @Param [phone]
     * @return boolean
     **/
    boolean checkPhone(String phone, Integer userId);
}
