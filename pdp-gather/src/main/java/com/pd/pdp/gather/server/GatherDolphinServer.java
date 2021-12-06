package com.pd.pdp.gather.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.gather.config.GatherProperties;
import com.pd.pdp.gather.connector.GatherConnector;
import com.pd.pdp.gather.connector.GatherSession;
import com.pd.pdp.gather.connector.impl.ConnectionProviderHikariCP;
import com.pd.pdp.gather.constant.Constant;
import com.pd.pdp.gather.constant.DataSoureceType;
import com.pd.pdp.gather.entity.GatherDolphinJobEntity;
import com.pd.pdp.gather.okhttp.FastHttpClient;
import com.pd.pdp.gather.process.DolphinRequest;
import com.pd.pdp.gather.process.MetaDataConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/11
 */

@Service
public class GatherDolphinServer {
    private Logger logger = LoggerFactory.getLogger(GatherDolphinServer.class);

    @Autowired
    GatherConnector gatherConnector;

    @Autowired
    GatherProperties gatherProperties;

    @Autowired
    MetaDataConvert metaDataConvert;

    @Autowired
    DolphinRequest dolphinRequest;

    @Autowired
    GatherSession gatherSession;


    public List<Map<String, String>> selectDBByDatasource(String driver, String url, String username, String password, String dataSourceType) {
        logger.info("start exec selectDBByDatasource which has dataSourceType...");

        DataSoureceType type = DataSoureceType.getType(dataSourceType);
        List<Map<String, String>> list = null;
        switch (type) {
            case JDBC:
            case HIVE:
                list = selectDBByDatasource(driver, url, username, password);
                break;
            case HTTP:
                try {
                    list = getDolphinProject(url, username, password);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new IllegalStateException("Unexpected value: " + dataSourceType);
                }
                break;
            default:
                logger.error("Unexpected value: " + dataSourceType);
                throw new IllegalStateException("Unexpected value: " + dataSourceType);
        }

        return list;
    }

    public List<Map<String, String>> selectDBByDatasource(String driver, String url, String username, String password) {
        logger.info("start exec selectDBByDatasource...");

        ConnectionProviderHikariCP jdbcConn = gatherConnector.getConnInPool(driver, url, username, password);

        //为sql server做特殊处理
        List<Map<String, Object>> dbs = null;
        if (url.contains(Constant.SQL_SERVER)) {
            dbs = jdbcConn.excuteQuery(Constant.SHOW_DATABASES_SQL_SERVER);
        } else {
            dbs = jdbcConn.excuteQuery(Constant.SHOW_DATABASES);
        }

        List<Map<String, String>> list = new ArrayList<>();

        //hive/mysql读取的字段名不一致，将字段都替换为dbName
        for (Map<String, Object> map : dbs) {
            HashMap<String, String> hashMap = new HashMap<>();

            hashMap.put("dbName", map.values().toArray()[0].toString());
            list.add(hashMap);
        }

        return list;
    }

    public List<Map<String, String>> selectTablesByDB(String db, String driver, String url, String username, String password) {
        logger.info("start exec selectTablesByDB...");

        ConnectionProviderHikariCP jdbcConn = gatherConnector.getConnInPool(driver, url, username, password);

        List<Map<String, Object>> tables = null;
        if (url.contains(Constant.SQL_SERVER)) {
            tables = jdbcConn.excuteQuery(String.format(Constant.SHOW_TABLES_SQL_SERVER, db));
        } else {
            tables = jdbcConn.excuteQuery(String.format(Constant.SHOW_TABLES, db));
        }


        List<Map<String, String>> list = new ArrayList<>();

        //hive/mysql读取的字段名不一致，将字段都替换为tableName
        for (Map<String, Object> map : tables) {
            HashMap<String, String> hashMap = new HashMap<>();

            hashMap.put("tableName", map.values().toArray()[0].toString());
            list.add(hashMap);
        }

        return list;
    }


