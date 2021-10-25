package com.pd.pdp.server.mapper;

import com.pd.notscan.BaseMapper;
import com.pd.pdp.server.entity.PlatformInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PlatformEntityMapper extends BaseMapper<PlatformInfo> {
    @Select(value = "select * from pdp_db.platform_info where name like '%${name}%' and status = 1")
    public List<PlatformInfo> findByName(String name);

    @Select(value = "select * from pdp_db.platform_info where status = 1")
    public List<PlatformInfo> findAllByStatus();

    @Select(value = "select * from pdp_db.platform_info where status = 1 and url_type = 1 order by url_sort")
    public List<PlatformInfo> findAllReportBoardByStatus();


}
