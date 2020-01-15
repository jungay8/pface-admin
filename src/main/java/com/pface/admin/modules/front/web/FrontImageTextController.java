package com.pface.admin.modules.front.web;

import com.pface.admin.common.Global;
import com.pface.admin.core.utils.*;
import com.pface.admin.modules.base.service.StorageService;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.member.enums.*;
import com.pface.admin.modules.member.po.*;
import com.pface.admin.modules.member.service.*;
import com.pface.admin.modules.member.service.impl.MediaDocImgServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;

/**
 * @author daniel.liu
 */
@Controller
@Slf4j
@RequestMapping("/front/imageText")
public class FrontImageTextController extends BaseCrudController<MemberMedia> {

//    @Value("${pface.upload.dir}")
//    String uploadDir;
    @Autowired
    JmgoFilePathService jmgoFilePathService;
    @Autowired
    MemberMediaService memberMediaService;
    @Autowired
    MemberUserService memberService;
    @Autowired
    MemMediaFileService memMediaFileService;
    @Autowired
    MediaDocImgServiceImpl fileImgService;
    @Autowired
    private StorageService storageService;
    /**
     * 存多张图片中的一张文件
     */
    @ResponseBody
    @PostMapping(value = {"/savePic"})
    public Result savePic(@RequestParam(name = "imageTextPicFile") MultipartFile picFile,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(true);
        MemberUser user = (MemberUser)session.getAttribute(MemberUser.USER_SESSION_KEY);
        if(user==null){
            redirectAttributes.addFlashAttribute("erros","用户信息错误");
        }

        // 父级目录
        JmgoFilePath jmgoFilePath = jmgoFilePathService.getCurrentFP();
//        String dir = uploadDir;
        if (jmgoFilePath == null) {
            redirectAttributes.addFlashAttribute("erros","上传路径没有配置！请先配置好表：jmgo_file_path!");
            log.debug("上传路径没有配置！请先配置好表：jmgo_file_path");
            return Result.failure(ResultCodeEnum.PARAM_ERROR);
        }else {
            String dir = jmgoFilePath.getVirtualpath();
            // 子级目录
            String filepath = "";
            if (user != null) {
                filepath = String.valueOf(user.getId());
            }
            filepath = filepath + File.separator + Global.IMAGE_BASE_URL;

            String saveName = null;
            String fileAbsPath = null;
            Long fileSize = null;
            try {
                String rawName = picFile.getOriginalFilename();
                String nameNoExtend = rawName.substring(0, rawName.lastIndexOf("."));
                String nameExtend = rawName.substring(rawName.lastIndexOf("."), rawName.length());
                saveName = nameNoExtend + "_" + IdUtils.getOrderSnByTime18() + nameExtend;
                File filePath = FileUtil.saveFile(picFile, dir, filepath, nameExtend);
//            File filePath = FileUtil.saveFile(picFile, dir, filepath,  FileUtil.getFileExtension(picFile.getOriginalFilename()));
                fileAbsPath = filePath.getAbsolutePath();
                fileSize = filePath.length();
            } catch (Exception e) {
                e.printStackTrace();
                return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg(e.getMessage());
            }
            return Result.success(fileAbsPath + "#" + fileSize);
        }
    }

    @ResponseBody
    @PostMapping(value = {"/submit"})
    public Result submit(@RequestParam(name = "imageTextFile") MultipartFile files,
                       @RequestParam(name = "imageTextCover") MultipartFile cover,
                       @RequestParam(name = "relationMediaIds") String[] relationMediaIds,
                       @RequestParam(name = "imageTextPicList") String[] picList,
                       @RequestParam(name = "imageTextCataId") String cataId,
                       @RequestParam(name = "imageTextBelongLabelId") String labelId,
                       @RequestParam(name = "imageTextTitle") String title,
                       @RequestParam(name = "imageTextKeyword") String keyword,
                       @RequestParam(name = "imageTextBrief") String brief,
                       @RequestParam(name = "imageTextPriceId") String priceId,
                       @RequestParam(name = "imageTextPublishId") String publishId,
                       RedirectAttributes redirectAttributes,
                       HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(true);
        MemberUser user = (MemberUser)session.getAttribute(MemberUser.USER_SESSION_KEY);
        if(user==null){
            redirectAttributes.addFlashAttribute("erros","用户信息错误");
        }

        // 父级目录
        JmgoFilePath jmgoFilePath = jmgoFilePathService.getCurrentFP();
//        String dir = uploadDir;
        if (jmgoFilePath == null) {
            redirectAttributes.addFlashAttribute("erros","上传路径没有配置！请先配置好表：jmgo_file_path!");
            log.debug("上传路径没有配置！请先配置好表：jmgo_file_path");
            return Result.failure(ResultCodeEnum.PARAM_ERROR);
        }else {
            String dir = jmgoFilePath.getVirtualpath();
            // 子级目录
            String filepath = "";
            if (user != null) {
                filepath = String.valueOf(user.getId());
            }
            filepath = filepath + File.separator + Global.IMAGE_BASE_URL;

            try {
                storageService.createImageTextMediaFile(files, cover, relationMediaIds, picList, cataId,
                        labelId, title, keyword, brief, priceId, publishId, user, dir, filepath);
            } catch (Exception e) {
                //redirectAttributes.addFlashAttribute("erros",e.getMessage());
                e.printStackTrace();
                return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg(e.getMessage());
            }

            return Result.success();
        }

    }

