package com.pface.admin.common;

import com.pface.admin.core.utils.SystemConfigUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/28
 * @描述
 * @旁白 生命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
public class Global {

    /**
     * 上传文件基础虚拟路径
     */
    public static final String USERFILES_BASE_URL = "userfiles";

    /**
     * 上传图片基础虚拟路径
     */
    public static final String IMAGE_BASE_URL = "IMAGETEXT";



    /**
     * 获取上传文件的根目录
     *
     * @return
     */
//    public static String getUserfilesBaseDir() {
//        String dir = SystemConfigUtils.getVal("pface.upload.dir");
//        if (StringUtils.isBlank(dir)) {
//            try {
//                dir = System.getProperty("user.home");
//            } catch (Exception e) {
//                return "";
//            }
//        }
//        if (!dir.endsWith("/")) {
//            dir += "/";
//        }
//        // System.out.println("userfiles.basedir: " + dir);
//        return dir;
//    }

}
