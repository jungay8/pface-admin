package com.pface.admin.modules.member.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.exception.BizException;
import com.pface.admin.core.utils.Result;
import com.pface.admin.core.utils.ResultCodeEnum;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.member.po.FaceSensebox;
import com.pface.admin.modules.member.service.FaceCommonService;
import com.pface.admin.modules.member.service.FaceSenseboxService;
import com.pface.admin.modules.system.service.UserService;
import io.swagger.annotations.ApiOperation;
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
import java.util.List;

@Controller
@RequestMapping("/admin/facesensebox")
public class FaceSenceboxController  extends BaseCrudController<FaceSensebox> {

    @Autowired
    private FaceSenseboxService faceSenseboxService;
    @Autowired
    private UserService userService;
    @Autowired
    private FaceCommonService faceCommonService;
    @GetMapping
    @RequiresPermissions("facesensebox:view")
    public String facesenseboxPage(Model model) {
        return "member/facesensebox";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("facesensebox:view")
    @Override
    public Result<List<FaceSensebox>> queryList(FaceSensebox faceSensebox, PageQuery pageQuery) {

        //使用条件查询，灵活度高
        Condition condition = new Condition(FaceSensebox.class);
        condition.orderBy("updateDate").desc();
        Example.Criteria criteria = condition.createCriteria();
        if(StringUtils.isNotEmpty(faceSensebox.getDeviceId())){
            criteria.andLike("deviceId", "%"+faceSensebox.getDeviceId() + "%"); //注意：%后不要有单引号
        }
        criteria.andEqualTo("delFlag", "1");

        Page<FaceSensebox> page =(Page<FaceSensebox>) faceSenseboxService.queryList(pageQuery,condition);

        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @PostMapping("/create")
    @Override
    public Result create(@Validated FaceSensebox faceSensebox) {

        FaceSensebox faceSensebox_cond = new FaceSensebox();
        faceSensebox_cond.setIp(faceSensebox.getIp());
        faceSensebox_cond.setPort(faceSensebox.getPort());
        faceSensebox_cond.setDelFlag("1");
        FaceSensebox oneFaceSensebox = faceSenseboxService.queryOne(faceSensebox_cond);
        if (oneFaceSensebox!=null) {  //重复添加设备，看做修改
            return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("ip:"+faceSensebox.getIp() + "，端口："+faceSensebox.getPort()+"的设备已经存在！");
        }

        try {
            faceSenseboxService.pullSenseboxInfo(faceSensebox);
        }catch (BizException ex){
            return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg(ex.getMsg());
        }


        FaceSensebox faceSensebox_cond2 = new FaceSensebox();
        faceSensebox_cond2.setDeviceId(faceSensebox.getDeviceId());
        faceSensebox_cond2.setDelFlag("1");
        FaceSensebox oneFaceSensebox2 = faceSenseboxService.queryOne(faceSensebox_cond2);
        if (oneFaceSensebox2!=null) {  //重复的deviceId
            return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("设备ID："+faceSensebox.getDeviceId()
                    +"已在系统中存在，请修改智能盒子（ip："+faceSensebox.getIp()+",端口："+faceSensebox.getPort()+"）中设备ID的配置！");
        }

        faceSenseboxService.myCreate(faceSensebox);
//        faceSenseboxService.checkdeviceid(faceSensebox);
        return Result.success();
    }


    @ResponseBody
    @PostMapping("/update")
    @Override
    public Result update(FaceSensebox faceSensebox) {
        String old_deviceId = faceSensebox.getDeviceId();
        faceSenseboxService.pullSenseboxInfo(faceSensebox);
        String new_deviceId = faceSensebox.getDeviceId();
        if (!old_deviceId.equals(new_deviceId)){
            FaceSensebox faceSensebox_cond2 = new FaceSensebox();
            faceSensebox_cond2.setDeviceId(new_deviceId);
            FaceSensebox oneFaceSensebox2 = faceSenseboxService.queryOne(faceSensebox_cond2);
            if (oneFaceSensebox2!=null) {  //重复的deviceId
                return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("设备ID："+faceSensebox.getDeviceId()
                        +"已在系统中存在，请修改智能盒子（ip："+faceSensebox.getIp()+",端口："+faceSensebox.getPort()+"）中设备ID的配置！");
            }
        }

        faceSensebox = faceCommonService.setCommonField(faceSensebox, "update");
        faceSenseboxService.updateNotNull(faceSensebox);
//        faceSenseboxService.checkdeviceid(faceSensebox);
        return Result.success();

    }


    @ResponseBody
    @PostMapping("/logic-delete-batch")
    @ApiOperation("逻辑删除客户数据")
    public Result logicDeleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
        Long[] boxIds = Arrays.stream(ids).map(p -> Long.valueOf(p.toString())).toArray(Long[]::new);
        faceSenseboxService.logicDeleteBatchByIds(boxIds);
        return Result.success();
    }
}
