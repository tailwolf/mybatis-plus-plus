package com.tailwolf.service;

import com.tailwolf.mybatis.core.common.service.EntityOptService;
import com.tailwolf.entity.SysUser;
import com.tailwolf.mybatis.datasourse.DataSource;

import java.util.List;

public interface SysUserService extends EntityOptService<SysUser>{
    /**
     * 测试使用注解指定数据源
     * 指定了数据源
     * @return
     */
    boolean testAnnotateDataSource();

    /**
     * 测试使用注解指定数据源
     * 不指定数据源，作为对照
     * @return
     */
    boolean testAnnotateDataSource2();

    /**
     * 测试回滚
     */
    void testDataSourceRollBack();
}