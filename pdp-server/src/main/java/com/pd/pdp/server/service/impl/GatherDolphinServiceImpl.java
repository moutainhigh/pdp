package com.pd.pdp.server.service.impl;

import com.pd.pdp.gather.constant.Constant;
import com.pd.pdp.gather.server.GatherDolphinServer;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.DataSourcesInfo;
import com.pd.pdp.server.entity.GatherDolphinInfo;
import com.pd.pdp.server.entity.GatherTypeInfo;
import com.pd.pdp.server.mapper.GatherDolphinMapper;
import com.pd.pdp.server.service.GatherDolphinService;
import com.pd.pdp.gather.utils.JdbcPool;
import com.pd.pdp.server.utils.MD5Util;
import com.pd.pdp.server.utils.PageUtil;
import com.pd.pdp.server.vo.GatherDolphinPageVo;
import com.pd.pdp.server.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/9
 */

@Service
public class GatherDolphinServiceImpl implements GatherDolphinService {

    @Autowired
    GatherDolphinMapper gatherDolphinMapper;

    @Autowired
    GatherDolphinServer gatherDolphinServer;


    @Override
    public int insert(GatherDolphinInfo gatherDolphinInfo) {
        return gatherDolphinMapper.insert(gatherDolphinInfo);
    }

    @Override
    public int delete(long id) {
        return gatherDolphinMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(GatherDolphinInfo gatherDolphinInfo) {
        return gatherDolphinMapper.updateByPrimaryKey(gatherDolphinInfo);
    }

    @Override
    public int deleteBatchById(List<String> ids) {
        for (String id : ids) {
            gatherDolphinMapper.deleteBatchById(id);
        }

        return 0;
    }

    @Override
    public PageDTO<GatherDolphinInfo> findByCondition(PageVO<GatherDolphinPageVo> pageVO, int createUserId) {
        PageDTO<GatherDolphinInfo> pageDTO = new PageDTO<>();
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNumber(pageVO.getPageNumber());
        pageUtil.setTotalNumber(gatherDolphinMapper.selectGatherDolphinCount(pageVO, createUserId));
        pageUtil.setCurrentPage(pageVO.getCurrentPage());
        pageVO.setDbIndex(pageUtil.getDbIndex());
        pageVO.setDbNumber(pageUtil.getDbNumber());
        List<GatherDolphinInfo> gatherDolphinInfoList = gatherDolphinMapper.selectGatherDolphinInfoList(pageVO, createUserId);
        pageDTO.setPageUtil(pageUtil);
        pageDTO.setList(gatherDolphinInfoList);
        return pageDTO;
    }

    @Override
    public List<GatherDolphinInfo> select(GatherDolphinInfo gatherDolphinInfo) {
        return gatherDolphinMapper.select(gatherDolphinInfo);
    }

    @Override
    public List<Map<String, String>> selectDBByDatasource(DataSourcesInfo dataSourcesInfo) {
        String driver = dataSourcesInfo.getDriver();
        String url = dataSourcesInfo.getUrl();
        String username = dataSourcesInfo.getUsername();
        String password = dataSourcesInfo.getPassword();
        String dataSourceType = dataSourcesInfo.getDataSourceType();
        List<Map<String, String>> dbs = gatherDolphinServer.selectDBByDatasource(driver, url, username, password,dataSourceType);

        return dbs;
    }

    /**
     *
     * @param db
     * @return
     */
    @Override
    public List<Map<String, String>> selectTablesByDB(String db, DataSourcesInfo dataSourcesInfo) {
        String driver = dataSourcesInfo.getDriver();
        String url = dataSourcesInfo.getUrl();
        String username = dataSourcesInfo.getUsername();
        String password = dataSourcesInfo.getPassword();
        List<Map<String, String>> tables = gatherDolphinServer.selectTablesByDB(db, driver, url, username, password);

        return tables;
    }

    // @Override
    // public List<Map<String, String>> getDolphinProject(DataSourcesInfo dataSourcesInfo) {
    //     String url = dataSourcesInfo.getUrl();
    //     String username = dataSourcesInfo.getUsername();
    //     String password = dataSourcesInfo.getPassword();
    //
    //     try {
    //         List<Map<String, String>> dolphinProject = gatherDolphinServer.getDolphinProject(url, username, password);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return null;
    // }

    @Override
    public List<GatherTypeInfo> selectSyncType() {
        List<GatherTypeInfo> gatherTypeInfos = gatherDolphinMapper.selectSyncType();
        return gatherTypeInfos;
    }

}
