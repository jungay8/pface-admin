package com.pface.admin.modules.jiekou.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pface.admin.core.utils.FileUtil;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.jiekou.Constants.Faceconstant;
import com.pface.admin.modules.jiekou.service.FaceSenseboxInterfaceService;
import com.pface.admin.modules.jiekou.utils.BizUtils;
import com.pface.admin.modules.jiekou.utils.HttpClientHelper;
import com.pface.admin.modules.jiekou.utils.HttpException;
import com.pface.admin.modules.member.po.FaceSensebox;
import com.pface.admin.modules.member.po.FaceSnapList;
import com.pface.admin.modules.member.service.FaceCommonService;
import com.pface.admin.modules.member.service.FaceSenseboxService;
import com.pface.admin.modules.member.service.FaceSnapListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags= "人脸无感签到接口", description = "无感签到接口", value = "人脸")
@RestController
@RequestMapping("/appapi/face")
public class FaceController {

    @Value("publickey")
    private String publickey;

    @Autowired
    private FaceCommonService faceCommonService;

    @Autowired
    private FaceSnapListService faceSnapListService;

    @Autowired
    private FaceSenseboxService faceSenseboxService;

    @ApiOperation(value = "7.3 7.4 监听接口", position = 1, notes = "接收摄像机的监听结果")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/monitor", method = RequestMethod.POST)
    @ResponseBody
    public void monitor(HttpServletRequest request){
        log.info("in face monitor()");
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        String jsonStr = params.getParameter("json");
        log.info("jsonStr = "+jsonStr);//json字符串请自行解析
        JSONObject json = JSONObject.parseObject(jsonStr);
        int msg_id = json.getIntValue("msg_id");
        if(msg_id == 775){
            log.info("这是心跳消息");
        }
        if(msg_id == 774){
            log.info("这是抓拍消息");
            JSONObject data = json.getJSONObject("data");
            FaceSnapList faceSnapList = data.toJavaObject(FaceSnapList.class);

            //修正json方法不能映射的字段值
            faceSnapList.setTriggerTime(data.getTimestamp("trigger"));
            //通用字段赋值
            faceSnapList = faceCommonService.setCommonField(faceSnapList, "create");
            //形成可在浏览器访问的web路径
            FaceSensebox faceSensebox_cond = new FaceSensebox();
            faceSensebox_cond.setDeviceId(faceSnapList.getDeviceId());
            FaceSensebox faceSensebox = faceSenseboxService.queryOne(faceSensebox_cond);
            if (faceSensebox!=null){
                String url = BizUtils.getAccessImageUrl(faceSensebox.getIp(), faceSensebox.getPort());
                if (StringUtils.isNotEmpty(url)){
                    faceSnapList.setImgPath(url + faceSnapList.getImgPath());
                    faceSnapList.setSnapPath(url + faceSnapList.getSnapPath());
                }
            }

            faceSnapListService.create(faceSnapList);

            MultipartFile snap = params.getFile("snap"); //抓拍图---同snap_path属性
            MultipartFile snap_frame = params.getFile("snap_frame"); //抓拍图
            MultipartFile img = params.getFile("img"); // 底图可能为空---同img_path属性

            if (!snap_frame.isEmpty()) {//其他图片保存同理
                try {
                    File snap_frame_file = new File(snap_frame.getOriginalFilename());
//                    FileUtil.saveFile(snap_frame, )
                    BufferedOutputStream out_snap_frame = new BufferedOutputStream(new FileOutputStream(snap_frame_file));
                    log.info(snap_frame.getName());
                    out_snap_frame.write(snap_frame.getBytes());
                    out_snap_frame.flush();
                    out_snap_frame.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "应用端拿人脸数据接口", position = 2, notes = "应用端拿人脸数据接口")
    @RequestMapping(value = "/pulldata", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "callbacurl", value = "回调URL", required = true, dataType = "String",paramType="form"),
            @ApiImplicitParam(name = "operator", value = "应用端账户", required = true, dataType = "String",paramType="form"),
            @ApiImplicitParam(name = "password", value = "应用端登陆密码", required = true, dataType = "String",paramType="form"),
            @ApiImplicitParam(name = "requestid", value = "请求唯一id", required = true, dataType = "String",paramType="form")  //防重放攻击
    })

    @ResponseBody
    public void pulldata(@RequestParam("url")String callbacurl, @RequestParam("operator")String operator, @RequestParam("password")String password){

        //D1 鉴权
        //D2 读取数据
        //D3 加密数据（公钥加密数据）
        //D4 数据加密在网络上传输
        //D5 带上token（operator+password+公钥生成）和 数据ids的base64加密传输(authids)
    }


    @ApiOperation(value = "应用端回调接口", position = 2, notes = "应用端回调接口")
    @RequestMapping(value = "/pulldatacallback", method = RequestMethod.POST)
    @ApiImplicitParams({})
    @ResponseBody
    public void pulldatacallback(HttpServletRequest request){
        //String token = request.getParameter("token");
        //用公钥解开（遍历用户表），没通过的，则丢弃
        //通过，则:String uuid = request.getParameter("authids");
        //还原ids数据，删除之
    }

    @ApiOperation(value="获取登录sessionid", position = 2, notes="获取登录sessionid")
    @RequestMapping(value="/login",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysDeviceId", value = "智能盒子系统id", required = true, dataType = "String",paramType="form",defaultValue= "1")
    })
    @ResponseBody
    public String  login(@RequestParam("sysDeviceId")String sysDeviceId){
        return faceCommonService.getSessionid(Long.valueOf(sysDeviceId));
    }

