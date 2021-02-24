package com.tailwolf.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode()
public class TaskVo implements Serializable {
    //任务名称
    private String taskName;

    //项目id
    private Integer projectId;
}
