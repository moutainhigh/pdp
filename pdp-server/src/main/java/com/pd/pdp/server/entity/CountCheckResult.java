    package com.pd.pdp.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CountCheckResult implements Serializable {

    private static final long serialVersionUID = -2126718447287158588L;

    @Id
    @Column(name = "id")
    private int id;
    private String jobId;
    private String rulesId;
    private String systemName;
    private String dbName;
    private String tableName;
    private int sourceCount;
    private int sinkCount;
    private boolean result;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Column(updatable = false)
    private Timestamp createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp updateTime;


}
