package com.pface.admin.modules.member.service.impl;

import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.member.mapper.MemberPublishLabelMapper;
import com.pface.admin.modules.member.mapper.MemberUserMapper;
import com.pface.admin.modules.member.po.MemberPriceLabel;
import com.pface.admin.modules.member.po.MemberPublishLabel;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.MemPubLabelService;
import com.pface.admin.modules.member.service.MemberUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/8
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Service
public class MemPubLabelSerImpl extends BaseService<MemberPublishLabel> implements MemPubLabelService {

    @Autowired
    private MemberPublishLabelMapper publishLabelMapper;

    @Override
    public List<MemberPublishLabel> queryByIds(List<Integer> ids) {
        return publishLabelMapper.queryByIds(ids);
    }

    @Override
    @Transactional
    public void logicDeleteBatchByIds(Long[] ids) {

        for(int i=0;i<ids.length;i++) {
            MemberPublishLabel label = new MemberPublishLabel();
            label.setId(ids[i]);
            label.setDelFlag(1);
            super.updateNotNull(label);
        }
    }
}
