package com.pface.admin.modules.member.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.Result;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.front.vo.MediaFileInfoVo;
import com.pface.admin.modules.front.vo.MediaVo;
import com.pface.admin.modules.member.enums.AuditStatusEnum;
import com.pface.admin.modules.member.enums.MediaStatusEnum;
import com.pface.admin.modules.member.enums.MediaTypeEnum;
import com.pface.admin.modules.member.po.MemberAuditLog;
import com.pface.admin.modules.member.po.MemberMedia;
import com.pface.admin.modules.member.po.MemberMediaFile;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author daniel.liu
 */
@Controller
@RequestMapping("/admin/audit")
public class MemAuditLogController extends BaseCrudController<MemberAuditLog> {

    @Autowired
    private MemAuditLogService auditLogService;
    @Autowired
    private MemberUserService memberUserService;

    @Autowired
    private MemMediaFileService mediaFileService;

    @Autowired
    private MemberMediaService memberMediaService;

    @Autowired
    private MemBelongLabService belongLabService;

    @Autowired
    private MemPriceLabService priceLabService;

    @Autowired
    private MemPubLabelService pubLabelService;

    @GetMapping
    @RequiresPermissions("audit:view")
    public String memberPage(Model model) {
        model.addAttribute("mediaTypes", MediaTypeEnum.values());
        model.addAttribute("memberList", memberUserService.queryList(new MemberUser()));
        return "member/audit";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("audit:view")
    @Override
    public Result<List<MemberAuditLog>> queryList(MemberAuditLog audit, PageQuery pageQuery) {
        Page<MemberAuditLog> page = (Page<MemberAuditLog>) auditLogService.queryList(audit, pageQuery);

        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @GetMapping("/listGoods")
    @RequiresPermissions("audit:view")
    public Result<List<MediaFileInfoVo>> listGoods(MemberMediaFile memberMedia, PageQuery pageQuery) {

        memberMedia.setMediaStatus(AuditStatusEnum.AUDITING);
        Page<MemberMediaFile> page = (Page<MemberMediaFile>) mediaFileService.queryList(memberMedia, pageQuery);
        List<MediaFileInfoVo> mediaFileInfoVos = new ArrayList<>();
        page.forEach(file -> {
            MediaFileInfoVo fileInfoVo = new MediaFileInfoVo(file);
            if(MediaStatusEnum.YES==file.getIsMediaInfo()) {
                //有商品简介
                MemberMedia media= memberMediaService.queryOne(new MemberMedia().setMediaFileId(file.getId()));
                MediaVo mediaVo=new MediaVo();
                BeanUtils.copyProperties(media,mediaVo);
                mediaVo.setBelongLabel(belongLabService.queryByIds(StringUtils.trans2long(media.getBelongLabelid(), ",")));
                mediaVo.setPriceLabel(priceLabService.queryByIds(StringUtils.trans2long(media.getPriceLabelid(), ",")));
                mediaVo.setPublishLabel(pubLabelService.queryByIds(StringUtils.trans2long(media.getPublishLabelid(), ",")));
                fileInfoVo.setMemberMedia(mediaVo);
            }

          //if(AuditStatusEnum.)

            mediaFileInfoVos.add(fileInfoVo);
        });
        return Result.success(mediaFileInfoVos).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @PostMapping("/create")
    @SystemLog("归属标签创建")
    @Override
    public Result create(@Validated MemberAuditLog audit) {
        auditLogService.create(audit);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    @SystemLog("归属标签更新")
    @Override
    public Result update(@Validated MemberAuditLog audit) {
        auditLogService.updateNotNull(audit);
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

}
