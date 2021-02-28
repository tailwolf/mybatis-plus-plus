package com.tailwolf.mybatis.core.config;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis-complete关于mapper接口的配置
 * @author tailwolf
 * @date 2020-09-06
 */
public class MapperListProperties {
    public static List<String> dslMapperList = new ArrayList<>();
    public static List<String> daoMapperList = new ArrayList<>();

    static {
        dslMapperList.add("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.dslQuery");
        dslMapperList.add("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.dslQueryOne");
        dslMapperList.add("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.dslDelete");
        dslMapperList.add("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.dslUpdate");
        dslMapperList.add("com.tailwolf.mybatis.core.common.dao.DslOptMapper.joinQuery");

        daoMapperList.add("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.insert");
        daoMapperList.add("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.updateByPk");
        daoMapperList.add("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.updateBatchByPk");
        daoMapperList.add("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.deleteBatchByPk");
        daoMapperList.add("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.deleteByPk");
        daoMapperList.add("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.delete");
        daoMapperList.add("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.findList");
        daoMapperList.add("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.findOne");
        daoMapperList.add("com.tailwolf.mybatis.core.common.dao.EntityOptMapper.findByPk");
    }

}
