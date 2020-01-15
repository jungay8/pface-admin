package com.pface.admin.otherdb.event;

import com.pface.admin.modules.member.po.MemberCert;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.otherdb.po.MtAuth;
import com.pface.admin.otherdb.po.UserInfo;
import com.pface.admin.otherdb.service.MtAuthService;
import com.pface.admin.otherdb.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 日志事件监听
 * @author cjbi,daniel.liu@outlook.com
 */
@Component
public class MtAuthCreateListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MtAuthService mtAuthService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    public void handleLogEvent(MemberCert cert) {
//        logger.info("Handling cert event.", cert);
//
//        MtAuth mtAuth=new MtAuth();
//        mtAuth.setUserid(cert.getUid().intValue());
//        mtAuth.setAuthtype(1);
//
//        mtAuthService.create(mtAuth);
    }

}
