package com.pd.pdp.server.service.impl;

import com.pd.pdp.server.entity.MysqlHiveSchemaCheck;
import com.pd.pdp.server.mapper.MysqlHiveSchemaCheckMapper;
import com.pd.pdp.server.service.MysqlHiveSchemaCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MysqlHiveSchemaCheckServiceImp implements MysqlHiveSchemaCheckService {
    @Autowired
    MysqlHiveSchemaCheckMapper mysqlHiveSchemaCheckMapper;

    @Override
    public List<MysqlHiveSchemaCheck> findToday() {
        return mysqlHiveSchemaCheckMapper.findToday();
    }
}
