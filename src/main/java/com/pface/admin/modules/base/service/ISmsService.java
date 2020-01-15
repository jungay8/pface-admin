package com.pface.admin.modules.base.service;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/12
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
public interface ISmsService {

    public Object sendMsg(String mobile,String content);
}
