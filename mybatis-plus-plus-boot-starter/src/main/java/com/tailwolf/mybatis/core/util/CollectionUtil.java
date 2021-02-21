package com.tailwolf.mybatis.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 集合工具类
 * @author tailwolf
 * @date 2020-03-26
 */
public class CollectionUtil {
    /**
     * 将两个集合合并成一个
     * @param list1
     * @param list2
     * @param <T>
     * @return
     */
    public static <T> List<T> mergeList(List<T> list1, List<T> list2){
        if(list1 == null && list2 != null){
            return list2;
        }
        if(list1 != null && list2 == null){
            return list1;
        }

        List<T> allList = new ArrayList<>();
        allList.addAll(list1);
        allList.addAll(list2);
        return allList;
    }

    /**
     * 判断集合是否为空
     * @param list
     */
    public static boolean isEmtpy(List list){
        return list == null || list.size() < 1;
    }
}
