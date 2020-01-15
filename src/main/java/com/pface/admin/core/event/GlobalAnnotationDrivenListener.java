package com.pface.admin.core.event;

import com.pface.admin.core.utils.JedisUtils;
import com.pface.admin.core.utils.SpringUtils;
import com.pface.admin.modules.base.service.impl.SmsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

/**
 * @author cjbi,daniel.liu@outlook.com
 */
@Component
public class GlobalAnnotationDrivenListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @EventListener
    public void handleApplicationStarted(ApplicationStartedEvent ase) {
        logger.info("Handling application {} started event.", ase);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>服务启动完成！");
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        JedisUtils.getResource().set("test","redis服务启动完成！");
        logger.info("打印redis操作={}",JedisUtils.getResource().get("test"));

        //SmsServiceImpl smsService=  SpringUtils.getBean(SmsServiceImpl.class);

        //logger.info("这是测试短信=",smsService.sendMsg("18874909295","这是测试短信"));


    }
}
