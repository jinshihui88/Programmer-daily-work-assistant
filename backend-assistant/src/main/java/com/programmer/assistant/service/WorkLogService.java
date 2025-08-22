package com.programmer.assistant.service;

import com.programmer.assistant.entity.WorkLog;
import com.programmer.assistant.mapper.WorkLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        LocalDateTime startOfWeek = LocalDateTime.now().minusDays(7);
        LocalDateTime endOfWeek = LocalDateTime.now();
        return findByDateRange(startOfWeek, endOfWeek);
    }
    
    /**
     * 获取本月工作日志
     */
    public List<WorkLog> getThisMonthLogs() {
        LocalDateTime startOfMonth = LocalDateTime.now().minusDays(30);
        LocalDateTime endOfMonth = LocalDateTime.now();
        return findByDateRange(startOfMonth, endOfMonth);
    }
}