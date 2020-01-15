package com.pface.admin.modules.member.service;

import com.github.pagehelper.Page;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.member.po.FaceDeviceChannelRes;
import com.pface.admin.modules.member.vo.FaceDeviceChannelResParams;
import com.pface.admin.modules.member.vo.FaceDeviceChannelResVo;
import com.pface.admin.modules.member.vo.FaceUserChannelResParams;
import com.pface.admin.modules.member.vo.FaceUserChannelResVo;

public interface FaceDeviceChannelResService extends IService<FaceDeviceChannelRes> {
    /**
     * 用户资源查询,分页
     *
     * @return
     */
    Page<FaceDeviceChannelResVo> queryFaceDeviceChannelResList(PageQuery pageQuery, FaceDeviceChannelResParams params);

}
