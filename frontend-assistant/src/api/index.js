import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    console.error('API请求错误:', error)
    return Promise.reject(error)
  }
)

// 工作日志API
export const workLogApi = {
  // 获取所有工作日志
  getAll: () => api.get('/work-logs'),
  
  // 根据ID获取工作日志
  getById: (id) => api.get(`/work-logs/${id}`),
  
  // 创建工作日志
  create: (data) => api.post('/work-logs', data),
  
  // 更新工作日志
  update: (id, data) => api.put(`/work-logs/${id}`, data),
  
  // 删除工作日志
  delete: (id) => api.delete(`/work-logs/${id}`),
  
  // 搜索工作日志
  search: (title) => api.get('/work-logs/search', { params: { title } }),
  
  // 获取最近的工作日志
  getRecent: () => api.get('/work-logs/recent'),
  
  // 获取本周工作日志
  getThisWeek: () => api.get('/work-logs/this-week'),
  
  // 获取本月工作日志
  getThisMonth: () => api.get('/work-logs/this-month')
}

// 代码片段API
export const codeSnippetApi = {
  // 获取所有代码片段
  getAll: () => api.get('/code-snippets'),
  
  // 根据ID获取代码片段
  getById: (id) => api.get(`/code-snippets/${id}`),
  
  // 创建代码片段
  create: (data) => api.post('/code-snippets', data),
  
  // 更新代码片段
  update: (id, data) => api.put(`/code-snippets/${id}`, data),
  
  // 删除代码片段
  delete: (id) => api.delete(`/code-snippets/${id}`),
  
  // 根据编程语言查询
  getByLanguage: (language) => api.get(`/code-snippets/language/${language}`),
  
  // 搜索代码片段
  search: (title) => api.get('/code-snippets/search', { params: { title } }),
  
  // 根据标签搜索
  getByTag: (tag) => api.get(`/code-snippets/tag/${tag}`),
  
  // 获取所有编程语言
  getLanguages: () => api.get('/code-snippets/languages')
}

// 学习进度API
export const learningProgressApi = {
  // 获取所有学习进度
  getAll: () => api.get('/learning-progress'),
  
  // 根据ID获取学习进度
  getById: (id) => api.get(`/learning-progress/${id}`),
  
  // 创建学习进度
  create: (data) => api.post('/learning-progress', data),
  
  // 更新学习进度
  update: (id, data) => api.put(`/learning-progress/${id}`, data),
  
  // 更新完成小时数
  updateHours: (id, hours) => api.put(`/learning-progress/${id}/hours`, null, { params: { hours } }),
  
  // 删除学习进度
  delete: (id) => api.delete(`/learning-progress/${id}`),
  
  // 根据状态查询
  getByStatus: (status) => api.get(`/learning-progress/status/${status}`),
  
  // 搜索学习进度
  search: (subject) => api.get('/learning-progress/search', { params: { subject } }),
  
  // 获取进行中的学习项目
  getInProgress: () => api.get('/learning-progress/in-progress'),
  
  // 获取状态统计
  getStatistics: () => api.get('/learning-progress/statistics')
}

// 番茄钟API
export const pomodoroApi = {
  // 获取所有番茄钟会话
  getAll: () => api.get('/pomodoro-sessions'),
  
  // 根据ID获取番茄钟会话
  getById: (id) => api.get(`/pomodoro-sessions/${id}`),
  
  // 开始新的番茄钟会话
  start: (task, durationMinutes = 25) => api.post('/pomodoro-sessions/start', null, { 
    params: { task, durationMinutes } 
  }),
  
  // 完成番茄钟会话
  complete: (id) => api.put(`/pomodoro-sessions/${id}/complete`),
  
  // 中断番茄钟会话
  interrupt: (id) => api.put(`/pomodoro-sessions/${id}/interrupt`),
  
  // 根据状态查询
  getByStatus: (status) => api.get(`/pomodoro-sessions/status/${status}`),
  
  // 获取今日完成的番茄钟数量
  getTodayCount: () => api.get('/pomodoro-sessions/today/count'),
  
  // 获取最近的番茄钟记录
  getRecent: () => api.get('/pomodoro-sessions/recent'),
  
  // 获取本周番茄钟记录
  getThisWeek: () => api.get('/pomodoro-sessions/this-week'),
  
  // 删除番茄钟会话
  delete: (id) => api.delete(`/pomodoro-sessions/${id}`)
}

// 报表API
export const reportApi = {
  // 生成周报
  getWeeklyReport: () => api.get('/reports/weekly'),
  
  // 生成月报
  getMonthlyReport: () => api.get('/reports/monthly'),
  
  // 获取仪表板数据
  getDashboard: () => api.get('/reports/dashboard')
}

export default api