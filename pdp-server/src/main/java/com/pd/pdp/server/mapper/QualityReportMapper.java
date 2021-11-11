package com.pd.pdp.server.mapper;

import com.pd.notscan.BaseMapper;
import com.pd.pdp.server.entity.*;
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
public interface QualityReportMapper extends BaseMapper<CountCheckResult> {

    @Select(value = "select * from count_check_result where DATE_FORMAT(create_time,'%Y-%m-%d') = current_date()")
    public List<CountCheckResult> findToday();

    @Select(value = "select b.create_time, count(b.create_time) as count, `result` from (select DATE_FORMAT(a.create_time,'%Y-%m-%d') as create_time, a.`result` from count_check_result a where UNIX_TIMESTAMP(a.create_time) >= ${startUnixTime} and UNIX_TIMESTAMP(a.create_time) <= ${endUnixTime} and a.`result` = ${checkResult}) b GROUP BY b.create_time")
    public List<DataCheckResultStatistics> statisticsEveryDayDataQuality(@Param("startUnixTime") int startUnixTime, @Param("endUnixTime") int endUnixTime, @Param("checkResult") int checkResult);


}
