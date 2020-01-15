package com.pface.admin.modules.jiekou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("接口invoker")
public class JmgoMemberUserParams {

    @ApiModelProperty(value = "集团id", required = false, position = 1)
    private String groupId;

    @ApiModelProperty(value = "invoker手机号码", required = true, position = 2)
    private String appId;  //MemberUser.mobile

    @ApiModelProperty(value = "invokerMd5加密密钥", required = true, position = 3)
    private String appSecret;//MemberUser.pwd
}
