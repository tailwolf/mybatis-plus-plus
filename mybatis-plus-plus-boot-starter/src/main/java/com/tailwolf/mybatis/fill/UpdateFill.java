package com.tailwolf.mybatis.fill;

import java.util.HashMap;
import java.util.Map;

/**
 * com.tailwolf.mybatis.fill.FillFieldHandler#updateFill(com.tailwolf.mybatis.fill.UpdateFill)的入参
 * @author tailwolf
 * @date 2020-01-24
 */
public class UpdateFill {
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
