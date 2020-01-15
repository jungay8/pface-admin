package com.pface.admin.modules.member.service.impl;

import com.github.pagehelper.PageHelper;
import com.pface.admin.core.exception.BizException;
import com.pface.admin.core.utils.*;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.service.StorageService;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.front.vo.GoodsVo;
import com.pface.admin.modules.front.vo.MemberMediaPojo;
import com.pface.admin.modules.front.vo.MemberMediaQueryParams;
import com.pface.admin.modules.front.vo.MemberUserspaceAssemblyParams;
import com.pface.admin.modules.member.enums.*;
import com.pface.admin.modules.member.mapper.*;
import com.pface.admin.modules.member.po.*;
import com.pface.admin.modules.member.service.MemMediaFileService;
import com.pface.admin.modules.member.service.MemberMediaService;
import com.pface.admin.modules.member.utils.Constants;
import com.pface.admin.modules.member.vo.AuditLogVo;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/8
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Service
public class MemMediaFileSerImpl  extends BaseService<MemberMediaFile> implements MemMediaFileService {

    @Autowired
    private MemberMediaFileMapper mediaFileMapper;

    @Autowired
    private MemberMediaService memberMediaService;

    @Autowired
    private MemberMediaMapper memberMediaMapper;

    @Autowired
    private JmgoMemberMediaDocImgfilesMapper jmgoMemberMediaDocImgfilesMapper;

    @Autowired
    private JmgoMemberMediaRelationMapper jmgoMemberMediaRelationMapper;

    @Autowired
    private MemberAuditLogMapper memberAuditLogMapper;

    @Autowired
    private StorageService storageService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public void logicDeleteBatchByIds(Long[] ids) {

        for(int i=0;i<ids.length;i++) {
            MemberMediaFile label = new MemberMediaFile();
            label.setId(ids[i]);
            label.setDelFlag(1);
            super.updateNotNull(label);
        }
    }

    @Override
    @Transactional
    public Long insert(MemberMediaFile memberMediaFile){
        mediaFileMapper.insert(memberMediaFile);
        //mediaFileMapper.insertUseGeneratedKeys(memberMediaFile);
        return memberMediaFile.getId();
    }

    @Override
    @Transactional
    public void batchDel(List<GoodsVo> ids) {
        for(int i=0;i<ids.size();i++) {
            GoodsVo vo=ids.get(i);
            MemberMediaFile label = new MemberMediaFile();
            label.setId(vo.getId());
            label.setDelFlag(1);
            super.updateNotNull(label);
        }
    }

    @Override
    public void batchGoodsOn(List<GoodsVo> ids) {
        for(int i=0;i<ids.size();i++) {
            GoodsVo vo=ids.get(i);
            MemberMediaFile label = new MemberMediaFile();
            label.setId(vo.getId());
            label.setGoodsStatus(GoodsStatusEnum.ON);
            super.updateNotNull(label);
        }
    }

    @Override
    public void batchGoodsOff(List<GoodsVo> ids) {
        for(int i=0;i<ids.size();i++) {
            GoodsVo vo=ids.get(i);
            MemberMediaFile label = new MemberMediaFile();
            label.setId(vo.getId());
            label.setGoodsStatus(GoodsStatusEnum.OFF);
            super.updateNotNull(label);
        }
    }

