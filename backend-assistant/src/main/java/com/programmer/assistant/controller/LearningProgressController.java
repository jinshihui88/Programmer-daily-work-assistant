package com.programmer.assistant.controller;

import com.programmer.assistant.common.ApiResponse;
import com.programmer.assistant.entity.LearningProgress;
import com.programmer.assistant.service.LearningProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ApiResponse<LearningProgress> createLearningProgress(@RequestBody LearningProgress learningProgress) {
        LearningProgress savedProgress = learningProgressService.save(learningProgress);
        return ApiResponse.success("学习进度创建成功", savedProgress);
    }
    
    /**
     * 获取所有学习进度
     */
    @GetMapping
    public ApiResponse<List<LearningProgress>> getAllLearningProgress() {
        List<LearningProgress> progressList = learningProgressService.findAll();
        return ApiResponse.success("获取学习进度列表成功", progressList);
    }
    
    /**
     * 根据ID获取学习进度
     */
    @GetMapping("/{id}")
    public ApiResponse<LearningProgress> getLearningProgressById(@PathVariable Long id) {
        LearningProgress progress = learningProgressService.findById(id);
        if (progress != null) {
            return ApiResponse.success("获取学习进度成功", progress);
        }
        return ApiResponse.notFound("学习进度不存在");
    }
    
    /**
     * 更新学习进度
     */
    @PutMapping("/{id}")
    public ApiResponse<LearningProgress> updateLearningProgress(@PathVariable Long id, @RequestBody LearningProgress learningProgress) {
        LearningProgress existingProgress = learningProgressService.findById(id);
        if (existingProgress != null) {
            learningProgress.setId(id);
            LearningProgress updatedProgress = learningProgressService.save(learningProgress);
            return ApiResponse.success("学习进度更新成功", updatedProgress);
        }
        return ApiResponse.notFound("学习进度不存在");
    }
    
    /**
     * 更新完成小时数
     */
    @PutMapping("/{id}/hours")
    public ApiResponse<LearningProgress> updateCompletedHours(@PathVariable Long id, @RequestParam Integer hours) {
        LearningProgress updatedProgress = learningProgressService.updateProgress(id, hours);
        if (updatedProgress != null) {
            return ApiResponse.success("学习时长更新成功", updatedProgress);
        }
        return ApiResponse.notFound("学习进度不存在");
    }
    
    /**
     * 删除学习进度
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteLearningProgress(@PathVariable Long id) {
        learningProgressService.deleteById(id);
        return ApiResponse.success("学习进度删除成功");
    }
    
    /**
     * 根据状态查询
     */
    @GetMapping("/status/{status}")
    public ApiResponse<List<LearningProgress>> getLearningProgressByStatus(@PathVariable LearningProgress.Status status) {
        List<LearningProgress> progressList = learningProgressService.findByStatus(status);
        return ApiResponse.success("根据状态查询成功", progressList);
    }
    
    /**
     * 搜索学习进度
     */
    @GetMapping("/search")
    public ApiResponse<List<LearningProgress>> searchLearningProgress(@RequestParam String subject) {
        List<LearningProgress> progressList = learningProgressService.searchBySubject(subject);
        return ApiResponse.success("搜索学习进度成功", progressList);
    }
    
    /**
     * 获取进行中的学习项目
     */
    @GetMapping("/in-progress")
    public ApiResponse<List<LearningProgress>> getInProgressLearning() {
        List<LearningProgress> progressList = learningProgressService.getInProgressLearning();
        return ApiResponse.success("获取进行中学习项目成功", progressList);
    }
    
    /**
     * 获取状态统计
     */
    @GetMapping("/statistics")
    public ApiResponse<Map<String, Long>> getStatusStatistics() {
        Map<String, Long> statistics = learningProgressService.getStatusStatistics();
        return ApiResponse.success("获取状态统计成功", statistics);
    }
}