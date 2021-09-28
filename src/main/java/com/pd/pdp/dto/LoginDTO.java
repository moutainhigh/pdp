package com.pd.pdp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pd.pdp.entity.RoleEntity;
import com.pd.pdp.entity.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author pdp
 * @Description
 * @Date 2020-10-15 8:57 上午
 **/

@Data
@ApiModel("登陆返回对象")
public class LoginDTO {

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    @ApiModelProperty("登陆地址")
    private String address;

    @ApiModelProperty("用户对象")
    private UserEntity userEntity;

    @ApiModelProperty("角色集合对象")
    private List<RoleEntity> roleEntities;

    @ApiModelProperty("权限集合对象")
    private List<AuthEntityDTO> authEntityDTOS;

    @ApiModelProperty("按钮集合对象")
    private List<ButtonMapDTO> buttonMapDTOS;
}
