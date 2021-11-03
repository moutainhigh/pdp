package com.pd.pdp.server.mapper;

import com.pd.notscan.BaseMapper;
import com.pd.pdp.server.entity.CountCheckResult;
import com.pd.pdp.server.entity.GatherQualityInfo;
import com.pd.pdp.server.entity.MysqlHiveSchemaCheck;
import com.pd.pdp.server.entity.QualityRulesInfo;
import com.pd.pdp.server.vo.GatherQualityPageVo;
import com.pd.pdp.server.vo.PageVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
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


}
