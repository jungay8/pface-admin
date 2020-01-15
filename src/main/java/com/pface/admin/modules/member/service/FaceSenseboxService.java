package com.pface.admin.modules.member.service;

import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.member.dto.FaceSenseboxExDTO;
import com.pface.admin.modules.member.po.FaceSensebox;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 智能盒子业务Service
 */
public interface FaceSenseboxService extends IService<FaceSensebox> {

    /**
     * 拉取智能盒子的基础信息
     * @param faceSensebox
     */
    void pullSenseboxInfo(FaceSensebox faceSensebox);

    /**
     * 新增数据，适用于多表场景（有与主表关联的其它表）
     * @param faceSensebox
     * @return
     */
    int myCreate(FaceSensebox faceSensebox);

    /**
     * 检查设备ID的唯一性
     * @param faceSensebox
     */
    public void checkdeviceid(@Validated FaceSensebox faceSensebox);

    /**
     * 查询授权给用户的Senseboxbox
     *
     * @param s
     * @return
     */
    List<FaceSenseboxExDTO> queryAuthedSenseboxOfUser(String sysUserid);

    /**
     * 最大最小算法，算出一个Sensebox
     *
     * @return
     */
//    public FaceSensebox maxminAlgorithmByAuthdate(Integer sysUserid);
}
