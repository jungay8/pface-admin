package com.pface.admin.modules.member.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pface.admin.modules.base.enums.IBaseEnum;
import org.apache.commons.lang3.StringUtils;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GoodsStatusEnum implements IBaseEnum {

    ON("ON","已上架"), OFF("OFF","已下架");


    private String name;
    private String text;

    GoodsStatusEnum(String name, String text) {
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

    public static GoodsStatusEnum getEnumByName(String name){
        name= StringUtils.trimToEmpty(name).toUpperCase();
        switch (name){
            case "ON":return GoodsStatusEnum.ON;
            case "OFF":return GoodsStatusEnum.OFF;
            default:return GoodsStatusEnum.OFF;
        }
    }

    @Override
    public String toString() {
        return "GoodsStatusEnum{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}