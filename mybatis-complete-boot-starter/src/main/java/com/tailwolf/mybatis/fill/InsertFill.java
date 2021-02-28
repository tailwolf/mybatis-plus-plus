package com.tailwolf.mybatis.fill;

import java.util.HashMap;
import java.util.Map;

/**
 * com.tailwolf.mybatis.fill.FillFieldHandler#insertFill(com.tailwolf.mybatis.fill.InsertFill)的入参
 * @author tailwolf
 * @date 2020-01-24
 */
public class InsertFill {
    private HashMap<String, Object> hashMap = new HashMap<>();

    public void fillColumn(String column, Object value){
        this.hashMap.put(column, value);
    }

    public void clear(){
        this.hashMap.clear();
    }

    public Map<String, Object> getProperties(){
        return hashMap;
    }
}
