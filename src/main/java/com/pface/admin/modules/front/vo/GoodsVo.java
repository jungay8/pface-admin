package com.pface.admin.modules.front.vo;

import java.io.Serializable;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/7/2
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
public class GoodsVo implements Serializable {

    public GoodsVo(){}

    public GoodsVo(Long id, String goodsType) {
        this.id = id;
        this.goodsType = goodsType;
    }

    private Long id;

    private String goodsType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

}
