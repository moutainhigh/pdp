package com.pd.pdp.server.mapper;

import com.pd.notscan.BaseMapper;
import com.pd.pdp.server.entity.DataSourcesInfo;
import com.pd.pdp.server.entity.DataSourcesTypeInfo;
import com.pd.pdp.server.entity.GatherDolphinInfo;
import com.pd.pdp.server.entity.GatherTypeInfo;
import com.pd.pdp.server.vo.DatasourcePageVO;
import com.pd.pdp.server.vo.GatherDolphinPageVo;
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
 * @author zz
 * @date 2021/10/9
 */

@Mapper
@Repository
public interface GatherDolphinMapper extends BaseMapper<GatherDolphinInfo> {

    @Delete("delete from gather_dolphin_info where id = #{ids_str}")
    public int deleteBatchById(@Param("ids_str") String ids_str);


    @Select(value = "select id, sync_type from gather_type_info")
    public List<GatherTypeInfo> selectSyncType();


    @Select({"<script>",
            "SELECT count(1) FROM gather_dolphin_info",
            "WHERE 1=1",
            "<when test='pageVO.data.gatherContext!=null and pageVO.data.gatherContext !=\"\"'>",
            "AND gather_context like '%${pageVO.data.gatherContext}%'",
            "</when>",
            "<when test='pageVO.data.databaseNameInput!=null and pageVO.data.databaseNameInput !=\"\"'>",
            "AND database_name_input = #{pageVO.data.databaseNameInput}",
            "</when>",
            "<when test='pageVO.data.syncType!=null and pageVO.data.syncType !=\"\"'>",
            "AND sync_type = #{pageVO.data.syncType}",
            "</when>",
            "</script>"})
    public Integer selectGatherDolphinCount(@Param("pageVO") PageVO<GatherDolphinPageVo> pageVO, @Param("createUserId") int createUserId);


    @Select({"<script>",
            "SELECT *",
            "FROM gather_dolphin_info ",
            "WHERE 1=1",
            "<when test='pageVO.data.gatherContext!=null and pageVO.data.gatherContext !=\"\"'>",
            "AND gather_context like '%${pageVO.data.gatherContext}%'",
            "</when>",
            "<when test='pageVO.data.databaseNameInput!=null and pageVO.data.databaseNameInput !=\"\"'>",
            "AND database_name_input = #{pageVO.data.databaseNameInput}",
            "</when>",
            "<when test='pageVO.data.syncType!=null and pageVO.data.syncType !=\"\"'>",
            "AND sync_type = #{pageVO.data.syncType}",
            "</when>",
            "LIMIT #{pageVO.dbIndex,jdbcType=INTEGER},#{pageVO.dbNumber,jdbcType=INTEGER}",
            "</script>"})
    public List<GatherDolphinInfo> selectGatherDolphinInfoList(@Param("pageVO") PageVO<GatherDolphinPageVo> pageVO, @Param("createUserId") int createUserId);

    @Select(value = "select gather_job_id,datasource_output from gather_dolphin_info where datasource_input = #{datasourceInputId} and  database_name_input = #{sourceDb} and table_name = #{sourceTable} ")
    public GatherDolphinInfo selectGatherJobId(@Param("datasourceInputId") int datasourceInputId , @Param("sourceDb")String  sourceDb, @Param("sourceTable")String sourceTable );

}
