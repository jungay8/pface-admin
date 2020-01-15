package com.pface.admin.modules.base.service.impl;

import com.pface.admin.core.exception.BizException;
import com.pface.admin.core.utils.*;
import com.pface.admin.modules.base.query.FileUploadRet;
import com.pface.admin.modules.base.service.StorageService;
import com.pface.admin.modules.front.vo.VideoMultipartFileParam;
import com.pface.admin.modules.member.enums.*;
import com.pface.admin.modules.member.mapper.JmgoFilePathMapper;
import com.pface.admin.modules.member.mapper.JmgoMemberMediaDocImgfilesMapper;
import com.pface.admin.modules.member.mapper.JmgoMemberMediaRelationMapper;
import com.pface.admin.modules.member.po.*;
import com.pface.admin.modules.member.service.JmgoFilePathService;
import com.pface.admin.modules.member.service.MemMediaFileService;
import com.pface.admin.modules.member.service.MemberMediaService;
import com.pface.admin.modules.member.service.impl.MediaDocImgServiceImpl;
import com.pface.admin.modules.member.utils.Constants;
import com.pface.admin.modules.member.utils.FileMD5Util;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class StorageServiceImpl implements StorageService {

    private final Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);


    // 保存文件的根目录
//    private Path rootPaht;

    @Autowired
    private MemMediaFileService memMediaFileService;

    @Autowired
    private MemberMediaService memberMediaService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private JmgoFilePathService jmgoFilePathService;

    @Autowired
    private JmgoMemberMediaRelationMapper jmgoMemberMediaRelationMapper;

    @Autowired
    MediaDocImgServiceImpl fileImgService;

    //这个必须与前端设定的值一致
    @Value("${jmgo.upload.chunkSize}")
    private long CHUNK_SIZE;
    @Value("${jmgo.debug}")
    private boolean debug;

//    @Value("${pface.upload.dir}")
//    private String finalDirPath;
//
//    @Value("${pface.upload.realdir}")
//    private String finalRealDirPath;
//
//    @Autowired
//    public StorageServiceImpl(@Value("${pface.upload.dir}") String location) {
//        this.rootPaht = Paths.get(location);
//    }


    @Override
    public void deleteAll() {
        if (debug) {
            JmgoFilePath jmgoFilePath = jmgoFilePathService.getCurrentFP();
            if (jmgoFilePath != null) {
                Path rootPaht = Paths.get(jmgoFilePath.getVirtualpath());
                logger.info("开发初始化清理数据，start");
//                FileSystemUtils.deleteRecursively(rootPaht.toFile());
//                stringRedisTemplate.delete(Constants.FILE_UPLOAD_STATUS+1);
//                stringRedisTemplate.delete(Constants.FILE_MD5_KEY + 1);
                logger.info("开发初始化清理数据，end");
            }
        }
    }

