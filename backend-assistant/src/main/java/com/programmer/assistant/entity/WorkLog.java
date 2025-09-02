package com.programmer.assistant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 工作日志实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("work_log")
public class WorkLog {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    
    private String content;
    
    @TableField("work_date")
    private LocalDate workDate;
    
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 自定义构造函数
     */
    public WorkLog(String title, String content, LocalDate workDate) {
        this.title = title;
        this.content = content;
        this.workDate = workDate;
    }
}