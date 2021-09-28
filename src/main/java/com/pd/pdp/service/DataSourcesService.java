package com.pd.pdp.service;


import com.pd.pdp.dto.PageDTO;
import com.pd.pdp.entity.DataSourcesInfo;
import com.pd.pdp.entity.DataSourcesTypeInfo;
import com.pd.pdp.vo.DatasourcePageVO;
import com.pd.pdp.vo.PageVO;

import java.util.List;

/**
 * ClassName: DataSourcesService
 *
 * @author zyc-admin
 * @date 2017年12月26日
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
