package com.pface.admin.modules.member.service;

import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.member.po.MemberCatalogue;
import com.pface.admin.modules.member.po.MemberCert;
import com.pface.admin.modules.system.dto.TreeDto;
import com.pface.admin.modules.system.po.Organization;

import java.util.List;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/8
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
public interface MemCatalogService extends IService<MemberCatalogue> {

    void createCatalog(MemberCatalogue catalogue);

    List<TreeDto> queryCataTree(Long pId);

    List<MemberCatalogue> queryAllWithExclude(MemberCatalogue excludeCatalog);

    void move(MemberCatalogue source, MemberCatalogue target);

    List<MemberCatalogue> queryCataData();
}
