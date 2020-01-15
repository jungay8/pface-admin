package com.pface.admin.modules.front.web;

import com.pface.admin.core.utils.*;
import com.pface.admin.modules.base.query.FileUploadRet;
import com.pface.admin.modules.base.service.StorageService;
import com.pface.admin.modules.front.vo.VideoMultipartFileParam;
import com.pface.admin.modules.member.enums.AuditStatusEnum;
import com.pface.admin.modules.member.enums.CoverOriginEnum;
import com.pface.admin.modules.member.enums.MediaStatusEnum;
import com.pface.admin.modules.member.po.JmgoFilePath;
import com.pface.admin.modules.member.po.MemberMedia;
import com.pface.admin.modules.member.po.MemberMediaFile;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.JmgoFilePathService;
import com.pface.admin.modules.member.service.MemMediaFileService;
import com.pface.admin.modules.member.service.MemberMediaService;
import com.pface.admin.modules.member.utils.Constants;
import com.pface.admin.modules.member.vo.ResultStatus;
import com.pface.admin.modules.member.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * pcj
 */
@Slf4j
@Controller
@RequestMapping(value = "/front/file")
public class FrontFileUploadController {

//    private Logger logger = LoggerFactory.getLogger(FrontFileUploadController.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private StorageService storageService;

    @Autowired
    private JmgoFilePathService jmgoFilePathService;

    @Autowired
    private MemberMediaService memberMediaService;

    @Autowired
    private MemMediaFileService memMediaFileService;

    /**
     * 秒传判断，断点判断
     *
     * @return
     */
    @RequestMapping(value = "checkFileMd5", method = RequestMethod.POST)
    @ResponseBody
    public Object checkFileMd5(String md5, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(true);
        MemberUser user = (MemberUser) session.getAttribute(MemberUser.USER_SESSION_KEY);
        String userId = "";
        if (user != null) {
            userId = user.getId() + "";
        }
        Object processingObj = stringRedisTemplate.opsForHash().get(Constants.FILE_UPLOAD_STATUS + userId, md5);
        if (processingObj == null) {
            return new ResultVo(ResultStatus.NO_HAVE);
        }
        String processingStr = processingObj.toString();
        boolean processing = Boolean.parseBoolean(processingStr);
        String value = stringRedisTemplate.opsForValue().get(Constants.FILE_MD5_KEY + md5);
        if (processing) {
            return new ResultVo(ResultStatus.IS_HAVE, value);
        } else {
            if (value != null) {
                File confFile = new File(value);
                byte[] completeList = FileUtils.readFileToByteArray(confFile);
                List<String> missChunkList = new LinkedList<>();
                for (int i = 0; i < completeList.length; i++) {
                    if (completeList[i] != Byte.MAX_VALUE) {
                        missChunkList.add(i + "");
                    }
                }
                return new ResultVo<>(ResultStatus.ING_HAVE, missChunkList);
            }else{
                return new ResultVo(ResultStatus.NO_HAVE);
            }

        }
    }

    /**
     * 上传文件--单文件--视频、音频--编目
     */
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public Object fileUpload(VideoMultipartFileParam param, HttpServletRequest request) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        FileUploadRet fileUploadRet = null;
        if (isMultipart) {
            log.info("上传文件start。");
            try {
                // 方法1
                //storageService.uploadFileRandomAccessFile(param);
                HttpSession session = request.getSession(true);
                MemberUser user = (MemberUser) session.getAttribute(MemberUser.USER_SESSION_KEY);
                param.setUserId(user.getId());

                JmgoFilePath jmgoFilePath = jmgoFilePathService.getCurrentFP();
                if (jmgoFilePath != null) {
                    // 方法2 这个更快点
                    fileUploadRet = storageService.uploadFileByMappedByteBuffer(param, jmgoFilePath);
                } else {
                    fileUploadRet = new FileUploadRet();
                    fileUploadRet.setUploaded(false);
                    log.info("没有配置有效的上传路径");
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error("文件上传失败。{}", param.getName());
            }
            log.info("上传文件end。");
        }
        return new ResultVo<>(ResultStatus.SUCCESS, fileUploadRet);
    }


