# 星海智伴 - 养老护理管理系统

基于若依（RuoYi）框架开发的养老护理管理系统前端项目。

## 项目概述

- **项目名称**: 星海智伴
- **技术栈**: Vue 3 + Vite + Element Plus + Pinia
- **开发环境**: Node.js
- **基础框架**: 若依（RuoYi）Vue3 版本 v3.8.7

## 技术栈详情

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.4.0 | 前端框架 |
| Vite | 5.0.4 | 构建工具 |
| Element Plus | 2.4.3 | UI 组件库 |
| Pinia | 2.1.7 | 状态管理 |
| Vue Router | 4.2.5 | 路由管理 |
| Axios | 0.27.2 | HTTP 请求 |
| Echarts | 5.4.3 | 数据可视化 |
| Sass | 1.69.5 | CSS 预处理器 |

## 目录结构

```
xhzb_ui/
├── public/                 # 静态资源
├── src/
│   ├── api/               # API 接口
│   │   ├── monitor/       # 监控相关接口
│   │   ├── nursing/       # 养老护理相关接口
│   │   ├── system/        # 系统管理接口
│   │   └── tool/          # 工具类接口
│   ├── assets/            # 资源文件
│   │   ├── 401_images/    # 401 错误图片
│   │   ├── 404_images/    # 404 错误图片
│   │   ├── icons/         # 图标
│   │   ├── images/        # 图片资源
│   │   ├── logo/          # Logo
│   │   └── styles/        # 全局样式
│   ├── components/        # 公共组件
│   │   ├── Breadcrumb/    # 面包屑导航
│   │   ├── Pagination/    # 分页组件
│   │   ├── Editor/        # 富文本编辑器
│   │   ├── FileUpload/    # 文件上传
│   │   ├── ImageUpload/   # 图片上传
│   │   ├── ImagePreview/  # 图片预览
│   │   ├── TreeSelect/    # 树选择组件
│   │   ├── DictTag/       # 字典标签
│   │   └── ...            # 其他组件
│   ├── directive/         # 自定义指令
│   ├── layout/            # 布局组件
│   ├── plugins/           # 插件
│   ├── router/            # 路由配置
│   ├── store/             # 状态管理
│   │   └── modules/       # Pinia 模块
│   │       ├── app.js           # 应用状态
│   │       ├── dict.js          # 字典数据
│   │       ├── permission.js    # 权限管理
│   │       ├── settings.js      # 系统设置
│   │       ├── tagsView.js      # 标签页视图
│   │       └── user.js          # 用户信息
│   ├── utils/             # 工具函数
│   ├── views/             # 页面视图
│   │   ├── dashboard/     # 仪表盘
│   │   ├── monitor/       # 系统监控
│   │   ├── nursing/       # 养老护理业务模块
│   │   ├── system/        # 系统管理
│   │   └── tool/          # 系统工具
│   ├── App.vue            # 根组件
│   ├── main.js            # 入口文件
│   ├── permission.js      # 权限控制
│   └── settings.js        # 全局配置
├── vite/                  # Vite 配置
│   └── plugins/           # Vite 插件
├── .env                   # 环境变量
├── .env.development       # 开发环境配置
├── .env.production        # 生产环境配置
├── .env.staging           # 预发环境配置
├── vite.config.js         # Vite 配置文件
├── package.json           # 项目依赖
└── index.html             # HTML 模板
```

## 主要功能模块

### 1. 养老护理模块 (nursing/)
- `alertData` - 告警数据管理
- `alertRule` - 告警规则管理
- `arrange` - 排班管理
- `checkIn` - 签到/签退管理
- `contract` - 合同管理
- `device` - 设备管理
- `floor` - 楼层管理
- `healthAssessment` - 健康评估
- `knowledgeBase` - 知识库管理
- `nursingLevel` - 护理等级管理
- `nursingPlan` - 护理计划管理
- `oldPeople` - 老人信息管理
- `project` - 项目管理
- `reservation` - 预约管理
- `roomType` - 房间类型管理
- `smartBed` - 智能床管理
- `zhixun` - 智询管理

### 2. 系统管理模块 (system/)
- `user` - 用户管理
- `role` - 角色管理
- `menu` - 菜单管理
- `dept` - 部门管理
- `post` - 岗位管理
- `dict` - 字典管理
- `config` - 参数配置
- `notice` - 通知公告

### 3. 系统监控模块 (monitor/)
- `online` - 在线用户
- `job` - 定时任务
- `druid` - 数据监控
- `cache` - 缓存监控
- `server` - 服务监控
- `operlog` - 操作日志
- `logininfor` - 登录日志

### 4. 系统工具模块 (tool/)
- `build` - 表单构建
- `gen` - 代码生成
- `swagger` - 系统接口

## 快速开始

### 环境要求

- Node.js >= 14.x
- npm >= 6.x 或 pnpm >= 7.x

### 安装依赖

```bash
# 使用 npm
npm install

# 或使用 pnpm（推荐）
pnpm install
```

### 启动开发服务器

```bash
# 使用 npm
npm run dev

# 或使用 pnpm
pnpm dev
```

启动后默认访问地址: `http://localhost:9001`

### 构建生产环境

```bash
# 生产环境构建
npm run build:prod

# 预发环境构建
npm run build:stage
```

### 预览构建结果

```bash
npm run preview
```

## 环境配置说明

### 开发环境 (.env.development)
```bash
VITE_APP_TITLE = 星海智伴
VITE_APP_ENV = 'development'
VITE_APP_BASE_API = '/dev-api'
```

### 生产环境 (.env.production)
```bash
VITE_APP_TITLE = 星海智伴
VITE_APP_ENV = 'production'
VITE_APP_BASE_API = '/prod-api'
```

### 代理配置

开发环境下，`/dev-api` 请求会被代理到后端服务：

```javascript
// vite.config.js
proxy: {
  '/dev-api': {
    target: 'http://localhost:8080',
    changeOrigin: true,
    rewrite: (p) => p.replace(/^\/dev-api/, '')
  }
}
```

**注意**: 修改 `target` 地址以匹配你的后端服务地址。

## 全局组件

项目已全局注册以下组件，可直接在任何 Vue 文件中使用：

- `<Pagination>` - 分页组件
- `<TreeSelect>` - 树选择组件
- `<FileUpload>` - 文件上传组件
- `<ImageUpload>` - 图片上传组件
- `<ImagePreview>` - 图片预览组件
- `<RightToolbar>` - 表格工具栏组件
- `<Editor>` - 富文本编辑器组件
- `<DictTag>` - 字典标签组件
- `<svg-icon>` - SVG 图标组件

## 全局方法

以下方法已挂载到全局属性上：

```javascript
// 字典相关
useDict(...types)

// 请求相关
download(url, params, filename)

// 工具方法
parseTime(time, pattern)
resetForm(ref)
handleTree(data, id, parentId, children)
addDateRange(params, dateRange)
selectDictLabel(datas, value)
selectDictLabels(datas, values)
```

## 常见问题

### 端口被占用
如果 9001 端口被占用，可以在 `vite.config.js` 中修改 `server.port` 配置。

### 跨域问题
开发环境通过 Vite 的 proxy 配置解决跨域问题，确保后端服务地址配置正确。

### 样式问题
全局样式文件位于 `src/assets/styles/index.scss`，可在此文件中修改全局样式。

## 许可证

MIT License

## 参考资料

- [Vue 3 文档](https://cn.vuejs.org/)
- [Vite 文档](https://cn.vitejs.dev/)
- [Element Plus 文档](https://element-plus.org/)
- [若依官网](http://ruoyi.vip/)
