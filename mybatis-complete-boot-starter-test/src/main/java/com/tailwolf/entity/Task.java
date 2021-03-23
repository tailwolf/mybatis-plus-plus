package com.tailwolf.entity;


import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.tailwolf.mybatis.core.annotation.Table;
import java.io.Serializable;

@Data
@EqualsAndHashCode()
@Table(name = "task")
public class Task implements Serializable{
    //主键id
    private Integer id;

    //任务名称
    private String taskName;

    //任务时间
    private LocalDateTime taskTime;

    //项目id
    private Integer projectId;

    //创建日期
    private LocalDateTime createOn;

    //创建人
    private String createBy;

    //更新日期
    private LocalDateTime updateOn;

    //更新人
    private String updateBy;


}