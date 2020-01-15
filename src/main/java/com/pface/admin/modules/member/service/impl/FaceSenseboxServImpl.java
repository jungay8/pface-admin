package com.pface.admin.modules.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pface.admin.core.exception.BizException;
import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.core.utils.RedisUtils;
import com.pface.admin.core.utils.ResultCodeEnum;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.jiekou.Constants.Faceconstant;
import com.pface.admin.modules.jiekou.service.FaceSenseboxInterfaceService;
import com.pface.admin.modules.jiekou.utils.BizUtils;
import com.pface.admin.modules.member.dto.FaceSenseboxExDTO;
import com.pface.admin.modules.member.mapper.FaceSenseboxMapper;
import com.pface.admin.modules.member.po.FaceCamera;
import com.pface.admin.modules.member.po.FaceDeviceChannelRes;
import com.pface.admin.modules.member.po.FaceSensebox;
import com.pface.admin.modules.member.po.FaceUser;
import com.pface.admin.modules.member.service.FaceCameraService;
import com.pface.admin.modules.member.service.FaceCommonService;
import com.pface.admin.modules.member.service.FaceDeviceChannelResService;
import com.pface.admin.modules.member.service.FaceSenseboxService;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 智能盒子业务Service实现
 */
@Slf4j
@Service
public class FaceSenseboxServImpl extends BaseService<FaceSensebox> implements FaceSenseboxService {
    @Autowired
    private UserService userService;
    @Autowired
    private FaceDeviceChannelResService faceDeviceChannelResService;
    @Autowired
    private FaceCameraService faceCameraService;
    @Override
    public List<FaceSensebox> queryAll() {
        FaceSensebox faceSensebox_cond = new FaceSensebox();
        faceSensebox_cond.setDelFlag("1");
        return super.queryList(faceSensebox_cond);
    }

    @Autowired
    private FaceCommonService faceCommonService;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private FaceSenseboxInterfaceService faceSenseboxInterfaceService;

    @Autowired
    private FaceSenseboxMapper faceSenseboxMapper;
    /**
     * 拉取智能盒子的信息
     *
     * @param faceSensebox
     */
    @Override
    public void pullSenseboxInfo(FaceSensebox faceSensebox) {
        try {
            //6.3 获取设备的版本信息
            Map<String, Object> params = new HashMap<>();
            params.put("msg_id", "1281");
            String jsonstr = faceCommonService.invokeInterface(faceSensebox, params);
            JSONObject obj = JSON.parseObject(jsonstr);
            int code = obj.getIntValue("code");
            if (code == 0) {
                String device_id = obj.getJSONObject("data").getString("device_id");
                faceSensebox.setDeviceId(device_id);
                faceSensebox.setSerialId(obj.getJSONObject("data").getString("serial_id"));
                faceSensebox.setVersion(obj.getJSONObject("data").getString("version"));
                faceSensebox.setWebVersion(obj.getJSONObject("data").getString("web_version"));
            }

            //6.4 获取设备的存储信息
            Map<String, Object> params2 = new HashMap<>();
            params2.put("msg_id", "1288");
            String jsonstr2 = faceCommonService.invokeInterface(faceSensebox, params2);
            JSONObject obj2 = JSON.parseObject(jsonstr2);
            int code2 = obj.getIntValue("code");
            if (code2 == 0) {
                faceSensebox.setCurStorage(obj2.getJSONObject("data").getIntValue("cur_storage"));
                faceSensebox.setMaxStorage(obj2.getJSONObject("data").getIntValue("max_storage"));
                faceSensebox.setStorageStrategy(obj2.getJSONObject("data").getByteValue("storage_strategy"));
            }

            //7.1 获取http信息
            Map<String, Object> params3 = new HashMap<>();
            params3.put("msg_id", "1299");
            String jsonstr3 = faceCommonService.invokeInterface(faceSensebox, params3);
            JSONObject obj3 = JSON.parseObject(jsonstr3);
            int code3 = obj3.getIntValue("code");
            if (code3 == 0) {
                faceSensebox.setHttpKey(obj3.getJSONObject("data").getString("key"));
                faceSensebox.setHttpUrl(obj3.getJSONObject("data").getString("url"));
            }

            //4.5 相机模式
            Map<String, Object> params4 = new HashMap<>();
            params4.put("msg_id", "519");
            String jsonstr4 = faceCommonService.invokeInterface(faceSensebox, params4);
            JSONObject obj4 = JSON.parseObject(jsonstr4);
            int code4 = obj4.getIntValue("code");
            if (code4 == 0) {
                faceSensebox.setCameraMode(obj4.getJSONObject("data").getString("mode"));
                faceSensebox.setStartno(obj4.getJSONObject("data").getIntValue("start_no"));
                faceSensebox.setEndno(obj4.getJSONObject("data").getIntValue("end_no"));
            }
        }catch (Exception ex){
           ex.printStackTrace();
           throw new BizException(ResultCodeEnum.SESENSE_BOX_COLLECTION_ERROR);
        }
    }

