package com.programmer.assistant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 代码片段实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("code_snippet")
public class CodeSnippet {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    
    private String code;
    
    private String language;
    
    private String description;
    
    private String tags;
    
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 自定义构造函数
     */
    public CodeSnippet(String title, String code, String language, String description, String tags) {
        this.title = title;
        this.code = code;
        this.language = language;
        this.description = description;
        this.tags = tags;
    }
}