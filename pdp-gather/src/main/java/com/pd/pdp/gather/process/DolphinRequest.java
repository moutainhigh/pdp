package com.pd.pdp.gather.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.gather.config.GatherProperties;
import com.pd.pdp.gather.connector.GatherSession;
import com.pd.pdp.gather.constant.Constant;
import com.pd.pdp.gather.entity.GatherDolphinJobEntity;
import com.pd.pdp.gather.okhttp.FastHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/14
 */

@Service
public class DolphinRequest {
    private Logger logger = LoggerFactory.getLogger(DolphinRequest.class);

    @Autowired
    GatherProperties gatherProperties;
    @Autowired
    GatherSession gatherSession;


    public HashMap getCreateDolphinJobJson(String dataXJson, GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec getCreateDolphinJobJson...");

        String stgToOdsSql = "";
        String odsCols = "";
        String stgTCols = "";
        String stgCols = "";
        String description = "";
        String checkSqlInput = "";
        String checkSqlOutput = "";

        String partitionColName = gatherProperties.getHiveOdsPartitionCol().split(Constant.BACK_QUOTE)[1].trim();
        String partitionColNameMonth = gatherProperties.getHiveOdsPartitionColMonth().split(Constant.BACK_QUOTE)[1].trim();
        String dolphinProjectName = gatherDolphinJobEntity.getDolphinProjectName();

        int syncType = gatherDolphinJobEntity.getSyncType();
        String syncTypeString = "";
        //1 全量，2 增量，3 每日快照
        switch (syncType) {
            case 1:
                syncTypeString = Constant.SYNC_TYPE_FULL;
                break;
            case 2:
                syncTypeString = Constant.SYNC_TYPE_INCRE;
                break;
            case 3:
                syncTypeString = Constant.SYNC_TYPE_SNAPSHOT;
                break;
            default:
                break;
        }
        String stgTableName = gatherDolphinJobEntity.getSystemName() + Constant.UNDERLINE + gatherDolphinJobEntity.getDatabaseNameInput() + Constant.UNDERLINE + gatherDolphinJobEntity.getTableNameInput() + Constant.UNDERLINE + gatherProperties.getHiveStgTableLastFix();
        String odsTableName = gatherDolphinJobEntity.getSystemName() + Constant.UNDERLINE + gatherDolphinJobEntity.getDatabaseNameInput() + Constant.UNDERLINE + gatherDolphinJobEntity.getTableNameInput() + Constant.UNDERLINE + syncTypeString + Constant.UNDERLINE + gatherProperties.getHiveOdsTableLastFix();
        //replace "-" to "_"
        stgTableName = stgTableName.replaceAll(Constant.MIDLINE, Constant.UNDERLINE);
        odsTableName = odsTableName.replaceAll(Constant.MIDLINE, Constant.UNDERLINE);


        String name = odsTableName + Constant.UNDERLINE + gatherDolphinJobEntity.getGatherJobId();
        //stg to ods sql
        List<Map<String, String>> columnsOfInputTable = gatherDolphinJobEntity.getColumnsOfInputTable();
        for (Map<String, String> colMap : columnsOfInputTable) {
            String colName = colMap.get(Constant.COL_META_NAME);

            stgCols += Constant.BACK_QUOTE + colName + Constant.BACK_QUOTE + Constant.COMMA + Constant.ENTER + Constant.SPACE;
            stgTCols += "stg_t." + Constant.BACK_QUOTE + colName + Constant.BACK_QUOTE + Constant.COMMA + Constant.ENTER + Constant.SPACE;
            odsCols += "ods_t." + Constant.BACK_QUOTE + colName + Constant.BACK_QUOTE + Constant.COMMA + Constant.ENTER + Constant.SPACE;
        }


        String stgAddCol = gatherProperties.getHiveStgAddCol();
        String odsAddCol = gatherProperties.getHiveOdsAddCol();
        String odsAddColName = odsAddCol.split(Constant.BACK_QUOTE)[1];
        stgCols += Constant.BACK_QUOTE + stgAddCol.split(Constant.BACK_QUOTE)[1] + Constant.BACK_QUOTE;


        switch (gatherDolphinJobEntity.getSyncType()) {
            //全量
            case 1:
                stgToOdsSql = gatherProperties.getDolphinStgToOdsSqlFull().replace(Constant.ODS_TABLE_NAME, odsTableName).replace(Constant.STG_TABLE_NAME, stgTableName)
                        .replace(Constant.STG_COLS, stgCols);
                description = Constant.SYNC_TYPE_FULL + Constant.COLON + gatherDolphinJobEntity.getGatherJobId();
                checkSqlInput = gatherProperties.getDolphinCheckFullInputQuerySql();
                checkSqlOutput = gatherProperties.getDolphinCheckFullOutputQuerySql();
                break;
            //增量(月分区)
            case 2:

                stgTCols += "stg_t." + Constant.BACK_QUOTE + odsAddColName + Constant.BACK_QUOTE + Constant.COMMA + Constant.ENTER;
                odsCols += "ods_t." + Constant.BACK_QUOTE + odsAddColName + Constant.BACK_QUOTE + Constant.COMMA + Constant.ENTER;
                stgToOdsSql = gatherProperties.getDolphinStgToOdsSqlIncrement().replace(Constant.ODS_TABLE_NAME, odsTableName).replace(Constant.STG_TABLE_NAME, stgTableName)
                        .replace(Constant.ODS_COLS, odsCols).replace(Constant.STG_T_COLS, stgTCols).replace(Constant.STG_COLS, stgCols + " as " + odsAddColName + Constant.COMMA + Constant.SPACE).replace(Constant.PARTITION_COL_NAME, partitionColNameMonth);
                description = Constant.SYNC_TYPE_INCRE + Constant.COLON + gatherDolphinJobEntity.getGatherJobId();
                checkSqlInput = gatherProperties.getDolphinCheckIncrementInputQuerySql();
                checkSqlOutput = gatherProperties.getDolphinCheckIncrementOutputQuerySql().replace(Constant.DATA_CHECK_COL, odsAddColName);
                break;
            //每日快照
            case 3:
                stgToOdsSql = gatherProperties.getDolphinStgToOdsSqlSnapshot().replace(Constant.ODS_TABLE_NAME, odsTableName).replace(Constant.STG_TABLE_NAME, stgTableName)
                        .replace(Constant.STG_COLS, stgCols).replace(Constant.PARTITION_COL_NAME, partitionColName);
                description = Constant.SYNC_TYPE_SNAPSHOT + Constant.COLON + gatherDolphinJobEntity.getGatherJobId();
                checkSqlInput = gatherProperties.getDolphinCheckFullInputQuerySql();
                checkSqlOutput = gatherProperties.getDolphinCheckIncrementOutputQuerySql().replace(Constant.DATA_CHECK_COL, odsAddColName);
                break;
            default:
                break;
        }

        //dolphin job json file
//        String path = MetaDataConvert.class.getClassLoader().getResource(Constant.GATHER_DOLPHIN_FILE_NAME).getPath();
        ClassPathResource classPathResource = new ClassPathResource(Constant.GATHER_DOLPHIN_FILE_NAME);
        StringBuilder jsonStringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()));) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonStringBuilder.append(line + Constant.ENTER);
            }

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        HashMap dolphinMap = JSONObject.parseObject(String.valueOf(jsonStringBuilder), HashMap.class);
        dolphinMap.put(Constant.DESCRIPTION, description);
        dolphinMap.put(Constant.NAME, name);
        dolphinMap.put(Constant.PROJECT_NAME, dolphinProjectName);
        dolphinMap.put(Constant.CONNECTIONS, JSON.toJSONString(dolphinMap.get(Constant.CONNECTIONS)));
        dolphinMap.put(Constant.LOCATIONS, JSON.toJSONString(dolphinMap.get(Constant.LOCATIONS)));

        Object processDefinitionObject = dolphinMap.get(Constant.PROCESS_DEFINITION_JSON);
        LinkedHashMap processDefinitionJsonMap = JSONObject.parseObject(String.valueOf(processDefinitionObject), LinkedHashMap.class);
        processDefinitionJsonMap.put(Constant.TENANT_ID_NAME, gatherProperties.getDolphinTenant());

        //0 清空stg, 1 stg同步到ods, 2 mysql同步到stg, 3 invalidate metadata, 4 数据校验
        JSONArray tasksArray = JSONArray.parseArray(JSONArray.toJSONString(processDefinitionJsonMap.get(Constant.TASKS)));
        JSONObject task0Object = JSONObject.parseObject(tasksArray.get(0).toString());
        JSONObject task1Object = JSONObject.parseObject(tasksArray.get(1).toString());
        JSONObject task2Object = JSONObject.parseObject(tasksArray.get(2).toString());
        JSONObject task3Object = JSONObject.parseObject(tasksArray.get(3).toString());
        JSONObject task4Object = JSONObject.parseObject(tasksArray.get(4).toString());
        JSONObject task0 = task0Object.getJSONObject(Constant.PARAMS);
        JSONObject task1 = task1Object.getJSONObject(Constant.PARAMS);
        JSONObject task2 = task2Object.getJSONObject(Constant.PARAMS);
        JSONObject task3 = task3Object.getJSONObject(Constant.PARAMS);
        JSONObject task4 = task4Object.getJSONObject(Constant.PARAMS);

        //0
        HashMap task0Map = JSONObject.parseObject(JSONObject.toJSONString(task0), HashMap.class);
        task0Map.put(Constant.SQL, String.format(Constant.TRUNCATE_TABLE, Constant.STG, stgTableName));
        task0Map.put(Constant.DATASOURCE_NAME, gatherProperties.getDolphinDatasourceName());
        task0Map.put(Constant.DATASOURCE, Integer.valueOf(gatherProperties.getDolphinDatasourceId()));
        //1
        HashMap task1Map = JSONObject.parseObject(JSONObject.toJSONString(task1), HashMap.class);
        task1Map.put(Constant.SQL, stgToOdsSql);
        task1Map.put(Constant.DATASOURCE_NAME, gatherProperties.getDolphinDatasourceName());
        task1Map.put(Constant.DATASOURCE, Integer.valueOf(gatherProperties.getDolphinDatasourceId()));
        //2
        HashMap task2Map = JSONObject.parseObject(JSONObject.toJSONString(task2), HashMap.class);
        task2Map.put(Constant.JSON, dataXJson);
        //3
        HashMap task3Map = JSONObject.parseObject(JSONObject.toJSONString(task3), HashMap.class);
        task3Map.put(Constant.RAW_SCRIPT, String.format(gatherProperties.getDolphinOdsInvalidateMetadataSql(), Constant.ODS, odsTableName));

        String urlInput = gatherDolphinJobEntity.getUrlInput();
        String[] urlAndPort = urlInput.split(Constant.BACKSLASH)[2].split(Constant.COLON);
        String ip = urlAndPort[0];
        String port = urlAndPort[1];
        if (port.contains(Constant.BACKSLASH)) {
            port = port.substring(0, port.indexOf(Constant.BACKSLASH));
        } else {
            if (port.contains(Constant.QUESTION)) {
                port = port.substring(0, port.indexOf(Constant.QUESTION));
            }
        }

        String dolphinCheckDataShell = gatherProperties.getDolphinCheckDataShell();

        //sqlserver 做特殊处理
        if (gatherDolphinJobEntity.getUrlInput().contains(Constant.SQL_SERVER)) {
            if (port.contains(Constant.SEMICOLONS)) {
                port = port.substring(0, port.indexOf(Constant.SEMICOLONS));
            }
            dolphinCheckDataShell = dolphinCheckDataShell.replace("mysql -s -N -h$ip_input -P$port_input -u$user_name_input -p`echo -n \"$password_input\" | base64 -d`  -e \"$mysql_query\"", "sqlcmd -S $ip_input -H $port_input  -U $user_name_input  -P`echo -n \"$password_input\" | base64 -d`  -Q \"$mysql_query\" -h-1| awk -F'('  '{print $1}'");
            checkSqlInput = checkSqlInput.replace("$dbname_input.$tablename_input", "$dbname_input..$tablename_input");

        }

        dolphinCheckDataShell = dolphinCheckDataShell
                .replace(Constant.SYSTEM_NAME, gatherDolphinJobEntity.getSystemName())
                .replace(Constant.DATABASE_NAME_INPUT, gatherDolphinJobEntity.getDatabaseNameInput())
                .replace(Constant.TABLE_NAME_INPUT, gatherDolphinJobEntity.getTableNameInput()).replace(Constant.IP_INPUT, ip)
                .replace(Constant.PORT_INPUT, port)
                .replace(Constant.USERNAME_INPUT, gatherDolphinJobEntity.getUsernameInput())
                .replace(Constant.PASSWORD_INPUT, gatherDolphinJobEntity.getPasswordInput())
                .replace(Constant.URL_OUTPUT, gatherDolphinJobEntity.getUrlOutput())
                .replace(Constant.DATABASE_NAME_OUTPUT, Constant.ODS)
                .replace(Constant.TABLE_NAME_OUTPUT, odsTableName)
                .replace(Constant.USERNAME_OUTPUT, gatherDolphinJobEntity.getUsernameOutput())
                .replace(Constant.PASSWORD_OUTPUT, gatherDolphinJobEntity.getPasswordOutput())
                .replace(Constant.GATHER_JOB_ID, gatherDolphinJobEntity.getGatherJobId())
                .replace(Constant.SYNC_TYPE, String.valueOf(gatherDolphinJobEntity.getSyncType()))
                .replace(Constant.MYSQL_QUERY, checkSqlInput)
                .replace(Constant.HIVE_QUERY, checkSqlOutput);
        //4
        HashMap task4Map = JSONObject.parseObject(JSONObject.toJSONString(task4), HashMap.class);
        task4Map.put(Constant.RAW_SCRIPT, dolphinCheckDataShell);

        task0Object.put(Constant.PARAMS, task0Map);
        task1Object.put(Constant.PARAMS, task1Map);
        task2Object.put(Constant.PARAMS, task2Map);
        task3Object.put(Constant.PARAMS, task3Map);
        task4Object.put(Constant.PARAMS, task4Map);
        tasksArray.clear();
        tasksArray.add(task0Object);
        tasksArray.add(task1Object);
        tasksArray.add(task2Object);
        tasksArray.add(task3Object);
        tasksArray.add(task4Object);
        processDefinitionJsonMap.put(Constant.TASKS, tasksArray);
        String processDefinitionJsonString = JSON.toJSONString(processDefinitionJsonMap);
        dolphinMap.put(Constant.PROCESS_DEFINITION_JSON, processDefinitionJsonString.replace("\\\\{", "{").replace("\\\\}", "}"));


        logger.info("dolphinMap:\n{}", JSON.toJSONString(dolphinMap));

        return dolphinMap;

    }

    public String createOrUpdateDolphinJob(HashMap createDolphinJobJsonMap, GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec createOrUpdateDolphinJob...");

        String dolphinProjectName = gatherDolphinJobEntity.getDolphinProjectName();
        String urlDolphin = gatherDolphinJobEntity.getUrlDolphin();
        String saveUrl = String.format(Constant.SAVE_URL, urlDolphin, dolphinProjectName);
        String updateUrl = String.format(Constant.UPDATE_URL, urlDolphin, dolphinProjectName);

        String dolphinSession = "";
        String response = "";
        String jobId = null;
        try {
            dolphinSession = gatherSession.getDolphinSession(urlDolphin, gatherDolphinJobEntity.getUsernameDolphin(), gatherDolphinJobEntity.getPasswordDolphin());
            JSONObject searchDolphinJobJson = searchDolphinJob(gatherDolphinJobEntity);
            if (searchDolphinJobJson != null) {
                String releaseState = searchDolphinJobJson.getString(Constant.RELEASE_STATE);
                if (releaseState.equalsIgnoreCase(Constant.ONLINE)) {
                    offLineDolphinJob(gatherDolphinJobEntity);
                }
                jobId = searchDolphinJobJson.getString(Constant.ID);
            }

            if (jobId == null) {
                response = FastHttpClient.
                        post().
                        addHeader("cookie", dolphinSession).
                        params(createDolphinJobJsonMap).
                        url(saveUrl).
                        build().
                        execute().string();
            } else {
                createDolphinJobJsonMap.put(Constant.ID, jobId);
                response = FastHttpClient.
                        post().
                        addHeader("cookie", dolphinSession).
                        params(createDolphinJobJsonMap).
                        url(updateUrl).
                        build().
                        execute().string();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("create or update dolphin job successfully!");

        return response;
    }


    public String offLineDolphinJob(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec offLineDolphinJob...");

        String dolphinProjectName = gatherDolphinJobEntity.getDolphinProjectName();
        String urlDolphin = gatherDolphinJobEntity.getUrlDolphin();
        String releaseUrl = String.format(Constant.RELEASE_URL, urlDolphin, dolphinProjectName);

        String dolphinSession = "";
        String response = null;
        try {
            dolphinSession = gatherSession.getDolphinSession(urlDolphin, gatherDolphinJobEntity.getUsernameDolphin(), gatherDolphinJobEntity.getPasswordDolphin());
            JSONObject jsonObject = searchDolphinJob(gatherDolphinJobEntity);
            if (jsonObject == null) {
                return null;
            }
            String jobId = jsonObject.getString(Constant.ID);
            if (jobId != null) {
                Map<String, String> params = new HashMap<>(2);
                params.put(Constant.PROCESS_ID, jobId);
                params.put(Constant.RELEASE_STATE, Constant.RELEASE_STATE_OFF_LINE);
                response = FastHttpClient.
                        post().
                        addHeader("cookie", dolphinSession).
                        params(params).
                        url(releaseUrl).
                        build().
                        execute().string();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("offline dolphin job successfully!");

        return response;
    }

    public String onlineDolphinJob(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec onlineDolphinJob...");

        String urlDolphin = gatherDolphinJobEntity.getUrlDolphin();
        String dolphinProjectName = gatherDolphinJobEntity.getDolphinProjectName();

        String jobOnLineUrl = String.format(Constant.RELEASE_URL, urlDolphin, dolphinProjectName);
        String dolphinSession = "";
        String response = null;
        try {
            dolphinSession = gatherSession.getDolphinSession(urlDolphin, gatherDolphinJobEntity.getUsernameDolphin(), gatherDolphinJobEntity.getPasswordDolphin());
            JSONObject jsonObject = searchDolphinJob(gatherDolphinJobEntity);
            if (jsonObject == null) {
                return response;
            }
            String jobId = jsonObject.getString(Constant.ID);
            Map<String, String> params = new HashMap<>(2);
            params.put(Constant.PROCESS_ID, jobId);
            params.put(Constant.RELEASE_STATE, Constant.RELEASE_STATE_ON_LINE);
            //上线job
            response = FastHttpClient.
                    post().
                    addHeader("cookie", dolphinSession).
                    params(params).
                    url(jobOnLineUrl).
                    build().
                    execute().string();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("online dolphin job successfully!");
        return response;
    }

    public String delDolphinJob(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec delDolphinJob...");

        String dolphinProjectName = gatherDolphinJobEntity.getDolphinProjectName();
        String urlDolphin = gatherDolphinJobEntity.getUrlDolphin();
        String deleteUrl = String.format(Constant.DELETE_URL, urlDolphin, dolphinProjectName);

        String dolphinSession = "";
        String response = null;
        try {
            dolphinSession = gatherSession.getDolphinSession(urlDolphin, gatherDolphinJobEntity.getUsernameDolphin(), gatherDolphinJobEntity.getPasswordDolphin());
            JSONObject jsonObject = searchDolphinJob(gatherDolphinJobEntity);
            if (jsonObject == null) {
                return null;
            }
            String jobId = jsonObject.getString(Constant.ID);
            if (jobId != null) {
                Map<String, String> params = new HashMap<>(1);
                params.put(Constant.PROCESS_DEFINITION_ID, jobId);
                response = FastHttpClient.
                        get().
                        addHeader("cookie", dolphinSession).
                        params(params).
                        url(deleteUrl).
                        build().
                        execute().string();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("del dolphin job successfully!");
        return response;
    }

    public JSONArray getDolphinJobJson(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec getDolphinJobJson...");

        String dolphinProjectName = gatherDolphinJobEntity.getDolphinProjectName();
        String urlDolphin = gatherDolphinJobEntity.getUrlDolphin();
        String exportUrl = String.format(Constant.EXPORT_URL, urlDolphin, dolphinProjectName);

        String dolphinSession = "";
        String response = "";
        try {
            dolphinSession = gatherSession.getDolphinSession(urlDolphin, gatherDolphinJobEntity.getUsernameDolphin(), gatherDolphinJobEntity.getPasswordDolphin());
            JSONObject jsonObject = searchDolphinJob(gatherDolphinJobEntity);
            if (jsonObject == null) {
                return null;
            }
            String jobId = jsonObject.getString(Constant.ID);
            if (jobId != null) {
                Map<String, String> params = new HashMap<>(1);
                params.put(Constant.PROCESS_DEFINITION_IDS, jobId);
                response = FastHttpClient.
                        get().
                        addHeader("cookie", dolphinSession).
                        params(params).
                        url(exportUrl).
                        build().
                        execute().string();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        JSONArray jsonArray = JSON.parseArray(response);
        logger.info("get dolphin job json successfully!");
        return jsonArray;
    }

    public JSONObject searchDolphinJob(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec searchDolphinJob...");

        int syncType = gatherDolphinJobEntity.getSyncType();
        String syncTypeString = "";
        //1 全量，2 增量，3 每日快照
        switch (syncType) {
            case 1:
                syncTypeString = Constant.SYNC_TYPE_FULL;
                break;
            case 2:
                syncTypeString = Constant.SYNC_TYPE_INCRE;
                break;
            case 3:
                syncTypeString = Constant.SYNC_TYPE_SNAPSHOT;
                break;
            default:
                break;
        }

        String urlDolphin = gatherDolphinJobEntity.getUrlDolphin();
        String dolphinProjectName = gatherDolphinJobEntity.getDolphinProjectName();
        String odsTableName = gatherDolphinJobEntity.getDatabaseNameInput() + Constant.UNDERLINE + gatherDolphinJobEntity.getTableNameInput() + Constant.UNDERLINE + syncTypeString + Constant.UNDERLINE + gatherProperties.getHiveOdsTableLastFix();
        //replace "-" to "_"
        odsTableName = odsTableName.replaceAll(Constant.MIDLINE, Constant.UNDERLINE);
        String name = odsTableName + Constant.UNDERLINE + gatherDolphinJobEntity.getGatherJobId();

        String searchUrl = String.format(Constant.SEARCH_URL, urlDolphin, dolphinProjectName, name);
        String dolphinSession = "";
        String response = null;
        JSONObject jsonObject = null;
        try {
            dolphinSession = gatherSession.getDolphinSession(urlDolphin, gatherDolphinJobEntity.getUsernameDolphin(), gatherDolphinJobEntity.getPasswordDolphin());
            response = FastHttpClient.
                    get().
                    addHeader("cookie", dolphinSession).
                    url(searchUrl).
                    build().
                    execute().string();
            if (JSONObject.parseObject(response).getInteger(Constant.CODE) == 0) {
                JSONArray jsonArray = JSON.parseObject(response).getJSONObject("data").getJSONArray("totalList");
                if (jsonArray.size() > 0) {
                    jsonObject = jsonArray.getJSONObject(0);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("search dolphin job json successfully!");
        return jsonObject;
    }


    public String createOrUpdateScheduler(String jobId, GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec createOrUpdateScheduler...");

        Map<String, String> params = new HashMap<>(8);
        String dolphinProjectName = gatherDolphinJobEntity.getDolphinProjectName();
        String urlDolphin = gatherDolphinJobEntity.getUrlDolphin();

        String createSchedulerUrl = String.format(Constant.SCHEDULER_CREATE_URL, urlDolphin, dolphinProjectName);
        String updateSchedulerUrl = String.format(Constant.SCHEDULER_UPDATE_URL, urlDolphin, dolphinProjectName);

        String schedule = String.format(gatherProperties.getDolphinSchedule(), gatherDolphinJobEntity.getCrontab());
        params.put(Constant.SCHEDULE, schedule);
        params.put(Constant.FAILURE_STRATEGY, gatherProperties.getDolphinFailureStrategy());
        params.put(Constant.PROCESS_INSTANCE_PRIORITY, gatherProperties.getDolphinProcessInstancePriority());
        params.put(Constant.WARNING_GROUP_ID, gatherProperties.getDolphinWarningGroupId());
        params.put(Constant.RECEIVERS, gatherProperties.getDolphinReceivers());
        params.put(Constant.RECEIVERS_CC, gatherProperties.getDolphinReceiversCc());
        params.put(Constant.WORKER_GROUP, gatherProperties.getDolphinWarningGroupId());


        String dolphinSession = "";
        String schedulerResponse = "";
        String schedulerId = null;
        try {
            dolphinSession = gatherSession.getDolphinSession(urlDolphin, gatherDolphinJobEntity.getUsernameDolphin(), gatherDolphinJobEntity.getPasswordDolphin());

            JSONObject searchDolphinJobJson = searchDolphinJob(gatherDolphinJobEntity);
            if (searchDolphinJobJson != null) {
                String releaseState = searchDolphinJobJson.getString(Constant.RELEASE_STATE);
                String scheduleReleaseState = searchDolphinJobJson.getString(Constant.RELEASE_STATE);
                if (releaseState.equalsIgnoreCase(Constant.OFFLINE)) {
                    onlineDolphinJob(gatherDolphinJobEntity);
                }
                if (scheduleReleaseState.equalsIgnoreCase(Constant.ONLINE)) {
                    JSONObject schedulerJson = getSchedulerJson(jobId, gatherDolphinJobEntity);
                    if (schedulerJson != null) {
                        schedulerId = schedulerJson.getString(Constant.ID);
                        offLineScheduler(schedulerId, gatherDolphinJobEntity);
                    }
                }

            }

            if (schedulerId == null) {
                params.put(Constant.PROCESS_DEFINITION_ID, jobId);
                schedulerResponse = FastHttpClient.
                        post().
                        addHeader("cookie", dolphinSession).
                        params(params).
                        url(createSchedulerUrl).
                        build().
                        execute().string();

            } else {
                params.put(Constant.ID, schedulerId);
                schedulerResponse = FastHttpClient.
                        post().
                        addHeader("cookie", dolphinSession).
                        params(params).
                        url(updateSchedulerUrl).
                        build().
                        execute().string();
            }

        } catch (IOException ioException) {
            logger.error(ioException.getMessage(), ioException);
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }
        logger.info("create or update scheduler successfully!");
        return schedulerResponse;

    }

    public String onLineScheduler(String schedulerId, GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec onLineScheduler...");

        String dolphinProjectName = gatherDolphinJobEntity.getDolphinProjectName();
        String urlDolphin = gatherDolphinJobEntity.getUrlDolphin();

        String schedulerOnLineUrl = String.format(Constant.SCHEDULER_ON_LINE_URL, urlDolphin, dolphinProjectName);


        String dolphinSession = "";
        String onLineSchedulerResponse = "";
        try {
            dolphinSession = gatherSession.getDolphinSession(urlDolphin, gatherDolphinJobEntity.getUsernameDolphin(), gatherDolphinJobEntity.getPasswordDolphin());
            Map<String, String> schedulerOnLineParams = new HashMap<>(1);
            if (schedulerId != null) {
                schedulerOnLineParams.put(Constant.ID, schedulerId);
                onLineSchedulerResponse = FastHttpClient.
                        post().
                        addHeader("cookie", dolphinSession).
                        params(schedulerOnLineParams).
                        url(schedulerOnLineUrl).
                        build().
                        execute().string();

                return onLineSchedulerResponse;
            }


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("online scheduler successfully!");
        return onLineSchedulerResponse;

    }

    public String offLineScheduler(String schedulerId, GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec offLineScheduler...");

        String dolphinProjectName = gatherDolphinJobEntity.getDolphinProjectName();
        String urlDolphin = gatherDolphinJobEntity.getUrlDolphin();

        String schedulerOnLineUrl = String.format(Constant.SCHEDULER_OFF_LINE_URL, urlDolphin, dolphinProjectName);


        String dolphinSession = "";
        String onLineSchedulerResponse = "";
        try {
            dolphinSession = gatherSession.getDolphinSession(urlDolphin, gatherDolphinJobEntity.getUsernameDolphin(), gatherDolphinJobEntity.getPasswordDolphin());
            Map<String, String> schedulerOnLineParams = new HashMap<>(1);
            if (schedulerId != null) {
                schedulerOnLineParams.put(Constant.ID, schedulerId);
                onLineSchedulerResponse = FastHttpClient.
                        post().
                        addHeader("cookie", dolphinSession).
                        params(schedulerOnLineParams).
                        url(schedulerOnLineUrl).
                        build().
                        execute().string();

                return onLineSchedulerResponse;
            }


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("offline scheduler successfully!");
        return onLineSchedulerResponse;

    }


    public String createJobByJson(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec createJobByJson...");

        String dolphinProjectName = gatherDolphinJobEntity.getDolphinProjectName();
        String urlDolphin = gatherDolphinJobEntity.getUrlDolphin();

        String importDefinitionUrl = String.format(Constant.IMPORT_DEFINITION_URL, urlDolphin);


        String dolphinSession = "";
        String response = "";
        String gatherJobInfo = gatherDolphinJobEntity.getGatherJobInfo();

        try {
            dolphinSession = gatherSession.getDolphinSession(urlDolphin, gatherDolphinJobEntity.getUsernameDolphin(), gatherDolphinJobEntity.getPasswordDolphin());
            Map<String, String> params = new HashMap<>(1);
            params.put(Constant.PROJECT_NAME, dolphinProjectName);
            byte[] imageContent = gatherJobInfo.getBytes("UTF-8");
            response = FastHttpClient.newBuilder().
                    connectTimeout(10, TimeUnit.SECONDS).
                    build().
                    post().
                    addHeader("cookie", dolphinSession).
                    url(importDefinitionUrl).
                    params(params).
                    addFile("file", dolphinProjectName, imageContent).
                    build().
                    execute().string();

            return response;


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("create job by json successfully!");
        return response;

    }


    public JSONObject getSchedulerJson(String jobId, GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec getSchedulerJson...");

        JSONObject jsonObject = null;
        String dolphinProjectName = gatherDolphinJobEntity.getDolphinProjectName();
        String urlDolphin = gatherDolphinJobEntity.getUrlDolphin();
        String searchScheduleUrl = String.format(Constant.SEARCH_SCHEDULE_URL, urlDolphin, dolphinProjectName, jobId);

        try {
            String dolphinSession = gatherSession.getDolphinSession(urlDolphin, gatherDolphinJobEntity.getUsernameDolphin(), gatherDolphinJobEntity.getPasswordDolphin());
            String searchSchedulerResponse = FastHttpClient.
                    get().
                    addHeader("cookie", dolphinSession).
                    url(searchScheduleUrl).
                    build().
                    execute().string();
            JSONArray jsonArray = JSON.parseObject(searchSchedulerResponse).getJSONObject("data").getJSONArray("totalList");
            if (jsonArray.size() > 0) {
                jsonObject = jsonArray.getJSONObject(0);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("get job json successfully!");
        return jsonObject;
    }


}
