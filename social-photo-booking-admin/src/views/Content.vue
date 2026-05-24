<template>
  <div class="content-container">
    <h2>内容审核</h2>
    
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="内容ID">
          <el-input v-model="searchForm.contentId" placeholder="请输入内容ID" />
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="searchForm.type" placeholder="请选择类型">
            <el-option label="全部" value="" />
            <el-option label="作品" value="0" />
            <el-option label="需求" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态">
            <el-option label="全部" value="" />
            <el-option label="待审核" value="0" />
            <el-option label="审核通过" value="1" />
            <el-option label="审核拒绝" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card class="table-card">
      <el-table :data="contentList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="内容ID" width="100" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="scope">
            <el-tag size="small" :type="scope.row.type === 0 ? 'primary' : 'success'">
              {{scope.row.type === 0 ? '作品' : '需求'}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="scope">
            ¥{{scope.row.price}}
          </template>
        </el-table-column>
        <el-table-column prop="location" label="地点" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag size="small" :type="getStatusType(scope.row.status)">
              {{getStatusText(scope.row.status)}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button v-if="scope.row.status === 0" type="warning" size="small" @click="handleAudit(scope.row)">审核</el-button>
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
    
    <!-- 查看内容对话框-->
    <el-dialog v-model="viewDialogVisible" title="查看内容">
      <div class="view-content">
        <div class="view-item">
          <span class="view-label">标题：</span>
          <span class="view-value">{{viewContent.title}}</span>
        </div>
        <div class="view-item">
          <span class="view-label">描述：</span>
          <span class="view-value">{{viewContent.description}}</span>
        </div>
        <div class="view-item">
          <span class="view-label">价格：</span>
          <span class="view-value">¥{{viewContent.price}}</span>
        </div>
        <div class="view-item">
          <span class="view-label">地点：</span>
          <span class="view-value">{{viewContent.location}}</span>
        </div>
        <div class="view-item">
          <span class="view-label">标签：</span>
          <span class="view-value">{{viewContent.tags}}</span>
        </div>
        <div class="view-item">
          <span class="view-label">图片：</span>
          <div class="view-images">
            <el-image
              v-for="(image, index) in viewContent.images.split(',')"
              :key="index"
              :src="image"
              fit="cover"
              style="width: 100px; height: 100px; margin-right: 10px;"
            />
          </div>
        </div>
      </div>
    </el-dialog>
    
    <!-- 拒绝审核对话框-->
    <el-dialog v-model="rejectDialogVisible" title="拒绝审核">
      <el-form :model="rejectForm" :rules="rejectRules" ref="rejectFormRef">
        <el-form-item label="拒绝理由" prop="reason">
          <el-input v-model="rejectForm.reason" type="textarea" rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="rejectDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleRejectSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 审核操作对话框-->
    <el-dialog v-model="auditDialogVisible" title="审核操作">
      <el-form :model="auditForm" ref="auditFormRef">
        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.result">
            <el-radio label="approve">通过</el-radio>
            <el-radio label="reject">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="auditForm.result === 'reject'" label="拒绝理由" prop="reason">
          <el-input v-model="auditForm.reason" type="textarea" rows="4" placeholder="请输入拒绝理由" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="auditDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAuditSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'Content',
  setup() {
    const searchForm = reactive({
      contentId: '',
      title: '',
      type: '',
      status: ''
    })
    
    const contentList = ref([])
    const currentPage = ref(1)
    const total = ref(0)
    const loading = ref(false)
    
    const viewDialogVisible = ref(false)
    const viewContent = reactive({
      title: '',
      description: '',
      price: '',
      location: '',
      tags: '',
      images: ''
    })
    
    const rejectDialogVisible = ref(false)
    const rejectFormRef = ref(null)
    const rejectForm = reactive({
      reason: ''
    })
    const currentRejectId = ref(null)
    
    // 审核操作对话框
    const auditDialogVisible = ref(false)
    const currentAuditId = ref(null)
    const auditForm = reactive({
      result: 'approve', // approve 或 reject
      reason: ''
    })
    const auditFormRef = ref(null)
    
    const rejectRules = {
      reason: [
        { required: true, message: '请输入拒绝理由', trigger: 'blur' }
      ]
    }
    
    const getStatusText = (status) => {
      switch (status) {
        case 0:
          return '待审核'
        case 1:
          return '审核通过'
        case 2:
          return '审核拒绝'
        default:
          return '待审核'
      }
    }
    
    const getStatusType = (status) => {
      switch (status) {
        case 0:
          return 'warning'
        case 1:
          return 'success'
        case 2:
          return 'danger'
        default:
          return 'warning'
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
    
    const getContents = async () => {
      loading.value = true
      try {
        const token = localStorage.getItem('token')
        const response = await axios.get('http://localhost:8086/admin/api/content/admin/list', {
          params: {
            pageNum: currentPage.value,
            pageSize: 10,
            id: searchForm.contentId,
            title: searchForm.title,
            type: searchForm.type,
            status: searchForm.status
          },
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })
        
        if (response.data.code === 200) {
          contentList.value = response.data.data.list
          total.value = response.data.data.total
        } else {
          console.error('获取内容列表失败:', response.data.message)
        }
      } catch (error) {
        console.error('获取内容列表失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    const handleSearch = () => {
      currentPage.value = 1
      getContents()
    }
    
    const resetSearch = () => {
      searchForm.contentId = ''
      searchForm.title = ''
      searchForm.type = ''
      searchForm.status = ''
      currentPage.value = 1
      getContents()
    }
    
    const handleView = async (row) => {
      try {
        const token = localStorage.getItem('token')
        const response = await axios.get(`http://localhost:8086/admin/api/content/admin/detail/${row.id}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })
        if (response.data.code === 200) {
          const content = response.data.data
          viewContent.title = content.title
          viewContent.description = content.description
          viewContent.price = content.price
          viewContent.location = content.location
          
          // 获取标签
          const tagsResponse = await axios.get(`http://localhost:8086/admin/api/content/admin/tags/${row.id}`, {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          })
          if (tagsResponse.data.code === 200) {
            viewContent.tags = tagsResponse.data.data.join(', ')
          }
          
          // 获取图片
          const imagesResponse = await axios.get(`http://localhost:8086/admin/api/content/admin/images/${row.id}`, {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          })
          if (imagesResponse.data.code === 200) {
            viewContent.images = imagesResponse.data.data.map(img => img.imageUrl).join(',')
          }
          
          viewDialogVisible.value = true
        }
      } catch (error) {
        console.error('获取内容详情失败:', error)
      }
    }
    
    const handleApprove = async (row) => {
      try {
        const token = localStorage.getItem('token')
        const response = await axios.post('http://localhost:8086/admin/api/content/admin/approve', {
          id: row.id
        }, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        })
        
        if (response.data.code === 200) {
          // 刷新内容列表
          getContents()
        } else {
          console.error('通过审核失败:', response.data.message)
        }
      } catch (error) {
        console.error('通过审核失败:', error)
      }
    }
    
    const handleReject = (row) => {
      currentRejectId.value = row.id
      rejectForm.reason = ''
      rejectDialogVisible.value = true
    }
    
    // 打开审核操作对话框
    const handleAudit = (row) => {
      currentAuditId.value = row.id
      auditForm.result = 'approve'
      auditForm.reason = ''
      auditDialogVisible.value = true
    }
    
    // 提交审核操作
    const handleAuditSubmit = async () => {
      try {
        const token = localStorage.getItem('token')
        
        if (auditForm.result === 'approve') {
          // 审核通过
          const response = await axios.post('http://localhost:8086/admin/api/content/admin/approve', {
            id: currentAuditId.value
          }, {
            headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json'
            }
          })
          
          if (response.data.code === 200) {
            // 刷新内容列表
            getContents()
            auditDialogVisible.value = false
          } else {
            console.error('通过审核失败:', response.data.message)
          }
        } else if (auditForm.result === 'reject') {
          // 审核拒绝
          if (!auditForm.reason.trim()) {
            alert('请输入拒绝理由')
            return
          }
          
          const response = await axios.post('http://localhost:8086/admin/api/content/admin/reject', {
            id: currentAuditId.value,
            reason: auditForm.reason
          }, {
            headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json'
            }
          })
          
          if (response.data.code === 200) {
            // 刷新内容列表
            getContents()
            auditDialogVisible.value = false
          } else {
            console.error('拒绝审核失败:', response.data.message)
          }
        }
      } catch (error) {
        console.error('审核操作失败:', error)
      }
    }
    
    const handleRejectSubmit = async () => {
      if (!rejectFormRef.value) return
      
      try {
        await rejectFormRef.value.validate()
        
        const token = localStorage.getItem('token')
        // 处理拒绝提交逻辑
        const response = await axios.post('http://localhost:8086/admin/api/content/admin/reject', {
          id: currentRejectId.value,
          reason: rejectForm.reason
        }, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        })
        
        if (response.data.code === 200) {
          // 刷新内容列表
          getContents()
          rejectDialogVisible.value = false
        } else {
          console.error('拒绝审核失败:', response.data.message)
        }
      } catch (error) {
        console.error('验证失败:', error)
      }
    }
    
    const handleCurrentChange = (current) => {
      currentPage.value = current
      getContents()
    }
    
    // 组件初始化时获取内容列表
    onMounted(() => {
      getContents()
    })
    
    return {
      searchForm,
      contentList,
      currentPage,
      total,
      loading,
      viewDialogVisible,
      viewContent,
      rejectDialogVisible,
      rejectFormRef,
      rejectForm,
      rejectRules,
      currentRejectId,
      // 审核操作对话框
      auditDialogVisible,
      auditForm,
      auditFormRef,
      currentAuditId,
      getStatusText,
      getStatusType,
      formatTime,
      handleSearch,
      resetSearch,
      handleView,
      handleApprove,
      handleReject,
      handleRejectSubmit,
      handleAudit,
      handleAuditSubmit,
      handleCurrentChange
    }
  }
}
</script>

<style scoped>
.content-container {
  padding: 20px;
}

.content-container h2 {
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

.view-content {
  padding: 10px;
}

.view-item {
  margin-bottom: 15px;
}

.view-label {
  font-weight: bold;
  margin-right: 10px;
}

.view-images {
  display: flex;
  flex-wrap: wrap;
  margin-top: 10px;
}
</style>
