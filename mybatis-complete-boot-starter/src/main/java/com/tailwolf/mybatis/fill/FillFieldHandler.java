package com.tailwolf.mybatis.fill;

import java.util.HashMap;

/**
 * 关于插入和更新数据时，填充指定属性的值
 * @author tailwolf
 * @date 2020-09-06
 */
public interface FillFieldHandler {
    void insertFill(InsertFill insertFill);

    void updateFill(UpdateFill updateFill);
}