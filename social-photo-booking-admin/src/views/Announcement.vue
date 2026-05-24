<template>
  <div class="announcement-container">
    <h2>公告管理</h2>
    
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="公告ID">
          <el-input v-model="searchForm.announcementId" placeholder="请输入公告ID" />
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="primary" @click="handleAdd">添加公告</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card class="table-card">
      <el-table :data="announcementList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="公告ID" width="100" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag size="small" :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{scope.row.status === 1 ? '发布' : '草稿'}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="更新时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.updateTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination" style="margin-top: 20px;">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 添加/编辑公告对话框-->
    <el-dialog v-model="dialogVisible" :title="announcementForm.id ? '编辑公告' : '添加公告'" width="80%">
      <el-form :model="announcementForm" :rules="announcementRules" ref="announcementFormRef">
        <el-form-item label="标题" prop="title">
          <el-input v-model="announcementForm.title" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="announcementForm.content" type="textarea" rows="10" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="announcementForm.status" />
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
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'Announcement',
  setup() {
    const searchForm = reactive({
      announcementId: '',
      title: ''
    })
    
    const announcementList = ref([])
    const currentPage = ref(1)
    const pageSize = ref(10)
    const total = ref(0)
    const loading = ref(false)
    
    const dialogVisible = ref(false)
    const announcementFormRef = ref(null)
    const announcementForm = reactive({
      id: '',
      title: '',
      content: '',
      status: true
    })
    
    const announcementRules = {
      title: [
        { required: true, message: '请输入标题', trigger: 'blur' }
      ],
      content: [
        { required: true, message: '请输入内容', trigger: 'blur' }
      ]
    }
    
    const getAnnouncements = async () => {
      loading.value = true
      try {
        const response = await axios.get('http://localhost:8086/admin/notice/list', {
          params: {
            pageNum: currentPage.value,
            pageSize: pageSize.value,
            id: searchForm.announcementId,
            title: searchForm.title
          }
        })
        
        if (response.data.code === 200) {
          announcementList.value = response.data.data.list
          total.value = response.data.data.total
        } else {
          console.error('获取公告列表失败:', response.data.message)
        }
      } catch (error) {
        console.error('获取公告列表失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    const handleSearch = () => {
      currentPage.value = 1
      getAnnouncements()
    }
    
    const resetSearch = () => {
      searchForm.announcementId = ''
      searchForm.title = ''
      currentPage.value = 1
      getAnnouncements()
    }
    
    const handleAdd = () => {
      announcementForm.id = ''
      announcementForm.title = ''
      announcementForm.content = ''
      announcementForm.status = true
      dialogVisible.value = true
    }
    
    const handleEdit = (row) => {
      announcementForm.id = row.id
      announcementForm.title = row.title
      announcementForm.content = row.content
      announcementForm.status = row.status
      dialogVisible.value = true
    }
    
    const handleSave = async () => {
      if (!announcementFormRef.value) return
      
      try {
        await announcementFormRef.value.validate()
        // 处理保存逻辑
        console.log('保存公告:', announcementForm)
        dialogVisible.value = false
      } catch (error) {
        console.error('验证失败:', error)
      }
    }
    
    const formatTime = (time) => {
      if (!time) return '-'
      // 处理数组格式的时间 [year, month, day, hour, minute, second]
      if (Array.isArray(time) && time.length >= 5) {
        const [year, month, day, hour, minute, second = 0] = time
        return `${year}.${month.toString().padStart(2, '0')}.${day.toString().padStart(2, '0')} ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:${second.toString().padStart(2, '0')}`
      }
      // 处理其他格式的时间
      const date = new Date(time)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      const second = String(date.getSeconds()).padStart(2, '0')
      return `${year}.${month}.${day} ${hour}:${minute}:${second}`
    }
    
    const handleDelete = (row) => {
      // 处理删除逻辑
      console.log('删除公告:', row)
    }
    
    const handleSizeChange = (size) => {
      pageSize.value = size
      getAnnouncements()
    }
    
    const handleCurrentChange = (current) => {
      currentPage.value = current
      getAnnouncements()
    }
    
    // 组件初始化时获取公告列表
    onMounted(() => {
      getAnnouncements()
    })
    
    return {
      searchForm,
      announcementList,
      currentPage,
      pageSize,
      total,
      loading,
      dialogVisible,
      announcementFormRef,
      announcementForm,
      announcementRules,
      formatTime,
      handleSearch,
      resetSearch,
      handleAdd,
      handleEdit,
      handleSave,
      handleDelete,
      handleSizeChange,
      handleCurrentChange
    }
  }
}
</script>

<style scoped>
.announcement-container {
  padding: 20px;
}

.announcement-container h2 {
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
