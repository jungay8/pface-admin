package com.pface.admin.modules.member.service.impl;

import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.member.dto.DiskInfoDTO;
import com.pface.admin.modules.member.mapper.MemberMediaFileMapper;
import com.pface.admin.modules.member.po.MemberCatalogue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/24
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Service
public class MemDiskServiceImpl {

    @Autowired
    private MemberMediaFileMapper mediaFileMapper;


    List<DiskInfoDTO> getDiskByCatalog(Long catalogId){
        return mediaFileMapper.getDiskByCatalog(catalogId);
    }

    List<DiskInfoDTO>  getDiskByUid(Long uid){
        return mediaFileMapper.getDiskByUid(uid);
    }

}
