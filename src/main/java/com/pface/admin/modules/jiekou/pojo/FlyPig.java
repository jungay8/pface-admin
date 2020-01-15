package com.pface.admin.modules.jiekou.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

public class FlyPig implements Serializable {
    private static final long serialVersionUID = 1L;
    private static String AGE = "269";  //static 属性不序列化；测试方法：先序列号，再修改这个值，在读取这个这个值，读取的结果是修改的这个值而不是执行序列化之前的那个值
    private String name;
    private String color;
    transient private String car;  //存储文件的方式序列化时，transient修饰的属性不序列化

    private String addTip;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getAddTip() {
        return addTip;
    }

    public void setAddTip(String addTip) {
        this.addTip = addTip;
    }

    @Override
    public String toString() {
        return "FlyPig{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", car='" + car + '\'' +
                ", AGE='" + AGE + '\'' +
                //", addTip='" + addTip + '\'' +
                '}';
    }
}
