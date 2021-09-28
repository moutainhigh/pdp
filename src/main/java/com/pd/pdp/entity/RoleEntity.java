package com.pd.pdp.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Table: au_role
 */
@Data
@ApiModel("角色实体类对象")
public class RoleEntity implements Serializable {
    /**
     * 主键
     *
     * Table:     au_role
     * Column:    id
     * Nullable:  false
     */
    @ApiModelProperty(value = "角色ID")
    private Integer id;

    /**
     * 角色名
     *
     * Table:     au_role
     * Column:    name
     * Nullable:  true
     */
    @ApiModelProperty(value = "角色名称")
    private String name;

    /**
     * 所属组织
     *
     * Table:     au_role
     * Column:    org_id
     * Nullable:  true
     */
    @ApiModelProperty(value = "所属组织")
    private Integer orgId;

    /**
     * 字典类型
     *
     * Table:     au_role
     * Column:    dict_value
     * Nullable:  true
     */
    @ApiModelProperty(value = "字典类型")
    private Integer dictValue;

    /**
     * 状态：0-不可用，1-可用，2-删除
     *
     * Table:     au_role
     * Column:    status
     * Nullable:  true
     */
    @ApiModelProperty(value = "状态：0-不可用，1-可用，2-删除")
    private Integer status;

    /**
     * 描述
     *
     * Table:     au_role
     * Column:    description
     * Nullable:  true
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 创建人ID
     *
     * Table:     au_role
     * Column:    create_user_id
     * Nullable:  true
     */
    @ApiModelProperty(value = "创建人ID")
    private Integer createUserId;

    /**
     * 创建时间
     *
     * Table:     au_role
     * Column:    create_time
     * Nullable:  false
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新人ID
     *
     * Table:     au_role
     * Column:    update_user_id
     * Nullable:  true
     */
    @ApiModelProperty(value = "更新人ID")
    private Integer updateUserId;

    /**
     * 系统时间
     *
     * Table:     au_role
     * Column:    update_time
     * Nullable:  true
     */
    @ApiModelProperty(value = "系统时间")
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table au_role
     *
     * @mbggenerated Thu Oct 15 13:19:35 CST 2020
     */
    private static final long serialVersionUID = 1L;
}