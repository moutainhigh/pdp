package com.pd.pdp.mapper;

import com.pd.notscan.BaseMapper;
import com.pd.pdp.entity.DataSourcesInfo;
import com.pd.pdp.entity.DataSourcesTypeInfo;
import com.pd.pdp.vo.DatasourcePageVO;
import com.pd.pdp.vo.PageVO;
import com.pd.pdp.vo.UserPageVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ClassName: DataSourcesMapper
 *
 * @author zyc-admin
 * @date 2017年12月26日
 * @Description:
 */
@Mapper
@Repository
public interface DataSourcesMapper extends BaseMapper<DataSourcesInfo> {

    @Delete("delete from data_sources_info where id = #{ids_str}")
    public int deleteBatchById(@Param("ids_str") String ids_str);

    @Select(value = "select id,sources_type from data_sources_type_info")
    public List<DataSourcesTypeInfo> selectDataSourcesType();


    @Select({"<script>",
            "SELECT count(1) FROM data_sources_info",
            "WHERE create_user_id=#{createUserId}",
            "<when test='pageVO.data.dataSourceContext!=null and pageVO.data.dataSourceContext !=\"\"'>",
            "AND data_source_context like '%${pageVO.data.dataSourceContext}%'",
            "</when>",
            "<when test='pageVO.data.dataSourceType!=null and pageVO.data.dataSourceType !=\"\"'>",
            "AND data_source_type = #{pageVO.data.dataSourceType}",
            "</when>",
            "<when test='pageVO.data.url!=null and pageVO.data.url !=\"\"'>",
            "AND url like '%${pageVO.data.url}%'",
            "</when>",
            "</script>"})
    public Integer selectDatasourcesCount(@Param("pageVO") PageVO<DatasourcePageVO> pageVO, @Param("createUserId") int createUserId);


    @Select({"<script>",
            "SELECT * FROM data_sources_info",
            "WHERE create_user_id=#{createUserId}",
            "<when test='pageVO.data.dataSourceContext!=null and pageVO.data.dataSourceContext !=\"\"'>",
            "AND data_source_context like '%${pageVO.data.dataSourceContext}%'",
            "</when>",
            "<when test='pageVO.data.dataSourceType!=null and pageVO.data.dataSourceType !=\"\"'>",
            "AND data_source_type = #{pageVO.data.dataSourceType}",
            "</when>",
            "<when test='pageVO.data.url!=null and pageVO.data.url !=\"\"'>",
            "AND url like '%${pageVO.data.url}%'",
            "</when>",
            "LIMIT #{pageVO.dbIndex,jdbcType=INTEGER},#{pageVO.dbNumber,jdbcType=INTEGER}",
            "</script>"})
    public List<DataSourcesInfo> selectDataSourcesInfoList(@Param("pageVO") PageVO<DatasourcePageVO> pageVO, @Param("createUserId") int createUserId);
}
