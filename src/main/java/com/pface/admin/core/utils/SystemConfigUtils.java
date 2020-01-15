package com.pface.admin.core.utils;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/28
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Configuration
public class SystemConfigUtils implements EnvironmentAware {


   private static Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
       environment=environment;
    }

  /*
    private static SystemConfigUtils instance;
    private SystemConfigUtils() {}

    public static SystemConfigUtils getInstance() {
        if (instance == null) {
            synchronized (SystemConfigUtils.class) {
                if (instance == null) {
                    instance = new SystemConfigUtils();
                }
            }
        }
        return instance;
    }*/

    public  static String getVal(String key){
        return environment.getProperty(key);
    }

    public  static Integer getIntVal(String key){
        return Integer.valueOf(environment.getProperty(key));
    }

    public  static boolean getBooleanVal(String key){
        return Boolean.valueOf(environment.getProperty(key));
    }

}
