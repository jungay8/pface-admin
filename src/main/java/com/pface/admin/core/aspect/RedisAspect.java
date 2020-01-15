package com.pface.admin.core.aspect;

import com.pface.admin.core.exception.BizException;
import com.pface.admin.core.utils.ResultCodeEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/3
 * @描述 Redis切面处理类
 * @旁白 生命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Aspect
@Component
public class RedisAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 是否开启redis缓存  true开启   false关闭
     */
    @Value("${renren.redis.open: true}")
    private boolean open;

    @Around("execution(* com.pface.admin.core.utils.RedisUtils.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        if(open){
            try{
                result = point.proceed();
            }catch (Exception e){
                logger.error("redis error", e);
                throw new BizException(ResultCodeEnum.NOT_IMPLEMENTED).setMsg("Redis服务异常");
            }
        }
        return result;
    }

}
