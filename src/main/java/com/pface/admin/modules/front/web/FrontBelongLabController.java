package com.pface.admin.modules.front.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.Result;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.member.enums.AuditStatusEnum;
import com.pface.admin.modules.member.po.MemberBelongLabel;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.MemBelongLabService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author cjbi,daniel.liu
 */
@Controller
@RequestMapping("/front/belonglab")
public class FrontBelongLabController extends BaseCrudController<MemberBelongLabel> {

    @Autowired
    private MemBelongLabService belongLabService;

    @ModelAttribute("userinfo")
    public MemberUser getUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        MemberUser user = (MemberUser)session.getAttribute(MemberUser.USER_SESSION_KEY);
        return user;
    }

    @ResponseBody
    @GetMapping("/list")
    @Override
    public Result<List<MemberBelongLabel>> queryList(MemberBelongLabel belongLabel, PageQuery pageQuery) {
        Page<MemberBelongLabel> page = (Page<MemberBelongLabel>) belongLabService.queryList(belongLabel, pageQuery);

        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @PostMapping("/createLabel")
    @SystemLog("归属标签创建")
    public Result create(@Validated MemberBelongLabel belongLabel,MemberUser user,Model model) {
        user=(MemberUser)model.asMap().get("userinfo");
        belongLabel.setLabelStatus(AuditStatusEnum.AUDITED);
        belongLabel.setDelFlag(0);
        belongLabel.setUid(user.getId());
        belongLabel.setUname(user.getUname());
        belongLabel.setOpDate(new Date());
        belongLabel.setId(null);
        belongLabService.create(belongLabel);
        return Result.success().addExtra("url","/front/attribution");
    }

    @ResponseBody
    @PostMapping("/update")
    @SystemLog("归属标签更新")
    @Override
    public Result update(@Validated MemberBelongLabel belongLabel) {
        belongLabel.setLabelStatus(AuditStatusEnum.AUDITED);
        belongLabService.updateNotNull(belongLabel);
        return Result.success().addExtra("url","/front/attribution");
    }

    @ResponseBody
    @PostMapping("/logic-delete-batch")
    @ApiOperation("删除多条数据")
    public Result logicDeleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
        Long[] vehicleIds =Arrays.stream(ids).map(p -> Long.valueOf(p.toString())).toArray(Long[]::new);
        belongLabService.logicDeleteBatchByIds(vehicleIds);
        return Result.success();
    }

}
