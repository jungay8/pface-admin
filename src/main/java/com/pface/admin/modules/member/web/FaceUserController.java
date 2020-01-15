package com.pface.admin.modules.member.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.core.utils.Result;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.jiekou.Constants.Faceconstant;
import com.pface.admin.modules.member.enums.MemberTypeEnum;
import com.pface.admin.modules.member.po.FaceUser;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.FaceUserService;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import com.pface.admin.core.utils.StringUtils;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/faceuser")
public class FaceUserController  extends BaseCrudController<FaceUser> {
    @Autowired
    private FaceUserService faceUserService;
    @Autowired
    private UserService userService;
    @GetMapping
    @RequiresPermissions("faceuser:view")
    public String faceuserPage(Model model) {
        return "member/faceuser";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("faceuser:view")
    @Override
    public Result<List<FaceUser>> queryList(FaceUser faceUser, PageQuery pageQuery) {

        //使用条件查询，灵活度高
        Condition condition = new Condition(FaceUser.class);
        condition.orderBy("updateDate").desc();
        Example.Criteria criteria = condition.createCriteria();
        if(StringUtils.isNotBlank(faceUser.getUsername())){
            criteria.andLike("username", '%' + faceUser.getUsername() + "%");
        }
        criteria.andEqualTo("delFlag", "1");

        Page<FaceUser> page =(Page<FaceUser>) faceUserService.queryList(pageQuery,condition);

        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }


    @ResponseBody
    @PostMapping("/create")
//    @SystemLog("face_user创建")
    @Override
    public Result create(@Validated FaceUser faceUser) {
        Date now = DateUtils.getNowDate();
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.queryOne(new User().setUsername(username));
        faceUser.setCreateDate(now);
        faceUser.setUpdateDate(now);
        if (user!=null && user.getLocked()!=null){
            faceUser.setCreateBy(user.getId().toString());
            faceUser.setUpdateBy(user.getId().toString());
        }

        faceUser.setPassword(DigestUtils.md5Hex(Faceconstant.faceuser_password_default));
        faceUserService.pushFaceUserData2app(faceUser);

        faceUserService.create(faceUser);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    @Override
    public Result update(FaceUser faceUser) {

        Date now = DateUtils.getNowDate();
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.queryOne(new User().setUsername(username));
        faceUser.setUpdateDate(now);
        if (user!=null && user.getLocked()!=null){
            faceUser.setUpdateBy(user.getId().toString());
        }

        if (faceUser.getPassword()!=null && StringUtils.isNotEmpty(faceUser.getPassword().trim())){
            faceUser.setPassword(DigestUtils.md5Hex(faceUser.getPassword()));//前端会员加密算法
        }else{
            faceUser.setPassword(null);
        }

        faceUserService.pushFaceUserData2app(faceUser);

        faceUserService.updateNotNull(faceUser);
        return Result.success();
    }


    @ResponseBody
    @PostMapping("/logic-delete-batch")
    @ApiOperation("逻辑删除客户数据")
    public Result logicDeleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
        Long[] vehicleIds =Arrays.stream(ids).map(p -> Long.valueOf(p.toString())).toArray(Long[]::new);
        faceUserService.logicDeleteBatchByIds(vehicleIds);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/delete-batch")
//    @SystemLog("物理删除客户数据")
    @Override
    public Result deleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
        super.deleteBatchByIds(ids); //物理删除
        return Result.success();
    }
}
