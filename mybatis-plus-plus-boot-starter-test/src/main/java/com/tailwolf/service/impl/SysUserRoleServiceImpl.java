package com.tailwolf.service.impl;

import com.tailwolf.mybatis.core.common.service.impl.EntityOptServiceImpl;
import org.springframework.stereotype.Service;
import com.tailwolf.entity.SysUserRole;
import com.tailwolf.mapper.SysUserRoleMapper;
import com.tailwolf.service.SysUserRoleService;

@Service
public class SysUserRoleServiceImpl extends EntityOptServiceImpl<SysUserRole, SysUserRoleMapper> implements SysUserRoleService{
}