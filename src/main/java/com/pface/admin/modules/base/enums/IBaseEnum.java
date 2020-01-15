package com.pface.admin.modules.base.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/10
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@JsonDeserialize(using = BaseEnumDeserializer.class)
public interface IBaseEnum extends Serializable {

    /**
     * 调用枚举的this.name()
     * @return
     */
    String getName();

    String getText();

    public static <E extends Enum<E> & IBaseEnum> E valueOf(String enumCode,Class<E> clazz) {
        E enumm = Enum.valueOf(clazz, enumCode);
        return enumm;
    }

    public static <T extends IBaseEnum> Object getIEnum(Class<T> targerType, String source) {
        for(T enumObj : targerType.getEnumConstants()){
            if(source.equalsIgnoreCase( enumObj.getName())){
                return enumObj;
            }
        }
        return null;
    }

}
