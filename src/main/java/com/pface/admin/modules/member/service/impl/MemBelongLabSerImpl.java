package com.pface.admin.modules.member.service.impl;

import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.member.mapper.MemberBelongLabelMapper;
import com.pface.admin.modules.member.mapper.MemberUserMapper;
import com.pface.admin.modules.member.po.MemberBelongLabel;
import com.pface.admin.modules.member.po.MemberCatalogue;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.MemBelongLabService;
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
public class MemBelongLabSerImpl extends BaseService<MemberBelongLabel> implements MemBelongLabService {

    @Autowired
    private MemberBelongLabelMapper belongLabelMapper;

    @Override
    public List<MemberBelongLabel> queryByIds(List<Integer> ids) {
        return belongLabelMapper.queryByIds(ids);
    }

    @Override
    @Transactional
    public void logicDeleteBatchByIds(Long[] ids) {

        for(int i=0;i<ids.length;i++) {
            MemberBelongLabel label = new MemberBelongLabel();
            label.setId(ids[i]);
            label.setDelFlag(1);
            super.updateNotNull(label);
            //belongLabelMapper.updateByPrimaryKeySelective(label);
        }
    }
}
