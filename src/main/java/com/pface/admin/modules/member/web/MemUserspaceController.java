package com.pface.admin.modules.member.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.utils.Result;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.member.po.MemberUserspace;
import com.pface.admin.modules.member.service.MemMediaFileService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin/userspace")
public class MemUserspaceController  {

    @Autowired
    private MemMediaFileService mediaFileService;

    @GetMapping
    @RequiresPermissions("userspace:view")
    public String memberPage(Model model) {
//        model.addAttribute("memberList", memberUserService.queryList(new MemberUser()));
        return "member/userspace";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("userspace:view")
//    @Override
    public Result<List<MemberUserspace>> queryList(MemberUserspace memberUserspace, PageQuery pageQuery) {
        Page<MemberUserspace> page = (Page<MemberUserspace>) mediaFileService.assembleUserspacePageList(pageQuery, memberUserspace);
        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }
}
