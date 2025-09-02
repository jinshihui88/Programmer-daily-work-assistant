<template>
  <div class="pomodoro">
    <div class="page-header">
      <h2 class="page-title">
        <el-icon><Timer /></el-icon>
        番茄钟计时器
      </h2>
    </div>

    <!-- 番茄钟计时器 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="timer-card">
          <div class="timer-container">
            <div class="timer-display">
              <div class="time-circle">
                <div class="time-text">{{ formatTime(remainingTime) }}</div>
                <div class="task-text">{{ currentTask || '未开始' }}</div>
              </div>
            </div>
            
            <div class="timer-controls">
              <div v-if="!isRunning" class="start-controls">
                <el-input
                  v-model="newTask"
                  placeholder="输入任务名称"
                  class="task-input"
                />
                <el-input-number
                  v-model="duration"
                  :min="1"
                  :max="120"
                  :step="1"
                  controls-position="right"
                  class="duration-input"
                  @change="handleDurationChange"
                />
                <span class="duration-label">分钟</span>
                <el-button type="primary" @click="startTimer" :disabled="!newTask.trim()">
                  <el-icon><VideoPlay /></el-icon>
                  开始
                </el-button>
              </div>
              
              <div v-else class="running-controls">
                <el-button type="success" @click="completeTimer">
                  <el-icon><Check /></el-icon>
                  完成
                </el-button>
                <el-button type="warning" @click="interruptTimer">
                  <el-icon><VideoPause /></el-icon>
                  中断
                </el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="stats-card">
          <template #header>
            <span>今日统计</span>
          </template>
          <div class="stats-content">
            <div class="stat-item">
              <div class="stat-number">{{ todayCount }}</div>
              <div class="stat-label">完成番茄钟</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">{{ (todayCount * 25) }}</div>
              <div class="stat-label">专注分钟</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近记录 -->
    <el-card class="records-card">
      <template #header>
        <div class="card-header">
          <span>最近记录</span>
          <el-button type="text" @click="loadSessions">刷新</el-button>
        </div>
      </template>
      
      <div v-if="loading" class="loading-state">
        <el-skeleton :rows="5" animated />
      </div>
      
      <div v-else-if="sessions.length === 0" class="empty-state">
        <el-empty description="暂无番茄钟记录" />
      </div>
      
      <div v-else class="sessions-list">
        <div 
          v-for="session in sessions" 
          :key="session.id" 
          class="session-item"
        >
          <div class="session-info">
            <div class="session-task">{{ session.task }}</div>
            <div class="session-meta">
              <el-tag 
                :type="getStatusType(session.status)" 
                size="small"
              >
                {{ getStatusText(session.status) }}
              </el-tag>
              <span class="session-duration">{{ session.durationMinutes }}分钟</span>
              <span class="session-time">{{ formatDateTime(session.startTime) }}</span>
            </div>
          </div>
          <div class="session-actions">
            <el-button type="text" @click="deleteSession(session.id)" class="delete-btn">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { pomodoroApi } from '../api'
import { 
  Timer, 
  VideoPlay, 
  VideoPause, 
  Check, 
  Delete 
} from '@element-plus/icons-vue'

