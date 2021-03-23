package com.tailwolf.entity;


import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.tailwolf.mybatis.core.annotation.Table;
import java.io.Serializable;

@Data
@EqualsAndHashCode()
@Table(name = "sys_role")
public class SysRole implements Serializable{
    private Integer id;

    //角色名
    private String roleName;

    //角色描述
    private String roleDesc;

    //创建日期
    private Date createOn;

    //创建人
    private String createBy;

    //更新日期
    private Date updateOn;

    //更新人
    private String updateBy;

    //是否删除，0否，1是
    private Integer deleted;


}