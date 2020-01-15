package com.pface.admin.modules.system.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pface.admin.modules.base.enums.IBaseEnum;
import com.pface.admin.modules.member.enums.CertTypeEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResourceType implements IBaseEnum {

    MENU("MENU","菜单"), BUTTON("BUTTON","按钮");

    private String name;
    private String text;

    ResourceType(String name, String text) {
        this.name = name;
        this.text = text;
    }

    @Override
    public String getName() {
        return this.name;
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

    public static ResourceType getEnumByName(String name){
        switch (name){
            case "MENU":return ResourceType.MENU;
            case "BUTTON":return ResourceType.BUTTON;
            default:return ResourceType.MENU;
        }
    }

    @Override
    public String toString() {
        return "ResourceType{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}