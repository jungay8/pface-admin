package com.pface.admin.modules.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.system.mapper.GroupMapper;
import com.pface.admin.modules.system.po.Group;
import com.pface.admin.modules.system.service.GroupService;

@Service
public class GroupServiceImpl extends BaseService<Group> implements GroupService {

    @Autowired
    private GroupMapper groupMapper;
}
