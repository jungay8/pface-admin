package com.pface.admin.modules.member.mapper;

import com.pface.admin.core.utils.MyMapper;
import com.pface.admin.modules.front.vo.MemberMediaPojo;
import com.pface.admin.modules.front.vo.MemberMediaQueryParams;
import com.pface.admin.modules.front.vo.MemberUserspaceAssemblyParams;
import com.pface.admin.modules.member.dto.DiskInfoDTO;
import com.pface.admin.modules.member.po.MemberMediaFile;
import com.pface.admin.modules.member.po.MemberUserspace;

import java.util.List;

public interface MemberMediaFileMapper extends MyMapper<MemberMediaFile> {

    List<DiskInfoDTO>  getDiskByCatalog(Long catalogId);

    List<DiskInfoDTO>  getDiskByUid(Long uid);

    int insert(MemberMediaFile memberMediaFile);

    /**
     * 媒体内容查询
     *
     * @return
     */
    List<MemberMediaPojo> queryMemberMediaList(MemberMediaQueryParams params);

    List<MemberUserspace> assembleUserspace(MemberUserspace memberUserspace);
}