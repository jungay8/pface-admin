package com.pface.admin.modules.front.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.google.code.kaptcha.Constants;
import com.pface.admin.common.Global;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.*;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.service.ISmsService;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.front.constants.FaceAppConstant;
import com.pface.admin.modules.front.vo.FaceAppUserPojo;
import com.pface.admin.modules.front.vo.LoginVo;
import com.pface.admin.modules.front.vo.UpdateUserVo;
import com.pface.admin.modules.jiekou.utils.BizUtils;
import com.pface.admin.modules.member.enums.AuditStatusEnum;
import com.pface.admin.modules.member.enums.CertTypeEnum;
import com.pface.admin.modules.member.enums.CoverOriginEnum;
import com.pface.admin.modules.member.enums.MediaStatusEnum;
import com.pface.admin.modules.member.po.*;
import com.pface.admin.modules.member.service.FaceUserService;
import com.pface.admin.modules.member.service.JmgoFilePathService;
import com.pface.admin.modules.member.service.MemberCertService;
import com.pface.admin.modules.member.service.MemberUserService;
import com.pface.admin.modules.member.vo.MemberUserVo;
import com.pface.admin.modules.system.service.PasswordHelper;
import com.pface.admin.otherdb.po.UserInfo;
import com.pface.admin.otherdb.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author cjbi,daniel.liu
 */
@Slf4j
@Controller
@RequestMapping("/front/user")
public class FrontUserController extends BaseCrudController<MemberUser> {

    @Autowired
    private MemberUserService memberService;

    @Autowired
    private MemberCertService certService;

    @Autowired
    private PasswordHelper passwordHelper;

    @Autowired
    private ISmsService iSmsService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserInfoService userInfoService;

    @Value("${jmgo.synchronizeUser}")
    private Boolean synchronizeUser;

    @Autowired
    private JmgoFilePathService jmgoFilePathService;

    @Value("${face.serverip}")
    private String faceserverip;

    @Value("${face.serverport}")
    private String faceserverport;

