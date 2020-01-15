package com.pface.admin.modules.front.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.*;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.service.ISmsService;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.front.vo.*;
import com.pface.admin.modules.member.enums.*;
import com.pface.admin.modules.member.mapper.JmgoMemberMediaRelationMapper;
import com.pface.admin.modules.member.po.*;
import com.pface.admin.modules.member.service.*;
import com.pface.admin.modules.member.vo.MemberUserVo;
import com.pface.admin.modules.system.service.PasswordHelper;
import com.pface.admin.otherdb.service.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author daniel.liu
 */
@Controller
@RequestMapping
public class FrontIndexController extends BaseCrudController<MemberUser> {

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

    @Autowired
    private MemPriceLabService memPriceLabService;

    @Autowired
    private MemCatalogService memCatalogService;

    @Autowired
    private MemBelongLabService memBelongLabService;

    @Autowired
    private MemPubLabelService memPubLabelService;

    @Autowired
    private MemAuditLogService auditLogService;

    @Autowired
    private MemMediaFileService mediaFileService;

    @Autowired
    private MemberMediaService memberMediaService;

    @Autowired
    private MediaDocImgService mediaDocImgService;

    @Autowired
    private JmgoMemberMediaRelationMapper jmgoMemberMediaRelationMapper;
//
//    @ModelAttribute("userinfo")
//    public MemberUser getUser( HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession(true);
//        MemberUser user = (MemberUser)session.getAttribute(MemberUser.USER_SESSION_KEY);
//        return user;
//    }
//    //登录
//    @GetMapping(value={"","login","/front/login"})
//    public String login(Model model,HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession(true);
//        session.removeAttribute(MemberUser.USER_SESSION_KEY);
//        model.addAttribute("userinfo",null);
//        return "front/login";
//    }
//
//    @GetMapping(value={"/front/logout"})
//    public String logout(Model model,HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession(true);
//        session.removeAttribute(MemberUser.USER_SESSION_KEY);
//        model.addAttribute("userinfo",null);
//        return "front/login";
//    }

    //注册
//    @GetMapping(value={"/front/reg"})
//    public String reg(Model model) {
//
//        return "front/register";
//    }

//    //登录成功后
//    @GetMapping(value={"/front/front"})
//    public String index(MemberUser user,Model model, HttpServletRequest request, HttpServletResponse response) {
//
//        model.addAttribute("navclass","upload");
//        return "front/upload";
//    }


    //价格标签
//    @RequestMapping(value={"/front/price"},method = {RequestMethod.POST,RequestMethod.GET})
//    public String price( PageQuery pageQuery,MemberUser userinfo,Model model,
//                        HttpServletRequest request, HttpServletResponse response) {
//
//        userinfo=(MemberUser)model.asMap().get("userinfo");
//
//        MemberPriceLabel priceLabel=new MemberPriceLabel();
//        priceLabel.setUid(userinfo.getId());
//        priceLabel.setDelFlag(0);
//        //PageQuery pageQuery=new PageQuery();
//        if(pageQuery.getPageSizeKey()<=0){
//            pageQuery.setPageSizeKey(10);
//        }
//        if(pageQuery.getPageNumKey()<=0) {
//            pageQuery.setPageNumKey(1);
//        }
//
//        Page<MemberPriceLabel> page = (Page<MemberPriceLabel>) memPriceLabService.queryList(priceLabel, pageQuery);
//        model.addAttribute("priceList",page.getResult());
//        model.addAttribute("total", page.getTotal());
//        model.addAttribute("pageNum", page.getPageNum());
//        model.addAttribute("pageSize", page.getPageSize());
//
//        model.addAttribute("navclass","price");
//       // return Result.success(page.getResult()).addExtra("total", page.getTotal());
//        return "front/priceM";
//    }


