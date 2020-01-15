package com.pface.admin.modules.system.mapper;

import com.pface.admin.core.utils.MyMapper;
import com.pface.admin.modules.system.po.Organization;

public interface OrganizationMapper extends MyMapper<Organization> {

    int updateSalefParentIds(String makeSelfAsParentIds);

}