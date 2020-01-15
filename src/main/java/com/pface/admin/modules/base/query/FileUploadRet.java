package com.pface.admin.modules.base.query;

import lombok.Data;

@Data
public class FileUploadRet {

    private Integer mediaId;  //jmgo_member_media.id
    private Integer mediaFileId;  //jmgo_member_media_file.id
    private String filePath;

    private String fileName;

    /**
     * 一个文件上传完的标识
     */
    private Boolean uploaded;
}
