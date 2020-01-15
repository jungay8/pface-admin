package com.pface.admin.modules.member.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.DateUtil;
import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.core.utils.Result;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.jiekou.Constants.Faceconstant;
import com.pface.admin.modules.member.enums.MemberTypeEnum;
import com.pface.admin.modules.member.po.MemberCatalogue;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.MemberCertService;
import com.pface.admin.modules.member.service.MemberUserService;
import com.pface.admin.modules.member.vo.UserListVo;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.service.PasswordHelper;
import com.pface.admin.modules.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cjbi,daniel.liu
 */
@Controller
@RequestMapping("/admin/userlist")
public class UserListController extends BaseCrudController<MemberUser> {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private MemberUserService memberService;

    @Autowired
    private MemberCertService certService;

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordHelper passwordHelper;

    /////////////////////////////////////////////jmgo_sys_user start///////////////////////

    @GetMapping
    @RequiresPermissions("userlist:view")
    public String memberPage(Model model) {
//        model.addAttribute("memberTypeList", MemberTypeEnum.values());
        return "member/userlist";
    }

    @ResponseBody
    @GetMapping("/listPage")
    @RequiresPermissions("userlist:view")
    public Result<List<User>> queryList2(PageQuery pageQuery,HttpServletRequest request,
                                              HttpServletResponse response) {

//        String userType=request.getParameter("userType");
        String searchName=request.getParameter("searchName");
        //使用实体查询，不能实现模糊查询
//        User user_cond = new User();
//        if(StringUtils.isNotBlank(searchName)){
//            user_cond.setUsername(searchName);
//        }
//        user_cond.setRoleIds("5");
//        Page<User> page2 =(Page<User>) userService.queryList(user_cond, pageQuery);

        //使用条件查询，灵活度高
        Condition condition = new Condition(User.class);
        condition.orderBy("opDate").desc();
        Example.Criteria criteria = condition.createCriteria();
        if(StringUtils.isNotBlank(searchName)){
            criteria.andLike("username", '%' + searchName + "%");
        }
        criteria.andEqualTo("roleIds", Faceconstant.sysuser_roleids_default);
        Page<User> page3 =(Page<User>) userService.queryList(pageQuery,condition);
        return Result.success(page3.getResult()).addExtra("total", page3.getTotal());

//        UserListVo vo=new UserListVo();
//        if(StringUtils.isNotBlank(searchName)){
//            vo.setUname(searchName);
//        }
//        if("all".equalsIgnoreCase(userType)){
//            Page<UserListVo> page =(Page<UserListVo>) userService.queryAllUsers(pageQuery,vo);
//            return Result.success(page.getResult()).addExtra("total", page.getTotal());
//        }else if("back".equalsIgnoreCase(userType)){
//            Page<UserListVo> page =(Page<UserListVo>) userService.queryBackUsers(pageQuery,vo);
//            return Result.success(page.getResult()).addExtra("total", page.getTotal());
//        }else if("front".equalsIgnoreCase(userType)){
//            Page<UserListVo> page =(Page<UserListVo>) userService.queryFrontUsers(pageQuery,vo);
//            return Result.success(page.getResult()).addExtra("total", page.getTotal());
//        }else{
//            Page<UserListVo> page =(Page<UserListVo>) userService.queryBackUsers(pageQuery,vo);
//            return Result.success(page.getResult()).addExtra("total", page.getTotal());
//        }

    }


    @ResponseBody
    @PostMapping("/createUser")
//    @SystemLog("用户管理创建用户")
    public Result createUser(@Validated(User.UserCreateChecks.class) User user) {
//        user.setPassword(Faceconstant.sysuser_password_default);  //创建系统用户的默认密码
//        user.setOpDate(DateUtils.getNowDate());
        userService.createUser(user);
        return Result.success();
    }


    @ResponseBody
    @PostMapping("/updateUser")
//    @SystemLog("用户管理创建用户")
    public Result UpdateUser(@Validated(User.UserCreateChecks.class) User user) {
        userService.updateNotNull(user);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/deleteUser")
//    @SystemLog("会员管理管理删除")
//    @Override
    public Result deleteUser(@NotNull @RequestParam("id")  Object id) {
        userService.deleteById(id);
        return Result.success();
    }
    /////////////////////////////////////////////jmgo_sys_user end///////////////////////

    /////////////////////////////////////////////jmgo_member_user start///////////////////////
    @ResponseBody
    @PostMapping("/create")
//    @SystemLog("会员管理创建")
    @Override
    public Result create(@Validated MemberUser memberUser) {
        memberService.create(memberUser);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
//    @SystemLog("会员管理更")
    @Override
    public Result update(@Validated MemberUser memberUser) {
        memberService.updateNotNull(memberUser);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/delete-batch")
    @SystemLog("会员管理管理删除")
    @Override
    public Result deleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
        super.deleteBatchByIds(ids);
        return Result.success();
    }
    /////////////////////////////////////////////jmgo_member_user end///////////////////////

}
