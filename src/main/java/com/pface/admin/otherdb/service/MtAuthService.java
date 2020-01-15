package com.pface.admin.otherdb.service;

import com.pface.admin.core.exception.BizException;
import com.pface.admin.modules.base.service.IService;
import com.pface.admin.otherdb.po.MtAuth;
import com.pface.admin.otherdb.po.UserInfo;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/18
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
public interface MtAuthService extends IService<MtAuth> {
    /**
     * 创建用户
     * @param user
     */
    void createAuth(MtAuth mtAuth) throws BizException;
}
