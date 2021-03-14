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

/**
 * 测试单表的dsl操作。
 * dslQuery方法，查询一个对象集合
 * 如果配置了逻辑删除，则会带上逻辑删除的字段
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TestSingleDslQuery {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 测试eq（等于）语法
     * SELECT create_by, deleted, update_by, user_pwd, create_on, id, user_name, update_on, version, account FROM sys_user WHERE update_on = '2021-02-21 18:10:47' AND deleted = 0
     */
    @Test
    public void eq(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.eq(SysUser::getUpdateOn, "2021-02-21 18:10:47");
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }
    /**
     * 测试ne（不等于）语法
     * SELECT create_by, deleted, update_by, user_pwd, create_on, id, user_name, update_on, version, account FROM sys_user WHERE id != 1 AND deleted = 0
     */
    @Test
    public void ne(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.ne(SysUser::getId, 1);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }
    /**
     * 测试ge（大于等于）语法
     * SELECT create_by, deleted, update_by, user_pwd, create_on, id, user_name, update_on, version, account FROM sys_user WHERE id >= 322 AND deleted = 0
     */
    @Test
    public void ge(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.ge(SysUser::getId, 322);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试le（小于等于）语法
     * SELECT create_by, deleted, update_by, user_pwd, create_on, id, user_name, update_on, version, account FROM sys_user WHERE id <= 1000 AND deleted = 0
     */
    @Test
    public void le(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.le(SysUser::getId, 1000);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试gt（大于）语法
     * SELECT create_by, deleted, update_by, user_pwd, create_on, id, user_name, update_on, version, account FROM sys_user WHERE id > 1 AND deleted = 0
     */
    @Test
    public void gt(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.gt(SysUser::getId, 1);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试lt（小于）语法
     * SELECT create_by, deleted, update_by, user_pwd, create_on, id, user_name, update_on, version, account FROM sys_user WHERE id < 1000 AND deleted = 0
     */
    @Test
    public void lt(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.lt(SysUser::getId, 1000);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试like（模糊查询）
     * SELECT create_by, deleted, update_by, user_pwd, create_on, id, user_name, update_on, version, account FROM sys_user WHERE account LIKE concat('%', 'account', '%') AND user_pwd LIKE concat('%', 'userPwd') AND user_name LIKE concat('userName', '%') AND deleted = 0
     */
    @Test
    public void like(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.like(SysUser::getAccount, "account");
        entityQuery.leftLike(SysUser::getUserPwd, "userPwd");
        entityQuery.rightLike(SysUser::getUserName, "userName");
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试or语法。
     * SELECT create_by, deleted, update_by, user_pwd, create_on, id, user_name, update_on, version, account FROM sys_user WHERE id = 1 AND (user_pwd = 'mmm' OR account = 'vvv') OR user_pwd = 'mmm' OR (user_pwd = 'mmm' OR account = 'vvv') AND deleted = 0
     */
    @Test
    public void or(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.eq(SysUser::getId, 1);
        entityQuery.and(and -> and.eq(SysUser::getUserPwd, "llp").or().eq(SysUser::getAccount, "bbn"));
        entityQuery.or();
        entityQuery.eq(SysUser::getUserPwd, "ss");
        entityQuery.or(and -> and.eq(SysUser::getUserPwd, "mmm").or().eq(SysUser::getAccount, "vvv"));
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试in语法
     * SELECT create_by, deleted, update_by, user_pwd, create_on, id, user_name, update_on, version, account FROM sys_user WHERE account IN (1, 2, 3) AND deleted = 0
     */
    @Test
    public void in(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.in(SysUser::getAccount, 1,2,3);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试not in语法
     * SELECT create_by, deleted, update_by, user_pwd, create_on, id, user_name, update_on, version, account FROM sys_user WHERE account NOT IN (1, 2, 3) AND deleted = 0
     */
    @Test
    public void notIn(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.notIn(SysUser::getAccount, 1,2,3);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试is null语法
     * SELECT create_by, deleted, update_by, user_pwd, create_on, id, user_name, update_on, version, account FROM sys_user WHERE account IS NULL AND deleted = 0
     */
    @Test
    public void isNull(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.isNull(SysUser::getAccount);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试is not null语法
     * SELECT create_by, deleted, update_by, user_pwd, create_on, id, user_name, update_on, version, account FROM sys_user WHERE account IS NOT NULL AND deleted = 0
     */
    @Test
    public void isNotNull(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.isNotNull(SysUser::getAccount);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试select语法
     * SELECT id, deleted FROM sys_user WHERE deleted = 0
     */
    @Test
    public void select(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.select(SysUser::getId, SysUser::getDeleted);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试groupBy和having语法
     * SELECT create_by, deleted, update_by, user_pwd, create_on, id, user_name, update_on, version, account FROM sys_user WHERE deleted = 0 GROUP BY id, account HAVING id != 1
     */
    @Test
    public void group(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.groupBy(SysUser::getId, SysUser::getAccount);
        entityQuery.having(having -> having.ne(SysUser::getId, 1));
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }

    /**
     * 测试orderBy语法
     * SELECT create_by, deleted, update_by, user_pwd, create_on, id, user_name, update_on, version, account FROM sys_user WHERE user_pwd = 'ggg' AND deleted = 0 ORDER BY id ASC, account DESC
     */
    @Test
    public void orderBy(){
        EntityQuery<SysUser> entityQuery = new EntityQuery<>();
        entityQuery.eq(SysUser::getUserPwd, "ggg");
        entityQuery.asc(SysUser::getId);
        entityQuery.desc(SysUser::getAccount);
        System.out.println(JSON.toJSONString(sysUserService.dslQuery(entityQuery)));
    }
}
