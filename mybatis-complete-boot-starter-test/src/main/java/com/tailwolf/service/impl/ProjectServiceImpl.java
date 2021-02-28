package com.tailwolf.service.impl;

import com.tailwolf.mybatis.core.common.service.impl.EntityOptServiceImpl;
import org.springframework.stereotype.Service;
import com.tailwolf.entity.Project;
import com.tailwolf.mapper.ProjectMapper;
import com.tailwolf.service.ProjectService;

@Service
public class ProjectServiceImpl extends EntityOptServiceImpl<Project, ProjectMapper> implements ProjectService{
}