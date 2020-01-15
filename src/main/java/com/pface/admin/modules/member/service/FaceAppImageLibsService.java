package com.pface.admin.modules.member.service;

import com.alibaba.fastjson.JSONArray;
import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.member.dto.FaceSenseboxDTO;
import com.pface.admin.modules.member.po.FaceAppImageLibs;

import java.util.List;

public interface FaceAppImageLibsService extends IService<FaceAppImageLibs> {
    /**
     * 获取授权给客户的所有远程智能设备
     *
     * @param sysUserid
     * @param faceserverip
     * @param faceserverport
     * @return
     */
    List<FaceSenseboxDTO> remoteQuerySensebox(Integer sysUserid, String faceserverip, String faceserverport);

    /**
     * 远程创建人像库
     *
     * @param sysDeviceId
     * @param editlibname
     * @param editlibtype
     * @return
     */
    String remoteCreateImageLib(String faceserverip, String faceserverport, Integer sysDeviceId, String editlibname, Integer editlibtype);

    /**
     * 远程确认人像库是否存在
     *
     * @param deviceId
     * @return
     */
    Boolean remoteEnsureImageLibExists(String faceserverip, String faceserverport, String deviceId, Byte libId);

    /**
     * 远程修改人像库
     *
     * @param faceserverip
     * @param faceserverport
     * @param editlibId
     * @param editlibname
     * @param editlibtype
     * @return
     */
    boolean remoteUpdateImageLib(String faceserverip, String faceserverport,  String deviceId, Byte editlibId, String editlibname, Integer editlibtype);

    /**
     * 远程删除人像库
     * @param faceserverip
     * @param faceserverport
     * @param deviceId
     * @param editlibId
     * @return
     */
    boolean remoteDeleteImageLib(String faceserverip, String faceserverport,  String deviceId, Byte editlibId);

    /**
     * 远程刷新人像库，并保存到本地
     * @param deviceId
     * @param libId
     * @return
     */
    JSONArray remoteQueryImageLibs(String faceserverip, String faceserverport,  String deviceId, Byte libId);
}
