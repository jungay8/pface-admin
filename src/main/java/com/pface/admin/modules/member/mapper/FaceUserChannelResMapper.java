package com.pface.admin.modules.member.mapper;

import com.pface.admin.core.utils.MyMapper;
import com.pface.admin.modules.member.po.FaceUserChannelRes;
import com.pface.admin.modules.member.vo.FaceUserChannelResParams;
import com.pface.admin.modules.member.vo.FaceUserChannelResVo;

import java.util.List;

public interface FaceUserChannelResMapper extends MyMapper<FaceUserChannelRes> {
    /**
     * 查询授权记录，即用户资源
     * @param params
     * @return
     */
    List<FaceUserChannelResVo> queryFaceUserChannelResList(FaceUserChannelResParams params);
}