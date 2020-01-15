package com.pface.admin.modules.member.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.core.utils.Result;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.member.enums.AuditStatusEnum;
import com.pface.admin.modules.member.po.MemberCatalogue;
import com.pface.admin.modules.member.po.MemberPriceLabel;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.MemPriceLabService;
import com.pface.admin.modules.member.service.MemberUserService;
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

/**
 * @author cjbi,daniel.liu
 */
@Controller
@RequestMapping("/admin/pricelab")
public class MemPriceLabController extends BaseCrudController<MemberPriceLabel> {

    @Autowired
    private MemPriceLabService memPriceLabService;

    @Autowired
    private MemberUserService memberUserService;

    @GetMapping
    @RequiresPermissions("pricelab:view")
    public String memberPage(Model model) {
//        model.addAttribute("memberList", memberUserService.queryList(new MemberUser()));
        return "member/pricelab";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("pricelab:view")
    @Override
    public Result<List<MemberPriceLabel>> queryList(MemberPriceLabel priceLabel, PageQuery pageQuery) {

//        priceLabel.setDelFlag(0);
//        Page<MemberPriceLabel> page = (Page<MemberPriceLabel>) memPriceLabService.queryList(priceLabel, pageQuery);
        Condition condition = new Condition(MemberPriceLabel.class);
        condition.orderBy("opDate").desc();
        Example.Criteria criteria = condition.createCriteria();
        if (StringUtils.isNotEmpty(priceLabel.getUname())) {
            criteria.andLike("uname", '%' + priceLabel.getUname() + "%");
        }
        if (StringUtils.isNotEmpty(priceLabel.getLabel())) {
            criteria.andLike("label", '%' + priceLabel.getLabel() + "%");
        }
        criteria.andEqualTo("delFlag", 0);

        Page<MemberPriceLabel> page = (Page<MemberPriceLabel>)memPriceLabService.queryList(pageQuery, condition);
        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @PostMapping("/create")
    @SystemLog("价格标签创建")
    @Override
    public Result create(@Validated MemberPriceLabel priceLabel) {
        priceLabel.setLabelStatus(AuditStatusEnum.AUDITED);
        priceLabel.setOpDate(DateUtils.getNowDate());
        memPriceLabService.create(priceLabel);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    @SystemLog("价格标签更新")
    @Override
    public Result update(@Validated MemberPriceLabel priceLabel) {
        priceLabel.setOpDate(DateUtils.getNowDate());
        memPriceLabService.updateNotNull(priceLabel);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/delete-batch")
    @SystemLog("价格标签删除")
    @Override
    public Result deleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
        super.deleteBatchByIds(ids);
        return Result.success();
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
