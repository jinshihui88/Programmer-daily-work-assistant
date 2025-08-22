package com.programmer.assistant.controller;

import com.programmer.assistant.entity.LearningProgress;
import com.programmer.assistant.service.LearningProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 学习进度控制器
 */
@RestController
@RequestMapping("/learning-progress")
@CrossOrigin(origins = "*")
public class LearningProgressController {
    
    @Autowired
    private LearningProgressService learningProgressService;
    
    /**
     * 创建学习进度
     */
    @PostMapping
    public ResponseEntity<LearningProgress> createLearningProgress(@RequestBody LearningProgress learningProgress) {
        LearningProgress savedProgress = learningProgressService.save(learningProgress);
        return ResponseEntity.ok(savedProgress);
    }
    
    /**
     * 获取所有学习进度
     */
    @GetMapping
    public ResponseEntity<List<LearningProgress>> getAllLearningProgress() {
        List<LearningProgress> progressList = learningProgressService.findAll();
        return ResponseEntity.ok(progressList);
    }
    
    /**
     * 根据ID获取学习进度
     */
    @GetMapping("/{id}")
    public ResponseEntity<LearningProgress> getLearningProgressById(@PathVariable Long id) {
        LearningProgress progress = learningProgressService.findById(id);
        if (progress != null) {
            return ResponseEntity.ok(progress);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 更新学习进度
     */
    @PutMapping("/{id}")
    public ResponseEntity<LearningProgress> updateLearningProgress(@PathVariable Long id, @RequestBody LearningProgress learningProgress) {
        LearningProgress existingProgress = learningProgressService.findById(id);
        if (existingProgress != null) {
            learningProgress.setId(id);
            LearningProgress updatedProgress = learningProgressService.save(learningProgress);
            return ResponseEntity.ok(updatedProgress);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 更新完成小时数
     */
    @PutMapping("/{id}/hours")
    public ResponseEntity<LearningProgress> updateCompletedHours(@PathVariable Long id, @RequestParam Integer hours) {
        LearningProgress updatedProgress = learningProgressService.updateProgress(id, hours);
        if (updatedProgress != null) {
            return ResponseEntity.ok(updatedProgress);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 删除学习进度
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLearningProgress(@PathVariable Long id) {
        learningProgressService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 根据状态查询
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LearningProgress>> getLearningProgressByStatus(@PathVariable LearningProgress.Status status) {
        List<LearningProgress> progressList = learningProgressService.findByStatus(status);
        return ResponseEntity.ok(progressList);
    }
    
    /**
     * 搜索学习进度
     */
    @GetMapping("/search")
    public ResponseEntity<List<LearningProgress>> searchLearningProgress(@RequestParam String subject) {
        List<LearningProgress> progressList = learningProgressService.searchBySubject(subject);
        return ResponseEntity.ok(progressList);
    }
    
    /**
     * 获取进行中的学习项目
     */
    @GetMapping("/in-progress")
    public ResponseEntity<List<LearningProgress>> getInProgressLearning() {
        List<LearningProgress> progressList = learningProgressService.getInProgressLearning();
        return ResponseEntity.ok(progressList);
    }
    
    /**
     * 获取状态统计
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getStatusStatistics() {
        Map<String, Long> statistics = learningProgressService.getStatusStatistics();
        return ResponseEntity.ok(statistics);
    }
}