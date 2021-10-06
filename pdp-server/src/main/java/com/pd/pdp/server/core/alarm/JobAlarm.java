package com.pd.pdp.server.core.alarm;


import com.pd.pdp.server.core.model.XxlJobInfo;
import com.pd.pdp.server.core.model.XxlJobLog;


public interface JobAlarm {

    /**
     * job alarm
     *
     * @param info
     * @param jobLog
     * @return
     */
    public boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog);

}
