package com.tailwolf.test.doc;

import com.tailwolf.entity.SysUser;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityDelete;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityUpdate;
import com.tailwolf.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试dsl的更新操作。目前dsl更新只支持单表
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TestSingleDslUpdate {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 删除语法和查询语法一模一样，可以查看TestSingleDslQuery文件
     * 这里只给出一个示例
     * UPDATE sys_user SET account = '测试setAccount', user_name = '测试setUsername', update_on = '2021-02-25 00:08:16', update_by = 'test' WHERE id = 10000 AND deleted = 0
     * 这条sql语句多了update_on，update_by字段，这是因为本人测试的时候，开启了自动填充功能，详见本测试项目的com.tailwolf.config.MyBatisCompleteConfig
     *
     * 注意：如果没有set条件的时候，mybatis-complete会抛异常
     */
    @Test
    public void testDslUpdate(){
        EntityUpdate<SysUser> sysUserUpdate = new EntityUpdate<>();
        sysUserUpdate.set(SysUser::getAccount, "测试setAccount").set(SysUser::getUserName, "测试setUsername");
        sysUserUpdate.eq(SysUser::getId, 10000);
        sysUserService.dslUpdate(sysUserUpdate);
    }
}