    @Override
    @Transactional
    public boolean auditNoPass(AuditLogVo auditLogVo) {
        //用户表更新
        Date now = DateUtils.getNowDate();
        MemberMediaFile mediaFile= super.queryById(auditLogVo.getMsgOriginId());
        mediaFile.setId(auditLogVo.getMsgOriginId());
        mediaFile.setMediaStatus(AuditStatusEnum.UNAUDITED);
        mediaFile.setGoodsStatus(null);
        mediaFile.setOpDate(now);
        super.updateAll(mediaFile);

        updateMediaMediaFile(auditLogVo);

        //更新审核记录表
        MemberAuditLog memberAuditLog=new MemberAuditLog();
        memberAuditLog.setAuditStatus(AuditStatusEnum.UNAUDITED);
        memberAuditLog.setBelongUid(auditLogVo.getBelongUid());
        memberAuditLog.setMsgOriginId(auditLogVo.getMsgOriginId());
        memberAuditLog.setAuditDate(now);
        memberAuditLog.setOpDate(now);
        memberAuditLog.setDelFlag(0);
        memberAuditLog.setAuditMsg(auditLogVo.getAuditMsg());
        memberAuditLog.setAuditUid(auditLogVo.getAuditUid());

        int row= memberAuditLogMapper.insert(memberAuditLog);

        return row>0;
    }

    @Override
    @Transactional
    public boolean auditPass(AuditLogVo auditLogVo) {
        Date now = DateUtils.getNowDate();
        //用户表更新
        MemberMediaFile mediaFile=new MemberMediaFile();
        mediaFile.setId(auditLogVo.getMsgOriginId());
        mediaFile.setMediaStatus(AuditStatusEnum.AUDITED);
        mediaFile.setGoodsStatus(GoodsStatusEnum.ON);
        mediaFile.setOpDate(now);
        super.updateNotNull(mediaFile);

        updateMediaMediaFile(auditLogVo);

        //更新审核记录表
        MemberAuditLog memberAuditLog=new MemberAuditLog();
        memberAuditLog.setAuditStatus(AuditStatusEnum.AUDITED);
        memberAuditLog.setBelongUid(auditLogVo.getBelongUid());
        memberAuditLog.setMsgOriginId(auditLogVo.getMsgOriginId());
        memberAuditLog.setAuditDate(now);
        memberAuditLog.setOpDate(now);
        memberAuditLog.setDelFlag(0);
        memberAuditLog.setAuditMsg(auditLogVo.getAuditMsg());
        memberAuditLog.setAuditUid(auditLogVo.getAuditUid());

        int row= memberAuditLogMapper.insert(memberAuditLog);

        return row>0;
    }

