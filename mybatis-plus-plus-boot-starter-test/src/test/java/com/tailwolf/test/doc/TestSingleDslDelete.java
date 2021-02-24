package com.tailwolf.test.doc;

import com.tailwolf.entity.SysUser;
import com.tailwolf.mybatis.core.dsl.wrapper.EntityDelete;
import com.tailwolf.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试dsl的删除操作。目前dsl删除只支持单表
 * 如果配置了逻辑删除，delete语句会变成update语句
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TestSingleDslDelete {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 删除语法和查询语法一模一样，可以查看TestSingleDslQuery文件
     * 这里只给出一个示例
     *
     * 启动了逻辑删除：
     * UPDATE sys_user SET deleted = 1 WHERE id = 10000 AND deleted = 0
     * 关闭了逻辑删除
     * DELETE FROM sys_user WHERE id = 10000
     */
    @Test
    public void testDslDelete(){
        EntityDelete<SysUser> sysUserDelete = new EntityDelete<>();
        sysUserDelete.eq(SysUser::getId, 10000);
        sysUserService.dslDelete(sysUserDelete);
    }
}
