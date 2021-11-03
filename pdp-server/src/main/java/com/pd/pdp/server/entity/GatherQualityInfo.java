package com.pd.pdp.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class GatherQualityInfo implements Serializable {

    private static final long serialVersionUID = -2126718447287158588L;

    @Id
    @Column(name = "id")
    private int id;
    private String gatherJobId;
    private String rulesId;
    private String checkContext;
    private int datasourceInputId;
    private String systemName;
    private String sourceDb;
    private String sourceTable;
    private int datasourceOutputId;
    private Double countErrorRange;
    private int createUserId;
    private int updateUserId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Column(updatable = false)
    private Timestamp createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp updateTime;


}
