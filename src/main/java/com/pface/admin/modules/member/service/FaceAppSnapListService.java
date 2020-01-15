package com.pface.admin.modules.member.service;

import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.front.vo.FaceAppSnapListAssemblePojo;
import com.pface.admin.modules.front.vo.FaceAppSnapListPojo;
import com.pface.admin.modules.front.vo.FaceAppSnapListQueryParams;
import com.pface.admin.modules.member.po.FaceAppSnapList;

import java.util.List;

public interface FaceAppSnapListService  extends IService<FaceAppSnapList> {
    /**
     * 抓取服务端人脸抓取数据
     */
    public void pullSnapList();

    /**
     * 查询人脸抓拍记录
     *
     * @param pageQuery
     * @param params
     * @return
     */
    List<FaceAppSnapListPojo> queryAppSnapList(PageQuery pageQuery, FaceAppSnapListQueryParams params);

    /**
     * 签到汇总
     * @param pageQuery
     * @param params
     * @return
     */
    public List<FaceAppSnapListAssemblePojo> assembleAppSnapList(PageQuery pageQuery, FaceAppSnapListQueryParams params);
}
