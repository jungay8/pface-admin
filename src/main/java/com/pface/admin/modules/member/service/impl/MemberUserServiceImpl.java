package com.pface.admin.modules.member.service.impl;

import com.pface.admin.core.exception.BizException;
import com.pface.admin.core.utils.ResultCodeEnum;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.member.enums.AuditStatusEnum;
import com.pface.admin.modules.member.enums.CertTypeEnum;
import com.pface.admin.modules.member.mapper.MemberAuditLogMapper;
import com.pface.admin.modules.member.mapper.MemberUserMapper;
import com.pface.admin.modules.member.po.MemberAuditLog;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.MemberUserService;
import com.pface.admin.modules.member.vo.AuditLogVo;
import com.pface.admin.modules.system.service.PasswordHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/8
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Service
public class MemberUserServiceImpl extends BaseService<MemberUser> implements MemberUserService {

    @Autowired
    private MemberUserMapper memberUserMapper;

    @Autowired
    private PasswordHelper passwordHelper;

    @Autowired
    private MemberAuditLogMapper memberAuditLogMapper;

    @Override
    @Transactional
    public void createUser(MemberUser user) {
        MemberUser u = memberUserMapper.selectOne(new MemberUser().setUname(user.getUname()));
        if (u != null) {
            throw new BizException(ResultCodeEnum.FAILED_USER_ALREADY_EXIST);
        }
        // 加密密码
         passwordHelper.encryptPassword(user);
        memberUserMapper.insertSelective(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String newPassword) {
        MemberUser user = memberUserMapper.selectByPrimaryKey(userId);
        user.setPwd(newPassword);
        passwordHelper.encryptPassword(user);
        memberUserMapper.updateByPrimaryKey(user);
    }

    @Override
    @Transactional
    public boolean auditNoPass(AuditLogVo auditLogVo) {

      //用户表更新
        MemberUser memberUser=new MemberUser();
        memberUser.setId(auditLogVo.getBelongUid());
        memberUser.setIsCert(CertTypeEnum.CERTFAIL);
        memberUser.setOpDate(new Date());
        super.updateNotNull(memberUser);

        //更新审核记录表
        MemberAuditLog memberAuditLog=new MemberAuditLog();
        memberAuditLog.setAuditStatus(AuditStatusEnum.UNAUDITED);
        memberAuditLog.setBelongUid(auditLogVo.getBelongUid());
        memberAuditLog.setMsgOriginId(auditLogVo.getMsgOriginId());
        memberAuditLog.setAuditDate(new Date());
        memberAuditLog.setDelFlag(0);
        memberAuditLog.setAuditMsg(auditLogVo.getAuditMsg());
        memberAuditLog.setAuditUid(auditLogVo.getAuditUid());

       int row= memberAuditLogMapper.insert(memberAuditLog);

        return row>0;
    }


    @Override
    @Transactional
    public boolean auditPass(AuditLogVo auditLogVo) {

        //用户表更新

        MemberUser memberUser=new MemberUser();
        memberUser.setId(auditLogVo.getBelongUid());
        memberUser.setIsCert(CertTypeEnum.CERTIFIED);
        memberUser.setOpDate(new Date());
        super.updateNotNull(memberUser);

        //更新审核记录表
        MemberAuditLog memberAuditLog=new MemberAuditLog();
        memberAuditLog.setAuditStatus(AuditStatusEnum.AUDITED);
        memberAuditLog.setBelongUid(auditLogVo.getBelongUid());
        memberAuditLog.setMsgOriginId(auditLogVo.getMsgOriginId());
        memberAuditLog.setAuditDate(new Date());
        memberAuditLog.setDelFlag(0);
        memberAuditLog.setAuditMsg(auditLogVo.getAuditMsg());
        memberAuditLog.setAuditUid(auditLogVo.getAuditUid());

        int row= memberAuditLogMapper.insert(memberAuditLog);

        return row>0;
    }


    @Override
    @Transactional
    public void logicDeleteBatchByIds(Long[] ids) {
        for(int i=0;i<ids.length;i++) {
            MemberUser label = new MemberUser();
            label.setId(ids[i]);
            label.setDelFlag(1);
            super.updateNotNull(label);
        }
    }
}
