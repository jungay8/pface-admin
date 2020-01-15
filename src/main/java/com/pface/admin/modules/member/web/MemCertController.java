package com.pface.admin.modules.member.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.Result;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.member.enums.CertTypeEnum;
import com.pface.admin.modules.member.enums.MemberTypeEnum;
import com.pface.admin.modules.member.po.MemberAuditLog;
import com.pface.admin.modules.member.po.MemberCert;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.MemAuditLogService;
import com.pface.admin.modules.member.service.MemberCertService;
import com.pface.admin.modules.member.service.MemberUserService;
import com.pface.admin.modules.member.vo.AuditLogVo;
import com.pface.admin.modules.member.vo.UserCertVo;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author daniel.liu
 */
@Controller
@RequestMapping("/admin/cert")
public class MemCertController extends BaseCrudController<MemberCert> {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private UserService userService;

    @Autowired
    private MemberCertService certService;

    @Autowired
    private MemberUserService memberService;

    @Autowired
    private MemAuditLogService memAuditLogService;

    @Value("${jmgo.synchronizeUser}")
    private boolean synchronizeUser;

    @GetMapping
    @RequiresPermissions("cert:view")
    public String memberPage(Model model) {
        model.addAttribute("memberTypeList", MemberTypeEnum.values());
        model.addAttribute("certTypeList",CertTypeEnum.values());
        return "member/memberAudit";
    }

    @ResponseBody
    @GetMapping("/listUser")
    @RequiresPermissions("cert:view")
    public Result<List<UserCertVo>> queryUserList( PageQuery pageQuery,HttpServletRequest request) {


        String memberType=request.getParameter("memberType");
        String isCert=request.getParameter("isCert");
        String uname=request.getParameter("uname");

        Example userExample=new Example(MemberUser.class);
        Example.Criteria userCriteria= userExample.createCriteria();
        if(StringUtils.isNotBlank(memberType) && !"ALL".equalsIgnoreCase(memberType)){
            userCriteria.andEqualTo("memberType",MemberTypeEnum.getEnumByName(memberType));
        }
        if(StringUtils.isNotBlank(isCert) && !"ALL".equalsIgnoreCase(isCert)){
            userCriteria.andEqualTo("isCert",CertTypeEnum.getEnumByName(isCert));
        }

        if(StringUtils.isNotBlank(uname)){
            userCriteria.andLike("uname","%"+uname+"%");
        }

        Page<MemberUser> page = (Page<MemberUser>) memberService.queryList(pageQuery,userExample);
        //memberService.queryList(pageQuery,);
        List<UserCertVo> userCertVoList=new ArrayList<UserCertVo>();
        for(MemberUser user:page.getResult()){
             UserCertVo userCertVo=new UserCertVo();
             userCertVo.setMemberUser(user);
             userCertVo.setMemberCert(certService.queryOne(new MemberCert().setUid(user.getId())));
             userCertVoList.add(userCertVo);

            Example example =new Example(MemberAuditLog.class);
            example.orderBy("auditDate").desc();
            Example.Criteria  criteria= example.createCriteria();
            criteria.andEqualTo("msgOriginId",user.getId());
            criteria.andEqualTo("belongUid",user.getId());
            criteria.andEqualTo("delFlag",0);
           List<MemberAuditLog> memberAuditLogList=memAuditLogService.queryListByExample(example);
           if(memberAuditLogList!=null && memberAuditLogList.size()>0){
               userCertVo.setLastedAuditMsg(memberAuditLogList.get(0).getAuditMsg());
           }
        }
        return Result.success(userCertVoList).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @RequestMapping(value="/auditPass",method = {RequestMethod.POST,RequestMethod.GET})
    @RequiresPermissions("cert:view")
    public Result auditPass(AuditLogVo auditLogVo){

        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.queryOne(new User().setUsername(username));
        auditLogVo.setAuditUid(user.getId());
        auditLogVo.setMsgOriginId(auditLogVo.getBelongUid());
        memberService.auditPass(auditLogVo);

        //屏蔽
//        if(synchronizeUser){
//            applicationEventPublisher.publishEvent(memberService.queryById(auditLogVo.getBelongUid()));
//        }

        return Result.success();
    }

    @ResponseBody
    @RequestMapping(value="/auditNoPass",method = {RequestMethod.POST,RequestMethod.GET})
    @RequiresPermissions("cert:view")
    public Result auditNoPass(AuditLogVo auditLogVo){

        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.queryOne(new User().setUsername(username));
        auditLogVo.setAuditUid(user.getId());
        auditLogVo.setMsgOriginId(auditLogVo.getBelongUid());
        memberService.auditNoPass(auditLogVo);

        return Result.success();
    }


    @ResponseBody
    @PostMapping("/cert/view")
    public Result viewCertByuid(@NotNull @RequestParam("uid") Long uid){
        return Result.success().setData(certService.queryOne(new MemberCert().setUid(uid)));
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("cert:view")
    @Override
    public Result<List<MemberCert>> queryList(MemberCert cert, PageQuery pageQuery) {
        Page<MemberCert> page = (Page<MemberCert>) certService.queryList(cert, pageQuery);
        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @PostMapping("/create")
    @SystemLog("归属标签创建")
    @Override
    public Result create(@Validated MemberCert cert) {
        certService.create(cert);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    @SystemLog("归属标签更新")
    @Override
    public Result update(@Validated MemberCert cert) {
        certService.updateNotNull(cert);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/delete-batch")
    @SystemLog("归属标签删除")
    @Override
    public Result deleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
        super.deleteBatchByIds(ids);
        return Result.success();
    }



}
