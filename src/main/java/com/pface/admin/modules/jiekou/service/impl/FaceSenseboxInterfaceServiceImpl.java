package com.pface.admin.modules.jiekou.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.pface.admin.core.exception.BizException;
import com.pface.admin.core.utils.RedisUtils;
import com.pface.admin.core.utils.ResultCodeEnum;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.jiekou.Constants.Faceconstant;
import com.pface.admin.modules.jiekou.service.FaceSenseboxInterfaceService;
import com.pface.admin.modules.jiekou.utils.BizUtils;
import com.pface.admin.modules.jiekou.utils.HttpClientHelper;
import com.pface.admin.modules.jiekou.utils.HttpException;
import com.pface.admin.modules.member.po.FaceSensebox;
import com.pface.admin.modules.member.service.FaceSenseboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 智能盒子接口Service实现
 * 包括：
 * 1、登录、登出
 * 2、配置信息
 * 3、心跳监听接口
 */

@Slf4j
@Service
public class FaceSenseboxInterfaceServiceImpl implements FaceSenseboxInterfaceService {

    @Override
    public String getSessionid(FaceSensebox faceSensebox) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("msg_id", "257");
            params.put("user_name", faceSensebox.getLoginname());
            params.put("user_pwd", faceSensebox.getLoginpassword());
            String url = BizUtils.getAccessSenseboxUrl(faceSensebox.getIp(), faceSensebox.getPort());
            if (url != null) {
                String response = HttpClientHelper.postJson(url, JSON.toJSONString(params), "");
                response = BizUtils.convertResponse(response);
                JSONObject obj = JSON.parseObject(response);
                String sessionId = obj.getString("data");
                return sessionId;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(ResultCodeEnum.SESENSE_BOX_COLLECTION_ERROR);
//            return null;
        }
        return  null;
    }
}
