package com.programmer.assistant.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 番茄钟会话实体
 */
@TableName("pomodoro_session")
public class PomodoroSession {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String task;
    
    @TableField("duration_minutes")
    private Integer durationMinutes;
    
    @TableField("start_time")
    private LocalDateTime startTime;
    
    @TableField("end_time")
    private LocalDateTime endTime;
    
    private Status status;
    
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    public enum Status {
        RUNNING, COMPLETED, INTERRUPTED
    }
    
    // 构造函数
    public PomodoroSession() {}
    
    public PomodoroSession(String task, Integer durationMinutes) {
        this.task = task;
        this.durationMinutes = durationMinutes;
        this.startTime = LocalDateTime.now();
        this.status = Status.RUNNING;
    }
    
    // 完成番茄钟
    public void complete() {
        this.endTime = LocalDateTime.now();
        this.status = Status.COMPLETED;
    }
    
    // 中断番茄钟
    public void interrupt() {
        this.endTime = LocalDateTime.now();
        this.status = Status.INTERRUPTED;
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTask() {
        return task;
    }
    
    public void setTask(String task) {
        this.task = task;
    }
    
    public Integer getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}