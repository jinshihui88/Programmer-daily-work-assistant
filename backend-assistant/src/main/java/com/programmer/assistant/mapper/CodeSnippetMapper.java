package com.programmer.assistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programmer.assistant.entity.CodeSnippet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 代码片段Mapper
 */
@Mapper
public interface CodeSnippetMapper extends BaseMapper<CodeSnippet> {
    
    /**
     * 根据编程语言查询
     */
    @Select("SELECT * FROM code_snippet WHERE language = #{language} ORDER BY create_time DESC")
    List<CodeSnippet> findByLanguage(@Param("language") String language);
    
    /**
     * 根据标题模糊查询
     */
    @Select("SELECT * FROM code_snippet WHERE title LIKE CONCAT('%', #{title}, '%') ORDER BY create_time DESC")
    List<CodeSnippet> findByTitleContaining(@Param("title") String title);
    
    /**
     * 根据标签查询
     */
    @Select("SELECT * FROM code_snippet WHERE tags LIKE CONCAT('%', #{tag}, '%') ORDER BY create_time DESC")
    List<CodeSnippet> findByTagsContaining(@Param("tag") String tag);
    
    /**
     * 获取所有编程语言
     */
    @Select("SELECT DISTINCT language FROM code_snippet ORDER BY language")
    List<String> findAllLanguages();
}