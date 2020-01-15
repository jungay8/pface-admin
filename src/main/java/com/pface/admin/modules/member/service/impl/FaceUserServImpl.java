package com.pface.admin.modules.member.service.impl;

import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.member.po.FaceSensebox;
import com.pface.admin.modules.member.po.FaceUser;
import com.pface.admin.modules.member.po.MemberMediaFile;
import com.pface.admin.modules.member.service.FaceUserService;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 系统登模块Service实现
 */
@Service
public class FaceUserServImpl extends BaseService<FaceUser> implements FaceUserService {
    @Autowired
    private UserService userService;

    /**
     * 账户数据下发的应用端
     *
     * @param faceUser
     */
    @Override
    public void pushFaceUserData2app(FaceUser faceUser) {
        if (StringUtils.isEmpty(faceUser.getPort())){
            faceUser.setPort("80");
        }
        if (StringUtils.isNotEmpty(faceUser.getIp())){
            //调应用端的接口，下发账户数据 ：接口实现：有就修改，没有就新增 TODO
        }
    }

    @Override
    public List<FaceUser> queryAll() {
        FaceUser faceUser_cond = new FaceUser();
        faceUser_cond.setDelFlag("1");
        return super.queryList(faceUser_cond);
    }

    @Override
    @Transactional
    public void logicDeleteBatchByIds(Long[] ids) {

        Date now = DateUtils.getNowDate();
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.queryOne(new User().setUsername(username));

        FaceUser faceUser = null;

        for(int i=0;i<ids.length;i++) {
            faceUser = new FaceUser();
            if (user!=null && user.getLocked()!=null){
                faceUser.setUpdateBy(user.getId().toString());
            }
            faceUser.setUpdateDate(now);

            faceUser.setId(Integer.parseInt(ids[i].toString()));
            faceUser.setDelFlag("0");
            super.updateNotNull(faceUser);
        }
    }

}
