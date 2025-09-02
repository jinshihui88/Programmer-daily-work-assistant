package com.programmer.assistant.controller;

import com.programmer.assistant.common.ApiResponse;
import com.programmer.assistant.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 报表控制器
 */
@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*")
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    /**
     * 生成周报
     */
    @GetMapping("/weekly")
    public ApiResponse<Map<String, Object>> generateWeeklyReport() {
        Map<String, Object> report = reportService.generateWeeklyReport();
        return ApiResponse.success("生成周报成功", report);
    }
    
    /**
     * 生成月报
     */
    @GetMapping("/monthly")
    public ApiResponse<Map<String, Object>> generateMonthlyReport() {
        Map<String, Object> report = reportService.generateMonthlyReport();
        return ApiResponse.success("生成月报成功", report);
    }
    
    /**
     * 获取仪表板数据
     */
    @GetMapping("/dashboard")
    public ApiResponse<Map<String, Object>> getDashboardData() {
        Map<String, Object> dashboard = reportService.generateDashboardData();
        return ApiResponse.success("获取仪表板数据成功", dashboard);
    }
}