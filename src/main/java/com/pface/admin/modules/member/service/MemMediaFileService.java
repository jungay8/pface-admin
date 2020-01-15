package com.pface.admin.modules.member.service;

import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.front.vo.GoodsVo;
import com.pface.admin.modules.front.vo.MemberMediaPojo;
import com.pface.admin.modules.front.vo.MemberMediaQueryParams;
import com.pface.admin.modules.front.vo.MemberUserspaceAssemblyParams;
import com.pface.admin.modules.member.po.*;
import com.pface.admin.modules.member.vo.AuditLogVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/8
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
public interface MemMediaFileService extends IService<MemberMediaFile> {

    Long insert(MemberMediaFile memberMediaFile);

    void batchDel(List<GoodsVo> ids);

    void batchGoodsOn(List<GoodsVo> ids);
    void batchGoodsOff(List<GoodsVo> ids);

    boolean auditNoPass(AuditLogVo auditLogVo);

    boolean auditPass(AuditLogVo auditLogVo);

    /**
     * 媒体内容查询,分页
     *
     * @return
     */
    List<MemberMediaPojo> queryMemberMediaPageList(PageQuery pageQuery, MemberMediaQueryParams params);

    /**
     * 媒体内容查询,不分页
     *
     * @return
     */
    List<MemberMediaPojo> queryMemberMediaList(MemberMediaQueryParams params);

    /**
     * 物理删除
     * @param
     */
    void physicalDeleteFileAndDb(String mediaType, String mediaId, String mediaFileId, MemberUser userinfo);

    List<MemberUserspace> assembleUserspacePageList(PageQuery pageQuery,MemberUserspace memberUserspace);

    //新增编目
    public void createVideoFileAttr(MultipartFile coverfile,
                                    Long mediaFileId,
                                    String[] relationMediaIds,
                                    String mediaType,
                                    String mediaTitle,
                                    Long catalogueId,
                                    String mediaKeyword,
                                    String belongLabelid,
                                    String priceLabelid,
                                    String publishLabelid,
                                    String mediaBrief,
                                    MemberUser user, JmgoFilePath jmgoFilePath);

    //修改编目
    public void updateVideoFileAttr(MultipartFile coverfile,
                                    Long mediaFileId,
                                    String[] relationMediaIds,
                                    String coverOrigin,
                                    String mediaType,
                                    String mediaTitle,
                                    Long catalogueId,
                                    String mediaKeyword,
                                    String belongLabelid,
                                    String priceLabelid,
                                    String publishLabelid,
                                    String mediaBrief, MemberUser user, JmgoFilePath jmgoFilePath,
                                    MemberMedia memberMediaOld);
}
