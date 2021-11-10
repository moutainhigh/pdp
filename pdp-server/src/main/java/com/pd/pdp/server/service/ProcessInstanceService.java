package com.pd.pdp.server.service;

import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.ProcessInstanceInfo;
import com.pd.pdp.server.entity.ProcessInstanceRunStatusStatistics;
import com.pd.pdp.server.vo.ProcessInstancePageVO;
import com.pd.pdp.server.vo.PageVO;

import java.util.List;

public interface ProcessInstanceService {


    public PageDTO<ProcessInstanceInfo> findByCondition(PageVO<ProcessInstancePageVO> pageVO);
    public List<ProcessInstanceRunStatusStatistics> statisticsEveryDayRunStatus(int startUnixTime, int endUnixTime, String runState);

}
