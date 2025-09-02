<template>
  <div class="reports">
    <div class="page-header">
      <h2 class="page-title">
        <el-icon><DataAnalysis /></el-icon>
        数据报表
      </h2>
    </div>

    <!-- 报表类型选择 -->
    <el-card class="report-selector">
      <el-radio-group v-model="reportType" @change="loadReport">
        <el-radio-button label="weekly">周报</el-radio-button>
        <el-radio-button label="monthly">月报</el-radio-button>
      </el-radio-group>
      <el-button type="primary" @click="exportReport" class="export-btn">
        <el-icon><Download /></el-icon>
        导出报表
      </el-button>
    </el-card>

    <!-- 报表内容 -->
    <div v-if="loading" class="loading-state">
      <el-skeleton :rows="8" animated />
    </div>

    <div v-else-if="reportData" class="report-content">
      <!-- 报表头部信息 -->
      <el-card class="report-header">
        <h3>{{ reportData.reportType }}</h3>
        <p class="report-period">
          统计期间: {{ formatDate(reportData.startDate) }} - {{ formatDate(reportData.endDate) }}
        </p>
      </el-card>

      <!-- 统计概览 -->
      <el-row :gutter="20" class="stats-overview">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon work-log">
                <el-icon><Document /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ reportData.workLogCount || 0 }}</div>
                <div class="stat-label">工作日志</div>
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
                <div class="stat-number">{{ reportData.pomodoroStats?.completedSessions || 0 }}</div>
                <div class="stat-label">完成番茄钟</div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon time">
                <el-icon><Clock /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ reportData.pomodoroStats?.totalHours || 0 }}</div>
                <div class="stat-label">专注小时</div>
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
                <div class="stat-number">{{ reportData.learningStats?.COMPLETED || 0 }}</div>
                <div class="stat-label">完成学习</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 详细数据 -->
      <el-row :gutter="20" class="detail-section">
        <!-- 番茄钟详情 -->
        <el-col :span="12">
          <el-card class="detail-card">
            <template #header>
              <span>番茄钟统计详情</span>
            </template>
            <div class="detail-content">
              <div class="detail-item">
                <span class="detail-label">总会话数:</span>
                <span class="detail-value">{{ reportData.pomodoroStats?.totalSessions || 0 }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">完成会话:</span>
                <span class="detail-value">{{ reportData.pomodoroStats?.completedSessions || 0 }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">总时长:</span>
                <span class="detail-value">{{ reportData.pomodoroStats?.totalMinutes || 0 }} 分钟</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">专注时长:</span>
                <span class="detail-value">{{ reportData.pomodoroStats?.totalHours || 0 }} 小时</span>
              </div>
              <div v-if="reportData.pomodoroStats?.averageDailyMinutes" class="detail-item">
                <span class="detail-label">日均时长:</span>
                <span class="detail-value">{{ reportData.pomodoroStats.averageDailyMinutes }} 分钟</span>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 学习进度详情 -->
        <el-col :span="12">
          <el-card class="detail-card">
            <template #header>
              <span>学习进度统计</span>
            </template>
            <div class="detail-content">
              <div class="detail-item">
                <span class="detail-label">未开始:</span>
                <span class="detail-value">{{ reportData.learningStats?.NOT_STARTED || 0 }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">进行中:</span>
                <span class="detail-value">{{ reportData.learningStats?.IN_PROGRESS || 0 }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">已完成:</span>
                <span class="detail-value">{{ reportData.learningStats?.COMPLETED || 0 }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">已暂停:</span>
                <span class="detail-value">{{ reportData.learningStats?.PAUSED || 0 }}</span>
              </div>
              <div v-if="reportData.learningStats?.totalLearningHours" class="detail-item">
                <span class="detail-label">总学习时长:</span>
                <span class="detail-value">{{ reportData.learningStats.totalLearningHours }} 小时</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 工作日志列表 (仅周报显示) -->
      <div v-if="reportType === 'weekly' && reportData.workLogs && reportData.workLogs.length > 0">
        <el-card class="logs-card">
          <template #header>
            <span>本周工作日志</span>
          </template>
          <div class="logs-list">
            <div 
              v-for="log in reportData.workLogs" 
              :key="log.id" 
              class="log-item"
            >
              <div class="log-title">{{ log.title }}</div>
              <div class="log-date">{{ formatDate(log.workDate) }}</div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 日志分布图表 (仅月报显示) -->
      <div v-if="reportType === 'monthly' && reportData.dailyLogCount">
        <el-card class="chart-card">
          <template #header>
            <span>工作日志分布</span>
          </template>
          <div class="chart-content">
            <div class="daily-stats">
              <div 
                v-for="(count, date) in reportData.dailyLogCount" 
                :key="date" 
                class="daily-item"
              >
                <span class="daily-date">{{ date }}</span>
                <span class="daily-count">{{ count }} 条</span>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <div v-else class="empty-state">
      <el-empty description="暂无报表数据" />
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { reportApi } from '../api'
import { 
  DataAnalysis, 
  Download, 
  Document, 
  Timer, 
  Clock, 
  Reading 
} from '@element-plus/icons-vue'

export default {
  name: 'Reports',
  components: {
    DataAnalysis,
    Download,
    Document,
    Timer,
    Clock,
    Reading
  },
  setup() {
    const reportType = ref('weekly')
    const reportData = ref(null)
    const loading = ref(false)

    const loadReport = async () => {
      loading.value = true
      try {
        let data
        if (reportType.value === 'weekly') {
          data = await reportApi.getWeeklyReport()
        } else {
          data = await reportApi.getMonthlyReport()
        }
        reportData.value = data
      } catch (error) {
        ElMessage.error(error.message || '加载报表失败')
        console.error(error)
      } finally {
        loading.value = false
      }
    }

    const exportReport = () => {
      if (!reportData.value) {
        ElMessage.warning('暂无数据可导出')
        return
      }

      // 生成报表文本
      const reportText = generateReportText(reportData.value)
      
      // 创建下载链接
      const blob = new Blob([reportText], { type: 'text/plain;charset=utf-8' })
      const url = URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = `${reportData.value.reportType}_${new Date().toISOString().split('T')[0]}.txt`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      URL.revokeObjectURL(url)
      
      ElMessage.success('报表导出成功')
    }

    const generateReportText = (data) => {
      let text = `${data.reportType}\n`
      text += `统计期间: ${formatDate(data.startDate)} - ${formatDate(data.endDate)}\n\n`
      
      text += `=== 统计概览 ===\n`
      text += `工作日志: ${data.workLogCount || 0} 条\n`
      text += `完成番茄钟: ${data.pomodoroStats?.completedSessions || 0} 个\n`
      text += `专注时长: ${data.pomodoroStats?.totalHours || 0} 小时\n`
      text += `完成学习: ${data.learningStats?.COMPLETED || 0} 项\n\n`
      
      text += `=== 番茄钟详情 ===\n`
      text += `总会话数: ${data.pomodoroStats?.totalSessions || 0}\n`
      text += `完成会话: ${data.pomodoroStats?.completedSessions || 0}\n`
      text += `总时长: ${data.pomodoroStats?.totalMinutes || 0} 分钟\n`
      if (data.pomodoroStats?.averageDailyMinutes) {
        text += `日均时长: ${data.pomodoroStats.averageDailyMinutes} 分钟\n`
      }
      text += `\n`
      
      text += `=== 学习进度统计 ===\n`
      text += `未开始: ${data.learningStats?.NOT_STARTED || 0}\n`
      text += `进行中: ${data.learningStats?.IN_PROGRESS || 0}\n`
      text += `已完成: ${data.learningStats?.COMPLETED || 0}\n`
      text += `已暂停: ${data.learningStats?.PAUSED || 0}\n`
      if (data.learningStats?.totalLearningHours) {
        text += `总学习时长: ${data.learningStats.totalLearningHours} 小时\n`
      }
      
      if (data.workLogs && data.workLogs.length > 0) {
        text += `\n=== 工作日志 ===\n`
        data.workLogs.forEach(log => {
          text += `${formatDate(log.workDate)} - ${log.title}\n`
        })
      }
      
      return text
    }

    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }

    onMounted(() => {
      loadReport()
    })

    return {
      reportType,
      reportData,
      loading,
      loadReport,
      exportReport,
      formatDate
    }
  }
}
</script>

<style scoped>
.reports {
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

.report-selector {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.export-btn {
  margin-left: auto;
}

.loading-state {
  padding: 40px;
}

.report-header {
  margin-bottom: 20px;
  text-align: center;
}

.report-header h3 {
  margin: 0 0 10px 0;
  font-size: 24px;
  color: #303133;
}

.report-period {
  color: #909399;
  font-size: 14px;
  margin: 0;
}

.stats-overview {
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

.stat-icon.time {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.learning {
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

.detail-section {
  margin-bottom: 20px;
}

.detail-card {
  height: 280px;
}

.detail-content {
  padding: 10px 0;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.detail-item:last-child {
  border-bottom: none;
}

.detail-label {
  color: #606266;
  font-size: 14px;
}

.detail-value {
  color: #303133;
  font-weight: 500;
}

.logs-card {
  margin-bottom: 20px;
}

.logs-list {
  max-height: 400px;
  overflow-y: auto;
}

.log-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.log-item:last-child {
  border-bottom: none;
}

.log-title {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

.log-date {
  font-size: 12px;
  color: #909399;
}

.chart-card {
  margin-bottom: 20px;
}

.chart-content {
  padding: 20px 0;
}

.daily-stats {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 15px;
}

.daily-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.daily-date {
  font-size: 12px;
  color: #909399;
  margin-bottom: 5px;
}

.daily-count {
  font-size: 16px;
  font-weight: 600;
  color: #409eff;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}
</style>
