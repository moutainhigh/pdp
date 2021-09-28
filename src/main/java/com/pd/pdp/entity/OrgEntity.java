package com.pd.pdp.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Table: au_org
 */
@Data
public class OrgEntity implements Serializable {
    /**
     * Table:     au_org
     * Column:    id
     * Nullable:  false
     */
    private Integer id;

    /**
     * 组织编码
     *
     * Table:     au_org
     * Column:    code
     * Nullable:  false
     */
    private String code;

    /**
     * 组织名称
     *
     * Table:     au_org
     * Column:    name
     * Nullable:  false
     */
    private String name;

    /**
     * 组织类别
     *
     * Table:     au_org
     * Column:    org_type
     * Nullable:  true
     */
    private Integer orgType;

    /**
     * 上级组织id
     *
     * Table:     au_org
     * Column:    parent_id
     * Nullable:  false
     */
    private Integer parentId;

    /**
     * 状态：0-不可用，1-可用，2-删除
     *
     * Table:     au_org
     * Column:    status
     * Nullable:  true
     */
    private Integer status;

    /**
     * 是否是父节点 0 否 1 是
     *
     * Table:     au_org
     * Column:    is_parent
     * Nullable:  false
     */
    private Integer isParent;

    /**
     * 描述信息
     *
     * Table:     au_org
     * Column:    description
     * Nullable:  true
     */
    private String description;

    /**
     * 创建的时间
     *
     * Table:     au_org
     * Column:    create_time
     * Nullable:  true
     */
    private Date createTime;

    /**
     * 创建信息的用户id
     *
     * Table:     au_org
     * Column:    create_user_id
     * Nullable:  true
     */
    private Integer createUserId;

    /**
     * 更新信息的用户id
     *
     * Table:     au_org
     * Column:    update_user_id
     * Nullable:  true
     */
    private Integer updateUserId;

    /**
     * 更新时间
     *
     * Table:     au_org
     * Column:    update_time
     * Nullable:  true
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table au_org
     *
     * @mbggenerated Thu Oct 15 13:19:35 CST 2020
     */
    private static final long serialVersionUID = 1L;
}