    @ApiOperation(value="登出", position = 4, notes="登出")
    @RequestMapping(value="/logout",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysDeviceId", value = "智能盒子系统id", required = true, dataType = "String",paramType="form",defaultValue= "1")
    })
    @ResponseBody
    public String  logout(@RequestParam("sysDeviceId")String sysDeviceId){
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("msg_id", "258");
            String url = faceCommonService.getAccessSenseboxUrl(Long.valueOf(sysDeviceId));
            String response = HttpClientHelper.postJson(url, JSON.toJSONString(params),"");
            return BizUtils.convertResponse(response);
        } catch (HttpException e) {
            return e.toJsonStr();
        }
    }
//
//    @ApiOperation(value="formApi-3.7 人脸入库", position = 5,notes="formApi-3.7 人脸入库")
//    @RequestMapping(value="/form",method = RequestMethod.POST)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "sysDeviceId", value = "智能盒子系统id", required = true, dataType = "String",paramType="form",defaultValue= "1")
//            ,@ApiImplicitParam(name = "lib_id", value = "人像库id", required = true, dataType = "Integer",paramType="form", defaultValue = "1")
//    })
//    @ResponseBody
//    public String  form(@RequestParam(value = "sysDeviceId", required = true)String sysDeviceId,
//                        @RequestParam(value = "lib_id", required = true)Integer lib_id,
//                        @RequestParam(value = "file", required = true) MultipartFile file){
//        File tmpFile = null;
//        try {
//            tmpFile = File.createTempFile("tmp", null);
//            file.transferTo(tmpFile);
//            Map<String, File> fileMap = new HashMap<>();
//            fileMap.put("img",tmpFile);
//
////            Map<String,String> params = JSON.parseObject(jsonStr,Map.class);
//            Map<String,String> params = new HashMap<>();
//            params.put("msg_id", "1029");
//            params.put("lib_id", lib_id+"");
//            String url = faceCommonService.getAccessSenseboxUrl(Long.valueOf(sysDeviceId));
//            String sessionId = faceCommonService.getSessionid(Long.valueOf(sysDeviceId));
//            String response = HttpClientHelper.postForm(url,fileMap,params,sessionId);
//            tmpFile.deleteOnExit();
//            return BizUtils.convertResponse(response);
//        } catch (HttpException e) {
//            return e.toJsonStr();
//        }
//        catch (IOException ex){
//            return ex.getMessage();
//        }
//    }


    @ApiOperation(value="7.1 http配置信息", position = 6, notes="http配置信息")
    @RequestMapping(value="/httpconfiginfo",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysDeviceId", value = "智能盒子系统id", required = true, dataType = "String",paramType="form",defaultValue= "1")
    })
    @ResponseBody
    public String  httpconfiginfo(@RequestParam("sysDeviceId")String sysDeviceId){
        Map<String, Object> params = new HashMap<>();
        params.put("msg_id", "1299");
        return faceCommonService.invokeInterface(sysDeviceId, params);
    }



//    @ApiOperation(value="websock布控结果密钥", position = 7, notes="查询websock布控结果密钥")
//    @RequestMapping(value="/websockconfiginfo",method = RequestMethod.POST)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "url", value = "Nebula-M的json API URL", required = true, dataType = "String",paramType="form",defaultValue="http://192.168.2.10/api/json")
//            ,@ApiImplicitParam(name = "sessionId", value = "sessionId用户令牌，通过登录接口获取", required = true, dataType = "String",paramType="header"),
//    })
//    @ResponseBody
//    public String  websockconfiginfo(@RequestParam("url")String url
//            ,@RequestHeader("sessionId")String sessionId){
//        try {
//            Map<String, Object> params = new HashMap<>();
//            params.put("msg_id", "1286");
//            String response = HttpClientHelper.postJson(url, JSON.toJSONString(params),sessionId);
//            return convertResponse(response);
//        } catch (HttpException e) {
//            return e.toJsonStr();
//        }
//    }


//    @ApiOperation(value="websock布控结果推送", position = 8, notes="websock布控结果推送")
//    @RequestMapping(value="/websockpush",method = RequestMethod.POST)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "url", value = "Nebula-M的json API URL", required = true, dataType = "String",paramType="form",defaultValue=Faceconstant.websock_uri)
//            ,@ApiImplicitParam(name = "sessionId", value = "sessionId用户令牌，通过登录接口获取", required = true, dataType = "String",paramType="header"),
//            @ApiImplicitParam(name = "key", value = "订阅密钥", required = true, dataType = "String",paramType="form")
//
//    })
//    @ResponseBody
//    public String  websockpush(@RequestParam("url")String url
//            ,@RequestHeader("sessionId")String sessionId
//            ,@RequestParam("key")String key){
//        try {
//            Map<String, Object> params = new HashMap<>();
//            params.put("msg_id", "776");
//            params.put("key",key);
//            String response = HttpClientHelper.postJson(url, JSON.toJSONString(params),sessionId);
//            return convertResponse(response);
//        } catch (HttpException e) {
//            return e.toJsonStr();
//        }
//    }

//    private String convertResponse(String response) throws HttpException{
//        JSONObject obj = JSON.parseObject(response);
//        if (obj.getIntValue("code") == 0) {
//            log.info(response);
//            return response;
//        } else {
//            throw new HttpException(200, obj.getIntValue("code"), obj.getString("msg"));
//        }
//    }
}