   //认证
//    @RequestMapping(value={"/front/certification"},method = {RequestMethod.POST,RequestMethod.GET})
//    public String certification(MemberUser userinfo,Model model, HttpServletRequest request,
//                                HttpServletResponse response) {
//
//        userinfo=(MemberUser)model.asMap().get("userinfo");
//        if(userinfo.getIsCert()!=null && userinfo.getIsCert()!=CertTypeEnum.UNCERTIFIED){
//            //已经提交认证的，要取信息
//            MemberCert memberCert= certService.queryOne(new MemberCert().setUid(userinfo.getId()));
//            if(memberCert==null){memberCert=new MemberCert();}
//            model.addAttribute("memberCert",memberCert);
//            if(userinfo.getIsCert()==CertTypeEnum.CERTFAIL){
//                //认证失败，要去获取原因
//                //MemberAuditLog auditLog=new MemberAuditLog();
//                //auditLog.setBelongUid(userinfo.getId());
//                //auditLog.setMsgOriginId(userinfo.getId());
//                //auditLog.setAuditStatus(AuditStatusEnum.UNAUDITED);
//                Example example =new Example(MemberAuditLog.class);
//                example.orderBy("auditDate").desc();
//                Example.Criteria  criteria= example.createCriteria();
//                criteria.andEqualTo("msgOriginId",userinfo.getId());
//                criteria.andEqualTo("belongUid",userinfo.getId());
//                criteria.andEqualTo("delFlag",0);
//                criteria.andEqualTo("auditStatus",AuditStatusEnum.UNAUDITED);
//                List<MemberAuditLog> memberAuditLogList=auditLogService.queryListByExample(example);
//                if(memberAuditLogList!=null && memberAuditLogList.size()>0){
//                    model.addAttribute("auditLogs",memberAuditLogList.get(0).getAuditMsg());
//                }
//                //model.addAttribute("auditLogs",auditLogService.queryList(auditLog));
//            }
//
//        }else {
//            model.addAttribute("memberCert",new MemberCert());
//        }
//        model.addAttribute("navclass","certification");
//        return "front/certification";
//    }

    //我的账号
//    @RequestMapping(value={"/front/myinfo"},method = {RequestMethod.POST,RequestMethod.GET})
//    public String myinfo(MemberUser userinfo,Model model, HttpServletRequest request,
//                                HttpServletResponse response) {
//        MemberUser curuserinfo=(MemberUser)model.asMap().get("userinfo");
//        MemberUser lastedMemberUser = memberService.queryById(curuserinfo.getId());
//        model.addAttribute("lastedUserInfo", lastedMemberUser);
//        model.addAttribute("navclass","myinfo");
//        return "front/AccountM";
//    }

