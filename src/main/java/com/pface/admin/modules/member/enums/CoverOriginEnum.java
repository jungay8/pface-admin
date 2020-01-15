package com.pface.admin.modules.member.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pface.admin.modules.base.enums.IBaseEnum;
import org.apache.commons.lang3.StringUtils;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CoverOriginEnum implements IBaseEnum {

//    VIDEO_CAPTURE("videoCapture","视频截图"), MANUAL_IMAGE("manualImage","手动图片");
    VIDEO_CAPTURE("1","视频截图"), MANUAL_IMAGE("0","手动图片");


    private String name;
    private String text;

    CoverOriginEnum(String name, String text) {
        this.name = name;
        this.text = text;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static CoverOriginEnum getEnumByName(String name){
        name= StringUtils.trimToEmpty(name).toUpperCase();
        switch (name){
            case "1":return CoverOriginEnum.VIDEO_CAPTURE;
            case "0":return CoverOriginEnum.MANUAL_IMAGE;
            case "VIDEO_CAPTURE":return CoverOriginEnum.VIDEO_CAPTURE;
            case "MANUAL_IMAGE":return CoverOriginEnum.MANUAL_IMAGE;
            case "VIDEOCAPTURE":return CoverOriginEnum.VIDEO_CAPTURE;
            case "MANUALIMAGE":return CoverOriginEnum.MANUAL_IMAGE;
            default:return CoverOriginEnum.VIDEO_CAPTURE;
        }
    }

    @Override
    public String toString() {
        return "CoverOriginEnum{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}