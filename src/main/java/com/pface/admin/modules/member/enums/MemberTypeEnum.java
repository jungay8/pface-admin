package com.pface.admin.modules.member.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pface.admin.modules.base.enums.IBaseEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/8
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MemberTypeEnum implements IBaseEnum {
    //会员类型 0个人，1企业
    ALL("ALL", "全部"),
    PERSON("PERSON", "个人"),
    COMPANY("COMPANY", "企业");


    private String name;
    private String text;

    MemberTypeEnum(String name, String text) {
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

   public static MemberTypeEnum getEnumByName(String name){
       name= StringUtils.trimToEmpty(name);
        if("PERSON".equalsIgnoreCase(name)){
            return MemberTypeEnum.PERSON;
        }else  if("COMPANY".equalsIgnoreCase(name)){
            return MemberTypeEnum.COMPANY;
        }else{
            return MemberTypeEnum.COMPANY;
        }
   }

    @Override
    public String toString() {
        return "MemberTypeEnum{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
