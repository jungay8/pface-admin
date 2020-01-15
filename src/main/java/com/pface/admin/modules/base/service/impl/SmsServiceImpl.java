package com.pface.admin.modules.base.service.impl;

import com.pface.admin.core.utils.OKHttpUtil;
import com.pface.admin.modules.base.service.ISmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/12
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */

@Service
@ConfigurationProperties(prefix = "jmgosms")
public class SmsServiceImpl implements ISmsService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${jmgosms.url}")
    private String url;

    @Value("${jmgosms.appid}")
    private String appid;

    @Value("${jmgosms.appkey}")
    private String appkey;

    @Value("${jmgosms.signature}")
    private String signature;


    @Override
    public Object sendMsg(String mobile, String content) {

        logger.info("url?appid=&appkey=&signature=",url,appid,appkey,signature);
        logger.info("content=",content);
        Map<String, String> para =new HashMap<String,String>();
        para.put("appid",appid);
        para.put("signature",appkey);
        para.put("to",mobile);
        para.put("content",signature+content);
        /*
        *{
           "status":"success"
           "send_id":"093c0a7df143c087d6cba9cdf0cf3738"
           "fee":1,
           "sms_credits":14197
        *}
        * */
        return  OKHttpUtil.httpsPost(url,para);
    }


}
