package com.tailwolf.mybatis.core.dsl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.tailwolf.mybatis.core.common.dao.DslOptMapper;
import com.tailwolf.mybatis.core.dsl.wrapper.JoinQuery;

import java.util.List;
import java.util.Map;

/**
 * 自定义dsl服务层
 * @author tailwolf
 * @date 2020-08-23
 */
@Transactional
public class JoinOptService {

    @Autowired
    private DslOptMapper dslOptMapper;

    /**
     * 当双表查询的时候，需要传入反射对象，指明返回结果的类型
     * @param joinQuery
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> joinQuery(JoinQuery joinQuery, Class<T> clazz) {
        List<Map<String, Object>> resultMap = dslOptMapper.joinQuery(joinQuery, clazz);
        return JSON.parseArray(JSON.toJSONString(resultMap), clazz);
    }
}
