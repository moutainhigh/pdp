package com.pd.pdp.server.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProcessInstanceRunStatusStatistics implements Serializable {
    private static final long serialVersionUID = -2126718447287158588L;

    private String schedulingTime;
    private String state;
    private int count;
    private int start_unix_time;
    private int end_unix_time;

    public int getStartUnixTime(){
        return this.start_unix_time;
    }

    public int getEndUnixTime(){
        return this.end_unix_time;
    }
}
