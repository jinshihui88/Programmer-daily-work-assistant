package com.programmer.assistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programmer.assistant.entity.PomodoroSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 番茄钟会话Mapper
 */
@Mapper
public interface PomodoroSessionMapper extends BaseMapper<PomodoroSession> {
    
    /**
     * 根据状态查询
     */
    @Select("SELECT * FROM pomodoro_session WHERE status = #{status} ORDER BY start_time DESC")
    List<PomodoroSession> findByStatus(@Param("status") String status);
    
    /**
     * 根据日期范围查询
     */
    @Select("SELECT * FROM pomodoro_session WHERE start_time BETWEEN #{startDate} AND #{endDate} ORDER BY start_time DESC")
    List<PomodoroSession> findByStartTimeBetween(@Param("startDate") LocalDateTime startDate, 
                                                @Param("endDate") LocalDateTime endDate);
    
    /**
     * 获取今日番茄钟
     */
    @Select("SELECT * FROM pomodoro_session WHERE DATE(start_time) = CURDATE() ORDER BY start_time DESC")
    List<PomodoroSession> findTodaySessions();
    
    /**
     * 获取正在运行的番茄钟
     */
    @Select("SELECT * FROM pomodoro_session WHERE status = 'RUNNING' ORDER BY start_time DESC")
    List<PomodoroSession> findRunningSessions();
    
    /**
     * 统计完成的番茄钟数量
     */
    @Select("SELECT COUNT(*) FROM pomodoro_session WHERE status = 'COMPLETED' AND DATE(start_time) = CURDATE()")
    Long countTodayCompletedSessions();
}