    //内容管理old
//    @RequestMapping(value={"/front/contentbak"},method = {RequestMethod.POST,RequestMethod.GET})
//    public String contentbak(PageQuery pageQuery,Model model, HttpServletRequest request,
//                         HttpServletResponse response) {
//
//        MemberUser userinfo = (MemberUser) model.asMap().get("userinfo");
//        if (pageQuery.getPageSizeKey() <= 0) {
//            pageQuery.setPageSizeKey(10);
//        }
//        if (pageQuery.getPageNumKey() <= 0) {
//            pageQuery.setPageNumKey(1);
//        }
//
//        Example example = new Example(MemberMediaFile.class);
//        String goodsStatus = request.getParameter("goodsStatus");
//        String mediaTypes = request.getParameter("mediaTypes");
//
//        // MemberMediaFile mediaFile= new MemberMediaFile();
//        if (userinfo != null) {
//            Example.Criteria criteria = example.createCriteria();
//            criteria.andEqualTo("uid", userinfo.getId());
//            criteria.andEqualTo("isMediaInfo",MediaStatusEnum.YES);
//            //mediaFile.setUid(userinfo.getId());
//            if (org.apache.commons.lang3.StringUtils.isNotBlank(goodsStatus)) {
//                //mediaFile.setGoodsStatus(GoodsStatusEnum.getEnumByName(goodsStatus));
//                criteria.andEqualTo("goodsStatus", GoodsStatusEnum.getEnumByName(goodsStatus));
//                model.addAttribute("selecGgoodsStatus", goodsStatus);
//            }
//            if (org.apache.commons.lang3.StringUtils.isNotBlank(mediaTypes)) {
//                //mediaFile.setMediaType(MediaTypeEnum.getEnumByName(mediaTypes));
//                model.addAttribute("selectMediaType", mediaTypes);
//                String[] types = mediaTypes.split(",");
//                if (types.length > 1) {
//                    List<String> mediaTypeEnumList = new ArrayList<String>();
//                    for (String type : types) {
//                        mediaTypeEnumList.add(type);
//                    }
//                    Example.Criteria criteria12 = example.createCriteria();
//                    criteria.andIn("mediaType", mediaTypeEnumList);
//                } else {
//                    criteria.andEqualTo("mediaType", MediaTypeEnum.getEnumByName(types[0]).getName());
//                }
//            }
//
//            Page<MemberMediaFile> mediaFilePage = (Page<MemberMediaFile>) mediaFileService.queryList(pageQuery, example);
//            List<MediaFileInfoVo> mediaFileInfoVos = new ArrayList<>();
//            for(MemberMediaFile file:mediaFilePage.getResult()){
//            //mediaFilePage.forEach(file -> {
//                MediaFileInfoVo fileInfoVo = new MediaFileInfoVo(file);
//                if (MediaStatusEnum.YES.getName().equals(file.getIsMediaInfo().getName())) {
//                    MemberMedia media = memberMediaService.queryOne(new MemberMedia().setMediaFileId(file.getId()));
//                    MediaVo mediaVo = new MediaVo();
//                    if(media!=null){
//                     BeanUtils.copyProperties(media, mediaVo);
//                    }else{
//                        continue;
//                    }
//                    String blabelId=media.getBelongLabelid();
//                    List<Integer> blabels=StringUtils.trans2long(media.getBelongLabelid(), ",");
//                    if(blabels.size()>1) {
//                        mediaVo.setBelongLabel(memBelongLabService.queryByIds(blabels));
//                        List<String> belabelList = new ArrayList<>();
//                        for (int i = 0, s = mediaVo.getBelongLabel().size(); i < s; i++) {
//                            MemberBelongLabel belongLabel = mediaVo.getBelongLabel().get(i);
//                            belabelList.add(belongLabel.getLabel());
//                        }
//                        mediaVo.setBelongLabels(org.apache.commons.lang3.StringUtils.join(belabelList, ","));
//                    }else{
//                        MemberBelongLabel belongLabel =memBelongLabService.queryById(blabelId);
//                        mediaVo.setBelongLabels(org.apache.commons.lang3.StringUtils.trimToEmpty(belongLabel.getLabel()));
//                }
//                    mediaVo.setMemberPriceLabel(memPriceLabService.queryById(media.getPriceLabelid()));
//                   // mediaVo.setPriceLabel(memPriceLabService.queryByIds(StringUtils.trans2long(media.getPriceLabelid(), ",")));
//                    //mediaVo.setMemberPriceLabel(mediaVo.getPriceLabel().get(0));
//
//                    mediaVo.setPublishLabel(memPubLabelService.queryByIds(StringUtils.trans2long(media.getPublishLabelid(), ",")));
//                    List<String> publabelList = new ArrayList<>();
//                    for (int i = 0, s = mediaVo.getPublishLabel().size(); i < s; i++) {
//                        MemberPublishLabel label = mediaVo.getPublishLabel().get(i);
//                        publabelList.add(label.getLabel());
//                    }
//                    mediaVo.setPubLabels(org.apache.commons.lang3.StringUtils.join(publabelList, ","));
//
//                    fileInfoVo.setMemberMedia(mediaVo);
//                }else{
//                    fileInfoVo.setMemberMedia(new MediaVo());
//                    continue;
//                }
//                mediaFileInfoVos.add(fileInfoVo);
//            }
//
//            //2 归属标签
//            MemberBelongLabel memberBelongLabel = new MemberBelongLabel();
//            memberBelongLabel.setUid(userinfo.getId());
//            List<MemberBelongLabel> belongLabels = memBelongLabService.queryList(memberBelongLabel);
//
//            model.addAttribute("mediaFileInfoVos", mediaFileInfoVos);
//            model.addAttribute("belongLabList", belongLabels);
//            model.addAttribute("total", mediaFilePage.getTotal());
//            model.addAttribute("pageNum", mediaFilePage.getPageNum());
//            model.addAttribute("pageSize", mediaFilePage.getPageSize());
//        }
//
//        model.addAttribute("mediaType",MediaTypeEnum.values());
//        model.addAttribute("goodsStatus",GoodsStatusEnum.values());
//        model.addAttribute("navclass","content");
//
//        return "front/conterM";
//    }

//    内容管理
    @RequestMapping(value={"/front/content"},method = {RequestMethod.POST,RequestMethod.GET})
    public String content(PageQuery pageQuery,Model model, HttpServletRequest request,
                          HttpServletResponse response) {

        MemberUser userinfo = (MemberUser) model.asMap().get("userinfo");
        if (pageQuery.getPageSizeKey() <= 0) {
            pageQuery.setPageSizeKey(10);
        }
        if (pageQuery.getPageNumKey() <= 0) {
            pageQuery.setPageNumKey(1);
        }

        String goodsStatus = request.getParameter("goodsStatus");
        String mediaTypes = request.getParameter("mediaTypes");
        String hKeyword = request.getParameter("hKeyword");
        String hcontentMCatalogue = request.getParameter("hcontentMCatalogue");
        String hcontentBelongLabel = request.getParameter("hcontentBelongLabel");
        String hqcontentMAuditStatus = request.getParameter("hqcontentMAuditStatus");

        MemberMediaQueryParams params = new MemberMediaQueryParams();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(goodsStatus)) {
            model.addAttribute("selecGgoodsStatus", goodsStatus);
            params.setGoodsStatus(goodsStatus);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(mediaTypes)) {
            model.addAttribute("selectMediaType", mediaTypes);
            params.setMediaType(mediaTypes.split(","));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(hKeyword)) {
            model.addAttribute("inputKeyword", hKeyword);
            params.setMediaKeyword(IdUtils.filterSpecialChar(hKeyword));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(hcontentMCatalogue)) {
            model.addAttribute("selectcontentMCatalogue", hcontentMCatalogue);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(hqcontentMAuditStatus)) {
            model.addAttribute("selectAuditStatus", hqcontentMAuditStatus);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(hcontentBelongLabel)) {
            model.addAttribute("selectCkBelongLabel", hcontentBelongLabel);
        }