//    @Override
//    public void init() {
//        try {
//            Files.createDirectory(rootPaht);
//        } catch (FileAlreadyExistsException e) {
//            logger.error("文件夹已经存在了，不用再创建。");
//        } catch (IOException e) {
//            logger.error("初始化root文件夹失败。", e);
//        }
//    }

    @Override
    public void uploadFileRandomAccessFile(VideoMultipartFileParam param, JmgoFilePath jmgoFilePath) throws IOException {
        String fileName = param.getName();
        String tempDirPath = jmgoFilePath.getVirtualpath() + param.getMd5();
        String tempFileName = fileName + "_tmp";
        File tmpDir = new File(tempDirPath);
        File tmpFile = new File(tempDirPath, tempFileName);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }

        RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw");
        long offset = CHUNK_SIZE * param.getChunk();
        //定位到该分片的偏移量
        accessTmpFile.seek(offset);
        //写入该分片数据
        accessTmpFile.write(param.getFile().getBytes());
        // 释放
        accessTmpFile.close();

        boolean isOk = checkAndSetUploadProgress(param, tempDirPath);
        if (isOk) {
            boolean flag = FileUtil.renameFile(tmpFile, fileName);

            System.out.println("upload complete !!" + flag + " name=" + fileName);
        }
    }


    @Override
    public FileUploadRet uploadFileByMappedByteBufferModify(VideoMultipartFileParam param, JmgoFilePath jmgoFilePath) throws IOException {
        String fileName = param.getName();  //原文件名
//        String uploadDirPath = finalDirPath + param.getMd5();
//        String uploadDirPath = jmgoFilePath.getVirtualpath() + param.getMd5();
        String userAudioReletivePath = param.getUserId() + File.separator + param.getMediaType();
        String uploadDirPath = jmgoFilePath.getVirtualpath() + userAudioReletivePath;
        String tempFileName = fileName + "_tmp";
        File tmpDir = new File(uploadDirPath);
        File tmpFile = new File(uploadDirPath, tempFileName);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }

        RandomAccessFile tempRaf = new RandomAccessFile(tmpFile, "rw");
        FileChannel fileChannel = tempRaf.getChannel();

        //写入该分片数据
        long offset = CHUNK_SIZE * param.getChunk();
        byte[] fileData = param.getFile().getBytes();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
        mappedByteBuffer.put(fileData);
        // 释放
        FileMD5Util.freedMappedByteBuffer(mappedByteBuffer);
        fileChannel.close();
        tempRaf.close();  //add pchj

        boolean isOk = checkAndSetUploadProgress(param, uploadDirPath);

        FileUploadRet fileUploadRet = null;

        if (isOk) {

            String filterFilename = FileUtil.filterSpecFileName(fileName);
            param.setName(filterFilename);

            String new_filename = filterFilename.substring(0, filterFilename.indexOf(".")) + IdUtils.getOrderSnByTime18() + "."
                    + filterFilename.substring(filterFilename.indexOf(".") + 1, filterFilename.length());

            boolean flag = FileUtil.renameFile(tmpFile, new_filename);
            logger.info("upload complete !!" + flag + " fileName=" + fileName);

            String fileUrl = tmpFile.getParent() + File.separator + new_filename; //文件源，含文件名

            boolean debug = false;
            if (debug) {
                fileUploadRet = new FileUploadRet();
                fileUploadRet.setUploaded(true);
                fileUploadRet.setMediaId(1001);
            } else {
                if (param.getMediaType().equals("video")) {
                    fileUploadRet = createVideoMediaFile(param, fileUrl, new_filename, jmgoFilePath, userAudioReletivePath);
                } else if (param.getMediaType().equals("audio")) {
                    fileUploadRet = createAudioMediaFile(param, fileUrl, new_filename, jmgoFilePath, userAudioReletivePath);
                } else {
                    logger.info("上传媒体配置非法");
                }

            }

        }

        return fileUploadRet;
    }

    /**
     * 视频上传
     *
     * @param param
     * @param jmgoFilePath
     * @return
     * @throws IOException
     */
    @Override
    public FileUploadRet uploadFileByMappedByteBuffer(VideoMultipartFileParam param, JmgoFilePath jmgoFilePath) throws IOException {
        String fileName = param.getName();  //原文件名
//        String uploadDirPath = finalDirPath + param.getMd5();
//        String uploadDirPath = jmgoFilePath.getVirtualpath() + param.getMd5();
        String userAudioReletivePath = param.getUserId() + File.separator + param.getMediaType();
        String uploadDirPath = jmgoFilePath.getVirtualpath() + userAudioReletivePath;
        String tempFileName = fileName + "_tmp";
        File tmpDir = new File(uploadDirPath);
        File tmpFile = new File(uploadDirPath, tempFileName);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }

        RandomAccessFile tempRaf = new RandomAccessFile(tmpFile, "rw");
        FileChannel fileChannel = tempRaf.getChannel();

        //写入该分片数据
        long offset = CHUNK_SIZE * param.getChunk();
        byte[] fileData = param.getFile().getBytes();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
        mappedByteBuffer.put(fileData);
        // 释放
        FileMD5Util.freedMappedByteBuffer(mappedByteBuffer);
        fileChannel.close();
        tempRaf.close();  //add pchj

        boolean isOk = checkAndSetUploadProgress(param, uploadDirPath);

        FileUploadRet fileUploadRet = null;

        if (isOk) {

            String filterFilename = FileUtil.filterSpecFileName(fileName);
            param.setName(filterFilename);

            String new_filename = filterFilename.substring(0, filterFilename.indexOf(".")) + IdUtils.getOrderSnByTime18() + "."
                    + filterFilename.substring(filterFilename.indexOf(".") + 1, filterFilename.length());

            boolean flag = FileUtil.renameFile(tmpFile, new_filename);
            logger.info("upload complete !!" + flag + " fileName=" + fileName);

            String fileUrl = tmpFile.getParent() + File.separator + new_filename; //文件源，含文件名

            boolean debug = false;
            if (debug) {
                fileUploadRet = new FileUploadRet();
                fileUploadRet.setUploaded(true);
                fileUploadRet.setMediaId(1001);
            } else {

                if (param.getMediaType().equalsIgnoreCase("VIDEO")) {
                    fileUploadRet = createVideoMediaFile(param, fileUrl, new_filename, jmgoFilePath, userAudioReletivePath);
                } else if (param.getMediaType().equalsIgnoreCase("AUDIO")) {
                    fileUploadRet = createAudioMediaFile(param, fileUrl, new_filename, jmgoFilePath, userAudioReletivePath);
                } else {
                    logger.info("上传媒体配置非法");
                }

            }

        }

        return fileUploadRet;
    }

    @Override
    @Transactional
    public FileUploadRet createVideoMediaFile(VideoMultipartFileParam videoMultipartFileParam, String fileUrl, String newFileName, JmgoFilePath jmgoFilePath, String userAudioReletivePath) {

        FileUploadRet fileUploadRet = new FileUploadRet();
        fileUploadRet.setUploaded(true);
        fileUploadRet.setMediaId(videoMultipartFileParam.getMediaId().intValue());
        String fileName = videoMultipartFileParam.getName(); //原始文件名,做了过滤
        String ext = fileName.substring(fileName.indexOf(".") + 1, fileName.length());

        if (XmlUtils.ZHICHI_CHANGECODE_FILEEXT.indexOf(ext.toLowerCase()) >= 0) {
            try {

                logger.info("视频文件上传完了，存库。");
                String filename_std = fileName.substring(0, fileName.indexOf(".")) + "std_" + IdUtils.getOrderSnByTime18() + "." + ext;
                String filename_pre = fileName.substring(0, fileName.indexOf(".")) + "pre_" + IdUtils.getOrderSnByTime18() + "." + ext;
                String filename_jietu = fileName.substring(0, fileName.indexOf(".")) + IdUtils.getOrderSnByTime18() + ".jpg";//截图文件的扩展名确定为jpg

                //D1 保存文件memberMediaFile
                //D1.1 通过界面和系统设计确认的set信息
                MemberMediaFile memberMediaFile = new MemberMediaFile();
                memberMediaFile.setUid(videoMultipartFileParam.getUserId());
                memberMediaFile.setMediaType(MediaTypeEnum.VIDEO);
                memberMediaFile.setCatalogueId(videoMultipartFileParam.getCatalogueId());
                memberMediaFile.setIsMediaInfo(MediaStatusEnum.YES);
                memberMediaFile.setIsPublishMedia(MediaStatusEnum.NO);
                memberMediaFile.setMediaStatus(AuditStatusEnum.AUDITING);
                //            memberMediaFile.setGoodsStatus(GoodsStatusEnum.OFF);
                memberMediaFile.setFileMd5(videoMultipartFileParam.getMd5());

                //set文件名
                memberMediaFile.setFileName(fileName);
                memberMediaFile.setFileNewName(newFileName);
                memberMediaFile.setFileEncodeName(filename_std);
                memberMediaFile.setFileViewName(filename_pre);
                memberMediaFile.setFileCoverName(filename_jietu);  //为修正截图文件名作为依据
                //set文件路径
                memberMediaFile.setFileUrl(fileUrl);
                memberMediaFile.setFileEncodeUrl(jmgoFilePath.getVirtualpath() + userAudioReletivePath + File.separator + filename_std);
                memberMediaFile.setFileViewUrl(jmgoFilePath.getVirtualpath() + userAudioReletivePath + File.separator + filename_pre);
                memberMediaFile.setChangcodeflag(0); //这里是视频上传，需要转码
                //            memberMediaFile.setFileSize(FileUtil.getFileSize(fileUrl));
                memberMediaFile.setFileSize(Double.valueOf(videoMultipartFileParam.getSize()));  //统一使用页面传来的


                //            D1.2 通过转码平台接口获取媒体信息
                logger.info("debug：" + debug);
                if (!debug) {
                    logger.info("开始获取媒体信息：" + fileUrl);
                    String mediaInfo = XmlUtils.getMediaInfo(jmgoFilePath.getRealpath() + userAudioReletivePath + File.separator + newFileName);
                    logger.info("转码接口获取媒体的信息：" + mediaInfo);
                    if (StringUtils.isNotEmpty(mediaInfo)) {
                        memberMediaFile.setFileExt(XmlUtils.getNodeValue(XmlUtils.MEDIAINFO_EXT, mediaInfo));
                        String duration = XmlUtils.getNodeValue(XmlUtils.MEDIAINFO_DURATION, mediaInfo);
                        memberMediaFile.setFileDuration(StringUtils.isEmpty(duration) ? null : Long.parseLong(duration));
                        String fileSize = XmlUtils.getNodeValue(XmlUtils.MEDIAINFO_SIZE, mediaInfo);
                        memberMediaFile.setFileSize(StringUtils.isNotEmpty(fileSize) ? Double.parseDouble(fileSize) : 0D);
                        memberMediaFile.setFileBitrate(XmlUtils.getNodeValue(XmlUtils.MEDIAINFO_BITRATE, mediaInfo));
                        memberMediaFile.setFileCodec(XmlUtils.getNodeValue(XmlUtils.MEDIAINFO_CODEC, mediaInfo));
                        String resolution = XmlUtils.getNodeValue(XmlUtils.MEDIAINFO_RESOLUTION, mediaInfo);
                        memberMediaFile.setFileResolution(resolution);
                        String aspectRation = XmlUtils.getNodeValue(XmlUtils.MEDIAINFO_ASPECT_RATIO, mediaInfo);
                        memberMediaFile.setFileAspectRation(aspectRation);

                        //D1.3 提交转码，set转码信息
                        String zmRet = XmlUtils.changeVideo(fileName, newFileName, filename_std, filename_pre, filename_jietu, aspectRation, resolution, jmgoFilePath.getRealpath() + userAudioReletivePath + File.separator);
                        //获取转码任务id
                        String taskId = XmlUtils.getNodeAttrValue("/task", zmRet, "id");
                        memberMediaFile.setChangcodetaskid(taskId);
                        memberMediaFile.setChangcodeResult(StringUtils.isNotEmpty(zmRet) ? zmRet.substring(0, 500) : null);
                    }
                }
                memberMediaFile.setUploadDate(DateUtils.getNowDate());
                memberMediaFile.setDelFlag(0);
                memberMediaFile.setOpDate(memberMediaFile.getUploadDate());

                //D1.4 执行保存
                Long mediaFileid = memMediaFileService.insert(memberMediaFile);
                logger.info("saveMediaFileRet = " + mediaFileid);

                //存储关联媒体
                saveRelationMedia(mediaFileid, videoMultipartFileParam.getRelationMediaIds());

                //D2 保存MemberMedia
                MemberMedia memberMedia = new MemberMedia();
                memberMedia.setMediaFileId(Long.valueOf(mediaFileid));
                memberMedia.setBelongLabelid(videoMultipartFileParam.getBelongLabelid());
                memberMedia.setMediaTitle(IdUtils.filterSpecialChar(videoMultipartFileParam.getMediaTitle()));
                memberMedia.setMediaKeyword(IdUtils.filterSpecialChar(videoMultipartFileParam.getMediaKeyword()));
                memberMedia.setPriceLabelid(videoMultipartFileParam.getPriceLabelid());
                memberMedia.setPublishLabelid(videoMultipartFileParam.getPublishLabelid());
                memberMedia.setCatalogueId(videoMultipartFileParam.getCatalogueId());
                memberMedia.setMediaBrief(IdUtils.filterSpecialChar(videoMultipartFileParam.getMediaBrief()));
                if (videoMultipartFileParam.getCoverOrigin().equals("0")) {
                    memberMedia.setCoverOrigin(CoverOriginEnum.MANUAL_IMAGE);
                } else {
                    memberMedia.setCoverOrigin(CoverOriginEnum.VIDEO_CAPTURE);
                }
                memberMedia.setUid(videoMultipartFileParam.getUserId());
                memberMedia.setOpDate(DateUtils.getNowDate());
                memberMedia.setDelFlag(0);
                Integer saveMediaRet = memberMediaService.create(memberMedia);
                logger.info(" add saveMediaRet = " + saveMediaRet);

                //D3 设置返回信息
                fileUploadRet.setMediaId(memberMedia.getId().intValue());
                fileUploadRet.setFileName(fileName);
                fileUploadRet.setFilePath(fileUrl);
            } catch (Exception ex) {
                ex.printStackTrace();
                fileUploadRet.setMediaId(-1);
                MemberUser user = new MemberUser();
                user.setId(videoMultipartFileParam.getUserId());
                memMediaFileService.physicalDeleteFileAndDb(videoMultipartFileParam.getMediaType(), null, null, user);//删除文件
//                throw new BizException(ResultCodeEnum.NOT_IMPLEMENTED);
            }
        } else {
            //完善封面信息（如果有）
            if (StringUtils.isNotEmpty(videoMultipartFileParam.getCoverOrigin()) &&
                    videoMultipartFileParam.getCoverOrigin().equals("0")) { //通过上传来的封面
                try {
                    MemberMedia memberMedia = new MemberMedia();
                    memberMedia.setId(videoMultipartFileParam.getMediaId());
                    //                memberMedia.setCoverUrl(fileUrl);
                    memberMedia.setUploadCoverUrl(fileUrl);
                    memberMedia.setFileCoverRawName(fileName);
                    //封面属性不需要实现秒传，所以这里不需要存储md5
                    //                memberMedia.setFileCoverMd5(videoMultipartFileParam.getMd5());
                    if (StringUtils.isNotEmpty(videoMultipartFileParam.getMd5()) && stringRedisTemplate.opsForHash().hasKey(
                            Constants.FILE_UPLOAD_STATUS + videoMultipartFileParam.getUserId(), videoMultipartFileParam.getMd5())) {
                        stringRedisTemplate.opsForHash().delete(Constants.FILE_UPLOAD_STATUS + videoMultipartFileParam.getUserId(), videoMultipartFileParam.getMd5());
                    }
                    if (StringUtils.isNotEmpty(videoMultipartFileParam.getMd5())) {
                        stringRedisTemplate.delete(Constants.FILE_MD5_KEY + videoMultipartFileParam.getMd5());
                    }
                    if (videoMultipartFileParam.getMediaId() != null && videoMultipartFileParam.getMediaId() > 0) {
                        Integer ii = memberMediaService.updateNotNull(memberMedia);
                        logger.info(" update saveMediaRet = " + ii);
                    }
                } catch (Exception ex) {

                    MemberMedia memberMedia = memberMediaService.queryById(videoMultipartFileParam.getMediaId());
                    memberMediaService.deleteById(videoMultipartFileParam.getMediaId());
                    memMediaFileService.deleteById(memberMedia.getMediaFileId());
                    MemberUser user = new MemberUser();
                    user.setId(videoMultipartFileParam.getUserId());
                    memMediaFileService.physicalDeleteFileAndDb(videoMultipartFileParam.getMediaType(), videoMultipartFileParam.getMediaId()+"", memberMedia.getMediaFileId() + "", user);//删除文件和数据库

                    ex.printStackTrace();
//                    throw new BizException(ResultCodeEnum.NOT_IMPLEMENTED);
                }
            }
        }

        return fileUploadRet;
    }

    @Override
    @Transactional
    public FileUploadRet createAudioMediaFile(VideoMultipartFileParam videoMultipartFileParam, String fileUrl, String newFileName, JmgoFilePath jmgoFilePath, String userAudioReletivePath) {
        videoMultipartFileParam.setCoverOrigin("0");  //音频封面的来源，音频不转码，来自手动上传
        FileUploadRet fileUploadRet = new FileUploadRet();
        fileUploadRet.setUploaded(true);
        fileUploadRet.setMediaId(videoMultipartFileParam.getMediaId().intValue());
        String fileName = videoMultipartFileParam.getName(); //原始文件名,做了过滤
        String ext = fileName.substring(fileName.indexOf(".") + 1, fileName.length());

        if (XmlUtils.ZHICHI_AUDIO_FILEEXT.indexOf(ext.toLowerCase()) >= 0) {
            logger.info("音频文件上传完了，存库。");
            try {

                //D1 保存文件memberMediaFile
                //D1.1 通过界面和系统设计确认的set信息
                MemberMediaFile memberMediaFile = new MemberMediaFile();
                memberMediaFile.setUid(videoMultipartFileParam.getUserId());
                memberMediaFile.setMediaType(MediaTypeEnum.AUDIO);
                memberMediaFile.setCatalogueId(videoMultipartFileParam.getCatalogueId());

                memberMediaFile.setIsMediaInfo(MediaStatusEnum.YES);
                memberMediaFile.setIsPublishMedia(MediaStatusEnum.NO);
                memberMediaFile.setMediaStatus(AuditStatusEnum.AUDITING);
//            memberMediaFile.setGoodsStatus(GoodsStatusEnum.OFF);
                memberMediaFile.setFileMd5(videoMultipartFileParam.getMd5());
                memberMediaFile.setFileName(fileName);
                memberMediaFile.setFileNewName(newFileName);
                memberMediaFile.setFileUrl(fileUrl);
                memberMediaFile.setChangcodeflag(3); //这里是音频上传，不需要转码
                memberMediaFile.setFileSize(Double.valueOf(videoMultipartFileParam.getSize()));
                memberMediaFile.setUploadDate(DateUtils.getNowDate());
                memberMediaFile.setDelFlag(0);
                memberMediaFile.setOpDate(memberMediaFile.getUploadDate());

                //D1.4 执行保存
                Long mediaFileid = memMediaFileService.insert(memberMediaFile);
                logger.info("saveMediaFileRet = " + mediaFileid);

                //保存关联媒体
                saveRelationMedia(mediaFileid, videoMultipartFileParam.getRelationMediaIds());

                //D2 保存MemberMedia
                MemberMedia memberMedia = new MemberMedia();
                memberMedia.setMediaFileId(Long.valueOf(mediaFileid));
                memberMedia.setBelongLabelid(videoMultipartFileParam.getBelongLabelid());
                memberMedia.setMediaTitle(IdUtils.filterSpecialChar(videoMultipartFileParam.getMediaTitle()));
                memberMedia.setMediaKeyword(IdUtils.filterSpecialChar(videoMultipartFileParam.getMediaKeyword()));
                memberMedia.setPriceLabelid(videoMultipartFileParam.getPriceLabelid());
                memberMedia.setPublishLabelid(videoMultipartFileParam.getPublishLabelid());
                memberMedia.setCatalogueId(videoMultipartFileParam.getCatalogueId());
                memberMedia.setMediaBrief(IdUtils.filterSpecialChar(videoMultipartFileParam.getMediaBrief()));
                memberMedia.setCoverOrigin(CoverOriginEnum.MANUAL_IMAGE); //手动上传来的
//            memberMedia.setCoverUrl("empty cover abs path");

                memberMedia.setUid(videoMultipartFileParam.getUserId());
                memberMedia.setOpDate(DateUtils.getNowDate());
                memberMedia.setDelFlag(0);
                Integer saveMediaRet = memberMediaService.create(memberMedia);
                logger.info(" add saveMediaRet = " + saveMediaRet);

                //D3 设置返回信息
                fileUploadRet.setMediaId(memberMedia.getId().intValue());
                fileUploadRet.setFileName(fileName);
                fileUploadRet.setFilePath(fileUrl);
            } catch (Exception ex) {
                MemberUser user = new MemberUser();
                user.setId(videoMultipartFileParam.getUserId());
                memMediaFileService.physicalDeleteFileAndDb(videoMultipartFileParam.getMediaType(), null, null, user);//删除文件
                fileUploadRet.setMediaId(-1);
                ex.printStackTrace();
//                throw new BizException(ResultCodeEnum.NOT_IMPLEMENTED);
            }
        } else {
            //完善封面信息（如果有）
            if (StringUtils.isNotEmpty(videoMultipartFileParam.getCoverOrigin()) &&
                    videoMultipartFileParam.getCoverOrigin().equals("0")) { //通过上传来的封面
                try {
                    logger.info("音频文件上传完了，更新封面:mediaId = " + videoMultipartFileParam.getMediaId());
                    logger.info("音频文件上传完了，更新封面:fileUrl = " + fileUrl);
                    MemberMedia memberMedia = new MemberMedia();
                    memberMedia.setId(videoMultipartFileParam.getMediaId());
                    memberMedia.setFileCoverRawName(fileName);
                    //                memberMedia.setCoverUrl(fileUrl);
                    memberMedia.setUploadCoverUrl(fileUrl);
                    //                memberMedia.setFileCoverMd5(videoMultipartFileParam.getMd5());
                    if (StringUtils.isNotEmpty(videoMultipartFileParam.getMd5()) && stringRedisTemplate.opsForHash().hasKey(
                            Constants.FILE_UPLOAD_STATUS + videoMultipartFileParam.getUserId(), videoMultipartFileParam.getMd5())) {
                        stringRedisTemplate.opsForHash().delete(Constants.FILE_UPLOAD_STATUS + videoMultipartFileParam.getUserId(), videoMultipartFileParam.getMd5());
                    }
                    if (StringUtils.isNotEmpty(videoMultipartFileParam.getMd5())) {
                        stringRedisTemplate.delete(Constants.FILE_MD5_KEY + videoMultipartFileParam.getMd5());
                    }
                    //                memberMedia.setCoverSourceUrl();
                    if (videoMultipartFileParam.getMediaId() != null && videoMultipartFileParam.getMediaId() > 0) {
                        Integer ii = memberMediaService.updateNotNull(memberMedia);
                        logger.info(" update saveMediaRet = " + ii);
                    }
                } catch (Exception ex) {

                    MemberMedia memberMedia = memberMediaService.queryById(videoMultipartFileParam.getMediaId());
                    memberMediaService.deleteById(videoMultipartFileParam.getMediaId());
                    memMediaFileService.deleteById(memberMedia.getMediaFileId());
                    MemberUser user = new MemberUser();
                    user.setId(videoMultipartFileParam.getUserId());
                    memMediaFileService.physicalDeleteFileAndDb(videoMultipartFileParam.getMediaType(),
                            videoMultipartFileParam.getMediaId()+"",
                            memberMedia.getMediaFileId() + "", user);//删除文件和数据库

                    ex.printStackTrace();
//                    throw new BizException(ResultCodeEnum.NOT_IMPLEMENTED);
                }
            }
        }

        return fileUploadRet;
    }


    /**
     * 保存关联媒体
     *
     * @param mediaFileId
     * @param relationMediaFileIds
     */
    @Override
    public void saveRelationMedia(Long mediaFileId, String[] relationMediaFileIds) {
        if (relationMediaFileIds.length <= 0) {
            return;
        }
        if (mediaFileId == null) {
            return;
        }

        //删除
        Condition condition = new Condition(JmgoMemberMediaRelation.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("mediaFileId", mediaFileId);
        jmgoMemberMediaRelationMapper.deleteByCondition(condition);
        //新增
        List<JmgoMemberMediaRelation> insertList = new ArrayList<JmgoMemberMediaRelation>();
        JmgoMemberMediaRelation jmgoMemberMediaRelation = null;
        for (String relationMediaFileId : relationMediaFileIds) {
            if (!relationMediaFileId.equalsIgnoreCase(String.valueOf(mediaFileId))) { //自己不能关联自己
                jmgoMemberMediaRelation = new JmgoMemberMediaRelation();
                jmgoMemberMediaRelation.setMediaFileId(mediaFileId.intValue());
                jmgoMemberMediaRelation.setRelationMediaFileId(Integer.valueOf(relationMediaFileId));
                insertList.add(jmgoMemberMediaRelation);
            }
        }
        if (!insertList.isEmpty()) {
            jmgoMemberMediaRelationMapper.insertList(insertList);
        }
    }

    /**
     * 视频批量上传
     *
     * @param param
     * @param jmgoFilePath
     * @return
     * @throws IOException
     */
    @Override
    public FileUploadRet uploadFileByMappedByteBufferM(VideoMultipartFileParam param, JmgoFilePath jmgoFilePath) throws IOException {
        String fileName = param.getName();  //原文件名
//        String uploadDirPath = finalDirPath + param.getMd5();
//        String uploadDirPath = jmgoFilePath.getVirtualpath() + param.getMd5();
        String userAudioReletivePath = param.getUserId() + File.separator + "VIDEO";
        String uploadDirPath = jmgoFilePath.getVirtualpath() + userAudioReletivePath;
        String tempFileName = fileName + "_tmp";
        File tmpDir = new File(uploadDirPath);
        File tmpFile = new File(uploadDirPath, tempFileName);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }

        RandomAccessFile tempRaf = new RandomAccessFile(tmpFile, "rw");
        FileChannel fileChannel = tempRaf.getChannel();

        //写入该分片数据
        long offset = CHUNK_SIZE * param.getChunk();
        byte[] fileData = param.getFile().getBytes();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
        mappedByteBuffer.put(fileData);
        // 释放
        FileMD5Util.freedMappedByteBuffer(mappedByteBuffer);
        fileChannel.close();
        tempRaf.close();  //add pchj

        boolean isOk = checkAndSetUploadProgress(param, uploadDirPath);

        FileUploadRet fileUploadRet = null;

        if (isOk) {

            String filterFilename = FileUtil.filterSpecFileName(fileName);
            param.setName(filterFilename);

            String new_filename = filterFilename.substring(0, filterFilename.indexOf(".")) + IdUtils.getOrderSnByTime18() + "."
                    + filterFilename.substring(filterFilename.indexOf(".") + 1, filterFilename.length());

            boolean flag = FileUtil.renameFile(tmpFile, new_filename);
            logger.info("upload complete !!" + flag + " fileName=" + fileName);

            String fileUrl = tmpFile.getParent() + File.separator + new_filename; //文件源，含文件名


            fileUploadRet = createVideoMediaFileM(param, fileUrl, new_filename, jmgoFilePath, userAudioReletivePath);

        }

        return fileUploadRet;
    }

    @Override
    @Transactional
    public FileUploadRet createVideoMediaFileM(VideoMultipartFileParam videoMultipartFileParam, String fileUrl, String newFileName, JmgoFilePath jmgoFilePath, String userAudioReletivePath) {

        FileUploadRet fileUploadRet = new FileUploadRet();
        fileUploadRet.setUploaded(true);
        fileUploadRet.setMediaFileId(videoMultipartFileParam.getMediaFileId().intValue());
        String fileName = videoMultipartFileParam.getName(); //原始文件名,做了过滤
        String ext = fileName.substring(fileName.indexOf(".") + 1, fileName.length());
        fileUploadRet.setFileName(fileName);
        fileUploadRet.setFilePath(fileUrl);

        try{
            logger.info("视频文件上传完了，存库。");
            String filename_std = fileName.substring(0, fileName.indexOf(".")) + "std_" + IdUtils.getOrderSnByTime18() + "." + ext;
            String filename_pre = fileName.substring(0, fileName.indexOf(".")) + "pre_" + IdUtils.getOrderSnByTime18() + "." + ext;
            String filename_jietu = fileName.substring(0, fileName.indexOf(".")) + IdUtils.getOrderSnByTime18() + ".jpg";

            //D1 保存文件memberMediaFile
            //D1.1 通过界面和系统设计确认的set信息
            MemberMediaFile memberMediaFile = new MemberMediaFile();
            memberMediaFile.setUid(videoMultipartFileParam.getUserId());
            memberMediaFile.setMediaType(MediaTypeEnum.VIDEO);
            memberMediaFile.setCatalogueId(videoMultipartFileParam.getCatalogueId());
            memberMediaFile.setIsMediaInfo(MediaStatusEnum.NO);
            memberMediaFile.setIsPublishMedia(MediaStatusEnum.NO);
            memberMediaFile.setMediaStatus(AuditStatusEnum.AUDITING);
    //        memberMediaFile.setGoodsStatus(GoodsStatusEnum.OFF);
            memberMediaFile.setFileMd5(videoMultipartFileParam.getMd5());

            //set文件名
            memberMediaFile.setFileName(fileName);
            memberMediaFile.setFileNewName(newFileName);
            memberMediaFile.setFileEncodeName(filename_std);
            memberMediaFile.setFileViewName(filename_pre);
            memberMediaFile.setFileCoverName(filename_jietu);
            //set文件路径
            memberMediaFile.setFileUrl(fileUrl);
            memberMediaFile.setFileEncodeUrl(jmgoFilePath.getVirtualpath() + userAudioReletivePath + File.separator + filename_std);
            memberMediaFile.setFileViewUrl(jmgoFilePath.getVirtualpath() + userAudioReletivePath + File.separator + filename_pre);
            memberMediaFile.setChangcodeflag(0); //这里是视频上传，需要转码
            memberMediaFile.setFileSize(Double.valueOf((new File(fileUrl)).length()));

    //            D1.2 通过转码平台接口获取媒体信息

            if (!debug) {
                String mediaInfo = XmlUtils.getMediaInfo(jmgoFilePath.getRealpath() + userAudioReletivePath + File.separator + newFileName);
                if (StringUtils.isNotEmpty(mediaInfo)) {
                    memberMediaFile.setFileExt(XmlUtils.getNodeValue(XmlUtils.MEDIAINFO_EXT, mediaInfo));
                    String duration = XmlUtils.getNodeValue(XmlUtils.MEDIAINFO_DURATION, mediaInfo);
                    memberMediaFile.setFileDuration(StringUtils.isEmpty(duration) ? null : Long.parseLong(duration));
                    String fileSize = XmlUtils.getNodeValue(XmlUtils.MEDIAINFO_SIZE, mediaInfo);
                    memberMediaFile.setFileSize(StringUtils.isNotEmpty(fileSize) ? Double.parseDouble(fileSize) : null);
                    memberMediaFile.setFileBitrate(XmlUtils.getNodeValue(XmlUtils.MEDIAINFO_BITRATE, mediaInfo));
                    memberMediaFile.setFileCodec(XmlUtils.getNodeValue(XmlUtils.MEDIAINFO_CODEC, mediaInfo));
                    String resolution = XmlUtils.getNodeValue(XmlUtils.MEDIAINFO_RESOLUTION, mediaInfo);
                    memberMediaFile.setFileResolution(resolution);
                    String aspectRation = XmlUtils.getNodeValue(XmlUtils.MEDIAINFO_ASPECT_RATIO, mediaInfo);
                    memberMediaFile.setFileAspectRation(aspectRation);

                    //D1.3 提交转码，set转码信息
                    String zmRet = XmlUtils.changeVideo(fileName, newFileName, filename_std, filename_pre, filename_jietu, aspectRation, resolution, jmgoFilePath.getRealpath() + userAudioReletivePath + File.separator);

                    //获取转码任务id
                    if (StringUtils.isNotEmpty(zmRet)) {
                        String taskId = XmlUtils.getNodeAttrValue("/task", zmRet, "id");
                        memberMediaFile.setChangcodetaskid(taskId);
                        memberMediaFile.setChangcodeResult(StringUtils.isNotEmpty(zmRet) ? zmRet.substring(0, 500) : null);
                    }
                }
            }
            memberMediaFile.setUploadDate(DateUtils.getNowDate());
            memberMediaFile.setDelFlag(0);
            memberMediaFile.setOpDate(memberMediaFile.getUploadDate());

            //D1.4 执行保存
            Long mediaFileid = memMediaFileService.insert(memberMediaFile);

            logger.info("saveMediaFileRet = " + mediaFileid);

            //D2 设置返回信息
            fileUploadRet.setMediaFileId(mediaFileid.intValue());
        } catch (Exception ex) {
            ex.printStackTrace();
            fileUploadRet.setMediaFileId(-1);
            MemberUser user = new MemberUser();
            user.setId(videoMultipartFileParam.getUserId());
            memMediaFileService.physicalDeleteFileAndDb(videoMultipartFileParam.getMediaType(), null, null, user);//删除文件
//                throw new BizException(ResultCodeEnum.NOT_IMPLEMENTED);
        }

        return fileUploadRet;
    }

    /**
     * 定时器
     * <p>
     * 修正 监控转码状态、修正截图文件名称
     */
    @Override
    public void checkChangeCodeFileTimer() {
        MemberMediaFile memberMediaFile = new MemberMediaFile();
        memberMediaFile.setChangcodeflag(0);
        memberMediaFile.setIsMediaInfo(MediaStatusEnum.YES);//编目的文件才纳入修正
        List<MemberMediaFile> list = memMediaFileService.queryList(memberMediaFile);
        MemberMediaFile ff = null;
        logger.info("修正转码进度任务数：" + list.size());
        long s = System.currentTimeMillis();

        for (MemberMediaFile file : list) {

            String ss = XmlUtils.getChangeCodeStatus(file);
            logger.info("获取转码进度的返回：" + ss);
            //            获取转码进度的返回：<?xml version="1.0" encoding="UTF-8"?>
            //<results>
            //		<result id="64944">
            //		<status>COMPLETED</status>
            //		<startAt>2019-07-15 11:47:44</startAt>
            //		<completeAt>2019-07-15 11:47:58</completeAt>
            //		<lastError></lastError>
            //		<lastErrorDescription></lastErrorDescription>
            //		<progress>
            //			<input index="0" elapsed="14" power="42">100</input>
            //		</progress>
            //	</result>
            //</results>
            if (StringUtils.isNotEmpty(ss)) {
                String status = XmlUtils.getNodeValue("/results/result/status", ss);
                logger.info("获取转码进度的状态：" + status);
//            COMPLETED：已经转码成功
//            RUNINNG：正在转码
//            ERROR：转码失败
                if (StringUtils.isNotEmpty(status)) {
                    logger.info("转码进度的状态不空");
                    if (status.equalsIgnoreCase("COMPLETED")) {
                        ff = new MemberMediaFile();
                        ff.setId(file.getId());
                        ff.setChangcodeflag(1);
                        ff.setFileEncodeSize(FileUtil.getFileSize(file.getFileEncodeUrl()));
                        ff.setFileViewSize(FileUtil.getFileSize(file.getFileViewUrl()));
                        Integer re = memMediaFileService.updateNotNull(ff);
                        logger.info("更新转码状态返回：" + re);
                        if (StringUtils.isNotEmpty(file.getFileCoverName())) {
                            logger.info("进入修正截图：" + file.getFileCoverName());
                            adjustJietuImg(file);
                        }
                    } else if (status.equalsIgnoreCase("ERROR")) {
                        ff = new MemberMediaFile();
                        ff.setId(file.getId());
                        ff.setChangcodeflag(2);
                        memMediaFileService.updateNotNull(ff);
                    } else if (status.equalsIgnoreCase("RUNNING")) {
                        logger.info("文件状态：" + file.getFileUrl() + "  正在转码。");
                    } else {
                        logger.info("文件状态：" + file.getFileUrl() + "  未知。");
                    }
                }
            }
        }

        logger.info("修正耗时：" + (System.currentTimeMillis() - s) + "ms");
    }

    /**
     * 修正截图封面文件名
     * <p>
     * 如：546456456757_filename -->filename
     *
     * @param memberMediaFile
     */
    public void adjustJietuImg(MemberMediaFile memberMediaFile) {

        logger.info("进入adjustJietuImg()");
        MemberMedia memberMedia = new MemberMedia();
        memberMedia.setMediaFileId(memberMediaFile.getId());
        memberMedia.setCoverOrigin(CoverOriginEnum.VIDEO_CAPTURE);//截图的才需要修正
        List<MemberMedia> list = memberMediaService.queryList(memberMedia);
        if (!list.isEmpty()) {

            String imgDirPath = memberMediaFile.getFileEncodeUrl().substring(0, memberMediaFile.getFileEncodeUrl().indexOf(memberMediaFile.getFileEncodeName()));
            List<File> files = FileUtil.searchFileInDirLike(new File(imgDirPath), memberMediaFile.getFileCoverName());
            logger.info("找到类似文件数：" + files.size());

            MemberMedia saveMemberMedia = list.get(0);
            saveMemberMedia.setCoverUrl(imgDirPath + memberMediaFile.getFileCoverName());
            Integer ii = memberMediaService.updateNotNull(saveMemberMedia);
            logger.info(" update saveMediaRet = " + ii);

            if (!files.isEmpty()) {
                File file = files.get(0);  //memberMediaFile.getFileCoverName()的命名是保证是唯一的，所以这里只能找到一个
                boolean flag = FileUtil.renameFile(file, memberMediaFile.getFileCoverName());
                if (flag) {
                    logger.info("修正文件名：" + file.getName() + "成功");
                } else {
                    logger.info("修正文件名：" + file.getName() + "失败");
                }
            }
        }
    }


    /**
     * 检查并修改文件上传进度
     *
     * @param param
     * @param uploadDirPath
     * @return
     * @throws IOException
     */
    private boolean checkAndSetUploadProgress(VideoMultipartFileParam param, String uploadDirPath) throws IOException {
        String fileName = param.getName();
        File confFile = new File(uploadDirPath, fileName + ".conf");
        RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");
        //把该分段标记为 true 表示完成
        System.out.println("set part " + param.getChunk() + " complete");
        accessConfFile.setLength(param.getChunks());
        accessConfFile.seek(param.getChunk());
        accessConfFile.write(Byte.MAX_VALUE);

        //completeList 检查是否全部完成,如果数组里是否全部都是(全部分片都成功上传)
        byte[] completeList = FileUtils.readFileToByteArray(confFile);
        byte isComplete = Byte.MAX_VALUE;
        for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
            //与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
            isComplete = (byte) (isComplete & completeList[i]);
            System.out.println("check part " + i + " complete?:" + completeList[i]);
        }

        accessConfFile.close();
        if (isComplete == Byte.MAX_VALUE) {
            stringRedisTemplate.opsForHash().put(Constants.FILE_UPLOAD_STATUS + param.getUserId(), param.getMd5(), "true");
            stringRedisTemplate.opsForValue().set(Constants.FILE_MD5_KEY + param.getMd5(), uploadDirPath + File.separator + fileName);
            return true;
        } else {
//          if (!stringRedisTemplate.opsForHash().hasKey(Constants.FILE_UPLOAD_STATUS, param.getMd5())) {
            if (!stringRedisTemplate.opsForHash().hasKey(Constants.FILE_MD5_KEY, param.getMd5())) {
                stringRedisTemplate.opsForHash().put(Constants.FILE_UPLOAD_STATUS + param.getUserId(), param.getMd5(), "false");
            }
            if (stringRedisTemplate.hasKey(Constants.FILE_MD5_KEY + param.getMd5())) {
                stringRedisTemplate.opsForValue().set(Constants.FILE_MD5_KEY + param.getMd5(), uploadDirPath + File.separator + fileName + ".conf");
            }
            return false;
        }
    }

    @Override
    @Transactional
    public void createImageTextMediaFile(MultipartFile files,
                                          MultipartFile cover,
                                          String[] relationMediaIds,
                                          String[] picList,
                                          String cataId,
                                          String labelId,
                                          String title,
                                          String keyword,
                                          String brief,
                                          String priceId,
                                          String publishId,
                                          MemberUser user, String dir, String filepath) {
        File coverFile = null;
        File saveFile = null;
        try {
            saveFile = FileUtil.saveFile(files, dir,
                    filepath, FileUtil.getFileExtension(files.getOriginalFilename()));
            String fileName = saveFile.getName();

            String absPath = null;
            if (cover != null) {
                coverFile = FileUtil.saveFile(cover, dir,
                        filepath, FileUtil.getFileExtension(cover.getOriginalFilename()));
                absPath = coverFile.getAbsolutePath();
            }
            Date nowDate = DateUtils.getNowDate();
            MemberMediaFile memberMediaFile = new MemberMediaFile();
            memberMediaFile.setUid(user.getId());
            memberMediaFile.setMediaType(MediaTypeEnum.IMAGETEXT);
            memberMediaFile.setCatalogueId(Long.valueOf(cataId));
            memberMediaFile.setIsMediaInfo(MediaStatusEnum.YES);
            memberMediaFile.setIsPublishMedia(MediaStatusEnum.NO);
            memberMediaFile.setMediaStatus(AuditStatusEnum.AUDITING);
//      memberMediaFile.setGoodsStatus(GoodsStatusEnum.OFF);
            memberMediaFile.setOpDate(nowDate);
            memberMediaFile.setUploadDate(nowDate);
            memberMediaFile.setFileUrl(saveFile.getAbsolutePath());
            memberMediaFile.setFileName(fileName);
            memberMediaFile.setFileSize(Double.valueOf(filepath.length()));
            memberMediaFile.setChangcodeflag(3);
            memberMediaFile.setDelFlag(0);

//      Long mediaFileid = memMediaFileService.insert(memberMediaFile);
            memMediaFileService.create(memberMediaFile);
            Long mediaFileid = memberMediaFile.getId();

            saveRelationMedia(mediaFileid, relationMediaIds);

            for (int i = 0; i < picList.length; i++) {
                logger.info("pic " + picList[i]);
                String[] picNameArray = picList[i].split("#");
                String imagepath = picNameArray[0];
                String fileSize = picNameArray[1];
                if (cover == null) {
                    if (i == 0) {//取第一个作为封面
                        absPath = imagepath;
                    }
                }
                JmgoMemberMediaDocImgfiles docImgFiles = new JmgoMemberMediaDocImgfiles();
                docImgFiles.setDocid(mediaFileid);
                docImgFiles.setImagePath(imagepath);
                docImgFiles.setImageFileSize(Double.parseDouble(fileSize));
                docImgFiles.setOpDate(nowDate);
                docImgFiles.setDelFlag(0);
                fileImgService.create(docImgFiles);
            }

            MemberMedia memberMedia = new MemberMedia();
            memberMedia.setMediaFileId(Long.valueOf(mediaFileid));
            memberMedia.setUid(user.getId());
//      memberMedia.setCoverUrl(absPath);
            memberMedia.setUploadCoverUrl(absPath);
            memberMedia.setBelongLabelid(labelId);
            memberMedia.setCatalogueId(Long.valueOf(cataId));
            memberMedia.setMediaBrief(IdUtils.filterSpecialChar(brief));
            memberMedia.setMediaTitle(IdUtils.filterSpecialChar(title));
            memberMedia.setMediaKeyword(IdUtils.filterSpecialChar(keyword));
            memberMedia.setPriceLabelid(priceId);
            memberMedia.setPublishLabelid(publishId);
            memberMedia.setCoverOrigin(CoverOriginEnum.MANUAL_IMAGE);
            memberMedia.setOpDate(nowDate);
            memberMedia.setDelFlag(0);
            memberMediaService.create(memberMedia);

        } catch (Exception ex) {
            ex.printStackTrace();
            rollbackImageTextFile(saveFile, coverFile, picList);
            throw new BizException(ResultCodeEnum.NOT_IMPLEMENTED);
        }
    }

    private void rollbackImageTextFile(File saveFile, File coverFile,String[] picList){
        if (saveFile!=null){
            FileUtil.deleteFile(saveFile.getAbsolutePath());
        }
        if (coverFile!=null){
            FileUtil.deleteFile(coverFile.getAbsolutePath());
        }

        if (picList!=null && picList.length>0){
            for (int i = 0; i < picList.length; i++) {
                String[] picNameArray = picList[i].split("#");
                String imagepath = picNameArray[0];
                FileUtil.deleteFile(imagepath);
            }
        }

    }
}
