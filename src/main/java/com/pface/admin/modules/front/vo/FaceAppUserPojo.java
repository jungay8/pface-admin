package com.pface.admin.modules.front.vo;

import lombok.Data;

@Data
public class FaceAppUserPojo {
    private Integer id;
    private String username;
    private String operator;
    private String password;
    private String status;
}
