package com.pface.admin.modules.front.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.Result;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.member.po.MemberCatalogue;
import com.pface.admin.modules.member.service.MemCatalogService;
import com.pface.admin.modules.system.dto.TreeDto;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.weekend.Weekend;

import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
@RequestMapping("/front/catalogue")
public class FrontCatalogController extends BaseCrudController<MemberCatalogue> {

    @Autowired
    private MemCatalogService catalogService;


    @ResponseBody
    @PostMapping("/tree")
    public List<TreeDto> findOrgTree(Long pId) {
        return catalogService.queryCataTree(pId);
    }

    @ResponseBody
    @PostMapping("/list")
    @Override
    public Result<List<MemberCatalogue>> queryList(MemberCatalogue catalogue, PageQuery pageQuery) {
        catalogue.setLeaf(true);
        pageQuery.setOrderBy("priority");
        Page<MemberCatalogue> page = (Page<MemberCatalogue>)catalogService .queryList(catalogue, pageQuery);

        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }


    @ResponseBody
    @PostMapping("/listAll")
    public Result<List<MemberCatalogue>> queryListAll() {

        MemberCatalogue catalogue=new MemberCatalogue();
        catalogue.setLeaf(true);
        //pageQuery.setOrderBy("priority");

        return Result.success( catalogService.queryList(catalogue));
    }

}
