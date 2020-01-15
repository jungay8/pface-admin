package com.pface.admin.modules.front.task;

import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.modules.base.service.StorageService;
import com.pface.admin.modules.member.service.FaceAppSnapListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CheckMediaUploadData {
    @Autowired
    private StorageService storageService;

    @Autowired
    private FaceAppSnapListService faceAppSnapListService;
    /** 定时任务参数如下：
     * 1. 秒（0~59）
     * 2. 分钟（0~59）
     * 3. 小时（0~23）
     * 4. 天（0~31）
     * 5. 月（0~11）
     * 6. 星期（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT）
     * 7.年份（1970－2099）
     * 其中每个元素可以是一个值(如6),一个连续区间(9-12),一个间隔时间(8-18/4)(/表示每隔4小时),一个列表(1,3,5),通配符。
     * 由于"月份中的日期"和"星期中的日期"这两个元素互斥的,必须要对其中一个设置?.
     * **/
    @Scheduled(cron="0 */2 * * * ?")
    public void checkChangeCodeFileTimer(){

//        log.info("定时器执行时间："+ DateUtils.getNowDate());
//        storageService.checkChangeCodeFileTimer();
    }

    @Scheduled(cron="0 */5 * * * ?")
    public void pullFaceSnapListTimer(){
        log.info("定时器执行时间："+ DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        faceAppSnapListService.pullSnapList();
    }
}
