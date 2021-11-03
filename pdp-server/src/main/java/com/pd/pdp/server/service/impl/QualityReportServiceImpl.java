package com.pd.pdp.server.service.impl;

import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.*;
import com.pd.pdp.server.mapper.GatherDolphinMapper;
import com.pd.pdp.server.mapper.GatherQualityMapper;
import com.pd.pdp.server.mapper.QualityReportMapper;
import com.pd.pdp.server.service.GatherQualityService;
import com.pd.pdp.server.service.QualityReportService;
import com.pd.pdp.server.utils.PageUtil;
import com.pd.pdp.server.vo.GatherQualityPageVo;
import com.pd.pdp.server.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QualityReportServiceImpl implements QualityReportService {



    @Autowired
    QualityReportMapper qualityReportMapper;


    @Override
    public List<CountCheckResult> findToday() {
        return qualityReportMapper.findToday();
    }
}
