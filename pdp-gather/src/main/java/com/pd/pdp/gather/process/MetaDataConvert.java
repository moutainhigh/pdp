package com.pd.pdp.gather.process;

import com.alibaba.fastjson.JSON;
import com.pd.pdp.gather.config.GatherProperties;
import com.pd.pdp.gather.constant.Constant;
import com.pd.pdp.gather.entity.GatherDolphinJobEntity;
import com.pd.pdp.gather.utils.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/13
 */

@Service
public class MetaDataConvert {
    private Logger logger = LoggerFactory.getLogger(MetaDataConvert.class);

    @Autowired
    GatherProperties gatherProperties;

    public HashMap<String, String> createHiveTableMeta(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec createHiveTableMeta...");

        HashMap<String, String> map = new HashMap<String, String>();
        //stg
        String stgSql = getCreateHiveTableSql(Constant.STG, gatherDolphinJobEntity);
        //ods
        String odsSql = getCreateHiveTableSql(Constant.ODS, gatherDolphinJobEntity);

        map.put(Constant.STG, stgSql);
        map.put(Constant.ODS, odsSql);

        logger.info("createHiveTableMeta:\n{}", JSON.toJSONString(map));
        return map;

    }

    private String getCreateHiveTableSql(String whichDB, GatherDolphinJobEntity gatherDolphinJobEntity) {
        String external = "";
        String location = "";
        String tableName = "";
        String partitionInfo = "";
        String hiveFileType = "";
        String hiveTablePath = "";
        String hiveRowFormat = "";

        String db = gatherDolphinJobEntity.getDatabaseNameInput();
        String tableNameInput = gatherDolphinJobEntity.getTableNameInput();

        List<Map<String, String>> columnsOfInputTable = gatherDolphinJobEntity.getColumnsOfInputTable();
        //示例
        //`id` int COMMENT"主键",
        // `data_source_context` varchar COMMENT"数据源说明"
        String colsString = getColsString(columnsOfInputTable);

        int syncType = gatherDolphinJobEntity.getSyncType();
        String hiveOdsPartitionCol = gatherProperties.getHiveOdsPartitionCol();
        String hiveOdsPartitionColMonth = gatherProperties.getHiveOdsPartitionColMonth();
        String tableCommentOfInputTable = gatherDolphinJobEntity.getTableCommentOfInputTable();
        String syncTypeString = "";

        //1 全量，2 增量，3 每日快照
        switch (syncType) {
            case 2:
                if (!hiveOdsPartitionColMonth.trim().isEmpty()) {
                    partitionInfo = String.format(Constant.HIVE_PARTITION_INFO, hiveOdsPartitionColMonth);
                }
                syncTypeString = Constant.SYNC_TYPE_INCRE;
                break;
            case 3:
                if (!hiveOdsPartitionCol.trim().isEmpty()) {
                    partitionInfo = String.format(Constant.HIVE_PARTITION_INFO, hiveOdsPartitionCol);
                }
                syncTypeString = Constant.SYNC_TYPE_SNAPSHOT;
                break;
            default:
                syncTypeString = Constant.SYNC_TYPE_FULL;
                break;
        }

        switch (whichDB) {
            case Constant.STG:
                String hiveStgTableLastFix = gatherProperties.getHiveStgTableLastFix();
                tableName = gatherDolphinJobEntity.getSystemName() + Constant.UNDERLINE + db + Constant.UNDERLINE + tableNameInput + Constant.UNDERLINE + hiveStgTableLastFix;
                String hiveStgTableType = gatherProperties.getHiveStgTableType();
                String hiveStgCompressionType = gatherProperties.getHiveStgCompressionType();
                String hiveStgTablePath = gatherProperties.getHiveStgTablePath();
                String hiveStgRowFormat = gatherProperties.getHiveStgRowFormat();
                String hiveStgFileType = gatherProperties.getHiveStgFileType();
                if (!hiveStgTablePath.endsWith(Constant.BACKSLASH)) {
                    hiveTablePath = hiveStgTablePath + Constant.BACKSLASH;
                }

                if (hiveStgTableType.equalsIgnoreCase(Constant.HIVE_EXTERNAL)) {
                    external = hiveStgTableType;
                    location = Constant.LOCATION + Constant.SPACE + hiveTablePath;
                }

                if (!hiveStgCompressionType.trim().isEmpty() && !hiveStgCompressionType.trim().equalsIgnoreCase(Constant.HIVE_FILE_TEXT_TYPE)) {
                    hiveStgFileType += String.format(Constant.TBL_PROPERTIES, hiveStgFileType.toLowerCase(), hiveStgCompressionType);
                }
                db = Constant.STG;
                hiveRowFormat = hiveStgRowFormat;
                hiveFileType = hiveStgFileType;
                colsString += gatherProperties.getHiveStgAddCol();
                partitionInfo = "";

                break;
            case Constant.ODS:
                String hiveOdsTableLastFix = gatherProperties.getHiveOdsTableLastFix();
                tableName = gatherDolphinJobEntity.getSystemName() + Constant.UNDERLINE + db + Constant.UNDERLINE + tableNameInput + Constant.UNDERLINE + syncTypeString + Constant.UNDERLINE + hiveOdsTableLastFix;
                String hiveOdsTableType = gatherProperties.getHiveOdsTableType();
                String hiveOdsCompressionType = gatherProperties.getHiveOdsCompressionType();
                String hiveOdsTablePath = gatherProperties.getHiveOdsTablePath();
                String hiveOdsRowFormat = gatherProperties.getHiveOdsRowFormat();
                String hiveOdsFileType = gatherProperties.getHiveOdsFileType();
                if (!hiveOdsTablePath.endsWith(Constant.BACKSLASH)) {
                    hiveTablePath = hiveOdsTablePath + Constant.BACKSLASH;
                }

                if (hiveOdsTableType.equalsIgnoreCase(Constant.HIVE_EXTERNAL)) {
                    external = hiveOdsTableType;
                    location = Constant.LOCATION + Constant.SPACE + hiveTablePath;
                }

                if (!hiveOdsCompressionType.trim().isEmpty() && !hiveOdsCompressionType.equalsIgnoreCase(Constant.HIVE_FILE_TEXT_TYPE)) {
                    hiveOdsFileType += String.format(Constant.TBL_PROPERTIES, hiveOdsFileType.toLowerCase(), hiveOdsCompressionType);
                }
                db = Constant.ODS;
                hiveRowFormat = hiveOdsRowFormat;
                hiveFileType = hiveOdsFileType;
                colsString += gatherProperties.getHiveOdsAddCol();
                break;
            default:
                break;

        }


        if (!tableCommentOfInputTable.isEmpty()) {
            tableCommentOfInputTable = Constant.COMMENT + Constant.SPACE + Constant.QUOTE + tableCommentOfInputTable + Constant.QUOTE;
        }
        //create %s table  IF NOT EXISTS %s.%s (
        // %s
        // ) %s
        // %s
        //  ROW FORMAT DELIMITED FIELDS TERMINATED BY %s
        //  STORED AS %s
        // %s
        String sql = String.format(Constant.CREATE_HIVE_TABLE_SQL, external, db, tableName, colsString, tableCommentOfInputTable, partitionInfo, hiveRowFormat, hiveFileType, location);

        return sql;
    }

