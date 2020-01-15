package com.pface.admin.core.utils;

import org.apache.shiro.util.CollectionUtils;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class IdUtils {

    /**
     * 生成18位随机数字串
     *
     * @return
     */
    public static String getOrderSnByTime18() {
        String s = (System.currentTimeMillis() + "").substring(1) + (System.nanoTime() + "").substring(7, 10);
        Random rm = new Random();
        double pross = (1 + rm.nextDouble()) * Math.pow(10, 12);
        String fixLenthString = String.valueOf(pross);
        return s + fixLenthString.substring(2, 5);
    }

    public static String id() {
        String code = String.valueOf(Math.abs(IdUtils.uuid().hashCode()));
        String random = String.valueOf(new Random().nextInt(900) + 100);
        return code + random;
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static int random(int size) {
        if (size == 0) {
            throw new RuntimeException("Array is Empty.");
        }

        if (size == 1) {
            return 0;
        }

        return new Random().nextInt(size);
    }


    /*
     * 将查询条件按分隔符拼成数据库查询的in条件 例如 '1','2','3'
     * conds 原始条件 "1","2"
     * split 分隔符
     */
    public static String buildInCond(String conds, String split)
    {
        if (StringUtils.isBlank(conds) || StringUtils.isBlank(split))
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        String[] condArray = conds.split(split);

        for (int i = 0; i < condArray.length; i++)
        {
            if (i != 0)
            {
                sb.append(",");
            }
            sb.append("'").append(condArray[i]).append("'");

        }
        return sb.toString();
    }

    /*
     * 将查询条件按分隔符拼成数据库查询的in条件 例如 '1','2','3'
     * conds 原始条件
     * split 分隔符
     */
    public static String buildInCondByList(List<String> conds)
    {
        if (CollectionUtils.isEmpty(conds))
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < conds.size(); i++)
        {
            if (i != 0)
            {
                sb.append(",");
            }
            sb.append("'").append(conds.get(i)).append("'");

        }
        return sb.toString();
    }

    public static String filterSpecialChar(String s){
        if (StringUtils.isEmpty(s)){
            return null;
        }
        String thatS = s;
        thatS = thatS.replaceAll("\\#", "");//去#号

        return thatS;
    }
    public static void main(String[] args) {
        System.out.println(uuid());
        System.out.println(id());
//        System.out.println(getOrderSnByTime18());
//        System.out.println(getOrderSnByTime18());
//        System.out.println(getOrderSnByTime18());
//        System.out.println(getOrderSnByTime18());
//        System.out.println(getOrderSnByTime18());
//        System.out.println(getOrderSnByTime18());

    }
}
