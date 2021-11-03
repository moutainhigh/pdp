package com.pd.pdp.server.mapper;

import com.pd.notscan.BaseMapper;
import com.pd.pdp.server.entity.GatherQualityInfo;
import com.pd.pdp.server.entity.QualityRulesInfo;
import com.pd.pdp.server.vo.GatherQualityPageVo;
import com.pd.pdp.server.vo.PageVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 *
 * @author ckw
 * @date 2021/10/27
 */

@Mapper
@Repository
public interface GatherQualityMapper extends BaseMapper<GatherQualityInfo> {

    @Delete("delete from gather_quality_info where id = #{ids_str}")
    public int deleteBatchById(@Param("ids_str") String ids_str);


    @Select(value = "select id, rule_name from quality_rules_info")
    public List<QualityRulesInfo> selectRules();


    @Select({"<script>",
            "SELECT count(1) FROM gather_quality_info",
            "WHERE create_user_id=#{createUserId}",
            "<when test='pageVO.data.systemName !=null and pageVO.data.systemName !=\"\"'>",
            "AND system_name like '%${pageVO.data.systemName}%'",
            "</when>",
            "<when test='pageVO.data.sourceDb!=null and pageVO.data.sourceDb !=\"\"'>",
            "AND source_db like '%${pageVO.data.sourceDb}%' ",
            "</when>",
            "<when test='pageVO.data.sourceTable !=null and pageVO.data.sourceTable !=\"\"'>",
            "AND source_table like '%${pageVO.data.sourceTable}%' ",
            "</when>",
            "</script>"})
    public Integer selectGatherQualityCount(@Param("pageVO") PageVO<GatherQualityPageVo> pageVO, @Param("createUserId") int createUserId);


    @Select({"<script>",
            "SELECT *",
            "FROM gather_quality_info ",
            "WHERE create_user_id=#{createUserId}",
            "<when test='pageVO.data.systemName !=null and pageVO.data.systemName !=\"\"'>",
            "AND system_name like '%${pageVO.data.systemName}%'",
            "</when>",
            "<when test='pageVO.data.sourceDb!=null and pageVO.data.sourceDb !=\"\"'>",
            "AND source_db like '%${pageVO.data.sourceDb}%'",
            "</when>",
            "<when test='pageVO.data.sourceTable!=null and pageVO.data.sourceTable !=\"\"'>",
            "AND source_table like '%${pageVO.data.sourceTable}%'",
            "</when>",
            "LIMIT #{pageVO.dbIndex,jdbcType=INTEGER},#{pageVO.dbNumber,jdbcType=INTEGER}",
            "</script>"})
    public List<GatherQualityInfo> selectGatherQualityInfoList(@Param("pageVO") PageVO<GatherQualityPageVo> pageVO, @Param("createUserId") int createUserId);

}
