package com.programmer.assistant.service;

import com.programmer.assistant.entity.WorkLog;
import com.programmer.assistant.mapper.WorkLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * 工作日志服务
 */
@Service
public class WorkLogService {
    
    @Autowired
    private WorkLogMapper workLogMapper;
    
    /**
     * 保存工作日志
     */
    public WorkLog save(WorkLog workLog) {
        if (workLog.getId() == null) {
            workLogMapper.insert(workLog);
        } else {
            workLogMapper.updateById(workLog);
        }
        return workLog;
    }
    
    /**
     * 根据ID查询
     */
    public WorkLog findById(Long id) {
        return workLogMapper.selectById(id);
    }
    
    /**
     * 查询所有工作日志
     */
    public List<WorkLog> findAll() {
        return workLogMapper.selectList(null);
    }
    
    /**
     * 根据日期范围查询
     */
    public List<WorkLog> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return workLogMapper.findByWorkDateBetween(startDate, endDate);
    }
    
    /**
     * 根据标题搜索
     */
    public List<WorkLog> searchByTitle(String title) {
        return workLogMapper.findByTitleContaining(title);
    }
    
    /**
     * 获取最近的工作日志
     */
    public List<WorkLog> getRecentLogs() {
        return workLogMapper.findTop10ByOrderByWorkDateDesc();
    }
    
    /**
     * 删除工作日志
     */
    public void deleteById(Long id) {
        workLogMapper.deleteById(id);
    }
    
    /**
     * 获取本周工作日志
     */
    public List<WorkLog> getThisWeekLogs() {
        LocalDateTime now = LocalDateTime.now();
        
        // 获取本周周一的开始时间（00:00:00）
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        
        // 获取本周周日的结束时间（23:59:59）
        LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        return findByDateRange(startOfWeek, endOfWeek);
    }
    
    /**
     * 获取本月工作日志
     */
    public List<WorkLog> getThisMonthLogs() {
        LocalDateTime now = LocalDateTime.now();
        
        // 获取本月第一天的开始时间（00:00:00）
        LocalDateTime startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        
        // 获取本月最后一天的结束时间（23:59:59）
        LocalDateTime endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        return findByDateRange(startOfMonth, endOfMonth);
    }
}