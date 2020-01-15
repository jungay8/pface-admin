package com.pface.admin.modules.member.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pface.admin.modules.base.enums.IBaseEnum;
import org.apache.commons.lang3.StringUtils;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MediaTypeEnum implements IBaseEnum {

    VIDEO("VIDEO","视频"), AUDIO("AUDIO","音频"), IMAGETEXT("IMAGETEXT","图文");


    private String name;
    private String text;

    MediaTypeEnum(String name, String text) {
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

    public static MediaTypeEnum getEnumByName(String name){
        name= StringUtils.trimToEmpty(name).toUpperCase();
        switch (name){
            case "VIDEO":return MediaTypeEnum.VIDEO;
            case "AUDIO":return MediaTypeEnum.AUDIO;
            case "IMAGETEXT":return MediaTypeEnum.IMAGETEXT;
            case "IMAGE_TEXT":return MediaTypeEnum.IMAGETEXT;
            default:return MediaTypeEnum.VIDEO;
        }
    }

    @Override
    public String toString() {
        return "MediaTypeEnum{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}