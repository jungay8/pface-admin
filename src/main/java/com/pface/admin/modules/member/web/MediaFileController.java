package com.pface.admin.modules.member.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.Result;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.front.vo.MediaFileInfoVo;
import com.pface.admin.modules.front.vo.MediaVo;
import com.pface.admin.modules.member.enums.*;
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
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author daniel.liu
 */
@Controller
@RequestMapping("/admin/mediafile")
@Api
public class MediaFileController extends BaseCrudController<MemberMediaFile> {

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


    @GetMapping
    @RequiresPermissions("mediafile:view")
    public String memberPage(Model model) {

        model.addAttribute("mediaTypes", MediaTypeEnum.values());
        model.addAttribute("memberList", memberUserService.queryList(new MemberUser()));
        return "member/mediafile";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("mediafile:view")
    @Override
    public Result<List<MediaFileInfoVo>> queryList(MemberMediaFile memberMedia, PageQuery pageQuery) {

        Page<MemberMediaFile> page = (Page<MemberMediaFile>) mediaFileService.queryList(memberMedia, pageQuery);
        List<MediaFileInfoVo> mediaFileInfoVos = new ArrayList<>();
        page.forEach(file -> {

            MediaFileInfoVo fileInfoVo = new MediaFileInfoVo(file);
            //String[] blabs=media.getBelongLabelid().split(",");
            if(MediaStatusEnum.YES.getName().equals(file.getIsMediaInfo().getName())) {
                MemberMedia media= memberMediaService.queryOne(new MemberMedia().setMediaFileId(file.getId()));
                MediaVo mediaVo=new MediaVo();
                BeanUtils.copyProperties(media,mediaVo);
                mediaVo.setBelongLabel(belongLabService.queryByIds(StringUtils.trans2long(media.getBelongLabelid(), ",")));
                mediaVo.setPriceLabel(priceLabService.queryByIds(StringUtils.trans2long(media.getPriceLabelid(), ",")));
                mediaVo.setPublishLabel(pubLabelService.queryByIds(StringUtils.trans2long(media.getPublishLabelid(), ",")));
                fileInfoVo.setMemberMedia(mediaVo);
            }
            mediaFileInfoVos.add(fileInfoVo);
        });
        return Result.success(mediaFileInfoVos).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @GetMapping("/listAudit")
    @RequiresPermissions("mediafile:view")
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

          if(StringUtils.isNotBlank(uid)){
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
    @GetMapping("/auditPass/{id}")
    @RequiresPermissions("mediafile:view")
    public Result auditPass(@NotNull @PathVariable("id") Long id){

        MemberMediaFile mediaFile=new MemberMediaFile();
        mediaFile.setId(id);
        mediaFile.setMediaStatus(AuditStatusEnum.AUDITED);
        mediaFile.setOpDate(new Date());
        mediaFileService.updateNotNull(mediaFile);
        return Result.success();
    }

    @ResponseBody
    @GetMapping("/auditNoPass")
    @RequiresPermissions("cert:view")
    public Result auditNoPass(AuditLogVo auditLogVo){
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.queryOne(new User().setUsername(username));
        auditLogVo.setAuditUid(user.getId());
        mediaFileService.auditNoPass(auditLogVo);
        return Result.success();
    }

}
