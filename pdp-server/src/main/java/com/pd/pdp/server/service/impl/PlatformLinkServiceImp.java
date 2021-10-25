package com.pd.pdp.server.service.impl;

import com.pd.pdp.server.entity.PlatformInfo;
import com.pd.pdp.server.service.PlatformLinkService;
import com.pd.pdp.server.mapper.PlatformEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformLinkServiceImp implements PlatformLinkService {
    @Autowired
    PlatformEntityMapper platformEntityMapper;

    @Override
    public int insert(PlatformInfo platformInfo) {
        return platformEntityMapper.insert(platformInfo);
    }

    @Override
    public void update(PlatformInfo platformInfo) {
        platformEntityMapper.updateByPrimaryKey(platformInfo);
    }

    @Override
    public List<PlatformInfo> findAllByStatus() {
        return platformEntityMapper.findAllByStatus();
    }

    @Override
    public List<PlatformInfo> findAllReportBoardByStatus() {
        return platformEntityMapper.findAllReportBoardByStatus();
    }

    @Override
    public List<PlatformInfo> findByName(String name) {
        return platformEntityMapper.findByName(name);
    }
}
