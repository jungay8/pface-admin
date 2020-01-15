package com.pface.admin.modules.member.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.member.mapper.FaceUserChannelResMapper;
import com.pface.admin.modules.member.po.FaceAppUsescene;
import com.pface.admin.modules.member.po.FaceCamera;
import com.pface.admin.modules.member.po.FaceUserChannelRes;
import com.pface.admin.modules.member.service.FaceUserChannelResService;
import com.pface.admin.modules.member.service.FaceUserService;
import com.pface.admin.modules.member.vo.FaceUserChannelResParams;
import com.pface.admin.modules.member.vo.FaceUserChannelResVo;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class FaceUserChannelResServiceImpl  extends BaseService<FaceUserChannelRes> implements FaceUserChannelResService {
    @Autowired
    FaceUserChannelResMapper faceUserChannelResMapper;
    @Autowired
    private FaceUserService faceUserService;
    @Autowired
    private UserService userService;

    @Override
    public Page<FaceUserChannelResVo> queryFaceUserChannelResList(PageQuery pageQuery, FaceUserChannelResParams params) {
        return PageHelper.startPage(pageQuery)
                .doSelectPage(() -> faceUserChannelResMapper.queryFaceUserChannelResList(params));
    }

    @Override
    @Transactional
    public void logicDeleteBatchByIds(Long[] ids) {

        Date now = DateUtils.getNowDate();
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.queryOne(new User().setUsername(username));

        FaceUserChannelRes faceUserChannelRes = null;

        for(int i=0;i<ids.length;i++) {
            faceUserChannelRes = new FaceUserChannelRes();
            if (user!=null && user.getLocked()!=null){
                faceUserChannelRes.setUpdateBy(user.getId().toString());
            }
            faceUserChannelRes.setUpdateDate(now);

            faceUserChannelRes.setId(Integer.parseInt(ids[i].toString()));
            faceUserChannelRes.setDelFlag("0");
            super.updateNotNull(faceUserChannelRes);
        }
    }

}
