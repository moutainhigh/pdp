package com.pd.pdp.server.mapper;

import com.pd.notscan.BaseMapper;
import com.pd.pdp.server.entity.MysqlHiveSchemaCheck;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MysqlHiveSchemaCheckMapper extends BaseMapper<MysqlHiveSchemaCheck> {
    @Select(value = "select * from pdp_bi_db.mysql_hive_schema_check where DATE_FORMAT(create_time,'%Y-%m-%d') = current_date()")
    public List<MysqlHiveSchemaCheck> findToday();
}
