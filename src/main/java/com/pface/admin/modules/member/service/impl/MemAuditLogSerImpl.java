package com.pface.admin.modules.member.service.impl;

import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.member.mapper.MemberAuditLogMapper;
import com.pface.admin.modules.member.mapper.MemberPriceLabelMapper;
import com.pface.admin.modules.member.po.MemberAuditLog;
import com.pface.admin.modules.member.po.MemberBelongLabel;
import com.pface.admin.modules.member.service.MemAuditLogService;
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
public class MemAuditLogSerImpl extends BaseService<MemberAuditLog> implements MemAuditLogService {
    @Autowired
    private MemberAuditLogMapper auditLogMapper;

    @Override
    @Transactional
    public void logicDeleteBatchByIds(Long[] ids) {

        for(int i=0;i<ids.length;i++) {
            MemberAuditLog label = new MemberAuditLog();
            label.setId(ids[i]);
            label.setDelFlag(1);
            super.updateNotNull(label);
        }
    }
}
