package com.pd.pdp.server.service;



import com.pd.pdp.server.entity.*;



import java.util.List;

public interface QualityReportService {

    public List<CountCheckResult> findToday();

}
