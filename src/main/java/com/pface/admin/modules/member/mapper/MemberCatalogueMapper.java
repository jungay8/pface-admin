package com.pface.admin.modules.member.mapper;

import com.pface.admin.core.utils.MyMapper;
import com.pface.admin.modules.member.po.MemberCatalogue;

public interface MemberCatalogueMapper extends MyMapper<MemberCatalogue> {
    int updateSalefParentIds(String makeSelfAsParentIds);
}