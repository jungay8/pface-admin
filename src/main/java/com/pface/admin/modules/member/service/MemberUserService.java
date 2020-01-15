package com.pface.admin.modules.member.service;

import com.pface.admin.core.exception.BizException;
import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.vo.AuditLogVo;
import com.pface.admin.modules.system.po.User;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/8
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
public interface MemberUserService extends IService<MemberUser> {

    /**
     * 创建用户
     * @param user
     */
    void createUser(MemberUser user) throws BizException;

    /**
     * 修改密码
     * @param userId
     * @param newPassword
     */
    void changePassword(Long userId, String newPassword);


    boolean auditNoPass(AuditLogVo auditLogVo);


    boolean auditPass(AuditLogVo auditLogVo);

}
