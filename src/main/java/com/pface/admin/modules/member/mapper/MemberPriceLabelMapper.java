package com.pface.admin.modules.member.mapper;

import com.pface.admin.core.utils.MyMapper;
import com.pface.admin.modules.member.po.MemberBelongLabel;
import com.pface.admin.modules.member.po.MemberPriceLabel;

import java.util.List;

public interface MemberPriceLabelMapper extends MyMapper<MemberPriceLabel> {
    public List<MemberPriceLabel> queryByIds(List<Integer> list);
}