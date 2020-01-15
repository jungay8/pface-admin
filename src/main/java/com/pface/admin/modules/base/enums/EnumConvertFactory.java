package com.pface.admin.modules.base.enums;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/7/11
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */

//@Component
public class EnumConvertFactory implements ConverterFactory<String,IBaseEnum> {


    @Override
    public <T extends IBaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToIEum<>(targetType);
    }

    private static class StringToIEum<T extends IBaseEnum>  implements Converter<String, T> {

    private  Class<T> targerType;
        /**
         * Instantiates a new String to i eum.
         *
         * @param targerType the targer type
         */
    public StringToIEum(Class<T> targerType) {
            this.targerType = targerType;
        }

    @Override
    public T convert(String source) {
       if (StringUtils.isBlank(source)) {
                return null;
       }
       return (T) IBaseEnum.getIEnum(targerType,source);

      }

    }

}
