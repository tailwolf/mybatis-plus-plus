package com.tailwolf.entity;


import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.tailwolf.mybatis.core.annotation.Table;
import java.io.Serializable;

@Data
@EqualsAndHashCode()
@Table(name = "project")
public class Project implements Serializable {
    //主键id
    private Integer id;

    //项目名称
    private String projectName;

    //类型
    private Integer type;

    //是否删除
    private Integer deleted;

    //创建日期
    private LocalDateTime createOn;

    //创建人
    private String createBy;

    //更新时间
    private LocalDateTime updateOn;

    //更新人
    private String updateBy;


}