package com.pface.admin.modules.member.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.ArrayUtil;
import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.core.utils.Result;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.member.enums.AuditStatusEnum;
import com.pface.admin.modules.member.po.MemberBelongLabel;
import com.pface.admin.modules.member.po.MemberPublishLabel;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.MemPubLabelService;
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
@RequestMapping("/admin/publishlab")
public class MemPubLabController extends BaseCrudController<MemberPublishLabel> {

    @Autowired
    private MemPubLabelService pubLabelService;

    @Autowired
    private MemberUserService memberUserService;

    @GetMapping
    @RequiresPermissions("publishlab:view")
    public String memberPage(Model model) {
//        model.addAttribute("memberList", memberUserService.queryList(new MemberUser()));
        return "member/publishlab";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("publishlab:view")
    @Override
    public Result<List<MemberPublishLabel>> queryList(MemberPublishLabel publishlab, PageQuery pageQuery) {

//        publishlab.setDelFlag(0);
//        Page<MemberPublishLabel> page = (Page<MemberPublishLabel>)pubLabelService .queryList(publishlab, pageQuery);

        Condition condition = new Condition(MemberPublishLabel.class);
        condition.orderBy("opDate").desc();
        Example.Criteria criteria = condition.createCriteria();
        if (StringUtils.isNotEmpty(publishlab.getUname())) {
            criteria.andLike("uname", '%' + publishlab.getUname() + "%");
        }
        if (StringUtils.isNotEmpty(publishlab.getLabel())) {
            criteria.andLike("label", '%' + publishlab.getLabel() + "%");
        }
        criteria.andEqualTo("delFlag", 0);
        Page<MemberPublishLabel> page = (Page<MemberPublishLabel>) pubLabelService.queryList(pageQuery,condition);

        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @PostMapping("/create")
    @SystemLog("转载标签创建")
    @Override
    public Result create(@Validated MemberPublishLabel publishlab) {
        publishlab.setLabelStatus(AuditStatusEnum.AUDITED);
        pubLabelService.create(publishlab);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    @SystemLog("转载标签更新")
    @Override
    public Result update(@Validated MemberPublishLabel publishlab) {
        publishlab.setOpDate(DateUtils.getNowDate());
        pubLabelService.updateNotNull(publishlab);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/delete-batch")
    @SystemLog("转载标签删除")
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

        // Long[] vehicleIds = new Long[ ids.length ];//定义一个Long类型的数组
        // System.arraycopy(ids, 0, vehicleIds, 0, ids.length); //转换
        // ArrayUtil.toArray(C,Long.class);
        List list=  Arrays.asList(ids);
        Long[] vehicleIds =  ArrayUtil.toArray(list,Long.class);

        pubLabelService.logicDeleteBatchByIds(vehicleIds);
        return Result.success();
    }

}