        Long uid = userinfo.getId();

        params.setUid(uid);
        params.setIsMediaInfo(MediaStatusEnum.YES.getName());
        //params.setMediaType(IdUtils.buildInCond(mediaTypes, ","));

        if (StringUtils.isNotEmpty(hcontentBelongLabel)){
            if (hcontentBelongLabel.indexOf(",") > 0){
                String[] hcontentBelongLabelArr = hcontentBelongLabel.split(",");
                StringBuilder partSql = new StringBuilder("(");
                int len = hcontentBelongLabelArr.length;
                for (int i=0; i<len; i++){
                    if (i!=len-1) {
                        partSql.append(" FIND_IN_SET('" + hcontentBelongLabelArr[i] + "', m.belong_labelid) > 0 ").append(" or ");
                    }else{
                        partSql.append(" FIND_IN_SET('" + hcontentBelongLabelArr[i] + "', m.belong_labelid) > 0 ");
                    }
                }
                partSql.append(")");
                params.setBelongLabelid(partSql.toString());
            }else {
                params.setBelongLabelid("(FIND_IN_SET('"+hcontentBelongLabel+"', m.belong_labelid) > 0)");
            }
        }

        if(StringUtils.isNotEmpty(hcontentMCatalogue)) {
            params.setCatalogueId(Long.parseLong(hcontentMCatalogue));
        }
        if(StringUtils.isNotEmpty(hqcontentMAuditStatus)) {
            params.setMediaStatus(hqcontentMAuditStatus);
        }
        Page<MemberMediaPojo> memberMediaPage =  (Page<MemberMediaPojo>) mediaFileService.queryMemberMediaPageList(pageQuery,params);

        model.addAttribute("mediaFileInfoVos", memberMediaPage);
        model.addAttribute("total", memberMediaPage.getTotal());
        model.addAttribute("pageNum", memberMediaPage.getPageNum());
        model.addAttribute("pageSize", memberMediaPage.getPageSize());

        //2 归属标签
//        MemberBelongLabel memberBelongLabel = new MemberBelongLabel();
//        memberBelongLabel.setUid(uid);
//        List<MemberBelongLabel> belongLabels = memBelongLabService.queryList(memberBelongLabel);