    private String getColsString(List<Map<String, String>> columnsOfInputTable) {

        String colsString = "";
        String hiveColType = "";
        for (int i = 0; i < columnsOfInputTable.size(); i++) {
            //[{COLUMN_NAME=Host, DATA_TYPE=char, COLUMN_COMMENT=}]
            Map<String, String> colInfoMap = columnsOfInputTable.get(i);
            String columnName = colInfoMap.get(Constant.COL_META_NAME);
            String dataType = colInfoMap.get(Constant.COL_META_TYPE);
            String columnComment = colInfoMap.get(Constant.COL_META_COMMENT);

            if (dataType.startsWith(Constant.MYSQL_INT) || dataType.startsWith(Constant.MYSQL_SMALLINT) || dataType.startsWith(Constant.MYSQL_MEDIUMINT)) {
                hiveColType = Constant.HIVE_INT;
            } else if (dataType.contains(Constant.MYSQL_VARCHAR) || dataType.contains(Constant.MYSQL_CHAR)) {
                hiveColType = Constant.HIVE_STRING;
            } else if (dataType.startsWith(Constant.MYSQL_TINYINT)) {
                hiveColType = Constant.HIVE_TINYINT;
            } else if (dataType.startsWith(Constant.MYSQL_DOUBLE)) {
                hiveColType = dataType;
            } else if (dataType.startsWith(Constant.MYSQL_DECIMAL)) {
                hiveColType = Constant.HIVE_STRING;
                ;
            } else if (dataType.startsWith(Constant.MYSQL_FLOAT)) {
                hiveColType = dataType;
            } else {
                hiveColType = Constant.HIVE_STRING;
            }
            //colName + colType
            colsString += Constant.BACK_QUOTE + columnName + Constant.BACK_QUOTE + Constant.SPACE + hiveColType;

            //colName + colType + comment
            if (!columnComment.isEmpty()) {
                colsString += Constant.SPACE + Constant.COMMENT + Constant.SPACE + Constant.QUOTE + columnComment + Constant.QUOTE + Constant.COMMA + Constant.ENTER;
            } else {
                colsString += Constant.COMMA + Constant.ENTER;
            }
        }
        return colsString;

    }

