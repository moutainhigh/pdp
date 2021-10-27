package com.pd.pdp.server.mapper;

import com.pd.pdp.server.vo.PageVO;
import com.pd.pdp.server.entity.DataSourcesInfo;
import com.pd.pdp.server.entity.DataSourcesTypeInfo;
import com.pd.pdp.server.vo.DatasourcePageVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.pd.notscan.BaseMapper;

import java.util.List;


@Mapper
@Repository
public interface DataSourcesMapper extends BaseMapper<DataSourcesInfo> {

    @Delete("delete from data_sources_info where id = #{ids_str}")
    public int deleteBatchById(@Param("ids_str") String ids_str);

    @Select(value = "select id,sources_type from data_sources_type_info")
    public List<DataSourcesTypeInfo> selectDataSourcesType();

    @Select(value = "select sources_type from data_sources_type_info where id = ${id}")
    public DataSourcesTypeInfo selectDataSourcesTypeById(int id);


    @Select({"<script>",
            "SELECT count(1) FROM data_sources_info",
            "WHERE 1=1",
            "<when test='pageVO.data.dataSourceContext!=null and pageVO.data.dataSourceContext !=\"\"'>",
            "AND data_source_context like '%${pageVO.data.dataSourceContext}%'",
            "</when>",
            "<when test='pageVO.data.dataSourceType!=null and pageVO.data.dataSourceType !=\"\"'>",
            "AND data_source_type like '%${pageVO.data.dataSourceType}%'",
            "</when>",
            "<when test='pageVO.data.systemName!=null and pageVO.data.systemName !=\"\"'>",
            "AND system_name like '%${pageVO.data.systemName}%'",
            "</when>",
            "</script>"})
    public Integer selectDatasourcesCount(@Param("pageVO") PageVO<DatasourcePageVO> pageVO, @Param("createUserId") int createUserId);


    @Select({"<script>",
            "SELECT * FROM data_sources_info",
            "WHERE 1=1",
            "<when test='pageVO.data.dataSourceContext!=null and pageVO.data.dataSourceContext !=\"\"'>",
            "AND data_source_context like '%${pageVO.data.dataSourceContext}%'",
            "</when>",
            "<when test='pageVO.data.dataSourceType!=null and pageVO.data.dataSourceType !=\"\"'>",
            "AND data_source_type like '%${pageVO.data.dataSourceType}%'",
            "</when>",
            "<when test='pageVO.data.systemName!=null and pageVO.data.systemName !=\"\"'>",
            "AND system_name like '%${pageVO.data.systemName}%'",
            "</when>",
            "LIMIT #{pageVO.dbIndex,jdbcType=INTEGER},#{pageVO.dbNumber,jdbcType=INTEGER}",
            "</script>"})
    public List<DataSourcesInfo> selectDataSourcesInfoList(@Param("pageVO") PageVO<DatasourcePageVO> pageVO, @Param("createUserId") int createUserId);
}
