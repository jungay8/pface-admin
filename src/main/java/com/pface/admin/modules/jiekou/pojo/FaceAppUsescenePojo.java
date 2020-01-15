package com.pface.admin.modules.jiekou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("使用场景")
public class FaceAppUsescenePojo implements Serializable {

    private static final long serialVersionUID = -3415798708929331666L;

    @ApiModelProperty("场景id")
    private Integer id;

    @ApiModelProperty("客户id")
    private Integer sysUserid;

    @ApiModelProperty("场景名称,如：会议名称")
    private String name;

    @ApiModelProperty("开始时间,如:会议开始时间，精确到天")
    private Date startDate;

    @ApiModelProperty("结束时间,如:会议结束时间")
    private Date endDate;

    @ApiModelProperty("签到开始时间")
    private Date asignStarttime;

    @ApiModelProperty("签到结束时间")
    private Date asignEndtime;

    @ApiModelProperty("状态: 0未开始1进行中2已结束")
    private Byte status;

    @ApiModelProperty("场景主题")
    private String theme;

    @ApiModelProperty("场景地址")
    private String address;

    @ApiModelProperty("场景创建人")
    private String createBy;

    @ApiModelProperty("场景创建日期")
    private Date createDate;

    @ApiModelProperty("场景更新人")
    private String updateBy;

    @ApiModelProperty("场景更新日期")
    private Date updateDate;

    @ApiModelProperty("场景备注")
    private String remark;

    @ApiModelProperty("删除标记，0删除；1正常，默认1")
    private String delFlag;
}
