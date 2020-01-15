package com.pface.admin.modules.base.bean;

import com.pface.admin.modules.member.po.JmgoFilePath;
import com.pface.admin.modules.member.service.JmgoFilePathService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/7/6
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Component
public class ImageFileBean {

    @Autowired
    private JmgoFilePathService jmgoFilePathService;

    @Value("${jmgo.pubfile}")
    private String jmgoPubfile;

    @Value("${face.uploaddir}")
    private String uploaddir;
//
    public String getImageFileUrl(String fileFullPath){

        String url="";
        try {

            url="/image?url="+URLEncoder.encode(StringUtils.trimToEmpty(fileFullPath),"UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }
        return url;
    }

    public String getPublicFileUrl(String fileFullPath){

        String url="/pubfile/"+fileFullPath.substring(getPublicPath().length());

        return url;
    }

//    public String getFacePublicFileUrl(String fileFullPath){
//
//        String url="/pubfile/"+fileFullPath.substring(getFacePublicPath().length());
//
//        return url;
//    }
//
//    public String getFacePublicPath(){
//        String url=uploaddir;
//        return url;
//    }

    public String getPublicPath(){
        String url = uploaddir;
//        String url=jmgoPubfile;
//        JmgoFilePath jmgoFilePath=jmgoFilePathService.getCurrentFP();
//        if(jmgoFilePath!=null && StringUtils.isNotBlank(jmgoFilePath.getVirtualpath())){
//            url=jmgoFilePath.getVirtualpath();
//        }
        return url;
    }
}
