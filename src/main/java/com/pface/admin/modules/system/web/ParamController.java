package com.pface.admin.modules.system.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.Result;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.system.po.Param;
import com.pface.admin.modules.system.po.Role;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.service.ParamService;
import com.pface.admin.modules.system.service.ResourceService;
import com.pface.admin.modules.system.service.RoleService;
import com.pface.admin.modules.system.vo.UserVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cjbi,daniel.liu
 */
@Controller
@RequestMapping("/admin/param")
public class ParamController extends BaseCrudController<Param> {

    @Autowired
    private ParamService paramService;

    @GetMapping
    @RequiresPermissions("param:view")
    public String paramPage(Model model) {
        //model.addAttribute("resourceList", resourceService.queryAll());
        return "system/param";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("param:view")
    @Override
    public Result<List<Param>> queryList(Param param, PageQuery pageQuery) {
        Page<Param> page = (Page<Param>) paramService.queryList(param, pageQuery);
        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @RequiresPermissions("param:create")
    @SystemLog("创建参数")
    @PostMapping("/create")
    @Override
    public Result create(@Validated Param param) {
        paramService.create(param);
        return Result.success();
    }

    @ResponseBody
    @RequiresPermissions("param:update")
    @SystemLog("更新参数")
    @PostMapping("/update")
    @Override
    public Result update(@Validated Param param) {
        paramService.updateNotNull(param);
        return Result.success();
    }

    @ResponseBody
    @RequiresPermissions("param:delete")
    @SystemLog("删除参数")
    @PostMapping("/delete-batch")
    @Override
    public Result deleteBatchByIds(@NotNull @RequestParam("id") Object[] ids) {
        super.deleteBatchByIds(ids);
        return Result.success();
    }

}
