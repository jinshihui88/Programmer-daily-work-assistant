package com.programmer.assistant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 学习进度实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("learning_progress")
public class LearningProgress {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String subject;
    
    private String description;
    
    @TableField("total_hours")
    private Integer totalHours;
    
    @TableField("completed_hours")
    private Integer completedHours;
    
    private Status status;
    
    @TableField("start_date")
    private LocalDateTime startDate;
    
    @TableField("target_date")
    private LocalDateTime targetDate;
    
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    public enum Status {
        NOT_STARTED, IN_PROGRESS, COMPLETED, PAUSED
    }
    
    /**
     * 自定义构造函数
     */
    public LearningProgress(String subject, String description, Integer totalHours) {
        this.subject = subject;
        this.description = description;
        this.totalHours = totalHours;
        this.completedHours = 0;
        this.status = Status.NOT_STARTED;
    }
    
    /**
     * 计算完成百分比
     */
    public Double getCompletionPercentage() {
        if (totalHours == null || totalHours == 0) {
            return 0.0;
        }
        return (completedHours.doubleValue() / totalHours.doubleValue()) * 100;
    }
}