package com.pd.pdp.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/9
 */

@Data
public class GatherDolphinInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private int id;
    private String gatherJobId;
    private String gatherContext;
    private int datasourceInput;
    private String databaseNameInput;
    private String tableName;
    private int datasourceOutput;
    private int syncType;
    private int datasourceDolphin;
    private String dolphinProjectName;
    private boolean createHiveTable;
    private boolean isOnline;
    private String crontab;
    private String gatherJobInfo;
    private int createUserId;
    private int updateUserId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(updatable = false)
    private Timestamp createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;

}
