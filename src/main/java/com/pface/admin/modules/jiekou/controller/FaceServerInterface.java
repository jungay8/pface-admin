package com.pface.admin.modules.jiekou.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pface.admin.core.utils.*;
import com.pface.admin.modules.base.query.ResultCode;
import com.pface.admin.modules.jiekou.pojo.FaceUserPojo;
import com.pface.admin.modules.jiekou.utils.BizUtils;
import com.pface.admin.modules.jiekou.utils.HttpClientHelper;
import com.pface.admin.modules.jiekou.utils.HttpException;
import com.pface.admin.modules.member.dto.FaceSenseboxDTO;
import com.pface.admin.modules.member.dto.FaceSenseboxExDTO;
import com.pface.admin.modules.member.po.*;
import com.pface.admin.modules.member.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.*;

@Slf4j
@Api(tags= "平台端发布的接口", description = "平台端发布的接口", value = "平台端发布的接口")
@RestController
@RequestMapping("/appapi/faceserverapi")
public class FaceServerInterface {

    @Autowired
    FaceUserService faceUserService ;
    @Autowired
    FaceUserChannelResService faceUserChannelResService;
    @Autowired
    FaceSenseboxService faceSenseboxService;
    @Autowired
    private FaceCommonService faceCommonService;
    @Autowired
    private FaceSnapListService faceSnapListService;
    @ApiOperation(value="获取客户信息", position = 1, notes="获取客户信息")
    @RequestMapping(value="/userinfo",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operator", value = "账户", required = false, dataType = "String",paramType="form",defaultValue= "cs1001")
            ,@ApiImplicitParam(name = "id", value = "id", required = false, dataType = "String",paramType="form",defaultValue= "cs1001")
    })
    @ResponseBody
    public String  userinfo(@RequestParam("operator")String operator
                                ,@RequestParam("id") String id){
        FaceUser faceUser_cond = new FaceUser();
        Result result = null;
        if (StringUtils.isEmpty(operator) && StringUtils.isEmpty(id)){
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
        }
        if ((StringUtils.isNotEmpty(operator) && StringUtils.isEmpty(id))||
                (StringUtils.isNotEmpty(operator) && StringUtils.isNotEmpty(id))) {
            faceUser_cond.setOperator(operator);
        }

        if (StringUtils.isEmpty(operator) && StringUtils.isNotEmpty(id)){
            faceUser_cond.setId(Integer.parseInt(id));
        }

        FaceUser faceUser = faceUserService.queryOne(faceUser_cond);
        FaceUserPojo faceUserPojo = new FaceUserPojo();
        try {
            if (faceUser !=null) {
                BeanUtils.copyProperties(faceUserPojo, faceUser);
                result = Result.success(faceUserPojo);
            }else{
                result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            }
        } catch (IllegalAccessException e) {
            result = Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            result = Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }

        return JSON.toJSONString(result);
    }

    @ApiOperation(value="获取客户已授权通道资源的所有智能设备", position = 1, notes="获取客户已授权通道资源的所有智能设备")
    @RequestMapping(value="/queryOneUserAllSensebox",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUserid", value = "客户ID", required = true, dataType = "String",paramType="form",defaultValue= "1")
    })
    @ResponseBody
    public String  queryOneUserAllSensebox(@RequestParam("sysUserid") String sysUserid){
        FaceUserChannelRes faceUserChannelRes_cond = new FaceUserChannelRes();
        faceUserChannelRes_cond.setSysUserid(Integer.parseInt(sysUserid));
        faceUserChannelRes_cond.setDelFlag("1");

        List<FaceUserChannelRes> list = faceUserChannelResService.queryList(faceUserChannelRes_cond);
        Result result = null;
        if (!list.isEmpty()){
            if (list.size() == 1){
                FaceSensebox faceSensebox_cond = new FaceSensebox();
                faceSensebox_cond.setDeviceId(list.get(0).getDeviceId());
                faceSensebox_cond.setDelFlag("1");
                FaceSensebox faceSensebox = faceSenseboxService.queryOne(faceSensebox_cond);
                FaceSenseboxDTO faceSenseboxDTO = new FaceSenseboxDTO();

                try {
                    BeanUtils.copyProperties(faceSenseboxDTO, faceSensebox);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                if (faceSenseboxDTO!=null){
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.add(faceSenseboxDTO);
                    result = Result.success(jsonArray);
                }else{
                    result = Result.failure(ResultCodeEnum.PARAM_ERROR);
                }
            }else{
                //查询授权给用户的Sensebox
                List<FaceSenseboxExDTO> faceSenseboxList = faceSenseboxService.queryAuthedSenseboxOfUser(sysUserid + "");
                FaceSenseboxDTO faceSenseboxDTO = null;
                List<FaceSenseboxDTO> faceSenseboxDTOS = null;
                if (faceSenseboxList!=null && !faceSenseboxList.isEmpty()){
                    faceSenseboxDTOS = new ArrayList<>();
                    for (FaceSensebox faceSensebox:faceSenseboxList){
                        try {
                            faceSenseboxDTO = new FaceSenseboxDTO();
                            BeanUtils.copyProperties(faceSenseboxDTO, faceSensebox);
                            faceSenseboxDTOS.add(faceSenseboxDTO);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }

                }

                if (faceSenseboxDTOS!=null && !faceSenseboxDTOS.isEmpty()){
                    result = Result.success(JSONArray.parseArray(JSON.toJSONString(faceSenseboxDTOS))); //转jsonarray传输
                }else{
                    result = Result.failure(ResultCodeEnum.PARAM_ERROR);
                }
            }
        }else{
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
        }
        return JSON.toJSONString(result);
    }


    @ApiOperation(value="查询一个智能设备", position = 1, notes="查询一个智能设备")
    @RequestMapping(value="/queryOneSensebox",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "智能设备系统ID", required = false, dataType = "String",paramType="form",defaultValue= "1")
            ,@ApiImplicitParam(name = "deviceId", value = "智能设备ID", required = false, dataType = "String",paramType="form",defaultValue= "cs10001")
    })
    @ResponseBody
    public String  queryOneSensebox(@RequestParam("id") String id,
                @RequestParam("deviceId") String deviceId){
        Result result = null;
        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(deviceId)){
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
        }

        FaceSensebox faceSensebox = null;
        if ((StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(deviceId)) ||
                (StringUtils.isNotEmpty(id) && StringUtils.isEmpty(deviceId))){
            faceSensebox = faceSenseboxService.queryById(id);
        }

        if (StringUtils.isEmpty(id) && StringUtils.isNotEmpty(deviceId)){
            FaceSensebox faceSensebox_cond = new FaceSensebox();
            faceSensebox_cond.setDeviceId(deviceId);
            faceSensebox = faceSenseboxService.queryOne(faceSensebox_cond);
        }

        FaceSenseboxDTO faceSenseboxDTO = new FaceSenseboxDTO();

        if (faceSensebox!=null){
            try {
                BeanUtils.copyProperties(faceSenseboxDTO, faceSensebox);
                if (faceSenseboxDTO!=null && faceSenseboxDTO.getId()!=null){
                    result = Result.success(faceSenseboxDTO);
                }else{
                    result = Result.failure(ResultCodeEnum.PARAM_ERROR);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }else{
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
        }

        return JSON.toJSONString(result);
    }


    @ApiOperation(value="创建人像库", position = 1, notes="创建人像库")
    @RequestMapping(value="/createImageLib",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysDeviceId", value = "智能设备系统id", required = true, dataType = "String",paramType="form",defaultValue= "1")
            ,@ApiImplicitParam(name = "editlibname", value = "人像库名称", required = true, dataType = "String",paramType="form",defaultValue= "test")
            ,@ApiImplicitParam(name = "editlibtype", value = "人像库类型：1黑名单库2白名单库", required = true, dataType = "Integer",paramType="form",defaultValue= "2")
    })
    @ResponseBody
    public String createImageLib(@RequestParam("sysDeviceId") String sysDeviceId,
                                 @RequestParam("editlibname") String editlibname,
                                 @RequestParam("editlibtype") Integer editlibtype){
        Result result = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("msg_id", "1025");
            params.put("lib_name", editlibname);
            params.put("lib_type", editlibtype);
            String jsonstr = faceCommonService.invokeInterface(sysDeviceId, params);
            JSONObject jsonObject = JSON.parseObject(jsonstr);
            int code = jsonObject.getIntValue("code");
            if (code == 0){
                 result = Result.success(jsonObject.getJSONObject("data"));
            }else {
                result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            }
        }catch (Exception ex){
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            ex.printStackTrace();
        }
        return JSON.toJSONString(result);
    }


    @ApiOperation(value="查询所有人像库", position = 1, notes="查询所有人像库")
    @RequestMapping(value="/queryAllImageLibs",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "智能设备id", required = true, dataType = "String",paramType="form",defaultValue= "1")
    })
    @ResponseBody
    public String queryAllImageLibs(@RequestParam("deviceId") String deviceId){
        Result result = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("msg_id", "1028");
            FaceSensebox faceSensebox_cond = new FaceSensebox();
            faceSensebox_cond.setDeviceId(deviceId);
            faceSensebox_cond.setDelFlag("1");
            FaceSensebox faceSensebox = faceSenseboxService.queryOne(faceSensebox_cond);
            if (faceSensebox!=null){
                String jsonstr = faceCommonService.invokeInterface(faceSensebox, params);
                JSONObject jsonObject = JSON.parseObject(jsonstr);
                int code = jsonObject.getIntValue("code");
                if (code == 0){
                    result = Result.success(jsonObject.getJSONArray("data"));
                }else {
                    result = Result.failure(ResultCodeEnum.PARAM_ERROR);
                }
            }

        }catch (Exception ex){
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            ex.printStackTrace();
        }
        return JSON.toJSONString(result);
    }

    @ApiOperation(value="有条件查询人像库", position = 1, notes="有条件查询人像库")
    @RequestMapping(value="/queryImageLibs",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "智能设备id", required = true, dataType = "String",paramType="form",defaultValue= "1")
            ,@ApiImplicitParam(name = "libId", value = "人像库ID", required = false, dataType = "String",paramType="form")
            ,@ApiImplicitParam(name = "libName", value = "人像库名称", required = false, dataType = "String",paramType="form")

    })
    @ResponseBody
    public String queryImageLibs(@RequestParam("deviceId") String deviceId,
                                 @RequestParam("libId") String libId,
                                 @RequestParam("libName") String libName
    ){
        Result result = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("msg_id", "1034");
            if (StringUtils.isNotEmpty(libId)){
                params.put("lib_id", Integer.valueOf(libId));
            }
            if (StringUtils.isNotEmpty(libName)){
                params.put("lib_name", libName);
            }
            FaceSensebox faceSensebox_cond = new FaceSensebox();
            faceSensebox_cond.setDeviceId(deviceId);
            faceSensebox_cond.setDelFlag("1");
            FaceSensebox faceSensebox = faceSenseboxService.queryOne(faceSensebox_cond);
            if (faceSensebox!=null){
                String jsonstr = faceCommonService.invokeInterface(faceSensebox, params);
                JSONObject jsonObject = JSON.parseObject(jsonstr);
                int code = jsonObject.getIntValue("code");
                if (code == 0){
                    result = Result.success(jsonObject.getJSONObject("data").getJSONArray("record"));
                }else {
                    result = Result.failure(ResultCodeEnum.PARAM_ERROR);
                }
            }

        }catch (Exception ex){
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            ex.printStackTrace();
        }
        return JSON.toJSONString(result);
    }


    @ApiOperation(value="修改人像库", position = 1, notes="修改人像库")
    @RequestMapping(value="/updateImageLib",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "智能设备id", required = true, dataType = "String",paramType="form",defaultValue= "1")
            ,@ApiImplicitParam(name = "libid", value = "库id", required = true, dataType = "String",paramType="form",defaultValue= "1")
            ,@ApiImplicitParam(name = "libname", value = "库名称", required = true, dataType = "String",paramType="form",defaultValue= "1")
            ,@ApiImplicitParam(name = "libtype", value = "库类型", required = true, dataType = "String",paramType="form",defaultValue= "1")

    })
    @ResponseBody
    public String updateImageLib(@RequestParam("deviceId") String deviceId,
                                 @RequestParam("libid") String libid,
                                 @RequestParam("libname") String libname,
                                 @RequestParam("libtype") String libtype
                                 ){
        Result result = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("msg_id", "1027");
            params.put("lib_id", Integer.valueOf(libid));
            params.put("lib_name", libname);
            params.put("lib_type", Integer.valueOf(libtype));
            FaceSensebox faceSensebox_cond = new FaceSensebox();
            faceSensebox_cond.setDeviceId(deviceId);
            faceSensebox_cond.setDelFlag("1");
            FaceSensebox faceSensebox = faceSenseboxService.queryOne(faceSensebox_cond);
            if (faceSensebox!=null){
                String jsonstr = faceCommonService.invokeInterface(faceSensebox, params);
                JSONObject jsonObject = JSON.parseObject(jsonstr);
                int code = jsonObject.getIntValue("code");
                if (code == 0){
                    result = Result.success(jsonObject.getJSONObject("data"));
                }else {
                    result = Result.failure(ResultCodeEnum.PARAM_ERROR);
                }
            }

        }catch (Exception ex){
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            ex.printStackTrace();
        }
        return JSON.toJSONString(result);
    }


    @ApiOperation(value="删除人像库", position = 1, notes="删除人像库")
    @RequestMapping(value="/deleteImageLib",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "智能设备id", required = true, dataType = "String",paramType="form",defaultValue= "1")
            ,@ApiImplicitParam(name = "libid", value = "库id", required = true, dataType = "String",paramType="form",defaultValue= "1")

    })
    @ResponseBody
    public String deleteImageLib(@RequestParam("deviceId") String deviceId,
                                 @RequestParam("libid") String libid){

        Result result = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("msg_id", "1026");
            params.put("lib_id", Integer.valueOf(libid));
            FaceSensebox faceSensebox_cond = new FaceSensebox();
            faceSensebox_cond.setDeviceId(deviceId);
            faceSensebox_cond.setDelFlag("1");
            FaceSensebox faceSensebox = faceSenseboxService.queryOne(faceSensebox_cond);
            if (faceSensebox!=null){
                String jsonstr = faceCommonService.invokeInterface(faceSensebox, params);
                JSONObject jsonObject = JSON.parseObject(jsonstr);
                int code = jsonObject.getIntValue("code");
                if (code == 0){
                    result = Result.success(jsonObject.getJSONObject("data"));
                }else {
                    result = Result.failure(ResultCodeEnum.PARAM_ERROR);
                }
            }

        }catch (Exception ex){
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            ex.printStackTrace();
        }
        return JSON.toJSONString(result);
    }

    //=============================================人脸图片start=======================================================

    @ApiOperation(value="单张人脸图片查询", position = 1, notes="单张人脸图片查询")
    @RequestMapping(value="/queryOneImage",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "智能设备id", required = true, dataType = "String",paramType="form",defaultValue= "1")
            ,@ApiImplicitParam(name = "imgId", value = "图片id", required = true, dataType = "String",paramType="form")
            ,@ApiImplicitParam(name = "libId", value = "库id", required = true, dataType = "String",paramType="form",defaultValue= "1")
    })
    @ResponseBody
    public String queryOneImage(@RequestParam("deviceId") String deviceId,
                                @RequestParam("imgId") String imgId,
                                 @RequestParam("libId") String libId){

        Result result = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("msg_id", "1032");
            params.put("img_id", imgId);
            params.put("lib_id", Integer.valueOf(libId));
            FaceSensebox faceSensebox_cond = new FaceSensebox();
            faceSensebox_cond.setDeviceId(deviceId);
            faceSensebox_cond.setDelFlag("1");
            FaceSensebox faceSensebox = faceSenseboxService.queryOne(faceSensebox_cond);
            if (faceSensebox!=null){
                String jsonstr = faceCommonService.invokeInterface(faceSensebox, params);
                JSONObject jsonObject = JSON.parseObject(jsonstr);
                int code = jsonObject.getIntValue("code");
                if (code == 0){
                    result = Result.success(jsonObject.getJSONObject("data"));
                }else {
                    result = Result.failure(ResultCodeEnum.PARAM_ERROR);
                }
            }

        }catch (Exception ex){
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            ex.printStackTrace();
        }
        return JSON.toJSONString(result);
    }




    @ApiOperation(value="单张人脸图片属性修改", position = 1, notes="单张人脸图片属性修改")
    @RequestMapping(value="/updateImage",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "智能设备id", required = true, dataType = "String",paramType="form",defaultValue= "cs10001")
            ,@ApiImplicitParam(name = "ImgId", value = "库名称", required = true, dataType = "String",paramType="form",defaultValue= "1")
            ,@ApiImplicitParam(name = "libId", value = "库id", required = true, dataType = "String",paramType="form",defaultValue= "1")
            ,@ApiImplicitParam(name = "personIdcard", value = "证件号", required = false, dataType = "String",paramType="form")
            ,@ApiImplicitParam(name = "personName", value = "姓名", required = false, dataType = "String",paramType="form")
            ,@ApiImplicitParam(name = "personGender", value = "性别", required = false, dataType = "String",paramType="form")
            ,@ApiImplicitParam(name = "personAge", value = "年龄", required = false, dataType = "String",paramType="form")
            ,@ApiImplicitParam(name = "personAddr", value = "地址", required = false, dataType = "String",paramType="form")
    })
    @ResponseBody
    public String updateImage(@RequestParam("deviceId") String deviceId,
                              @RequestParam("imgId") String imgId,
                              @RequestParam("libId") String libId,
                              @RequestParam("personIdcard") String personIdcard,
                              @RequestParam("personName") String personName,
                              @RequestParam("personGender") String personGender,
                              @RequestParam("personAge") String personAge,
                              @RequestParam("personAddr") String personAddr

    ){
        Result result = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("msg_id", "1033");
            params.put("lib_id", Integer.valueOf(libId));
            params.put("img_id", imgId);
            params.put("person_idcard", personIdcard);
            params.put("person_name", personName);
            params.put("person_gender", personGender);
            params.put("person_age", personAge);
            params.put("person_addr", personAddr);
            FaceSensebox faceSensebox_cond = new FaceSensebox();
            faceSensebox_cond.setDeviceId(deviceId);
            faceSensebox_cond.setDelFlag("1");
            FaceSensebox faceSensebox = faceSenseboxService.queryOne(faceSensebox_cond);
            if (faceSensebox!=null){
                String jsonstr = faceCommonService.invokeInterface(faceSensebox, params);
                JSONObject jsonObject = JSON.parseObject(jsonstr);
                int code = jsonObject.getIntValue("code");
                if (code == 0){
                    result = Result.success(jsonObject.getJSONObject("data"));
                }else {
                    result = Result.failure(ResultCodeEnum.PARAM_ERROR);
                }
            }

        }catch (Exception ex){
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            ex.printStackTrace();
        }
        return JSON.toJSONString(result);
    }


    @ApiOperation(value="删除单张人脸图片", position = 1, notes="删除单张人脸图片")
    @RequestMapping(value="/deleteImage",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "智能设备id", required = true, dataType = "String",paramType="form",defaultValue= "cs10001")
            ,@ApiImplicitParam(name = "imgId", value = "图片id", required = true, dataType = "String",paramType="form")
            ,@ApiImplicitParam(name = "libId", value = "库id", required = true, dataType = "String",paramType="form")
    })
    @ResponseBody
    public String deleteImage(@RequestParam("deviceId") String deviceId,
                              @RequestParam("imgId") String imgId,
                              @RequestParam("libId") String libId){

        Result result = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("msg_id", "1031");
            params.put("img_id", imgId);
            params.put("lib_id", Integer.valueOf(libId));
            FaceSensebox faceSensebox_cond = new FaceSensebox();
            faceSensebox_cond.setDeviceId(deviceId);
            faceSensebox_cond.setDelFlag("1");
            FaceSensebox faceSensebox = faceSenseboxService.queryOne(faceSensebox_cond);
            if (faceSensebox!=null){
                String jsonstr = faceCommonService.invokeInterface(faceSensebox, params);
                JSONObject jsonObject = JSON.parseObject(jsonstr);
                int code = jsonObject.getIntValue("code");
                if (code == 0){
                    result = Result.success(jsonObject.getJSONObject("data"));
                }else {
                    result = Result.failure(ResultCodeEnum.PARAM_ERROR);
                }
            }

        }catch (Exception ex){
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            ex.printStackTrace();
        }
        return JSON.toJSONString(result);
    }


    @ApiOperation(value="formApi-3.7 人脸入库", position = 5,notes="formApi-3.7 人脸入库")
    @RequestMapping(value="/uploadoneimage",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysDeviceId", value = "智能盒子系统id", required = true, dataType = "String",paramType="form",defaultValue= "1")
            ,@ApiImplicitParam(name = "libId", value = "人像库id", required = true, dataType = "Integer",paramType="form", defaultValue = "1")
            ,@ApiImplicitParam(name = "imgId", value = "图片ID", required = true, dataType = "String",paramType="form")

    })
    @ResponseBody
    public String  uploadoneimage(@RequestParam(value = "sysDeviceId", required = true)String sysDeviceId,
                                  @RequestParam(value = "libId", required = true)Integer lib_id,
                                  @RequestParam(value = "imgId", required = true)String img_id,
                                  @RequestParam(value = "file", required = true) MultipartFile file){
        Result result = null;
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("tmp", null);
            file.transferTo(tmpFile);
            Map<String, File> fileMap = new HashMap<>();
            fileMap.put("img",tmpFile);

//            String jsonStr = "{\"msg_id\":\"1029\",\"lib_id\":"+lib_id+"}";
//            Map<String,String> params = JSON.parseObject(jsonStr,Map.class);
            Map<String,String> params = new HashMap<>();
            params.put("msg_id", "1029");
            params.put("lib_id", lib_id+"");
            params.put("img_id", img_id);
            String url = faceCommonService.getAccessSenseboxUrlByForm(Long.valueOf(sysDeviceId));
            String sessionId = faceCommonService.getSessionid(Long.valueOf(sysDeviceId));
            String response = HttpClientHelper.postForm(url,fileMap,params,sessionId);
            tmpFile.deleteOnExit();

            JSONObject jsonObject = JSON.parseObject(BizUtils.convertResponse(response));
            int code = jsonObject.getIntValue("code");
            if (code == 0){
                result = Result.success(jsonObject.getJSONObject("data"));
            }else {
                result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            }
        } catch (HttpException e) {
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            log.info(e.toJsonStr());
        }
        catch (IOException ex){
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            ex.getMessage();
        }

        return JSON.toJSONString(result);
    }

    //=============================================人脸图片end=======================================================

    //=============================================face_snap_list -- start===========================================
    @ApiOperation(value="查询抓取记录", position = 1, notes="查询抓取记录")
    @RequestMapping(value="/querySnapList",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imgIds", value = "图片ids,以,分割", required = true, dataType = "String",paramType="form")
    })
    @ResponseBody
    public String querySnapList(@RequestParam("imgIds") String imgIds){

        Result result = null;
        try {

            Example example = new Example(FaceSnapList.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andCondition(" img_id in ("+imgIds+")");
            criteria.andEqualTo("delFlag","1");
            List<FaceSnapList> faceSnapLists = faceSnapListService.queryListByExample(example);
            if (!faceSnapLists.isEmpty()){
                result = Result.success(JSONArray.parseArray(JSON.toJSONString(faceSnapLists)));//转jsonarray传输
            }else{
                result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            }

        }catch (Exception ex){
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            ex.printStackTrace();
        }
        return JSON.toJSONString(result);
    }
    @ApiOperation(value="逻辑删除一条人脸抓取记录", position = 1, notes="逻辑删除一条人脸抓取记录")
    @RequestMapping(value="/updateOneFaceSnapList",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "图片imgId", required = true, dataType = "String",paramType="form")
    })
    @ResponseBody
    public String updateOneFaceSnapList(@RequestParam("id") String id){

        Result result = null;
        try {
            FaceSnapList updateFaceSnapList = new FaceSnapList();
            updateFaceSnapList.setId(Integer.parseInt(id));
            updateFaceSnapList.setDelFlag("0");
            updateFaceSnapList.setRemark("逻辑删除成功");
            faceCommonService.setCommonField(updateFaceSnapList, "update");
            int ret = faceSnapListService.updateNotNull(updateFaceSnapList);
            if (ret>0){
                result = Result.success();
            }else{
                result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            }

        }catch (Exception ex){
            result = Result.failure(ResultCodeEnum.PARAM_ERROR);
            ex.printStackTrace();
        }
        return JSON.toJSONString(result);
    }

}
