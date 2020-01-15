package com.pface.admin.modules.member.vo;

import lombok.Data;

import java.util.Date;

@Data
public class FaceUserChannelResVo {
    private Integer id;
    private String username;
    private String deviceId;
    private Byte channel;
    private Date authBegindate;
    private Date authEnddate;
    private String cameraName;
    private String cameraStatus;
    private String cameraInstallLocation;
    private String cameraMode;
}
