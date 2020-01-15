package com.pface.admin.modules.front.vo;

import com.pface.admin.modules.member.enums.CoverOriginEnum;
import com.pface.admin.modules.member.po.MemberBelongLabel;
import com.pface.admin.modules.member.po.MemberPriceLabel;
import com.pface.admin.modules.member.po.MemberPublishLabel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MediaVo {

    /**
     * 资源id
     */
    private Long id;

    private Long mediaFileId;

    /**
     * 媒体标题
     */
    private String mediaTitle;

    /**
     * 关键词
     */
    private String mediaKeyword;

    /**
     * 归属标签id
     */
    private List<MemberBelongLabel> belongLabel;

    private String belongLabels;

    /**
     * 价格标签id
     */
    private List<MemberPriceLabel> priceLabel;

    private MemberPriceLabel memberPriceLabel;

    /**
     * 转载标签id
     */
    private List<MemberPublishLabel> publishLabel;

    private String pubLabels;

    /**
     * 目录分类id
     */
    private Long catalogueId;

    private String catalogueName;

    /**
     * 封面地址
     */
    private String coverUrl;

    /**
     * 0上传 1截取
     */
    private CoverOriginEnum coverOrigin;


    /**
     * 归属用户id
     */
    private Long uid;

    /**
     * 操作修改日期
     */
    private Date opDate;

    /**
     * 媒体简介
     */
    private String mediaBrief;

}