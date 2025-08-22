<template>
  <div id="app">
    <el-container>
      <!-- 顶部导航 -->
      <el-header class="header">
        <div class="header-content">
          <h1 class="title">
            <el-icon><Monitor /></el-icon>
            程序员工作助手
          </h1>
          <el-menu
            :default-active="$route.path"
            class="nav-menu"
            mode="horizontal"
            router
          >
            <el-menu-item index="/dashboard">
              <el-icon><Odometer /></el-icon>
              <span>仪表板</span>
            </el-menu-item>
            <el-menu-item index="/work-logs">
              <el-icon><Document /></el-icon>
              <span>工作日志</span>
            </el-menu-item>
            <el-menu-item index="/code-snippets">
              <el-icon><Collection /></el-icon>
              <span>代码片段</span>
            </el-menu-item>
            <el-menu-item index="/learning">
              <el-icon><Reading /></el-icon>
              <span>学习进度</span>
            </el-menu-item>
            <el-menu-item index="/pomodoro">
              <el-icon><Timer /></el-icon>
              <span>番茄钟</span>
            </el-menu-item>
            <el-menu-item index="/reports">
              <el-icon><DataAnalysis /></el-icon>
              <span>报表</span>
            </el-menu-item>
          </el-menu>
        </div>
      </el-header>

      <!-- 主要内容区域 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { 
  Monitor, 
  Odometer, 
  Document, 
  Collection, 
  Reading, 
  Timer, 
  DataAnalysis 
} from '@element-plus/icons-vue'

export default {
  name: 'App',
  components: {
    Monitor,
    Odometer,
    Document,
    Collection,
    Reading,
    Timer,
    DataAnalysis
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  height: 100vh;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 0;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 20px;
}

.title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 20px;
  font-weight: 600;
  color: white;
}

.nav-menu {
  background: transparent;
  border: none;
}

.nav-menu .el-menu-item {
  color: rgba(255, 255, 255, 0.8);
  border-bottom: 2px solid transparent;
}

.nav-menu .el-menu-item:hover,
.nav-menu .el-menu-item.is-active {
  color: white;
  background: rgba(255, 255, 255, 0.1);
  border-bottom-color: white;
}

.main-content {
  background: #f5f7fa;
  padding: 20px;
  min-height: calc(100vh - 60px);
}
</style>