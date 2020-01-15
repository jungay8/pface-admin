package com.pface.admin.modules.jiekou.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class Test {
    public static void main(String[] args) throws Exception {
        serializeFlyPig();
        FlyPig flyPig = deserializeFlyPig();
        System.out.println(flyPig.toString());

        test2JsonSerializable();
    }

    /**
     * 序列化
     */
    private static void serializeFlyPig() throws IOException {
        FlyPig flyPig = new FlyPig();
        flyPig.setColor("black");
        flyPig.setName("naruto");
        flyPig.setCar("0000");
        // ObjectOutputStream 对象输出流，将 flyPig 对象存储到E盘的 flyPig.txt 文件中，完成对 flyPig 对象的序列化操作
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("d:/flyPig.txt")));
        oos.writeObject(flyPig);
        System.out.println("FlyPig 对象序列化成功！");
        oos.close();
    }

    /**
     * 反序列化
     */
    private static FlyPig deserializeFlyPig() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("d:/flyPig.txt")));
        FlyPig person = (FlyPig) ois.readObject();
        System.out.println("FlyPig 对象反序列化成功！");
        return person;
    }

    private static void test2JsonSerializable() {
        //对map类型
        Map map = new HashMap();
        map.put("a", null);
        map.put("b", "b");
        String servial = serial2json(map);
        System.out.println("map类型序列化值："+servial);

        Map unmap = unserial2json(servial, Map.class);
        System.out.println("map类型反序列化值：a="+unmap.get("a") + "，b=" + unmap.get("b"));

        //对vo类型
        FlyPig flyPig = new FlyPig();
        flyPig.setColor(null);
        flyPig.setName("我是name");
        flyPig.setCar("oooo");
        String servial2 = serial2json(flyPig);
        System.out.println("vo类型序列化值："+servial2);

        FlyPig unmap2 = unserial2json(servial2, FlyPig.class);
        System.out.println("vo类型反序列化：color="+unmap2.getColor()+ ",name=" + unmap2.getName() + ",car=" + unmap2.getCar());
    }

    //通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
    //Include.Include.ALWAYS 默认
    //Include.NON_DEFAULT 属性为默认值不序列化
    //Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
    //Include.NON_NULL 属性为NULL 不序列化
    public static final JsonInclude.Include include = JsonInclude.Include.NON_NULL;

    private static String serial2json(Object o) {
        ObjectMapper mapper = new ObjectMapper();

        mapper.setSerializationInclusion(include);
        String ret_val = null;
        try {
            ret_val = mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        return ret_val;
    }

    private static <T> T unserial2json(String json, Class<T> clasz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(include);
        try {
             return  mapper.readValue(json, clasz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
