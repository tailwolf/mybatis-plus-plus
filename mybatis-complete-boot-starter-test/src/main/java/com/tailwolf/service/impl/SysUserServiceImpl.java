package com.tailwolf.service.impl;

import com.tailwolf.mybatis.core.common.service.impl.EntityOptServiceImpl;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityQuery;
import com.tailwolf.mybatis.datasourse.DataSource;
import org.springframework.stereotype.Service;
import com.tailwolf.entity.SysUser;
import com.tailwolf.mapper.SysUserMapper;
import com.tailwolf.service.SysUserService;

import java.util.List;

@Service
public class SysUserServiceImpl extends EntityOptServiceImpl<SysUser, SysUserMapper> implements SysUserService{

    @DataSource(name = "datasource1")
    @Override
    public List<SysUser> testAnnotateDataSource() {
        EntityQuery<SysUser> sysUserQuery = new EntityQuery<>();
        sysUserQuery.eq(SysUser::getId, 1);
        return this.dslQuery(sysUserQuery);
    }

    @Override
    public List<SysUser> testAnnotateDataSource2() {
        EntityQuery<SysUser> sysUserQuery = new EntityQuery<>();
        sysUserQuery.eq(SysUser::getId, 1);
        return this.dslQuery(sysUserQuery);
    }
}