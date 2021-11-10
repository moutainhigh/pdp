package com.pd.pdp.server.mapper;

import com.pd.notscan.BaseMapper;
import com.pd.pdp.server.entity.ProcessInstanceInfo;
import com.pd.pdp.server.entity.ProcessInstanceRunStatusStatistics;
import com.pd.pdp.server.vo.ProcessInstancePageVO;
import com.pd.pdp.server.vo.PageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface ProcessInstanceMapper extends BaseMapper<ProcessInstanceInfo> {

    @Select({"<script>",
            "SELECT count(1) FROM process_instance_info",
            "WHERE 1 = 1",
            "<when test='pageVO.data.dsId!=null and pageVO.data.dsId !=\"\"'>",
            "AND ds_id like '%${pageVO.data.dsId}%'",
            "</when>",
            "<when test='pageVO.data.processName!=null and pageVO.data.processName !=\"\"'>",
            "AND process_name like '%${pageVO.data.processName}%'",
            "</when>",
            "<when test='pageVO.data.projectName!=null and pageVO.data.projectName !=\"\"'>",
            "AND project_name like '%${pageVO.data.projectName}%'",
            "</when>",
            "<when test='pageVO.data.state!=null and pageVO.data.state !=\"\"'>",
            "AND state = #{pageVO.data.state}",
            "</when>",
            "<when test='pageVO.data.schedulingTime!=null and pageVO.data.schedulingTime !=\"\"'>",
            "AND scheduling_time like '%${pageVO.data.schedulingTime}%'",
            "</when>",
            "<when test='pageVO.data.executor!=null and pageVO.data.executor !=\"\"'>",
            "AND executor like '%${pageVO.data.executor}%'",
            "</when>",
            "</script>"})
    public Integer selectProcessInstanceCount(@Param("pageVO") PageVO<ProcessInstancePageVO> pageVO);

    @Select({"<script>",
            "SELECT * FROM process_instance_info",
            "WHERE 1 = 1",
            "<when test='pageVO.data.dsId!=null and pageVO.data.dsId !=\"\"'>",
            "AND ds_id like '%${pageVO.data.dsId}%'",
            "</when>",
            "<when test='pageVO.data.processName!=null and pageVO.data.processName !=\"\"'>",
            "AND process_name like '%${pageVO.data.processName}%'",
            "</when>",
            "<when test='pageVO.data.projectName!=null and pageVO.data.projectName !=\"\"'>",
            "AND project_name like '%${pageVO.data.projectName}%'",
            "</when>",
            "<when test='pageVO.data.state!=null and pageVO.data.state !=\"\"'>",
            "AND state = #{pageVO.data.state}",
            "</when>",
            "<when test='pageVO.data.schedulingTime!=null and pageVO.data.schedulingTime !=\"\"'>",
            "AND scheduling_time like '%${pageVO.data.schedulingTime}%'",
            "</when>",
            "<when test='pageVO.data.executor!=null and pageVO.data.executor !=\"\"'>",
            "AND executor like '%${pageVO.data.executor}%'",
            "</when>",
            "LIMIT #{pageVO.dbIndex,jdbcType=INTEGER},#{pageVO.dbNumber,jdbcType=INTEGER}",
            "</script>"})
    public List<ProcessInstanceInfo> selectProcessInstanceInfoList(@Param("pageVO") PageVO<ProcessInstancePageVO> pageVO);

    @Select(value = "SELECT b.scheduling_time as scheduling_time, count(b.scheduling_time) as count, state FROM (select DATE_FORMAT(a.scheduling_time,'%Y-%m-%d') as  scheduling_time, state from process_instance_info a where a.state = '${runState}' \n" +
            "and a.scheduling_time <> 'none' and UNIX_TIMESTAMP(a.scheduling_time) >= ${startUnixTime} and UNIX_TIMESTAMP(a.scheduling_time) <= ${endUnixTime}) b group by b.scheduling_time")
    public List<ProcessInstanceRunStatusStatistics> statisticsEveryDayRunStatus(int startUnixTime, int endUnixTime, String runState);
}