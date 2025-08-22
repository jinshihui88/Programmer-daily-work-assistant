# 程序员日常工作助手

一个基于Spring Boot + MySQL + Vue.js的程序员日常工作管理系统，帮助程序员更好地管理工作日志、代码片段、学习进度和番茄钟计时。

## 功能特性

### 后端功能
- **每日工作日志记录** - 记录和管理每日工作内容
- **代码片段收藏夹** - 保存和分类常用代码片段
- **技术学习进度跟踪** - 跟踪学习项目的进度和状态
- **番茄钟计时器** - 专注时间管理工具

### 前端功能
- **简洁的仪表板界面** - 数据概览和快速操作
- **响应式设计** - 支持各种设备访问

### 数据导出功能
- **周报生成** - 自动生成周工作报告
- **月报生成** - 自动生成月工作报告
- **数据导出** - 支持导出为文本文件

## 技术栈

- **后端**: Spring Boot 2.7.18
- **数据库**: MySQL 8.0+
- **前端**: Vue.js 3 + Element Plus
- **构建工具**: Maven + Vite

## 项目结构

```
programmer-assistant/
├── backend-assistant/          # Spring Boot后端
│   ├── src/main/java/
│   │   └── com/programmer/assistant/
│   │       ├── entity/         # 实体类
│   │       ├── repository/     # 数据访问层
│   │       ├── service/        # 业务逻辑层
│   │       ├── controller/     # 控制器层
│   │       └── config/         # 配置类
│   └── src/main/resources/
│       ├── application.yml     # 应用配置
│       └── schema.sql         # 数据库初始化脚本
└── frontend-assistant/         # Vue.js前端
    ├── src/
    │   ├── views/             # 页面组件
    │   ├── api/               # API服务
    │   └── router/            # 路由配置
    └── package.json
```

## 快速开始

### 环境要求

- Java 8+
- Node.js 14+
- MySQL 8.0+
- Maven 3.6+

### 后端启动

1. **配置数据库**
   ```bash
   # 创建数据库
   mysql -u root -p
   CREATE DATABASE programmer_assistant CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **修改配置文件**
   编辑 `backend-assistant/src/main/resources/application.yml`，修改数据库连接信息：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/programmer_assistant
       username: your_username
       password: your_password
   ```

3. **启动后端服务**
   ```bash
   cd backend-assistant
   mvn clean install
   mvn spring-boot:run
   ```

   后端服务将在 http://localhost:8080 启动

### 前端启动

1. **安装依赖**
   ```bash
   cd frontend-assistant
   npm install
   ```

2. **启动开发服务器**
   ```bash
   npm run dev
   ```

   前端应用将在 http://localhost:5173 启动

### 数据库初始化

首次启动时，系统会自动执行 `schema.sql` 脚本创建表结构并插入示例数据。

## API接口

### 工作日志 API
- `GET /api/work-logs` - 获取工作日志列表
- `POST /api/work-logs` - 创建工作日志
- `PUT /api/work-logs/{id}` - 更新工作日志
- `DELETE /api/work-logs/{id}` - 删除工作日志

### 代码片段 API
- `GET /api/code-snippets` - 获取代码片段列表
- `POST /api/code-snippets` - 创建代码片段
- `PUT /api/code-snippets/{id}` - 更新代码片段
- `DELETE /api/code-snippets/{id}` - 删除代码片段

### 学习进度 API
- `GET /api/learning-progress` - 获取学习进度列表
- `POST /api/learning-progress` - 创建学习项目
- `PUT /api/learning-progress/{id}` - 更新学习进度
- `DELETE /api/learning-progress/{id}` - 删除学习项目

### 番茄钟 API
- `POST /api/pomodoro/start` - 开始番茄钟
- `PUT /api/pomodoro/{id}/complete` - 完成番茄钟
- `PUT /api/pomodoro/{id}/interrupt` - 中断番茄钟
- `GET /api/pomodoro/recent` - 获取最近记录

### 报表 API
- `GET /api/reports/weekly` - 获取周报
- `GET /api/reports/monthly` - 获取月报

## 使用说明

### 1. 工作日志管理
- 在"工作日志"页面记录每日工作内容
- 支持按日期筛选和搜索
- 可以编辑和删除已有日志

### 2. 代码片段管理
- 在"代码片段"页面保存常用代码
- 支持多种编程语言语法高亮
- 可以添加标签进行分类管理

### 3. 学习进度跟踪
- 在"学习进度"页面创建学习项目
- 跟踪学习进度和时间投入
- 设置目标日期和学习时长

### 4. 番茄钟计时
- 在"番茄钟"页面开始专注工作
- 默认25分钟，可自定义时长
- 记录完成和中断的会话

### 5. 数据报表
- 在"数据报表"页面查看统计信息
- 支持周报和月报切换
- 可导出报表为文本文件

## 开发说明

### 后端开发
- 使用Spring Boot 2.7.18框架
- JPA/Hibernate进行数据持久化
- RESTful API设计
- 统一异常处理和响应格式

### 前端开发
- Vue.js 3 Composition API
- Element Plus UI组件库
- Axios进行HTTP请求
- Vue Router进行路由管理

## 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 联系方式

如有问题或建议，请提交 Issue 或联系开发者。

---

**享受高效的编程工作管理体验！** 🚀