        //1 类型
//        MemberCatalogue catalogue= new MemberCatalogue();
//        catalogue.setLeaf(true);
//        List<MemberCatalogue> cataloguesLabels = memCatalogService.queryList(catalogue);
        //1 类型 解决排序
//        Example example = new Example(MemberCatalogue.class);
//        example.orderBy("priority").asc();
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("leaf", 1);//是叶子
        List<MemberCatalogue> cataloguesLabels = memCatalogService.queryCataData();//.queryListByExample(example);

        //2 归属标签
        MemberBelongLabel memberBelongLabel = new MemberBelongLabel();
        memberBelongLabel.setUid(uid);
        List<MemberBelongLabel> belongLabels = memBelongLabService.queryList(memberBelongLabel);
        //3 价格
        MemberPriceLabel memberPriceLabel = new MemberPriceLabel();
        memberPriceLabel.setUid(uid);
        List<MemberPriceLabel> pricesLabels = memPriceLabService.queryList(memberPriceLabel);
        //4 转载
        MemberPublishLabel memberPublishLabel = new MemberPublishLabel();
        memberPublishLabel.setUid(uid);
        List<MemberPublishLabel> publishLabels = memPubLabelService.queryList(memberPublishLabel);

        model.addAttribute("cataloguesLabels", cataloguesLabels.toArray());
        model.addAttribute("belongLabels", belongLabels.toArray());
        model.addAttribute("pricesLabels", pricesLabels.toArray());
        model.addAttribute("publishLabels", publishLabels.toArray());
        model.addAttribute("belongLabList", belongLabels);
        model.addAttribute("mediaType",MediaTypeEnum.values());
        model.addAttribute("goodsStatus",GoodsStatusEnum.values());
        model.addAttribute("navclass","content");