    @ResponseBody
    @PostMapping(value = {"/submitNoCover"})
    public Result submitNoCover(@RequestParam(name = "imageTextFile") MultipartFile files,
//                         @RequestParam(name = "imageTextCover") MultipartFile cover,
                         @RequestParam(name = "relationMediaIds") String[] relationMediaIds,
                         @RequestParam(name = "imageTextPicList") String[] picList,
                         @RequestParam(name = "imageTextCataId") String cataId,
                         @RequestParam(name = "imageTextBelongLabelId") String labelId,
                         @RequestParam(name = "imageTextTitle") String title,
                         @RequestParam(name = "imageTextKeyword") String keyword,
                         @RequestParam(name = "imageTextBrief") String brief,
                         @RequestParam(name = "imageTextPriceId") String priceId,
                         @RequestParam(name = "imageTextPublishId") String publishId,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(true);
        MemberUser user = (MemberUser) session.getAttribute(MemberUser.USER_SESSION_KEY);
        if (user == null) {
            redirectAttributes.addFlashAttribute("erros", "用户信息错误");
        }

        // 父级目录
        JmgoFilePath jmgoFilePath = jmgoFilePathService.getCurrentFP();
//        String dir = uploadDir;
        if (jmgoFilePath == null) {
            redirectAttributes.addFlashAttribute("erros","上传路径没有配置！请先配置好表：jmgo_file_path!");
            log.debug("上传路径没有配置！请先配置好表：jmgo_file_path");
            return Result.failure(ResultCodeEnum.PARAM_ERROR);

        } else {
            String dir = jmgoFilePath.getVirtualpath();
            // 子级目录
            String filepath = "";
            if (user != null) {
                filepath = String.valueOf(user.getId());
            }
            filepath = filepath + File.separator + Global.IMAGE_BASE_URL;

            try {

                storageService.createImageTextMediaFile(files, null, relationMediaIds, picList, cataId,
                        labelId, title, keyword, brief, priceId, publishId, user, dir, filepath);
//
//                File saveFile = FileUtil.saveFile(files, dir,
//                        filepath, FileUtil.getFileExtension(files.getOriginalFilename()));
//                String fileName = saveFile.getName();
//
//                Date date = DateUtils.getNowDate();
//                MemberMediaFile memberMediaFile = new MemberMediaFile();
//                memberMediaFile.setUid(user.getId());
//                memberMediaFile.setMediaType(MediaTypeEnum.IMAGETEXT);
//                memberMediaFile.setCatalogueId(Long.valueOf(cataId));
//                memberMediaFile.setIsMediaInfo(MediaStatusEnum.YES);
//                memberMediaFile.setIsPublishMedia(MediaStatusEnum.NO);
//                memberMediaFile.setMediaStatus(AuditStatusEnum.AUDITING);
////                memberMediaFile.setGoodsStatus(GoodsStatusEnum.OFF);
//                memberMediaFile.setOpDate(date);
//                memberMediaFile.setUploadDate(date);
//                memberMediaFile.setFileName(fileName);
//                memberMediaFile.setFileUrl(saveFile.getAbsolutePath());
//                memberMediaFile.setFileSize(Double.valueOf(filepath.length()));
//                memberMediaFile.setChangcodeflag(3);
//                memberMediaFile.setDelFlag(0);
//
////                Long mediaFileid = memMediaFileService.insert(memberMediaFile);
//                memMediaFileService.create(memberMediaFile);
//                Long mediaFileid= memberMediaFile.getId();
//
//                storageService.saveRelationMedia(mediaFileid, relationMediaIds);
//
//                for (int i = 0; i < picList.length; i++) {
//                    log.info("pic " + picList[i]);
//                    String[] picNameArray = picList[i].split("#");
//                    String picName = picNameArray[0];
//                    String fileSize = picNameArray[1];
//                    JmgoMemberMediaDocImgfiles docImgFiles = new JmgoMemberMediaDocImgfiles();
//                    docImgFiles.setDocid(mediaFileid);
//                    docImgFiles.setImagePath(picName);
//                    docImgFiles.setImageFileSize(Double.parseDouble(fileSize));
//                    docImgFiles.setOpDate(date);
//                    docImgFiles.setDelFlag(0);
//                    fileImgService.create(docImgFiles);
//                }
//
//                MemberMedia memberMedia = new MemberMedia();
//                memberMedia.setMediaFileId(Long.valueOf(mediaFileid));
//                memberMedia.setUid(user.getId());
//                memberMedia.setBelongLabelid(labelId);
//                memberMedia.setCatalogueId(Long.valueOf(cataId));
//                memberMedia.setMediaBrief(IdUtils.filterSpecialChar(brief));
//                memberMedia.setMediaTitle(IdUtils.filterSpecialChar(title));
//                memberMedia.setMediaKeyword(IdUtils.filterSpecialChar(keyword));
//                memberMedia.setPriceLabelid(priceId);
//                memberMedia.setPublishLabelid(publishId);
//                memberMedia.setOpDate(date);
//                memberMedia.setDelFlag(0);
//                memberMediaService.create(memberMedia);
            } catch (Exception e) {
                //redirectAttributes.addFlashAttribute("erros",e.getMessage());
                e.printStackTrace();
                return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg(e.getMessage());
            }
            return Result.success();
        }
    }

}
