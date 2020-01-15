package com.pface.admin.modules.member.mapper;

import com.pface.admin.core.utils.MyMapper;
import com.pface.admin.modules.member.po.MemberBelongLabel;

import java.util.List;

public interface MemberBelongLabelMapper extends MyMapper<MemberBelongLabel> {

    public List<MemberBelongLabel> queryByIds(List<Integer> list);
}