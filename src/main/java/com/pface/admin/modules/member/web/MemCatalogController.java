package com.pface.admin.modules.member.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.core.utils.Result;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.member.po.MemberCatalogue;
import com.pface.admin.modules.member.po.MemberPublishLabel;
import com.pface.admin.modules.member.service.MemCatalogService;
import com.pface.admin.modules.system.dto.TreeDto;
import com.pface.admin.modules.system.po.Organization;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/catalogue")
public class MemCatalogController extends BaseCrudController<MemberCatalogue> {

    @Autowired
    private MemCatalogService catalogService;

    @GetMapping
    @RequiresPermissions("catalogue:view")
    public String cataloguePage() {
        Weekend example = Weekend.of(MemberCatalogue.class);
        example.setOrderByClause("priority");
        return "member/catalogue2";
    }

    @ResponseBody
    @RequestMapping("/tree")
    public List<TreeDto> findOrgTree(Long pId) {
        return catalogService.queryCataTree(pId);
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("catalogue:view")
    @Override
    public Result<List<MemberCatalogue>> queryList(MemberCatalogue catalogue, PageQuery pageQuery) {
//        catalogue.setLeaf(true);
//        pageQuery.setOrderBy("priority");
//        Page<MemberCatalogue> page = (Page<MemberCatalogue>)catalogService.queryList(catalogue, pageQuery);

        Condition condition = new Condition(MemberCatalogue.class);
        condition.orderBy("priority").asc();
        Example.Criteria criteria = condition.createCriteria();
        if (StringUtils.isNotEmpty(catalogue.getName())) {
            criteria.andLike("name", '%' + catalogue.getName() + "%");
        }
        criteria.andEqualTo("leaf", 1);//是叶子

        Page<MemberCatalogue> page = (Page<MemberCatalogue>)catalogService.queryList(pageQuery, condition);
        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @RequiresPermissions("catalogue:view")
    @RequestMapping(value = "{id}/load", method = RequestMethod.POST)
    public Result load(@PathVariable Long id) {
        MemberCatalogue catalogue = catalogService.queryOne(new MemberCatalogue().setId(id));
        return Result.success(catalogue);
    }

    @ResponseBody
    @RequiresPermissions("catalogue:create")
//    @SystemLog("分类栏目管理创建分类")
    @PostMapping("/create")
    @Override
    public Result create(MemberCatalogue catalogue) {
        catalogue.setLeaf(true);
        catalogue.setParentId(1L);
        catalogue.setParentIds("0/1/");
        catalogue.setAvailable(true);
        catalogue.setOpDate(DateUtils.getNowDate());

        catalogService.createCatalog(catalogue);
        return Result.success();
    }

    @ResponseBody
    @RequiresPermissions("catalogue:update")
//    @SystemLog("分类栏目管理更新分类")
    @PostMapping("/update")
    @Override
    public Result update(MemberCatalogue catalogue) {
        catalogue.setOpDate(DateUtils.getNowDate());
        catalogService.updateNotNull(catalogue);
        return Result.success();
    }

    @ResponseBody
    @RequiresPermissions("catalogue:delete")
//    @SystemLog("分类栏目管理删除分类")
    @PostMapping("/delete")
    @Override
    public Result delete(@NotNull @RequestParam("id") Object id) {
        super.delete(id);
        return Result.success();
    }

}
