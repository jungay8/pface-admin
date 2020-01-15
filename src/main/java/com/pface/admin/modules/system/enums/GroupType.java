package com.pface.admin.modules.system.enums;

import com.pface.admin.modules.base.enums.IBaseEnum;

public enum GroupType implements IBaseEnum {
    GROUP_1("组一"),
    GROUP_2("组二"),
    GROUP_3("组三");
    /**
     * 1、用户User：最终操作人员，权限的最终受益者，控制权限控制权限实际上就是控制用户的权限，而不是角色或者用户组的权限
     * 2、用户组UserGroup：是相对垂直而言的。比如说采购部这个用户组实际上是由采购部的业务员(暂且定义都为用户)组成的，具有上下级的明确关系；采购部只能查看属于采购部的文档，销售部只能查看属于销售部的文档，带有强烈的部门(组)性质，但是采购部业务员虽然都是属于同一个部门，但是却不一定有着相同的权限，比如说经理和一般业务员的权限肯定存在差异
     * 3、角色Role：用户组是带有一种垂直既自上而下的性质，而角色的范围则没有带着那么浓厚的垂直关系，而是带有比较明显的水平(交叉)性质；比方说现定义一个角色：经理，这个经理包含了各个部门的经理，而不单单是采购部经理或者是销售部经理，很明显这个‘经理’角色显然同时具有各部门的经理的权限，也就是说这时候如果各部门经理们只是处于该‘经理’角色，那么采购部经理不但具有采购部经理的操作权限，同时也被赋予了其他各部门经理的权限，这个时候各个部门经理的权限是一致的，但是这样势必造成权限的拥堵或者混乱，此时刚才提到的第一个对象：用户就派上用场了，几个部门经理同属于‘经理’角色情况下又想他们之间的权限有区别，你只能对每个部门经理(身份为：用户)单独授权了，当然你也可以根据该用户身处的用户组和角色之间的关联关系或者排斥关系来确认用户的最终权限。
     */
    //···相比用户角色管理，可以垂直粒度进行用户管理

    private GroupType(String text) {
        this.text = text;
    }

    private String text;
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getText() {
        return null;
    }
}