        return "front/conterM";
    }

    //简单内容查询
    @ResponseBody
    @RequestMapping(value={"/front/queryMedia"},method = {RequestMethod.POST})
    public Result queryMedia(HttpServletRequest request, HttpServletResponse response) {
        String title = request.getParameter("title");
        String mediaType = request.getParameter("mediaType");
        HttpSession session = request.getSession(true);
        MemberUser user = (MemberUser)session.getAttribute(MemberUser.USER_SESSION_KEY);
        MemberMediaQueryParams params = new MemberMediaQueryParams();
        params.setUid(user.getId());
        params.setMediaType(new String[]{mediaType});
        params.setMediaKeyword(title);
        params.setIsMediaInfo("YES");
        List<MemberMediaPojo> memberMediaList =   mediaFileService.queryMemberMediaList(params);

        return Result.success().setData(memberMediaList);
    }

    @ResponseBody
    @RequestMapping(value={"/front/getRelationFiles"},method = {RequestMethod.POST})
    public Result getRelationFiles(HttpServletRequest request, HttpServletResponse response) {
        String fileMediaId = request.getParameter("fileMediaId");

        HttpSession session = request.getSession(true);
        MemberUser user = (MemberUser)session.getAttribute(MemberUser.USER_SESSION_KEY);

        MemberMediaQueryParams params = new MemberMediaQueryParams();
        params.setMediaFileId(Long.valueOf(fileMediaId));
        params.setUid(user.getId());

        List<MemberMediaRelationPojo> memberMediaList =  jmgoMemberMediaRelationMapper.queryMemberMediaRelationList(params);

        return Result.success().setData(memberMediaList);
    }

    //内容暂存库
    @RequestMapping(value={"/front/contentPause"},method = {RequestMethod.POST,RequestMethod.GET})
    public String contentPause(PageQuery pageQuery,Model model, HttpServletRequest request,
                          HttpServletResponse response) {
                String tabelTime = request.getParameter("tabelTime");
                String times = request.getParameter("times");

                Date bDate = null;
                Date eDate = null;
                if (StringUtils.isEmpty(tabelTime)){
                    tabelTime = "1";
                }

                if (tabelTime.equals("0")){
                    bDate = DateUtil.addDay(DateUtils.getNowDate(), -1);
                    eDate = DateUtils.getNowDate();
                }else {
                    if (StringUtils.isNotEmpty(times)){
                        String[] timesArr = times.split("至");
                        bDate = DateUtils.parseDate(timesArr[0]);
                        eDate = DateUtils.parseDate(timesArr[1]);
                    }
                }

                model.addAttribute("times", times);
                model.addAttribute("tabelTime", tabelTime);

                MemberUser userinfo = (MemberUser) model.asMap().get("userinfo");
                if (pageQuery.getPageSizeKey() <= 0) {
                    pageQuery.setPageSizeKey(10);
                }
                if (pageQuery.getPageNumKey() <= 0) {
                    pageQuery.setPageNumKey(1);
                }

                Example example = new Example(MemberMediaFile.class);

                if (userinfo != null) {
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andEqualTo("uid", userinfo.getId());
                    criteria.andEqualTo("isMediaInfo",MediaStatusEnum.NO);//没有编过目的
                    criteria.andEqualTo("mediaType",MediaTypeEnum.VIDEO);
                    criteria.andGreaterThanOrEqualTo("uploadDate", bDate);
                    criteria.andLessThanOrEqualTo("uploadDate", eDate);
                    example.orderBy("uploadDate").desc();
                    Page<MemberMediaFile> mediaFilePage = (Page<MemberMediaFile>) mediaFileService.queryList(pageQuery, example);

//                    MemberMediaQueryParams params = new MemberMediaQueryParams();
//                    params.setUid(userinfo.getId());
//                    params.setBUploadDate(bDate);
//                    params.setEUploadDate(eDate);
//                    params.setIsMediaInfo(MediaStatusEnum.YES.getName());
//                    params.setMediaType(MediaTypeEnum.VIDEO.getName());
//                    Page<MemberMediaPojo> memberMediaPage =   (Page<MemberMediaPojo>) mediaFileService.queryMemberMediaPageList(pageQuery,params);

                    model.addAttribute("mediaFileList", mediaFilePage.getResult());
                    model.addAttribute("total", mediaFilePage.getTotal());

                }
                    model.addAttribute("pageNum", pageQuery.getPageNum());
                    model.addAttribute("pageSize", pageQuery.getPageSize());

                //初始化数据
                Long uid = userinfo.getId();
                //1 类型
//                MemberCatalogue catalogue= new MemberCatalogue();
//                catalogue.setLeaf(true);
//                List<MemberCatalogue> cataloguesLabels = memCatalogService.queryList(catalogue);

                //1 类型 解决排序
                List<MemberCatalogue> cataloguesLabels = memCatalogService.queryCataData();

                //2 归属标签
                MemberBelongLabel memberBelongLabel = new MemberBelongLabel();
                memberBelongLabel.setUid(uid);
                List<MemberBelongLabel> belongLabels = memBelongLabService.queryList(memberBelongLabel);
                //3 价格
                MemberPriceLabel memberPriceLabel = new MemberPriceLabel();
                memberPriceLabel.setUid(uid);
                List<MemberPriceLabel> pricesLabels = memPriceLabService.queryList(memberPriceLabel);
                //4 转载
                MemberPublishLabel memberPublishLabel = new MemberPublishLabel();
                memberPublishLabel.setUid(uid);
                List<MemberPublishLabel> publishLabels = memPubLabelService.queryList(memberPublishLabel);

                model.addAttribute("cataloguesLabels", cataloguesLabels.toArray());
                model.addAttribute("belongLabels", belongLabels.toArray());
                model.addAttribute("pricesLabels", pricesLabels.toArray());
                model.addAttribute("publishLabels", publishLabels.toArray());

                model.addAttribute("navclass","contentPause");

                return "front/contentPause";
    }


    //用户归属标签
