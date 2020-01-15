package com.pface.admin.modules.member.mapper;

import com.pface.admin.core.utils.MyMapper;
import com.pface.admin.modules.member.po.MemberCert;

public interface MemberCertMapper extends MyMapper<MemberCert> {

    MemberCert getByUid(String uid);

}