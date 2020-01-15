package com.pface.admin.modules.front.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@Data
public class FaceAppUsescenePojo {
    private Integer id;
    private String scenename;
    private Date startDate;
    private Date asignStarttime;
    private String theme;
    private String address;
    private String remark;
}
