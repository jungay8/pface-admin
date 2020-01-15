package com.pface.admin.modules.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.core.utils.FileUtil;
import com.pface.admin.core.utils.OKHttpUtil;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.front.constants.FaceAppConstant;
import com.pface.admin.modules.front.vo.FaceAppUserPojo;
import com.pface.admin.modules.member.dto.FaceSenseboxDTO;
import com.pface.admin.modules.member.po.FaceAppImageLibs;
import com.pface.admin.modules.member.po.FaceAppImages;
import com.pface.admin.modules.member.po.FaceAppUsescene;
import com.pface.admin.modules.member.service.FaceAppImageLibsService;
import com.pface.admin.modules.member.service.FaceAppImagesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FaceAppImageLibsServiceImpl extends BaseService<FaceAppImageLibs> implements FaceAppImageLibsService {
    @Autowired
    FaceAppImagesService faceAppImagesService;

    @Override
    public List<FaceSenseboxDTO> remoteQuerySensebox(Integer sysUserid, String faceserverip, String faceserverport) {
        String url = "http://" + faceserverip + ":" + faceserverport + "/appapi/faceserverapi/queryOneUserAllSensebox";
        Map<String, String> param = new HashMap<>();
        param.put("sysUserid", sysUserid + "");
        String jsonstr = OKHttpUtil.httpPost(url, param);
        List<FaceSenseboxDTO> faceSenseboxDTOList = null;
        if (StringUtils.isNotEmpty(jsonstr)) {
            jsonstr = jsonstr.replaceAll("\\\\", "");
            jsonstr = jsonstr.substring(1, jsonstr.length() - 1);
            JSONObject jsonObject = JSON.parseObject(jsonstr);
            Boolean sucess = jsonObject.getBoolean("success");
            if (sucess) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                faceSenseboxDTOList = jsonArray.toJavaList(FaceSenseboxDTO.class);
                return faceSenseboxDTOList;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String remoteCreateImageLib(String faceserverip, String faceserverport, Integer sysDeviceId, String editlibname, Integer editlibtype) {
        String url = "http://" + faceserverip + ":" + faceserverport + "/appapi/faceserverapi/createImageLib";
        Map<String, String> param = new HashMap<>();
        param.put("sysDeviceId", sysDeviceId + "");
        param.put("editlibname", editlibname);
        param.put("editlibtype", editlibtype + "");
        String jsonstr = OKHttpUtil.httpPost(url, param);
        String libid = null;
        if (StringUtils.isNotEmpty(jsonstr)) {
            jsonstr = jsonstr.replaceAll("\\\\", "");
            jsonstr = jsonstr.substring(1, jsonstr.length() - 1);
            JSONObject jsonObject = JSON.parseObject(jsonstr);
            Boolean sucess = jsonObject.getBoolean("success");
            if (sucess) {
                JSONObject object = jsonObject.getJSONObject("data");
                libid = object.getString("lib_id");
                return libid;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public JSONArray remoteQueryImageLibs(String faceserverip, String faceserverport, String deviceId, Byte libId) {
        String url = "http://" + faceserverip + ":" + faceserverport + "/appapi/faceserverapi/queryImageLibs";
        Map<String, String> param = new HashMap<>();
        param.put("deviceId", deviceId);
        param.put("libId", libId + "");
        param.put("libName", "");
        String jsonstr = OKHttpUtil.httpPost(url, param);
        boolean ret = false;
        JSONArray jsonArray = null;
        if (StringUtils.isNotEmpty(jsonstr)) {
            jsonstr = jsonstr.replaceAll("\\\\", "");
            jsonstr = jsonstr.substring(1, jsonstr.length() - 1);
            JSONObject jsonObject = JSON.parseObject(jsonstr);
            Boolean sucess = jsonObject.getBoolean("success");
            if (sucess) {
                jsonArray = jsonObject.getJSONArray("data");
            }
        }

        return jsonArray;
    }

    @Override
    public Boolean remoteEnsureImageLibExists(String faceserverip, String faceserverport, String deviceId, Byte libId) {
        String url = "http://" + faceserverip + ":" + faceserverport + "/appapi/faceserverapi/queryAllImageLibs";
        Map<String, String> param = new HashMap<>();
        param.put("deviceId", deviceId + "");
        String jsonstr = OKHttpUtil.httpPost(url, param);
        boolean ret = false;
        if (StringUtils.isNotEmpty(jsonstr)) {
            jsonstr = jsonstr.replaceAll("\\\\", "");
            jsonstr = jsonstr.substring(1, jsonstr.length() - 1);
            JSONObject jsonObject = JSON.parseObject(jsonstr);
            Boolean sucess = jsonObject.getBoolean("success");
            if (sucess) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                JSONObject object = null;
                Byte temp_libid = null;
                for (int i = 0; i < jsonArray.size(); i++) {
                    object = jsonArray.getJSONObject(i);
                    temp_libid = object.getByteValue("lib_id");
                    if (temp_libid.byteValue() == libId) {
                        ret = true;
                    }
                }
            } else {
                ret = false;
            }
        } else {
            ret = false;
        }

        return ret;
    }

    @Override
    public boolean remoteUpdateImageLib(String faceserverip, String faceserverport, String deviceId,
                                        Byte editlibId, String editlibname, Integer editlibtype) {
        String url = "http://" + faceserverip + ":" + faceserverport + "/appapi/faceserverapi/updateImageLib";
        Map<String, String> param = new HashMap<>();
        param.put("deviceId", deviceId);
        param.put("libid", editlibId + "");
        param.put("libname", editlibname);
        param.put("libtype", editlibtype + "");
        String jsonstr = OKHttpUtil.httpPost(url, param);
        boolean ret = false;
        if (StringUtils.isNotEmpty(jsonstr)) {
            jsonstr = jsonstr.replaceAll("\\\\", "");
            jsonstr = jsonstr.substring(1, jsonstr.length() - 1);
            JSONObject jsonObject = JSON.parseObject(jsonstr);
            Boolean sucess = jsonObject.getBoolean("success");
            if (sucess) {
                ret = true;
            }
        }
        return ret;
    }

    @Override
    public boolean remoteDeleteImageLib(String faceserverip, String faceserverport, String deviceId, Byte editlibId) {
        String url = "http://" + faceserverip + ":" + faceserverport + "/appapi/faceserverapi/deleteImageLib";
        Map<String, String> param = new HashMap<>();
        param.put("deviceId", deviceId);
        param.put("libid", editlibId + "");
        String jsonstr = OKHttpUtil.httpPost(url, param);
        boolean ret = false;
        if (StringUtils.isNotEmpty(jsonstr)) {
            jsonstr = jsonstr.replaceAll("\\\\", "");
            jsonstr = jsonstr.substring(1, jsonstr.length() - 1);
            JSONObject jsonObject = JSON.parseObject(jsonstr);
            Boolean sucess = jsonObject.getBoolean("success");
            if (sucess) {
                ret = true;
            }
        }
        return ret;
    }


    @Override
    @Transactional
    public void logicDeleteBatchByIds(Long[] ids, FaceAppUserPojo userinfo) {

        Date now = DateUtils.getNowDate();
        FaceAppImageLibs faceAppImageLibs = null;
        FaceAppImageLibs oldFaceAppImageLibs = null;
        List<FaceAppImages> faceAppImagesList = null;
        for (int i = 0; i < ids.length; i++) {

            //删除图片
            oldFaceAppImageLibs = queryById(ids[i]);
            if (oldFaceAppImageLibs != null) {
                FaceAppImages faceAppImages_cond = new FaceAppImages();
                faceAppImages_cond.setLibId(oldFaceAppImageLibs.getLibId());
                faceAppImages_cond.setDelFlag("1");
                faceAppImagesList = faceAppImagesService.queryList(faceAppImages_cond);
                Long[] idds = {};
                int j = 0;
                if (!faceAppImagesList.isEmpty()) {
                    for (FaceAppImages faceAppImages : faceAppImagesList) {
                        idds[j] = Long.parseLong(faceAppImages.getId().toString());
                        j++;
                    }
                    faceAppImagesService.logicDeleteBatchByIds(idds, userinfo);
                }
            }
            //删除图片库
            faceAppImageLibs = new FaceAppImageLibs();
            if (userinfo != null) {
                faceAppImageLibs.setUpdateBy(userinfo.getId().toString());
            }
            faceAppImageLibs.setUpdateDate(now);

            faceAppImageLibs.setId(Integer.parseInt(ids[i].toString()));
            faceAppImageLibs.setDelFlag("0");
            super.updateNotNull(faceAppImageLibs);
        }
    }
}
