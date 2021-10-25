package com.pd.pdp.server.service;

import com.pd.pdp.server.entity.PlatformInfo;


import java.util.List;

public interface PlatformLinkService {
    /**
     * @Author pdp
     * @Description 保存平台url
     * @Param [name, url]
     * @return void
     **/
    public int insert(PlatformInfo platformInfo);

    /**
     * @Author pdp
     * @Description 修改平台url
     * @Param [id]
     * @return void
     **/
    public void update(PlatformInfo platformInfo);

    /**
     * @Author pdp
     * @Description 查询平台url信息
     * @Param [id]
     * @return void
     **/
    public List<PlatformInfo> findAllByStatus();

    /**
     * @Author pdp
     * @Description 根据名称查询平台url信息
     * @Param [id]
     * @return void
     **/
    public List<PlatformInfo> findByName(String name);
}
