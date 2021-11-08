package com.pd.pdp.server.service;

import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.ProcessInstanceInfo;
import com.pd.pdp.server.vo.ProcessInstancePageVO;
import com.pd.pdp.server.vo.PageVO;

public interface ProcessInstanceService {


    public PageDTO<ProcessInstanceInfo> findByCondition(PageVO<ProcessInstancePageVO> pageVO);

}
