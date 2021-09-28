package com.pd.pdp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class DataSourcesInfo implements Serializable {

    private static final long serialVersionUID = -2126718447287158588L;

    @Id
    @Column(name = "id")
    private int id;
    private String dataSourceContext;
    private String dataSourceType;
    private String driver;
    private String url;
    private String username;
    private String password;
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
