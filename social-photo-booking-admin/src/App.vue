<template>
  <div class="app-container">
    <router-view v-if="!isLoggedIn" />

    <div v-else class="main-app">
      <el-container class="container">
        <el-aside width="240px" class="sidebar">
          <div class="logo">约拍平台管理后台</div>
          <el-menu
            :default-active="activeMenu"
            class="el-menu-vertical-demo"
            @select="handleMenuSelect"
          >
            <el-menu-item index="dashboard">
              <el-icon><PieChart /></el-icon>
              <span>数据看板</span>
            </el-menu-item>
            <el-menu-item index="user">
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
            <el-sub-menu index="review">
              <template #title>
                <el-icon><DocumentChecked /></el-icon>
                <span>入驻审核</span>
              </template>
              <el-menu-item index="photographer-review">摄影师审核</el-menu-item>
              <el-menu-item index="model-review">模特审核</el-menu-item>
            </el-sub-menu>
            <el-menu-item index="content">
              <el-icon><Picture /></el-icon>
              <span>内容审核</span>
            </el-menu-item>
            <el-menu-item index="order">
              <el-icon><Tickets /></el-icon>
              <span>订单管理</span>
            </el-menu-item>
            <el-menu-item index="report">
              <el-icon><Warning /></el-icon>
              <span>举报管理</span>
            </el-menu-item>
            <el-menu-item v-if="adminRole <= 2" index="system">
              <el-icon><Setting /></el-icon>
              <span>系统配置</span>
            </el-menu-item>
            <el-menu-item v-if="adminRole <= 2" index="admin-management">
              <el-icon><User /></el-icon>
              <span>管理员管理</span>
            </el-menu-item>
            <el-menu-item index="announcement">
              <el-icon><Bell /></el-icon>
              <span>公告管理</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-container>
          <el-header class="header">
            <div class="user-info">
              <span>{{ adminRoleName }}</span>
              <el-tooltip :content="adminPermissionDesc" placement="bottom">
                <el-button type="text" @click="showRoleInfo">角色信息</el-button>
              </el-tooltip>
              <el-button type="text" @click="handleLogout">退出登录</el-button>
            </div>
          </el-header>
          <el-main class="main">
            <router-view />
          </el-main>
        </el-container>
      </el-container>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, watch, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { PieChart, User, Picture, Tickets, Warning, Setting, Bell, DocumentChecked } from '@element-plus/icons-vue'

export default {
  name: 'App',
  components: {
    PieChart,
    User,
    Picture,
    Tickets,
    Warning,
    Setting,
    Bell,
    DocumentChecked
  },
  setup() {
    const router = useRouter()
    const route = useRoute()
    const activeMenu = ref('dashboard')
    const isLoggedIn = ref(false)
    const adminRole = ref(3)

    const handleMenuSelect = (index) => {
      activeMenu.value = index
      router.push(`/${index}`)
    }

    const handleLogout = () => {
      localStorage.removeItem('token')
      localStorage.removeItem('adminRole')
      isLoggedIn.value = false
      adminRole.value = 3
      router.push('/login')
    }

    const showRoleInfo = () => {
      alert(`当前角色：${adminRoleName.value}\n\n${adminPermissionDesc.value}`)
    }

    const checkLoginStatus = () => {
      const token = localStorage.getItem('token')
      const role = localStorage.getItem('adminRole')
      isLoggedIn.value = !!token

      if (role) {
        adminRole.value = parseInt(role)
      }

      if (!isLoggedIn.value && route.path !== '/login' && route.path !== '/register') {
        router.push('/login')
      }

      if (isLoggedIn.value && (route.path === '/login' || route.path === '/register')) {
        router.push('/dashboard')
      }
    }

    const adminRoleName = computed(() => {
      switch (adminRole.value) {
        case 1:
          return '超级管理员'
        case 2:
          return '高级管理员'
        case 3:
          return '普通管理员'
        default:
          return '普通管理员'
      }
    })

    const adminPermissionDesc = computed(() => {
      switch (adminRole.value) {
        case 1:
          return '权限范围：\n- 负责系统整体配置和维护\n- 管理所有管理员账号\n- 查看所有数据和操作日志'
        case 2:
          return '权限范围：\n- 管理普通管理员账号\n- 查看大部分数据，除了系统核心配置\n- 协助超级管理员进行日常管理'
        case 3:
          return '权限范围：\n- 负责日常业务操作\n- 只能查看和管理指定范围内的数据\n- 无法管理其他管理员账号'
        default:
          return '权限范围：\n- 负责日常业务操作\n- 只能查看和管理指定范围内的数据\n- 无法管理其他管理员账号'
      }
    })

    watch(() => route.path, () => {
      checkLoginStatus()
      const path = route.path.replace('/', '')
      if (path && path !== 'login' && path !== 'register') {
        activeMenu.value = path
      }
    })

    onMounted(() => {
      checkLoginStatus()
    })

    return {
      activeMenu,
      handleMenuSelect,
      handleLogout,
      isLoggedIn,
      adminRole,
      adminRoleName,
      adminPermissionDesc,
      showRoleInfo
    }
  }
}
</script>

<style scoped>
.app-container {
  width: 100%;
  min-height: 100vh;
  overflow: visible;
  background-color: #F8F9FA;
  position: relative;
}

.main-app {
  width: 100%;
  min-height: 100vh;
  overflow: visible;
  display: flex;
}

.container {
  width: 100%;
  min-height: 100vh;
  overflow: visible;
  display: flex;
}

:deep(.el-aside) {
  position: fixed;
  left: 0;
  top: 0;
  height: 100vh;
  z-index: 100;
  overflow: hidden;
}

:deep(.el-container > .el-container) {
  margin-left: 240px;
  width: calc(100% - 240px);
  min-height: 100vh;
  overflow: visible;
  display: flex;
  flex-direction: column;
}

.sidebar {
  background: linear-gradient(180deg, #A8E6CF 0%, #D4A5FF 100%);
  padding: 20px 0;
}

.logo {
  font-size: 18px;
  font-weight: 700;
  color: #FFFFFF;
  text-align: center;
  margin-bottom: 30px;
  padding: 0 20px;
}

:deep(.el-menu) {
  background: transparent;
  border: none;
}

:deep(.el-menu-item) {
  color: rgba(255, 255, 255, 0.8);
  margin: 8px 16px;
  border-radius: 12px;
}

:deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.2);
  color: #FFFFFF;
}

:deep(.el-menu-item.is-active) {
  background: rgba(255, 255, 255, 0.3);
  color: #FFFFFF;
}

:deep(.el-sub-menu__title) {
  color: rgba(255, 255, 255, 0.8);
  margin: 8px 16px;
  border-radius: 12px;
}

:deep(.el-sub-menu__title:hover) {
  background: rgba(255, 255, 255, 0.2);
  color: #FFFFFF;
}

.header {
  background: #FFFFFF;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0 24px;
  height: 64px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
  color: #333333;
  font-size: 14px;
}

.main {
  background: #F8F9FA;
  padding: 24px;
  min-height: calc(100vh - 64px);
  overflow: visible;
}

:deep(.el-aside) {
  overflow: hidden;
}

body {
  overflow-y: auto;
  margin: 0;
  padding: 0;
}

:deep(.el-main) {
  overflow: visible;
  min-height: calc(100vh - 64px);
}
</style>