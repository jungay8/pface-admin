package com.pface.admin.modules.member.event;

import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.MemberUserService;
import com.pface.admin.otherdb.po.UserInfo;
import com.pface.admin.otherdb.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 日志事件监听
 * @author cjbi,daniel.liu@outlook.com
 */
@Component
public class UserUpdateListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MemberUserService memberService;

    @EventListener
    public void handleLogEvent(UserDto userDto) {

        logger.info("UserUpdateListener Handling user event.userDto={}", userDto);
        MemberUser user=new MemberUser();
        user.setId(userDto.getId());
        user.setUserId(userDto.getUserId());

        memberService.updateNotNull(user);
    }

}
