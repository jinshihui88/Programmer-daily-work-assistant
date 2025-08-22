package com.programmer.assistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programmer.assistant.entity.LearningProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 学习进度Mapper
 */
@Mapper
public interface LearningProgressMapper extends BaseMapper<LearningProgress> {
    
    /**
     * 根据状态查询
     */
    @Select("SELECT * FROM learning_progress WHERE status = #{status} ORDER BY create_time DESC")
    List<LearningProgress> findByStatus(@Param("status") String status);
    
    /**
     * 根据科目模糊查询
     */
    @Select("SELECT * FROM learning_progress WHERE subject LIKE CONCAT('%', #{subject}, '%') ORDER BY create_time DESC")
    List<LearningProgress> findBySubjectContaining(@Param("subject") String subject);
    
    /**
     * 获取进行中的学习项目
     */
    @Select("SELECT * FROM learning_progress WHERE status = 'IN_PROGRESS' ORDER BY create_time DESC")
    List<LearningProgress> findInProgressLearning();
    
    /**
     * 获取已完成的学习项目
     */
    @Select("SELECT * FROM learning_progress WHERE status = 'COMPLETED' ORDER BY create_time DESC")
    List<LearningProgress> findCompletedLearning();
}