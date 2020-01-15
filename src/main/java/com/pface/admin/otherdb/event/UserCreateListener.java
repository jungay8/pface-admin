package com.pface.admin.otherdb.event;

import com.pface.admin.modules.member.enums.MemberTypeEnum;
import com.pface.admin.modules.member.event.UserDto;
import com.pface.admin.modules.member.po.MemberCert;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.MemberCertService;
import com.pface.admin.modules.system.po.Log;
import com.pface.admin.modules.system.service.LogService;
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
public class UserCreateListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MtAuthService mtAuthService;

    @Autowired
    private MemberCertService memberCertService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;



    @EventListener
    public void handleLogEvent(MemberUser user) {
//        logger.info("Handling UserCreateListener user event.user={}", user);
//
//        MemberCert cert=new MemberCert();
//        cert.setUid(user.getId());
//        MemberCert c= memberCertService.queryOne(cert);
//
//
//        UserInfo userInfo=new UserInfo();
//        userInfo.setUserName(user.getUname());
//        userInfo.setUserLogin(user.getUname());
//        userInfo.setUserLogo(c.getIconUrl());
//        userInfo.setUserPassword(user.getPwd());
//        userInfo.setUserPhone(user.getMobile());
//        userInfo.setUserMail(user.getEmail());
//        userInfo.setUserType(1);
//        userInfo.setUserStatus(true);
//        userInfoService.save(userInfo);
//
//        MtAuth mtAuth=new MtAuth();
//        mtAuth.setUserid(userInfo.getUserId());
//
//        if(user.getMemberType()==MemberTypeEnum.COMPANY){
//            mtAuth.setAuthtype(1);
//            mtAuth.setComimage(c.getCompanyFront());
//            mtAuth.setCompany(c.getCompanyName());
//            mtAuth.setComno(c.getCompanyCode());
//
//        }
//        if(user.getMemberType()==MemberTypeEnum.PERSON){
//            mtAuth.setAuthtype(0);
//            mtAuth.setCardback(c.getPersonidBack());
//            mtAuth.setCardfront(c.getPersonidFront());
//        }
//        mtAuth.setComlogo(user.getHeadUrl());
//        mtAuth.setCheckdate(user.getOpDate());
//        mtAuth.setCheckok(1);
//        mtAuth.setUserlogin(user.getUname());
//
//        mtAuthService.create(mtAuth);
//
//        UserDto userDto=new UserDto();
//        userDto.setId(user.getId());
//        userDto.setUserId(userInfo.getUserId());
//
//        c.setUserId(userInfo.getUserId());
//        memberCertService.updateNotNull(c);
//        applicationEventPublisher.publishEvent(userDto);

    }

}