//    @RequestMapping(value={"/front/attribution"},method = {RequestMethod.POST,RequestMethod.GET})
//    public String attribution(PageQuery pageQuery,MemberUser userinfo,Model model,
//                              HttpServletRequest request, HttpServletResponse response) {
//        userinfo=(MemberUser)model.asMap().get("userinfo");
//        MemberBelongLabel label=new MemberBelongLabel();
//        label.setUid(userinfo.getId());
//        label.setDelFlag(0);
//        //PageQuery pageQuery=new PageQuery();
//        if(pageQuery.getPageSizeKey()<=0){
//            pageQuery.setPageSizeKey(10);
//        }
//        if(pageQuery.getPageNumKey()<=0) {
//            pageQuery.setPageNumKey(1);
//        }
//
//        Page<MemberBelongLabel> page = (Page<MemberBelongLabel>) memBelongLabService.queryList(label, pageQuery);
//        model.addAttribute("labelList",page.getResult());
//        model.addAttribute("total", page.getTotal());
//        model.addAttribute("pageNum", page.getPageNum());
//        model.addAttribute("pageSize", page.getPageSize());
//        model.addAttribute("navclass","attribution");
//
//        return "front/AttributionM";
//    }

    //上传
//    @GetMapping(value={"/front/upload"})
//    public String upload(MemberUser user,Model model, HttpServletRequest request,
//                              HttpServletResponse response) {
//        setUpdateStatsData(user, model);
//        model.addAttribute("navclass","upload");
//        return "front/upload";
//    }

   //转载标签
//    @RequestMapping(value={"/front/publab"},method = {RequestMethod.POST,RequestMethod.GET})
//    public String publab(PageQuery pageQuery,MemberUser userinfo,Model model,
//                         HttpServletRequest request, HttpServletResponse response) {
//        userinfo=(MemberUser)model.asMap().get("userinfo");
//        MemberPublishLabel label=new MemberPublishLabel();
//        label.setUid(userinfo.getId());
//        label.setDelFlag(0);
//        //PageQuery pageQuery=new PageQuery();
//        if(pageQuery.getPageSizeKey()<=0){
//            pageQuery.setPageSizeKey(10);
//        }
//        if(pageQuery.getPageNumKey()<=0) {
//            pageQuery.setPageNumKey(1);
//        }
//
//        Page<MemberPublishLabel> page = (Page<MemberPublishLabel>) memPubLabelService.queryList(label, pageQuery);
//        model.addAttribute("labelList",page.getResult());
//        model.addAttribute("total", page.getTotal());
//        model.addAttribute("pageNum", page.getPageNum());
//        model.addAttribute("pageSize", page.getPageSize());
//        model.addAttribute("navclass","publab");
//
//        return "front/publabM";
//    }

    //批量上传
//    @GetMapping(value={"/front/video"})
//    public String video(MemberUser user,Model model, HttpServletRequest request,
//                         HttpServletResponse response) {
//        //初始化数据
//        MemberUser userinfo=(MemberUser)model.asMap().get("userinfo");
//        Long uid = userinfo.getId();
//        //1 类型
////        MemberCatalogue catalogue= new MemberCatalogue();
////        catalogue.setLeaf(true);
////        List<MemberCatalogue> cataloguesLabels = memCatalogService.queryList(catalogue);
//        //1 类型 解决排序
//        Example example_Cata = new Example(MemberCatalogue.class);
//        example_Cata.orderBy("priority").asc();
//        Example.Criteria criteria = example_Cata.createCriteria();
//        criteria.andEqualTo("leaf", 1);//是叶子
//        List<MemberCatalogue> cataloguesLabels = memCatalogService.queryListByExample(example_Cata);
//
//
//        //2 归属标签
//        MemberBelongLabel memberBelongLabel = new MemberBelongLabel();
//        memberBelongLabel.setUid(uid);
//        List<MemberBelongLabel> belongLabels = memBelongLabService.queryList(memberBelongLabel);
//        //3 价格
//        MemberPriceLabel memberPriceLabel = new MemberPriceLabel();
//        memberPriceLabel.setUid(uid);
//        List<MemberPriceLabel> pricesLabels = memPriceLabService.queryList(memberPriceLabel);
//        //4 转载
//        MemberPublishLabel memberPublishLabel = new MemberPublishLabel();
//        memberPublishLabel.setUid(uid);
//        List<MemberPublishLabel> publishLabels = memPubLabelService.queryList(memberPublishLabel);
//
//        model.addAttribute("cataloguesLabels", cataloguesLabels.toArray());
//        model.addAttribute("belongLabels", belongLabels.toArray());
//        model.addAttribute("pricesLabels", pricesLabels.toArray());
//        model.addAttribute("publishLabels", publishLabels.toArray());
//
//        model.addAttribute("navclass","video");
//        return "front/videoM";
//    }

