package com.pface.admin.modules.front.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.Result;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.member.enums.AuditStatusEnum;
import com.pface.admin.modules.member.po.MemberPriceLabel;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.MemPriceLabService;
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
@RequestMapping("/front/pricelab")
public class FrontPriceLabController extends BaseCrudController<MemberPriceLabel> {

    @Autowired
    private MemPriceLabService memPriceLabService;

    @ModelAttribute("userinfo")
    public MemberUser getUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        MemberUser user = (MemberUser)session.getAttribute(MemberUser.USER_SESSION_KEY);
        return user;
    }

    @ResponseBody
    @PostMapping("/list")
    @Override
    public Result<List<MemberPriceLabel>> queryList(MemberPriceLabel priceLabel, PageQuery pageQuery) {
        Page<MemberPriceLabel> page = (Page<MemberPriceLabel>) memPriceLabService.queryList(priceLabel, pageQuery);

        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @PostMapping("/createLabel")
    @SystemLog("价格标签创建")
    public Result create(@Validated MemberPriceLabel priceLabel,MemberUser user,Model model) {
        user=(MemberUser)model.asMap().get("userinfo");
        priceLabel.setLabelStatus(AuditStatusEnum.AUDITED);
        priceLabel.setDelFlag(0);
        priceLabel.setUid(user.getId());
        priceLabel.setUname(user.getUname());
        priceLabel.setOpDate(new Date());
        priceLabel.setId(null);
        memPriceLabService.create(priceLabel);
        return Result.success().addExtra("url","/front/price");
    }

    @ResponseBody
    @PostMapping("/update")
    @SystemLog("价格标签更新")
    @Override
    public Result update(@Validated MemberPriceLabel priceLabel) {
        priceLabel.setLabelStatus(AuditStatusEnum.AUDITED);
        memPriceLabService.updateNotNull(priceLabel);
        return Result.success().addExtra("url","/front/price");
    }


    @ResponseBody
    @PostMapping("/logic-delete-batch")
    @SystemLog("价格标签删除")
    @ApiOperation("删除多条数据")
    public Result logicDeleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {

        Long[] vehicleIds =Arrays.stream(ids).map(p -> Long.valueOf(p.toString())).toArray(Long[]::new);
        memPriceLabService.logicDeleteBatchByIds(vehicleIds);
        return Result.success();
    }

}
