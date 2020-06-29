package com.pface.admin.modules.base.service.impl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
//        String fileName = "9080909.mp4";
//        String newStdFilename = fileName.substring(0, fileName.indexOf("."))+"_std." + fileName.substring(fileName.indexOf(".") + 1 , fileName.length());
//
//        String ext = fileName.substring(fileName.indexOf(".") + 1, fileName.length());
//        System.out.printf(ext);
//        System.out.printf(newStdFilename);

//        String s = "彭1d";
//        System.out.println("b:"+Byte.MAX_VALUE);
//            byte[] bytes = s.getBytes();
//            for (byte b: bytes){
//                System.out.println("b:"+b);
//            }
//        b:-27
//        b:-67
//        b:-83
//        b:-27
//        b:-97
//        b:-114
//        b:-27
//        b:-122
//        b:-101

        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("127.0.0.1");
        System.out.println("连接成功");
        //查看服务是否运行
        jedis.auth("test");
        System.out.println("服务正在运行: "+jedis.ping());

        //哨兵
        Jedis sentinelJedis = new Jedis("127.0.0.1", 26381);
        List<String> masters = sentinelJedis.sentinelGetMasterAddrByName("mymaster");
        List<Map<String, String>> list = sentinelJedis.sentinelMasters();
        for (String s : masters){
            System.out.println(s);
        }

        for (Map<String, String> map : list){
            for (Map.Entry me:map.entrySet()){
                System.out.println("key:" + me.getKey()+",value:" + me.getValue());
            }
        }
    }
}
