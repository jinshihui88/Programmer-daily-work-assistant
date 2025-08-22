package com.programmer.assistant.controller;

import com.programmer.assistant.entity.CodeSnippet;
import com.programmer.assistant.service.CodeSnippetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<CodeSnippet> createCodeSnippet(@RequestBody CodeSnippet codeSnippet) {
        CodeSnippet savedSnippet = codeSnippetService.save(codeSnippet);
        return ResponseEntity.ok(savedSnippet);
    }
    
    /**
     * 获取所有代码片段
     */
    @GetMapping
    public ResponseEntity<List<CodeSnippet>> getAllCodeSnippets() {
        List<CodeSnippet> snippets = codeSnippetService.findAll();
        return ResponseEntity.ok(snippets);
    }
    
    /**
     * 根据ID获取代码片段
     */
    @GetMapping("/{id}")
    public ResponseEntity<CodeSnippet> getCodeSnippetById(@PathVariable Long id) {
        CodeSnippet snippet = codeSnippetService.findById(id);
        if (snippet != null) {
            return ResponseEntity.ok(snippet);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 更新代码片段
     */
    @PutMapping("/{id}")
    public ResponseEntity<CodeSnippet> updateCodeSnippet(@PathVariable Long id, @RequestBody CodeSnippet codeSnippet) {
        CodeSnippet existingSnippet = codeSnippetService.findById(id);
        if (existingSnippet != null) {
            codeSnippet.setId(id);
            CodeSnippet updatedSnippet = codeSnippetService.save(codeSnippet);
            return ResponseEntity.ok(updatedSnippet);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 删除代码片段
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCodeSnippet(@PathVariable Long id) {
        codeSnippetService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 根据编程语言查询
     */
    @GetMapping("/language/{language}")
    public ResponseEntity<List<CodeSnippet>> getCodeSnippetsByLanguage(@PathVariable String language) {
        List<CodeSnippet> snippets = codeSnippetService.findByLanguage(language);
        return ResponseEntity.ok(snippets);
    }
    
    /**
     * 搜索代码片段
     */
    @GetMapping("/search")
    public ResponseEntity<List<CodeSnippet>> searchCodeSnippets(@RequestParam String title) {
        List<CodeSnippet> snippets = codeSnippetService.searchByTitle(title);
        return ResponseEntity.ok(snippets);
    }
    
    /**
     * 根据标签搜索
     */
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<CodeSnippet>> getCodeSnippetsByTag(@PathVariable String tag) {
        List<CodeSnippet> snippets = codeSnippetService.searchByTag(tag);
        return ResponseEntity.ok(snippets);
    }
    
    /**
     * 获取所有编程语言
     */
    @GetMapping("/languages")
    public ResponseEntity<List<String>> getAllLanguages() {
        List<String> languages = codeSnippetService.getAllLanguages();
        return ResponseEntity.ok(languages);
    }
}