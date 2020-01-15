package com.pface.admin.modules.member.service.impl;

import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.member.mapper.JmgoFilePathMapper;
import com.pface.admin.modules.member.po.JmgoFilePath;
import com.pface.admin.modules.member.service.JmgoFilePathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class JmgoFilePathSerImpl extends BaseService<JmgoFilePath> implements JmgoFilePathService {

    @Autowired
    private JmgoFilePathMapper jmgoFilePathMapper;

    @Override
    public JmgoFilePath getCurrentFP(){
        JmgoFilePath cond  = new JmgoFilePath();
        cond.setUseflag(1);
        List<JmgoFilePath> list =jmgoFilePathMapper.select(cond);

        if (!list.isEmpty()){
            return list.get(0);
        }

        return null;
    }
}
