package com.pface.admin.modules.member.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.xml.soap.SAAJResult;
import java.util.Date;

@Data
public class FaceDeviceChannelResVo {
    private Integer id;
    private String deviceId;
    private Byte channel;
    private String issetup;
    private String cameraName;
    private String cameraStatus;
    private String cameraInstallLocation;
    private String authedUsernames;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date canAuthStartdate;
}
