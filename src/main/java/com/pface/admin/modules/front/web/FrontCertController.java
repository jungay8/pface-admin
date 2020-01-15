package com.pface.admin.modules.front.web;

import com.github.pagehelper.Page;
import com.pface.admin.common.Global;
import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.FileUtil;
import com.pface.admin.core.utils.Result;
import com.pface.admin.core.utils.ResultCodeEnum;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.front.vo.CertVo;
import com.pface.admin.modules.member.enums.CertTypeEnum;
import com.pface.admin.modules.member.enums.MemberTypeEnum;
import com.pface.admin.modules.member.po.JmgoFilePath;
import com.pface.admin.modules.member.po.MemberCert;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.JmgoFilePathService;
import com.pface.admin.modules.member.service.MemberCertService;
import com.pface.admin.modules.member.service.MemberUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.List;

/**
 * @author daniel.liu
 */
@Slf4j
@Controller
@RequestMapping("/front/cert")
public class FrontCertController extends BaseCrudController<MemberCert> {

    @Autowired
    private MemberCertService certService;

    @Autowired
    private MemberUserService memberService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

//    @Value("${pface.pubfile}")
//    private String pubfile;
//
//    @Value("${pface.upload.dir}")
//    private String uploadDir;

    @Autowired
    private JmgoFilePathService jmgoFilePathService;

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("cert:view")
    @Override
    public Result<List<MemberCert>> queryList(MemberCert cert, PageQuery pageQuery) {
        Page<MemberCert> page = (Page<MemberCert>) certService.queryList(cert, pageQuery);

        return Result.success(page.getResult()).addExtra("total", page.getTotal());
    }

    @ResponseBody
    @PostMapping("/create")
    @SystemLog("归属标签创建")
    @Override
    public Result create(@Validated MemberCert cert) {
        certService.create(cert);
        applicationEventPublisher.publishEvent(cert);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/update")
    @SystemLog("归属标签更新")
    @Override
    public Result update(@Validated MemberCert cert) {
        certService.updateNotNull(cert);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/delete-batch")
    @SystemLog("归属标签删除")
    @Override
    public Result deleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
        super.deleteBatchByIds(ids);
        return Result.success();
    }

    @PostMapping(value = { "/submitCert" })
    public String submitCert(@RequestParam(name="cert",required=false)MultipartFile[] files, CertVo certVo,
                            RedirectAttributes redirectAttributes,
                             HttpServletRequest request, HttpServletResponse response){

        MemberUser  user= memberService.queryOne(new MemberUser().setId(certVo.getUid()));
        if (certVo!=null && StringUtils.isNotEmpty(certVo.getMemberType())){
            if(certVo.getMemberType().equalsIgnoreCase(MemberTypeEnum.COMPANY.getName())){
                user.setMemberType(MemberTypeEnum.COMPANY);
            }else{
                user.setMemberType(MemberTypeEnum.PERSON);
            }
        }

        if(user==null){
            redirectAttributes.addFlashAttribute("erros","用户信息错误");
        }
        if(files.length<3){
            if(user.getMemberType()==MemberTypeEnum.COMPANY){
                redirectAttributes.addFlashAttribute("erros","企业标志或公司证件扫描件没上传");
            }else{
                redirectAttributes.addFlashAttribute("erros","个人头像或身份证件扫描件没上传");
            }
        }

        String filepath= "";
        if(user!=null){
            filepath=String.valueOf(user.getId()) + File.separator + Global.USERFILES_BASE_URL;
        }
        JmgoFilePath jmgoFilePath = jmgoFilePathService.getCurrentFP();
        String dir=jmgoFilePath.getVirtualpath();
        if(jmgoFilePath==null){
            log.debug("上传路径没有配置！请先配置好表：jmgo_file_path");
//            return Result.failure(ResultCodeEnum.PARAM_ERROR);
//            dir=jmgoFilePath.getVirtualpath();
        }else {
            try {
                //第一个文件图片
                File filePath1 = FileUtil.saveFile(files[0], dir, filepath, FileUtil.getFileExtension(files[0].getOriginalFilename()));
                String fileName1 = filePath1.getPath();// filePath1.getPath().substring(dir.length()).substring(filepath.length()+1).replaceAll("\\\\","");

                //第二个文件
                File filePath2 = FileUtil.saveFile(files[1], dir, filepath, FileUtil.getFileExtension(files[1].getOriginalFilename()));
                String fileName2 = filePath2.getPath();//filePath2.getPath().substring(dir.length()).substring(filepath.length()+1).replaceAll("\\\\","");

                //第三个文件
                File filePath3 = FileUtil.saveFile(files[2], dir, filepath, FileUtil.getFileExtension(files[2].getOriginalFilename()));
                String fileName3 = filePath3.getPath();//filePath3.getPath().substring(dir.length()).substring(filepath.length()+1).replaceAll("\\\\","");

                MemberCert memberCert = certService.queryOne(new MemberCert().setUid(user.getId()));
                if (memberCert == null) {
                    memberCert = new MemberCert();
                }

                if (user.getMemberType() == MemberTypeEnum.COMPANY) {
                    //公司

                    memberCert.setCompanyName(certVo.getCompanyName());
                    memberCert.setCompanyCode(certVo.getCompanyCert());
                    memberCert.setCompanyFront(fileName2);
                    memberCert.setCompanyBack(fileName3);
                } else {
                    //个人
                    memberCert.setPersonId(certVo.getPersonId());
                    memberCert.setPersonidFront(fileName2);
                    memberCert.setPersonidBack(fileName3);
                }
                memberCert.setIconUrl(fileName1);
                memberCert.setContactWay1(certVo.getTel());
                memberCert.setContactWay2(certVo.getTel());

                if (memberCert.getId() == null) {
                    memberCert.setUid(user.getId());
                    certService.create(memberCert);
                } else {
                    certService.updateNotNull(memberCert);
                }
                user.setIsCert(CertTypeEnum.CERTIFING);
                memberService.updateNotNull(user);
                //MemberUser  user= memberService.queryOne(new MemberUser().setId(certVo.getUid()));
                HttpSession session = request.getSession(true);
                session.setAttribute(MemberUser.USER_SESSION_KEY, memberService.queryOne(new MemberUser().setId(certVo.getUid())));

            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("erros", e.getMessage());
            }
        }
        return "redirect:/front/certification";
    }

}
