package com.programmer.assistant.controller;

import com.programmer.assistant.common.ApiResponse;
import com.programmer.assistant.entity.WorkLog;
import com.programmer.assistant.service.WorkLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
    public ApiResponse<WorkLog> createWorkLog(@RequestBody WorkLog workLog) {
        WorkLog savedLog = workLogService.save(workLog);
        return ApiResponse.success("工作日志创建成功", savedLog);
    }
    
    /**
     * 获取所有工作日志
     */
    @GetMapping
    public ApiResponse<List<WorkLog>> getAllWorkLogs() {
        List<WorkLog> logs = workLogService.findAll();
        return ApiResponse.success("获取工作日志列表成功", logs);
    }
    
    /**
     * 根据ID获取工作日志
     */
    @GetMapping("/{id}")
    public ApiResponse<WorkLog> getWorkLogById(@PathVariable Long id) {
        WorkLog log = workLogService.findById(id);
        if (log != null) {
            return ApiResponse.success("获取工作日志成功", log);
        }
        return ApiResponse.notFound("工作日志不存在");
    }
    
    /**
     * 更新工作日志
     */
    @PutMapping("/{id}")
    public ApiResponse<WorkLog> updateWorkLog(@PathVariable Long id, @RequestBody WorkLog workLog) {
        WorkLog existingLog = workLogService.findById(id);
        if (existingLog != null) {
            workLog.setId(id);
            WorkLog updatedLog = workLogService.save(workLog);
            return ApiResponse.success("工作日志更新成功", updatedLog);
        }
        return ApiResponse.notFound("工作日志不存在");
    }
    
    /**
     * 删除工作日志
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteWorkLog(@PathVariable Long id) {
        workLogService.deleteById(id);
        return ApiResponse.success("工作日志删除成功");
    }
    
    /**
     * 搜索工作日志
     */
    @GetMapping("/search")
    public ApiResponse<List<WorkLog>> searchWorkLogs(@RequestParam String title) {
        List<WorkLog> logs = workLogService.searchByTitle(title);
        return ApiResponse.success("搜索工作日志成功", logs);
    }
    
    /**
     * 获取最近的工作日志
     */
    @GetMapping("/recent")
    public ApiResponse<List<WorkLog>> getRecentWorkLogs() {
        List<WorkLog> logs = workLogService.getRecentLogs();
        return ApiResponse.success("获取最近工作日志成功", logs);
    }
    
    /**
     * 获取本周工作日志
     */
    @GetMapping("/this-week")
    public ApiResponse<List<WorkLog>> getThisWeekLogs() {
        List<WorkLog> logs = workLogService.getThisWeekLogs();
        return ApiResponse.success("获取本周工作日志成功", logs);
    }
    
    /**
     * 获取本月工作日志
     */
    @GetMapping("/this-month")
    public ApiResponse<List<WorkLog>> getThisMonthLogs() {
        List<WorkLog> logs = workLogService.getThisMonthLogs();
        return ApiResponse.success("获取本月工作日志成功", logs);
    }
}