    /**
     * 单个视频修改上传
     *
     * @param param
     * @param request
     * @return
     */
//    @RequestMapping(value = "/fileUploadModify", method = RequestMethod.POST)
//    @ResponseBody
//    public Object fileUploadModify(VideoMultipartFileParam param, HttpServletRequest request) {
//        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//        FileUploadRet fileUploadRet = null;
//        if (isMultipart) {
//            log.info("上传文件start。");
//            try {
//                HttpSession session = request.getSession(true);
//                MemberUser user = (MemberUser)session.getAttribute(MemberUser.USER_SESSION_KEY);
//                param.setUserId(user.getId());
//
//                JmgoFilePath jmgoFilePath = jmgoFilePathService.getCurrentFP();
//                if (jmgoFilePath != null) {
//                    // 方法2 这个更快点
//                    fileUploadRet = storageService.uploadFileByMappedByteBufferModify(param , jmgoFilePath);
//                }else{
//                    fileUploadRet = new FileUploadRet();
//                    fileUploadRet.setUploaded(false);
//                    log.info("没有配置有效的上传路径");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                log.error("文件上传失败。{}", param.getName());
//            }
//            log.info("上传文件end。");
//        }
//        return new ResultVo<>(ResultStatus.SUCCESS, fileUploadRet);
//    }


    /**
     * 视频批量上传 -- 不编目
     */
    @RequestMapping(value = "/fileUploadM", method = RequestMethod.POST)
    @ResponseBody
    public Object fileUploadM(VideoMultipartFileParam param, HttpServletRequest request) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        FileUploadRet fileUploadRet = null;
        if (isMultipart) {
            log.info("上传文件start。");
            try {
                HttpSession session = request.getSession(true);
                MemberUser user = (MemberUser) session.getAttribute(MemberUser.USER_SESSION_KEY);
                param.setUserId(user.getId());
                JmgoFilePath jmgoFilePath = jmgoFilePathService.getCurrentFP();
                if (jmgoFilePath != null) {
                    fileUploadRet = storageService.uploadFileByMappedByteBufferM(param, jmgoFilePath);
                } else {
                    fileUploadRet = new FileUploadRet();
                    fileUploadRet.setUploaded(false);
                    log.info("没有配置有效的上传路径");
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error("文件上传失败。{}", param.getName());
            }
            log.info("上传文件end。");
        }
//        return ResponseEntity.ok().body("上传成功。");
//        return ResponseEntity.ok().body(fileUploadRet);
        return new ResultVo<>(ResultStatus.SUCCESS, fileUploadRet);

    }

    /**
     * 带封面的文件的视频编目  --  新增
     */
    @ResponseBody
    @PostMapping("/hasfileCommit")
    public Result hasfileCommit(
            @RequestParam(name = "coverfile") MultipartFile coverfile,
            @RequestParam(name = "mediaFileId") Long mediaFileId,
            @RequestParam(name = "relationMediaIds") String[] relationMediaIds,
            @RequestParam(name = "coverOrigin") String coverOrigin,
            @RequestParam(name = "mediaType") String mediaType,
            @RequestParam(name = "mediaTitle") String mediaTitle,
            @RequestParam(name = "catalogueId") Long catalogueId,
            @RequestParam(name = "mediaKeyword") String mediaKeyword,
            @RequestParam(name = "belongLabelid") String belongLabelid,
            @RequestParam(name = "priceLabelid") String priceLabelid,
            @RequestParam(name = "publishLabelid") String publishLabelid,
            @RequestParam(name = "mediaBrief") String mediaBrief,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request, HttpServletResponse response) {
        try {

            HttpSession session = request.getSession(true);
            MemberUser user = (MemberUser) session.getAttribute(MemberUser.USER_SESSION_KEY);

            JmgoFilePath jmgoFilePath = null;
            if (coverOrigin.equals("0")) {
                jmgoFilePath = jmgoFilePathService.getCurrentFP();
                if (jmgoFilePath == null || StringUtils.isEmpty(jmgoFilePath.getVirtualpath())) {
                    log.info("没有配置有效的上传路径");
                    return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg("没有配置有效封面的上传路径");
                }
            }

            memMediaFileService.createVideoFileAttr(coverfile, mediaFileId, relationMediaIds, mediaType, mediaTitle, catalogueId,
                    mediaKeyword, belongLabelid, priceLabelid, publishLabelid, mediaBrief, user, jmgoFilePath);

        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg(ex.getMessage());
        }

        return Result.success();
    }

