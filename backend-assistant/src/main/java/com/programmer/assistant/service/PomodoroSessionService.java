package com.programmer.assistant.service;

import com.programmer.assistant.entity.PomodoroSession;
import com.programmer.assistant.mapper.PomodoroSessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 番茄钟会话服务
 */
@Service
public class PomodoroSessionService {
    
    @Autowired
    private PomodoroSessionMapper pomodoroSessionMapper;
    
    /**
     * 开始新的番茄钟会话
     */
    public PomodoroSession startSession(String task, Integer durationMinutes) {
        PomodoroSession session = new PomodoroSession(task, durationMinutes);
        pomodoroSessionMapper.insert(session);
        return session;
    }
    
    /**
     * 完成番茄钟会话
     */
    public PomodoroSession completeSession(Long id) {
        PomodoroSession session = pomodoroSessionMapper.selectById(id);
        if (session != null) {
            session.complete();
            pomodoroSessionMapper.updateById(session);
            return session;
        }
        return null;
    }
    
    /**
     * 中断番茄钟会话
     */
    public PomodoroSession interruptSession(Long id) {
        PomodoroSession session = pomodoroSessionMapper.selectById(id);
        if (session != null) {
            session.interrupt();
            pomodoroSessionMapper.updateById(session);
            return session;
        }
        return null;
    }
    
    /**
     * 根据ID查询
     */
    public PomodoroSession findById(Long id) {
        return pomodoroSessionMapper.selectById(id);
    }
    
    /**
     * 查询所有会话
     */
    public List<PomodoroSession> findAll() {
        return pomodoroSessionMapper.selectList(null);
    }
    
    /**
     * 根据状态查询
     */
    public List<PomodoroSession> findByStatus(PomodoroSession.Status status) {
        return pomodoroSessionMapper.findByStatus(status.name());
    }
    
    /**
     * 根据日期范围查询
     */
    public List<PomodoroSession> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return pomodoroSessionMapper.findByStartTimeBetween(startDate, endDate);
    }
    
    /**
     * 获取今日完成的番茄钟数量
     */
    public Long getTodayCompletedCount() {
        return pomodoroSessionMapper.countTodayCompletedSessions();
    }
    
    /**
     * 获取最近的番茄钟记录
     */
    public List<PomodoroSession> getRecentSessions() {
        return pomodoroSessionMapper.findByStatus("COMPLETED");
    }
    
    /**
     * 获取本周番茄钟记录
     */
    public List<PomodoroSession> getThisWeekSessions() {
        LocalDateTime startOfWeek = LocalDateTime.now().minusDays(7);
        LocalDateTime endOfWeek = LocalDateTime.now();
        return findByDateRange(startOfWeek, endOfWeek);
    }
    
    /**
     * 删除会话
     */
    public void deleteById(Long id) {
        pomodoroSessionMapper.deleteById(id);
    }
}
