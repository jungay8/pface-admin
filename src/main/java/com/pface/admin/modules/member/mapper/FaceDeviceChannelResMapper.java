package com.pface.admin.modules.member.mapper;

import com.pface.admin.core.utils.MyMapper;
import com.pface.admin.modules.member.po.FaceDeviceChannelRes;
import com.pface.admin.modules.member.vo.FaceDeviceChannelResParams;
import com.pface.admin.modules.member.vo.FaceDeviceChannelResVo;
import com.pface.admin.modules.member.vo.FaceUserChannelResVo;

import java.util.List;

public interface FaceDeviceChannelResMapper extends MyMapper<FaceDeviceChannelRes> {

    List<FaceDeviceChannelResVo> queryFaceUserChannelResList(FaceDeviceChannelResParams params);
}