    /**
     * 不带封面的文件视频编目  --  新增
     */
    @ResponseBody
    @PostMapping("/normalCommit")
    public Result normalCommit(@RequestParam(name = "mediaFileId") Long mediaFileId,
                               @RequestParam(name = "relationMediaIds") String[] relationMediaIds,
                               @RequestParam(name = "coverOrigin") String coverOrigin,
                               @RequestParam(name = "mediaType") String mediaType,
                               @RequestParam(name = "mediaTitle") String mediaTitle,
                               @RequestParam(name = "catalogueId") Long catalogueId,
                               @RequestParam(name = "mediaKeyword") String mediaKeyword,
                               @RequestParam(name = "belongLabelid") String belongLabelid,
                               @RequestParam(name = "priceLabelid") String priceLabelid,
                               @RequestParam(name = "publishLabelid") String publishLabelid,
                               @RequestParam(name = "mediaBrief") String mediaBrief,
                               RedirectAttributes redirectAttributes,
                               HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(true);
            MemberUser user = (MemberUser) session.getAttribute(MemberUser.USER_SESSION_KEY);

            memMediaFileService.createVideoFileAttr(null, mediaFileId, relationMediaIds, mediaType, mediaTitle, catalogueId,
                    mediaKeyword, belongLabelid, priceLabelid, publishLabelid, mediaBrief, user, null);
        } catch (Exception ex) {
            log.info("不带封面视频文件编目系统异常");
            ex.printStackTrace();
            return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg("不带封面视频文件编目系统异常");
        }

