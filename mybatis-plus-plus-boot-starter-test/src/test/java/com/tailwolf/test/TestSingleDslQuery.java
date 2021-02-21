package com.tailwolf.test;

import com.alibaba.fastjson.JSON;
import com.tailwolf.entity.SysUser;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityQuery;
import com.tailwolf.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试单表的dsl操作。
 * dslQuery方法，查询一个对象集合
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TestSingleDslQuery {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 测试eq（等于）语法
     */
    @Test
    public void eq(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.eq(SysUser::getUpdateOn, "2021-02-21 18:10:47");
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }
    /**
     * 测试ne（不等于）语法
     */
    @Test
    public void ne(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.ne(SysUser::getId, 1);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }
    /**
     * 测试ge（大于等于）语法
     */
    @Test
    public void ge(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.ge(SysUser::getId, 322);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试le（小于等于）语法
     */
    @Test
    public void le(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.le(SysUser::getId, 1000);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试gt（大于）语法
     */
    @Test
    public void gt(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.gt(SysUser::getId, 1);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试lt（小于）语法
     */
    @Test
    public void lt(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.lt(SysUser::getId, 1000);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试like（模糊查询）语法
     */
    @Test
    public void like(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.like(SysUser::getAccount, "%account%");
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试or（或者）语法。or语法有问题
     */
//    @Test
//    public void or(){
//        //dslQuery，查询集合
//        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
//        entityQuery.and(con -> con.(SysUser::getAccount, "account"));
//        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
//
//        //dslQueryOne，查询一个对象
////        EntityQuery<SysUser> entityQuery2 = new EntityQuery<>();
////        entityQuery2.like(SysUser::getAccount, "%account%");
////        System.out.println(JSON.toJSONString(sysUserService.dslQueryOne(entityQuery2)));
//    }

    /**
     * 测试in语法
     */
    @Test
    public void in(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.in(SysUser::getAccount, 1,2,3);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试not in语法
     */
    @Test
    public void notIn(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.notIn(SysUser::getAccount, 1,2,3);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试is null语法
     */
    @Test
    public void isNull(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.isNull(SysUser::getAccount);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试is not null语法
     */
    @Test
    public void isNotNull(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.isNotNull(SysUser::getAccount);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }
}
