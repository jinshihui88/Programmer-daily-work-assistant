package com.programmer.assistant.controller;

import com.programmer.assistant.common.ApiResponse;
import com.programmer.assistant.entity.PomodoroSession;
import com.programmer.assistant.service.PomodoroSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 番茄钟会话控制器
 */
@RestController
@RequestMapping("/pomodoro-sessions")
@CrossOrigin(origins = "*")
public class PomodoroSessionController {
    
    @Autowired
    private PomodoroSessionService pomodoroSessionService;
    
    /**
     * 开始新的番茄钟会话
     */
    @PostMapping("/start")
    public ApiResponse<PomodoroSession> startSession(@RequestParam String task, 
                                                      @RequestParam(defaultValue = "25") Integer durationMinutes) {
        PomodoroSession session = pomodoroSessionService.startSession(task, durationMinutes);
        return ApiResponse.success("番茄钟会话开始成功", session);
    }
    
    /**
     * 根据ID获取番茄钟会话
     */
    @GetMapping("/{id}")
    public ApiResponse<PomodoroSession> getPomodoroSessionById(@PathVariable Long id) {
        PomodoroSession session = pomodoroSessionService.findById(id);
        if (session != null) {
            return ApiResponse.success("获取番茄钟会话成功", session);
        }
        return ApiResponse.notFound("番茄钟会话不存在");
    }
    
    /**
     * 完成番茄钟会话
     */
    @PutMapping("/{id}/complete")
    public ApiResponse<PomodoroSession> completeSession(@PathVariable Long id) {
        PomodoroSession session = pomodoroSessionService.completeSession(id);
        if (session != null) {
            return ApiResponse.success("番茄钟会话完成成功", session);
        }
        return ApiResponse.notFound("番茄钟会话不存在");
    }
    
    /**
     * 中断番茄钟会话
     */
    @PutMapping("/{id}/interrupt")
    public ApiResponse<PomodoroSession> interruptSession(@PathVariable Long id) {
        PomodoroSession session = pomodoroSessionService.interruptSession(id);
        if (session != null) {
            return ApiResponse.success("番茄钟会话中断成功", session);
        }
        return ApiResponse.notFound("番茄钟会话不存在");
    }
    
    /**
     * 获取所有番茄钟会话
     */
    @GetMapping
    public ApiResponse<List<PomodoroSession>> getAllSessions() {
        List<PomodoroSession> sessions = pomodoroSessionService.findAll();
        return ApiResponse.success("获取番茄钟会话列表成功", sessions);
    }
    
    
    /**
     * 根据状态查询
     */
    @GetMapping("/status/{status}")
    public ApiResponse<List<PomodoroSession>> getSessionsByStatus(@PathVariable PomodoroSession.Status status) {
        List<PomodoroSession> sessions = pomodoroSessionService.findByStatus(status);
        return ApiResponse.success("根据状态查询成功", sessions);
    }
    
    /**
     * 获取今日完成的番茄钟数量
     */
    @GetMapping("/today/count")
    public ApiResponse<Long> getTodayCompletedCount() {
        Long count = pomodoroSessionService.getTodayCompletedCount();
        return ApiResponse.success("获取今日番茄钟数量成功", count);
    }
    
    /**
     * 获取最近的番茄钟记录
     */
    @GetMapping("/recent")
    public ApiResponse<List<PomodoroSession>> getRecentSessions() {
        List<PomodoroSession> sessions = pomodoroSessionService.getRecentSessions();
        return ApiResponse.success("获取最近番茄钟记录成功", sessions);
    }
    
    /**
     * 获取本周番茄钟记录
     */
    @GetMapping("/this-week")
    public ApiResponse<List<PomodoroSession>> getThisWeekSessions() {
        List<PomodoroSession> sessions = pomodoroSessionService.getThisWeekSessions();
        return ApiResponse.success("获取本周番茄钟记录成功", sessions);
    }
    
    /**
     * 删除番茄钟会话
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteSession(@PathVariable Long id) {
        pomodoroSessionService.deleteById(id);
        return ApiResponse.success("番茄钟会话删除成功");
    }
}