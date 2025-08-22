package com.programmer.assistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programmer.assistant.entity.WorkLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工作日志Mapper
 */
@Mapper
public interface WorkLogMapper extends BaseMapper<WorkLog> {
    
    /**
     * 根据日期范围查询工作日志
     */
    @Select("SELECT * FROM work_log WHERE work_date BETWEEN #{startDate} AND #{endDate} ORDER BY work_date DESC")
    List<WorkLog> findByWorkDateBetween(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
    
    /**
     * 根据标题模糊查询
     */
    @Select("SELECT * FROM work_log WHERE title LIKE CONCAT('%', #{title}, '%') ORDER BY work_date DESC")
    List<WorkLog> findByTitleContaining(@Param("title") String title);
    
    /**
     * 获取最近的工作日志
     */
    @Select("SELECT * FROM work_log ORDER BY work_date DESC LIMIT 10")
    List<WorkLog> findTop10ByOrderByWorkDateDesc();
}