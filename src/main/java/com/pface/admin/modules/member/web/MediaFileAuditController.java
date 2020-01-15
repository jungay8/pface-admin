package com.pface.admin.modules.member.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.Result;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.front.vo.*;
import com.pface.admin.modules.member.enums.AuditStatusEnum;
import com.pface.admin.modules.member.enums.GoodsStatusEnum;
import com.pface.admin.modules.member.enums.MediaStatusEnum;
import com.pface.admin.modules.member.enums.MediaTypeEnum;
import com.pface.admin.modules.member.po.*;
import com.pface.admin.modules.member.service.*;
import com.pface.admin.modules.member.vo.AuditLogVo;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.service.UserService;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author daniel.liu
 */
@Controller
@RequestMapping("/admin/auditfile")
@Api
public class MediaFileAuditController extends BaseCrudController<MemberMediaFile> {

    @Autowired
    private MemMediaFileService mediaFileService;

    @Autowired
    private MemberMediaService memberMediaService;

    @Autowired
    private MemBelongLabService  belongLabService;

    @Autowired
    private MemPriceLabService priceLabService;

    @Autowired
    private MemPubLabelService pubLabelService;

    @Autowired
    private MemberUserService memberUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private MediaDocImgService mediaDocImgService;

    @Autowired
    private MemCatalogService catalogService;

    @Autowired
    private MemPriceLabService memPriceLabService;

    @Autowired
    private MemBelongLabService memBelongLabService;

    @Autowired
    private MemPubLabelService memPubLabelService;

    @GetMapping
    @RequiresPermissions("auditfile:view")
    public String memberPage(Model model) {

        model.addAttribute("auditStatus", AuditStatusEnum.values());
        model.addAttribute("mediaTypes", MediaTypeEnum.values());
        model.addAttribute("memberList", memberUserService.queryList(new MemberUser()));
        //分类
        List<MemberCatalogue> cataloguesLabels = catalogService.queryCataData();
        model.addAttribute("catalogList", cataloguesLabels);

        return "member/mediafileAudit";
    }

    @ResponseBody
    @RequestMapping(value={"/getStaticData"},method = {RequestMethod.POST})
    public Result getRelationFiles(HttpServletRequest request, HttpServletResponse response) {
        String uids = request.getParameter("uid");
        Long uid = Long.valueOf(uids);
        //2 归属标签
        MemberBelongLabel memberBelongLabel = new MemberBelongLabel();
        memberBelongLabel.setUid(uid);
        List<MemberBelongLabel> belongLabels = memBelongLabService.queryList(memberBelongLabel);
        //3 价格
        MemberPriceLabel memberPriceLabel = new MemberPriceLabel();
        memberPriceLabel.setUid(uid);
        List<MemberPriceLabel> pricesLabels = memPriceLabService.queryList(memberPriceLabel);
        //4 转载
        MemberPublishLabel memberPublishLabel = new MemberPublishLabel();
        memberPublishLabel.setUid(uid);
        List<MemberPublishLabel> publishLabels = memPubLabelService.queryList(memberPublishLabel);

        return Result.success()
                .addExtra("belongLabels", belongLabels)
                .addExtra("pricesLabels",pricesLabels)
                .addExtra("publishLabels", publishLabels);
    }

