package com.tailwolf.mybatis.core.util;

/**
 * 数组工具类
 * @author tailwolf
 * @date 2020-01-11
 */
public class ArrayUtils extends org.apache.commons.lang3.ArrayUtils{
    /**
     * 把字符串数组用指定字符拼接起来
     * @param array         数组
     * @param chara         指定的字符
     * @param startIndex    开始索引
     * @param endIndex      结束索引
     * @return
     */
    public static String joinToString(String[] array, String chara, int startIndex, int endIndex){
        StringBuffer buffer = new StringBuffer();
        for(int i = startIndex; i < array.length && i <= endIndex; i++){
            buffer.append(array[i]);
            if(i < array.length -1 && i < endIndex){
                buffer.append(chara);
            }
        }

        return buffer.toString();
    }
}