    @Override
    @Transactional
    public int myCreate(FaceSensebox faceSensebox) {
        int ret = 0;
        faceCommonService.setCommonField(faceSensebox, "create");
        ret = create(faceSensebox);
        if (ret>0) {
            FaceDeviceChannelRes faceDeviceChannelRes = null;
            for (int i = faceSensebox.getStartno(); i <= faceSensebox.getEndno(); i++) {
                faceDeviceChannelRes = new FaceDeviceChannelRes();
                faceDeviceChannelRes.setDeviceId(faceSensebox.getDeviceId());
                faceDeviceChannelRes.setChannel((byte) i);
                faceDeviceChannelRes.setIssetup("no");
                faceCommonService.setCommonField(faceDeviceChannelRes, "create");
                faceDeviceChannelResService.create(faceDeviceChannelRes);
            }
        }
       return ret;
    }

    @Override
    @Deprecated
    @Transactional
    public void checkdeviceid(@Validated FaceSensebox faceSensebox) {
        String device_id = faceSensebox.getDeviceId();
        FaceSensebox faceSensebox1_cond = new FaceSensebox();
        faceSensebox1_cond.setDeviceId(device_id);
        faceSensebox1_cond.setDelFlag("1");
        List<FaceSensebox> faceSenseboxList = queryList(faceSensebox1_cond);
        if (!faceSenseboxList.isEmpty() && faceSenseboxList.size()>1){//有重复deviceid，这里去重，这里条件：faceSenseboxList.size()>1,而不是faceSenseboxList.size()>0，是因为上面已经提交了数据，这里是马后炮的做法
            String newDevice_id = device_id + faceSensebox.getId();
            FaceSensebox updateFaceSensebox = new FaceSensebox();
            updateFaceSensebox.setId(faceSensebox.getId());
            updateFaceSensebox.setDeviceId(newDevice_id);
            int ret = updateNotNull(updateFaceSensebox);

            //修正资源表中device_id
            FaceDeviceChannelRes res_cond = new FaceDeviceChannelRes();
            res_cond.setDeviceId(device_id);
            res_cond.setDeviceId("1");
            List<FaceDeviceChannelRes> list = faceDeviceChannelResService.queryList(res_cond);
            FaceDeviceChannelRes res = null;
            for (FaceDeviceChannelRes r:list){
                res = new FaceDeviceChannelRes();
                res.setId(r.getId());
                res.setDeviceId(newDevice_id);
                faceCommonService.setCommonField(res, "update");
                faceDeviceChannelResService.updateNotNull(res);
            }
            //远程修正智能盒子的id
            if (ret>0) {
                Map<String, Object> mapParams = new HashMap<>();
                mapParams.put("msg_id", 1300); //接口6.13修改设备id
                mapParams.put("device_id", newDevice_id);
                faceCommonService.invokeInterface(faceSensebox.getId() + "", mapParams);
            }
        }
    }

    @Override
    public List<FaceSenseboxExDTO> queryAuthedSenseboxOfUser(String sysUserid) {
        if (StringUtils.isEmpty(sysUserid)){
            return null;
        }
        Map<String , String> param = new HashMap<>();
        param.put("sysUserid", sysUserid);
        return faceSenseboxMapper.queryAuthedSenseboxOfUser(param);
    }

//    @Override
//    public FaceSensebox maxminAlgorithmByAuthdate(Integer sysUserid) {
//
//        return faceSenseboxMapper.maxminAlgorithmByAuthdate();
//    }

    @Override
    @Transactional
    public void logicDeleteBatchByIds(Long[] ids) {

        Date now = DateUtils.getNowDate();
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.queryOne(new User().setUsername(username));

        FaceSensebox faceSensebox = null;

        for(int i=0;i<ids.length;i++) {
            faceSensebox = new FaceSensebox();
            if (user!=null && user.getLocked()!=null){
                faceSensebox.setUpdateBy(user.getId().toString());
            }
            faceSensebox.setUpdateDate(now);

            faceSensebox.setId(Integer.parseInt(ids[i].toString()));
            faceSensebox.setDelFlag("0");
            super.updateNotNull(faceSensebox);

            //删除其通道资源
            FaceSensebox oneFaceSensebox = queryById(Integer.parseInt(ids[i].toString()));
            if (oneFaceSensebox!=null){
                FaceDeviceChannelRes res_cond = new FaceDeviceChannelRes();
                res_cond.setDeviceId(oneFaceSensebox.getDeviceId());
                res_cond.setDeviceId("1");
                List<FaceDeviceChannelRes> list = faceDeviceChannelResService.queryList(res_cond);
                FaceDeviceChannelRes res = null;
                for (FaceDeviceChannelRes r:list){
                    res = new FaceDeviceChannelRes();
                    res.setId(r.getId());
                    res.setDelFlag("0");
                    faceCommonService.setCommonField(res, "update");
                    faceDeviceChannelResService.updateNotNull(res);
                }
            }
        }
    }


}
