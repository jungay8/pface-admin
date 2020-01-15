package com.pface.admin.modules.member.service;

import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.member.po.MemberBelongLabel;
import com.pface.admin.modules.member.po.MemberCatalogue;

import java.util.List;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/8
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
public interface MemBelongLabService extends IService<MemberBelongLabel> {

    List<MemberBelongLabel> queryByIds(List<Integer> ids);
}
