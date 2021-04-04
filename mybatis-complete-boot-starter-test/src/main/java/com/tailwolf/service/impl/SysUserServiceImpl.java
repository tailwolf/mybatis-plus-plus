package com.tailwolf.service.impl;

import com.tailwolf.mybatis.core.common.service.impl.EntityOptServiceImpl;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityQuery;
import com.tailwolf.mybatis.datasourse.DataSource;
import org.springframework.stereotype.Service;
import com.tailwolf.entity.SysUser;
import com.tailwolf.mapper.SysUserMapper;
import com.tailwolf.service.SysUserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysUserServiceImpl extends EntityOptServiceImpl<SysUser, SysUserMapper> implements SysUserService{

    @Override
    public boolean testAnnotateDataSource() {
        SysUser sysUser = new SysUser();
        sysUser.setUserName("数据源");
        return this.insert(sysUser);
    }

    @Override
    @DataSource(name = "datasource2")
    public boolean testAnnotateDataSource2() {
        SysUser sysUser = new SysUser();
        sysUser.setUserName("数据源2");
        return this.insert(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testDataSourceRollBack() {
        this.testAnnotateDataSource2();
//        int i = 10 / 0;
        this.testAnnotateDataSource();
    }
}