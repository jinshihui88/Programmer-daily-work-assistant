package com.programmer.assistant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programmer.assistant.entity.LearningProgress;
import com.programmer.assistant.mapper.LearningProgressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习进度服务
 */
@Service
public class LearningProgressService {
    
    @Autowired
    private LearningProgressMapper learningProgressMapper;
    
    /**
     * 保存学习进度
     */
    public LearningProgress save(LearningProgress learningProgress) {
        if (learningProgress.getId() == null) {
            learningProgressMapper.insert(learningProgress);
        } else {
            learningProgressMapper.updateById(learningProgress);
        }
        return learningProgress;
    }
    
    /**
     * 根据ID查询
     */
    public LearningProgress findById(Long id) {
        return learningProgressMapper.selectById(id);
    }
    
    /**
     * 查询所有学习进度
     */
    public List<LearningProgress> findAll() {
        return learningProgressMapper.selectList(null);
    }
    
    /**
     * 根据状态查询
     */
    public List<LearningProgress> findByStatus(LearningProgress.Status status) {
        return learningProgressMapper.findByStatus(status.name());
    }
    
    /**
     * 根据主题搜索
     */
    public List<LearningProgress> searchBySubject(String subject) {
        return learningProgressMapper.findBySubjectContaining(subject);
    }
    
    /**
     * 获取进行中的学习项目
     */
    public List<LearningProgress> getInProgressLearning() {
        return learningProgressMapper.findInProgressLearning();
    }
    
    /**
     * 更新学习进度
     */
    public LearningProgress updateProgress(Long id, Integer completedHours) {
        LearningProgress progress = findById(id);
        if (progress != null) {
            progress.setCompletedHours(completedHours);
            
            // 自动更新状态
            if (completedHours > 0 && progress.getStatus() == LearningProgress.Status.NOT_STARTED) {
                progress.setStatus(LearningProgress.Status.IN_PROGRESS);
            } else if (completedHours.equals(progress.getTotalHours())) {
                progress.setStatus(LearningProgress.Status.COMPLETED);
            }
            
            return save(progress);
        }
        return null;
    }
    
    /**
     * 获取状态统计
     */
    public Map<String, Long> getStatusStatistics() {
        QueryWrapper<LearningProgress> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("status, count(*) as count").groupBy("status");
        List<Map<String, Object>> results = learningProgressMapper.selectMaps(queryWrapper);
        
        Map<String, Long> statistics = new HashMap<>();
        for (Map<String, Object> result : results) {
            String status = (String) result.get("status");
            Long count = (Long) result.get("count");
            statistics.put(status, count);
        }
        
        return statistics;
    }
    
    /**
     * 删除学习进度
     */
    public void deleteById(Long id) {
        learningProgressMapper.deleteById(id);
    }
}
