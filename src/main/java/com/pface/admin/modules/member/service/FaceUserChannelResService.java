package com.pface.admin.modules.member.service;

import com.github.pagehelper.Page;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.member.po.FaceUserChannelRes;
import com.pface.admin.modules.member.vo.FaceUserChannelResParams;
import com.pface.admin.modules.member.vo.FaceUserChannelResVo;

import java.util.List;

public interface FaceUserChannelResService extends IService<FaceUserChannelRes> {
    /**
     * 用户资源查询,分页
     *
     * @return
     */
    Page<FaceUserChannelResVo> queryFaceUserChannelResList(PageQuery pageQuery, FaceUserChannelResParams params);

}
