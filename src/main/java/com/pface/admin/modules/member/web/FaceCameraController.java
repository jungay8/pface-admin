package com.pface.admin.modules.member.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.core.utils.Result;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.member.po.FaceCamera;
import com.pface.admin.modules.member.po.FaceSensebox;
import com.pface.admin.modules.member.service.FaceCameraService;
import com.pface.admin.modules.member.service.FaceCommonService;
import com.pface.admin.modules.member.service.FaceSenseboxService;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/facecamera")
public class FaceCameraController  extends BaseCrudController<FaceCamera> {

    @Autowired
    private FaceSenseboxService faceSenseboxService;
    @Autowired
    private FaceCameraService faceCameraService;
    @Autowired
    private UserService userService;
    @Autowired
    private FaceCommonService faceCommonService;
    @GetMapping
    @RequiresPermissions("facecamera:view")
    public String facecameraboxPage(Model model) {
        List<FaceSensebox> list = faceSenseboxService.queryAll();
        model.addAttribute("senseboxalllist", list);
        return "member/facecamera";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("facecamera:view")
    @Override
    public Result<List<FaceCamera>> queryList(FaceCamera faceCamera, PageQuery pageQuery) {

        //使用条件查询，灵活度高
        Condition condition = new Condition(FaceCamera.class);
        condition.orderBy("deviceId").desc();
        condition.orderBy("updateDate").desc();
        Example.Criteria criteria = condition.createCriteria();
        if(StringUtils.isEmpty(faceCamera.getDeviceId())){
            criteria.andEqualTo("deviceId", faceCamera.getDeviceId());
        }
        criteria.andEqualTo("delFlag", "1");

        Page<FaceCamera> page =(Page<FaceCamera>) faceCameraService.queryList(pageQuery,condition);

        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @PostMapping("/create")
    @Override
    public Result create(@Validated FaceCamera faceCamera) {
//        Date now = DateUtils.getNowDate();
//        String username = (String) SecurityUtils.getSubject().getPrincipal();
//        User user = userService.queryOne(new User().setUsername(username));
//        faceCamera.setCreateDate(now);
//        faceCamera.setUpdateDate(now);
//        if (user!=null && user.getLocked()!=null){
//            faceCamera.setCreateBy(user.getId().toString());
//            faceCamera.setUpdateBy(user.getId().toString());
//        }
        faceCommonService.setCommonField(faceCamera, "create");
        faceCameraService.pullCameraInfoAndSave(faceCamera);

        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    @Override
    public Result update(FaceCamera faceCamera) {

        Date now = DateUtils.getNowDate();
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.queryOne(new User().setUsername(username));
        faceCamera.setUpdateDate(now);
        if (user!=null && user.getLocked()!=null){
            faceCamera.setUpdateBy(user.getId().toString());
        }

        faceCameraService.updateNotNull(faceCamera);
        return Result.success();
    }


    @ResponseBody
    @PostMapping("/logic-delete-batch")
    @ApiOperation("逻辑删除客户数据")
    public Result logicDeleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
        Long[] boxIds = Arrays.stream(ids).map(p -> Long.valueOf(p.toString())).toArray(Long[]::new);
        faceCameraService.logicDeleteBatchByIds(boxIds);
        return Result.success();
    }
}
