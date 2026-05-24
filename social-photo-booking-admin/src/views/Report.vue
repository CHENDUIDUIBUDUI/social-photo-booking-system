<template>
  <div class="report-container">
    <h2>举报管理</h2>
    
    <el-card class="statistics-card">
      <el-row :gutter="15" justify="center">
        <el-col :span="3">
          <div class="statistic-item">
            <div class="statistic-title">总举报数</div>
            <div class="statistic-value">{{ statistics.totalReports }}</div>
          </div>
        </el-col>
        <el-col :span="3">
          <div class="statistic-item pending">
            <div class="statistic-title">待处理</div>
            <div class="statistic-value">{{ statistics.pendingReports }}</div>
          </div>
        </el-col>
        <el-col :span="3">
          <div class="statistic-item handled">
            <div class="statistic-title">已处理</div>
            <div class="statistic-value">{{ statistics.handledReports }}</div>
          </div>
        </el-col>
        <el-col :span="3">
          <div class="statistic-item rejected">
            <div class="statistic-title">驳回</div>
            <div class="statistic-value">{{ statistics.rejectedReports }}</div>
          </div>
        </el-col>
        <el-col :span="3">
          <div class="statistic-item">
            <div class="statistic-title">内容举报</div>
            <div class="statistic-value">{{ statistics.contentReports }}</div>
          </div>
        </el-col>
        <el-col :span="3">
          <div class="statistic-item">
            <div class="statistic-title">评论举报</div>
            <div class="statistic-value">{{ statistics.commentReports }}</div>
          </div>
        </el-col>
        <el-col :span="3">
          <div class="statistic-item">
            <div class="statistic-title">用户举报</div>
            <div class="statistic-value">{{ statistics.userReports }}</div>
          </div>
        </el-col>
      </el-row>
    </el-card>
    
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="举报类型">
          <el-select v-model="searchForm.type" placeholder="请选择举报类型" clearable>
            <el-option label="全部" :value="null" />
            <el-option label="内容" :value="1" />
            <el-option label="评论" :value="2" />
            <el-option label="用户" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="全部" :value="null" />
            <el-option label="待处理" :value="0" />
            <el-option label="已处理" :value="1" />
            <el-option label="驳回" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card class="table-card">
      <el-table :data="reportList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="举报ID" width="80" />
        <el-table-column label="举报人" width="150">
          <template #default="scope">
            <div class="user-info">
              <el-avatar :size="32" :src="scope.row.reporterAvatar || 'https://via.placeholder.com/32x32?text=用户'" />
              <span style="margin-left: 10px;">{{ scope.row.reporterNickname }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="typeName" label="举报类型" width="100">
          <template #default="scope">
            <el-tag size="small" :type="getTypeType(scope.row.type)">
              {{ scope.row.typeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="被举报对象" width="200">
          <template #default="scope">
            <div v-if="scope.row.type === 1">
              <div>标题：{{ scope.row.targetTitle }}</div>
              <div class="target-content">内容：{{ scope.row.targetContent }}</div>
            </div>
            <div v-else-if="scope.row.type === 2 || scope.row.type === 3">
              用户：{{ scope.row.targetUserNickname }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="举报理由" width="200" show-overflow-tooltip />
        <el-table-column prop="statusName" label="状态" width="100">
          <template #default="scope">
            <el-tag size="small" :type="getStatusType(scope.row.status)">
              {{ scope.row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="举报时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row)">查看详情</el-button>
            <el-button v-if="scope.row.status === 0" type="success" size="small" @click="handleProcess(scope.row)">
              处理
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <el-dialog v-model="viewDialogVisible" title="举报详情" width="800px">
      <div class="view-content">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="举报ID">{{ viewReport.id }}</el-descriptions-item>
          <el-descriptions-item label="举报时间">{{ formatDateTime(viewReport.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="举报人">
            <div class="user-info">
              <el-avatar :size="32" :src="viewReport.reporterAvatar || 'https://via.placeholder.com/32x32?text=用户'" />
              <span style="margin-left: 10px;">{{ viewReport.reporterNickname }}</span>
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="举报类型">
            <el-tag size="small" :type="getTypeType(viewReport.type)">
              {{ viewReport.typeName }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="被举报对象" :span="2">
            <div v-if="viewReport.type === 1">
              <div>标题：{{ viewReport.targetTitle || '无' }}</div>
              <div>内容：{{ viewReport.targetContent || '无' }}</div>
              <div>作者：{{ viewReport.targetUserNickname || '无' }}</div>
            </div>
            <div v-else-if="viewReport.type === 2 || viewReport.type === 3">
              用户：{{ viewReport.targetUserNickname || '无' }}
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="举报理由" :span="2">{{ viewReport.reason }}</el-descriptions-item>
          <el-descriptions-item label="举报图片" :span="2" v-if="viewReport.images">
            <div class="image-preview">
              <el-image
                v-for="(image, index) in parseImages(viewReport.images)"
                :key="index"
                :src="image"
                :preview-src-list="parseImages(viewReport.images)"
                style="width: 100px; height: 100px; margin-right: 10px;"
                fit="cover"
                :error="handleImageError"
                :preview-teleported="true"
              />
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="举报图片" :span="2" v-else>
            <span>无</span>
          </el-descriptions-item>
          <el-descriptions-item label="处理状态">
            <el-tag size="small" :type="getStatusType(viewReport.status)">
              {{ viewReport.statusName }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="处理人" v-if="viewReport.handlerName">
            {{ viewReport.handlerName }}
          </el-descriptions-item>
          <el-descriptions-item label="处理时间" v-if="viewReport.handleTime">
            {{ formatDateTime(viewReport.handleTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="处理结果" :span="2" v-if="viewReport.handleResult">
            {{ isGarbled(viewReport.handleResult) ? '处理结果显示异常' : viewReport.handleResult }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
    
    <el-dialog v-model="processDialogVisible" title="处理举报" width="600px">
      <el-form :model="processForm" :rules="processRules" ref="processFormRef" label-width="100px">
        <el-form-item label="处理状态" prop="status">
          <el-radio-group v-model="processForm.status">
            <el-radio :label="1">已处理</el-radio>
            <el-radio :label="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="处理结果" prop="handleResult">
          <el-input v-model="processForm.handleResult" type="textarea" :rows="4" placeholder="请输入处理结果" />
        </el-form-item>
        <el-form-item label="上传证据" v-if="processForm.status === 1">
          <el-upload
            v-model:file-list="fileList"
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :on-preview="handlePicturePreview"
            :on-remove="handleRemove"
            :limit="5"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="processDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleProcessSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
    

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const searchForm = reactive({
  type: null,
  status: null
})

const reportList = ref([])
const statistics = ref({
  totalReports: 0,
  pendingReports: 0,
  handledReports: 0,
  rejectedReports: 0,
  contentReports: 0,
  commentReports: 0,
  userReports: 0
})
const loading = ref(false)

const viewDialogVisible = ref(false)
const viewReport = reactive({
  id: '',
  reporterId: '',
  reporterNickname: '',
  reporterAvatar: '',
  type: '',
  typeName: '',
  targetId: '',
  targetTitle: '',
  targetContent: '',
  targetUserNickname: '',
  reason: '',
  images: '',
  status: '',
  statusName: '',
  handleResult: '',
  handlerId: '',
  handlerName: '',
  handleTime: '',
  createTime: ''
})

const processDialogVisible = ref(false)
const processFormRef = ref(null)
const processForm = reactive({
  id: '',
  status: 1,
  handleResult: '',
  handlerId: 1
})

const processRules = {
  status: [
    { required: true, message: '请选择处理状态', trigger: 'change' }
  ],
  handleResult: [
    { required: true, message: '请输入处理结果', trigger: 'blur' }
  ]
}

const fileList = ref([])

const getTypeType = (type) => {
  switch (type) {
    case 1:
      return 'danger'
    case 2:
      return 'warning'
    case 3:
      return 'info'
    default:
      return 'danger'
  }
}

const getStatusType = (status) => {
  switch (status) {
    case 0:
      return 'warning'
    case 1:
      return 'success'
    case 2:
      return 'info'
    default:
      return 'warning'
  }
}

const parseImages = (images) => {
  if (!images) return []
  try {
    return JSON.parse(images)
  } catch (e) {
    return []
  }
}

const getReports = async () => {
  loading.value = true
  try {
    const params = {}
    if (searchForm.type !== null) {
      params.type = searchForm.type
    }
    if (searchForm.status !== null) {
      params.status = searchForm.status
    }
    
    const response = await axios.get('http://localhost:8086/admin/api/report/list', { params })
    
    if (response.data.code === 200) {
      reportList.value = response.data.data
    } else {
      ElMessage.error('获取举报列表失败：' + response.data.message)
    }
  } catch (error) {
    console.error('获取举报列表失败:', error)
    ElMessage.error('获取举报列表失败')
  } finally {
    loading.value = false
  }
}

const getStatistics = async () => {
  try {
    const response = await axios.get('http://localhost:8086/admin/api/report/statistics')
    
    if (response.data.code === 200) {
      statistics.value = response.data.data
    } else {
      ElMessage.error('获取统计数据失败：' + response.data.message)
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

const handleSearch = () => {
  getReports()
}

const resetSearch = () => {
  searchForm.type = null
  searchForm.status = null
  getReports()
}

const handleView = async (row) => {
  try {
    const response = await axios.get(`http://localhost:8086/admin/api/report/detail?id=${row.id}`)
    if (response.data.code === 200) {
      Object.assign(viewReport, response.data.data)
      viewDialogVisible.value = true
    } else {
      ElMessage.error('获取举报详情失败：' + response.data.message)
    }
  } catch (error) {
    console.error('获取举报详情失败:', error)
    ElMessage.error('获取举报详情失败')
  }
}

const handleProcess = (row) => {
  processForm.id = row.id
  processForm.status = 1
  processForm.handleResult = ''
  fileList.value = []
  processDialogVisible.value = true
}

const handleProcessSubmit = async () => {
  if (!processFormRef.value) return
  
  try {
    await processFormRef.value.validate()
    
    const response = await axios.post('http://localhost:8086/admin/api/report/handle', {
      id: processForm.id,
      status: processForm.status,
      handleResult: processForm.handleResult,
      handlerId: processForm.handlerId
    })
    
    if (response.data.code === 200) {
      ElMessage.success('处理成功')
      processDialogVisible.value = false
      getReports()
      getStatistics()
    } else {
      ElMessage.error('处理失败：' + response.data.message)
    }
  } catch (error) {
    console.error('处理失败:', error)
    ElMessage.error('处理失败')
  }
}



const handlePicturePreview = (file) => {
  console.log('预览图片:', file)
}

const handleRemove = (file) => {
  console.log('移除图片:', file)
}

const handleImageError = (e) => {
  // 图片加载失败时的处理
  e.target.src = 'https://via.placeholder.com/100x100?text=图片加载失败'
}

const isGarbled = (str) => {
  // 判断是否包含乱码字符（问号）
  return /\?{3,}/.test(str)
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  // 处理数组格式的时间（LocalDateTime序列化后的格式）
  if (Array.isArray(dateTime)) {
    const [year, month, day, hour, minute, second] = dateTime
    const paddedMonth = month.toString().padStart(2, '0')
    const paddedDay = day.toString().padStart(2, '0')
    const paddedHour = hour.toString().padStart(2, '0')
    const paddedMinute = minute.toString().padStart(2, '0')
    const paddedSecond = second ? second.toString().padStart(2, '0') : '00'
    return `${year}.${paddedMonth}.${paddedDay} ${paddedHour}:${paddedMinute}:${paddedSecond}`
  }
  // 处理字符串格式的时间
  if (typeof dateTime === 'string') {
    const date = new Date(dateTime)
    const paddedMonth = (date.getMonth() + 1).toString().padStart(2, '0')
    const paddedDay = date.getDate().toString().padStart(2, '0')
    const paddedHour = date.getHours().toString().padStart(2, '0')
    const paddedMinute = date.getMinutes().toString().padStart(2, '0')
    const paddedSecond = date.getSeconds().toString().padStart(2, '0')
    return `${date.getFullYear()}.${paddedMonth}.${paddedDay} ${paddedHour}:${paddedMinute}:${paddedSecond}`
  }
  return ''
}

onMounted(() => {
  getReports()
  getStatistics()
})
</script>

<style scoped>
.report-container {
  padding: 20px;
}

.report-container h2 {
  margin-bottom: 20px;
  color: #303133;
}

.statistics-card {
  margin-bottom: 20px;
}

.statistic-item {
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
  text-align: center;
}

.statistic-item.pending {
  background: #fef0f0;
  color: #f56c6c;
}

.statistic-item.handled {
  background: #f0f9ff;
  color: #409eff;
}

.statistic-item.rejected {
  background: #f4f4f5;
  color: #909399;
}

.statistic-title {
  font-size: 14px;
  color: #606266;
  margin-bottom: 10px;
}

.statistic-value {
  font-size: 24px;
  font-weight: bold;
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

.user-info {
  display: flex;
  align-items: center;
}

.target-content {
  color: #909399;
  font-size: 12px;
  margin-top: 5px;
}

.view-content {
  padding: 10px;
}

.image-preview {
  display: flex;
  flex-wrap: wrap;
}

.image-gallery {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}
</style>
