package com.pd.pdp.server.service.impl;

import com.pd.pdp.gather.server.GatherDolphinServer;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.*;
import com.pd.pdp.server.mapper.DataSourcesMapper;
import com.pd.pdp.server.mapper.GatherDolphinMapper;
import com.pd.pdp.server.mapper.GatherQualityMapper;
import com.pd.pdp.server.service.DataSourcesService;
import com.pd.pdp.server.service.GatherQualityService;
import com.pd.pdp.server.utils.PageUtil;
import com.pd.pdp.server.vo.DatasourcePageVO;
import com.pd.pdp.server.vo.GatherQualityPageVo;
import com.pd.pdp.server.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GatherQualityServiceImpl implements GatherQualityService {



    @Autowired
    GatherDolphinMapper gatherDolphinMapper;

    @Autowired
    GatherQualityMapper gatherQualityMapper;

    @Autowired
    GatherQualityService gatherQualityServiceImpl;




    /*@Override
    public DataSourcesInfo selectById(int id) {
        DataSourcesInfo dataSourcesInfo = new DataSourcesInfo();
        dataSourcesInfo.setId(id);
        return dataSourcesMapper.selectByPrimaryKey(dataSourcesInfo);
    }*/

    @Override
    public int insert(GatherQualityInfo gatherQualityInfo) {
        return gatherQualityMapper.insert(gatherQualityInfo);
    }

    @Override
    public GatherDolphinInfo selectGatherJobId(GatherQualityInfo gatherQualityInfo) {
        int datasourceInputId = gatherQualityInfo.getDatasourceInputId();
        String sourceDb = gatherQualityInfo.getSourceDb();
        String sourceTable = gatherQualityInfo.getSourceTable();
        GatherDolphinInfo gatherDolphinInfo = gatherDolphinMapper.selectGatherJobId(datasourceInputId, sourceDb, sourceTable);

        return gatherDolphinInfo;
    }

    @Override
    public int delete(long id) {
        return gatherQualityMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(GatherQualityInfo gatherQualityInfo) {
        return gatherQualityMapper.updateByPrimaryKey(gatherQualityInfo);
    }

    /*@Override
    public List<DataSourcesInfo> selectByExample(DataSourcesInfo dataSourcesInfo) {
        // return dataSourcesMapper.selectByExample(dataSourcesInfo);
        return null;
    }*/

    @Override
    public PageDTO<GatherQualityInfo> findByCondition(PageVO<GatherQualityPageVo> pageVO, int createUserId) {
        PageDTO<GatherQualityInfo> pageDTO = new PageDTO<>();
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNumber(pageVO.getPageNumber());
        pageUtil.setTotalNumber(gatherQualityMapper.selectGatherQualityCount(pageVO, createUserId));
        pageUtil.setCurrentPage(pageVO.getCurrentPage());
        pageVO.setDbIndex(pageUtil.getDbIndex());
        pageVO.setDbNumber(pageUtil.getDbNumber());
        List<GatherQualityInfo> GatherQualityInfoList = gatherQualityMapper.selectGatherQualityInfoList(pageVO, createUserId);
        pageDTO.setPageUtil(pageUtil);
        pageDTO.setList(GatherQualityInfoList);
        return pageDTO;
    }

 /*   @Override
    public List<DataSourcesInfo> selectAll() {
        return dataSourcesMapper.selectAll();
    }*/

    @Override
    public void deleteBatchById(List<String> ids) {
        for (String id : ids) {
            gatherQualityMapper.deleteBatchById(id);
        }

    }

    @Override
    public List<GatherQualityInfo> select(GatherQualityInfo gatherQualityInfo) {
        return gatherQualityMapper.select(gatherQualityInfo);
    }

//    @Override
//    public List<DataSourcesTypeInfo> selectDataSourcesType() {
//        return dataSourcesMapper.selectDataSourcesType();
//    }

    /*@Override
    public boolean testConn(DataSourcesInfo dataSourcesInfo) {
        int dataSourceTypeId = dataSourcesInfo.getDataSourceType();
        DataSourcesTypeInfo dataSourcesTypeInfo = dataSourcesMapper.selectDataSourcesTypeById(dataSourceTypeId);
        String dataSourceType = dataSourcesTypeInfo.getSourcesType();
        return gatherDolphinServer.testConn(dataSourcesInfo.getDriver(), dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(), dataSourcesInfo.getPassword(), dataSourceType);
    }*/

    @Override
    public List<QualityRulesInfo> checkRules(){
        return gatherQualityMapper.selectRules();
    };

}
