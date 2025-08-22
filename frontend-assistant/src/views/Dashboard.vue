<template>
  <div class="dashboard">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>
    
    <!-- 错误状态 -->
    <div v-else-if="error" class="error-container">
      <el-result
        icon="error"
        title="加载失败"
        :sub-title="errorMessage"
      >
        <template #extra>
          <el-button type="primary" @click="retryLoad">重新加载</el-button>
        </template>
      </el-result>
    </div>
    
    <!-- 正常内容 -->
    <div v-else class="dashboard-content">
      <h2 class="page-title">
        <el-icon><Odometer /></el-icon>
        工作仪表板
      </h2>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon work-log">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ dashboardData.todayWorkLogs || 0 }}</div>
              <div class="stat-label">今日工作日志</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon pomodoro">
              <el-icon><Timer /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ dashboardData.todayPomodoros || 0 }}</div>
              <div class="stat-label">今日番茄钟</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon learning">
              <el-icon><Reading /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ inProgressLearning.length || 0 }}</div>
              <div class="stat-label">进行中学习</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon code">
              <el-icon><Collection /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ learningStats.COMPLETED || 0 }}</div>
              <div class="stat-label">已完成学习</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 内容区域 -->
    <el-row :gutter="20" class="content-row">
      <!-- 最近工作日志 -->
      <el-col :span="12">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span>最近工作日志</span>
              <el-button type="text" @click="$router.push('/work-logs')">
                查看更多
              </el-button>
            </div>
          </template>
          
          <div v-if="recentWorkLogs.length === 0" class="empty-state">
            <el-empty description="暂无工作日志" />
          </div>
          
          <div v-else class="log-list">
            <div 
              v-for="log in recentWorkLogs.slice(0, 5)" 
              :key="log.id" 
              class="log-item"
            >
              <div class="log-title">{{ log.title }}</div>
              <div class="log-date">{{ formatDate(log.workDate) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 最近番茄钟 -->
      <el-col :span="12">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span>最近番茄钟</span>
              <el-button type="text" @click="$router.push('/pomodoro')">
                查看更多
              </el-button>
            </div>
          </template>
          
          <div v-if="recentPomodoros.length === 0" class="empty-state">
            <el-empty description="暂无番茄钟记录" />
          </div>
          
          <div v-else class="pomodoro-list">
            <div 
              v-for="session in recentPomodoros.slice(0, 5)" 
              :key="session.id" 
              class="pomodoro-item"
            >
              <div class="pomodoro-task">{{ session.task }}</div>
              <div class="pomodoro-info">
                <el-tag 
                  :type="getStatusType(session.status)" 
                  size="small"
                >
                  {{ getStatusText(session.status) }}
                </el-tag>
                <span class="pomodoro-duration">{{ session.durationMinutes }}分钟</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 学习进度 -->
    <el-row :gutter="20" class="content-row">
      <el-col :span="24">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span>学习进度概览</span>
              <el-button type="text" @click="$router.push('/learning')">
                查看更多
              </el-button>
            </div>
          </template>
          
          <div v-if="inProgressLearning.length === 0" class="empty-state">
            <el-empty description="暂无进行中的学习项目" />
          </div>
          
          <div v-else class="learning-list">
            <div 
              v-for="progress in inProgressLearning.slice(0, 3)" 
              :key="progress.id" 
              class="learning-item"
            >
              <div class="learning-header">
                <span class="learning-subject">{{ progress.subject }}</span>
                <span class="learning-percentage">{{ progress.completionPercentage?.toFixed(1) || 0 }}%</span>
              </div>
              <el-progress 
                :percentage="progress.completionPercentage || 0" 
                :stroke-width="8"
                :show-text="false"
              />
              <div class="learning-hours">
                {{ progress.completedHours || 0 }} / {{ progress.totalHours || 0 }} 小时
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    </div> <!-- 关闭 dashboard-content -->
  </div> <!-- 关闭 dashboard -->
</template>

<script>
import { ref, onMounted } from 'vue'
import { reportApi, learningProgressApi } from '../api'
import { Odometer, Document, Timer, Reading, Collection } from '@element-plus/icons-vue'

export default {
  name: 'Dashboard',
  components: {
    Odometer,
    Document,
    Timer,
    Reading,
    Collection
  },
  setup() {
    const loading = ref(true)
    const error = ref(false)
    const errorMessage = ref('')
    const dashboardData = ref({})
    const recentWorkLogs = ref([])
    const recentPomodoros = ref([])
    const inProgressLearning = ref([])
    const learningStats = ref({})

    const loadDashboardData = async () => {
      loading.value = true
      error.value = false
      errorMessage.value = ''
      
      try {
        const data = await reportApi.getDashboard()
        dashboardData.value = data
        recentWorkLogs.value = data.recentWorkLogs || []
        recentPomodoros.value = data.recentPomodoros || []
        inProgressLearning.value = data.inProgressLearning || []
        learningStats.value = data.learningStats || {}
      } catch (err) {
        console.error('加载仪表板数据失败:', err)
        error.value = true
        errorMessage.value = err.message || '网络连接异常，请检查后端服务是否正常运行'
      } finally {
        loading.value = false
      }
    }

    const retryLoad = () => {
      loadDashboardData()
    }

    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', { 
        hour: '2-digit', 
        minute: '2-digit' 
      })
    }

    const getStatusType = (status) => {
      const statusMap = {
        'COMPLETED': 'success',
        'RUNNING': 'warning',
        'INTERRUPTED': 'danger'
      }
      return statusMap[status] || 'info'
    }

    const getStatusText = (status) => {
      const statusMap = {
        'COMPLETED': '已完成',
        'RUNNING': '进行中',
        'INTERRUPTED': '已中断'
      }
      return statusMap[status] || status
    }

    onMounted(() => {
      loadDashboardData()
    })

    return {
      loading,
      error,
      errorMessage,
      dashboardData,
      recentWorkLogs,
      recentPomodoros,
      inProgressLearning,
      learningStats,
      retryLoad,
      formatDate,
      getStatusType,
      getStatusText
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.loading-container {
  padding: 20px;
}

.error-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  height: 120px;
}

.stat-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.stat-icon.work-log {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.pomodoro {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.learning {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.code {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-number {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.content-row {
  margin-bottom: 20px;
}

.content-card {
  min-height: 300px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
}

.log-item, .pomodoro-item {
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.log-item:last-child, .pomodoro-item:last-child {
  border-bottom: none;
}

.log-title {
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
}

.log-date {
  font-size: 12px;
  color: #909399;
}

.pomodoro-task {
  font-weight: 500;
  color: #303133;
  margin-bottom: 8px;
}

.pomodoro-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pomodoro-duration {
  font-size: 12px;
  color: #909399;
}

.learning-item {
  margin-bottom: 20px;
}

.learning-item:last-child {
  margin-bottom: 0;
}

.learning-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.learning-subject {
  font-weight: 500;
  color: #303133;
}

.learning-percentage {
  font-size: 14px;
  color: #409eff;
  font-weight: 600;
}

.learning-hours {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
  text-align: right;
}
</style>