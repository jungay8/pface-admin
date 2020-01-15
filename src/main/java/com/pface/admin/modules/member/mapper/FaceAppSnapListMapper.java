package com.pface.admin.modules.member.mapper;

import com.pface.admin.core.utils.MyMapper;
import com.pface.admin.modules.front.vo.FaceAppSnapListAssemblePojo;
import com.pface.admin.modules.front.vo.FaceAppSnapListPojo;
import com.pface.admin.modules.front.vo.FaceAppSnapListQueryParams;
import com.pface.admin.modules.member.po.FaceAppSnapList;

import java.util.List;

public interface FaceAppSnapListMapper extends MyMapper<FaceAppSnapList> {
    /**
     * 抓拍记录查询
     *
     * @param params
     * @return
     */
    List<FaceAppSnapListPojo> queryAppSnapList(FaceAppSnapListQueryParams params);

    /**
     * 签到汇总
     * @param params
     * @return
     */
    List<FaceAppSnapListAssemblePojo> assembleAppSnapList(FaceAppSnapListQueryParams params);

}