    public String writeDataXJson(GatherDolphinJobEntity gatherDolphinJobEntity) {
        logger.info("start exec writeDataXJson...");

//        String path = MetaDataConvert.class.getClassLoader().getResource(Constant.DATAX_JSON_FILE_NAME).getPath();
        ClassPathResource classPathResource = new ClassPathResource(Constant.DATAX_JSON_FILE_NAME);
        StringBuilder jsonStringBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()));) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonStringBuilder.append(line + Constant.ENTER);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        String db = gatherDolphinJobEntity.getDatabaseNameInput();
        String tableName = gatherDolphinJobEntity.getTableNameInput();

        //TODO 用json格式化处理...
        String json = jsonStringBuilder.toString();
        json = json.replace("$username", gatherDolphinJobEntity.getUsernameInput());
        json = json.replace("$password", Base64Util.getFromBase64(gatherDolphinJobEntity.getPasswordInput()));
        json = json.replace("$db", gatherDolphinJobEntity.getDatabaseNameInput());
        json = json.replace("$table", tableName);
        json = json.replace("$jdbcUrl", gatherDolphinJobEntity.getUrlInput());
        String fileType = gatherProperties.getHiveStgFileType();
        if (fileType.toLowerCase().contains("text")) {
            fileType = "text";
        }
        json = json.replace("$fileType", fileType);

        String fileName = gatherDolphinJobEntity.getSystemName() + Constant.UNDERLINE + db + Constant.UNDERLINE + tableName + Constant.UNDERLINE + gatherProperties.getHiveStgTableLastFix();
        json = json.replace("$fileName", fileName);

        json = json.replace("$path", gatherProperties.getHiveStgTablePath() + Constant.BACKSLASH + fileName.toLowerCase());

        String column = "";


        String mysqlCol = "";
        String colsString = getColsString(gatherDolphinJobEntity.getColumnsOfInputTable());
        String[] cols = colsString.split(Constant.COMMA + Constant.ENTER);

        for (String col : cols) {
            String[] colInfos = col.split(Constant.SPACE);
            String colName = colInfos[0].replaceAll(Constant.BACK_QUOTE, "");
            String colType = colInfos[1];

            column += "              {\n               \"name\": \"" + colName + "\",\n" +
                    "               \"type\": \"" + colType + "\"\n              },\n";
            mysqlCol += Constant.BACK_QUOTE + colName + Constant.BACK_QUOTE + Constant.COMMA + Constant.SPACE;

        }

        mysqlCol += gatherProperties.getHiveStgAddColValue();
        String[] stgAddColInfo = gatherProperties.getHiveStgAddCol().split(Constant.SPACE);

        //stg add col
        String addColName = stgAddColInfo[0].replace(Constant.BACK_QUOTE, "");
        String addColType = stgAddColInfo[1];
        column += "              {\n               \"name\": \"" + addColName + "\",\n" +
                "               \"type\": \"" + addColType + "\"\n              }";

        json = json.replace("\"$column\"", column);

        json = json.replace("$compress", gatherProperties.getHiveStgCompressionType());

        json = json.replace("$col", mysqlCol);

        switch (gatherDolphinJobEntity.getSyncType()) {
            case 2:
                json = json.replace("$where", gatherProperties.getHiveStgIncrementDataFunc());
                break;
            default:
                json = json.replace("$where", " where 1 = 1 ");
                //sqlserver特殊处理
                if (gatherDolphinJobEntity.getUrlInput().contains(Constant.SQL_SERVER)) {
                    String hiveStgAddColValue = gatherProperties.getHiveStgAddColValue();
                    json = json.replace("mysqlreader", "sqlserverreader").replace("`", "")
                            .replace(gatherDolphinJobEntity.getDatabaseNameInput() + ".", "")
                            .replace(hiveStgAddColValue.substring(0, hiveStgAddColValue.lastIndexOf("as")), "CONVERT(varchar(100), GETDATE(), 120)");
                }
                break;
        }

        logger.info("datax json:\n{}", json);
        return json;

    }
}
