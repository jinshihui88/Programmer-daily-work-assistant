<template>
  <div class="learning">
    <div class="page-header">
      <h2 class="page-title">
        <el-icon><Reading /></el-icon>
        学习进度跟踪
      </h2>
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        新建学习项目
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ statistics.NOT_STARTED || 0 }}</div>
            <div class="stat-label">未开始</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ statistics.IN_PROGRESS || 0 }}</div>
            <div class="stat-label">进行中</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ statistics.COMPLETED || 0 }}</div>
            <div class="stat-label">已完成</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ statistics.PAUSED || 0 }}</div>
            <div class="stat-label">已暂停</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索和筛选 -->
    <el-card class="filter-card">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input
            v-model="searchSubject"
            placeholder="搜索学习项目"
            clearable
            @input="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="8">
          <el-select v-model="statusFilter" placeholder="状态筛选" @change="handleStatusFilter">
            <el-option label="全部状态" value="" />
            <el-option label="未开始" value="NOT_STARTED" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已暂停" value="PAUSED" />
          </el-select>
        </el-col>
        <el-col :span="8">
          <el-button @click="loadLearningProgress">刷新</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 学习进度列表 -->
    <el-card class="progress-card">
      <div v-if="loading" class="loading-state">
        <el-skeleton :rows="5" animated />
      </div>
      
      <div v-else-if="learningProgress.length === 0" class="empty-state">
        <el-empty description="暂无学习项目">
          <el-button type="primary" @click="showCreateDialog = true">
            创建第一个学习项目
          </el-button>
        </el-empty>
      </div>
      
      <div v-else class="progress-list">
        <div 
          v-for="progress in learningProgress" 
          :key="progress.id" 
          class="progress-item"
        >
          <div class="progress-header">
            <div class="progress-info">
              <h3 class="progress-subject">{{ progress.subject }}</h3>
              <el-tag 
                :type="getStatusType(progress.status)" 
                size="small"
              >
                {{ getStatusText(progress.status) }}
              </el-tag>
            </div>
            <div class="progress-actions">
              <el-button type="text" @click="updateHours(progress)">
                <el-icon><Clock /></el-icon>
                更新进度
              </el-button>
              <el-button type="text" @click="editProgress(progress)">
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button type="text" @click="deleteProgress(progress.id)" class="delete-btn">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </div>
          
          <div v-if="progress.description" class="progress-description">
            {{ progress.description }}
          </div>
          
          <div class="progress-bar-section">
            <div class="progress-stats">
              <span>{{ progress.completedHours || 0 }} / {{ progress.totalHours || 0 }} 小时</span>
              <span class="progress-percentage">{{ (progress.completionPercentage || 0).toFixed(1) }}%</span>
            </div>
            <el-progress 
              :percentage="progress.completionPercentage || 0" 
              :stroke-width="12"
              :show-text="false"
            />
          </div>
          
          <div class="progress-dates">
            <span v-if="progress.startDate" class="date-item">
              <el-icon><Calendar /></el-icon>
              开始: {{ formatDate(progress.startDate) }}
            </span>
            <span v-if="progress.targetDate" class="date-item">
              <el-icon><Flag /></el-icon>
              目标: {{ formatDate(progress.targetDate) }}
            </span>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 创建/编辑对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="editingProgress ? '编辑学习项目' : '新建学习项目'"
      width="600px"
      @close="resetForm"
    >
      <el-form :model="progressForm" :rules="progressRules" ref="progressFormRef" label-width="80px">
        <el-form-item label="项目名称" prop="subject">
          <el-input v-model="progressForm.subject" placeholder="请输入学习项目名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="progressForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入项目描述（可选）"
          />
        </el-form-item>
        <el-form-item label="总时长" prop="totalHours">
          <el-input-number 
            v-model="progressForm.totalHours" 
            :min="1" 
            :max="10000"
            placeholder="预计总学习时长（小时）"
          />
        </el-form-item>
        <el-form-item label="已完成">
          <el-input-number 
            v-model="progressForm.completedHours" 
            :min="0" 
            :max="progressForm.totalHours || 10000"
            placeholder="已完成时长（小时）"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="progressForm.status" placeholder="选择状态">
            <el-option label="未开始" value="NOT_STARTED" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已暂停" value="PAUSED" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker
            v-model="progressForm.startDate"
            type="datetime"
            placeholder="选择开始日期"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="目标日期">
          <el-date-picker
            v-model="progressForm.targetDate"
            type="datetime"
            placeholder="选择目标完成日期"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="saveProgress" :loading="saving">
          {{ editingProgress ? '更新' : '创建' }}
        </el-button>
      </template>
    </el-dialog>

    <!-- 更新进度对话框 -->
    <el-dialog
      v-model="showUpdateHoursDialog"
      title="更新学习进度"
      width="400px"
    >
      <el-form label-width="100px">
        <el-form-item label="当前进度">
          <span>{{ updatingProgress?.completedHours || 0 }} / {{ updatingProgress?.totalHours || 0 }} 小时</span>
        </el-form-item>
        <el-form-item label="新的进度">
          <el-input-number 
            v-model="newCompletedHours" 
            :min="0" 
            :max="updatingProgress?.totalHours || 10000"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showUpdateHoursDialog = false">取消</el-button>
        <el-button type="primary" @click="saveHoursUpdate" :loading="updating">
          更新
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { learningProgressApi } from '../api'
import { 
  Reading, 
  Plus, 
  Search, 
  Clock,
  Edit, 
  Delete,
  Calendar,
  Flag
} from '@element-plus/icons-vue'

