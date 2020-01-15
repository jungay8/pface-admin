package com.pface.admin.modules.member.service;

import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.member.po.FaceUser;
import org.springframework.stereotype.Component;

/**
 * 系统登模块Service
 */
public interface FaceUserService extends IService<FaceUser> {
    void pushFaceUserData2app(FaceUser faceUser);
}
