package com.programmer.assistant.service;

import com.programmer.assistant.entity.LearningProgress;
import com.programmer.assistant.entity.PomodoroSession;
import com.programmer.assistant.entity.WorkLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表服务
 */
@Service
public class ReportService {
    
    @Autowired
    private WorkLogService workLogService;
    
    @Autowired
    private PomodoroSessionService pomodoroSessionService;
    
    @Autowired
    private LearningProgressService learningProgressService;
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 生成周报
     */
    public Map<String, Object> generateWeeklyReport() {
        Map<String, Object> report = new HashMap<>();
        
        // 时间范围
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(7);
        
        report.put("reportType", "周报");
        report.put("startDate", startDate.format(formatter));
        report.put("endDate", endDate.format(formatter));
        
        // 工作日志统计
        List<WorkLog> weeklyLogs = workLogService.findByDateRange(startDate, endDate);
        report.put("workLogCount", weeklyLogs.size());
        report.put("workLogs", weeklyLogs);
        
        // 番茄钟统计
        List<PomodoroSession> weeklySessions = pomodoroSessionService.findByDateRange(startDate, endDate);
        long completedSessions = weeklySessions.stream()
                .filter(session -> session.getStatus() == PomodoroSession.Status.COMPLETED)
                .count();
        int totalMinutes = weeklySessions.stream()
                .filter(session -> session.getStatus() == PomodoroSession.Status.COMPLETED)
                .mapToInt(PomodoroSession::getDurationMinutes)
                .sum();
        
        Map<String, Object> pomodoroStats = new HashMap<>();
        pomodoroStats.put("totalSessions", weeklySessions.size());
        pomodoroStats.put("completedSessions", completedSessions);
        pomodoroStats.put("totalMinutes", totalMinutes);
        pomodoroStats.put("totalHours", String.format("%.1f", totalMinutes / 60.0));
        report.put("pomodoroStats", pomodoroStats);
        
        // 学习进度统计
        List<LearningProgress> allProgress = learningProgressService.findAll();
        Map<String, Long> learningStats = learningProgressService.getStatusStatistics();
        report.put("learningStats", learningStats);
        report.put("totalLearningProjects", allProgress.size());
        
        return report;
    }
    
    /**
     * 生成月报
     */
    public Map<String, Object> generateMonthlyReport() {
        Map<String, Object> report = new HashMap<>();
        
        // 时间范围
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(30);
        
        report.put("reportType", "月报");
        report.put("startDate", startDate.format(formatter));
        report.put("endDate", endDate.format(formatter));
        
        // 工作日志统计
        List<WorkLog> monthlyLogs = workLogService.findByDateRange(startDate, endDate);
        report.put("workLogCount", monthlyLogs.size());
        
        // 按日期分组统计工作日志
        Map<String, Long> dailyLogCount = new HashMap<>();
        monthlyLogs.forEach(log -> {
            String date = log.getWorkDate().toString();
            dailyLogCount.put(date, dailyLogCount.getOrDefault(date, 0L) + 1);
        });
        report.put("dailyLogCount", dailyLogCount);
        
        // 番茄钟统计
        List<PomodoroSession> monthlySessions = pomodoroSessionService.findByDateRange(startDate, endDate);
        long completedSessions = monthlySessions.stream()
                .filter(session -> session.getStatus() == PomodoroSession.Status.COMPLETED)
                .count();
        int totalMinutes = monthlySessions.stream()
                .filter(session -> session.getStatus() == PomodoroSession.Status.COMPLETED)
                .mapToInt(PomodoroSession::getDurationMinutes)
                .sum();
        
        Map<String, Object> pomodoroStats = new HashMap<>();
        pomodoroStats.put("totalSessions", monthlySessions.size());
        pomodoroStats.put("completedSessions", completedSessions);
        pomodoroStats.put("totalMinutes", totalMinutes);
        pomodoroStats.put("totalHours", String.format("%.1f", totalMinutes / 60.0));
        pomodoroStats.put("averageDailyMinutes", String.format("%.1f", totalMinutes / 30.0));
        report.put("pomodoroStats", pomodoroStats);
        
        // 学习进度统计
        List<LearningProgress> allProgress = learningProgressService.findAll();
        Map<String, Long> learningStats = learningProgressService.getStatusStatistics();
        
        // 计算总学习时间
        int totalLearningHours = allProgress.stream()
                .mapToInt(progress -> progress.getCompletedHours() != null ? progress.getCompletedHours() : 0)
                .sum();
        
        Map<String, Object> learningStatsExtended = new HashMap<>(learningStats);
        learningStatsExtended.put("totalLearningHours", totalLearningHours);
        learningStatsExtended.put("totalProjects", allProgress.size());
        
        report.put("learningStats", learningStatsExtended);
        
        // 代码片段统计
        // 这里可以添加代码片段的统计信息
        
        return report;
    }
    
    /**
     * 生成仪表板数据
     */
    public Map<String, Object> generateDashboardData() {
        Map<String, Object> dashboard = new HashMap<>();
        
        // 今日数据
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime tomorrow = today.plusDays(1);
        
        List<WorkLog> todayLogs = workLogService.findByDateRange(today, tomorrow);
        dashboard.put("todayWorkLogs", todayLogs.size());
        
        Long todayPomodoros = pomodoroSessionService.getTodayCompletedCount();
        dashboard.put("todayPomodoros", todayPomodoros);
        
        // 最近数据
        dashboard.put("recentWorkLogs", workLogService.getRecentLogs());
        dashboard.put("recentPomodoros", pomodoroSessionService.getRecentSessions());
        
        // 学习进度
        dashboard.put("inProgressLearning", learningProgressService.getInProgressLearning());
        dashboard.put("learningStats", learningProgressService.getStatusStatistics());
        
        return dashboard;
    }
}