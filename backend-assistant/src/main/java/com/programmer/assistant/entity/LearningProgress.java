package com.programmer.assistant.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 学习进度实体
 */
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
    
    // 构造函数
    public LearningProgress() {}
    
    public LearningProgress(String subject, String description, Integer totalHours) {
        this.subject = subject;
        this.description = description;
        this.totalHours = totalHours;
        this.completedHours = 0;
        this.status = Status.NOT_STARTED;
    }
    
    // 计算完成百分比
    public Double getCompletionPercentage() {
        if (totalHours == null || totalHours == 0) {
            return 0.0;
        }
        return (completedHours.doubleValue() / totalHours.doubleValue()) * 100;
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getTotalHours() {
        return totalHours;
    }
    
    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }
    
    public Integer getCompletedHours() {
        return completedHours;
    }
    
    public void setCompletedHours(Integer completedHours) {
        this.completedHours = completedHours;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public LocalDateTime getTargetDate() {
        return targetDate;
    }
    
    public void setTargetDate(LocalDateTime targetDate) {
        this.targetDate = targetDate;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}