    @ResponseBody
    @GetMapping("/list")
    @Override
    public Result<List<MemberUser>> queryList(MemberUser memberUser, PageQuery pageQuery) {
        
        Page<MemberUser> page = (Page<MemberUser>) memberService.queryList(memberUser, pageQuery);

        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @PostMapping("/regUser")
    //@SystemLog("会员注册")
    public Result create(@Validated @RequestBody MemberUserVo vo, HttpServletRequest request, HttpServletResponse response) {

        String smsCode= stringRedisTemplate.opsForValue().get(vo.getMobile());
        if(!vo.getSmsCode().equals(smsCode)){
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("短信证码错误");
        }

        List<MemberUser> u=memberService.queryList(new MemberUser().setUname(vo.getUname()));
        if(u!=null && u.size()>0){
            return   Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("用户已经被注册");
        }

        MemberUser mobile_cond = new MemberUser();
        mobile_cond.setMobile(vo.getMobile());
        List<MemberUser> u2 = memberService.queryList(mobile_cond);
        if(u2!=null && u2.size()>0){
            return   Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("手机号已经被注册");
        }

        vo.setPwd(DigestUtils.md5Hex(vo.getPwd()));
        MemberUser user=new MemberUser();
        BeanUtils.copyProperties(vo,user);
        user.setIsCert(CertTypeEnum.UNCERTIFIED) ;
        user.setOpDate(DateUtils.getNowDate());
        if (synchronizeUser){
            try {
                UserInfo userInfo_cond = new UserInfo();
                userInfo_cond.setUserLogin(vo.getUname());
                UserInfo remoteUuserInfos = userInfoService.queryOne(userInfo_cond);
                if (remoteUuserInfos != null) {
                    return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("用户已经被注册");
                }

                //to sqlserver
                UserInfo userInfo = new UserInfo();
                userInfo.setUserName(user.getUname());
                userInfo.setUserLogin(user.getUname());
                userInfo.setUserPassword(user.getPwd());
                userInfo.setUserPhone(user.getMobile());
                userInfo.setUserMail(user.getEmail());
                userInfo.setUserType(1);
                userInfo.setUserStatus(true);
                userInfoService.save(userInfo); //to sqlserver
                user.setUserId(userInfo.getUserId());

                memberService.create(user); //to mysql
            }catch (Exception ex){
                ex.printStackTrace();
                log.info("同步账户数据到sqlserver异常，数据存储在mysql开始");
                memberService.create(user); //to mysql
                log.info("同步账户数据到sqlserver异常，数据存储在mysql结束");
                return Result.success().setData(user.getUname()).setMsg("恭喜您"+user.getUname()+"注册成功，请登录").addExtra("url","login");
            }
        }else{
            memberService.create(user); //to mysql
        }
        return Result.success().setData(user.getUname()).setMsg("恭喜您"+user.getUname()+"注册成功，请登录").addExtra("url","login");
    }

    @ResponseBody
    @PostMapping("/sendSmsCode")
//    @SystemLog("短信验证码")
    public Result sendSmsCode(@RequestBody Map<String,Object> params) {
       String mobile=(String) params.get("mobile");

        MemberUser mobile_cond = new MemberUser();
        mobile_cond.setMobile(mobile);
        List<MemberUser> u2 = memberService.queryList(mobile_cond);
        if(u2!=null && u2.size()>0){
            return   Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("手机号已经被注册");
        }
        String code=StringUtils.getFourRandom();
        log.info("短信验证码：{}",code);
        //发短信
        iSmsService.sendMsg(mobile,String.format("您的短信验证码:%s,请在1分钟内输入，若非本人操作，请忽略本短信。", code));
        //JedisUtils.set(mobile,code,60);
        stringRedisTemplate.opsForValue().set(mobile,code,60,TimeUnit.SECONDS);

        return Result.success().setData(code).setMsg("短信验证码发送成功");
    }


    @ResponseBody
    @PostMapping("/update")
    @SystemLog("会员管理更")
    @Override
    public Result update(@Validated MemberUser memberUser) {
        memberService.updateNotNull(memberUser);
        return Result.success();
    }



    @ResponseBody
    @PostMapping("/cert/view/{uid}")
    public Result viewCertByuid(@NotNull @PathVariable("uid") Long uid){
        return Result.success().setData(certService.queryOne(new MemberCert().setUid(uid)));
    }


    @ResponseBody
    @PostMapping("/loginJson")
    public Result loginJson(@RequestBody LoginVo loginVo,HttpServletRequest request, HttpServletResponse response){

       if(!doCaptchaValidate(loginVo.getCaptcha())){
           //return   Result.failure(ResultCodeEnum.KAPTCHA_VALIDATE_FAILED);
           return   Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("验证码错误");
       }

       MemberUser u=memberService.queryOne(new MemberUser().setUname(loginVo.getName()));
       if(u==null){
           return   Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("账号错误");
       }

       String pwd= DigestUtils.md5Hex(loginVo.getPassword());
       if(!pwd.equals(u.getPwd())){
           return   Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("密码错误");
       }

        HttpSession session = request.getSession(true);
        session.setAttribute(MemberUser.USER_SESSION_KEY, u);

        String jumpUrl="front";
        //还得判断旧系统用户
        if(!u.getIsCert().getName().equals(CertTypeEnum.CERTIFIED.getName())){
           //非认证用户，都统一导航到认证界面
            jumpUrl="certification";
        }


        return Result.success().setData(u.getUname()).addExtra("url",jumpUrl);
    }

//    @Autowired
//    FaceUserService faceUserService ;

    //执行登录
    @PostMapping("/login")
    public String login(LoginVo loginVo, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {

        ModelAndView modelAndView = new ModelAndView();
        if (!doCaptchaValidate(loginVo.getCaptcha())) {
            redirectAttributes.addFlashAttribute("errormsg", "验证码错误");
            return "redirect:/front/login";
        }

//        FaceUser faceUser_cond = new FaceUser();
//        faceUser_cond.setOperator(loginVo.getName());
//        FaceUser faceUser = faceUserService.queryOne(faceUser_cond);
//        MemberUser u = memberService.queryOne(new MemberUser().setUname(loginVo.getName()));
//        BizUtils bizUtils = new BizUtils();
        String url = "http://"+faceserverip +":"+faceserverport+ "/appapi/faceserverapi/userinfo";
        Map<String, String> param = new HashMap<>();
        param.put("operator", loginVo.getName()+"");
        param.put("id", "");
        String jsonstr = OKHttpUtil.httpPost(url, param);
        FaceAppUserPojo pojo = new FaceAppUserPojo();
        if (StringUtils.isNotEmpty(jsonstr)){
            jsonstr = jsonstr.replaceAll("\\\\", "");
            jsonstr = jsonstr.substring(1,jsonstr.length() -1);
            JSONObject jsonObject = JSON.parseObject(jsonstr);
            Boolean sucess = jsonObject.getBoolean("success");
            if (sucess){
                JSONObject object = jsonObject.getJSONObject("data");
                pojo = object.toJavaObject(FaceAppUserPojo.class);

                String pwd= DigestUtils.md5Hex(loginVo.getPassword());
                if(!pwd.equalsIgnoreCase(pojo.getPassword())){
                    redirectAttributes.addFlashAttribute("errormsg","密码错误");
                    return   "redirect:/front/login";
                }

                HttpSession session = request.getSession(true);
                session.setAttribute(FaceAppConstant.USER_SESSION_KEY, pojo);
            }else{
                redirectAttributes.addFlashAttribute("errormsg","账号错误");
                return   "redirect:/front/login";
            }
        }else{
            redirectAttributes.addFlashAttribute("errormsg","网络错误，请稍后再试");
            return   "redirect:/front/login";
        }


        String jumpUrl="redirect:/front/faceimagescene";
        redirectAttributes.addFlashAttribute("navclass","upload");
        //还得判断旧系统用户
//        if(!u.getIsCert().getName().equalsIgnoreCase(CertTypeEnum.CERTIFIED.getName())){
            //非认证用户，都统一导航到认证界面
//            jumpUrl="redirect:/front/certification";
//            redirectAttributes.addFlashAttribute("navclass","certification");
//        }
        int timeout=request.getSession().getMaxInactiveInterval();
        log.info("会话超时时间设置：{}秒,{}小时",timeout,request.getSession().getMaxInactiveInterval()/(60*60));

        return jumpUrl;
    }



    private boolean doCaptchaValidate(String captchaCode)  {
        // 从session获取正确的验证码
        Session session = SecurityUtils.getSubject().getSession();

        String validateCode = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);

        // 若验证码为空或匹配失败则返回false
        if(captchaCode==null || validateCode==null){
            //如果验证码失败了，存储失败key属性
            return false;
        }
        captchaCode = captchaCode.toLowerCase();
        validateCode = validateCode.toLowerCase();
        if(!captchaCode.equals(validateCode)) {
            //如果验证码失败了，存储失败key属性
            return false;
        }
        return true;
    }


    @PostMapping(value = { "/updateInfo" })
    @ResponseBody
    @Deprecated
    public Result updateInfo(@RequestParam(required=false)MultipartFile file,UpdateUserVo userVo,HttpServletRequest request){

        MemberUser  user= memberService.queryOne(new MemberUser().setId(userVo.getId()).setUname(userVo.getUname()));

        if(user==null){
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("用户信息错误");
        }
       if(org.apache.commons.lang3.StringUtils.isNotBlank(userVo.getNewPwd())){
           if(!DigestUtils.md5Hex(userVo.getPwd()).equals(user.getPwd())){
               return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("旧密码错误");
           }
           user.setPwd(DigestUtils.md5Hex(userVo.getNewPwd()));
       }

        if(file!=null){
                String filepath= "";
                if(user!=null){
                    filepath=String.valueOf(user.getId())+File.separator + Global.USERFILES_BASE_URL;
                }
                JmgoFilePath jmgoFilePath = jmgoFilePathService.getCurrentFP();
                if(jmgoFilePath==null){
                    log.debug("上传路径没有配置！请先配置好表：jmgo_file_path");
                    return Result.failure(ResultCodeEnum.PARAM_ERROR);
                }else {
                    try {
                        File filePath = FileUtil.saveFile(file, jmgoFilePath.getVirtualpath(), filepath, FileUtil.getFileExtension(file.getOriginalFilename()));
                        // String fileName=  filePath.getPath().substring(filePath.getPath().indexOf(filepath+1)).replaceAll("\\\\","");
                        user.setHeadUrl(filePath.getPath());//filePath.getName();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg(e.getMessage());
                    }
                }
        }
        user.setMobile(userVo.getMobile());
        memberService.updateNotNull(user);

        HttpSession session = request.getSession(true);
        session.setAttribute(MemberUser.USER_SESSION_KEY, user);
        return Result.success().addExtra("headUrl", user.getHeadUrl());
    }


    /**
     * 修改用户信息--带头像修改
     *
     * @param headUrl
     * @param isChangePassword
     * @param nPwd
     * @param oldHeadUrl
     * @param uid
     * @param redirectAttributes
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @PostMapping("/updateInfoHasHeadUrl")
    public Result updateInfoHasHeadUrl(
            @RequestParam(name = "headUrl") MultipartFile headUrl,
            @RequestParam(name = "isChangePassword") String isChangePassword,
            @RequestParam(name = "nPwd") String nPwd,
            @RequestParam(name = "uid") Long uid,
            @RequestParam(name = "oldPwd") String oldPwd,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request, HttpServletResponse response) {

            HttpSession session = request.getSession(true);
            MemberUser user = (MemberUser)session.getAttribute(MemberUser.USER_SESSION_KEY);

            MemberUser  olduser = memberService.queryById(uid);

            MemberUser saveUser = new MemberUser();
            saveUser.setId(uid);

            //改密
            if (StringUtils.isNotEmpty(isChangePassword) && isChangePassword.equals("1")){

                String oldPwdEncode = DigestUtils.md5Hex(oldPwd);
                if(!oldPwdEncode.equals(user.getPwd())){
                    return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("旧密码错误");
                }

                String nPwdEncode = DigestUtils.md5Hex(nPwd);
                saveUser.setPwd(nPwdEncode);
                user.setPwd(nPwdEncode);
            }

            //上传头像
            JmgoFilePath jmgoFilePath = jmgoFilePathService.getCurrentFP();
            if (jmgoFilePath == null) {
                log.info("没有配置有效的上传路径");
                return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg("没有配置有效封面的上传路径");
            }
            String filepath= "";
            if(user!=null){
                filepath=String.valueOf(user.getId())+File.separator + Global.USERFILES_BASE_URL;
            }
            try {
                File filePath = FileUtil.saveFile(headUrl, jmgoFilePath.getVirtualpath(), filepath, FileUtil.getFileExtension(headUrl.getOriginalFilename()));
                saveUser.setHeadUrl(filePath.getAbsolutePath());
                user.setHeadUrl(filePath.getAbsolutePath());
            } catch (Exception e) {
                log.info("头像文件上传异常。");
                e.printStackTrace();
                return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("头像文件上传异常。");
            }
            try {
                memberService.updateNotNull(saveUser);
                FileUtil.deleteFile(olduser.getHeadUrl());
            }catch (Exception ee){
                log.info("保存数据异常。");
                ee.printStackTrace();
                return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("保存数据异常");
            }

//            HttpSession session = request.getSession(true);
            session.setAttribute(MemberUser.USER_SESSION_KEY, user);

            return Result.success().setMsg("操作成功");
    }


    @ResponseBody
    @PostMapping("/updateInfoNoHeadUrl")
    public Result updateInfoNoHeadUrl(
            @RequestParam(name = "isChangePassword") String isChangePassword,
            @RequestParam(name = "nPwd") String nPwd,
            @RequestParam(name = "uid") Long uid,
            @RequestParam(name = "oldPwd") String oldPwd,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request, HttpServletResponse response) {
//            HttpSession session = request.getSession(true);
//            MemberUser user = (MemberUser)session.getAttribute(MemberUser.USER_SESSION_KEY);

        MemberUser  user = memberService.queryById(uid);
        MemberUser saveUser = new MemberUser();
        saveUser.setId(uid);
        //改密
        if (StringUtils.isNotEmpty(isChangePassword) && isChangePassword.equals("1")){

            String oldPwdEncode = DigestUtils.md5Hex(oldPwd);
            if(!oldPwdEncode.equals(user.getPwd())){
                return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("旧密码错误");
            }

            String nPwdEncode = DigestUtils.md5Hex(nPwd);
            saveUser.setPwd(nPwdEncode);
            user.setPwd(nPwdEncode);
        }

        try {
            memberService.updateNotNull(saveUser);
        }catch (Exception ee){
            log.info("保存数据异常。");
            ee.printStackTrace();
            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("保存数据异常");
        }

        HttpSession session = request.getSession(true);
        session.setAttribute(MemberUser.USER_SESSION_KEY, user);
        return Result.success().setMsg("操作成功.");
    }
}
