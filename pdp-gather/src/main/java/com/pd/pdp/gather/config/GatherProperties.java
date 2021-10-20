package com.pd.pdp.gather.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/13
 */

@Component
@PropertySource(value = "gather.properties")
@Data
public class GatherProperties {

    @Value("${hive.stg.table.last.fix}")
    private String hiveStgTableLastFix;

    @Value("${hive.stg.table.type}")
    private String hiveStgTableType;

    @Value("${hive.stg.table.path}")
    private String hiveStgTablePath;

    @Value("${hive.stg.row.format}")
    private String hiveStgRowFormat;

    @Value("${hive.stg.file.type}")
    private String hiveStgFileType;

    @Value("${hive.stg.add.col}")
    private String hiveStgAddCol;

    @Value("${hive.stg.add.col.value}")
    private String hiveStgAddColValue;

    @Value("${hive.stg.compression.type}")
    private String hiveStgCompressionType;

    @Value("${hive.ods.table.last.fix}")
    private String hiveOdsTableLastFix;

    @Value("${hive.ods.table.type}")
    private String hiveOdsTableType;

    @Value("${hive.ods.table.path}")
    private String hiveOdsTablePath;

    @Value("${hive.ods.row.format}")
    private String hiveOdsRowFormat;

    @Value("${hive.ods.file.type}")
    private String hiveOdsFileType;

    @Value("${hive.ods.compression.type}")
    private String hiveOdsCompressionType;


    @Value("${hive.ods.add.col}")
    private String hiveOdsAddCol;


    @Value("${hive.ods.partition.col}")
    private String hiveOdsPartitionCol;

    @Value("${hive.stg.increment.data.func}")
    private String hiveStgIncrementDataFunc;


    @Value("${dolphin.datasource.name}")
    private String dolphinDatasourceName;
    @Value("${dolphin.datasource.id}")
    private String dolphinDatasourceId;
    @Value("${dolphin.tenant}")
    private String dolphinTenant;

    @Value("${dolphin.schedule}")
    private String dolphinSchedule;
    @Value("${dolphin.failureStrategy}")
    private String dolphinFailureStrategy;
    @Value("${dolphin.warningType}")
    private String dolphinWarningType;
    @Value("${dolphin.processInstancePriority}")
    private String dolphinProcessInstancePriority;
    @Value("${dolphin.warningGroupId}")
    private String dolphinWarningGroupId;
    @Value("${dolphin.receivers}")
    private String dolphinReceivers;
    @Value("${dolphin.receiversCc}")
    private String dolphinReceiversCc;
    @Value("${dolphin.workerGroup}")
    private String dolphinWorkerGroup;
    @Value("${dolphin.stg.to.ods.sql.increment}")
    private String dolphinStgToOdsSqlIncrement;
    @Value("${dolphin.stg.to.ods.sql.snapshot}")
    private String dolphinStgToOdsSqlSnapshot;
    @Value("${dolphin.stg.to.ods.sql.full}")
    private String dolphinStgToOdsSqlFull;
    @Value("${dolphin.ods.invalidate.metadata.sql}")
    private String dolphinOdsInvalidateMetadataSql;

    @Value("${dolphin.check.data.shell}")
    private String dolphinCheckDataShell;
    @Value("${dolphin.check.full.input.query.sql}")
    private String dolphinCheckFullInputQuerySql;
    @Value("${dolphin.check.increment.input.query.sql}")
    private String dolphinCheckIncrementInputQuerySql;
    @Value("${dolphin.check.full.output.query.sql}")
    private String dolphinCheckFullOutputQuerySql;
    @Value("${dolphin.check.increment.output.query.sql}")
    private String dolphinCheckIncrementOutputQuerySql;


}
