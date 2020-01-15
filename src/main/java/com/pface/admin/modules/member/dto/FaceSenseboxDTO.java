package com.pface.admin.modules.member.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FaceSenseboxDTO implements Serializable {
    private static final long serialVersionUID = -2172266497125536890L;
    private Integer id;
    private String ip;
    private String port;
    private String deviceId;
    private Date minAuthBegindate;
    private Date maxAuthEnddate;
}
