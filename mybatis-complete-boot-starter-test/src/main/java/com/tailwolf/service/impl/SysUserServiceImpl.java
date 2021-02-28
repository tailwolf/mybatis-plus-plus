package com.tailwolf.service.impl;

import com.tailwolf.mybatis.core.common.service.impl.EntityOptServiceImpl;
import org.springframework.stereotype.Service;
import com.tailwolf.entity.SysUser;
import com.tailwolf.mapper.SysUserMapper;
import com.tailwolf.service.SysUserService;

@Service
public class SysUserServiceImpl extends EntityOptServiceImpl<SysUser, SysUserMapper> implements SysUserService{
}