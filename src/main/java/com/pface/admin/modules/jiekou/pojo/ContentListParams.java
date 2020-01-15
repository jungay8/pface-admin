package com.pface.admin.modules.jiekou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "内容查询业务参数")
public class ContentListParams {
    @ApiModelProperty(value = "上传开始时间", required = false)
    private String startTime;

    @ApiModelProperty(value = "上传结束时间", required = false)
    private String endTime;

    @ApiModelProperty(value = "分类中文名称", required = false)
    private String catalogue;

    @ApiModelProperty(value = "媒体类型：视频：VIDEO;音频：AUDIO; 图文：IMAGETEXT", required = false)
    private String mediaType;

    @ApiModelProperty(value = "栏目（用户定义的标签）", required = false)
    private String belongLabel;

    @ApiModelProperty(value = "关键字", required = false)
    private String keyWords;

    @ApiModelProperty(value = "是否转载: 0为原创,>0为转载", required = false)
    private Integer publishLabel;
}
