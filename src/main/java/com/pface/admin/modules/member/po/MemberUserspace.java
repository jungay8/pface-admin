package com.pface.admin.modules.member.po;

import lombok.Data;

@Data
public class MemberUserspace {
    private Long userId;
    private String userName;

    private Double videoSpace;
    private String videoSpaceName;

    private Double audioSpace;
    private String audioSpaceName;

    private Double docSpace;
    private String docSpaceName;

    private Double totalSpace;
    private String totalSpaceName;
}
