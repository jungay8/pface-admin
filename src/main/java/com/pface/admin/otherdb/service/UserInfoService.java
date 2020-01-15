package com.pface.admin.otherdb.service;

import com.pface.admin.core.exception.BizException;
import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.member.po.MemberPublishLabel;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.otherdb.po.UserInfo;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/18
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
public interface UserInfoService extends IService<UserInfo> {
    /**
     * 创建用户
     * @param user
     */
    void createUser(UserInfo user) throws BizException;


    /**
     * 创建用户
     * @param user
     */
    Integer save(UserInfo user) throws BizException;
}
