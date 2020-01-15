package com.pface.admin.modules.member.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FaceUserChannelResParams {
    private String username;
    private String deviceId;
    private String cameraStatus;

//    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private String canAuthStartdate; //可授权的最大时间

    private String channel;
    private Integer sysUserid;
    private String authBegindate;
    private String authEnddate;
    private String remark;
}
