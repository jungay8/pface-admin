package com.pface.admin.modules.member.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pface.admin.modules.base.enums.IBaseEnum;
import org.apache.commons.lang3.StringUtils;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AuditStatusEnum implements IBaseEnum {

    ALL("ALL","全部"),AUDITING("AUDITING","审核中"), AUDITED("AUDITED","审核通过"), UNAUDITED("UNAUDITED","审核不通过");


    private String name;
    private String text;

    AuditStatusEnum(String name, String text) {
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

    public static AuditStatusEnum getEnumByName(String name){
        name= StringUtils.trimToEmpty(name).toUpperCase();
        switch (name){
            case "AUDITING":return AuditStatusEnum.AUDITING;
            case "AUDITED":return AuditStatusEnum.AUDITED;
            case "UNAUDITED":return AuditStatusEnum.UNAUDITED;
            default:return AuditStatusEnum.AUDITED;
        }
    }

    @Override
    public String toString() {
        return "AuditStatusEnum{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}