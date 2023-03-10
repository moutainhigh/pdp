package com.pd.pdp.server.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Table: au_dict
 */
@Data
public class DictEntity implements Serializable {
    /**
     * 主键
     *
     * Table:     au_dict
     * Column:    id
     * Nullable:  false
     */
    private Integer id;

    /**
     * 字典键
     *
     * Table:     au_dict
     * Column:    dict_key
     * Nullable:  false
     */
    private String dictKey;

    /**
     * 字典值
     *
     * Table:     au_dict
     * Column:    dict_value
     * Nullable:  false
     */
    private String dictValue;

    /**
     * 字典排列序号
     *
     * Table:     au_dict
     * Column:    sort_no
     * Nullable:  false
     */
    private Integer sortNo;

    /**
     * 字典类型id
     *
     * Table:     au_dict
     * Column:    dict_type_id
     * Nullable:  true
     */
    private Integer dictTypeId;

    /**
     * 描述信息
     *
     * Table:     au_dict
     * Column:    description
     * Nullable:  true
     */
    private String description;

    /**
     * 字典状态：0-可用，1-不可用，2-删除
     *
     * Table:     au_dict
     * Column:    status
     * Nullable:  false
     */
    private Integer status;

    /**
     * 创建信息的用户id
     *
     * Table:     au_dict
     * Column:    create_user_id
     * Nullable:  true
     */
    private Integer createUserId;

    /**
     * 创建的时间
     *
     * Table:     au_dict
     * Column:    create_time
     * Nullable:  true
     */
    private Date createTime;

    /**
     * 更新信息的用户id
     *
     * Table:     au_dict
     * Column:    update_user_id
     * Nullable:  true
     */
    private Integer updateUserId;

    /**
     * 更新的时间
     *
     * Table:     au_dict
     * Column:    update_time
     * Nullable:  true
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table au_dict
     *
     * @mbggenerated Thu Oct 15 13:19:35 CST 2020
     */
    private static final long serialVersionUID = 1L;
}