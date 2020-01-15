package com.pface.admin.modules.jiekou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("客户账号信息")
public class FaceUserPojo implements Serializable {
    private static final long serialVersionUID = -190245840822224420L;

    @ApiModelProperty("客户id")
    private Integer id;
    @ApiModelProperty("名称")
    private String username;
    @ApiModelProperty("登录账户")
    private String operator;
    @ApiModelProperty("加密密码")
    private String password;
    @ApiModelProperty("状态：on启用off禁用")
    private String status;
}
