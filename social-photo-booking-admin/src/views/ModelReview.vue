<template>
  <div class="model-review-container">
    <h2>模特入驻审核</h2>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="申请人ID">
          <el-input v-model="searchForm.userId" placeholder="请输入申请人ID" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态">
            <el-option label="全部" value="" />
            <el-option label="待审核" value="0" />
            <el-option label="已通过" value="1" />
            <el-option label="已拒绝" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table :data="applicationList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="申请ID" width="100" />
        <el-table-column prop="userId" label="申请人ID" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="height" label="身高(cm)" width="100" />
        <el-table-column prop="weight" label="体重(kg)" width="100" />
        <el-table-column prop="styles" label="擅长风格" min-width="150" />
        <el-table-column prop="isPaid" label="是否付费" width="100">
          <template #default="scope">
            <el-tag size="small" :type="scope.row.isPaid === 1 ? 'success' : 'info'">
              {{ scope.row.isPaid === 1 ? '付费' : '免费' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="availability" label="可预约时间" min-width="150">
          <template #default="scope">
            {{ scope.row.availability || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag size="small" :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="申请时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button
              v-if="scope.row.status === 0"
              type="success"
              size="small"
              @click="handleApprove(scope.row)"
            >通过</el-button>
            <el-button
              v-if="scope.row.status === 0"
              type="danger"
              size="small"
              @click="handleReject(scope.row)"
            >拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination" style="margin-top: 20px;">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="total"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="viewDialogVisible" title="申请详情" width="700px">
      <div class="detail-content" v-if="currentApplication">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="申请ID">{{ currentApplication.id }}</el-descriptions-item>
          <el-descriptions-item label="申请人ID">{{ currentApplication.userId }}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{ currentApplication.name }}</el-descriptions-item>
          <el-descriptions-item label="身高">{{ currentApplication.height }} cm</el-descriptions-item>
          <el-descriptions-item label="体重">{{ currentApplication.weight }} kg</el-descriptions-item>
          <el-descriptions-item label="是否付费">
            <el-tag size="small" :type="currentApplication.isPaid === 1 ? 'success' : 'info'">
              {{ currentApplication.isPaid === 1 ? '付费' : '免费' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="擅长风格" :span="2">{{ currentApplication.styles }}</el-descriptions-item>
          <el-descriptions-item label="可预约时间" :span="2">{{ currentApplication.availability || '-' }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag size="small" :type="getStatusType(currentApplication.status)">
              {{ getStatusText(currentApplication.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="申请时间">
            {{ formatTime(currentApplication.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="作品集" :span="2">
            <a v-if="currentApplication.portfolio" :href="currentApplication.portfolio" target="_blank" class="portfolio-link">
              {{ currentApplication.portfolio }}
            </a>
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="自我介绍" :span="2">
            {{ currentApplication.introduction || '-' }}
          </el-descriptions-item>
          <el-descriptions-item v-if="currentApplication.rejectReason" label="拒绝原因" :span="2">
            {{ currentApplication.rejectReason }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewDialogVisible = false">关闭</el-button>
          <el-button
            v-if="currentApplication && currentApplication.status === 0"
            type="success"
            @click="handleApprove(currentApplication)"
          >通过</el-button>
          <el-button
            v-if="currentApplication && currentApplication.status === 0"
            type="danger"
            @click="handleReject(currentApplication)"
          >拒绝</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="rejectDialogVisible" title="拒绝申请" width="400px">
      <el-form :model="rejectForm" :rules="rejectRules" ref="rejectFormRef">
        <el-form-item label="拒绝原因" prop="reason">
          <el-input
            v-model="rejectForm.reason"
            type="textarea"
            :rows="4"
            placeholder="请输入拒绝原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="rejectDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="submitReject">确认拒绝</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'ModelReview',
  setup() {
    const searchForm = reactive({
      userId: '',
      name: '',
      status: ''
    })

    const applicationList = ref([])
    const currentPage = ref(1)
    const pageSize = ref(10)
    const total = ref(0)
    const loading = ref(false)

    const viewDialogVisible = ref(false)
    const rejectDialogVisible = ref(false)
    const currentApplication = ref(null)
    const rejectFormRef = ref(null)
    const rejectForm = reactive({
      reason: '',
      id: null
    })

    const rejectRules = {
      reason: [
        { required: true, message: '请输入拒绝原因', trigger: 'blur' }
      ]
    }

    const getStatusText = (status) => {
      switch (status) {
        case 0:
          return '待审核'
        case 1:
          return '已通过'
        case 2:
          return '已拒绝'
        default:
          return '未知'
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
          return 'info'
      }
    }

    const formatTime = (time) => {
      if (!time) return '-'
      if (Array.isArray(time) && time.length >= 5) {
        const [year, month, day, hour, minute, second = 0] = time
        return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')} ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:${second.toString().padStart(2, '0')}`
      }
      const date = new Date(time)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      const second = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    }

    const fetchApplications = async () => {
      loading.value = true
      try {
        const params = {
          page: currentPage.value,
          pageSize: pageSize.value
        }

        const response = await axios.get('http://localhost:8086/admin/api/model/application/pending', {
          params
        })

        if (response.data.code === 200) {
          applicationList.value = response.data.data.list
          total.value = response.data.data.total
        } else {
          ElMessage.error(response.data.message || '获取申请列表失败')
        }
      } catch (error) {
        console.error('获取申请列表失败:', error)
        ElMessage.error('获取申请列表失败')
      } finally {
        loading.value = false
      }
    }

    const handleSearch = () => {
      currentPage.value = 1
      fetchApplications()
    }

    const resetSearch = () => {
      searchForm.userId = ''
      searchForm.name = ''
      searchForm.status = ''
      currentPage.value = 1
      fetchApplications()
    }

    const handleView = (row) => {
      currentApplication.value = row
      viewDialogVisible.value = true
    }

    const handleApprove = async (row) => {
      try {
        await ElMessageBox.confirm('确认通过该模特入驻申请?', '审核确认', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await axios.post('http://localhost:8086/admin/api/model/application/audit', {
          id: row.id,
          status: 1,
          auditorId: 1 // 假设当前审核人ID为1，实际应从登录信息获取
        })

        if (response.data.code === 200) {
          ElMessage.success('审核通过')
          viewDialogVisible.value = false
          fetchApplications()
        } else {
          ElMessage.error(response.data.message || '审核失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('审核失败:', error)
          ElMessage.error('审核失败')
        }
      }
    }

    const handleReject = (row) => {
      rejectForm.id = row.id
      rejectForm.reason = ''
      rejectDialogVisible.value = true
    }

    const submitReject = async () => {
      if (!rejectFormRef.value) return

      try {
        await rejectFormRef.value.validate()

        const response = await axios.post('http://localhost:8086/admin/api/model/application/audit', {
          id: rejectForm.id,
          status: 2,
          rejectReason: rejectForm.reason,
          auditorId: 1 // 假设当前审核人ID为1，实际应从登录信息获取
        })

        if (response.data.code === 200) {
          ElMessage.success('已拒绝申请')
          rejectDialogVisible.value = false
          viewDialogVisible.value = false
          fetchApplications()
        } else {
          ElMessage.error(response.data.message || '拒绝失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('拒绝失败:', error)
          ElMessage.error('拒绝失败')
        }
      }
    }

    const handleCurrentChange = (current) => {
      currentPage.value = current
      fetchApplications()
    }

    onMounted(() => {
      fetchApplications()
    })

    return {
      searchForm,
      applicationList,
      currentPage,
      pageSize,
      total,
      loading,
      viewDialogVisible,
      rejectDialogVisible,
      currentApplication,
      rejectFormRef,
      rejectForm,
      rejectRules,
      getStatusText,
      getStatusType,
      formatTime,
      handleSearch,
      resetSearch,
      handleView,
      handleApprove,
      handleReject,
      submitReject,
      handleCurrentChange
    }
  }
}
</script>

<style scoped>
.model-review-container {
  padding: 20px;
}

.model-review-container h2 {
  margin-bottom: 20px;
  color: #303133;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-top: 20px;
}

.portfolio-link {
  color: #409EFF;
  text-decoration: none;
}

.portfolio-link:hover {
  text-decoration: underline;
}

.detail-content {
  padding: 10px 0;
}
</style>
