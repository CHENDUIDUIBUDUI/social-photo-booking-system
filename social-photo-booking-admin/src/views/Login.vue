<template>
  <div class="login-container">
    <!-- 背景光斑 -->
    <div class="light-spot spot-1"></div>
    <div class="light-spot spot-2"></div>
    <div class="light-spot spot-3"></div>
    
    <!-- 登录内容 -->
    <div class="login-content">
      <!-- 标题 -->
      <h1 class="main-title">约拍系统管理后台</h1>
      <p class="sub-title">管理员登录</p>
      
      <!-- 表单 -->
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" @keyup.enter="handleLogin">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入管理员用户名" prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            :type="passwordVisible ? 'text' : 'password'" 
            placeholder="请输入登录密码" 
            prefix-icon="Lock" 
            @keyup.enter="handleLogin"
          >
            <template #suffix>
              <el-icon class="password-eye" @click="passwordVisible = !passwordVisible">
                <View v-if="passwordVisible" />
                <Hide v-else />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
      
      <!-- 操作 -->
      <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">登录</el-button>
      <div class="bottom-actions">
        <el-checkbox v-model="loginForm.remember">记住密码</el-checkbox>
        <div class="right-actions">
          <el-button type="text" class="forgot-password">忘记密码</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { View, Hide } from '@element-plus/icons-vue'

export default {
  name: 'Login',
  components: {
    View,
    Hide
  },
  setup() {
    const router = useRouter()
    const loginFormRef = ref(null)
    const loading = ref(false)
    const passwordVisible = ref(false)
    
    const loginForm = reactive({
      username: '',
      password: '',
      remember: false
    })
    
    const loginRules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ]
    }
    
    const handleLogin = async () => {
      if (!loginFormRef.value) return
      
      try {
        await loginFormRef.value.validate()
        loading.value = true
        
        console.log('开始登录，用户名:', loginForm.username)
        console.log('登录请求URL:', 'http://localhost:8086/admin/admin/login')
        
        // 实际登录请求
        const response = await axios.post('http://localhost:8086/admin/admin/login', {
          username: loginForm.username,
          password: loginForm.password
        }, {
          timeout: 10000, // 10秒超时
          headers: {
            'Content-Type': 'application/json'
          }
        })
        
        console.log('登录请求响应:', response.data)
        
        if (response.data.code === 200) {
          // 登录成功
          localStorage.setItem('token', response.data.data.token) // 使用后端返回的token
          // 保存管理员角色信息
          if (response.data.data.user && response.data.data.user.role) {
            localStorage.setItem('adminRole', response.data.data.user.role)
          }
          router.push('/dashboard')
        } else {
          // 登录失败
          console.log('登录失败:', response.data.message)
          alert(response.data.message)
        }
        loading.value = false
      } catch (error) {
        console.error('登录失败:', error)
        if (error.response) {
          // 服务器返回错误
          console.error('错误响应:', error.response.data)
          const errorMessage = error.response.data.message || error.response.data.error || '服务器错误'
          alert('登录失败：' + errorMessage)
        } else if (error.request) {
          // 请求发送但没有收到响应
          console.error('请求未响应:', error.request)
          alert('登录失败：无法连接到服务器，请检查后端服务是否运行')
        } else {
          // 请求配置错误
          console.error('请求错误:', error.message)
          alert('登录失败：' + (error.message || '未知错误'))
        }
        loading.value = false
      }
    }
    
    return {
      loginForm,
      loginRules,
      loginFormRef,
      loading,
      passwordVisible,
      handleLogin
    }
  }
}
</script>

<style scoped>
/* 全局样式 */
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #F8F9FA; /* 背景 / 页面底色 */
  position: relative;
  overflow: hidden;
}

/* 背景光斑 */
.light-spot {
  position: absolute;
  border-radius: 50%;
  background: linear-gradient(90deg, rgba(168, 230, 207, 0.3) 0%, rgba(212, 165, 255, 0.3) 100%); /* 渐变 / 主色浅，30% 不透明度 */
  filter: blur(50px);
}

.spot-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  left: -100px;
}

.spot-2 {
  width: 200px;
  height: 200px;
  bottom: -50px;
  right: 200px;
}

.spot-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  right: -50px;
}

/* 登录内容 */
.login-content {
  width: 440px;
  padding: 40px 32px;
  background: rgba(255, 255, 255, 0.8); /* 填充 / 玻璃态底色 */
  border: 1px solid rgba(255, 255, 255, 0.3); /* 边框 / 玻璃态 */
  border-radius: 20px; /* 圆角 / 大 */
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.07); /* 效果 / 卡片阴影 */
  backdrop-filter: blur(20px); /* 效果 / 玻璃态 */
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 28px;
  z-index: 1;
}

/* 标题 */
.main-title {
  font-size: 24px;
  font-weight: 700;
  line-height: 32px;
  background: linear-gradient(90deg, #A8E6CF 0%, #D4A5FF 100%); /* 渐变 / 主色 */
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin: 0;
  text-align: center;
}

.sub-title {
  font-size: 14px;
  font-weight: 400;
  line-height: 20px;
  color: #8E8E8E; /* 文字 / 辅助 */
  margin: 0;
  text-align: center;
  margin-top: 8px;
}

/* 表单 */
el-form {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 输入框样式 */
:deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 12px;
  backdrop-filter: blur(20px);
}

:deep(.el-input__wrapper:focus-within) {
  border-color: #A8E6CF;
  box-shadow: 0 0 0 4px rgba(168, 230, 207, 0.3);
}

/* 密码眼睛图标 */
.password-eye {
  cursor: pointer;
  color: #8E8E8E;
  font-size: 16px;
}

.password-eye:hover {
  color: #A8E6CF;
}

/* 登录按钮 */
.login-btn {
  width: 100%;
  padding: 12px 24px;
  background: linear-gradient(90deg, #A8E6CF 0%, #D4A5FF 100%); /* 渐变 / 主色 */
  border: none;
  border-radius: 16px; /* 圆角 / 大 */
  font-size: 16px;
  font-weight: 600;
  line-height: 24px;
  color: #FFFFFF; /* 文字 / 反白 */
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.07); /* 效果 / 卡片阴影 */
  transition: all 0.3s ease;
  margin-top: 10px;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px 0 rgba(168, 230, 207, 0.3); /* 效果 / 按钮悬浮阴影 */
}

.login-btn:active {
  transform: scale(0.98);
}

/* 底部辅助 */
.bottom-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.remember-me {
  font-size: 12px;
  font-weight: 400;
  line-height: 16px;
  color: #8E8E8E;
}

.right-actions {
  display: flex;
  gap: 16px;
}

.forgot-password {
  font-size: 12px;
  font-weight: 400;
  line-height: 16px;
  color: #8E8E8E;
}

.forgot-password:hover {
  color: #A8E6CF;
}

.register-link {
  font-size: 12px;
  font-weight: 400;
  line-height: 16px;
  color: #A8E6CF;
}

.register-link:hover {
  color: #D4A5FF;
}
</style>