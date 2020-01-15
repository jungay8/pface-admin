package com.pface.admin.modules.front.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.Result;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.member.enums.AuditStatusEnum;
import com.pface.admin.modules.member.po.MemberPublishLabel;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.MemPubLabelService;
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
@RequestMapping("/front/publishlab")
public class FrontPubLabController extends BaseCrudController<MemberPublishLabel> {

    @Autowired
    private MemPubLabelService pubLabelService;

    @ModelAttribute("userinfo")
    public MemberUser getUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        MemberUser user = (MemberUser)session.getAttribute(MemberUser.USER_SESSION_KEY);
        return user;
    }

    @ResponseBody
    @PostMapping("/list")
    @Override
    public Result<List<MemberPublishLabel>> queryList(MemberPublishLabel publishlab, PageQuery pageQuery) {
        Page<MemberPublishLabel> page = (Page<MemberPublishLabel>)pubLabelService .queryList(publishlab, pageQuery);

        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @PostMapping("/createLabel")
    @SystemLog("转载标签创建")
    public Result create(@Validated MemberPublishLabel publishlab,MemberUser user,Model model) {
        user=(MemberUser)model.asMap().get("userinfo");
        publishlab.setLabelStatus(AuditStatusEnum.AUDITED);
        publishlab.setDelFlag(0);
        publishlab.setUid(user.getId());
        publishlab.setUname(user.getUname());
        publishlab.setOpDate(new Date());
        publishlab.setId(null);
        pubLabelService.create(publishlab);
        return Result.success().addExtra("url","/front/publab");
    }

    @ResponseBody
    @PostMapping("/update")
    @SystemLog("转载标签更新")
    @Override
    public Result update(@Validated MemberPublishLabel publishlab) {
        publishlab.setLabelStatus(AuditStatusEnum.AUDITED);
        pubLabelService.updateNotNull(publishlab);
        return Result.success().addExtra("url","/front/publab");
    }

    @ResponseBody
    @PostMapping("/logic-delete-batch")
    @ApiOperation("删除多条数据")
    public Result logicDeleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
        Long[] vehicleIds =Arrays.stream(ids).map(p -> Long.valueOf(p.toString())).toArray(Long[]::new);
        pubLabelService.logicDeleteBatchByIds(vehicleIds);
        return Result.success();
    }

}
