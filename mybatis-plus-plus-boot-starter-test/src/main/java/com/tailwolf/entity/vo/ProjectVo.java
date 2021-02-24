package com.tailwolf.entity.vo;

import com.tailwolf.entity.Task;
import com.tailwolf.mybatis.core.annotation.Association;
import com.tailwolf.mybatis.core.annotation.Collection;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode()
public class ProjectVo implements Serializable {
    //主键id
    private Integer id;

    //项目名称
    private String projectName;

    //类型
    private Integer type;

    //该注解相当于mybatis resultMap标签里的collection标签
    //@Association注解同理
    @Collection(clazz = TaskVo.class)
    private List<TaskVo> taskVoList;
}
