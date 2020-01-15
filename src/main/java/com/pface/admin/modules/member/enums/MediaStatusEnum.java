package com.pface.admin.modules.member.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pface.admin.modules.base.enums.IBaseEnum;
import org.apache.commons.lang3.StringUtils;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MediaStatusEnum implements IBaseEnum {

    NO("NO","否"), YES("YES","是");


    private String name;
    private String text;

    MediaStatusEnum(String name, String text) {
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

    public static MediaStatusEnum getEnumByName(String name){
        name= StringUtils.trimToEmpty(name).toUpperCase();
        switch (name){
            case "NO":return MediaStatusEnum.NO;
            case "YES":return MediaStatusEnum.YES;
            default:return MediaStatusEnum.NO;
        }
    }

    @Override
    public String toString() {
        return "MediaStatusEnum{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}