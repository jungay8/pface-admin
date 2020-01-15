package com.pface.admin.modules.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pface.admin.core.utils.*;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.front.vo.FaceAppUserPojo;
import com.pface.admin.modules.member.dto.FaceSenseboxDTO;
import com.pface.admin.modules.member.po.FaceAppImageLibs;
import com.pface.admin.modules.member.po.FaceAppImages;
import com.pface.admin.modules.member.po.FaceSensebox;
import com.pface.admin.modules.member.service.FaceAppImageLibsService;
import com.pface.admin.modules.member.service.FaceAppImagesService;
import com.pface.admin.modules.member.service.FaceSenseboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class FaceAppImagesServiceImpl extends BaseService<FaceAppImages> implements FaceAppImagesService {
    @Autowired
    FaceAppImageLibsService faceAppImageLibsService;
    @Autowired
    FaceSenseboxService faceSenseboxService;

    @Override
    public Boolean remoteEnsureImageExists(String faceserverip, String faceserverport, String deviceId, String imgId, String libId) {
        String url = "http://" + faceserverip + ":" + faceserverport + "/appapi/faceserverapi/queryOneImage";
        Map<String, String> param = new HashMap<>();
        param.put("deviceId", deviceId + "");
        param.put("imgId", imgId + "");
        param.put("libId", libId + "");
        String jsonstr = OKHttpUtil.httpPost(url, param);
        boolean ret = false;
        if (StringUtils.isNotEmpty(jsonstr)) {
            jsonstr = jsonstr.replaceAll("\\\\", "");
            jsonstr = jsonstr.substring(1,jsonstr.length() -1);
            JSONObject jsonObject = JSON.parseObject(jsonstr);
            Boolean sucess = jsonObject.getBoolean("success");
            if (sucess) {
                ret = true;
            } else {
                ret = false;
            }
        } else {
            ret = false;
        }

        return ret;
    }

    @Override
    public boolean remoteUpdateImage(String faceserverip, String faceserverport,
                                     String deviceId, String imgId, String libId,
                                     String personIdcard, String personName, String personGender, String personAge, String personAddr) {
        String url = "http://" + faceserverip + ":" + faceserverport + "/appapi/faceserverapi/updateImage";
        Map<String, String> param = new HashMap<>();
        param.put("deviceId", deviceId);
        param.put("imgId", imgId);
        param.put("libId", libId);
        param.put("personIdcard", personIdcard);
        param.put("personName", personName);
        param.put("personGender", personGender);
        param.put("personAge", personAge);
        param.put("personAddr", personAddr);
        String jsonstr = OKHttpUtil.httpPost(url, param);
        boolean ret = false;
        if (StringUtils.isNotEmpty(jsonstr)) {
            jsonstr = jsonstr.replaceAll("\\\\", "");
            jsonstr = jsonstr.substring(1,jsonstr.length() -1);
            JSONObject jsonObject = JSON.parseObject(jsonstr);
            Boolean sucess = jsonObject.getBoolean("success");
            if (sucess) {
                ret = true;
            }
        }
        return ret;
    }

    @Override
    public boolean remoteDeleteImage(String faceserverip, String faceserverport, String deviceId, String imgId, Byte libId) {
        String url = "http://" + faceserverip + ":" + faceserverport + "/appapi/faceserverapi/deleteImage";
        Map<String, String> param = new HashMap<>();
        param.put("deviceId", deviceId);
        param.put("imgId", imgId);
        param.put("libId", libId + "");
        String jsonstr = OKHttpUtil.httpPost(url, param);
        boolean ret = false;
        if (StringUtils.isNotEmpty(jsonstr)) {
            jsonstr = jsonstr.replaceAll("\\\\", "");
            jsonstr = jsonstr.substring(1,jsonstr.length() -1);
            JSONObject jsonObject = JSON.parseObject(jsonstr);
            Boolean sucess = jsonObject.getBoolean("success");
            if (sucess) {
                ret = true;
            }
        }
        return ret;
    }

    @Override
    public boolean uploadoneimage(String faceserverip, String faceserverport, String sysLibId, String imgId, FaceAppImageLibs faceAppImageLibs,File file) {

        if (faceAppImageLibs==null){
            return false;
        }
        //查询智能设备
        String url2 = "http://" + faceserverip + ":" + faceserverport + "/appapi/faceserverapi/queryOneSensebox";
        Map<String, String> param2 = new HashMap<>();
        param2.put("id", "");
        param2.put("deviceId", faceAppImageLibs.getDeviceId());
        String jsonstr2 = OKHttpUtil.httpPost(url2, param2);
        FaceSenseboxDTO dto = null;
        if (StringUtils.isNotEmpty(jsonstr2)) {
            jsonstr2 = jsonstr2.replaceAll("\\\\", "");
            jsonstr2 = jsonstr2.substring(1,jsonstr2.length() -1);
            JSONObject jsonObject = JSON.parseObject(jsonstr2);
            Boolean sucess = jsonObject.getBoolean("success");
            if (sucess) {
                dto =jsonObject.getJSONObject("data").toJavaObject(FaceSenseboxDTO.class);
            }
        }

        if (dto == null){
            return false;
        }

        try {
            Map<String, Object> param = new HashMap<>();
            param.put("sysDeviceId", dto.getId() + "");
            param.put("libId", faceAppImageLibs.getLibId() + "");
            param.put("imgId", imgId);
            param.put("file", file);
            String url = "http://" + faceserverip + ":" + faceserverport + "/appapi/faceserverapi/uploadoneimage";
            OKHttpUtil.post_file(url, param, file);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }


    }


    @Override
    @Transactional
    public void logicDeleteBatchByIds(Long[] ids, FaceAppUserPojo userinfo) {

        boolean debug = true;//为true时不删除
        Date now = DateUtils.getNowDate();
        FaceAppImages faceAppImages = null;
        FaceAppImages oldFaceAppImages = null;
        for(int i=0;i<ids.length;i++) {
            faceAppImages = new FaceAppImages();
            if (!debug) { //删除操作系统下的文件
                oldFaceAppImages = queryById(ids[i]);
                FileUtil.deleteFile(oldFaceAppImages.getImgPath());
                oldFaceAppImages = null;
            }
            if (userinfo!=null){
                faceAppImages.setUpdateBy(userinfo.getId().toString());
            }
            faceAppImages.setUpdateDate(now);

            faceAppImages.setId(Integer.parseInt(ids[i].toString()));
            faceAppImages.setDelFlag("0");
            super.updateNotNull(faceAppImages);
        }
    }
}
