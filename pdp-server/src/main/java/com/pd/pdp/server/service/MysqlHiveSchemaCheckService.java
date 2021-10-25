package com.pd.pdp.server.service;

import com.pd.pdp.server.entity.MysqlHiveSchemaCheck;

import java.util.List;

public interface MysqlHiveSchemaCheckService {

    /**
     * @Author pdp
     * @Description 查询今天mysql表元数据变更记录
     * @Param [id]
     * @return void
     **/
    public List<MysqlHiveSchemaCheck> findToday();
}
