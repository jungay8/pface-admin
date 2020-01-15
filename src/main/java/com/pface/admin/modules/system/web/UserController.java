package com.pface.admin.modules.system.web;

import com.github.pagehelper.Page;
import com.pface.admin.modules.system.service.*;
import com.pface.admin.modules.system.vo.UserUpdateVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.Result;
import com.pface.admin.core.utils.ResultCodeEnum;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.system.po.Group;
import com.pface.admin.modules.system.po.Organization;
import com.pface.admin.modules.system.po.Role;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.vo.UserVO;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author cjbi,daniel.liu
 */
@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseCrudController<User> {

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private PasswordHelper passwordHelper;

    @GetMapping
    @RequiresPermissions("user:view")
    public String userPage(Model model) {
        model.addAttribute("organizationList", organizationService.queryAll());
        model.addAttribute("roleList", roleService.queryAll());
        model.addAttribute("groupList", groupService.queryAll());
        return "system/user";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("user:view")
    @Override
    public Result<List<UserVO>> queryList(User user, PageQuery pageQuery) {
        Page<User> page = (Page<User>) userService.queryList(user, pageQuery);
        List<UserVO> userVOS = new ArrayList<>();
        page.forEach(u -> {
            UserVO userVO = new UserVO(u);
            userVO.setOrganizationName(getOrganizationName(Long.valueOf(userVO.getOrganizationId())));
            userVO.setRoleNames(getRoleNames(userVO.getRoleIdList()));
            userVO.setGroupNames(getGroupNames(userVO.getGroupIdList()));
            userVOS.add(userVO);
        });
        return Result.success(userVOS).addExtra("total", page.getTotal());
    }

    private String getGroupNames(Collection<Long> groupIds) {
        if (CollectionUtils.isEmpty(groupIds)) {
            return "";
        }

        StringBuilder s = new StringBuilder();
        for (Long groupId : groupIds) {
            Group role = groupService.queryById(groupId);
            if (role != null) {
                s.append(role.getName());
                s.append(",");
            }
        }

        if (s.length() > 0) {
            s.deleteCharAt(s.length() - 1);
        }

        return s.toString();
    }


    private String getRoleNames(Collection<Long> groupIds) {
        if (CollectionUtils.isEmpty(groupIds)) {
            return "";
        }

        StringBuilder s = new StringBuilder();
        for (Long roleId : groupIds) {
            Role role = roleService.queryById(roleId);
            if (role != null) {
                s.append(role.getDescription());
                s.append(",");
            }
        }

        if (s.length() > 0) {
            s.deleteCharAt(s.length() - 1);
        }

        return s.toString();
    }

    private String getOrganizationName(Long organizationId) {
        Organization organization = organizationService.queryOne(new Organization().setId(organizationId));
        if (organization == null) {
            return "";
        }
        return organization.getName();
    }

    @ResponseBody
    @PostMapping("/create")
    @RequiresPermissions("user:create")
    @SystemLog("用户管理创建用户")
    @Override
    public Result create(@Validated(User.UserCreateChecks.class) User user) {
        userService.createUser(user);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    @RequiresPermissions("user:update")
    @SystemLog("用户管理更新用户")
    @Override
    public Result update(@Validated(User.UserUpdateChecks.class) User user) {

        if(StringUtils.isBlank(user.getOldpassword())){
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("旧密码必须填写");
        }
        User u = userService.queryOne(new User().setId(user.getId()));
        if(u==null){
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("修改用户不存在");
        }
        String encrtyOldPwd=passwordHelper.encryptPassword(u.getUsername(),user.getOldpassword(),u.getSalt());
        if(!encrtyOldPwd.equals(u.getPassword())){
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("旧密码不对");
        }
        String encrtyNewPwd=passwordHelper.encryptPassword(u.getUsername(),user.getPassword(),u.getSalt());
        user.setPassword(encrtyNewPwd);
        userService.updateNotNull(user);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/delete-batch")
    @RequiresPermissions("user:delete")
    @SystemLog("用户管理删除用户")
    @Override
    public Result deleteBatchByIds(@NotNull @RequestParam("id") Object[] ids) {
        // 当前用户
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.queryOne(new User().setUsername(username));
        boolean isSelf = Arrays.stream(ids).anyMatch(id -> id.equals(user.getId()));
        if (isSelf) {
            return Result.failure(ResultCodeEnum.FAILED_DEL_OWN);
        }
        super.deleteBatchByIds(ids);
        return Result.success();
    }

    @ResponseBody
    @RequiresPermissions("user:update")
    @PostMapping("/{id}/change/password")
    @SystemLog("用户管理更改用户密码")
    public Result changePassword(@PathVariable("id") Long id, String newPassword) {
        userService.changePassword(id, newPassword);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/change/password")
    @SystemLog("用户管理更改用户密码")
    public Result changePassword(UserUpdateVo user) {
        if(StringUtils.isBlank(user.getOldpassword())){
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("旧密码必须填写");
        }
       /* if(user.getId()==null){
            String username = (String) SecurityUtils.getSubject().getPrincipal();
            User user2 = userService.queryOne(new User().setUsername(username));
        }*/
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User u = userService.queryOne(new User().setUsername(username));
        //User u = userService.queryOne(new User().setId(user.getId()));
        if(u==null){
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("修改用户不存在");
        }
        String encrtyOldPwd=passwordHelper.encryptPassword(u.getUsername(),user.getOldpassword(),u.getSalt());
        if(!encrtyOldPwd.equals(u.getPassword())){
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("旧密码不对");
        }
       // String encrtyNewPwd=passwordHelper.encryptPassword(u.getUsername(),user.getPassword(),u.getSalt());
       // user.setPassword(encrtyNewPwd);
        userService.changePassword(u.getId(),user.getPassword());
        return Result.success();
    }

}
