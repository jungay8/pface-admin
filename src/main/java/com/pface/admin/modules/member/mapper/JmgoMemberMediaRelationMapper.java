package com.pface.admin.modules.member.mapper;

import com.pface.admin.core.utils.MyMapper;
import com.pface.admin.modules.front.vo.MemberMediaQueryParams;
import com.pface.admin.modules.front.vo.MemberMediaRelationPojo;
import com.pface.admin.modules.member.po.JmgoMemberMediaRelation;

import java.util.List;

public interface JmgoMemberMediaRelationMapper extends MyMapper<JmgoMemberMediaRelation> {
    List<MemberMediaRelationPojo> queryMemberMediaRelationList(MemberMediaQueryParams memberMediaQueryParams);
}