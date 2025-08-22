package com.programmer.assistant.service;

import com.programmer.assistant.entity.CodeSnippet;
import com.programmer.assistant.mapper.CodeSnippetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 代码片段服务
 */
@Service
public class CodeSnippetService {
    
    @Autowired
    private CodeSnippetMapper codeSnippetMapper;
    
    /**
     * 保存代码片段
     */
    public CodeSnippet save(CodeSnippet codeSnippet) {
        if (codeSnippet.getId() == null) {
            codeSnippetMapper.insert(codeSnippet);
        } else {
            codeSnippetMapper.updateById(codeSnippet);
        }
        return codeSnippet;
    }
    
    /**
     * 根据ID查询
     */
    public CodeSnippet findById(Long id) {
        return codeSnippetMapper.selectById(id);
    }
    
    /**
     * 查询所有代码片段
     */
    public List<CodeSnippet> findAll() {
        return codeSnippetMapper.selectList(null);
    }
    
    /**
     * 根据编程语言查询
     */
    public List<CodeSnippet> findByLanguage(String language) {
        return codeSnippetMapper.findByLanguage(language);
    }
    
    /**
     * 根据标题搜索
     */
    public List<CodeSnippet> searchByTitle(String title) {
        return codeSnippetMapper.findByTitleContaining(title);
    }
    
    /**
     * 根据标签搜索
     */
    public List<CodeSnippet> searchByTag(String tag) {
        return codeSnippetMapper.findByTagsContaining(tag);
    }
    
    /**
     * 获取所有编程语言
     */
    public List<String> getAllLanguages() {
        return codeSnippetMapper.findAllLanguages();
    }
    
    /**
     * 删除代码片段
     */
    public void deleteById(Long id) {
        codeSnippetMapper.deleteById(id);
    }
}
