package com.pface.admin.modules.front.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.utils.IdUtils;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.front.constants.FaceAppConstant;
import com.pface.admin.modules.front.vo.*;
import com.pface.admin.modules.member.enums.GoodsStatusEnum;
import com.pface.admin.modules.member.enums.MediaStatusEnum;
import com.pface.admin.modules.member.enums.MediaTypeEnum;
import com.pface.admin.modules.member.po.*;
import com.pface.admin.modules.member.service.FaceAppImageLibsService;
import com.pface.admin.modules.member.service.FaceAppImagesService;
import com.pface.admin.modules.member.service.FaceAppSnapListService;
import com.pface.admin.modules.member.service.FaceAppUsesceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping
public class FaceAppIndexController {
    @Autowired
    FaceAppSnapListService faceAppSnapListService;

    @Autowired
    FaceAppUsesceneService faceAppUsesceneService;
    @ModelAttribute("userinfo")
    public FaceAppUserPojo getUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        FaceAppUserPojo user = (FaceAppUserPojo)session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        return user;
    }
    
    @Autowired
    FaceAppImageLibsService faceAppImageLibsService;
    @Autowired
    FaceAppImagesService faceAppImagesService;
    //到登录页面
    @GetMapping(value={"","login","/front/login"})
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        session.removeAttribute(FaceAppConstant.USER_SESSION_KEY);
        model.addAttribute("userinfo",null);
        return "front/login";
    }

    //执行退出，到登录页面
    @GetMapping(value={"/front/logout"})
    public String logout(Model model,HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        session.removeAttribute(FaceAppConstant.USER_SESSION_KEY);
        model.addAttribute("userinfo",null);
        return "front/login";
    }

    //登录成功后：场景管理（会议管理）页面
    @RequestMapping(value={"/front/faceimagescene"},method = {RequestMethod.POST,RequestMethod.GET})
    public String index(FaceAppUserPojo user, PageQuery pageQuery, Model model,
                        HttpServletRequest request, HttpServletResponse response) {
        String faceusescenename = request.getParameter("faceusescenename");
        model.addAttribute("faceusescenename", faceusescenename);

        HttpSession session = request.getSession(true);
        FaceAppUserPojo userinfo = (FaceAppUserPojo)session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        if (pageQuery.getPageSizeKey() <= 0) {
            pageQuery.setPageSizeKey(10);
        }
        if (pageQuery.getPageNumKey() <= 0) {
            pageQuery.setPageNumKey(1);
        }

        Example example = new Example(FaceAppUsescene.class);

        if (userinfo!=null){
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("sysUserid", userinfo.getId());
            if (StringUtils.isNotEmpty(faceusescenename)) {
                criteria.andLike("scenename", "%" + faceusescenename.trim() + "%");  //注意：%后不要有单引号
            }
            criteria.andEqualTo("delFlag","1");
            example.orderBy("updateDate").desc();

            Page<FaceAppUsescene> mediaFilePage = (Page<FaceAppUsescene>) faceAppUsesceneService.queryList(pageQuery, example);
            model.addAttribute("sceneList", mediaFilePage.getResult());
            model.addAttribute("total", mediaFilePage.getTotal());
        }

        model.addAttribute("pageNum", pageQuery.getPageNum());
        model.addAttribute("pageSize", pageQuery.getPageSize());

        model.addAttribute("navclass","faceimagescene");
        return "front/faceimagescene";
    }

    //人像库管理
    @RequestMapping(value={"/front/faceimagelibs"},method = {RequestMethod.POST,RequestMethod.GET})
    public String faceimage(PageQuery pageQuery, Model model,
                        HttpServletRequest request, HttpServletResponse response) {
        String libname = request.getParameter("libname");
        model.addAttribute("libname", libname);
        String libtype = request.getParameter("libtype");
        model.addAttribute("libtype", libtype);
        String syssceneid = request.getParameter("syssceneid");
        model.addAttribute("syssceneid", syssceneid);

        HttpSession session = request.getSession(true);
        FaceAppUserPojo userinfo = (FaceAppUserPojo)session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        if (pageQuery.getPageSizeKey() <= 0) {
            pageQuery.setPageSizeKey(10);
        }
        if (pageQuery.getPageNumKey() <= 0) {
            pageQuery.setPageNumKey(1);
        }


        if (userinfo!=null){
            Example example = new Example(FaceAppImageLibs.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("sysUserid", userinfo.getId());
            if (StringUtils.isNotEmpty(libname)) {
                criteria.andLike("libName", "%" + libname.trim() + "%");  //注意：%后不要有单引号
            }
            if (StringUtils.isNotEmpty(libtype)) {
                criteria.andEqualTo("libType", libtype);
            }
            if (StringUtils.isNotEmpty(syssceneid)) {
                criteria.andEqualTo("sysSceneid", syssceneid);
            }
            criteria.andEqualTo("delFlag","1");
            example.orderBy("updateDate").desc();

            Page<FaceAppImageLibs> mediaFilePage = (Page<FaceAppImageLibs>) faceAppImageLibsService.queryList(pageQuery, example);
            model.addAttribute("faceAppImageLibsList", mediaFilePage.getResult());
            model.addAttribute("total", mediaFilePage.getTotal());
        }

        model.addAttribute("pageNum", pageQuery.getPageNum());
        model.addAttribute("pageSize", pageQuery.getPageSize());

        FaceAppUsescene faceAppUsescene = new FaceAppUsescene();
        faceAppUsescene.setSysUserid(userinfo.getId());
        faceAppUsescene.setDelFlag("1");
        List<FaceAppUsescene> faceAppUsesceneAllList = faceAppUsesceneService.queryList(faceAppUsescene);
        model.addAttribute("faceAppUsesceneAllList", faceAppUsesceneAllList);

        model.addAttribute("navclass","faceimage");
        return "front/faceimagelibs";
    }


    //图片库管理
    @RequestMapping(value={"/front/faceappimages"},method = {RequestMethod.POST,RequestMethod.GET})
    public String faceappimages(PageQuery pageQuery, Model model,
                            HttpServletRequest request, HttpServletResponse response) {
        String personName = request.getParameter("personName");
        model.addAttribute("personName", personName);

        String libId = request.getParameter("libId");
        model.addAttribute("libId", libId);

        String sysLibId = request.getParameter("sysLibId");
        model.addAttribute("sysLibId", sysLibId);

        String deviceId = request.getParameter("deviceId");
        model.addAttribute("deviceId", deviceId);

        HttpSession session = request.getSession(true);
        FaceAppUserPojo userinfo = (FaceAppUserPojo)session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        if (pageQuery.getPageSizeKey() <= 0) {
            pageQuery.setPageSizeKey(10);
        }
        if (pageQuery.getPageNumKey() <= 0) {
            pageQuery.setPageNumKey(1);
        }

        if (userinfo!=null){
            Example example = new Example(FaceAppImages.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("sysUserid", userinfo.getId());

            if (StringUtils.isNotEmpty(libId)) {
                criteria.andEqualTo("libId", libId);
            }
            if (StringUtils.isNotEmpty(deviceId)) {
                criteria.andEqualTo("deviceId", deviceId);
            }
            if (StringUtils.isNotEmpty(personName)) {
                criteria.andLike("personName", "%"+personName + "%");
            }
            criteria.andEqualTo("delFlag","1");
            example.orderBy("updateDate").desc();

            Page<FaceAppImages> mediaFilePage = (Page<FaceAppImages>) faceAppImagesService.queryList(pageQuery, example);
            model.addAttribute("faceAppImagesList", mediaFilePage.getResult());
            model.addAttribute("total", mediaFilePage.getTotal());
        }
        if (StringUtils.isNotEmpty(sysLibId)) {
            FaceAppImageLibs faceAppImageLibs = faceAppImageLibsService.queryById(sysLibId);
            if (faceAppImageLibs != null) {
                model.addAttribute("libname", faceAppImageLibs.getLibName());
                FaceAppUsescene faceAppUsescene = faceAppUsesceneService.queryById(faceAppImageLibs.getSysSceneid());
                if (faceAppUsescene != null) {
                    model.addAttribute("usesecenenname", faceAppUsescene.getScenename());
                } else {
                    model.addAttribute("usesecenenname", "");
                }
            } else {
                model.addAttribute("libname", "");
            }
        }else{
            model.addAttribute("libname", "");
            model.addAttribute("usesecenenname", "");
        }

        model.addAttribute("pageNum", pageQuery.getPageNum());
        model.addAttribute("pageSize", pageQuery.getPageSize());
        model.addAttribute("navclass","faceimage");
        return "front/faceimages";
    }


//    抓拍记录
    @RequestMapping(value={"/front/queryAppSnapList"},method = {RequestMethod.POST,RequestMethod.GET})
    public String queryAppSnapList(PageQuery pageQuery, Model model, HttpServletRequest request){
        String personName = request.getParameter("personName");
        String syssceneid = request.getParameter("syssceneid");
        model.addAttribute("personName", personName);
        model.addAttribute("syssceneid", syssceneid);

        HttpSession session = request.getSession(true);
        FaceAppUserPojo userinfo = (FaceAppUserPojo)session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        if (pageQuery.getPageSizeKey() <= 0) {
            pageQuery.setPageSizeKey(10);
        }
        if (pageQuery.getPageNumKey() <= 0) {
            pageQuery.setPageNumKey(1);
        }
        FaceAppSnapListQueryParams params = new FaceAppSnapListQueryParams();
        if (StringUtils.isNotEmpty(personName)){
            params.setPersonFlexParam(personName);
        }

        if (StringUtils.isNotEmpty(syssceneid)){
            params.setSysSceneid(Integer.valueOf(syssceneid));
        }

        params.setSysUserid(userinfo.getId());
        Page<FaceAppSnapListPojo> appSnapListPojoPage =  (Page<FaceAppSnapListPojo>) faceAppSnapListService.queryAppSnapList(pageQuery,params);

        model.addAttribute("appSnapListPojoPage", appSnapListPojoPage.getResult());
        model.addAttribute("total", appSnapListPojoPage.getTotal());
        model.addAttribute("pageNum", appSnapListPojoPage.getPageNum());
        model.addAttribute("pageSize", appSnapListPojoPage.getPageSize());

        FaceAppUsescene faceAppUsescene = new FaceAppUsescene();
        faceAppUsescene.setSysUserid(userinfo.getId());
        faceAppUsescene.setDelFlag("1");
        List<FaceAppUsescene> faceAppUsesceneAllList = faceAppUsesceneService.queryList(faceAppUsescene);
        model.addAttribute("faceAppUsesceneAllList", faceAppUsesceneAllList);

        model.addAttribute("navclass","faceasignlist");
        return "front/faceasignlist";
    }

    //签到汇总
    @RequestMapping(value={"/front/assembleAppSnapList"},method = {RequestMethod.POST,RequestMethod.GET})
    public String assembleAppSnapList(PageQuery pageQuery, Model model, HttpServletRequest request){
        String syssceneid = request.getParameter("syssceneid");
        model.addAttribute("syssceneid", syssceneid);

        HttpSession session = request.getSession(true);
        FaceAppUserPojo userinfo = (FaceAppUserPojo)session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        if (pageQuery.getPageSizeKey() <= 0) {
            pageQuery.setPageSizeKey(10);
        }
        if (pageQuery.getPageNumKey() <= 0) {
            pageQuery.setPageNumKey(1);
        }
        FaceAppSnapListQueryParams params = new FaceAppSnapListQueryParams();

        if (StringUtils.isNotEmpty(syssceneid)){
            params.setSysSceneid(Integer.valueOf(syssceneid));
        }

        params.setSysUserid(userinfo.getId());
        Page<FaceAppSnapListAssemblePojo> appSnapListPojoAssemblePage =  (Page<FaceAppSnapListAssemblePojo>) faceAppSnapListService.assembleAppSnapList(pageQuery,params);

        model.addAttribute("appSnapListPojoAssemblePage", appSnapListPojoAssemblePage.getResult());
        model.addAttribute("total", appSnapListPojoAssemblePage.getTotal());
        model.addAttribute("pageNum", appSnapListPojoAssemblePage.getPageNum());
        model.addAttribute("pageSize", appSnapListPojoAssemblePage.getPageSize());

        FaceAppUsescene faceAppUsescene = new FaceAppUsescene();
        faceAppUsescene.setSysUserid(userinfo.getId());
        faceAppUsescene.setDelFlag("1");
        List<FaceAppUsescene> faceAppUsesceneAllList = faceAppUsesceneService.queryList(faceAppUsescene);
        model.addAttribute("faceAppUsesceneAllList", faceAppUsesceneAllList);

        model.addAttribute("navclass","faceasignlistasemble");
        return "front/faceasignlistassemble";
    }

    //签到大屏
    @RequestMapping(value={"/front/asignshow"},method = {RequestMethod.POST,RequestMethod.GET})
    public String asignshow(PageQuery pageQuery, Model model, HttpServletRequest request){
//        String syssceneid = request.getParameter("syssceneid");
//        model.addAttribute("syssceneid", syssceneid);

        HttpSession session = request.getSession(true);
        FaceAppUserPojo userinfo = (FaceAppUserPojo)session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        if (pageQuery.getPageSizeKey() <= 0) {
            pageQuery.setPageSizeKey(10);
        }
        if (pageQuery.getPageNumKey() <= 0) {
            pageQuery.setPageNumKey(1);
        }
//        FaceAppSnapListQueryParams params = new FaceAppSnapListQueryParams();

//        if (StringUtils.isNotEmpty(syssceneid)){
//            params.setSysSceneid(Integer.valueOf(syssceneid));
//        }

//        params.setSysUserid(userinfo.getId());
//        Page<FaceAppSnapListAssemblePojo> appSnapListPojoAssemblePage =  (Page<FaceAppSnapListAssemblePojo>) faceAppSnapListService.assembleAppSnapList(pageQuery,params);
//
//        model.addAttribute("appSnapListPojoAssemblePage", appSnapListPojoAssemblePage.getResult());
//        model.addAttribute("total", appSnapListPojoAssemblePage.getTotal());
//        model.addAttribute("pageNum", appSnapListPojoAssemblePage.getPageNum());
//        model.addAttribute("pageSize", appSnapListPojoAssemblePage.getPageSize());
//
        FaceAppUsescene faceAppUsescene = new FaceAppUsescene();
        faceAppUsescene.setSysUserid(userinfo.getId());
        faceAppUsescene.setDelFlag("1");
        List<FaceAppUsescene> faceAppUsesceneAllList = faceAppUsesceneService.queryList(faceAppUsescene);
        model.addAttribute("faceAppUsesceneAllList", faceAppUsesceneAllList);

        model.addAttribute("navclass","asignshow");
        return "front/faceasignshow";
    }
}