    private void updateMediaMediaFile(AuditLogVo auditLogVo) {
        if (auditLogVo!=null && auditLogVo.getMsgOriginId() != null) {
            Example example = new Example(MemberMedia.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("mediaFileId", auditLogVo.getMsgOriginId());

            MemberMedia updateMemberMedia = memberMediaMapper.selectOneByExample(example);
            updateMemberMedia.setMediaTitle(IdUtils.filterSpecialChar(auditLogVo.getMediaTitle()));
            updateMemberMedia.setMediaKeyword(IdUtils.filterSpecialChar(auditLogVo.getMediaKeyword()));
            updateMemberMedia.setCatalogueId(auditLogVo.getCatalogueId());
            updateMemberMedia.setPriceLabelid(auditLogVo.getPriceid());
            updateMemberMedia.setPublishLabelid(auditLogVo.getPubLabel());
            updateMemberMedia.setBelongLabelid(auditLogVo.getBelongLabels());
            updateMemberMedia.setMediaBrief(IdUtils.filterSpecialChar(auditLogVo.getMediaBrief()));
            updateMemberMedia.setOpDate(DateUtils.getNowDate());
            memberMediaMapper.updateByPrimaryKey(updateMemberMedia);
        }
    }

    /**
     * 媒体内容查询
     *
     * @return
     */
    @Override
    public List<MemberMediaPojo> queryMemberMediaPageList(PageQuery pageQuery,MemberMediaQueryParams params){
        return PageHelper.startPage(pageQuery)
                .doSelectPage(() -> mediaFileMapper.queryMemberMediaList(params));
    }

    @Override
    public List<MemberMediaPojo> queryMemberMediaList(MemberMediaQueryParams params){
        return mediaFileMapper.queryMemberMediaList(params);
    }


    @Override
    public List<MemberUserspace> assembleUserspacePageList(PageQuery pageQuery,MemberUserspace memberUserspace){
        return PageHelper.startPage(pageQuery)
                .doSelectPage(() -> mediaFileMapper.assembleUserspace(memberUserspace));
    }

    @Override
    @Transactional
    public void createVideoFileAttr(MultipartFile coverfile,
                                    Long mediaFileId,
                                    String[] relationMediaIds,
                                    String mediaType,
                                    String mediaTitle,
                                    Long catalogueId,
                                    String mediaKeyword,
                                    String belongLabelid,
                                    String priceLabelid,
                                    String publishLabelid,
                                    String mediaBrief,
                                    MemberUser user, JmgoFilePath jmgoFilePath) {
        File coverSave = null;
        try {

            MemberMedia memberMedia = new MemberMedia();
            memberMedia.setMediaFileId(mediaFileId);
            memberMedia.setCoverOrigin(CoverOriginEnum.VIDEO_CAPTURE);

            if (coverfile != null) {
                memberMedia.setCoverOrigin(CoverOriginEnum.MANUAL_IMAGE);

                String filepath = user.getId() + File.separator + "VIDEO";
                if (StringUtils.isNotEmpty(mediaType)) {
                    filepath = user.getId() + File.separator + mediaType;
                }

                if (jmgoFilePath != null) {
                    coverSave = FileUtil.saveFile(coverfile, jmgoFilePath.getVirtualpath(),
                            filepath, FileUtil.getFileExtension(coverfile.getOriginalFilename()));

//              memberMedia.setCoverUrl(coverPath.getAbsolutePath());
                    memberMedia.setUploadCoverUrl(coverSave.getAbsolutePath());
                    memberMedia.setFileCoverRawName(coverfile.getOriginalFilename());
                }
            }

            memberMedia.setMediaTitle(IdUtils.filterSpecialChar(mediaTitle));
            memberMedia.setCatalogueId(catalogueId);
            memberMedia.setMediaKeyword(IdUtils.filterSpecialChar(mediaKeyword));
            memberMedia.setBelongLabelid(belongLabelid);
            memberMedia.setPriceLabelid(priceLabelid);
            memberMedia.setPublishLabelid(publishLabelid);
            memberMedia.setMediaBrief(IdUtils.filterSpecialChar(mediaBrief));
            memberMedia.setUid(user.getId());
            memberMedia.setOpDate(DateUtils.getNowDate());
            memberMedia.setDelFlag(0);
            memberMediaService.create(memberMedia);

            MemberMediaFile memberMediaFile = new MemberMediaFile();
            memberMediaFile.setId(mediaFileId);
            memberMediaFile.setIsMediaInfo(MediaStatusEnum.YES);
            memberMediaFile.setOpDate(DateUtils.getNowDate());
            updateNotNull(memberMediaFile);

            storageService.saveRelationMedia(mediaFileId, relationMediaIds);

        } catch (Exception ex) {
            if (coverSave != null) {
                FileUtil.deleteFile(coverSave.getAbsolutePath());
            }
            logger.info("保存新增编目时错误。");
            ex.printStackTrace();
            throw new BizException(ResultCodeEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void updateVideoFileAttr(MultipartFile coverfile,
                                    Long mediaFileId,
                                    String[] relationMediaIds,
                                    String coverOrigin,
                                    String mediaType,
                                    String mediaTitle,
                                    Long catalogueId,
                                    String mediaKeyword,
                                    String belongLabelid,
                                    String priceLabelid,
                                    String publishLabelid,
                                    String mediaBrief, MemberUser user, JmgoFilePath jmgoFilePath,
                                    MemberMedia memberMediaOld){
        File coverSave = null;
        try {
            MemberMedia memberMedia = new MemberMedia();
            BeanUtils.copyProperties(memberMedia, memberMediaOld);

            if (StringUtils.isNotEmpty(coverOrigin)) {
                if (coverOrigin.equals("0")) {
                    memberMedia.setCoverOrigin(CoverOriginEnum.MANUAL_IMAGE);
                } else {
                    memberMedia.setCoverOrigin(CoverOriginEnum.VIDEO_CAPTURE);
                }
            }

            if (coverfile != null) {
                String filepath = user.getId() + File.separator + "VIDEO";
                if (StringUtils.isNotEmpty(mediaType)) {
                    filepath = user.getId() + File.separator + mediaType;
                }

                //修改前删除原来的文件
                FileUtil.deleteFile(memberMediaOld.getUploadCoverUrl());
                coverSave = FileUtil.saveFile(coverfile, jmgoFilePath.getVirtualpath(),
                        filepath, FileUtil.getFileExtension(coverfile.getOriginalFilename()));

                memberMedia.setUploadCoverUrl(coverSave.getAbsolutePath());
                memberMedia.setFileCoverRawName(coverfile.getOriginalFilename());
            }

            //修正文件
            if (memberMediaOld != null && memberMediaOld.getId() != null) {
                memberMedia.setId(memberMediaOld.getId());
            }
            memberMedia.setMediaFileId(mediaFileId);
            memberMedia.setMediaTitle(IdUtils.filterSpecialChar(mediaTitle));
            memberMedia.setCatalogueId(catalogueId);
            memberMedia.setMediaKeyword(IdUtils.filterSpecialChar(mediaKeyword));
            memberMedia.setBelongLabelid(belongLabelid);
            memberMedia.setPriceLabelid(priceLabelid);
            memberMedia.setPublishLabelid(publishLabelid);
            memberMedia.setMediaBrief(IdUtils.filterSpecialChar(mediaBrief));
            memberMedia.setUid(user.getId());
            memberMedia.setOpDate(DateUtils.getNowDate());
            memberMedia.setDelFlag(0);
            if (memberMediaOld != null && memberMediaOld.getId() != null) {
                memberMediaService.updateAll(memberMedia);
            } else {
                memberMediaService.create(memberMedia);
            }

            MemberMediaFile memberMediaFile = queryById(mediaFileId);
            memberMediaFile.setMediaStatus(AuditStatusEnum.AUDITING);
            memberMediaFile.setGoodsStatus(null);
            memberMediaFile.setId(mediaFileId);
            memberMediaFile.setIsMediaInfo(MediaStatusEnum.YES);
            memberMediaFile.setOpDate(DateUtils.getNowDate());
            updateAll(memberMediaFile);

            storageService.saveRelationMedia(mediaFileId, relationMediaIds);
        }catch (Exception ex){
            if (coverSave != null){
                FileUtil.deleteFile(coverSave.getAbsolutePath());
            }
            logger.info("保存修改编目时错误。");
            ex.printStackTrace();
            throw new BizException(ResultCodeEnum.INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    @Transactional
    public void physicalDeleteFileAndDb(String mediaType, String mediaId, String mediaFileId, MemberUser userinfo){
        MemberMedia memberMedia = memberMediaMapper.selectByPrimaryKey(mediaId);
        MemberMediaFile memberMediaFile = mediaFileMapper.selectByPrimaryKey(mediaFileId);

        //del from db
        if (StringUtils.isNotEmpty(mediaId)) {
            memberMediaMapper.deleteByPrimaryKey(mediaId);
        }
        if (StringUtils.isNotEmpty(mediaFileId)) {
            mediaFileMapper.deleteByPrimaryKey(mediaFileId);
        }

        //删除redis中文件的md5
        if (StringUtils.isNotEmpty(memberMediaFile.getFileMd5())){
            stringRedisTemplate.delete(Constants.FILE_MD5_KEY + memberMediaFile.getFileMd5());
        }
        if (StringUtils.isNotEmpty(memberMedia.getFileCoverMd5())){
            stringRedisTemplate.delete(Constants.FILE_MD5_KEY + memberMedia.getFileCoverMd5());
        }

        //删除封面
        if (StringUtils.isNotEmpty(memberMedia.getCoverUrl())) { //非默认封面才删除
            FileUtil.deleteFile(memberMedia.getCoverUrl());
        }
        if (StringUtils.isNotEmpty(memberMedia.getUploadCoverUrl())) { //非默认封面才删除
            FileUtil.deleteFile(memberMedia.getUploadCoverUrl());
        }
        //上传时产生的临时文件
        if ((mediaType.equalsIgnoreCase("VIDEO") ||
                (mediaType.equalsIgnoreCase("AUDIO"))) &&
                StringUtils.isNotEmpty(memberMedia.getUploadCoverUrl())){
            FileUtil.deleteFile(FileUtil.getDirName(memberMedia.getUploadCoverUrl()) + memberMedia.getFileCoverRawName() + ".conf");//删除临时文件
        }

        //删除文件
        if (StringUtils.isNotEmpty(memberMediaFile.getFileUrl())) {
            FileUtil.deleteFile(memberMediaFile.getFileUrl());
        }
        if (StringUtils.isNotEmpty(memberMediaFile.getFileEncodeUrl())) {
            FileUtil.deleteFile(memberMediaFile.getFileEncodeUrl());
        }
        if (StringUtils.isNotEmpty(memberMediaFile.getFileViewUrl())) {
            FileUtil.deleteFile(memberMediaFile.getFileViewUrl());
        }
        if (StringUtils.isNotEmpty(memberMediaFile.getFileUrl()) &&
                StringUtils.isNotEmpty(memberMediaFile.getFileName())) {
            FileUtil.deleteFile(FileUtil.getDirName(memberMediaFile.getFileUrl()) + memberMediaFile.getFileName() + ".conf");//删除临时文件
        }
       //删除redis hash 中的key
        if (userinfo!=null && userinfo.getId()!=null){
            if (StringUtils.isNotEmpty(memberMediaFile.getFileMd5()) && stringRedisTemplate.opsForHash().hasKey(
                    Constants.FILE_UPLOAD_STATUS+userinfo.getId(), memberMediaFile.getFileMd5())){
                stringRedisTemplate.opsForHash().delete(Constants.FILE_UPLOAD_STATUS+userinfo.getId(), memberMediaFile.getFileMd5());
            }
            if (StringUtils.isNotEmpty(memberMedia.getFileCoverMd5()) && stringRedisTemplate.opsForHash().hasKey(
                    Constants.FILE_UPLOAD_STATUS+userinfo.getId(), memberMedia.getFileCoverMd5())){
                stringRedisTemplate.opsForHash().delete(Constants.FILE_UPLOAD_STATUS+userinfo.getId(), memberMedia.getFileCoverMd5());
            }
        }

        if (mediaType.equalsIgnoreCase(MediaTypeEnum.IMAGETEXT.getName())){
            JmgoMemberMediaDocImgfiles docImgfiles = new JmgoMemberMediaDocImgfiles();
            docImgfiles.setDocid(Long.valueOf(mediaFileId));
            List<JmgoMemberMediaDocImgfiles> list = jmgoMemberMediaDocImgfilesMapper.select(docImgfiles);
            for (JmgoMemberMediaDocImgfiles docfile:list
                 ) {
                FileUtil.deleteFile(docfile.getImagePath());
            }

            jmgoMemberMediaDocImgfilesMapper.delete(docImgfiles);
        }

        //删除关联fromdb
        Example example_rela = new Example(JmgoMemberMediaRelation.class);
        Example.Criteria relaCriteria = example_rela.createCriteria();
        relaCriteria.andEqualTo("mediaFileId", mediaFileId);
        jmgoMemberMediaRelationMapper.deleteByExample(example_rela);
    }
}
