package com.pface.admin.modules.system.service.impl;

import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.system.mapper.ParamMapper;
import com.pface.admin.modules.system.mapper.ResourceMapper;
import com.pface.admin.modules.system.po.Param;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.service.ParamService;
import com.pface.admin.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/14
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Service
public class ParamServiceImpl extends BaseService<Param> implements ParamService {

    @Autowired
    private ParamMapper paramsMapper;
}
