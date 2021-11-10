package com.pd.pdp.server.service.impl;

import com.pd.pdp.server.entity.*;
import com.pd.pdp.server.mapper.QualityReportMapper;
import com.pd.pdp.server.service.QualityReportService;
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

    @Override
    public List<DataCheckResultStatistics> statisticsEveryDayDataQuality(int startUnixTime, int endUnixTime, int checkResult) {
        return qualityReportMapper.statisticsEveryDayDataQuality(startUnixTime, endUnixTime, checkResult);
    }
}
