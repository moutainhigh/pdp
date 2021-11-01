package com.pd.pdp.gather.constant;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/11
 */
public interface Constant {
    String SHOW_DATABASES = "show databases";
    String SHOW_TABLES = "show tables from `%s`";
    String DOLPHIN_LOGIN_URL = "%s/dolphinscheduler/login";
    String DOLPHIN_TEST_CONN_URL = "%s/dolphinscheduler/ui/";
    String DOLPHIN_ALL_PROJECT_URL = "%s/dolphinscheduler/projects/list-paging?pageSize=1000&pageNo=1&searchVal=";
    String SESSION_ID = "sessionId";
    String CODE = "code";

    //mysql data info

    // String SHOW_COLUMNS = "select column_name,column_comment,data_type from information_schema.columns where table_name='%s' and table_schema='%s'";
    String SHOW_COLUMNS = "show full columns from `%s`.`%s`";
    String TABLE_COMMENT = "SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES  WHERE TABLE_NAME='%s'";
    String TABLE_COMMENT_KEY = "TABLE_COMMENT";
    String UNDERLINE = "_";
    String BACKSLASH = "/";
    String QUOTE = "\"";
    String COMMA = ",";
    String COLON = ":";
    String QUESTION = "?";
    String ENTER = "\n";
    String SPACE = " ";
    String BACK_QUOTE = "`";
    String COMMENT = "COMMENT";
    String LOCATION = "LOCATION";
    String HIVE_FILE_TEXT_TYPE = "TEXTFILE";
    String HIVE_EXTERNAL = "external";
    String TBL_PROPERTIES = "\n tblproperties(\"%s.compress\"=\"%s\")";
    String HIVE_PARTITION_INFO = "PARTITIONED BY (%s)";
    String CREATE_HIVE_TABLE_SQL ="create %s table  IF NOT EXISTS %s.%s (\n%s\n) %s\n%s\n ROW FORMAT DELIMITED FIELDS TERMINATED BY %s \n STORED AS %s\n%s";

    String STG = "dc_stg";
    String ODS = "dc_ods";
    String  COL_META_NAME ="COLUMN_NAME";
    String  COL_META_TYPE="COLUMN_TYPE";
    String  COL_META_COMMENT="COLUMN_COMMENT";

    String  DATAX_JSON_FILE_NAME="datax.json";
    String  GATHER_DOLPHIN_FILE_NAME="gather_dolphin.json";

    String STG_TO_ODS_SQL_OF_INCRE = "stgToOdsSqlOfIncre";
    String STG_TO_ODS_SQL_OF_SNAPSHOT = "stgToOdsSqlOfSnapshot";
    String STG_TO_ODS_SQL_OF_FULL = "stgToOdsSqlOfFull";
    String STG_TABLE_NAME = "$stgTableName";
    String ODS_TABLE_NAME = "$odsTableName";
    String ODS_COLS = "$odsCols";
    String STG_T_COLS = "$stg_tCols";
    String STG_COLS = "$stgCols";
    String DATA_CHECK_COL = "$checkCol";
    String DATAX_JSON = "$dataxJson";
    String STG_TO_ODS_SQL = "$stgToOdsSql";
    String PARTITION_COL_NAME = "$partition_col_name";
    String TENANT_ID_NAME = "tenantId";
    String DATASOURCE_NAME = "datasourceName";
    String DATASOURCE = "datasource";
    String TASKS = "tasks";
    String PARAMS = "params";
    String SQL = "sql";
    String JSON = "json";
    String RAW_SCRIPT = "rawScript";
    String TRUNCATE_TABLE = "truncate table %s.%s";

    String SYSTEM_NAME = "systemName";
    String DATABASE_NAME_INPUT = "databaseNameInput";
    String TABLE_NAME_INPUT = "tableNameInput";
    String IP_INPUT = "ipInput";
    String PORT_INPUT = "portInput";
    String USERNAME_INPUT = "usernameInput";
    String PASSWORD_INPUT = "passwordInput";
    String URL_OUTPUT = "urlOutput";
    String DATABASE_NAME_OUTPUT = "databaseNameOutput";
    String TABLE_NAME_OUTPUT = "tableNameOutput";
    String USERNAME_OUTPUT = "usernameOutput";
    String PASSWORD_OUTPUT = "passwordOutput";
    String GATHER_JOB_ID = "gatherJobId";
    String SYNC_TYPE = "syncType";
    String MYSQL_QUERY = "mysqlQuery";
    String HIVE_QUERY = "hiveQuery";

    // String STG_TABLE_NAME = "$stgTableName";
    // String ODS_TABLE_NAME = "$odsTableName";
    // String ODS_COLS = "$odsCols";
    // String STG_T_COLS = "$stg_tCols";
    // String STG_COLS = "$stgCols";
    // String DATAX_JSON = "$dataxJson";
    // String STG_TO_ODS_SQL = "$stgToOdsSql";
    // String PARTITION_COL_NAME = "$partition_col_name";
    // String DATASOURCE_NAME = "$datasourceName";
    // String TENANT_ID_NAME = "$tenantId";

    //同步方式，1全量/2增量/3每日快照
    String SYNC_TYPE_FULL = "full";
    String SYNC_TYPE_SNAPSHOT = "snapshot";
    String SYNC_TYPE_INCRE = "increment";

