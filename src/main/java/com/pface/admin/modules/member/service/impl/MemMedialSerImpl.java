package com.pface.admin.modules.member.service.impl;

import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.member.mapper.MemberMediaMapper;
import com.pface.admin.modules.member.mapper.MemberPriceLabelMapper;
import com.pface.admin.modules.member.po.MemberMedia;
import com.pface.admin.modules.member.po.MemberMediaFile;
import com.pface.admin.modules.member.po.MemberPublishLabel;
import com.pface.admin.modules.member.service.MemMediaFileService;
import com.pface.admin.modules.member.service.MemberMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/8
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Service
public class MemMedialSerImpl extends BaseService<MemberMedia> implements MemberMediaService {

    @Autowired
    private MemberMediaMapper mediaMapper;


        @Override
        @Transactional
        public void logicDeleteBatchByIds(Long[] ids) {
            for(int i=0;i<ids.length;i++) {
                MemberMedia label = new MemberMedia();
                label.setId(ids[i]);
                label.setDelFlag(1);
                super.updateNotNull(label);
            }
        }
}
