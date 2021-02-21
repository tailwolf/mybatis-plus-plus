package com.tailwolf.mybatis.generator.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 关于生成实体类的配置
 * @author tailwolf
 * @date 2021-01-27
 */
public class EntityConfig {
    //1代表生成getset方法，2代表使用lombok。默认是1
    private Integer methodFormat = 1;
    //排除生成表字段
    private List<String> exColumnList = new ArrayList<>();

    public Integer getMethodFormat() {
        return methodFormat;
    }

    public void setMethodFormat(Integer methodFormat) {
        this.methodFormat = methodFormat;
    }

    public List<String> getExColumnList() {
        return exColumnList;
    }

    public void setExColumnList(List<String> exColumnList) {
        this.exColumnList = exColumnList;
    }
}
