package com.pd.pdp.server.service;

import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.DataSourcesInfo;
import com.pd.pdp.server.entity.GatherDolphinInfo;
import com.pd.pdp.server.entity.GatherTypeInfo;
import com.pd.pdp.server.vo.DatasourcePageVO;
import com.pd.pdp.server.vo.GatherDolphinPageVo;
import com.pd.pdp.server.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/9
 */
public interface GatherDolphinService {


    public int insert(GatherDolphinInfo gatherDolphinInfo);

    public int delete(long id);

    public int update(GatherDolphinInfo gatherDolphinInfo);

    public int deleteBatchById(List<String> ids);

    public PageDTO<GatherDolphinInfo> findByCondition(PageVO<GatherDolphinPageVo> pageVO, int createUserId);


    public List<GatherDolphinInfo> select(GatherDolphinInfo gatherDolphinInfo);

    public List<Map<String, String>> selectDBByDatasource(DataSourcesInfo dataSourcesInfo);

    public List<Map<String, String>> selectTablesByDB(String db, DataSourcesInfo dataSourcesInfo);

    // public List<Map<String, String>> getDolphinProject(DataSourcesInfo dataSourcesInfo);

    public List<GatherTypeInfo> selectSyncType();

}
