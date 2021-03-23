package com.tailwolf.entity;


import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.tailwolf.mybatis.core.annotation.Table;
import java.io.Serializable;

@Data
@EqualsAndHashCode()
@Table(name = "sys_user_role")
public class SysUserRole implements Serializable{
    //主键id
    private Integer id;

    //用户id
    private Integer sysUserId;

    //角色id
    private Integer sysRoleId;

    //创建日期
    private LocalDateTime createOn;

    //创建人
    private String createBy;

    //更新日期
    private LocalDateTime updateOn;

    //更新人
    private String updateBy;


}