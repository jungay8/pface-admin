package com.pface.admin.modules.front.web;

import com.pface.admin.core.utils.*;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.front.constants.FaceAppConstant;
import com.pface.admin.modules.front.vo.FaceAppUserPojo;
import com.pface.admin.modules.member.dto.FaceSenseboxDTO;
import com.pface.admin.modules.member.po.FaceAppImageLibs;
import com.pface.admin.modules.member.po.FaceAppImages;
import com.pface.admin.modules.member.service.FaceAppImageLibsService;
import com.pface.admin.modules.member.service.FaceAppImagesService;
import com.pface.admin.modules.member.service.FaceCommonService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/front/appimages")
public class FaceAppImagesController extends BaseCrudController<FaceAppImages> {
    @Autowired
    FaceAppImageLibsService faceAppImageLibsService;
    @Autowired
    FaceAppImagesService faceAppImagesService;
    @Autowired
    private FaceCommonService faceCommonService;
    @Value("${face.serverip}")
    private String faceserverip;

    @Value("${face.serverport}")
    private String faceserverport;

    @Value("${face.uploaddir}")
    private String uploaddir;

    @ModelAttribute("userinfo")
    public FaceAppUserPojo getUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        FaceAppUserPojo user = (FaceAppUserPojo) session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        return user;
    }

    @RequestMapping(value = "/uploadimags", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Result uploadimags(MultipartFile file, String sysLibId, HttpServletRequest request) {
        String parent = uploaddir;
        String child = sysLibId;//face_app_image_libs.id
        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(file);
        Result result = null;
        try {
            //保存到服务器（本地）
            String imgId = IdUtils.id();
            List<File> retFiles = FileUtil.saveFileList(multipartFiles, parent, child, "", imgId);
            if (!retFiles.isEmpty()) {
                File retFile = retFiles.get(0);
                FaceAppImageLibs faceAppImageLibs = faceAppImageLibsService.queryById(sysLibId);
                //上传到智能设备
                boolean uploadret = faceAppImagesService.uploadoneimage(faceserverip, faceserverport, sysLibId, imgId, faceAppImageLibs, retFiles.get(0));
                if (uploadret) {
                    //保存到db
                    HttpSession session = request.getSession(true);
                    FaceAppUserPojo userinfo = (FaceAppUserPojo) session.getAttribute(FaceAppConstant.USER_SESSION_KEY);

                    FaceAppImages createFaceAppImages = new FaceAppImages();
                    createFaceAppImages.setSysUserid(faceAppImageLibs.getSysUserid());
                    createFaceAppImages.setDeviceId(faceAppImageLibs.getDeviceId());
                    createFaceAppImages.setLibId(faceAppImageLibs.getLibId());
                    createFaceAppImages.setImgId(imgId);
                    createFaceAppImages.setImgPath(retFile.getAbsolutePath());
                    createFaceAppImages.setCreateTime(DateUtils.getNowDate());
                    faceCommonService.setAppCommonField(createFaceAppImages, "create", userinfo);
                    faceAppImagesService.create(createFaceAppImages);
                    result = Result.success().setMsg("上传成功");
                } else {
                    result = Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg("上传到远程智能设备失败");
                }
            } else {
                result = Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg("上传失败");
            }


        } catch (Exception e) {
            result = Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg("上传失败");
            e.printStackTrace();
        }

        return result;
    }


    @ResponseBody
    @PostMapping("/myupdate")
    public Result update(
            @RequestParam(name = "id") Integer id,
            @RequestParam(name = "editdeviceId") String deviceId,
            @RequestParam(name = "editimgId") String imgId,
            @RequestParam(name = "editlibId") String libId,
            @RequestParam(name = "editpersonIdcard") String personIdcard,
            @RequestParam(name = "editpersonName") String personName,
            @RequestParam(name = "editpersonGender") String personGender,
            @RequestParam(name = "editpersonAge") String personAge,
            @RequestParam(name = "editpersonAddr") String personAddr,
            @RequestParam(name = "querypersonName") String querypersonName,
            @RequestParam(name = "remark") String remark,
            HttpServletRequest request) {

        HttpSession session = request.getSession(true);
        FaceAppUserPojo userinfo = (FaceAppUserPojo) session.getAttribute(FaceAppConstant.USER_SESSION_KEY);

        try {
            if (id != null) {

                //调用接口检查远程人像库是否还存在
                Boolean libisexists = faceAppImagesService.remoteEnsureImageExists(faceserverip, faceserverport, deviceId, imgId, libId);
                boolean remoteUpdate = false;
                String tempremark = remark;
                Boolean hasAttchText = tempremark.indexOf("远程人像图片不存在") >= 0;
                if (libisexists) {
                    //调用接口远程修改
                    remoteUpdate = faceAppImagesService.remoteUpdateImage(faceserverip, faceserverport, deviceId, imgId, libId, personIdcard, personName, personGender, personAge, personAddr);
                } else {
                    if (!hasAttchText) {
                        tempremark = tempremark + ",远程人像图片不存在:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
                    }
                }

                if (!remoteUpdate && !hasAttchText) {
                    tempremark = tempremark + ",远程人像图片修改失败。";
                }

                //本地修改
                FaceAppImages faceAppImages = new FaceAppImages();
                faceAppImages.setId(id);
                faceAppImages.setPersonIdcard(FileUtil.filterSpecialChar(personIdcard));
                faceAppImages.setPersonName(FileUtil.filterSpecialChar(personName));
                faceAppImages.setPersonGender(personGender);
                faceAppImages.setPersonAge(personAge);
                faceAppImages.setPersonAddr(FileUtil.filterSpecialChar(personAddr));
                faceAppImages.setRemark(FileUtil.filterSpecialChar(tempremark));
                faceCommonService.setAppCommonField(faceAppImages, "update", userinfo);
                faceAppImagesService.updateNotNull(faceAppImages);
            } else {
                log.info("修改的ID为空！");
                return Result.success().setMsg("操作成功。").addExtra("url", "/front/faceappimages");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg("操作失败，请稍后再试。").addExtra("url", "/front/faceappimages");
        }

        return Result.success().setMsg("操作成功。").addExtra("url", "/front/faceappimages")
                .addExtra("personName",querypersonName)
                .addExtra("libId",libId)
                .addExtra("sysLibId",id)
                .addExtra("deviceId",deviceId);
    }

    @ResponseBody
    @PostMapping("/logic-delete")
    public Result logicDeleteByIds(@NotNull @RequestParam("id") String[] ids,
                                   @NotNull @RequestParam("deviceId") String deviceId,
                                   @NotNull @RequestParam("imgId") String imgId,
                                   @NotNull @RequestParam("libId") Byte libId,
                                   HttpServletRequest request) {

        Long[] byIds = Arrays.stream(ids).map(p -> Long.valueOf(p.toString())).toArray(Long[]::new);
        HttpSession session = request.getSession(true);
        FaceAppUserPojo userinfo = (FaceAppUserPojo) session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        boolean ret = faceAppImagesService.remoteDeleteImage(faceserverip, faceserverport, deviceId, imgId, libId);
        if (ret) {
            //本地图片库删除和删除操作系统下的文件
            faceAppImagesService.logicDeleteBatchByIds(byIds, userinfo);

            return Result.success().setMsg("操作成功。");
        } else {
            return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg("远程删除人脸图片错误，请检查网络！");
        }
    }


}
