package com.pface.admin.modules.member.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.member.mapper.FaceDeviceChannelResMapper;
import com.pface.admin.modules.member.po.FaceDeviceChannelRes;
import com.pface.admin.modules.member.service.FaceDeviceChannelResService;
import com.pface.admin.modules.member.vo.FaceDeviceChannelResParams;
import com.pface.admin.modules.member.vo.FaceDeviceChannelResVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FaceDeviceChannelResServiceImpl extends BaseService<FaceDeviceChannelRes> implements FaceDeviceChannelResService {
    @Autowired
    private FaceDeviceChannelResMapper faceDeviceChannelResMapper;

    @Override
    public Page<FaceDeviceChannelResVo> queryFaceDeviceChannelResList(PageQuery pageQuery, FaceDeviceChannelResParams params) {
        return PageHelper.startPage(pageQuery)
                .doSelectPage(() -> faceDeviceChannelResMapper.queryFaceUserChannelResList(params));
    }
}
