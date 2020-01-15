package com.pface.admin.modules.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.pface.admin.core.utils.OKHttpUtil;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.front.vo.FaceAppSnapListAssemblePojo;
import com.pface.admin.modules.front.vo.FaceAppSnapListPojo;
import com.pface.admin.modules.front.vo.FaceAppSnapListQueryParams;
import com.pface.admin.modules.front.vo.FaceAppUserPojo;
import com.pface.admin.modules.member.mapper.FaceAppSnapListMapper;
import com.pface.admin.modules.member.po.FaceAppImageLibs;
import com.pface.admin.modules.member.po.FaceAppImages;
import com.pface.admin.modules.member.po.FaceAppSnapList;
import com.pface.admin.modules.member.po.FaceAppUsescene;
import com.pface.admin.modules.member.service.*;
import com.pface.admin.modules.member.vo.FaceAppImagesVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FaceAppSnapListServiceImpl extends BaseService<FaceAppSnapList> implements FaceAppSnapListService {
    @Autowired
    FaceAppImagesService faceAppImagesService;
    @Value("${face.serverip}")
    private String faceserverip;

    @Value("${face.serverport}")
    private String faceserverport;

    @Autowired
    private FaceCommonService faceCommonService;
    @Autowired
    private FaceAppImageLibsService faceAppImageLibsService;
    @Autowired
    private FaceAppUsesceneService faceAppUsesceneService;
    @Autowired
    private FaceAppSnapListMapper faceAppSnapListMapper;
    public void pullSnapList() {
        FaceAppImages faceAppImages_cond = new FaceAppImages();
        faceAppImages_cond.setDelFlag("1");
        List<FaceAppImages> faceAppImagesList = faceAppImagesService.queryList(faceAppImages_cond);

        if (faceAppImagesList.isEmpty()) {
            log.info("没有要拉取的人脸抓拍数据。");
        } else {
            //转换成map
            Map<String, List<FaceAppImagesVo>> mapByUserid = new HashMap<>(); //<sysUserid,List<FaceAppImages>>，使用FaceAppImagesVo的目的是加入场景表的开始和结束时间
            String sysUserid = null;
            List<FaceAppImagesVo> tempValue = null;
            FaceAppImageLibs oneFaceAppImageLibs = null;
            FaceAppImagesVo faceAppImagesVo = null;
            FaceAppUsescene faceAppUsescene = null;
            for (FaceAppImages faceAppImages : faceAppImagesList) {

                //获取face_app_image_libs
                FaceAppImageLibs faceAppImageLibs_cond = new FaceAppImageLibs();
                faceAppImageLibs_cond.setSysUserid(faceAppImages.getSysUserid());
                faceAppImageLibs_cond.setDeviceId(faceAppImages.getDeviceId());
                faceAppImageLibs_cond.setLibId(faceAppImages.getLibId());
                oneFaceAppImageLibs = faceAppImageLibsService.queryOne(faceAppImageLibs_cond);

                faceAppImagesVo = new FaceAppImagesVo();
                try {
                    BeanUtils.copyProperties(faceAppImagesVo, faceAppImages);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                faceAppImagesVo.setSysSceneid(oneFaceAppImageLibs.getSysSceneid()); //这个属性，暂时没用到
                //获取face_app_usescene
                faceAppUsescene = faceAppUsesceneService.queryById(oneFaceAppImageLibs.getSysSceneid());
                faceAppImagesVo.setStartDate(faceAppUsescene.getStartDate());//这个属性，暂时没用到
                faceAppImagesVo.setEndDate(faceAppUsescene.getEndDate());//这个属性，暂时没用到
                faceAppImagesVo.setAsignStarttime(faceAppUsescene.getAsignStarttime());//这个属性，暂时没用到
                faceAppImagesVo.setAsignEndtime(faceAppUsescene.getAsignEndtime());//这个属性，暂时没用到

                sysUserid = faceAppImages.getSysUserid() + "";
                if (!mapByUserid.containsKey(sysUserid)) {
                    tempValue = new ArrayList<>();
                    tempValue.add(faceAppImagesVo);
                    mapByUserid.put(sysUserid, tempValue);
                } else {
                    (mapByUserid.get(sysUserid)).add(faceAppImagesVo);
                }
            }

            if (mapByUserid != null) {
                for (Map.Entry<String, List<FaceAppImagesVo>> me : mapByUserid.entrySet()) {
                    String sysUserid2 = me.getKey();

                    List<FaceAppImagesVo> values = me.getValue();
                    StringBuilder imgIds = new StringBuilder("");
                    int i = 0;
                    //int maxCommits = 500;
                    for (FaceAppImagesVo faio : values) {
                        imgIds.append("'").append(faio.getImgId()).append("'");  //imgId是全局唯一id，这里用它做为唯一值
                        if (i++ != values.size() -1){
                            imgIds.append(",");
                        }
                    }

                    //如果imgIds大于1000个，可以在这里分批次提交，以优化性能，这里暂时没做20200110

                    if (StringUtils.isNotEmpty(imgIds)){
                        String url = "http://" + faceserverip + ":" + faceserverport + "/appapi/faceserverapi/querySnapList";
                        Map<String, String> param = new HashMap<>();
                        param.put("imgIds", imgIds.toString());
                        String jsonstr = OKHttpUtil.httpPost(url, param);
                        if (StringUtils.isNotEmpty(jsonstr)) {
                            jsonstr = jsonstr.replaceAll("\\\\", "");
                            jsonstr = jsonstr.substring(1,jsonstr.length() -1);
                            JSONObject jsonObject = JSON.parseObject(jsonstr);
                            Boolean sucess = jsonObject.getBoolean("success");
                            if (sucess) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                List<FaceAppSnapList> faceAppSnapListList =  JSONObject.parseArray(jsonArray.toJSONString(), FaceAppSnapList.class);
                                String oldFaceSnapListId = null;
                                for (FaceAppSnapList faceAppSnapList : faceAppSnapListList ){
                                    oldFaceSnapListId = faceAppSnapList.getId() + ""; //这个id会映射过来
                                    faceAppSnapList.setId(null);//为了啊App端的创建create
                                    faceAppSnapList.setSysUserid(Integer.parseInt(sysUserid2));
                                    faceCommonService.setAppCommonField(faceAppSnapList,"create", faceCommonService.appFaceUser(faceserverip,faceserverport,"", sysUserid2));
                                    int retCreate = create(faceAppSnapList);
                                    if (retCreate >0){ //成功，则回写标志
                                        String url2 = "http://" + faceserverip + ":" + faceserverport + "/appapi/faceserverapi/updateOneFaceSnapList";
                                        Map<String, String> param2 = new HashMap<>();
                                        param2.put("id", oldFaceSnapListId);
                                        String jsonstr2 = OKHttpUtil.httpPost(url2, param2);
                                        if (StringUtils.isNotEmpty(jsonstr2)) {
                                            jsonstr2 = jsonstr2.replaceAll("\\\\", "");
                                            jsonstr2 = jsonstr2.substring(1, jsonstr2.length() - 1);
                                            JSONObject jsonObject2 = JSON.parseObject(jsonstr2);
                                            Boolean sucess2 = jsonObject2.getBoolean("success");
                                            if (sucess2) {
                                                log.info("拉取人脸抓拍数据回调成功，Server端face_snap_list.id；"+oldFaceSnapListId);
                                            }else{
                                                //不成功，则回滚，这里是分布式,执行物理删除
                                                Integer deleteFaceAppSnapListId = faceAppSnapList.getId();
                                                delete(queryById(deleteFaceAppSnapListId));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<FaceAppSnapListPojo> queryAppSnapList(PageQuery pageQuery, FaceAppSnapListQueryParams params) {
        return PageHelper.startPage(pageQuery)
                .doSelectPage(() -> faceAppSnapListMapper.queryAppSnapList(params));
    }

    @Override
    public List<FaceAppSnapListAssemblePojo > assembleAppSnapList(PageQuery pageQuery, FaceAppSnapListQueryParams params) {
        return PageHelper.startPage(pageQuery)
                .doSelectPage(() -> faceAppSnapListMapper.assembleAppSnapList(params));
    }

}
