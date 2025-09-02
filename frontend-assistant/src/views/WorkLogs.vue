<template>
  <div class="work-logs">
    <div class="page-header">
      <h2 class="page-title">
        <el-icon><Document /></el-icon>
        工作日志
      </h2>
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        新建日志
      </el-button>
    </div>

    <!-- 搜索和筛选 -->
    <el-card class="filter-card">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input
            v-model="searchTitle"
            placeholder="搜索日志标题"
            clearable
            @input="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="8">
          <el-select v-model="timeFilter" placeholder="时间筛选" @change="handleTimeFilter">
            <el-option label="全部" value="all" />
            <el-option label="本周" value="week" />
            <el-option label="本月" value="month" />
          </el-select>
        </el-col>
        <el-col :span="8">
          <el-button @click="loadWorkLogs">刷新</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 工作日志列表 -->
    <el-card class="logs-card">
      <div v-if="loading" class="loading-state">
        <el-skeleton :rows="5" animated />
      </div>
      
      <div v-else-if="workLogs.length === 0" class="empty-state">
        <el-empty description="暂无工作日志">
          <el-button type="primary" @click="showCreateDialog = true">
            创建第一个工作日志
          </el-button>
        </el-empty>
      </div>
      
      <div v-else class="logs-list">
        <div 
          v-for="log in workLogs" 
          :key="log.id" 
          class="log-item"
        >
          <div class="log-header">
            <h3 class="log-title">{{ log.title }}</h3>
            <div class="log-actions">
              <el-button type="text" @click="editLog(log)">
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button type="text" @click="deleteLog(log.id)" class="delete-btn">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </div>
          <div class="log-content">{{ log.content }}</div>
          <div class="log-meta">
            <span class="log-date">
              <el-icon><Calendar /></el-icon>
              {{ formatDate(log.workDate) }}
            </span>
            <span class="log-create-time">
              创建于 {{ formatDate(log.createTime) }}
            </span>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 创建/编辑对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="editingLog ? '编辑工作日志' : '新建工作日志'"
      width="600px"
      @close="resetForm"
    >
      <el-form :model="logForm" :rules="logRules" ref="logFormRef" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="logForm.title" placeholder="请输入日志标题" />
        </el-form-item>
        <el-form-item label="工作日期" prop="workDate">
          <el-date-picker
            v-model="logForm.workDate"
            type="date"
            placeholder="选择工作日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="logForm.content"
            type="textarea"
            :rows="6"
            placeholder="请输入工作内容"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="saveLog" :loading="saving">
          {{ editingLog ? '更新' : '创建' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { workLogApi } from '../api'
import { 
  Document, 
  Plus, 
  Search, 
  Edit, 
  Delete, 
  Calendar 
} from '@element-plus/icons-vue'

export default {
  name: 'WorkLogs',
  components: {
    Document,
    Plus,
    Search,
    Edit,
    Delete,
    Calendar
  },
  setup() {
    const workLogs = ref([])
    const loading = ref(false)
    const saving = ref(false)
    const showCreateDialog = ref(false)
    const editingLog = ref(null)
    const searchTitle = ref('')
    const timeFilter = ref('all')
    const logFormRef = ref()

    const logForm = reactive({
      title: '',
      content: '',
      workDate: ''
    })

    const logRules = {
      title: [
        { required: true, message: '请输入日志标题', trigger: 'blur' }
      ],
      workDate: [
        { required: true, message: '请选择工作日期', trigger: 'change' }
      ],
      content: [
        { required: true, message: '请输入工作内容', trigger: 'blur' }
      ]
    }

    const loadWorkLogs = async () => {
      loading.value = true
      try {
        const data = await workLogApi.getAll()
        workLogs.value = data
      } catch (error) {
        ElMessage.error(error.message || '加载工作日志失败')
        console.error(error)
      } finally {
        loading.value = false
      }
    }

    const handleSearch = async () => {
      if (searchTitle.value.trim()) {
        try {
          const data = await workLogApi.search(searchTitle.value)
          workLogs.value = data
        } catch (error) {
          ElMessage.error('搜索失败')
        }
      } else {
        loadWorkLogs()
      }
    }

    const handleTimeFilter = async () => {
      try {
        let data
        switch (timeFilter.value) {
          case 'week':
            data = await workLogApi.getThisWeek()
            break
          case 'month':
            data = await workLogApi.getThisMonth()
            break
          default:
            data = await workLogApi.getAll()
        }
        workLogs.value = data
      } catch (error) {
        ElMessage.error('筛选失败')
      }
    }

    const editLog = (log) => {
      editingLog.value = log
      logForm.title = log.title
      logForm.content = log.content
      logForm.workDate = log.workDate
      showCreateDialog.value = true
    }

    const saveLog = async () => {
      if (!logFormRef.value) return
      
      const valid = await logFormRef.value.validate().catch(() => false)
      if (!valid) return

      saving.value = true
      try {
        if (editingLog.value) {
          await workLogApi.update(editingLog.value.id, logForm)
          ElMessage.success('工作日志更新成功')
        } else {
          await workLogApi.create(logForm)
          ElMessage.success('工作日志创建成功')
        }
        showCreateDialog.value = false
        loadWorkLogs()
      } catch (error) {
        ElMessage.error(error.message || (editingLog.value ? '更新失败' : '创建失败'))
        console.error(error)
      } finally {
        saving.value = false
      }
    }

    const deleteLog = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除这个工作日志吗？', '确认删除', {
          type: 'warning'
        })
        
        await workLogApi.delete(id)
        ElMessage.success('删除成功')
        loadWorkLogs()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    }

    const resetForm = () => {
      editingLog.value = null
      logForm.title = ''
      logForm.content = ''
      logForm.workDate = ''
      if (logFormRef.value) {
        logFormRef.value.resetFields()
      }
    }

    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', { 
        hour: '2-digit', 
        minute: '2-digit' 
      })
    }

    onMounted(() => {
      loadWorkLogs()
    })

    return {
      workLogs,
      loading,
      saving,
      showCreateDialog,
      editingLog,
      searchTitle,
      timeFilter,
      logForm,
      logRules,
      logFormRef,
      loadWorkLogs,
      handleSearch,
      handleTimeFilter,
      editLog,
      saveLog,
      deleteLog,
      resetForm,
      formatDate
    }
  }
}
</script>

<style scoped>
.work-logs {
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

.logs-card {
  min-height: 400px;
}

.loading-state, .empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}

.log-item {
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.log-item:last-child {
  border-bottom: none;
}

.log-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}

.log-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.log-actions {
  display: flex;
  gap: 10px;
}

.delete-btn {
  color: #f56c6c;
}

.delete-btn:hover {
  color: #f56c6c;
}

.log-content {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 15px;
  white-space: pre-wrap;
}

.log-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #909399;
}

.log-date {
  display: flex;
  align-items: center;
  gap: 5px;
}

.log-create-time {
  font-style: italic;
}
</style>
