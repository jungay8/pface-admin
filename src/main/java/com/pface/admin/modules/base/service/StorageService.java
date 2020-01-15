package com.pface.admin.modules.base.service;

import com.pface.admin.common.MultipartFileParam;
import com.pface.admin.modules.base.query.FileUploadRet;
import com.pface.admin.modules.front.vo.VideoMultipartFileParam;
import com.pface.admin.modules.member.po.JmgoFilePath;
import com.pface.admin.modules.member.po.MemberUser;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

/**
 * 存储操作的service
 * Created by 超文 on 2017/5/2.
 */
public interface StorageService {

    /**
     * 删除全部数据
     */
    void deleteAll();

    /**
     * 初始化方法
     */
//    void init();

    /**
     * 上传文件方法1
     *
     * @param param
     * @throws IOException
     */
    void uploadFileRandomAccessFile(VideoMultipartFileParam param, JmgoFilePath jmgoFilePath) throws IOException;

    /**
     * 上传文件方法2
     * 处理文件分块，基于MappedByteBuffer来实现文件的保存
     *
     * @param param
     * @throws IOException
     */
    FileUploadRet uploadFileByMappedByteBuffer(VideoMultipartFileParam param, JmgoFilePath jmgoFilePath) throws IOException;
    FileUploadRet createVideoMediaFile(VideoMultipartFileParam videoMultipartFileParam, String fileUrl, String newFileName, JmgoFilePath jmgoFilePath, String userAudioReletivePath);
    FileUploadRet createAudioMediaFile(VideoMultipartFileParam videoMultipartFileParam, String fileUrl, String newFileName, JmgoFilePath jmgoFilePath, String userAudioReletivePath);

    /**
     * 单视频修改上传
     *
     * @param param
     * @param jmgoFilePath
     * @return
     * @throws IOException
     */
    FileUploadRet uploadFileByMappedByteBufferModify(VideoMultipartFileParam param, JmgoFilePath jmgoFilePath) throws IOException;

    FileUploadRet uploadFileByMappedByteBufferM(VideoMultipartFileParam param, JmgoFilePath jmgoFilePath) throws IOException;
    FileUploadRet createVideoMediaFileM(VideoMultipartFileParam videoMultipartFileParam, String fileUrl, String newFileName, JmgoFilePath jmgoFilePath, String userAudioReletivePath);

    void checkChangeCodeFileTimer();
    void saveRelationMedia(Long mediaFileId, String[] relationMediaFileIds);

    void createImageTextMediaFile(MultipartFile files,
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
                                          MemberUser user, String dir, String filepath);
}
