package com.tailwolf.service.impl;

import com.tailwolf.mybatis.core.common.service.impl.EntityOptServiceImpl;
import org.springframework.stereotype.Service;
import com.tailwolf.entity.Task;
import com.tailwolf.mapper.TaskMapper;
import com.tailwolf.service.TaskService;

@Service
public class TaskServiceImpl extends EntityOptServiceImpl<Task, TaskMapper> implements TaskService{
}