package com.programmer.assistant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 番茄钟会话实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    
    /**
     * 自定义构造函数
     */
    public PomodoroSession(String task, Integer durationMinutes) {
        this.task = task;
        this.durationMinutes = durationMinutes;
        this.startTime = LocalDateTime.now();
        this.status = Status.RUNNING;
    }
    
    /**
     * 完成番茄钟
     */
    public void complete() {
        this.endTime = LocalDateTime.now();
        this.status = Status.COMPLETED;
    }
    
    /**
     * 中断番茄钟
     */
    public void interrupt() {
        this.endTime = LocalDateTime.now();
        this.status = Status.INTERRUPTED;
    }
}