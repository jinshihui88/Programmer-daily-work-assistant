package com.programmer.assistant.controller;

import com.programmer.assistant.entity.WorkLog;
import com.programmer.assistant.service.WorkLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 工作日志控制器
 */
@RestController
@RequestMapping("/work-logs")
@CrossOrigin(origins = "*")
public class WorkLogController {
    
    @Autowired
    private WorkLogService workLogService;
    
    /**
     * 创建工作日志
     */
    @PostMapping
    public ResponseEntity<WorkLog> createWorkLog(@RequestBody WorkLog workLog) {
        WorkLog savedLog = workLogService.save(workLog);
        return ResponseEntity.ok(savedLog);
    }
    
    /**
     * 获取所有工作日志
     */
    @GetMapping
    public ResponseEntity<List<WorkLog>> getAllWorkLogs() {
        List<WorkLog> logs = workLogService.findAll();
        return ResponseEntity.ok(logs);
    }
    
    /**
     * 根据ID获取工作日志
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkLog> getWorkLogById(@PathVariable Long id) {
        WorkLog log = workLogService.findById(id);
        if (log != null) {
            return ResponseEntity.ok(log);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 更新工作日志
     */
    @PutMapping("/{id}")
    public ResponseEntity<WorkLog> updateWorkLog(@PathVariable Long id, @RequestBody WorkLog workLog) {
        WorkLog existingLog = workLogService.findById(id);
        if (existingLog != null) {
            workLog.setId(id);
            WorkLog updatedLog = workLogService.save(workLog);
            return ResponseEntity.ok(updatedLog);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 删除工作日志
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkLog(@PathVariable Long id) {
        workLogService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 搜索工作日志
     */
    @GetMapping("/search")
    public ResponseEntity<List<WorkLog>> searchWorkLogs(@RequestParam String title) {
        List<WorkLog> logs = workLogService.searchByTitle(title);
        return ResponseEntity.ok(logs);
    }
    
    /**
     * 获取最近的工作日志
     */
    @GetMapping("/recent")
    public ResponseEntity<List<WorkLog>> getRecentWorkLogs() {
        List<WorkLog> logs = workLogService.getRecentLogs();
        return ResponseEntity.ok(logs);
    }
    
    /**
     * 获取本周工作日志
     */
    @GetMapping("/this-week")
    public ResponseEntity<List<WorkLog>> getThisWeekLogs() {
        List<WorkLog> logs = workLogService.getThisWeekLogs();
        return ResponseEntity.ok(logs);
    }
    
    /**
     * 获取本月工作日志
     */
    @GetMapping("/this-month")
    public ResponseEntity<List<WorkLog>> getThisMonthLogs() {
        List<WorkLog> logs = workLogService.getThisMonthLogs();
        return ResponseEntity.ok(logs);
    }
}