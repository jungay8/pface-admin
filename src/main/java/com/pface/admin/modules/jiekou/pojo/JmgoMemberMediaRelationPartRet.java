package com.pface.admin.modules.jiekou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("关联文件")
public class JmgoMemberMediaRelationPartRet {

    @ApiModelProperty("管理文件id")
    private Integer contentId; //relationMediaFileId
}
