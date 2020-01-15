package com.pface.admin.modules.jiekou.controller;

import com.alibaba.fastjson.JSON;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.jiekou.Constants.Faceconstant;
import com.pface.admin.modules.jiekou.service.FaceImageInterfaceService;
import com.pface.admin.modules.jiekou.service.FaceSenseboxInterfaceService;
import com.pface.admin.modules.jiekou.utils.BizUtils;
import com.pface.admin.modules.jiekou.utils.HttpClientHelper;
import com.pface.admin.modules.jiekou.utils.HttpException;
import com.pface.admin.modules.member.service.FaceCommonService;
import com.pface.admin.modules.member.service.FaceImageService;
import com.pface.admin.modules.member.service.FaceSenseboxService;
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
@Api(tags= "人像库接口", description = "人像库接口", value = "人像")
@RestController
@RequestMapping("/appapi/face/image")
public class FaceImageController {

    @Autowired
    private FaceCommonService faceCommonService;

    @ApiOperation(value="3.1 所有人像库查询", position = 1, notes="所有人像库查询")
    @RequestMapping(value="/imagealllib",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysDeviceId", value = "智能盒子系统id", required = true, dataType = "String",paramType="form",defaultValue= "1")
    })
    @ResponseBody
    public String  imagealllib(@RequestParam("sysDeviceId")String sysDeviceId){
        Map<String, Object> params = new HashMap<>();
        params.put("msg_id", "1028");
        return faceCommonService.invokeInterface(sysDeviceId, params);
    }

    @ApiOperation(value="3.10 人像库图片查询", position = 4, notes="人像库图片查询")
    @RequestMapping(value="/queryimage",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysDeviceId", value = "智能盒子系统id", required = true, dataType = "String",paramType="form",defaultValue= "1")
            ,@ApiImplicitParam(name = "lib_id", value = "人像库id", required = true, dataType = "Integer",paramType="form", defaultValue = "1")
            ,@ApiImplicitParam(name = "start_no", value = "起始位置", required = false, dataType = "Integer",paramType="form", defaultValue = "1")
            ,@ApiImplicitParam(name = "qry_len", value = "查询数量，最大50", required = false, dataType = "Integer",paramType="form", defaultValue = "10")
            ,@ApiImplicitParam(name = "fuzzy_key", value = "姓名、性别（0或1）、年龄、人员证号、地址字段的模糊查询", required = false, dataType = "String",paramType="form")

    })
    @ResponseBody
    public String  queryimage(@RequestParam("sysDeviceId")String sysDeviceId
            ,@RequestParam("lib_id")Integer lib_id
            ,@RequestParam("start_no")Integer start_no
            ,@RequestParam("qry_len")Integer qry_len
            ,@RequestParam("fuzzy_key")String fuzzy_key)
    {
            Map<String, Object> params = new HashMap<>();
            params.put("msg_id", "1035");
            params.put("lib_id",lib_id);
            if (start_no == null){
                start_no = 1;
            }
            if (qry_len == null){
                qry_len = 10;
            }
            if (qry_len > 50){
                qry_len = 50;
            }
            params.put("start_no", start_no);
            params.put("qry_len", qry_len);

            if (StringUtils.isNotEmpty(fuzzy_key)){
                params.put("fuzzy_key", fuzzy_key);
            }

            return faceCommonService.invokeInterface(sysDeviceId, params);
    }
}
