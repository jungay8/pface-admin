package com.pface.admin.otherdb.service.impl;

import com.pface.admin.core.exception.BizException;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.otherdb.mapper.MtAuthMapper;
import com.pface.admin.otherdb.mapper.UserInfoMapper;
import com.pface.admin.otherdb.po.MtAuth;
import com.pface.admin.otherdb.po.UserInfo;
import com.pface.admin.otherdb.service.MtAuthService;
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
public class MtAuthSerImpl extends BaseService<MtAuth> implements MtAuthService {

    @Autowired
    private MtAuthMapper mtAuthMapper;

    @Override
    public void createAuth(MtAuth mtAuth) throws BizException {
        mtAuthMapper.insert(mtAuth);
    }
}
