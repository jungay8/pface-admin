package com.pface.admin.modules.member.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.Result;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.member.enums.MemberTypeEnum;
import com.pface.admin.modules.member.po.MemberBelongLabel;
import com.pface.admin.modules.member.po.MemberMedia;
import com.pface.admin.modules.member.service.MemBelongLabService;
import com.pface.admin.modules.member.service.MemberMediaService;
import com.pface.admin.modules.member.service.impl.MemDiskServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author daniel.liu
 */
@Controller
@RequestMapping("/admin/media")
public class MemMediaController extends BaseCrudController<MemberMedia> {

    @Autowired
    private MemberMediaService mediaService;

    @Autowired
    private MemDiskServiceImpl memDiskServiceImpl;

    @GetMapping
    @RequiresPermissions("media:view")
    public String memberPage(Model model) {
        model.addAttribute("memberTypeList", MemberTypeEnum.values());
        return "member/belonglab";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("media:view")
    @Override
    public Result<List<MemberMedia>> queryList(MemberMedia memberMedia, PageQuery pageQuery) {
        Page<MemberMedia> page = (Page<MemberMedia>) mediaService.queryList(memberMedia, pageQuery);

        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @PostMapping("/create")
    @SystemLog("归属标签创建")
    @Override
    public Result create(@Validated MemberMedia memberMedia) {
        mediaService.create(memberMedia);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    @SystemLog("归属标签更新")
    @Override
    public Result update(@Validated MemberMedia memberMedia) {
        mediaService.updateNotNull(memberMedia);
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
