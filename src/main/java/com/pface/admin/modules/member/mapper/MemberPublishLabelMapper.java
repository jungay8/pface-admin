package com.pface.admin.modules.member.mapper;

import com.pface.admin.core.utils.MyMapper;
import com.pface.admin.modules.member.po.MemberBelongLabel;
import com.pface.admin.modules.member.po.MemberPublishLabel;

import java.util.List;

public interface MemberPublishLabelMapper extends MyMapper<MemberPublishLabel> {
    public List<MemberPublishLabel> queryByIds(List<Integer> list);
}