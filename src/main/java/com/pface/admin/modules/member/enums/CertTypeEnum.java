package com.pface.admin.modules.member.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pface.admin.modules.base.enums.IBaseEnum;
import org.apache.commons.lang3.StringUtils;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CertTypeEnum implements IBaseEnum {

    ALL("ALL", "全部"),UNCERTIFIED("UNCERTIFIED","未认证"), CERTIFING("CERTIFING","认证中"), CERTFAIL("CERTFAIL","认证失败"), CERTIFIED("CERTIFIED","认证通过");


    private String name;
    private String text;

    CertTypeEnum(String name, String text) {
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

    public static CertTypeEnum getEnumByName(String name){
        name= StringUtils.trimToEmpty(name).toUpperCase();
        switch (name){
            case "UNCERTIFIED":return CertTypeEnum.UNCERTIFIED;
            case "CERTIFIED":return CertTypeEnum.CERTIFIED;
            case "CERTFAIL":return CertTypeEnum.CERTFAIL;
            case "CERTIFING":return CertTypeEnum.CERTIFING;
            default:return CertTypeEnum.UNCERTIFIED;
        }
    }

    @Override
    public String toString() {
        return "CertTypeEnum{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}