export default {
  name: 'Pomodoro',
  components: {
    Timer,
    VideoPlay,
    VideoPause,
    Check,
    Delete
  },
  setup() {
    const sessions = ref([])
    const loading = ref(false)
    const isRunning = ref(false)
    const currentSessionId = ref(null)
    const currentTask = ref('')
    const newTask = ref('')
    const duration = ref(25)
    
    // 监听duration变化，用于调试
    const handleDurationChange = (value) => {
      console.log('Duration changed to:', value)
      duration.value = value
    }
    const remainingTime = ref(0)
    const todayCount = ref(0)
    let timer = null

    const loadSessions = async () => {
      loading.value = true
      try {
        const data = await pomodoroApi.getRecent()
        sessions.value = data
      } catch (error) {
        ElMessage.error(error.message || '加载番茄钟记录失败')
        console.error(error)
      } finally {
        loading.value = false
      }
    }

    const loadTodayCount = async () => {
      try {
        const count = await pomodoroApi.getTodayCount()
        todayCount.value = count
      } catch (error) {
        console.error('加载今日统计失败:', error)
      }
    }

    const startTimer = async () => {
      try {
        const session = await pomodoroApi.start(newTask.value, duration.value)
        currentSessionId.value = session.id
        currentTask.value = newTask.value
        remainingTime.value = duration.value * 60
        isRunning.value = true
        newTask.value = ''
        
        // 开始倒计时
        timer = setInterval(() => {
          remainingTime.value--
          if (remainingTime.value <= 0) {
            completeTimer()
          }
        }, 1000)
        
        ElMessage.success('番茄钟已开始')
      } catch (error) {
        ElMessage.error('启动失败')
        console.error(error)
      }
    }

    const completeTimer = async () => {
      if (currentSessionId.value) {
        try {
          await pomodoroApi.complete(currentSessionId.value)
          ElMessage.success('番茄钟完成！')
          resetTimer()
          loadSessions()
          loadTodayCount()
        } catch (error) {
          ElMessage.error('完成失败')
          console.error(error)
        }
      }
    }

    const interruptTimer = async () => {
      if (currentSessionId.value) {
        try {
          await ElMessageBox.confirm('确定要中断当前番茄钟吗？', '确认中断', {
            type: 'warning'
          })
          
          await pomodoroApi.interrupt(currentSessionId.value)
          ElMessage.info('番茄钟已中断')
          resetTimer()
          loadSessions()
        } catch (error) {
          if (error !== 'cancel') {
            ElMessage.error('中断失败')
            console.error(error)
          }
        }
      }
    }

    const deleteSession = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除这个番茄钟记录吗？', '确认删除', {
          type: 'warning'
        })
        
        await pomodoroApi.delete(id)
        ElMessage.success('删除成功')
        loadSessions()
        loadTodayCount()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    }

    const resetTimer = () => {
      if (timer) {
        clearInterval(timer)
        timer = null
      }
      isRunning.value = false
      currentSessionId.value = null
      currentTask.value = ''
      remainingTime.value = 0
    }

    const formatTime = (seconds) => {
      const minutes = Math.floor(seconds / 60)
      const remainingSeconds = seconds % 60
      return `${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`
    }

    const formatDateTime = (dateString) => {
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
      loadSessions()
      loadTodayCount()
    })

    onUnmounted(() => {
      resetTimer()
    })

    return {
      sessions,
      loading,
      isRunning,
      currentTask,
      newTask,
      duration,
      remainingTime,
      todayCount,
      loadSessions,
      startTimer,
      completeTimer,
      interruptTimer,
      deleteSession,
      handleDurationChange,
      formatTime,
      formatDateTime,
      getStatusType,
      getStatusText
    }
  }
}
</script>

<style scoped>
.pomodoro {
  padding: 20px;
}

.page-header {
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

.timer-card {
  height: 400px;
}

.timer-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.timer-display {
  margin-bottom: 40px;
}

.time-circle {
  width: 200px;
  height: 200px;
  border: 8px solid #409eff;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.time-text {
  font-size: 36px;
  font-weight: 700;
  font-family: 'Monaco', 'Menlo', monospace;
}

.task-text {
  font-size: 14px;
  margin-top: 10px;
  text-align: center;
  opacity: 0.9;
}

.timer-controls {
  width: 100%;
}

.start-controls {
  display: flex;
  align-items: center;
  gap: 10px;
  justify-content: center;
}

.task-input {
  width: 200px;
}

.duration-input {
  width: 120px;
}

.duration-input :deep(.el-input-number__increase),
.duration-input :deep(.el-input-number__decrease) {
  background-color: #f5f7fa;
  border-color: #dcdfe6;
}

.duration-input :deep(.el-input-number__increase):hover,
.duration-input :deep(.el-input-number__decrease):hover {
  background-color: #409eff;
  color: #fff;
}

.duration-label {
  color: #606266;
  font-size: 14px;
}

.running-controls {
  display: flex;
  gap: 15px;
  justify-content: center;
}

.stats-card {
  height: 400px;
}

.stats-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  gap: 40px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 48px;
  font-weight: 700;
  color: #409eff;
  line-height: 1;
}

.stat-label {
  font-size: 16px;
  color: #909399;
  margin-top: 10px;
}

.records-card {
  margin-top: 20px;
  min-height: 300px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.loading-state, .empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
}

.session-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.session-item:last-child {
  border-bottom: none;
}

.session-task {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
}

.session-meta {
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 12px;
  color: #909399;
}

.session-duration {
  font-weight: 500;
}

.delete-btn {
  color: #f56c6c;
}
</style>