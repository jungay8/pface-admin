package com.pface.admin.modules.jiekou.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.pface.admin.modules.jiekou.pojo.FaceAppUsescenePojo;
import com.pface.admin.modules.member.po.FaceAppUsescene;
import com.pface.admin.modules.member.service.FaceAppUsesceneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

@Slf4j
@Api(tags= "应用端发布的接口", description = "应用端发布的接口", value = "应用端发布的接口")
@RestController
@RequestMapping("/appapi/faceapp")
public class FaceAppInterface {
    @Autowired
    private FaceAppUsesceneService faceAppUsesceneService;

    @ApiOperation(value="鉴权登录客户端", position = 1, notes="鉴权登录客户端")
    @RequestMapping(value="/login",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operator", value = "账户", required = true, dataType = "String",paramType="form",defaultValue= "cs1001"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String",paramType="form",defaultValue= "123456")
    })
    @ResponseBody
    public Boolean  login(@RequestParam("operator")String operator, @RequestParam("operator")String password){

        return true;
    }

    @ApiOperation(value="获取使用场景", position = 2, notes="获取使用场景")
    @RequestMapping(value="/queryoneusescene",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "使用场景id", required = true, dataType = "String",paramType="form",defaultValue= "1")
    })
    @ResponseBody
    public String queryoneusescene(@RequestParam("id")Integer id){
        FaceAppUsescene faceAppUsescene = faceAppUsesceneService.queryById(id);
        FaceAppUsescenePojo desc = new FaceAppUsescenePojo();
        try {
            if (faceAppUsescene != null) {
                BeanUtils.copyProperties(desc, faceAppUsescene);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(desc);
    }
}
