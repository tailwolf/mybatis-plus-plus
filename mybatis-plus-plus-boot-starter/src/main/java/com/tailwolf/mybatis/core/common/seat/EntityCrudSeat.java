package com.tailwolf.mybatis.core.common.seat;

import com.tailwolf.mybatis.core.annotation.Column;
import com.tailwolf.mybatis.core.annotation.Table;

/**
 * 占位实体查询类
 * @author tailwolf
 * @date 2020-05-29
 */
@Table(tableName = "entity_crud_seat")
public class EntityCrudSeat {
    /**
     * 主键id
     */
    @Column(name = "ID")
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "ROLE_NAME")
    private String roleName;

    /**
     * 用户密码
     */
    @Column(name = "ROLE_DESC")
    private String roleDesc;

    @Column(name = "USER_ID")
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