    @ResponseBody
    @GetMapping("/listAudit")
    @RequiresPermissions("auditfile:view")
    public Result<List<MediaFileInfoVo>> queryAuditList(MemberMediaFile memberMedia, PageQuery pageQuery,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {

        if (pageQuery.getPageSizeKey() <= 0) {
            pageQuery.setPageSizeKey(10);
        }
        if (pageQuery.getPageNumKey() <= 0) {
            pageQuery.setPageNumKey(1);
        }

        Example example = new Example(MemberMediaFile.class);
        String goodsStatus = request.getParameter("goodsStatus");
        String mediaTypes = request.getParameter("mediaTypes");
        String uid = request.getParameter("uid");
        String catalogId=request.getParameter("catalogId");

          Example.Criteria criteria = example.createCriteria();
          criteria.andEqualTo("isMediaInfo",MediaStatusEnum.YES);
          criteria.andEqualTo("mediaStatus",AuditStatusEnum.AUDITING);

          if(StringUtils.isNotBlank(uid) && !"-1".equals(uid)){
              criteria.andEqualTo("uid", uid);
          }

          if(StringUtils.isNotBlank(catalogId)){
            criteria.andEqualTo("catalogueId", catalogId);
           }

          if (org.apache.commons.lang3.StringUtils.isNotBlank(goodsStatus)) {
                criteria.andEqualTo("goodsStatus", GoodsStatusEnum.getEnumByName(goodsStatus));
            }
          if (org.apache.commons.lang3.StringUtils.isNotBlank(mediaTypes)) {
                String[] types = mediaTypes.split(",");
                if (types.length > 1) {
                    List<MediaTypeEnum> mediaTypeEnumList = new ArrayList<MediaTypeEnum>();
                    for (String type : types) {
                        mediaTypeEnumList.add(MediaTypeEnum.getEnumByName(type));
                    }
                    Example.Criteria criteria12 = example.createCriteria();
                    criteria.andIn("mediaType", mediaTypeEnumList);
                } else {
                    criteria.andEqualTo("mediaType", MediaTypeEnum.getEnumByName(types[0]));
                }
            }

            Page<MemberMediaFile> mediaFilePage = (Page<MemberMediaFile>) mediaFileService.queryList(pageQuery, example);
            List<MediaFileInfoVo> mediaFileInfoVos = new ArrayList<>();
            for(MemberMediaFile file:mediaFilePage.getResult()){

                MediaFileInfoVo fileInfoVo = new MediaFileInfoVo(file);
                if (MediaStatusEnum.YES.getName().equals(file.getIsMediaInfo().getName())) {
                    MemberMedia media = memberMediaService.queryOne(new MemberMedia().setMediaFileId(file.getId()));
                    MediaVo mediaVo = new MediaVo();
                    if(media!=null){
                        BeanUtils.copyProperties(media, mediaVo);
                    }else{
                        continue;
                    }

                    mediaVo.setBelongLabel(belongLabService.queryByIds(StringUtils.trans2long(media.getBelongLabelid(), ",")));
                    List<String> belabelList = new ArrayList<>();
                    for (int i = 0, s = mediaVo.getBelongLabel().size(); i < s; i++) {
                        MemberBelongLabel belongLabel = mediaVo.getBelongLabel().get(i);
                        belabelList.add(belongLabel.getLabel());
                    }
                    mediaVo.setBelongLabels(org.apache.commons.lang3.StringUtils.join(belabelList, ","));

                    mediaVo.setPriceLabel(priceLabService.queryByIds(StringUtils.trans2long(media.getPriceLabelid(), ",")));
                    mediaVo.setMemberPriceLabel(mediaVo.getPriceLabel().get(0));

                    mediaVo.setPublishLabel(pubLabelService.queryByIds(StringUtils.trans2long(media.getPublishLabelid(), ",")));
                    List<String> publabelList = new ArrayList<>();
                    for (int i = 0, s = mediaVo.getPublishLabel().size(); i < s; i++) {
                        MemberPublishLabel label = mediaVo.getPublishLabel().get(i);
                        publabelList.add(label.getLabel());
                    }
                    mediaVo.setPubLabels(org.apache.commons.lang3.StringUtils.join(publabelList, ","));

                    fileInfoVo.setMemberMedia(mediaVo);
                }else{
                    fileInfoVo.setMemberMedia(new MediaVo());
                    continue;
                }
                mediaFileInfoVos.add(fileInfoVo);
            }

        return Result.success(mediaFileInfoVos).addExtra("total", mediaFileInfoVos.size());
    }


    @ResponseBody
    @PostMapping("/create")
    @SystemLog("归属标签创建")
    @Override
    public Result create(@Validated MemberMediaFile memberMedia) {
        mediaFileService.create(memberMedia);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    @SystemLog("归属标签更新")
    @Override
    public Result update(@Validated MemberMediaFile memberMedia) {
        mediaFileService.updateNotNull(memberMedia);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/delete-batch")
    @SystemLog("归属标签删除")
    @Override
    public Result deleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
        super.deleteBatchByIds(ids);
        return Result.success();
    }


    @ResponseBody
    @RequestMapping(value="/auditPass",method = {RequestMethod.POST,RequestMethod.GET})
    @RequiresPermissions("auditfile:view")
    public Result auditPass(AuditLogVo auditLogVo){

        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.queryOne(new User().setUsername(username));
        auditLogVo.setAuditUid(user.getId());
        auditLogVo.setBelongUid(user.getId());
        mediaFileService.auditPass(auditLogVo);
        return Result.success().addExtra("url","/admin/auditfile/listAudit2");
    }

    @ResponseBody
    @RequestMapping(value="/auditNoPass",method = {RequestMethod.POST,RequestMethod.GET})
    @RequiresPermissions("auditfile:view")
    public Result auditNoPass(AuditLogVo auditLogVo){
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.queryOne(new User().setUsername(username));
        auditLogVo.setAuditUid(user.getId());
        auditLogVo.setBelongUid(user.getId());
        mediaFileService.auditNoPass(auditLogVo);
        return Result.success().addExtra("url","/admin/auditfile/listAudit2");
    }

    @ResponseBody
    @GetMapping("/listAudit2")
    @RequiresPermissions("auditfile:view")
    public Result<List<MemberMediaPojo>> queryAuditList(PageQuery pageQuery,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {

        if (pageQuery.getPageSizeKey() <= 0) {
            pageQuery.setPageSizeKey(10);
        }
        if (pageQuery.getPageNumKey() <= 0) {
            pageQuery.setPageNumKey(1);
        }

        String mediaTypes = request.getParameter("mediaTypes");
        String uid = request.getParameter("uid");
        String uname = request.getParameter("uname");
        String catalogId=request.getParameter("catalogId");
        String auditStatus=request.getParameter("auditStatus");

        MemberMediaQueryParams params=new MemberMediaQueryParams();

        if(StringUtils.isNotEmpty(auditStatus) && !AuditStatusEnum.ALL.getName().equalsIgnoreCase(auditStatus)){
            params.setMediaStatus(auditStatus);
        }

        params.setIsMediaInfo(MediaStatusEnum.YES.getName());
        //params.setChangcodeflag(1);//只查转码完成的

        if(StringUtils.isNotBlank(uid) && !"-1".equals(uid)){
            params.setUid(Long.parseLong(uid));
        }

        if(StringUtils.isNotBlank(catalogId) && !"-1".equals(catalogId)){
            params.setCatalogueId(Long.parseLong(catalogId));
        }


        if (StringUtils.isNotEmpty(mediaTypes) && !"all".equalsIgnoreCase(mediaTypes)) {
            params.setMediaType(mediaTypes.split(","));
        }

        if (StringUtils.isNotEmpty(uname)){
            params.setUname(uname);
        }

        Page<MemberMediaPojo> mediaFilePage = (Page<MemberMediaPojo>)mediaFileService.queryMemberMediaPageList(pageQuery,params);

//        for(int i=0;i<mediaFilePage.getResult().size();i++){
//            MemberMediaPojo pojo=mediaFilePage.getResult().get(i);
//            if(MediaTypeEnum.IMAGETEXT.getName().equalsIgnoreCase(pojo.getMediaType())){
//                List<JmgoMemberMediaDocImgfiles> docImgfilesList=mediaDocImgService.queryList(new JmgoMemberMediaDocImgfiles().setDocid(pojo.getId()));
//                List<String> docImage=new ArrayList<String>();
//                for(JmgoMemberMediaDocImgfiles imgfiles:docImgfilesList){
//                   docImage.add(imgfiles.getImagePath());
//                 }
//                pojo.setImagePath(org.apache.commons.lang3.StringUtils.join(docImage,","));
//            }
//
//        }

        return Result.success(mediaFilePage.getResult()).addExtra("total", mediaFilePage.getTotal());

    }

}
