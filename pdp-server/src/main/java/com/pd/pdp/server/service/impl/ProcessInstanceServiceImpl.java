package com.pd.pdp.server.service.impl;

import com.pd.pdp.gather.server.GatherDolphinServer;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.ProcessInstanceInfo;
import com.pd.pdp.server.entity.ProcessInstanceRunStatusStatistics;
import com.pd.pdp.server.mapper.ProcessInstanceMapper;
import com.pd.pdp.server.service.ProcessInstanceService;
import com.pd.pdp.server.utils.PageUtil;
import com.pd.pdp.server.vo.ProcessInstancePageVO;
import com.pd.pdp.server.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessInstanceServiceImpl implements ProcessInstanceService {

    @Autowired
    ProcessInstanceMapper processInstanceMapper;

    @Autowired
    GatherDolphinServer gatherDolphinServer;

    @Override
    public PageDTO<ProcessInstanceInfo> findByCondition(PageVO<ProcessInstancePageVO> pageVO) {
        PageDTO<ProcessInstanceInfo> pageDTO = new PageDTO<>();
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNumber(pageVO.getPageNumber());
        pageUtil.setTotalNumber(processInstanceMapper.selectProcessInstanceCount(pageVO));
        pageUtil.setCurrentPage(pageVO.getCurrentPage());
        pageVO.setDbIndex(pageUtil.getDbIndex());
        pageVO.setDbNumber(pageUtil.getDbNumber());
        List<ProcessInstanceInfo> processInstanceInfoList = processInstanceMapper.selectProcessInstanceInfoList(pageVO);
        pageDTO.setPageUtil(pageUtil);
        pageDTO.setList(processInstanceInfoList);
        return pageDTO;
    }

    @Override
    public List<ProcessInstanceRunStatusStatistics> statisticsEveryDayRunStatus(int startUnixTime, int endUnixTime, String runState) {
        return processInstanceMapper.statisticsEveryDayRunStatus(startUnixTime, endUnixTime, runState);
    }
}
