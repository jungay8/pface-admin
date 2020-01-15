package com.pface.admin.modules.member.service.impl;

import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.member.mapper.MemberPriceLabelMapper;
import com.pface.admin.modules.member.mapper.MemberUserMapper;
import com.pface.admin.modules.member.po.MemberPriceLabel;
import com.pface.admin.modules.member.po.MemberPublishLabel;
import com.pface.admin.modules.member.service.MemPriceLabService;
import com.pface.admin.modules.member.service.MemPubLabelService;
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
public class MemPriceLabSerImpl extends BaseService<MemberPriceLabel> implements MemPriceLabService {

    @Autowired
    private MemberPriceLabelMapper priceLabelMapper;

    @Override
    public List<MemberPriceLabel> queryByIds(List<Integer> ids) {
        return priceLabelMapper.queryByIds(ids);
    }

    @Override
    @Transactional
    public void logicDeleteBatchByIds(Long[] ids) {

        for(int i=0;i<ids.length;i++) {
            MemberPriceLabel priceLabel = new MemberPriceLabel();
            priceLabel.setId(ids[i]);
            priceLabel.setDelFlag(1);
            super.updateNotNull(priceLabel);
        }
    }
}
