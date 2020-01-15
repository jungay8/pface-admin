package com.pface.admin.modules.member.service;

import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.member.po.MemberPriceLabel;
import com.pface.admin.modules.member.po.MemberPublishLabel;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/8
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
public interface MemPriceLabService extends IService<MemberPriceLabel> {
    List<MemberPriceLabel> queryByIds(List<Integer> ids);

}
