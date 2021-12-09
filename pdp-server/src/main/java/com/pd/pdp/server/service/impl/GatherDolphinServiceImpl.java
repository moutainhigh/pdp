package com.pd.pdp.server.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.gather.constant.Constant;
import com.pd.pdp.gather.entity.GatherDolphinJobEntity;
import com.pd.pdp.gather.server.GatherDolphinServer;
import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.DataSourcesInfo;
import com.pd.pdp.server.entity.DataSourcesTypeInfo;
import com.pd.pdp.server.entity.GatherDolphinInfo;
import com.pd.pdp.server.entity.GatherTypeInfo;
import com.pd.pdp.server.mapper.DataSourcesMapper;
import com.pd.pdp.server.mapper.GatherDolphinMapper;
import com.pd.pdp.server.service.GatherDolphinService;
import com.pd.pdp.server.utils.PageUtil;
import com.pd.pdp.server.vo.GatherDolphinPageVo;
import com.pd.pdp.server.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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
    DataSourcesMapper dataSourcesMapper;

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
        int dataSourceTypeId = dataSourcesInfo.getDataSourceType();
        DataSourcesTypeInfo dataSourcesTypeInfo = dataSourcesMapper.selectDataSourcesTypeById(dataSourceTypeId);
        String dataSourceType = dataSourcesTypeInfo.getSourcesType();
        List<Map<String, String>> dbs = gatherDolphinServer.selectDBByDatasource(driver, url, username, password, dataSourceType);

        return dbs;
    }

    /**
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


    @Override
    public List<GatherTypeInfo> selectSyncType() {
        List<GatherTypeInfo> gatherTypeInfos = gatherDolphinMapper.selectSyncType();
        return gatherTypeInfos;
    }

    @Override
    public String initJob(List<String> ids) {
        String onlineResponse = null;

        GatherDolphinInfo gatherDolphinInfo = new GatherDolphinInfo();
        DataSourcesInfo dataSourcesInfoInput = new DataSourcesInfo();
        DataSourcesInfo dataSourcesInfoOutput = new DataSourcesInfo();
        DataSourcesInfo dataSourcesInfoDolphin = new DataSourcesInfo();
        GatherDolphinJobEntity gatherDolphinJobEntity = new GatherDolphinJobEntity();
        for (String id : ids) {
            gatherDolphinInfo.setId(Integer.valueOf(id));
            gatherDolphinInfo = gatherDolphinMapper.selectByPrimaryKey(gatherDolphinInfo);
            dataSourcesInfoInput.setId(gatherDolphinInfo.getDatasourceInput());
            dataSourcesInfoOutput.setId(gatherDolphinInfo.getDatasourceOutput());
            dataSourcesInfoDolphin.setId(gatherDolphinInfo.getDatasourceDolphin());
            dataSourcesInfoInput = dataSourcesMapper.selectByPrimaryKey(dataSourcesInfoInput);
            dataSourcesInfoOutput = dataSourcesMapper.selectByPrimaryKey(dataSourcesInfoOutput);
            dataSourcesInfoDolphin = dataSourcesMapper.selectByPrimaryKey(dataSourcesInfoDolphin);

            //set value
            gatherDolphinJobEntity.setGatherJobId(gatherDolphinInfo.getGatherJobId());

            //input data
            gatherDolphinJobEntity.setSystemName(dataSourcesInfoInput.getSystemName());
            gatherDolphinJobEntity.setDriverInput(dataSourcesInfoInput.getDriver());
            gatherDolphinJobEntity.setUrlInput(dataSourcesInfoInput.getUrl());
            gatherDolphinJobEntity.setUsernameInput(dataSourcesInfoInput.getUsername());
            gatherDolphinJobEntity.setPasswordInput(dataSourcesInfoInput.getPassword());
            gatherDolphinJobEntity.setDatabaseNameInput(gatherDolphinInfo.getDatabaseNameInput());
            gatherDolphinJobEntity.setTableNameInput(gatherDolphinInfo.getTableName());

            //output data
            gatherDolphinJobEntity.setDriverOutput(dataSourcesInfoOutput.getDriver());
            gatherDolphinJobEntity.setUrlOutput(dataSourcesInfoOutput.getUrl());
            gatherDolphinJobEntity.setUsernameOutput(dataSourcesInfoOutput.getUsername());
            gatherDolphinJobEntity.setPasswordOutput(dataSourcesInfoOutput.getPassword());

            //dolphin
            gatherDolphinJobEntity.setUrlDolphin(dataSourcesInfoDolphin.getUrl());
            gatherDolphinJobEntity.setUsernameDolphin(dataSourcesInfoDolphin.getUsername());
            gatherDolphinJobEntity.setPasswordDolphin(dataSourcesInfoDolphin.getPassword());
            gatherDolphinJobEntity.setDolphinProjectName(gatherDolphinInfo.getDolphinProjectName());

            //sync type
            gatherDolphinJobEntity.setSyncType(gatherDolphinInfo.getSyncType());

            //create hive table
            gatherDolphinJobEntity.setCreateHiveTable(gatherDolphinInfo.isCreateHiveTable());
            //init job不创建定时任务

            onlineResponse = gatherDolphinServer.initJob(gatherDolphinJobEntity);
        }

        return onlineResponse;
    }

    @Override
    public String createJobByTemplate(List<String> ids) {
        String onlineResponse = null;

        GatherDolphinInfo gatherDolphinInfo = new GatherDolphinInfo();
        DataSourcesInfo dataSourcesInfoInput = new DataSourcesInfo();
        DataSourcesInfo dataSourcesInfoOutput = new DataSourcesInfo();
        DataSourcesInfo dataSourcesInfoDolphin = new DataSourcesInfo();
        GatherDolphinJobEntity gatherDolphinJobEntity = new GatherDolphinJobEntity();
        for (String id : ids) {
            gatherDolphinInfo.setId(Integer.valueOf(id));
            gatherDolphinInfo = gatherDolphinMapper.selectByPrimaryKey(gatherDolphinInfo);
            dataSourcesInfoInput.setId(gatherDolphinInfo.getDatasourceInput());
            dataSourcesInfoOutput.setId(gatherDolphinInfo.getDatasourceOutput());
            dataSourcesInfoDolphin.setId(gatherDolphinInfo.getDatasourceDolphin());
            dataSourcesInfoInput = dataSourcesMapper.selectByPrimaryKey(dataSourcesInfoInput);
            dataSourcesInfoOutput = dataSourcesMapper.selectByPrimaryKey(dataSourcesInfoOutput);
            dataSourcesInfoDolphin = dataSourcesMapper.selectByPrimaryKey(dataSourcesInfoDolphin);

            //set value
            gatherDolphinJobEntity.setGatherJobId(gatherDolphinInfo.getGatherJobId());

            //input data
            gatherDolphinJobEntity.setSystemName(dataSourcesInfoInput.getSystemName());
            gatherDolphinJobEntity.setDriverInput(dataSourcesInfoInput.getDriver());
            gatherDolphinJobEntity.setUrlInput(dataSourcesInfoInput.getUrl());
            gatherDolphinJobEntity.setUsernameInput(dataSourcesInfoInput.getUsername());
            gatherDolphinJobEntity.setPasswordInput(dataSourcesInfoInput.getPassword());
            gatherDolphinJobEntity.setDatabaseNameInput(gatherDolphinInfo.getDatabaseNameInput());
            gatherDolphinJobEntity.setTableNameInput(gatherDolphinInfo.getTableName());

            //output data
            gatherDolphinJobEntity.setDriverOutput(dataSourcesInfoOutput.getDriver());
            gatherDolphinJobEntity.setUrlOutput(dataSourcesInfoOutput.getUrl());
            gatherDolphinJobEntity.setUsernameOutput(dataSourcesInfoOutput.getUsername());
            gatherDolphinJobEntity.setPasswordOutput(dataSourcesInfoOutput.getPassword());

            //dolphin
            gatherDolphinJobEntity.setUrlDolphin(dataSourcesInfoDolphin.getUrl());
            gatherDolphinJobEntity.setUsernameDolphin(dataSourcesInfoDolphin.getUsername());
            gatherDolphinJobEntity.setPasswordDolphin(dataSourcesInfoDolphin.getPassword());
            gatherDolphinJobEntity.setDolphinProjectName(gatherDolphinInfo.getDolphinProjectName());

            //sync type
            gatherDolphinJobEntity.setSyncType(gatherDolphinInfo.getSyncType());

            //create hive table
            gatherDolphinJobEntity.setCreateHiveTable(gatherDolphinInfo.isCreateHiveTable());

            //crontab
            gatherDolphinJobEntity.setCrontab(gatherDolphinInfo.getCrontab());

            onlineResponse = gatherDolphinServer.createJobByTemplate(gatherDolphinJobEntity);
            if (onlineResponse != null && JSONObject.parseObject(onlineResponse).getInteger(Constant.CODE) == 0) {
                gatherDolphinInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                gatherDolphinMapper.updateByPrimaryKey(gatherDolphinInfo);
            }
        }

        return onlineResponse;
    }

    @Override
    public String online(List<String> ids) {
        String onlineResponse = null;

        GatherDolphinInfo gatherDolphinInfo = new GatherDolphinInfo();
        DataSourcesInfo dataSourcesInfoInput = new DataSourcesInfo();
        DataSourcesInfo dataSourcesInfoOutput = new DataSourcesInfo();
        DataSourcesInfo dataSourcesInfoDolphin = new DataSourcesInfo();
        GatherDolphinJobEntity gatherDolphinJobEntity = new GatherDolphinJobEntity();
        for (String id : ids) {
            gatherDolphinInfo.setId(Integer.valueOf(id));
            gatherDolphinInfo = gatherDolphinMapper.selectByPrimaryKey(gatherDolphinInfo);
            dataSourcesInfoInput.setId(gatherDolphinInfo.getDatasourceInput());
            dataSourcesInfoOutput.setId(gatherDolphinInfo.getDatasourceOutput());
            dataSourcesInfoDolphin.setId(gatherDolphinInfo.getDatasourceDolphin());
            dataSourcesInfoInput = dataSourcesMapper.selectByPrimaryKey(dataSourcesInfoInput);
            dataSourcesInfoDolphin = dataSourcesMapper.selectByPrimaryKey(dataSourcesInfoDolphin);

            gatherDolphinJobEntity.setGatherJobId(gatherDolphinInfo.getGatherJobId());

            //input data
            gatherDolphinJobEntity.setSystemName(dataSourcesInfoInput.getSystemName());
            gatherDolphinJobEntity.setDatabaseNameInput(gatherDolphinInfo.getDatabaseNameInput());
            gatherDolphinJobEntity.setTableNameInput(gatherDolphinInfo.getTableName());

            //dolphin
            gatherDolphinJobEntity.setUrlDolphin(dataSourcesInfoDolphin.getUrl());
            gatherDolphinJobEntity.setUsernameDolphin(dataSourcesInfoDolphin.getUsername());
            gatherDolphinJobEntity.setPasswordDolphin(dataSourcesInfoDolphin.getPassword());
            gatherDolphinJobEntity.setDolphinProjectName(gatherDolphinInfo.getDolphinProjectName());

            //sync type
            gatherDolphinJobEntity.setSyncType(gatherDolphinInfo.getSyncType());


            onlineResponse = gatherDolphinServer.online(gatherDolphinJobEntity);
            if (JSONObject.parseObject(onlineResponse).getInteger(Constant.CODE) == 0) {
                gatherDolphinInfo.setOnline(true);
                gatherDolphinInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                gatherDolphinMapper.updateByPrimaryKey(gatherDolphinInfo);
            }
        }

        return onlineResponse;
    }

    @Override
    public String offline(List<String> ids) {
        String offlineResponse = null;

        GatherDolphinInfo gatherDolphinInfo = new GatherDolphinInfo();
        GatherDolphinJobEntity gatherDolphinJobEntity = new GatherDolphinJobEntity();
        DataSourcesInfo dataSourcesInfoDolphin = new DataSourcesInfo();

        for (String id : ids) {
            gatherDolphinInfo.setId(Integer.valueOf(id));
            gatherDolphinInfo = gatherDolphinMapper.selectByPrimaryKey(gatherDolphinInfo);
            dataSourcesInfoDolphin.setId(gatherDolphinInfo.getDatasourceDolphin());
            dataSourcesInfoDolphin = dataSourcesMapper.selectByPrimaryKey(dataSourcesInfoDolphin);

            //input data
            gatherDolphinJobEntity.setDatabaseNameInput(gatherDolphinInfo.getDatabaseNameInput());
            gatherDolphinJobEntity.setTableNameInput(gatherDolphinInfo.getTableName());

            gatherDolphinJobEntity.setGatherJobId(gatherDolphinInfo.getGatherJobId());

            //dolphin
            gatherDolphinJobEntity.setUrlDolphin(dataSourcesInfoDolphin.getUrl());
            gatherDolphinJobEntity.setUsernameDolphin(dataSourcesInfoDolphin.getUsername());
            gatherDolphinJobEntity.setPasswordDolphin(dataSourcesInfoDolphin.getPassword());
            gatherDolphinJobEntity.setDolphinProjectName(gatherDolphinInfo.getDolphinProjectName());

            //sync type
            gatherDolphinJobEntity.setSyncType(gatherDolphinInfo.getSyncType());


            offlineResponse = gatherDolphinServer.offline(gatherDolphinJobEntity);
            if (offlineResponse != null && JSONObject.parseObject(offlineResponse).getInteger(Constant.CODE) == 0) {
                gatherDolphinInfo.setOnline(false);
                gatherDolphinInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                gatherDolphinMapper.updateByPrimaryKey(gatherDolphinInfo);
            }
        }
        return offlineResponse;
    }

    @Override
    public String delDolphinJob(List<String> ids) {
        String delResponse = null;

        GatherDolphinInfo gatherDolphinInfo = new GatherDolphinInfo();
        GatherDolphinJobEntity gatherDolphinJobEntity = new GatherDolphinJobEntity();
        DataSourcesInfo dataSourcesInfoDolphin = new DataSourcesInfo();

        for (String id : ids) {
            gatherDolphinInfo.setId(Integer.valueOf(id));
            gatherDolphinInfo = gatherDolphinMapper.selectByPrimaryKey(gatherDolphinInfo);
            dataSourcesInfoDolphin.setId(gatherDolphinInfo.getDatasourceDolphin());
            dataSourcesInfoDolphin = dataSourcesMapper.selectByPrimaryKey(dataSourcesInfoDolphin);

            gatherDolphinJobEntity.setGatherJobId(gatherDolphinInfo.getGatherJobId());

            //input data
            gatherDolphinJobEntity.setDatabaseNameInput(gatherDolphinInfo.getDatabaseNameInput());
            gatherDolphinJobEntity.setTableNameInput(gatherDolphinInfo.getTableName());

            //dolphin
            gatherDolphinJobEntity.setUrlDolphin(dataSourcesInfoDolphin.getUrl());
            gatherDolphinJobEntity.setUsernameDolphin(dataSourcesInfoDolphin.getUsername());
            gatherDolphinJobEntity.setPasswordDolphin(dataSourcesInfoDolphin.getPassword());
            gatherDolphinJobEntity.setDolphinProjectName(gatherDolphinInfo.getDolphinProjectName());

            //sync type
            gatherDolphinJobEntity.setSyncType(gatherDolphinInfo.getSyncType());


            delResponse = gatherDolphinServer.delDolphinJob(gatherDolphinJobEntity);
            if (delResponse != null && JSONObject.parseObject(delResponse).getInteger(Constant.CODE) == 0) {
                gatherDolphinInfo.setOnline(false);
                gatherDolphinInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                gatherDolphinMapper.updateByPrimaryKey(gatherDolphinInfo);
            }


        }

        return delResponse;
    }


    @Override
    public JSONArray syncDolphinJobJson(List<String> ids) {
        JSONArray jsonArray = null;

        GatherDolphinInfo gatherDolphinInfo = new GatherDolphinInfo();
        GatherDolphinJobEntity gatherDolphinJobEntity = new GatherDolphinJobEntity();
        DataSourcesInfo dataSourcesInfoDolphin = new DataSourcesInfo();

        for (String id : ids) {
            gatherDolphinInfo.setId(Integer.valueOf(id));
            gatherDolphinInfo = gatherDolphinMapper.selectByPrimaryKey(gatherDolphinInfo);
            dataSourcesInfoDolphin.setId(gatherDolphinInfo.getDatasourceDolphin());
            dataSourcesInfoDolphin = dataSourcesMapper.selectByPrimaryKey(dataSourcesInfoDolphin);

            gatherDolphinJobEntity.setGatherJobId(gatherDolphinInfo.getGatherJobId());

            //input data
            gatherDolphinJobEntity.setDatabaseNameInput(gatherDolphinInfo.getDatabaseNameInput());
            gatherDolphinJobEntity.setTableNameInput(gatherDolphinInfo.getTableName());

            //dolphin
            gatherDolphinJobEntity.setUrlDolphin(dataSourcesInfoDolphin.getUrl());
            gatherDolphinJobEntity.setUsernameDolphin(dataSourcesInfoDolphin.getUsername());
            gatherDolphinJobEntity.setPasswordDolphin(dataSourcesInfoDolphin.getPassword());
            gatherDolphinJobEntity.setDolphinProjectName(gatherDolphinInfo.getDolphinProjectName());

            //sync type
            gatherDolphinJobEntity.setSyncType(gatherDolphinInfo.getSyncType());


            jsonArray = gatherDolphinServer.getDolphinJobJson(gatherDolphinJobEntity);
            if (jsonArray != null) {
                gatherDolphinInfo.setGatherJobInfo(jsonArray.toJSONString());
                gatherDolphinInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                gatherDolphinMapper.updateByPrimaryKey(gatherDolphinInfo);
            }
        }
        return jsonArray;
    }

    @Override
    public String createJobByJson(List<String> ids) {
        String response = null;

        GatherDolphinInfo gatherDolphinInfo = new GatherDolphinInfo();
        GatherDolphinJobEntity gatherDolphinJobEntity = new GatherDolphinJobEntity();
        DataSourcesInfo dataSourcesInfoDolphin = new DataSourcesInfo();

        for (String id : ids) {
            gatherDolphinInfo.setId(Integer.valueOf(id));
            gatherDolphinInfo = gatherDolphinMapper.selectByPrimaryKey(gatherDolphinInfo);
            dataSourcesInfoDolphin.setId(gatherDolphinInfo.getDatasourceDolphin());
            dataSourcesInfoDolphin = dataSourcesMapper.selectByPrimaryKey(dataSourcesInfoDolphin);
            gatherDolphinJobEntity.setGatherJobId(gatherDolphinInfo.getGatherJobId());

            //input data
            gatherDolphinJobEntity.setDatabaseNameInput(gatherDolphinInfo.getDatabaseNameInput());
            gatherDolphinJobEntity.setTableNameInput(gatherDolphinInfo.getTableName());
            gatherDolphinJobEntity.setGatherJobInfo(gatherDolphinInfo.getGatherJobInfo());

            //dolphin
            gatherDolphinJobEntity.setUrlDolphin(dataSourcesInfoDolphin.getUrl());
            gatherDolphinJobEntity.setUsernameDolphin(dataSourcesInfoDolphin.getUsername());
            gatherDolphinJobEntity.setPasswordDolphin(dataSourcesInfoDolphin.getPassword());
            gatherDolphinJobEntity.setDolphinProjectName(gatherDolphinInfo.getDolphinProjectName());

            //sync type
            gatherDolphinJobEntity.setSyncType(gatherDolphinInfo.getSyncType());


            response = gatherDolphinServer.createJobByJson(gatherDolphinJobEntity);
            if (JSONObject.parseObject(response).getInteger(Constant.CODE) == 0) {
                gatherDolphinInfo.setOnline(false);
                gatherDolphinInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                gatherDolphinMapper.updateByPrimaryKey(gatherDolphinInfo);
            }


        }

        return response;
    }


}
