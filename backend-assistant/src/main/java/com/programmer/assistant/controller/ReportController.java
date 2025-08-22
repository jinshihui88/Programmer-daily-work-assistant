package com.programmer.assistant.controller;

import com.programmer.assistant.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> generateWeeklyReport() {
        Map<String, Object> report = reportService.generateWeeklyReport();
        return ResponseEntity.ok(report);
    }
    
    /**
     * 生成月报
     */
    @GetMapping("/monthly")
    public ResponseEntity<Map<String, Object>> generateMonthlyReport() {
        Map<String, Object> report = reportService.generateMonthlyReport();
        return ResponseEntity.ok(report);
    }
    
    /**
     * 获取仪表板数据
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> dashboard = reportService.generateDashboardData();
        return ResponseEntity.ok(dashboard);
    }
}