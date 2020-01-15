package com.pface.admin.common;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by wenwen on 2017/4/16.
 * version 1.0
 */
@Data
public class MultipartFileParam {

    // 用户id
    private String uid;
    //任务ID
    private String id;
    //总分片数量
    private int chunks;
    //当前为第几块分片
    private int chunk;
    //当前分片大小
    private long size = 0L;
    //文件名
    private String name;
    //分片对象
    private MultipartFile file;
    // MD5
    private String md5;

    //一个文件完成传完的标示，初始-1
    private Boolean uploaded;

    @Override
    public String toString() {
        return "MultipartFileParam{" +
                "uid='" + uid + '\'' +
                ", id='" + id + '\'' +
                ", chunks=" + chunks +
                ", chunk=" + chunk +
                ", size=" + size +
                ", name='" + name + '\'' +
                ", file=" + file +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
