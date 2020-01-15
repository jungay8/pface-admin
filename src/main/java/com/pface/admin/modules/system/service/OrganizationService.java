package com.pface.admin.modules.system.service;

import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.system.dto.TreeDto;
import com.pface.admin.modules.system.po.Organization;

import java.util.List;

/**
 * @author cjbi,daniel.liu
 */
public interface OrganizationService extends IService<Organization> {

    void createOrganization(Organization organization);

    List<TreeDto> queryOrgTree(Long pId);

    List<Organization> queryAllWithExclude(Organization excludeOraganization);

    void move(Organization source, Organization target);
}
