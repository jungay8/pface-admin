package com.pface.admin.modules.base.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.BeanUtils;

import java.io.IOException;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/10
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
public class BaseEnumDeserializer  extends JsonDeserializer<IBaseEnum> {
    @Override
    @SuppressWarnings("unchecked")
    public IBaseEnum deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String currentName = jp.currentName();
        Object currentValue = jp.getCurrentValue();
        @SuppressWarnings("rawtypes")
        Class findPropertyType = BeanUtils.findPropertyType(currentName, currentValue.getClass());
        JsonFormat annotation = (JsonFormat) findPropertyType.getAnnotation(JsonFormat.class);
        IBaseEnum valueOf;
        if(annotation == null || annotation.shape() != JsonFormat.Shape.OBJECT) {
            valueOf = (IBaseEnum)IBaseEnum.getIEnum( findPropertyType,node.get("name").asText());

        }else {
            valueOf =  (IBaseEnum)IBaseEnum.getIEnum(findPropertyType,node.asText());
        }
        return valueOf;
    }
}
