package com.tailwolf.test.doc;

import com.alibaba.fastjson.JSON;
import com.tailwolf.entity.SysUser;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityQuery;
import com.tailwolf.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 测试动态数据源
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TestDynamicDataSource {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 测试dsl动态数据源
     */
    @Test
    public void dslDynamicDataSource(){
        //用的是默认数据源
        EntityQuery<SysUser> sysUserQuery = new EntityQuery<>();
        sysUserQuery.eq(SysUser::getId, 1);
        List<SysUser> sysUserList = sysUserService.dslQuery(sysUserQuery);
        System.out.println(JSON.toJSONString(sysUserList));
        //指定数据源
        EntityQuery<SysUser> dsSysUserQuery = new EntityQuery<>();
        dsSysUserQuery.eq(SysUser::getId, 1);
        dsSysUserQuery.setDataSource("datasource1");
        List<SysUser> dsSysUserList = sysUserService.dslQuery(dsSysUserQuery);
        System.out.println(JSON.toJSONString(dsSysUserList));
    }

    /**
     * 测试基于注解使用的数据源
     */
    @Test
    public void annotateDataSource(){
        sysUserService.testAnnotateDataSource();
        sysUserService.testAnnotateDataSource2();
    }
}
