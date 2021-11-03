package com.pd.pdp.server.service;


import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.*;
import com.pd.pdp.server.vo.DatasourcePageVO;
import com.pd.pdp.server.vo.GatherQualityPageVo;
import com.pd.pdp.server.vo.PageVO;

import java.util.List;

public interface GatherQualityService {

//    public DataSourcesInfo selectById(int id);

    public int insert(GatherQualityInfo gatherQualityInfo);

    public GatherDolphinInfo selectGatherJobId(GatherQualityInfo gatherQualityInfo);

    public int delete(long id);

    public int update(GatherQualityInfo gatherQualityInfo);

//    public List<DataSourcesInfo> selectByExample(DataSourcesInfo dataSourcesInfo);

    public PageDTO<GatherQualityInfo> findByCondition(PageVO<GatherQualityPageVo> pageVO, int createUserId);

//    public List<DataSourcesInfo> selectAll();

    public void deleteBatchById(List<String> ids);

    public List<GatherQualityInfo> select(GatherQualityInfo gatherQualityInfo);



    public List<QualityRulesInfo> checkRules();

}
