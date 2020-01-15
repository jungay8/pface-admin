package com.pface.admin.modules.member.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.pface.admin.core.utils.*;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.jiekou.pojo.FaceAppUsescenePojo;
import com.pface.admin.modules.member.enums.AuditStatusEnum;
import com.pface.admin.modules.member.enums.MediaTypeEnum;
import com.pface.admin.modules.member.po.*;
import com.pface.admin.modules.member.service.FaceCommonService;
import com.pface.admin.modules.member.service.FaceSenseboxService;
import com.pface.admin.modules.member.service.FaceUserChannelResService;
import com.pface.admin.modules.member.service.FaceUserService;
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
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/admin/faceuserchannelres")
public class FaceUserChannelResController extends BaseCrudController<FaceUserChannelRes> {
    @Autowired
    private FaceUserService faceUserService;
    @Autowired
    private FaceSenseboxService faceSenseboxService;
    @Autowired
    private FaceUserChannelResService faceUserChannelResService;
    @Autowired
    private FaceCommonService faceCommonService;

    @GetMapping
    @RequiresPermissions("faceuserchannelres:view")
    public String faceuserchannelresPage(Model model) {
        List<FaceUser> users = faceUserService.queryAll();
        model.addAttribute("users", users);
        List<FaceSensebox> list = faceSenseboxService.queryAll();
        model.addAttribute("senseboxalllist", list);

        return "member/faceuserchannelres";
    }

    @ResponseBody
    @GetMapping("/listpage")
    @RequiresPermissions("faceuserchannelres:view")
    public Result<List<FaceUserChannelResVo>> queryFaceUserChannelRes(PageQuery pageQuery,
                                                                      HttpServletRequest request,
                                                                      HttpServletResponse response){
        if (pageQuery.getPageSizeKey() <= 0) {
            pageQuery.setPageSizeKey(10);
        }
        if (pageQuery.getPageNumKey() <= 0) {
            pageQuery.setPageNumKey(1);
        }
        String username = request.getParameter("username");
        String deviceId = request.getParameter("deviceId");
//        String cameraStatus = request.getParameter("cameraStatus");
        FaceUserChannelResParams params = new FaceUserChannelResParams();
        if (StringUtils.isNotEmpty(username)){
            params.setUsername(username);
        }
        if (StringUtils.isNotEmpty(deviceId)){
            params.setDeviceId(deviceId);
        }
//        if (StringUtils.isNotEmpty(cameraStatus)){
//            params.setCameraStatus(cameraStatus);
//        }

        Page<FaceUserChannelResVo> page = faceUserChannelResService.queryFaceUserChannelResList(pageQuery, params);
        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }


    @ResponseBody
    @PostMapping("/update")
    @Override
    public Result update(FaceUserChannelRes faceUserChannelRes) {

        faceUserChannelRes = faceCommonService.setCommonField(faceUserChannelRes, "update");
        faceUserChannelResService.updateNotNull(faceUserChannelRes);
        return Result.success();
    }

    @ResponseBody
    @RequestMapping(value={"/checkFaceUserChannelRes"},method = {RequestMethod.POST})
    public Result checkFaceUserChannelRes(HttpServletRequest request, HttpServletResponse response) {
        Boolean flag = true;
        String msg = "ok";
        String id = request.getParameter("id");
        FaceUserChannelRes faceUserChannelRes_cond = new FaceUserChannelRes();
        FaceUserChannelRes oneFaceUserChannelRes = faceUserChannelResService.queryById(id);
        Date now = DateUtils.getNowDate();
        if (oneFaceUserChannelRes!=null){
            if (now.after(oneFaceUserChannelRes.getAuthBegindate())
                    && now.before(oneFaceUserChannelRes.getAuthEnddate())){ //授权在授权时间内
                flag = false;
                String sectionDate = DateUtils.formatDate(oneFaceUserChannelRes.getAuthBegindate(), "yyyy-MM-dd") +
                        " ~ "+DateUtils.formatDate(oneFaceUserChannelRes.getAuthEnddate(), "yyyy-MM-dd HH:mm:ss");
                msg = "在授权时间内：【" + sectionDate + "】,不能删除.";
            }
        }
//
//        if (oneFaceUserChannelRes!=null && oneFaceUserChannelRes.getSysSceneid()!=null){
//            //调起应用端接口
//            FaceUser faceUser = faceUserService.queryById(oneFaceUserChannelRes.getSysUserid());
//            String url = "http://"+faceUser.getIp()+":"+faceUser.getPort()+"/appapi/faceapp/queryoneusescene";
//            Map<String, String> param = new HashMap<>();
//            param.put("id", oneFaceUserChannelRes.getSysSceneid()+"");
//            String jsonstr = OKHttpUtil.httpPost(url, param);
//            FaceAppUsescenePojo pojo = new FaceAppUsescenePojo();
//            if (StringUtils.isNotEmpty(jsonstr)){
//                jsonstr = jsonstr.replaceAll("\\\\", "");
//                jsonstr = jsonstr.substring(1,jsonstr.length() -1);
//                pojo = JSON.parseObject(jsonstr, FaceAppUsescenePojo.class);
//
//                if (now.after(pojo.getStartDate()) && now.before(pojo.getEndDate())){ //会议正在进行
//                    flag = false;
//                    String sectionDate = DateUtils.formatDate(pojo.getStartDate(), "yyyy-MM-dd") + "~"+DateUtils.formatDate(pojo.getEndDate(), "yyyy-MM-dd");
//                    msg = "会议："+pojo.getName() + "正在进行，会议时间：" + sectionDate + ",不能删除";
////                    return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg();
//                }
//            }
//        }

        return Result.success()
                .addExtra("flag", flag)
                .addExtra("msg",msg);
    }

    @ResponseBody
    @PostMapping("/logic-delete-batch")
    @ApiOperation("逻辑删除客户数据")
    public Result logicDeleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
        Long[] boxIds = Arrays.stream(ids).map(p -> Long.valueOf(p.toString())).toArray(Long[]::new);
        faceUserChannelResService.logicDeleteBatchByIds(boxIds);
        return Result.success();
    }
}
