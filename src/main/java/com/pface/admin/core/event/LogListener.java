package com.pface.admin.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.pface.admin.modules.system.po.Log;
import com.pface.admin.modules.system.service.LogService;

/**
 * 日志事件监听
 * @author cjbi,daniel.liu@outlook.com
 */
@Component
public class LogListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogService logService;

    @EventListener
    public void handleLogEvent(Log log) {
        logger.info("Handling log event.", log);
        logService.create(log);
    }

}
