package com.pd.pdp.server.entity;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class ProcessInstanceInfo implements Serializable {

    private static final long serialVersionUID = -2126718447287158588L;

    @Id
    @Column(name = "id")
    private int id;
    private String dsId;
    private String processName;
    private String state;
    private String projectName;
    private String runType;
    private String schedulingTime;
    private String startTime;
    private String endTime;
    private String duration;
    private String runTimes;
    private String executor;
    private String host;
}
