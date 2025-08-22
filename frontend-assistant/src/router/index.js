import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import WorkLogs from '../views/WorkLogs.vue'
import CodeSnippets from '../views/CodeSnippets.vue'
import Learning from '../views/Learning.vue'
import Pomodoro from '../views/Pomodoro.vue'
import Reports from '../views/Reports.vue'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/work-logs',
    name: 'WorkLogs',
    component: WorkLogs
  },
  {
    path: '/code-snippets',
    name: 'CodeSnippets',
    component: CodeSnippets
  },
  {
    path: '/learning',
    name: 'Learning',
    component: Learning
  },
  {
    path: '/pomodoro',
    name: 'Pomodoro',
    component: Pomodoro
  },
  {
    path: '/reports',
    name: 'Reports',
    component: Reports
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router