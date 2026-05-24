<template>
  <div class="admin-management">
    <div class="page-header">
      <h2>管理员管理</h2>
      <el-button type="primary" @click="showCreateDialog" :disabled="!canCreateAdmin">
        <el-icon><Plus /></el-icon>
        添加管理员
      </el-button>
    </div>

    <el-table :data="adminList" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="150" />
      <el-table-column prop="realName" label="真实姓名" width="150" />
      <el-table-column prop="role" label="角色" width="120">
        <template #default="{ row }">
          <el-tag :type="getRoleType(row.role)">{{ getRoleName(row.role) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column prop="lastLoginTime" label="最后登录时间" width="180" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="showEditDialog(row)" :disabled="!canEditAdmin(row)">
            编辑
          </el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)" :disabled="!canDeleteAdmin(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 创建/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑管理员' : '添加管理员'" width="500px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" :disabled="isEdit" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!isEdit">
          <el-input v-model="formData.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="formData.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="formData.role" placeholder="请选择角色" :disabled="!canSelectRole">
            <el-option v-for="role in availableRoles" :key="role.value" :label="role.label" :value="role.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="formData.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import axios from 'axios'
import { Plus } from '@element-plus/icons-vue'

export default {
  name: 'AdminManagement',
  components: {
    Plus
  },
  setup() {
    const adminList = ref([])
    const dialogVisible = ref(false)
    const isEdit = ref(false)
    const formRef = ref(null)
    const adminRole = ref(3) // 默认普通管理员

    const formData = reactive({
      id: null,
      username: '',
      password: '',
      realName: '',
      role: 3,
      status: 1
    })

    const formRules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
      ],
      realName: [
        { required: true, message: '请输入真实姓名', trigger: 'blur' }
      ],
      role: [
        { required: true, message: '请选择角色', trigger: 'change' }
      ]
    }

    // 可用角色列表
    const availableRoles = computed(() => {
      const roles = [
        { label: '普通管理员', value: 3 }
      ]
      
      // 高级管理员及以上可以创建普通管理员
      if (adminRole.value <= 2) {
        roles.push({ label: '高级管理员', value: 2 })
      }
      
      // 只有超级管理员可以创建超级管理员
      if (adminRole.value === 1) {
        roles.push({ label: '超级管理员', value: 1 })
      }
      
      return roles
    })

    // 是否可以创建管理员
    const canCreateAdmin = computed(() => {
      return adminRole.value <= 2
    })

    // 是否可以选择角色
    const canSelectRole = computed(() => {
      return adminRole.value === 1
    })

    // 获取角色名称
    const getRoleName = (role) => {
      switch (role) {
        case 1:
          return '超级管理员'
        case 2:
          return '高级管理员'
        case 3:
          return '普通管理员'
        default:
          return '未知'
      }
    }

    // 获取角色类型
    const getRoleType = (role) => {
      switch (role) {
        case 1:
          return 'danger'
        case 2:
          return 'warning'
        case 3:
          return 'info'
        default:
          return ''
      }
    }

    // 是否可以编辑管理员
    const canEditAdmin = (row) => {
      // 超级管理员可以编辑所有管理员
      if (adminRole.value === 1) {
        return true
      }
      // 高级管理员可以编辑普通管理员
      if (adminRole.value === 2 && row.role === 3) {
        return true
      }
      return false
    }

    // 是否可以删除管理员
    const canDeleteAdmin = (row) => {
      // 不能删除自己
      if (row.id === getCurrentAdminId()) {
        return false
      }
      // 超级管理员可以删除所有管理员
      if (adminRole.value === 1) {
        return true
      }
      // 高级管理员可以删除普通管理员
      if (adminRole.value === 2 && row.role === 3) {
        return true
      }
      return false
    }

    // 获取当前管理员ID（从token解析，这里简化处理）
    const getCurrentAdminId = () => {
      // 实际应该从token中解析，这里简化处理
      return 1
    }

    // 加载管理员列表
    const loadAdminList = async () => {
      try {
        const response = await axios.get('http://localhost:8086/admin/admin/list')
        if (response.data.code === 200) {
          adminList.value = response.data.data
        }
      } catch (error) {
        console.error('加载管理员列表失败', error)
      }
    }

    // 显示创建对话框
    const showCreateDialog = () => {
      isEdit.value = false
      formData.id = null
      formData.username = ''
      formData.password = ''
      formData.realName = ''
      formData.role = 3
      formData.status = 1
      dialogVisible.value = true
    }

    // 显示编辑对话框
    const showEditDialog = (row) => {
      isEdit.value = true
      formData.id = row.id
      formData.username = row.username
      formData.password = ''
      formData.realName = row.realName
      formData.role = row.role
      formData.status = row.status
      dialogVisible.value = true
    }

    // 提交表单
    const handleSubmit = async () => {
      if (!formRef.value) return
      
      try {
        await formRef.value.validate()
        
        const data = {
          username: formData.username,
          realName: formData.realName,
          role: formData.role,
          status: formData.status
        }
        
        // 编辑时不需要密码，创建时需要密码
        if (!isEdit.value) {
          data.password = formData.password
        }
        
        // 如果是编辑，添加ID
        if (isEdit.value) {
          data.id = formData.id
        }
        
        const url = isEdit.value ? 'http://localhost:8086/admin/admin/update' : 'http://localhost:8086/admin/admin/create'
        const response = await axios.post(url, data)
        
        if (response.data.code === 200) {
          alert(isEdit.value ? '编辑成功' : '创建成功')
          dialogVisible.value = false
          loadAdminList()
        } else {
          alert(response.data.message)
        }
      } catch (error) {
        console.error('提交失败:', error)
        alert('操作失败，请稍后重试')
      }
    }

    // 删除管理员
    const handleDelete = async (row) => {
      if (!confirm(`确定要删除管理员 ${row.realName} 吗？`)) {
        return
      }
      
      try {
        const response = await axios.post('http://localhost:8086/admin/admin/delete', null, {
          params: { id: row.id }
        })
        
        if (response.data.code === 200) {
          alert('删除成功')
          loadAdminList()
        } else {
          alert(response.data.message)
        }
      } catch (error) {
        console.error('删除失败:', error)
        alert('删除失败，请稍后重试')
      }
    }

    onMounted(() => {
      // 获取当前管理员角色
      const role = localStorage.getItem('adminRole')
      if (role) {
        adminRole.value = parseInt(role)
      }
      
      // 加载管理员列表
      loadAdminList()
    })

    return {
      adminList,
      dialogVisible,
      isEdit,
      formRef,
      formData,
      formRules,
      availableRoles,
      canCreateAdmin,
      canSelectRole,
      getRoleName,
      getRoleType,
      canEditAdmin,
      canDeleteAdmin,
      showCreateDialog,
      showEditDialog,
      handleSubmit,
      handleDelete
    }
  }
}
</script>

<style scoped>
.admin-management {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.el-table {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-dialog__body) {
  padding: 20px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}
</style>