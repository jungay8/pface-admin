package com.pface.admin.modules.member.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.core.utils.Result;
import com.pface.admin.core.utils.ResultCodeEnum;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.member.po.*;
import com.pface.admin.modules.member.service.*;
import com.pface.admin.modules.member.vo.FaceDeviceChannelResParams;
import com.pface.admin.modules.member.vo.FaceDeviceChannelResVo;
import com.pface.admin.modules.member.vo.FaceUserChannelResParams;
import com.pface.admin.modules.member.vo.FaceUserChannelResVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/admin/facedevicechannelres")
public class FaceDeviceChannelResController extends BaseCrudController<FaceDeviceChannelRes> {
    @Autowired
    private FaceUserService faceUserService;
    @Autowired
    private FaceSenseboxService faceSenseboxService;
    @Autowired
    private FaceUserChannelResService faceUserChannelResService;
    @Autowired
    private FaceCommonService faceCommonService;
    @Autowired
    private FaceDeviceChannelResService faceDeviceChannelResService;
    @Autowired
    private FaceCameraService faceCameraService;

    @GetMapping
    @RequiresPermissions("facedevicechannelres:view")
    public String facedevicechannelresPage(Model model) {
        List<FaceUser> users = faceUserService.queryAll();
        model.addAttribute("users", users);
        List<FaceSensebox> list = faceSenseboxService.queryAll();
        model.addAttribute("senseboxalllist", list);
        return "member/facedevicechannelres";
    }

    @ResponseBody
    @GetMapping("/listpage")
    @RequiresPermissions("facedevicechannelres:view")
    public Result<List<FaceDeviceChannelResVo>> queryFaceDeviceChannelRes(PageQuery pageQuery,
                                                                          HttpServletRequest request,
                                                                          HttpServletResponse response) {
        if (pageQuery.getPageSizeKey() <= 0) {
            pageQuery.setPageSizeKey(10);
        }
        if (pageQuery.getPageNumKey() <= 0) {
            pageQuery.setPageNumKey(1);
        }
        String deviceId = request.getParameter("deviceId");
        String issetup = request.getParameter("issetup");
        FaceDeviceChannelResParams params = new FaceDeviceChannelResParams();

        if (StringUtils.isNotEmpty(deviceId)) {
            params.setDeviceId(deviceId);
        }
        if (StringUtils.isNotEmpty(issetup)) {
            params.setIssetup(issetup);
        }

        Page<FaceDeviceChannelResVo> page = faceDeviceChannelResService.queryFaceDeviceChannelResList(pageQuery, params);
        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @PostMapping("/authorize")
    public Result authorize(FaceUserChannelResParams faceUserChannelResParams) {

        //校验最大授权时间start
        String maxAuthStartdate = faceUserChannelResParams.getCanAuthStartdate();
        Date maxAuthStartdateDate = DateUtils.parseDate(maxAuthStartdate);
        String authBegindate = faceUserChannelResParams.getAuthBegindate();
        Date authBegindateDate = DateUtils.parseDate(authBegindate);
        if (authBegindateDate.before(maxAuthStartdateDate)){
                    String msg = "可授权时间必须大于等于："+maxAuthStartdate;
                    return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg(msg);
        }
        //校验最大授权时间end

        FaceUserChannelRes descFaceUserChannelRes = new FaceUserChannelRes();
        descFaceUserChannelRes.setSysUserid(faceUserChannelResParams.getSysUserid());
        descFaceUserChannelRes.setAuthBegindate(DateUtils.parseDate(faceUserChannelResParams.getAuthBegindate()));
        descFaceUserChannelRes.setAuthEnddate(DateUtils.addSeconds(DateUtils.addDays(DateUtils.parseDate(faceUserChannelResParams.getAuthEnddate()) ,1), -1));
        descFaceUserChannelRes.setDeviceId(faceUserChannelResParams.getDeviceId());
        descFaceUserChannelRes.setChannel(Byte.valueOf(faceUserChannelResParams.getChannel()));
        descFaceUserChannelRes.setRemark(faceUserChannelResParams.getRemark());

        descFaceUserChannelRes = faceCommonService.setCommonField(descFaceUserChannelRes, "create");
        faceUserChannelResService.create(descFaceUserChannelRes);

        //不支持日期类型的copy，要扩张才能用，所以先注释
//        try {
//        BeanUtils.copyProperties(descFaceUserChannelRes, faceUserChannelResParams);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }

        return Result.success();
    }


//    @ResponseBody
//    @PostMapping("/logic-delete-batch")
//    @ApiOperation("逻辑删除客户数据")
//    public Result logicDeleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
//        Long[] boxIds = Arrays.stream(ids).map(p -> Long.valueOf(p.toString())).toArray(Long[]::new);
//        faceUserChannelResService.logicDeleteBatchByIds(boxIds);
//        return Result.success();
//    }
}
