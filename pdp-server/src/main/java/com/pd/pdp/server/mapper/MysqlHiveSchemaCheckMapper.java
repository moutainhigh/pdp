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
    @Select(value = "select system_name, db_name, table_name, source_add_cloumns, source_delete_cloumns, update_time from pdp_db.mysql_hive_schema_check where DATE_FORMAT(update_time,'%Y-%m-%d') = current_date()")
    public List<MysqlHiveSchemaCheck> findToday();
}
