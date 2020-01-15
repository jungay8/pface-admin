package com.pface.admin.modules.member.service.impl;

import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.member.mapper.JmgoMemberMediaDocImgfilesMapper;
import com.pface.admin.modules.member.po.JmgoMemberMediaDocImgfiles;
import com.pface.admin.modules.member.service.MediaDocImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/7/2
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Service
public class MediaDocImgServiceImpl extends BaseService<JmgoMemberMediaDocImgfiles> implements MediaDocImgService {

    @Autowired
    private JmgoMemberMediaDocImgfilesMapper jmgoMemberMediaDocImgfilesMapper;

}