    String CONNECTIONS = "connects";
    String PROCESS_DEFINITION_JSON = "processDefinitionJson";
    String LOCATIONS = "locations";
    String PROJECT_NAME = "projectName";
    String NAME = "name";
    String DESCRIPTION = "description";

    String SAVE_URL = "%s/dolphinscheduler/projects/%s/process/save";
    String IMPORT_DEFINITION_URL = "%s/dolphinscheduler/projects/import-definition";
    String UPDATE_URL = "%s/dolphinscheduler/projects/%s/process/update";
    String RELEASE_URL = "%s/dolphinscheduler/projects/%s/process/release";
    String DELETE_URL = "%s/dolphinscheduler/projects/%s/process/delete";
    String EXPORT_URL = "%s/dolphinscheduler/projects/%s/process/export";
    String SEARCH_URL = "%s/dolphinscheduler/projects/%s/process/list-paging?pageSize=10&pageNo=1&searchVal=%s";
    String SEARCH_SCHEDULE_URL = "%s/dolphinscheduler/projects/%s/schedule/list-paging?processDefinitionId=%s&pageSize=10&pageNo=1&searchVal=";
    String SCHEDULER_CREATE_URL = "%s/dolphinscheduler/projects/%s/schedule/create";
    String SCHEDULER_UPDATE_URL = "%s/dolphinscheduler/projects/%s/schedule/update";
    String SCHEDULER_ON_LINE_URL = "%s/dolphinscheduler/projects/%s/schedule/online";
    String SCHEDULER_OFF_LINE_URL = "%s/dolphinscheduler/projects/%s/schedule/offline";
    String DOLPHIN_URL = "%s/dolphinscheduler/ui/#/projects/list";
    String JOB_DELETE_URL = "%s/dolphinscheduler/projects/%s/process/delete?processDefinitionId=%s";

    String PROCESS_ID = "processId";
    String RELEASE_STATE = "releaseState";
    String SCHEDULE_RELEASE_STATE = "scheduleReleaseState";
    String RELEASE_STATE_ON_LINE = "1";
    String RELEASE_STATE_OFF_LINE = "0";
    String ONLINE = "ONLINE";
    String OFFLINE = "OFFLINE";

    //scheduler
    String SCHEDULE = "schedule";
    String FAILURE_STRATEGY = "failureStrategy";
    String WARNING_TYPE = "warningType";
    String PROCESS_INSTANCE_PRIORITY = "processInstancePriority";
    String WARNING_GROUP_ID = "warningGroupId";
    String RECEIVERS = "receivers";
    String RECEIVERS_CC = "receiversCc";
    String WORKER_GROUP = "workerGroup";
    String PROCESS_DEFINITION_ID = "processDefinitionId";
    String PROCESS_DEFINITION_IDS = "processDefinitionIds";

    String ID = "id";



    String MYSQL_BIGINT = "bigint";
    String MYSQL_INT = "int";
    String MYSQL_SMALLINT = "smallint";
    String MYSQL_TINYINT = "tinyint";
    String MYSQL_MEDIUMINT = "mediumint";
    String MYSQL_DECIMAL = "decimal";
    String MYSQL_DECIMALDIGITS = "decimaldigits";
    String MYSQL_DOUBLE = "double";
    String MYSQL_FLOAT = "float";
    String MYSQL_BINARY = "binary";
    String MYSQL_VARBINARY = "varbinary";
    String MYSQL_CHAR = "char";
    String MYSQL_VARCHAR = "varchar";
    String MYSQL_MEDIUMTEXT = "mediumtext";
    String MYSQL_TEXT = "text";
    String MYSQL_DATETIME = "datetime";
    String MYSQL_TIME = "time";
    String MYSQL_TIMESTAMP = "timestamp";
    String MYSQL_DATE = "date";
    String MYSQL_JSON = "json";
    String MYSQL_BLOB = "blob";
    String MYSQL_MEDIUMBLOB = "mediumblob";
    String MYSQL_LONGBLOB = "longblob";
    String MYSQL_BIT = "bit";
    String HIVE_BOOLEAN = "boolean";
    String HIVE_TINYINT = "tinyint";
    String HIVE_SMALLINT = "smallint";
    String HIVE_INT = "int";
    String HIVE_BIGINT = "bigint";
    String HIVE_FLOAT = "float";
    String HIVE_DOUBLE = "double";
    String HIVE_DECIMAL = "double";
    String HIVE_DECIMAL_ORIGIN = "decimal";
    String HIVE_STRING = "string";
    String HIVE_VARCHAR = "varchar";
    String HIVE_CHAR = "char";
    String HIVE_BINARY = "string";
    String HIVE_BINARY_ORIGIN = "binary";
    String HIVE_TIMESTAMP = "timestamp";
    String HIVE_DATE = "date";
    String HIVE_ARRAY = "string";
    String HIVE_ARRAY_ORIGIN = "array";
    String HIVE_STRUCT = "string";
    String HIVE_STRUCT_ORIGIN = "struct";
    String HIVE_UNION = "string";
    String HIVE_UNION_ORIGIN = "union";
    String HIVE_MAP = "string";
    String HIVE_MAP_ORIGIN = "map";

}
