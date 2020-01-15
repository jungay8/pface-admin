package com.pface.admin.modules.member.service.impl;

import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.front.vo.FaceAppUserPojo;
import com.pface.admin.modules.front.vo.FaceAppUsescenePojo;
import com.pface.admin.modules.member.po.FaceAppUsescene;
import com.pface.admin.modules.member.po.FaceCamera;
import com.pface.admin.modules.member.service.FaceAppUsesceneService;
import com.pface.admin.modules.system.po.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class FaceAppUsesceneServiceImpl extends BaseService<FaceAppUsescene> implements FaceAppUsesceneService {
    @Override
    @Transactional
    public void logicDeleteBatchByIds(Long[] ids, FaceAppUserPojo userinfo) {

        Date now = DateUtils.getNowDate();
        FaceAppUsescene faceAppUsescene = null;
        for(int i=0;i<ids.length;i++) {
            faceAppUsescene = new FaceAppUsescene();
            if (userinfo!=null){
                faceAppUsescene.setUpdateBy(userinfo.getId().toString());
            }
            faceAppUsescene.setUpdateDate(now);

            faceAppUsescene.setId(Integer.parseInt(ids[i].toString()));
            faceAppUsescene.setDelFlag("0");
            super.updateNotNull(faceAppUsescene);
        }
    }
}
