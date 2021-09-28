package com.pd.pdp.mapper;

import com.pd.pdp.entity.OrgEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrgEntityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table au_org
     *
     * @mbggenerated Thu Oct 15 13:19:35 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table au_org
     *
     * @mbggenerated Thu Oct 15 13:19:35 CST 2020
     */
    int insert(OrgEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table au_org
     *
     * @mbggenerated Thu Oct 15 13:19:35 CST 2020
     */
    int insertSelective(OrgEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table au_org
     *
     * @mbggenerated Thu Oct 15 13:19:35 CST 2020
     */
    OrgEntity selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table au_org
     *
     * @mbggenerated Thu Oct 15 13:19:35 CST 2020
     */
    int updateByPrimaryKeySelective(OrgEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table au_org
     *
     * @mbggenerated Thu Oct 15 13:19:35 CST 2020
     */
    int updateByPrimaryKey(OrgEntity record);
}