package com.pface.admin.modules.base.service.impl;

import java.io.UnsupportedEncodingException;

public class Test {

    public static void main(String[] args) {
//        String fileName = "9080909.mp4";
//        String newStdFilename = fileName.substring(0, fileName.indexOf("."))+"_std." + fileName.substring(fileName.indexOf(".") + 1 , fileName.length());
//
//        String ext = fileName.substring(fileName.indexOf(".") + 1, fileName.length());
//        System.out.printf(ext);
//        System.out.printf(newStdFilename);

        String s = "彭1d";
        System.out.println("b:"+Byte.MAX_VALUE);
            byte[] bytes = s.getBytes();
            for (byte b: bytes){
                System.out.println("b:"+b);
            }
//        b:-27
//        b:-67
//        b:-83
//        b:-27
//        b:-97
//        b:-114
//        b:-27
//        b:-122
//        b:-101
    }
}
