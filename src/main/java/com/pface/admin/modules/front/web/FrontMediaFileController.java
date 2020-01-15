package com.pface.admin.modules.front.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.Result;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.front.vo.MediaFileInfoVo;
import com.pface.admin.modules.front.vo.MediaVo;
import com.pface.admin.modules.member.enums.MemberTypeEnum;
import com.pface.admin.modules.member.po.MemberMedia;
import com.pface.admin.modules.member.po.MemberMediaFile;
import com.pface.admin.modules.member.service.*;
import com.pface.admin.modules.system.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author daniel.liu
 */
@Controller
@RequestMapping("/front/mediafile")
public class FrontMediaFileController extends BaseCrudController<MemberMediaFile> {

    @Autowired
    private MemMediaFileService mediaFileService;

    @Autowired
    private MemberMediaService memberMediaService;

    @Autowired
    private MemBelongLabService  belongLabService;

    @Autowired
    private MemPriceLabService priceLabService;

    @Autowired
    private MemPubLabelService pubLabelService;


    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("media:view")
    @Override
    public Result<List<MediaFileInfoVo>> queryList(MemberMediaFile memberMedia, PageQuery pageQuery) {

        Page<MemberMediaFile> page = (Page<MemberMediaFile>) mediaFileService.queryList(memberMedia, pageQuery);
        List<MediaFileInfoVo> mediaFileInfoVos = new ArrayList<>();
        page.forEach(file -> {

            MediaFileInfoVo fileInfoVo = new MediaFileInfoVo(file);
            MemberMedia media= memberMediaService.queryOne(new MemberMedia().setMediaFileId(file.getId()));
            MediaVo mediaVo=new MediaVo();
            BeanUtils.copyProperties(mediaVo,media);
            //String[] blabs=media.getBelongLabelid().split(",");

            mediaVo.setBelongLabel(belongLabService.queryByIds(StringUtils.trans2long(media.getBelongLabelid(),",")));
            mediaVo.setPriceLabel(priceLabService.queryByIds(StringUtils.trans2long(media.getPriceLabelid(),",")));
            mediaVo.setPublishLabel(pubLabelService.queryByIds(StringUtils.trans2long(media.getPublishLabelid(),",")));

            fileInfoVo.setMemberMedia(mediaVo);

            mediaFileInfoVos.add(fileInfoVo);
        });
        return Result.success(mediaFileInfoVos).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @PostMapping("/create")
    @SystemLog("归属标签创建")
    @Override
    public Result create(@Validated MemberMediaFile memberMedia) {
        mediaFileService.create(memberMedia);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    @SystemLog("归属标签更新")
    @Override
    public Result update(@Validated MemberMediaFile memberMedia) {
        mediaFileService.updateNotNull(memberMedia);
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

    @ResponseBody
    @PostMapping("/logic-delete-batch")
    @ApiOperation("删除多条数据")
    public Result logicDeleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
        Long[] vehicleIds =Arrays.stream(ids).map(p -> Long.valueOf(p.toString())).toArray(Long[]::new);
        mediaFileService.logicDeleteBatchByIds(vehicleIds);
        return Result.success();
    }


}