export default {
  name: 'Learning',
  components: {
    Reading,
    Plus,
    Search,
    Clock,
    Edit,
    Delete,
    Calendar,
    Flag
  },
  setup() {
    const learningProgress = ref([])
    const statistics = ref({})
    const loading = ref(false)
    const saving = ref(false)
    const updating = ref(false)
    const showCreateDialog = ref(false)
    const showUpdateHoursDialog = ref(false)
    const editingProgress = ref(null)
    const updatingProgress = ref(null)
    const searchSubject = ref('')
    const statusFilter = ref('')
    const newCompletedHours = ref(0)
    const progressFormRef = ref()

    const progressForm = reactive({
      subject: '',
      description: '',
      totalHours: null,
      completedHours: 0,
      status: 'NOT_STARTED',
      startDate: '',
      targetDate: ''
    })

    const progressRules = {
      subject: [
        { required: true, message: '请输入项目名称', trigger: 'blur' }
      ],
      totalHours: [
        { required: true, message: '请输入总时长', trigger: 'blur' }
      ]
    }

    const loadLearningProgress = async () => {
      loading.value = true
      try {
        const data = await learningProgressApi.getAll()
        learningProgress.value = data
      } catch (error) {
        ElMessage.error('加载学习进度失败')
        console.error(error)
      } finally {
        loading.value = false
      }
    }

    const loadStatistics = async () => {
      try {
        const data = await learningProgressApi.getStatistics()
        statistics.value = data
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    }

    const handleSearch = async () => {
      if (searchSubject.value.trim()) {
        try {
          const data = await learningProgressApi.search(searchSubject.value)
          learningProgress.value = data
        } catch (error) {
          ElMessage.error('搜索失败')
        }
      } else {
        loadLearningProgress()
      }
    }

    const handleStatusFilter = async () => {
      if (statusFilter.value) {
        try {
          const data = await learningProgressApi.getByStatus(statusFilter.value)
          learningProgress.value = data
        } catch (error) {
          ElMessage.error('筛选失败')
        }
      } else {
        loadLearningProgress()
      }
    }

    const updateHours = (progress) => {
      updatingProgress.value = progress
      newCompletedHours.value = progress.completedHours || 0
      showUpdateHoursDialog.value = true
    }

    const saveHoursUpdate = async () => {
      updating.value = true
      try {
        await learningProgressApi.updateHours(updatingProgress.value.id, newCompletedHours.value)
        ElMessage.success('进度更新成功')
        showUpdateHoursDialog.value = false
        loadLearningProgress()
        loadStatistics()
      } catch (error) {
        ElMessage.error('更新失败')
        console.error(error)
      } finally {
        updating.value = false
      }
    }

    const editProgress = (progress) => {
      editingProgress.value = progress
      progressForm.subject = progress.subject
      progressForm.description = progress.description || ''
      progressForm.totalHours = progress.totalHours
      progressForm.completedHours = progress.completedHours || 0
      progressForm.status = progress.status
      progressForm.startDate = progress.startDate || ''
      progressForm.targetDate = progress.targetDate || ''
      showCreateDialog.value = true
    }

    const saveProgress = async () => {
      if (!progressFormRef.value) return
      
      const valid = await progressFormRef.value.validate().catch(() => false)
      if (!valid) return

      saving.value = true
      try {
        if (editingProgress.value) {
          await learningProgressApi.update(editingProgress.value.id, progressForm)
          ElMessage.success('学习项目更新成功')
        } else {
          await learningProgressApi.create(progressForm)
          ElMessage.success('学习项目创建成功')
        }
        showCreateDialog.value = false
        loadLearningProgress()
        loadStatistics()
      } catch (error) {
        ElMessage.error(editingProgress.value ? '更新失败' : '创建失败')
        console.error(error)
      } finally {
        saving.value = false
      }
    }

    const deleteProgress = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除这个学习项目吗？', '确认删除', {
          type: 'warning'
        })
        
        await learningProgressApi.delete(id)
        ElMessage.success('删除成功')
        loadLearningProgress()
        loadStatistics()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    }

    const resetForm = () => {
      editingProgress.value = null
      progressForm.subject = ''
      progressForm.description = ''
      progressForm.totalHours = null
      progressForm.completedHours = 0
      progressForm.status = 'NOT_STARTED'
      progressForm.startDate = ''
      progressForm.targetDate = ''
      if (progressFormRef.value) {
        progressFormRef.value.resetFields()
      }
    }

    const getStatusType = (status) => {
      const statusMap = {
        'NOT_STARTED': 'info',
        'IN_PROGRESS': 'warning',
        'COMPLETED': 'success',
        'PAUSED': 'danger'
      }
      return statusMap[status] || 'info'
    }

    const getStatusText = (status) => {
      const statusMap = {
        'NOT_STARTED': '未开始',
        'IN_PROGRESS': '进行中',
        'COMPLETED': '已完成',
        'PAUSED': '已暂停'
      }
      return statusMap[status] || status
    }

    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }

    onMounted(() => {
      loadLearningProgress()
      loadStatistics()
    })

    return {
      learningProgress,
      statistics,
      loading,
      saving,
      updating,
      showCreateDialog,
      showUpdateHoursDialog,
      editingProgress,
      updatingProgress,
      searchSubject,
      statusFilter,
      newCompletedHours,
      progressForm,
      progressRules,
      progressFormRef,
      loadLearningProgress,
      handleSearch,
      handleStatusFilter,
      updateHours,
      saveHoursUpdate,
      editProgress,
      saveProgress,
      deleteProgress,
      resetForm,
      getStatusType,
      getStatusText,
      formatDate
    }
  }
}
</script>

<style scoped>
.learning {
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

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
  padding: 20px;
}

.stat-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-number {
  font-size: 32px;
  font-weight: 700;
  color: #409eff;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}

.filter-card {
  margin-bottom: 20px;
}

.progress-card {
  min-height: 400px;
}

.loading-state, .empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}

.progress-item {
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.progress-item:last-child {
  border-bottom: none;
}

.progress-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}

.progress-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.progress-subject {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.progress-actions {
  display: flex;
  gap: 10px;
}

.delete-btn {
  color: #f56c6c;
}

.progress-description {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 15px;
}

.progress-bar-section {
  margin-bottom: 15px;
}

.progress-stats {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-size: 14px;
  color: #606266;
}

.progress-percentage {
  font-weight: 600;
  color: #409eff;
}

.progress-dates {
  display: flex;
  gap: 20px;
  font-size: 12px;
  color: #909399;
}

.date-item {
  display: flex;
  align-items: center;
  gap: 5px;
}
</style>