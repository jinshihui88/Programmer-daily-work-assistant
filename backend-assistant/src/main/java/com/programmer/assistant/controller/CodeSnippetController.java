package com.programmer.assistant.controller;

import com.programmer.assistant.common.ApiResponse;
import com.programmer.assistant.entity.CodeSnippet;
import com.programmer.assistant.service.CodeSnippetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代码片段控制器
 */
@RestController
@RequestMapping("/code-snippets")
@CrossOrigin(origins = "*")
public class CodeSnippetController {
    
    @Autowired
    private CodeSnippetService codeSnippetService;
    
    /**
     * 创建代码片段
     */
    @PostMapping
    public ApiResponse<CodeSnippet> createCodeSnippet(@RequestBody CodeSnippet codeSnippet) {
        CodeSnippet savedSnippet = codeSnippetService.save(codeSnippet);
        return ApiResponse.success("代码片段创建成功", savedSnippet);
    }
    
    /**
     * 获取所有代码片段
     */
    @GetMapping
    public ApiResponse<List<CodeSnippet>> getAllCodeSnippets() {
        List<CodeSnippet> snippets = codeSnippetService.findAll();
        return ApiResponse.success("获取代码片段列表成功", snippets);
    }
    
    /**
     * 根据ID获取代码片段
     */
    @GetMapping("/{id}")
    public ApiResponse<CodeSnippet> getCodeSnippetById(@PathVariable Long id) {
        CodeSnippet snippet = codeSnippetService.findById(id);
        if (snippet != null) {
            return ApiResponse.success("获取代码片段成功", snippet);
        }
        return ApiResponse.notFound("代码片段不存在");
    }
    
    /**
     * 更新代码片段
     */
    @PutMapping("/{id}")
    public ApiResponse<CodeSnippet> updateCodeSnippet(@PathVariable Long id, @RequestBody CodeSnippet codeSnippet) {
        CodeSnippet existingSnippet = codeSnippetService.findById(id);
        if (existingSnippet != null) {
            codeSnippet.setId(id);
            CodeSnippet updatedSnippet = codeSnippetService.save(codeSnippet);
            return ApiResponse.success("代码片段更新成功", updatedSnippet);
        }
        return ApiResponse.notFound("代码片段不存在");
    }
    
    /**
     * 删除代码片段
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCodeSnippet(@PathVariable Long id) {
        codeSnippetService.deleteById(id);
        return ApiResponse.success("代码片段删除成功");
    }
    
    /**
     * 根据编程语言查询
     */
    @GetMapping("/language/{language}")
    public ApiResponse<List<CodeSnippet>> getCodeSnippetsByLanguage(@PathVariable String language) {
        List<CodeSnippet> snippets = codeSnippetService.findByLanguage(language);
        return ApiResponse.success("根据编程语言查询成功", snippets);
    }
    
    /**
     * 搜索代码片段
     */
    @GetMapping("/search")
    public ApiResponse<List<CodeSnippet>> searchCodeSnippets(@RequestParam String title) {
        List<CodeSnippet> snippets = codeSnippetService.searchByTitle(title);
        return ApiResponse.success("搜索代码片段成功", snippets);
    }
    
    /**
     * 根据标签搜索
     */
    @GetMapping("/tag/{tag}")
    public ApiResponse<List<CodeSnippet>> getCodeSnippetsByTag(@PathVariable String tag) {
        List<CodeSnippet> snippets = codeSnippetService.searchByTag(tag);
        return ApiResponse.success("根据标签搜索成功", snippets);
    }
    
    /**
     * 获取所有编程语言
     */
    @GetMapping("/languages")
    public ApiResponse<List<String>> getAllLanguages() {
        List<String> languages = codeSnippetService.getAllLanguages();
        return ApiResponse.success("获取编程语言列表成功", languages);
    }
}