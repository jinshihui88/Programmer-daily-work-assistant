<template>
  <div class="code-snippets">
    <div class="page-header">
      <h2 class="page-title">
        <el-icon><Collection /></el-icon>
        代码片段收藏夹
      </h2>
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        新建片段
      </el-button>
    </div>

    <!-- 搜索和筛选 -->
    <el-card class="filter-card">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input
            v-model="searchTitle"
            placeholder="搜索代码片段"
            clearable
            @input="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-select v-model="languageFilter" placeholder="编程语言" @change="handleLanguageFilter">
            <el-option label="全部语言" value="" />
            <el-option 
              v-for="lang in languages" 
              :key="lang" 
              :label="lang" 
              :value="lang" 
            />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input
            v-model="tagFilter"
            placeholder="按标签筛选"
            clearable
            @input="handleTagFilter"
          >
            <template #prefix>
              <el-icon><PriceTag /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-button @click="loadCodeSnippets">刷新</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 代码片段列表 -->
    <el-card class="snippets-card">
      <div v-if="loading" class="loading-state">
        <el-skeleton :rows="5" animated />
      </div>
      
      <div v-else-if="codeSnippets.length === 0" class="empty-state">
        <el-empty description="暂无代码片段">
          <el-button type="primary" @click="showCreateDialog = true">
            创建第一个代码片段
          </el-button>
        </el-empty>
      </div>
      
      <div v-else class="snippets-grid">
        <div 
          v-for="snippet in codeSnippets" 
          :key="snippet.id" 
          class="snippet-card"
        >
          <div class="snippet-header">
            <div class="snippet-title">{{ snippet.title }}</div>
            <div class="snippet-actions">
              <el-button type="text" @click="copyCode(snippet.code)">
                <el-icon><CopyDocument /></el-icon>
                复制
              </el-button>
              <el-button type="text" @click="editSnippet(snippet)">
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button type="text" @click="deleteSnippet(snippet.id)" class="delete-btn">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </div>
          
          <div class="snippet-meta">
            <el-tag size="small" type="info">{{ snippet.language }}</el-tag>
            <span class="snippet-date">{{ formatDate(snippet.createTime) }}</span>
          </div>
          
          <div v-if="snippet.description" class="snippet-description">
            {{ snippet.description }}
          </div>
          
          <div class="snippet-code">
            <pre><code>{{ snippet.code }}</code></pre>
          </div>
          
          <div v-if="snippet.tags" class="snippet-tags">
            <el-tag 
              v-for="tag in snippet.tags.split(',')" 
              :key="tag" 
              size="small"
              class="tag-item"
            >
              {{ tag.trim() }}
            </el-tag>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 创建/编辑对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="editingSnippet ? '编辑代码片段' : '新建代码片段'"
      width="800px"
      @close="resetForm"
    >
      <el-form :model="snippetForm" :rules="snippetRules" ref="snippetFormRef" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="snippetForm.title" placeholder="请输入片段标题" />
        </el-form-item>
        <el-form-item label="编程语言" prop="language">
          <el-select v-model="snippetForm.language" placeholder="选择编程语言" filterable allow-create>
            <el-option label="JavaScript" value="javascript" />
            <el-option label="Python" value="python" />
            <el-option label="Java" value="java" />
            <el-option label="C++" value="cpp" />
            <el-option label="HTML" value="html" />
            <el-option label="CSS" value="css" />
            <el-option label="SQL" value="sql" />
            <el-option label="Shell" value="shell" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="snippetForm.description"
            type="textarea"
            :rows="2"
            placeholder="请输入片段描述（可选）"
          />
        </el-form-item>
        <el-form-item label="代码" prop="code">
          <el-input
            v-model="snippetForm.code"
            type="textarea"
            :rows="10"
            placeholder="请输入代码内容"
          />
        </el-form-item>
        <el-form-item label="标签">
          <el-input
            v-model="snippetForm.tags"
            placeholder="请输入标签，用逗号分隔"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="saveSnippet" :loading="saving">
          {{ editingSnippet ? '更新' : '创建' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { codeSnippetApi } from '../api'
import { 
  Collection, 
  Plus, 
  Search, 
  PriceTag,
  CopyDocument,
  Edit, 
  Delete
} from '@element-plus/icons-vue'

export default {
  name: 'CodeSnippets',
  components: {
    Collection,
    Plus,
    Search,
    PriceTag,
    CopyDocument,
    Edit,
    Delete
  },
  setup() {
    const codeSnippets = ref([])
    const languages = ref([])
    const loading = ref(false)
    const saving = ref(false)
    const showCreateDialog = ref(false)
    const editingSnippet = ref(null)
    const searchTitle = ref('')
    const languageFilter = ref('')
    const tagFilter = ref('')
    const snippetFormRef = ref()

    const snippetForm = reactive({
      title: '',
      language: '',
      description: '',
      code: '',
      tags: ''
    })

    const snippetRules = {
      title: [
        { required: true, message: '请输入片段标题', trigger: 'blur' }
      ],
      language: [
        { required: true, message: '请选择编程语言', trigger: 'change' }
      ],
      code: [
        { required: true, message: '请输入代码内容', trigger: 'blur' }
      ]
    }

    const loadCodeSnippets = async () => {
      loading.value = true
      try {
        const data = await codeSnippetApi.getAll()
        codeSnippets.value = data
      } catch (error) {
        ElMessage.error('加载代码片段失败')
        console.error(error)
      } finally {
        loading.value = false
      }
    }

    const loadLanguages = async () => {
      try {
        const data = await codeSnippetApi.getLanguages()
        languages.value = data
      } catch (error) {
        console.error('加载编程语言失败:', error)
      }
    }

    const handleSearch = async () => {
      if (searchTitle.value.trim()) {
        try {
          const data = await codeSnippetApi.search(searchTitle.value)
          codeSnippets.value = data
        } catch (error) {
          ElMessage.error('搜索失败')
        }
      } else {
        loadCodeSnippets()
      }
    }

    const handleLanguageFilter = async () => {
      if (languageFilter.value) {
        try {
          const data = await codeSnippetApi.getByLanguage(languageFilter.value)
          codeSnippets.value = data
        } catch (error) {
          ElMessage.error('筛选失败')
        }
      } else {
        loadCodeSnippets()
      }
    }

    const handleTagFilter = async () => {
      if (tagFilter.value.trim()) {
        try {
          const data = await codeSnippetApi.getByTag(tagFilter.value)
          codeSnippets.value = data
        } catch (error) {
          ElMessage.error('标签筛选失败')
        }
      } else {
        loadCodeSnippets()
      }
    }

    const copyCode = async (code) => {
      try {
        await navigator.clipboard.writeText(code)
        ElMessage.success('代码已复制到剪贴板')
      } catch (error) {
        ElMessage.error('复制失败')
      }
    }

    const editSnippet = (snippet) => {
      editingSnippet.value = snippet
      snippetForm.title = snippet.title
      snippetForm.language = snippet.language
      snippetForm.description = snippet.description || ''
      snippetForm.code = snippet.code
      snippetForm.tags = snippet.tags || ''
      showCreateDialog.value = true
    }

    const saveSnippet = async () => {
      if (!snippetFormRef.value) return
      
      const valid = await snippetFormRef.value.validate().catch(() => false)
      if (!valid) return

      saving.value = true
      try {
        if (editingSnippet.value) {
          await codeSnippetApi.update(editingSnippet.value.id, snippetForm)
          ElMessage.success('代码片段更新成功')
        } else {
          await codeSnippetApi.create(snippetForm)
          ElMessage.success('代码片段创建成功')
        }
        showCreateDialog.value = false
        loadCodeSnippets()
        loadLanguages()
      } catch (error) {
        ElMessage.error(editingSnippet.value ? '更新失败' : '创建失败')
        console.error(error)
      } finally {
        saving.value = false
      }
    }

    const deleteSnippet = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除这个代码片段吗？', '确认删除', {
          type: 'warning'
        })
        
        await codeSnippetApi.delete(id)
        ElMessage.success('删除成功')
        loadCodeSnippets()
        loadLanguages()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    }

    const resetForm = () => {
      editingSnippet.value = null
      snippetForm.title = ''
      snippetForm.language = ''
      snippetForm.description = ''
      snippetForm.code = ''
      snippetForm.tags = ''
      if (snippetFormRef.value) {
        snippetFormRef.value.resetFields()
      }
    }

    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }

    onMounted(() => {
      loadCodeSnippets()
      loadLanguages()
    })

    return {
      codeSnippets,
      languages,
      loading,
      saving,
      showCreateDialog,
      editingSnippet,
      searchTitle,
      languageFilter,
      tagFilter,
      snippetForm,
      snippetRules,
      snippetFormRef,
      loadCodeSnippets,
      handleSearch,
      handleLanguageFilter,
      handleTagFilter,
      copyCode,
      editSnippet,
      saveSnippet,
      deleteSnippet,
      resetForm,
      formatDate
    }
  }
}
</script>

<style scoped>
.code-snippets {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
  margin: 0;
}

.filter-card {
  margin-bottom: 20px;
}

.snippets-card {
  min-height: 400px;
}

.loading-state, .empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}

.snippets-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
}

.snippet-card {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 16px;
  background: white;
  transition: box-shadow 0.2s;
}

.snippet-card:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.snippet-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}

.snippet-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  flex: 1;
}

.snippet-actions {
  display: flex;
  gap: 5px;
}

.delete-btn {
  color: #f56c6c;
}

.snippet-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.snippet-date {
  font-size: 12px;
  color: #909399;
}

.snippet-description {
  color: #606266;
  font-size: 14px;
  margin-bottom: 10px;
  line-height: 1.5;
}

.snippet-code {
  background: #f5f7fa;
  border-radius: 4px;
  padding: 12px;
  margin-bottom: 10px;
  max-height: 200px;
  overflow-y: auto;
}

.snippet-code pre {
  margin: 0;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 12px;
  line-height: 1.4;
  color: #303133;
  white-space: pre-wrap;
  word-break: break-all;
}

.snippet-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.tag-item {
  font-size: 12px;
}
</style>