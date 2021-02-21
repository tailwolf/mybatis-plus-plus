package com.tailwolf.test;

import com.alibaba.fastjson.JSON;
import com.tailwolf.entity.Project;
import com.tailwolf.entity.SysUser;
import com.tailwolf.service.ProjectService;
import com.tailwolf.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试普通增删改查接口
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TestCrudApi {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 插入一条记录
     */
    @Test
    public void insert(){
        Project project = new Project();
        project.setProjectName("测试项目3");
        project.setType(2);
        System.out.println(JSON.toJSONString(projectService.insert(project)));
    }
    /**
     * 通过主键更新一条记录
     */
    @Test
    public void updateByPk(){
        Project project = new Project();
        project.setId(43);
        project.setProjectName("更新测试项目");
        project.setType(2);
        System.out.println(JSON.toJSONString(projectService.updateByPk(project)));
    }
    /**
     * 通过主键删除一条记录。传实体类
     */
    @Test
    public void deleteByPk(){
        Project project = new Project();
        project.setId(43);
        System.out.println(JSON.toJSONString(projectService.deleteByPk(project)));
    }
    /**
     * 通过主键删除一条记录。直接传主键
     */
    @Test
    public void deleteByPk2(){
        System.out.println(JSON.toJSONString(projectService.deleteByPk(43)));
    }
    /**
     * 删除记录，实体类什么字段有值就拿什么做where条件。有bug
     */
    @Test
    public void delete(){
        Project project = new Project();
        project.setType(2);
        System.out.println(JSON.toJSONString(projectService.delete(project)));
    }


    /**
     * 批量插入
     */
    @Test
    public void insertBatch(){
        List<SysUser> sysUserList = new ArrayList<>();
        for(int i = 1; i < 20; i++){
            SysUser sysUser = new SysUser();
            sysUser.setAccount("account" + i);
            sysUser.setUserName("userName" + i);
            sysUser.setUserPwd("userPwd" + i);
            sysUserList.add(sysUser);
        }
        System.out.println(JSON.toJSONString(sysUserService.insertBatch(sysUserList)));
    }
    /**
     * 通过主键批量删除，List泛型是实体类
     */
    @Test
    public void deleteBatchByPk(){
        List<SysUser> sysUserList = new ArrayList<>();
        SysUser sysUser = new SysUser();
        sysUser.setId(317);
        SysUser sysUser2 = new SysUser();
        sysUser2.setId(318);
        sysUserList.add(sysUser);
        sysUserList.add(sysUser2);

        System.out.println(JSON.toJSONString(sysUserService.deleteBatchByPk(sysUserList)));
    }
    /**
     * 批量更新
     */
    @Test
    public void updateBatchByPk(){
        List<SysUser> sysUserList = new ArrayList<>();
        SysUser sysUser = new SysUser();
        sysUser.setUserName("批量更新1");
        sysUser.setId(319);
        SysUser sysUser2 = new SysUser();
        sysUser2.setId(319);
        sysUser2.setUserName("批量更新2");
        sysUserList.add(sysUser);
        sysUserList.add(sysUser2);

        System.out.println(JSON.toJSONString(sysUserService.updateBatchByPk(sysUserList)));
    }
    /**
     * 通过主键批量删除，直接传主键集合
     */
    @Test
    public void deleteBatchByPk2(){
        List<Integer> idList = new ArrayList<>();
        idList.add(319);
        idList.add(320);

        System.out.println(JSON.toJSONString(sysUserService.deleteBatchByPk(idList)));
    }

    /**
     * 通过实体类查询实体类集合。实体类什么字段有值就拿什么做条件
     */
    @Test
    public void findList(){
        SysUser sysUser = new SysUser();
        sysUser.setUserName("userName7");
        System.out.println(JSON.toJSONString(sysUserService.findList(sysUser)));
    }
    /**
     * 通过实体类查询一个实体类。实体类什么字段有值就拿什么做条件
     */
    @Test
    public void findOne(){
        SysUser sysUser = new SysUser();
        sysUser.setUserName("userName7");
        System.out.println(JSON.toJSONString(sysUserService.findOne(sysUser)));
    }
    /**
     * 通过主键查询实体类。传实体类
     */
    @Test
    public void findByPk(){
        SysUser sysUser = new SysUser();
        sysUser.setId(321);
        System.out.println(JSON.toJSONString(sysUserService.findByPk(sysUser)));
    }
    /**
     * 通过主键查询实体类。主键传实体类
     */
    @Test
    public void findByPk2(){
        System.out.println(JSON.toJSONString(sysUserService.findByPk(321)));
    }
}