//    private void setUpdateStatsData(MemberUser user, Model model){
//        user=(MemberUser)model.asMap().get("userinfo");
//        //初始化数据
//        Long uid = user.getId();
//        //1 类型
////        MemberCatalogue catalogue= new MemberCatalogue();
////        catalogue.setLeaf(true);
////        List<MemberCatalogue> cataloguesLabels = memCatalogService.queryList(catalogue);
//
//        //1 类型 解决排序
////        Example example_Cata = new Example(MemberCatalogue.class);
////        example_Cata.orderBy("priority").asc();
////        Example.Criteria criteria = example_Cata.createCriteria();
////        criteria.andEqualTo("leaf", 1);//是叶子
//        List<MemberCatalogue> cataloguesLabels = memCatalogService.queryCataData();//.queryListByExample(example_Cata);
//
//        //2 归属标签
//        MemberBelongLabel memberBelongLabel = new MemberBelongLabel();
//        memberBelongLabel.setUid(uid);
//        List<MemberBelongLabel> belongLabels = memBelongLabService.queryList(memberBelongLabel);
//        //3 价格
//        MemberPriceLabel memberPriceLabel = new MemberPriceLabel();
//        memberPriceLabel.setUid(uid);
//        List<MemberPriceLabel> pricesLabels = memPriceLabService.queryList(memberPriceLabel);
//        //4 转载
//        MemberPublishLabel memberPublishLabel = new MemberPublishLabel();
//        memberPublishLabel.setUid(uid);
//        List<MemberPublishLabel> publishLabels = memPubLabelService.queryList(memberPublishLabel);
//
//        model.addAttribute("cataloguesLabels", cataloguesLabels);
//        model.addAttribute("belongLabels", belongLabels);
//        model.addAttribute("pricesLabels", pricesLabels);
//        model.addAttribute("publishLabels", publishLabels);
//    }

    //修改内容
//    @GetMapping(value={"/front/modifyContent"})
//    public String modifyContent(Model model,HttpServletRequest request,
//                                HttpServletResponse response) {
//
//        MemberUser user=(MemberUser)model.asMap().get("userinfo");
//        setUpdateStatsData(user,  model);
//
//        model.addAttribute("navclass","upload");
//
//
//
//        String id=request.getParameter("id");
//        MemberMediaFile  mediaFile=mediaFileService.queryById(id);
//        model.addAttribute("mediaFile",mediaFile);
//        if(mediaFile.getMediaType()==MediaTypeEnum.VIDEO){
//
//            MemberMedia  media= memberMediaService.queryOne(new MemberMedia().setMediaFileId(mediaFile.getId())); //TODO
//            if(media!=null) {
//                media.setCoverOrigin(CoverOriginEnum.MANUAL_IMAGE);
//                if (StringUtils.isNotEmpty(media.getCoverUrl())) {
//                    try {
//                        media.setCoverSourceUrl(URLEncoder.encode(media.getCoverUrl(), "utf-8"));
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//                model.addAttribute("media", media);
//            }
//            return "front/uploadMVideo";
//        }else if(mediaFile.getMediaType()==MediaTypeEnum.AUDIO){
//            MemberMedia  media= memberMediaService.queryOne(new MemberMedia().setMediaFileId(mediaFile.getId()));
//            model.addAttribute("media",media);
//
//            return "front/uploadMAudio";
//        }else if(mediaFile.getMediaType()==MediaTypeEnum.IMAGETEXT){
//            MemberMedia  media= memberMediaService.queryOne(new MemberMedia().setMediaFileId(mediaFile.getId()));
//            model.addAttribute("media",media);
//
//            JmgoMemberMediaDocImgfiles  docImgfile=new JmgoMemberMediaDocImgfiles();
//            docImgfile.setDocid(mediaFile.getId());
//            List<JmgoMemberMediaDocImgfiles> imgfilesList=mediaDocImgService.queryList(docImgfile);
//
//            model.addAttribute("imgFiles",imgfilesList);
//
//
//            return "front/uploadMText";
//        }
//
//        return "redirect:/front/content";
//    }


}