    public List<Map<String, String>> getDolphinProject(String url, String username, String password) throws Exception {
        logger.info("start exec getDolphinProject...");

        List<Map<String, String>> resultList = new ArrayList<>();

        String allProjectUrl = String.format(Constant.DOLPHIN_ALL_PROJECT_URL, url);
        String sessionId = gatherSession.getDolphinSession(url, username, password);
        String response = FastHttpClient.
                get().
                addHeader("cookie", sessionId).
                url(allProjectUrl).
                build().
                execute().string();

        //{"code":0,"msg":"success","data":{"totalList":[{"id":8,"userId":6,"userName":"xx","name":"report_data_sync","description":"日志数据同步","createTime":"2021-09-08T17:30:42.000+0800","updateTime":"2021-09-08T17:30:42.000+0800","perm":7,"defCount":7,"instRunningCount":0}],"total":6,"currentPage":1,"totalPage":1}}

        JSONArray jsonArray = JSON.parseObject(response).getJSONObject("data").getJSONArray("totalList");
        for (int i = 0; i < jsonArray.size(); i++) {
            HashMap<String, String> resultMap = new HashMap<>();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("name");
            resultMap.put("dbName", name);
            resultList.add(resultMap);
        }

        return resultList;
    }

    public boolean testConn(String driver, String url, String username, String password, String dataSourceType) {
        logger.info("start exec testConn...");

        DataSoureceType type = DataSoureceType.getType(dataSourceType);
        boolean result = false;
        switch (type) {
            case JDBC:
            case HIVE:
                List<Map<String, String>> maps = selectDBByDatasource(driver, url, username, password);
                if (maps.size() != 0) {
                    result = true;
                }
                break;
            case HTTP:
                try {
                    String dolphinSession = gatherSession.getDolphinSession(url, username, password);
                    if (dolphinSession != null) {
                        result = true;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new IllegalStateException("Unexpected value: " + dataSourceType);
                }
                break;
            default:
                logger.error("Unexpected value: " + dataSourceType);
                throw new IllegalStateException("Unexpected value: " + dataSourceType);
        }
        return result;
    }

    public String initJob(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec initJob...");

        //setInputTableInfo
        boolean setInputTableInfo = setInputTableInfo(gatherDolphinJobEntity);
        if (!setInputTableInfo) {
            return null;
        }

        //createHiveTable
        boolean hiveTable = createHiveTable(gatherDolphinJobEntity);
        if (!hiveTable) {
            return null;
        }

        //（2增量）的数据初始化按（3每日快照）逻辑生成json
        gatherDolphinJobEntity.setSyncType(3);
        //创建datax json
        String dataXJsonString = metaDataConvert.writeDataXJson(gatherDolphinJobEntity);

        //创建dolphin job任务链json
        HashMap createDolphinJobJsonMap = dolphinRequest.getCreateDolphinJobJson(dataXJsonString, gatherDolphinJobEntity);
        //（2增量）cast(date_format(update_time,'yyyyMM') as int) 按照月分区，（3每日快照）cast(date_format(date_sub(stg_update_time,1 ),'yyyyMMdd') as int)。data check sql sync_type="3"
        //TODO 先硬编码...
        String partitionColName = gatherProperties.getHiveOdsPartitionCol().split(Constant.BACK_QUOTE)[1].trim();
        String partitionColNameMonth = gatherProperties.getHiveOdsPartitionColMonth().split(Constant.BACK_QUOTE)[1].trim();
        String replace = createDolphinJobJsonMap.get(Constant.PROCESS_DEFINITION_JSON).toString()
                .replace("yyyyMMdd","yyyyMM")
                .replace(partitionColName,partitionColNameMonth)
                .replace(Constant.UNDERLINE + Constant.SYNC_TYPE_SNAPSHOT + Constant.UNDERLINE, Constant.UNDERLINE + Constant.SYNC_TYPE_INCRE + Constant.UNDERLINE)
                .replace("cast(date_format(date_sub(stg_update_time,1 ),'yyyyMMdd') as int)", "cast(date_format(update_time,'yyyyMM') as int)").replace("sync_type=\\\"3\\\"", "sync_type=\\\"2\\\"");
        createDolphinJobJsonMap.put(Constant.PROCESS_DEFINITION_JSON, replace);

        // create dolphin job request
        String response = dolphinRequest.createOrUpdateDolphinJob(createDolphinJobJsonMap, gatherDolphinJobEntity);
        //{"code":0,"msg":"success","data":null}
        if (JSONObject.parseObject(response).getInteger(Constant.CODE) == 0) {
            //dolphin job id
            JSONObject jsonObject = dolphinRequest.searchDolphinJob(gatherDolphinJobEntity);
            if (jsonObject == null) {
                return null;
            }
            String jobId = jsonObject.getString(Constant.ID);
            if (jobId != null) {
                //on line dolphin job, 上线运行一次
                response = dolphinRequest.onlineDolphinJob(gatherDolphinJobEntity);
            }
        }

        return response;
    }


    public String createJobByTemplate(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec createJobByTemplate...");

        //setInputTableInfo
        boolean setInputTableInfo = setInputTableInfo(gatherDolphinJobEntity);
        if (!setInputTableInfo) {
            return null;
        }

        //createHiveTable
        boolean hiveTable = createHiveTable(gatherDolphinJobEntity);
        if (!hiveTable) {
            return null;
        }

        //创建datax json
        String dataXJsonString = metaDataConvert.writeDataXJson(gatherDolphinJobEntity);

        //创建dolphin job任务链json
        HashMap createDolphinJobJsonMap = dolphinRequest.getCreateDolphinJobJson(dataXJsonString, gatherDolphinJobEntity);
        // create dolphin job request，按照odstableName+job_id命名，已存在会被更新
        String response = dolphinRequest.createOrUpdateDolphinJob(createDolphinJobJsonMap, gatherDolphinJobEntity);
        //{"code":0,"msg":"success","data":null}
        if (JSONObject.parseObject(response).getInteger(Constant.CODE) == 0) {
            //dolphin job id
            JSONObject jsonObject = dolphinRequest.searchDolphinJob(gatherDolphinJobEntity);
            if (jsonObject == null) {
                return null;
            }
            String jobId = jsonObject.getString(Constant.ID);
            if (jobId != null) {
                //on line dolphin job, 必需上线才能创建定时任务
                response = dolphinRequest.onlineDolphinJob(gatherDolphinJobEntity);
                if (JSONObject.parseObject(response).getInteger(Constant.CODE) == 0) {
                    //job online success then create scheduler
                    response = dolphinRequest.createOrUpdateScheduler(jobId, gatherDolphinJobEntity);
                    if (JSONObject.parseObject(response).getInteger(Constant.CODE) == 0) {
                        //创建完定时任务，下线job
                        response = dolphinRequest.offLineDolphinJob(gatherDolphinJobEntity);
                    }
                }
            }
        }

        return response;
    }


    public boolean setInputTableInfo(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec setInputTableInfo...");
        ConnectionProviderHikariCP connOfInputDS = null;

        //set table col meta
        List<Map<String, Object>> columnsOfInputTable = null;
        List<Map<String, Object>> tableCommentListOfInputTable = null;
        if (gatherDolphinJobEntity.getUrlInput().contains(Constant.SQL_SERVER)) {
            String urlInput = gatherDolphinJobEntity.getUrlInput();
            if (!urlInput.contains(Constant.DATABASE_SQL_SERVER)) {
                urlInput += Constant.DATABASE_SQL_SERVER + gatherDolphinJobEntity.getDatabaseNameInput();
            }

            //input data source info
            connOfInputDS = gatherConnector.getConnInPool(gatherDolphinJobEntity.getDriverInput(), urlInput, gatherDolphinJobEntity.getUsernameInput(), gatherDolphinJobEntity.getPasswordInput());
            columnsOfInputTable = connOfInputDS.excuteQuery(String.format(Constant.SHOW_COLUMNS_SQL_SERVER, gatherDolphinJobEntity.getTableNameInput()));
            tableCommentListOfInputTable = connOfInputDS.excuteQuery(String.format(Constant.TABLE_COMMENT_SQL_SERVER, gatherDolphinJobEntity.getTableNameInput()));

        } else {
            connOfInputDS = gatherConnector.getConnInPool(gatherDolphinJobEntity.getDriverInput(), gatherDolphinJobEntity.getUrlInput(), gatherDolphinJobEntity.getUsernameInput(), gatherDolphinJobEntity.getPasswordInput());
            columnsOfInputTable = connOfInputDS.excuteQuery(String.format(Constant.SHOW_COLUMNS, gatherDolphinJobEntity.getDatabaseNameInput(), gatherDolphinJobEntity.getDatabaseNameInput(), gatherDolphinJobEntity.getTableNameInput()));
            tableCommentListOfInputTable = connOfInputDS.excuteQuery(String.format(Constant.TABLE_COMMENT, gatherDolphinJobEntity.getTableNameInput()));
        }
        List<Map<String, String>> columnsInfoList = new ArrayList<>();
        for (Map<String, Object> col : columnsOfInputTable) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(Constant.COL_META_NAME, col.get(Constant.COL_META_NAME).toString());
            hashMap.put(Constant.COL_META_TYPE, col.get(Constant.COL_META_TYPE).toString());
            hashMap.put(Constant.COL_META_COMMENT, col.get(Constant.COL_META_COMMENT).toString());
            columnsInfoList.add(hashMap);
        }
        gatherDolphinJobEntity.setColumnsOfInputTable(columnsInfoList);

        //[{TABLE_COMMENT=Database privileges}]

        String tableCommentOfInputTable = tableCommentListOfInputTable.get(0).getOrDefault(Constant.TABLE_COMMENT_KEY, "").toString();
        gatherDolphinJobEntity.setTableCommentOfInputTable(tableCommentOfInputTable);

        return true;
    }

