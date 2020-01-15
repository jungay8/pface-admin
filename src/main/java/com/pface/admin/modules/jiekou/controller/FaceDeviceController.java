package com.pface.admin.modules.jiekou.controller;

import com.alibaba.fastjson.JSON;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.jiekou.Constants.Faceconstant;
import com.pface.admin.modules.jiekou.utils.BizUtils;
import com.pface.admin.modules.jiekou.utils.HttpClientHelper;
import com.pface.admin.modules.jiekou.utils.HttpException;
import com.pface.admin.modules.member.service.FaceCommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags= "无感人脸设备接口", description = "无感人脸设备接口", value = "设备")
@RestController
@RequestMapping("/appapi/face/device")
public class FaceDeviceController {
    @Autowired
    private FaceCommonService faceCommonService;

    @ApiOperation(value="6.3 设备节点信息", position = 1, notes="设备节点信息")
    @RequestMapping(value="/deviceinfo",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysDeviceId", value = "智能盒子系统id", required = true, dataType = "String",paramType="form",defaultValue= "1")
    })
    @ResponseBody
    public String  deviceinfo(@RequestParam("sysDeviceId")String sysDeviceId){
        Map<String, Object> params = new HashMap<>();
        params.put("msg_id", "1281");
        return faceCommonService.invokeInterface(sysDeviceId, params);
    }

    @ApiOperation(value="6.4 存储策略查询", position = 2, notes="存储策略查询")
    @RequestMapping(value="/storageway",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysDeviceId", value = "智能盒子系统id", required = true, dataType = "String",paramType="form",defaultValue= "1")
    })
    @ResponseBody
    public String  storageway(@RequestParam("sysDeviceId")String sysDeviceId){
        Map<String, Object> params = new HashMap<>();
        params.put("msg_id", "1288");
        return faceCommonService.invokeInterface(sysDeviceId, params);
    }

    @ApiOperation(value="6.12 设备id查询", position = 3, notes="设备id查询")
    @RequestMapping(value="/deviceid",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysDeviceId", value = "智能盒子系统id", required = true, dataType = "String",paramType="form",defaultValue= "1")
    })
    @ResponseBody
    public String  deviceid(@RequestParam(value = "sysDeviceId",required = true)String sysDeviceId){
        Map<String, Object> params = new HashMap<>();
        params.put("msg_id", "1301");
        return faceCommonService.invokeInterface(sysDeviceId, params);
    }


    //相机start==================================================================
    @ApiOperation(value="4.1 相机列表", position = 1, notes="相机列表")
    @RequestMapping(value="/cameralist",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysDeviceId", value = "智能盒子系统id", required = true, dataType = "String",paramType="form",defaultValue= "1")
    })
    @ResponseBody
    public String cameralist(@RequestParam("sysDeviceId")String sysDeviceId){
        Map<String, Object> params = new HashMap<>();
        params.put("msg_id", "516");
        return faceCommonService.invokeInterface(sysDeviceId, params);
    }

    @ApiOperation(value="4.5 相机接入模式", position = 1, notes="相机接入模式")
    @RequestMapping(value="/cameramode",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysDeviceId", value = "智能盒子系统id", required = true, dataType = "String",paramType="form",defaultValue= "1")
    })
    @ResponseBody
    public String cameramode(@RequestParam("sysDeviceId")String sysDeviceId){
        Map<String, Object> params = new HashMap<>();
        params.put("msg_id", "519");
        return faceCommonService.invokeInterface(sysDeviceId, params);
    }

    //相机end==================================================================

}
