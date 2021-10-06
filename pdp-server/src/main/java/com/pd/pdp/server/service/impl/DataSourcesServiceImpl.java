package com.pd.pdp.server.service.impl;

import com.pd.pdp.server.dto.PageDTO;
import com.pd.pdp.server.entity.DataSourcesInfo;
import com.pd.pdp.server.entity.DataSourcesTypeInfo;
import com.pd.pdp.server.mapper.DataSourcesMapper;
import com.pd.pdp.server.utils.PageUtil;
import com.pd.pdp.server.vo.DatasourcePageVO;
import com.pd.pdp.server.service.DataSourcesService;
import com.pd.pdp.server.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataSourcesServiceImpl implements DataSourcesService {

    @Autowired
    DataSourcesMapper dataSourcesMapper;


    @Override
    public DataSourcesInfo selectById(int id) {
        DataSourcesInfo dataSourcesInfo = new DataSourcesInfo();
        dataSourcesInfo.setId(id);
        return dataSourcesMapper.selectByPrimaryKey(dataSourcesInfo);
    }

    @Override
    public int insert(DataSourcesInfo dataSourcesInfo) {
        return dataSourcesMapper.insert(dataSourcesInfo);
    }

    @Override
    public int delete(long id) {
        return dataSourcesMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(DataSourcesInfo dataSourcesInfo) {
        return dataSourcesMapper.updateByPrimaryKey(dataSourcesInfo);
    }

    @Override
    public List<DataSourcesInfo> selectByExample(DataSourcesInfo dataSourcesInfo) {
        // return dataSourcesMapper.selectByExample(dataSourcesInfo);
        return null;
    }

    @Override
    public PageDTO<DataSourcesInfo> findByCondition(PageVO<DatasourcePageVO> pageVO, int createUserId) {
        PageDTO<DataSourcesInfo> pageDTO = new PageDTO<>();
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNumber(pageVO.getPageNumber());
        pageUtil.setTotalNumber(dataSourcesMapper.selectDatasourcesCount(pageVO, createUserId));
        pageUtil.setCurrentPage(pageVO.getCurrentPage());
        pageVO.setDbIndex(pageUtil.getDbIndex());
        pageVO.setDbNumber(pageUtil.getDbNumber());
        List<DataSourcesInfo> dataSourcesInfoList = dataSourcesMapper.selectDataSourcesInfoList(pageVO, createUserId);
        pageDTO.setPageUtil(pageUtil);
        pageDTO.setList(dataSourcesInfoList);
        return pageDTO;
    }

    @Override
    public List<DataSourcesInfo> selectAll() {
        return dataSourcesMapper.selectAll();
    }

    @Override
    public int deleteBatchById(List<String> ids) {
        for (String id : ids) {
            dataSourcesMapper.deleteBatchById(id);
        }

        return 0;
    }

    @Override
    public List<DataSourcesInfo> select(DataSourcesInfo dataSourcesInfo) {
        return dataSourcesMapper.select(dataSourcesInfo);
    }

    @Override
    public List<DataSourcesTypeInfo> selectDataSourcesType() {
        return dataSourcesMapper.selectDataSourcesType();
    }

}
