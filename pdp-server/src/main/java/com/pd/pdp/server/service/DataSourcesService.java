package com.pd.pdp.server.service;


import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.DataSourcesInfo;
import com.pd.pdp.server.entity.DataSourcesTypeInfo;
import com.pd.pdp.server.vo.DatasourcePageVO;
import com.pd.pdp.server.vo.PageVO;

import java.util.List;

/**
 * ClassName: DataSourcesService
 *
 * @author zyc-admin
 * @Description:
 */
public interface DataSourcesService {

    public DataSourcesInfo selectById(int id);

    public int insert(DataSourcesInfo dataSourcesInfo);

    public int delete(long id);

    public int update(DataSourcesInfo dataSourcesInfo);

    public List<DataSourcesInfo> selectByExample(DataSourcesInfo dataSourcesInfo);

    public PageDTO<DataSourcesInfo> findByCondition(PageVO<DatasourcePageVO> pageVO, int createUserId);

    public List<DataSourcesInfo> selectAll();

    public int deleteBatchById(List<String> ids);

    public List<DataSourcesInfo> select(DataSourcesInfo dataSourcesInfo);

    public List<DataSourcesTypeInfo> selectDataSourcesType();

}
