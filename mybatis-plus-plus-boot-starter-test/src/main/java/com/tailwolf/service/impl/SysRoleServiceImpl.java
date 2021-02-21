package com.tailwolf.service.impl;

import com.tailwolf.mybatis.core.common.service.impl.EntityOptServiceImpl;
import org.springframework.stereotype.Service;
import com.tailwolf.entity.SysRole;
import com.tailwolf.mapper.SysRoleMapper;
import com.tailwolf.service.SysRoleService;

@Service
public class SysRoleServiceImpl extends EntityOptServiceImpl<SysRole, SysRoleMapper> implements SysRoleService{
}