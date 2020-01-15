package com.pface.admin.modules.jiekou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("图片文件信息")
public class JmgoMemberMediaDocImgfilesPartRet {

    @ApiModelProperty("图片文件路径")
    private String imagePath;

    @ApiModelProperty("图片文件大小")
    private Double imageFileSize;

}
