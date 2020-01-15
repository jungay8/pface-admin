package com.pface.admin.modules.front.web;

import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.*;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.front.constants.FaceAppConstant;
import com.pface.admin.modules.front.vo.FaceAppUserPojo;
import com.pface.admin.modules.member.po.FaceAppUsescene;
import com.pface.admin.modules.member.service.FaceAppUsesceneService;
import com.pface.admin.modules.member.service.FaceCommonService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

@Controller
@RequestMapping("/front/appusescene")
public class FaceAppUseSceneController extends BaseCrudController<FaceAppUsescene> {
    @Autowired
    private FaceAppUsesceneService faceAppUsesceneService;
    @Autowired
    private FaceCommonService faceCommonService;
    @ModelAttribute("userinfo")
    public FaceAppUserPojo getUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        FaceAppUserPojo user = (FaceAppUserPojo)session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        return user;
    }

    @ResponseBody
    @PostMapping("/mysave")
    public Result create(
            @RequestParam(name = "sceneid") Long sceneid,
            @RequestParam(name = "scenename") String scenename,
            @RequestParam(name = "startEndDate") String startEndDate,
            @RequestParam(name = "asignStartEndDate") String asignStartEndDate,
            @RequestParam(name = "theme") String theme,
            @RequestParam(name = "address") String address,
            @RequestParam(name = "remark") String remark,
            HttpServletRequest request
    ) {
        if (StringUtils.isEmpty(scenename)){
            return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("会议名称不能为空。");
        }
        if (StringUtils.isEmpty(address)){
            return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("会议地址不能为空。");
        }

        String startDate = null;
        String endDate = null;
        String asignStartDate = null;
        String asignEndDate = null;
        Date date_startDate = null;
        Date date_endDate = null;
        Date date_asignStartDate = null;
        Date date_asignEndDate = null;
        if (StringUtils.isNotEmpty(startEndDate)){
            String[] startEndDateArray = startEndDate.split("~");
            startDate = StringUtils.isNotEmpty(startEndDateArray[0]) ? startEndDateArray[0].trim() : null;
            if (StringUtils.isEmpty(startDate)){
                return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("会议开始时间不能为空。");
            }

            endDate = StringUtils.isNotEmpty(startEndDateArray[1]) ? startEndDateArray[1].trim() : null;
            if (StringUtils.isEmpty(endDate)){
                return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("会议结束时间不能为空。");
            }

            date_startDate = DateUtils.parseDate(startDate);
            date_endDate = DateUtils.parseDate(endDate);
            if (date_startDate.after(date_endDate)){
                return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("会议开始时间不能大于会议结束时间。");
            }
        }else{
            return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("会议时段不能为空。");
        }

        if (StringUtils.isNotEmpty(asignStartEndDate)){
            String[] asignStartEndDateArray = asignStartEndDate.split("~");
            asignStartDate = StringUtils.isNotEmpty(asignStartEndDateArray[0]) ? asignStartEndDateArray[0].trim() : null;
            asignEndDate = StringUtils.isNotEmpty(asignStartEndDateArray[1]) ? asignStartEndDateArray[1].trim() : null;
            if (StringUtils.isEmpty(asignStartDate)){
                return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("签到开始时间不能为空。");
            }
            if (StringUtils.isEmpty(asignEndDate)){
                return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("签到结束时间不能为空。");
            }
            date_asignStartDate = DateUtils.parseDate(asignStartDate);
            date_asignEndDate = DateUtils.parseDate(asignEndDate);
            if (date_asignStartDate.after(date_asignEndDate)){
                return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("签到开始时间不能大于签到结束时间。");
            }

        }else{
            return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("签到时段不能为空。");
        }

        //签到时间要求在会议时间之间
        if (date_asignStartDate.before(date_startDate) || date_asignStartDate.after(date_endDate) ||
            date_asignEndDate.before(date_startDate) || date_asignEndDate.after(date_endDate)){
            return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("签到时段要求在会议时段之间。");
        }

        FaceAppUsescene faceAppUsescene= new FaceAppUsescene();
        HttpSession session = request.getSession(true);
        FaceAppUserPojo userinfo = (FaceAppUserPojo)session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        faceAppUsescene.setSysUserid(userinfo.getId());
        try {
            faceAppUsescene.setStartDate(DateUtils.parseDate(startDate, "yyyy-MM-dd"));
            faceAppUsescene.setEndDate(DateUtil.addSecond(DateUtils.addDays(DateUtils.parseDate(endDate, "yyyy-MM-dd"), 1),-1));
            faceAppUsescene.setAsignStarttime(DateUtils.parseDate(asignStartDate, "yyyy-MM-dd HH:mm:ss"));
            faceAppUsescene.setAsignEndtime(DateUtils.parseDate(asignEndDate, "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        faceAppUsescene.setScenename(FileUtil.filterSpecialChar(scenename));
        faceAppUsescene.setTheme(FileUtil.filterSpecialChar(theme));
        faceAppUsescene.setAddress(FileUtil.filterSpecialChar(address));
        faceAppUsescene.setRemark(FileUtil.filterSpecialChar(remark));
        if (sceneid!=null){
            faceAppUsescene.setId(sceneid.intValue());
            faceCommonService.setAppCommonField(faceAppUsescene, "update", userinfo);
            faceAppUsesceneService.updateNotNull(faceAppUsescene);
        }else{
            faceCommonService.setAppCommonField(faceAppUsescene, "create", userinfo);
            faceAppUsesceneService.create(faceAppUsescene);
        }

        return Result.success().setMsg("操作成功。").addExtra("url","/front/faceimagescene");
    }

    @Deprecated
    @ResponseBody
    @PostMapping("/mydelete")
    public Result mydelete(
            @RequestParam(name = "sceneid") Long sceneid,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(true);
        FaceAppUserPojo userinfo = (FaceAppUserPojo)session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        faceAppUsesceneService.logicDeleteBatchByIds(new Long[]{sceneid}, userinfo);
        return Result.success().setMsg("操作成功。").addExtra("url","/front/faceimagescene");
    }

    @ResponseBody
    @PostMapping("/logic-delete")
    public Result logicDeleteByIds(@NotNull @RequestParam("id")  String[] ids, HttpServletRequest request) {

        Long[] vehicleIds =Arrays.stream(ids).map(p -> Long.valueOf(p.toString())).toArray(Long[]::new);
        HttpSession session = request.getSession(true);
        FaceAppUserPojo userinfo = (FaceAppUserPojo)session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        faceAppUsesceneService.logicDeleteBatchByIds(vehicleIds, userinfo);
//        mediaFileService.logicDeleteBatchByIds(vehicleIds);
        return Result.success().setMsg("操作成功。");
    }
}
