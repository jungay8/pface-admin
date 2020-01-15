package com.pface.admin.modules.front.web;

import com.alibaba.fastjson.JSONArray;
import com.pface.admin.core.utils.*;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.front.constants.FaceAppConstant;
import com.pface.admin.modules.front.vo.FaceAppUserPojo;
import com.pface.admin.modules.member.dto.FaceSenseboxDTO;
import com.pface.admin.modules.member.po.FaceAppImageLibs;
import com.pface.admin.modules.member.po.FaceAppUsescene;
import com.pface.admin.modules.member.service.FaceAppImageLibsService;
import com.pface.admin.modules.member.service.FaceCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/front/appimagelibs")
public class FaceAppImageLibsController extends BaseCrudController<FaceAppImageLibs> {
    @Autowired
    private FaceCommonService faceCommonService;
    @Autowired
    FaceAppImageLibsService faceAppImageLibsService;

    @Value("${face.serverip}")
    private String faceserverip;

    @Value("${face.serverport}")
    private String faceserverport;

    @ModelAttribute("userinfo")
    public FaceAppUserPojo getUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        FaceAppUserPojo user = (FaceAppUserPojo) session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        return user;
    }

    @ResponseBody
    @PostMapping("/refreshImageLibs")
    public Result refreshImageLibs(@RequestParam(name = "reqid") String reqid, HttpServletRequest request){
        HttpSession session = request.getSession(true);
        FaceAppUserPojo userinfo = (FaceAppUserPojo)session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        FaceAppImageLibs faceAppImageLibs_cond = new FaceAppImageLibs();
        faceAppImageLibs_cond.setSysUserid(userinfo.getId());
        faceAppImageLibs_cond.setDelFlag("1");
        List<FaceAppImageLibs> libsList = faceAppImageLibsService.queryList(faceAppImageLibs_cond);
        if (libsList.isEmpty()){
            return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("还没有刷新的人像库！");
        }else{
            for (FaceAppImageLibs faceAppImageLibs : libsList){
                JSONArray jsonArray = faceAppImageLibsService.remoteQueryImageLibs(faceserverip, faceserverport,  faceAppImageLibs.getDeviceId(), faceAppImageLibs.getLibId());

                if (jsonArray!=null && !jsonArray.isEmpty()) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        FaceAppImageLibs updateFaceAppImageLibs = new FaceAppImageLibs();
                        updateFaceAppImageLibs.setId(faceAppImageLibs.getId());
                        updateFaceAppImageLibs.setLibName(jsonArray.getJSONObject(i).getString("lib_name"));
                        updateFaceAppImageLibs.setPictureNo(jsonArray.getJSONObject(i).getIntValue("picture_no"));
                        updateFaceAppImageLibs.setUpdateTime(jsonArray.getJSONObject(i).getDate("update_time"));
                        updateFaceAppImageLibs.setLibType(jsonArray.getJSONObject(i).getByteValue("lib_type"));
                        faceCommonService.setAppCommonField(updateFaceAppImageLibs, "update", userinfo);
                        faceAppImageLibsService.updateNotNull(updateFaceAppImageLibs);
                    }
                }
            }

            return Result.success().setMsg("刷新成功。").addExtra("url","/front/faceimagelibs");
        }
    }

    @ResponseBody
    @PostMapping("/mysave")
    public Result create(
            @RequestParam(name = "syslibid") Long syslibid,
            @RequestParam(name = "editsyssceneid") Long editsyssceneid,
            @RequestParam(name = "deviceid") String deviceid,
            @RequestParam(name = "editlibname") String editlibname,
            @RequestParam(name = "editlibtype") Integer editlibtype,
            @RequestParam(name = "editlibId") Byte editlibId,
            @RequestParam(name = "remark") String remark,
            HttpServletRequest request
    ) {

        if (StringUtils.isEmpty(editlibname)){
            return Result.failure(ResultCodeEnum.PARAM_ERROR).setMsg("库名称不能为空。");
        }

        HttpSession session = request.getSession(true);
        FaceAppUserPojo userinfo = (FaceAppUserPojo)session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        FaceAppImageLibs faceAppImageLibs= new FaceAppImageLibs();
        faceAppImageLibs.setSysSceneid(editsyssceneid.intValue());
        faceAppImageLibs.setLibName(FileUtil.filterSpecialChar(editlibname));
        faceAppImageLibs.setLibType(editlibtype.byteValue());
        faceAppImageLibs.setRemark(FileUtil.filterSpecialChar(remark));

        if (syslibid!=null){

            //调用接口检查远程人像库是否还存在
            Boolean libisexists = faceAppImageLibsService.remoteEnsureImageLibExists(faceserverip, faceserverport,  deviceid, editlibId);
            if (libisexists){
                //调用接口远程修改
                boolean remoteUpdate = faceAppImageLibsService.remoteUpdateImageLib(faceserverip, faceserverport,  deviceid,editlibId, editlibname, editlibtype);
                //本地修改
                if(remoteUpdate) {
                    faceAppImageLibs.setId(syslibid.intValue());
                    faceAppImageLibs.setUpdateTime(DateUtils.getNowDate());
                    faceCommonService.setAppCommonField(faceAppImageLibs, "update", userinfo);
                    faceAppImageLibsService.updateNotNull(faceAppImageLibs);
                }else{
                    return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg("远程修改人像库调用失败。").addExtra("url","/front/faceimagelibs");
                }
            }else{
                //远程人像库被删除，本地也删除？TODO
                return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg("警告：远程人像库被删除。").addExtra("url","/front/faceimagelibs");
            }
        }else{ //新增
            //获取授权给该用户的所有智能设备
            List<FaceSenseboxDTO> faceSenseboxDTOs = faceAppImageLibsService.remoteQuerySensebox(userinfo.getId(), faceserverip, faceserverport);
            if (!faceSenseboxDTOs.isEmpty()){
                for(FaceSenseboxDTO faceSenseboxDTO : faceSenseboxDTOs){
                    //远程创建人像库
                    String retLibid = faceAppImageLibsService.remoteCreateImageLib(faceserverip, faceserverport, faceSenseboxDTO.getId(), editlibname, editlibtype);
                    //远程创建成功，则本地创建
                    if (StringUtils.isNotEmpty(retLibid)){
                        faceAppImageLibs.setSysUserid(userinfo.getId());
                        faceAppImageLibs.setPictureNo(0);
                        faceAppImageLibs.setLibId(Byte.valueOf(retLibid));  //调用接口回写
                        faceAppImageLibs.setDeviceId(faceSenseboxDTO.getDeviceId());  //通过授权确定
                        faceAppImageLibs.setAuthBegindate(faceSenseboxDTO.getMinAuthBegindate());
                        faceAppImageLibs.setAuthEnddate(faceSenseboxDTO.getMaxAuthEnddate());
                        faceAppImageLibs.setUpdateTime(DateUtils.getNowDate());
                        faceCommonService.setAppCommonField(faceAppImageLibs, "create", userinfo);
                        faceAppImageLibsService.create(faceAppImageLibs);
                    }else{
                        return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg("远程新增人像库调用失败。");
                    }

                }
            }else{
                return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg("没有授权远程资源，请联系系统管理员。");
            }
        }

        return Result.success().setMsg("操作成功。").addExtra("url","/front/faceimagelibs");
    }

    @ResponseBody
    @PostMapping("/logic-delete")
    public Result logicDeleteByIds(@NotNull @RequestParam("id")  String[] ids,
                                   @NotNull @RequestParam("deviceId")  String deviceId,
                                   @NotNull @RequestParam("libId") Byte libId,
                                   HttpServletRequest request) {

        Long[] byIds = Arrays.stream(ids).map(p -> Long.valueOf(p.toString())).toArray(Long[]::new);
        HttpSession session = request.getSession(true);
        FaceAppUserPojo userinfo = (FaceAppUserPojo)session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        boolean ret = faceAppImageLibsService.remoteDeleteImageLib(faceserverip, faceserverport, deviceId, libId);
        if (ret) {
            //本地操作系统图片文件删除 TODO

            //本地人像库删除--数据库中
            faceAppImageLibsService.logicDeleteBatchByIds(byIds, userinfo);

            //本地图片库删除---数据库中 TODO

            return Result.success().setMsg("操作成功。");
        }else{
            return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg("远程删除人像库错误！");
        }
    }
}