        return Result.success().setMsg("操作成功");
    }

    /**
     * 带封面的文件的视频编目 -- 修改
     */
    @ResponseBody
    @PostMapping("/hasfileCommitUpdate")
    public Result hasfileCommitUpdate(
            @RequestParam(name = "coverfile") MultipartFile coverfile,
            @RequestParam(name = "mediaFileId") Long mediaFileId,
            @RequestParam(name = "relationMediaIds") String[] relationMediaIds,
            @RequestParam(name = "mediaId") Long mediaId,
            @RequestParam(name = "coverOrigin") String coverOrigin,
            @RequestParam(name = "mediaType") String mediaType,
            @RequestParam(name = "mediaTitle") String mediaTitle,
            @RequestParam(name = "catalogueId") Long catalogueId,
            @RequestParam(name = "mediaKeyword") String mediaKeyword,
            @RequestParam(name = "belongLabelid") String belongLabelid,
            @RequestParam(name = "priceLabelid") String priceLabelid,
            @RequestParam(name = "publishLabelid") String publishLabelid,
            @RequestParam(name = "mediaBrief") String mediaBrief,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(true);
            MemberUser user = (MemberUser) session.getAttribute(MemberUser.USER_SESSION_KEY);

            JmgoFilePath jmgoFilePath = null;
            if (coverOrigin.equals("0")) {
                jmgoFilePath = jmgoFilePathService.getCurrentFP();
                if (jmgoFilePath == null || StringUtils.isEmpty(jmgoFilePath.getVirtualpath())) {
                    log.info("没有配置有效的上传路径");
                    return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg("没有配置有效封面的上传路径");
                }
            }

            MemberMedia memberMedia_cond = new MemberMedia();
            memberMedia_cond.setMediaFileId(mediaFileId);
            MemberMedia memberMediaOld = memberMediaService.queryOne(memberMedia_cond);

            memMediaFileService.updateVideoFileAttr(coverfile, mediaFileId, relationMediaIds, coverOrigin, mediaType, mediaTitle, catalogueId, mediaKeyword, belongLabelid, priceLabelid, publishLabelid, mediaBrief, user, jmgoFilePath, memberMediaOld);

        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg(ex.getMessage());
        }

        return Result.success();
    }



    /**
     * 不带封面的文件视频编目 -- 修改
     */
    @ResponseBody
    @PostMapping("/normalCommitUpdate")
    public Result normalCommitUpdate(@RequestParam(name = "mediaFileId") Long mediaFileId,
                                     @RequestParam(name = "relationMediaIds") String[] relationMediaIds,
                                     @RequestParam(name = "mediaId") Long mediaId,
                                     @RequestParam(name = "coverOrigin") String coverOrigin,
                                     @RequestParam(name = "mediaType") String mediaType,
                                     @RequestParam(name = "mediaTitle") String mediaTitle,
                                     @RequestParam(name = "catalogueId") Long catalogueId,
                                     @RequestParam(name = "mediaKeyword") String mediaKeyword,
                                     @RequestParam(name = "belongLabelid") String belongLabelid,
                                     @RequestParam(name = "priceLabelid") String priceLabelid,
                                     @RequestParam(name = "publishLabelid") String publishLabelid,
                                     @RequestParam(name = "mediaBrief") String mediaBrief,
                                     RedirectAttributes redirectAttributes,
                                     HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(true);
            MemberUser user = (MemberUser) session.getAttribute(MemberUser.USER_SESSION_KEY);

            MemberMedia memberMedia_cond = new MemberMedia();
            memberMedia_cond.setMediaFileId(mediaFileId);
            MemberMedia memberMediaOld = memberMediaService.queryOne(memberMedia_cond);

            memMediaFileService.updateVideoFileAttr(null, mediaFileId, relationMediaIds, coverOrigin, mediaType, mediaTitle,
                    catalogueId, mediaKeyword, belongLabelid, priceLabelid, publishLabelid, mediaBrief, user, null, memberMediaOld);


//            MemberMedia memberMedia = new MemberMedia();
//            BeanUtils.copyProperties(memberMedia, memberMediaOld);
//
//            if (StringUtils.isNotEmpty(coverOrigin)) {
//                if (coverOrigin.equals("0")) {
//                    memberMedia.setCoverOrigin(CoverOriginEnum.MANUAL_IMAGE);
//                } else {
//                    memberMedia.setCoverOrigin(CoverOriginEnum.VIDEO_CAPTURE);
//                }
//            }
//
//            //修正文件
//            if (memberMediaOld != null && memberMediaOld.getId() != null) {
//                memberMedia.setId(memberMediaOld.getId());
//            }
//            memberMedia.setMediaFileId(mediaFileId);
//            memberMedia.setMediaTitle(IdUtils.filterSpecialChar(mediaTitle));
//            memberMedia.setCatalogueId(catalogueId);
//            memberMedia.setMediaKeyword(IdUtils.filterSpecialChar(mediaKeyword));
//            memberMedia.setBelongLabelid(belongLabelid);
//            memberMedia.setPriceLabelid(priceLabelid);
//            memberMedia.setPublishLabelid(publishLabelid);
//            memberMedia.setMediaBrief(IdUtils.filterSpecialChar(mediaBrief));
//            memberMedia.setUid(user.getId());
//            memberMedia.setOpDate(DateUtils.getNowDate());
//            memberMedia.setDelFlag(0);
//
//            if (memberMediaOld != null && memberMediaOld.getId() != null) {
//                memberMediaService.updateAll(memberMedia);
//            } else {
//                memberMediaService.create(memberMedia);
//            }
//
//            MemberMediaFile memberMediaFile =memMediaFileService.queryById(mediaFileId);
//            memberMediaFile.setMediaStatus(AuditStatusEnum.AUDITING);
//            memberMediaFile.setGoodsStatus(null);
//            memberMediaFile.setId(mediaFileId);
//            memberMediaFile.setIsMediaInfo(MediaStatusEnum.YES);
//            memberMediaFile.setOpDate(DateUtils.getNowDate());
//            memMediaFileService.updateAll(memberMediaFile);
//
//            storageService.saveRelationMedia(mediaFileId, relationMediaIds);

        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.failure(ResultCodeEnum.INTERNAL_SERVER_ERROR).setMsg(ex.getMessage());
        }
        return Result.success();
    }

}
