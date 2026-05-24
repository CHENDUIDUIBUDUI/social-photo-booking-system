<template>
  <div class="user-container">
    <h2>用户管理</h2>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="searchForm.nickname" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="searchForm.role" placeholder="请选择角色">
            <el-option label="全部" value="" />
            <el-option label="普通用户" value="0" />
            <el-option label="摄影师" value="1" />
            <el-option label="模特" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态">
            <el-option label="全部" value="" />
            <el-option label="启用" value="1" />
            <el-option label="禁用" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table :data="userList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="用户ID" width="100" />
        <el-table-column prop="nickname" label="用户名" />
        <el-table-column prop="avatar" label="头像" width="100">
          <template #default="scope">
            <el-avatar :size="40" :src="scope.row.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + scope.row.id" />
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="150" />
        <el-table-column prop="role" label="角色" width="100">
          <template #default="scope">
            <el-tag size="small" :type="getRoleType(scope.row.role)">
              {{getRoleText(scope.row.role)}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag size="small" :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{scope.row.status === 1 ? '启用' : '禁用'}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creditScore" label="信用分" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="warning" size="small" @click="handleToggleStatus(scope.row)">
              {{scope.row.status === 1 ? '禁用' : '启用'}}
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination" style="margin-top: 20px;">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="10"
          layout="total, prev, pager, next, jumper"
          :total="total"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" title="编辑用户">
      <el-form :model="editForm" :rules="editRules" ref="editFormRef">
        <el-form-item label="用户名" prop="nickname">
          <el-input v-model="editForm.nickname" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="editForm.phone" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="editForm.role">
            <el-option label="普通用户" value="0" />
            <el-option label="摄影师" value="1" />
            <el-option label="模特" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="信用分" prop="creditScore">
          <el-input v-model.number="editForm.creditScore" type="number" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'User',
  setup() {
    const searchForm = reactive({
      userId: '',
      nickname: '',
      role: '',
      status: ''
    })

    const userList = ref([])
    const currentPage = ref(1)
    const total = ref(0)
    const loading = ref(false)

    const dialogVisible = ref(false)
    const editFormRef = ref(null)
    const editForm = reactive({
      id: '',
      nickname: '',
      phone: '',
      role: '',
      creditScore: ''
    })

    const editRules = {
      nickname: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' }
      ],
      role: [
        { required: true, message: '请选择角色', trigger: 'change' }
      ],
      creditScore: [
        { required: true, message: '请输入信用分', trigger: 'blur' }
      ]
    }

    const getRoleText = (role) => {
      switch (role) {
        case 0:
          return '普通用户'
        case 1:
          return '摄影师'
        case 2:
          return '模特'
        default:
          return '普通用户'
      }
    }

    const getRoleType = (role) => {
      switch (role) {
        case 0:
          return 'info'
        case 1:
          return 'primary'
        case 2:
          return 'success'
        default:
          return 'info'
      }
    }

    const formatTime = (time) => {
      if (!time) return '-'
      if (Array.isArray(time) && time.length >= 5) {
        const [year, month, day, hour, minute, second = 0] = time
        return `${year}.${month.toString().padStart(2, '0')}.${day.toString().padStart(2, '0')} ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:${second.toString().padStart(2, '0')}`
      }
      const date = new Date(time)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      const second = String(date.getSeconds()).padStart(2, '0')
      return `${year}.${month}.${day} ${hour}:${minute}:${second}`
    }

    const getUsers = async () => {
      loading.value = true
      try {
        let role = searchForm.role
        if (role === '0') role = 3
        else if (role === '1') role = 1
        else if (role === '2') role = 2
        else role = null

        const response = await axios.get('/admin/api/list', {
          params: {
            pageNum: currentPage.value,
            pageSize: 10,
            nickname: searchForm.nickname,
            phone: searchForm.userId,
            role: role
          }
        })

        if (response.data.code === 200) {
          userList.value = response.data.data.list.map(user => {
            if (user.roleId === 1) user.role = 1
            else if (user.roleId === 2) user.role = 2
            else if (user.roleId === 3) user.role = 0
            else user.role = 0
            return user
          })
          total.value = response.data.data.total
        } else {
          console.error('获取用户列表失败:', response.data.message)
        }
      } catch (error) {
        console.error('获取用户列表失败:', error)
      } finally {
        loading.value = false
      }
    }

    const handleSearch = () => {
      currentPage.value = 1
      getUsers()
    }

    const resetSearch = () => {
      searchForm.userId = ''
      searchForm.nickname = ''
      searchForm.role = ''
      searchForm.status = ''
      currentPage.value = 1
      getUsers()
    }

    const handleEdit = (row) => {
      editForm.id = row.id
      editForm.nickname = row.nickname
      editForm.phone = row.phone
      editForm.role = row.role
      editForm.creditScore = row.creditScore
      dialogVisible.value = true
    }

    const handleSave = async () => {
      if (!editFormRef.value) return

      try {
        await editFormRef.value.validate()
        console.log('保存用户:', editForm)
        dialogVisible.value = false
      } catch (error) {
        console.error('验证失败:', error)
      }
    }

    const handleToggleStatus = async (row) => {
      try {
        const response = await axios.post('/admin/api/updateStatus', {
          id: row.id,
          status: row.status === 1 ? 0 : 1
        })

        if (response.data.code === 200) {
          getUsers()
        } else {
          console.error('更新状态失败', response.data.message)
        }
      } catch (error) {
        console.error('更新状态失败', error)
      }
    }

    const handleDelete = (row) => {
      console.log('删除用户:', row)
    }

    const handleCurrentChange = (current) => {
      currentPage.value = current
      getUsers()
    }

    onMounted(() => {
      getUsers()
    })

    return {
      searchForm,
      userList,
      currentPage,
      total,
      loading,
      dialogVisible,
      editFormRef,
      editForm,
      editRules,
      getRoleText,
      getRoleType,
      formatTime,
      handleSearch,
      resetSearch,
      handleEdit,
      handleSave,
      handleToggleStatus,
      handleDelete,
      handleCurrentChange
    }
  }
}
</script>

<style scoped>
.user-container {
  padding: 20px;
}

.user-container h2 {
  margin-bottom: 20px;
  color: #303133;
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 0;
}

.table-card {
  margin-top: 20px;
}
</style>