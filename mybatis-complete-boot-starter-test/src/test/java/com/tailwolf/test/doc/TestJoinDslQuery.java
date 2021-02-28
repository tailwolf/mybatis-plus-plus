package com.tailwolf.test.doc;

import com.alibaba.fastjson.JSON;
import com.tailwolf.entity.*;
import com.tailwolf.entity.vo.ProjectVo;
import com.tailwolf.mybatis.core.dsl.JoinOptService;
import com.tailwolf.mybatis.core.dsl.wrapper.JoinQuery;
import com.tailwolf.service.SysUserRoleService;
import com.tailwolf.service.SysUserService;
import com.tailwolf.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 双表查询的基础语法和单表查询一样，可以查看TestSingleDslQuery文件
 * 这里给出的是双表查询特有的语法
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TestJoinDslQuery {

    @Autowired
    private JoinOptService joinOptService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private TaskService taskService;

    /**
     * 测试innerJoin
     * SELECT * FROM sys_user t1 INNER JOIN sys_user_role t2 ON t1.id = t2.sys_user_id WHERE t1.deleted = 0
     */
    @Test
    public void testInnerJoin(){
        JoinQuery<SysUser, SysUserRole> joinQuery = new JoinQuery<>();
        joinQuery.from(SysUser::new).innerJoin(SysUserRole::new)
                .on(condition -> condition.eq(SysUser::getId, SysUserRole::getSysUserId));
        List<SysUser> sysUsers = joinOptService.joinQuery(joinQuery, SysUser.class);
        System.out.println(sysUsers);
    }

    /**
     * 测试left join
     * SELECT * FROM sys_user t1 LEFT JOIN sys_user_role t2 ON t1.id = t2.sys_user_id WHERE t1.deleted = 0
     */
    @Test
    public void testLeftjoin(){
        JoinQuery<SysUser, SysUserRole> joinQuery = new JoinQuery<>();
        joinQuery.from(SysUser::new).leftJoin(SysUserRole::new)
                .on(condition -> condition.eq(SysUser::getId, SysUserRole::getSysUserId));
        List<SysUser> sysUsers = joinOptService.joinQuery(joinQuery, SysUser.class);
        System.out.println(sysUsers);
    }

    /**
     * 测试rightJoin
     * SELECT * FROM sys_user t1 RIGHT JOIN sys_user_role t2 ON t1.id = t2.sys_user_id WHERE t1.deleted = 0
     */
    @Test
    public void testRightJoin(){
        JoinQuery<SysUser, SysUserRole> joinQuery = new JoinQuery<>();
        joinQuery.from(SysUser::new).rightJoin(SysUserRole::new)
                .on(condition -> condition.eq(SysUser::getId, SysUserRole::getSysUserId));
        List<SysUser> sysUsers = joinOptService.joinQuery(joinQuery, SysUser.class);
        System.out.println(sysUsers);
    }

    /**
     * 测试连表查询
     * SELECT * FROM sys_user t1, sys_user_role t2 WHERE t1.id = 1 AND t1.deleted = 0
     */
    @Test
    public void test(){
        JoinQuery<SysUser, SysUserRole> joinQuery = new JoinQuery<>();
        joinQuery.from(SysUser::new, SysUserRole::new)
                .eq(SysUser::getId, 1);
        List<SysUser> sysUsers = joinOptService.joinQuery(joinQuery, SysUser.class);
        System.out.println(sysUsers);
    }

    /**
     * 测试返回嵌套对象
     * SELECT * FROM project t1 INNER JOIN task t2 ON t1.id = t2.project_id WHERE t1.deleted = 0
     *
     * 本人机器上的返回结果
     * [{"id":44,"projectName":"测试项目3","taskVoList":[{"projectId":44,"taskName":"任务0"},{"projectId":44,"taskName":"任务1"},{"projectId":44,"taskName":"任务2"}],"type":2},{"id":45,"projectName":"测试项目4","taskVoList":[{"projectId":45,"taskName":"任务20"},{"projectId":45,"taskName":"任务21"},{"projectId":45,"taskName":"任务22"}],"type":1}]
     */
    @Test
    public void testNest(){
        JoinQuery<Project, Task> joinQuery = new JoinQuery<>();
        joinQuery.from(Project::new).innerJoin(Task::new)
                .on(condition -> condition.eq(Project::getId, Task::getProjectId));
        List<ProjectVo> projectVoList = joinOptService.joinQuery(joinQuery, ProjectVo.class);
        System.out.println(JSON.toJSONString(projectVoList));
    }

    /**
     * 测试select语法
     * SELECT t1.id, t1.project_name, t2.task_name FROM project t1 INNER JOIN task t2 ON t1.id = t2.project_id WHERE t1.deleted = 0
     *
     * 本人机器上的返回结果
     * [{"id":44,"projectName":"测试项目3","taskVoList":[{"taskName":"任务0"},{"taskName":"任务1"},{"taskName":"任务2"}]},{"id":45,"projectName":"测试项目4","taskVoList":[{"taskName":"任务20"},{"taskName":"任务21"},{"taskName":"任务22"}]}]
     */
    @Test
    public void testSelect(){
        JoinQuery<Project, Task> joinQuery = new JoinQuery<>();
        joinQuery.select(select -> select.column(Project::getId).column(Project::getProjectName).column(Task::getTaskName))
                .from(Project::new).innerJoin(Task::new)
                .on(condition -> condition.eq(Project::getId, Task::getProjectId));
        List<ProjectVo> projectVoList = joinOptService.joinQuery(joinQuery, ProjectVo.class);
        System.out.println(JSON.toJSONString(projectVoList));
    }
}
