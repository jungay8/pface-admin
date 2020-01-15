package com.pface.admin.otherdb.service.impl;

import com.pface.admin.core.exception.BizException;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.member.mapper.MemberPublishLabelMapper;
import com.pface.admin.modules.member.po.MemberPublishLabel;
import com.pface.admin.modules.member.service.MemPubLabelService;
import com.pface.admin.otherdb.mapper.UserInfoMapper;
import com.pface.admin.otherdb.po.UserInfo;
import com.pface.admin.otherdb.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/18
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Service
public class UserInfoSerImpl extends BaseService<UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public void createUser(UserInfo user) throws BizException {
        userInfoMapper.insert(user);
    }

    @Override
    public Integer save(UserInfo user) throws BizException {
        userInfoMapper.save(user);
        return user.getUserId();
    }
}
