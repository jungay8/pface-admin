package com.pface.admin.modules.member.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.Result;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.member.enums.MemberTypeEnum;
import com.pface.admin.modules.member.po.MemberCert;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.MemberCertService;
import com.pface.admin.modules.member.service.MemberUserService;
import com.pface.admin.modules.member.vo.UserListVo;
import com.pface.admin.modules.system.enums.GroupType;
import com.pface.admin.modules.system.po.Group;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.service.UserService;
import com.pface.admin.modules.system.vo.UserVO;
import org.apache.commons.codec.digest.DigestUtils;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author cjbi,daniel.liu
 */
@Controller
@RequestMapping("/admin/member")
public class MemberUserController extends BaseCrudController<MemberUser> {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private MemberUserService memberService;

    @Autowired
    private MemberCertService certService;
    @Autowired
    private UserService userService;

    @GetMapping
    @RequiresPermissions("member:view")
    public String memberPage(Model model) {
        model.addAttribute("memberTypeList", MemberTypeEnum.values());
        return "member/member";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("member:view")
    @Override
    public Result<List<MemberUser>> queryList(MemberUser memberUser, PageQuery pageQuery) {
//        String userType=request.getParameter("userType");
        MemberUser memberUser_cond = new MemberUser();
//        String searchName=request.getParameter("searchName");
//        if (StringUtils.isNotEmpty(searchName)){
//            memberUser_cond.setUname(searchName);
//        }

//        UserListVo vo=new UserListVo();
//        if((StringUtils.isNotEmpty(searchName)){
//            vo.setUname(searchName);
//        }
//
//        Page<UserListVo> page =(Page<UserListVo>) userService.queryFrontUsers(pageQuery,vo);
//        return Result.success(page.getResult()).addExtra("total", page.getTotal());

//        Page<MemberUser> page = (Page<MemberUser>) memberService.queryList(memberUser, pageQuery);

        //使用条件查询，灵活度高
        Condition condition = new Condition(MemberUser.class);
        condition.orderBy("opDate").desc();
        Example.Criteria criteria = condition.createCriteria();
        if(StringUtils.isNotBlank(memberUser.getUname())){
            criteria.andLike("uname", '%' + memberUser.getUname() + "%");
        }

        Page<MemberUser> page3 =(Page<MemberUser>) memberService.queryList(pageQuery,condition);

        return Result.success(page3.getResult()).addExtra("total", page3.getTotal());
    }

    @ResponseBody
    @PostMapping("/create")
    @SystemLog("会员管理创建")
    @Override
    public Result create(@Validated MemberUser memberUser) {
        memberService.create(memberUser);
        applicationEventPublisher.publishEvent(memberUser);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    @SystemLog("会员管理更")
    @Override
    public Result update(MemberUser memberUser) {
        if (StringUtils.isNotEmpty(memberUser.getPwd().trim())){
            memberUser.setPwd(DigestUtils.md5Hex(memberUser.getPwd()));//前端会员加密算法
        }else{
            memberUser.setPwd(null);
        }
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






}