    public boolean createHiveTable(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec createHiveTable...");

        HashMap<String, String> hiveTableSqls = metaDataConvert.createHiveTableMeta(gatherDolphinJobEntity);
        gatherDolphinJobEntity.setHiveTableSqls(hiveTableSqls);

        //create hive table
        if (gatherDolphinJobEntity.isCreateHiveTable()) {
            ConnectionProviderHikariCP connOfOutputDS = gatherConnector.getConnInPool(gatherDolphinJobEntity.getDriverOutput(), gatherDolphinJobEntity.getUrlOutput(), gatherDolphinJobEntity.getUsernameOutput(), gatherDolphinJobEntity.getPasswordOutput());
            try {
                connOfOutputDS.exec(hiveTableSqls.get(Constant.STG));
                connOfOutputDS.exec(hiveTableSqls.get(Constant.ODS));
            } catch (SQLException throwables) {
                return false;
            }
        }
        return true;
    }

    public String online(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec online...");

        String response = null;

        //dolphin job id
        JSONObject jsonObject = dolphinRequest.searchDolphinJob(gatherDolphinJobEntity);
        if (jsonObject == null) {
            return null;
        }
        String jobId = jsonObject.getString(Constant.ID);
        if (jobId != null) {
            //on line dolphin job
            response = dolphinRequest.onlineDolphinJob(gatherDolphinJobEntity);
            if (JSONObject.parseObject(response).getInteger(Constant.CODE) == 0) {
                //job online success
                JSONObject schedulerJson = dolphinRequest.getSchedulerJson(jobId, gatherDolphinJobEntity);

                String schedulerId = null;
                if (schedulerJson != null) {
                    schedulerId = schedulerJson.getString(Constant.ID);
                }
                if (schedulerId != null) {
                    response = dolphinRequest.onLineScheduler(schedulerId, gatherDolphinJobEntity);
                }
            }
        }

        return response;
    }

    public String offline(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec offline...");

        String offLineResponse = dolphinRequest.offLineDolphinJob(gatherDolphinJobEntity);

        return offLineResponse;
    }

    public String delDolphinJob(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec delDolphinJob...");

        //先下线任务
        dolphinRequest.offLineDolphinJob(gatherDolphinJobEntity);

        String response = dolphinRequest.delDolphinJob(gatherDolphinJobEntity);
        return response;
    }


    public JSONArray getDolphinJobJson(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec getDolphinJobJson...");

        JSONArray response = dolphinRequest.getDolphinJobJson(gatherDolphinJobEntity);

        return response;
    }

    public String createJobByJson(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec createJobByJson...");

        //先删除原有任务
        delDolphinJob(gatherDolphinJobEntity);

        String response = dolphinRequest.createJobByJson(gatherDolphinJobEntity);
        return response;
    }

}
