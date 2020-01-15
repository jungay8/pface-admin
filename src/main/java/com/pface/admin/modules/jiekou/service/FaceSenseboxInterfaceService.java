package com.pface.admin.modules.jiekou.service;

import com.pface.admin.modules.member.po.FaceSensebox;
import org.springframework.stereotype.Service;

/**
 * 智能盒子接口Service
 * 包括：
 * 1、登录、登出
 * 2、配置信息
 * 3、心跳监听接口
 */
public interface FaceSenseboxInterfaceService {
    /**
     * 登录以获取sessionid
     *
     * @param faceSensebox（ip,port,loginname, loginpassword 必须的4个属性）
     * @return
     */
    public String getSessionid(FaceSensebox faceSensebox);
}
