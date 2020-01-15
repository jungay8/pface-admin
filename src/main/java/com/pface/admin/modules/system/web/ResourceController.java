package com.pface.admin.modules.system.web;

import com.github.pagehelper.PageHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.Result;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.system.enums.ResourceType;
import com.pface.admin.modules.system.po.Resource;
import com.pface.admin.modules.system.service.ResourceService;

/**
 * @author cjbi,daniel.liu
 */
@Controller
@RequestMapping("/admin/resource")
public class ResourceController extends BaseCrudController<Resource> {

    @Autowired
    private ResourceService resourceService;

    @ModelAttribute("types")
    public ResourceType[] resourceTypes() {
        return ResourceType.values();
    }

    @RequiresPermissions("resource:view")
    @GetMapping
    public String resourcePage(Model model) {
        PageHelper.orderBy("priority");
        model.addAttribute("resourceList", resourceService.queryAll());
        return "system/resource";
    }

    @ResponseBody
    @RequiresPermissions("resource:create")
    @SystemLog("资源管理创建资源")
    @PostMapping("/create")
    @Override
    public Result create(@Validated Resource resource) {
        resourceService.createResource(resource);
        return Result.success();
    }

    @ResponseBody
    @RequiresPermissions("resource:update")
    @SystemLog("资源管理更新资源")
    @PostMapping("/update")
    @Override
    public Result update(@Validated Resource resource) {
        resourceService.updateNotNull(resource);
        return Result.success();
    }

    @ResponseBody
    @RequiresPermissions("resource:delete")
    @SystemLog("资源管理删除资源")
    @PostMapping("/delete")
    @Override
    public Result delete(@RequestParam("id") Object id) {
        super.delete(id);
        return Result.success();
    }

}
