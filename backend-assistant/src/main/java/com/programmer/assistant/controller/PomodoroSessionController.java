package com.programmer.assistant.controller;

import com.programmer.assistant.entity.PomodoroSession;
import com.programmer.assistant.service.PomodoroSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PomodoroSession> startSession(@RequestParam String task, 
                                                      @RequestParam(defaultValue = "25") Integer durationMinutes) {
        PomodoroSession session = pomodoroSessionService.startSession(task, durationMinutes);
        return ResponseEntity.ok(session);
    }
    
    /**
     * 根据ID获取番茄钟会话
     */
    @GetMapping("/{id}")
    public ResponseEntity<PomodoroSession> getPomodoroSessionById(@PathVariable Long id) {
        PomodoroSession session = pomodoroSessionService.findById(id);
        if (session != null) {
            return ResponseEntity.ok(session);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 完成番茄钟会话
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<PomodoroSession> completeSession(@PathVariable Long id) {
        PomodoroSession session = pomodoroSessionService.completeSession(id);
        if (session != null) {
            return ResponseEntity.ok(session);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 中断番茄钟会话
     */
    @PutMapping("/{id}/interrupt")
    public ResponseEntity<PomodoroSession> interruptSession(@PathVariable Long id) {
        PomodoroSession session = pomodoroSessionService.interruptSession(id);
        if (session != null) {
            return ResponseEntity.ok(session);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 获取所有番茄钟会话
     */
    @GetMapping
    public ResponseEntity<List<PomodoroSession>> getAllSessions() {
        List<PomodoroSession> sessions = pomodoroSessionService.findAll();
        return ResponseEntity.ok(sessions);
    }
    
    
    /**
     * 根据状态查询
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PomodoroSession>> getSessionsByStatus(@PathVariable PomodoroSession.Status status) {
        List<PomodoroSession> sessions = pomodoroSessionService.findByStatus(status);
        return ResponseEntity.ok(sessions);
    }
    
    /**
     * 获取今日完成的番茄钟数量
     */
    @GetMapping("/today/count")
    public ResponseEntity<Long> getTodayCompletedCount() {
        Long count = pomodoroSessionService.getTodayCompletedCount();
        return ResponseEntity.ok(count);
    }
    
    /**
     * 获取最近的番茄钟记录
     */
    @GetMapping("/recent")
    public ResponseEntity<List<PomodoroSession>> getRecentSessions() {
        List<PomodoroSession> sessions = pomodoroSessionService.getRecentSessions();
        return ResponseEntity.ok(sessions);
    }
    
    /**
     * 获取本周番茄钟记录
     */
    @GetMapping("/this-week")
    public ResponseEntity<List<PomodoroSession>> getThisWeekSessions() {
        List<PomodoroSession> sessions = pomodoroSessionService.getThisWeekSessions();
        return ResponseEntity.ok(sessions);
    }
    
    /**
     * 删除番茄钟会话
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        pomodoroSessionService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}