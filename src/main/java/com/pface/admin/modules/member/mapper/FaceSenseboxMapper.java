package com.pface.admin.modules.member.mapper;

import com.pface.admin.core.utils.MyMapper;
import com.pface.admin.modules.member.dto.FaceSenseboxExDTO;
import com.pface.admin.modules.member.po.FaceSensebox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FaceSenseboxMapper extends MyMapper<FaceSensebox> {
    /**
     * 查询授权给用户的Sensebox
     *
     * @param param
     * @return
     */
    List<FaceSenseboxExDTO> queryAuthedSenseboxOfUser(Map<String, String> param);
}