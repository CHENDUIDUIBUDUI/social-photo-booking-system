<template>
  <div class="dashboard-container">
    <h2>数据看板</h2>
    
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-item">
            <div class="stats-value">{{userCount}}</div>
            <div class="stats-label">用户总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-item">
            <div class="stats-value">{{photographerCount}}</div>
            <div class="stats-label">摄影师数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-item">
            <div class="stats-value">{{orderCount}}</div>
            <div class="stats-label">订单总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-item">
            <div class="stats-value">¥{{totalAmount}}</div>
            <div class="stats-label">交易总额</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card class="chart-card">
          <div class="chart-title">订单趋势</div>
          <div ref="orderChart" class="chart"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div class="chart-title">用户分布</div>
          <div ref="userChart" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card class="table-card">
          <div class="table-title">待审核内容</div>
          <el-table :data="pendingContent" style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="type" label="类型" width="100">
              <template #default="scope">
                <el-tag size="small" :type="scope.row.type === 0 ? 'primary' : 'success'">
                  {{scope.row.type === 0 ? '作品' : '需求'}}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="userId" label="用户ID" width="100" />
            <el-table-column label="创建时间" width="180">
              <template #default="scope">
                {{ formatTime(scope.row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag size="small" type="warning">待审核</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button type="primary" size="small" @click="handleReview(scope.row)">审核</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
    
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
import { ref, onMounted, reactive } from 'vue'
import * as echarts from 'echarts'
import axios from 'axios'

export default {
  name: 'Dashboard',
  setup() {
    const userCount = ref(0)
    const photographerCount = ref(0)
    const orderCount = ref(0)
    const totalAmount = ref(0)
    const loading = ref(false)
    
    const orderChart = ref(null)
    const userChart = ref(null)
    
    const pendingContent = ref([])
    
    const getDashboardStats = async () => {
      loading.value = true
      try {
        const response = await axios.get('http://localhost:8086/admin/dashboard/stats')
        if (response.data.code === 200) {
          const stats = response.data.data
          userCount.value = stats.userCount
          photographerCount.value = stats.photographerCount
          orderCount.value = stats.orderCount
          totalAmount.value = stats.totalAmount
        } else {
          console.error('获取统计数据失败:', response.data.message)
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    const getPendingContent = async () => {
      try {
        const token = localStorage.getItem('token')
        const response = await axios.get('http://localhost:8086/admin/api/content/admin/list', {
          params: {
            pageNum: 1,
            pageSize: 10,
            status: 0
          },
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })
        if (response.data.code === 200) {
          pendingContent.value = response.data.data.list
        } else {
          console.error('获取待审核内容失败', response.data.message)
        }
      } catch (error) {
        console.error('获取待审核内容失败', error)
      }
    }
    
    const getChartsData = async () => {
      try {
        const response = await axios.get('http://localhost:8086/admin/dashboard/charts')
        if (response.data.code === 200) {
          const chartsData = response.data.data
          initCharts(chartsData)
        } else {
          console.error('获取图表数据失败:', response.data.message)
          // 显示错误信息，不使用默认数据
          initCharts(null)
        }
      } catch (error) {
        console.error('获取图表数据失败:', error)
        // 显示错误信息，不使用默认数据
        initCharts(null)
      }
    }
    
    const initCharts = (chartsData) => {
      // 确保 DOM 元素存在
      if (!orderChart.value || !userChart.value) {
        console.error('图表 DOM 元素未准备好')
        return
      }
      
      // 订单趋势图表
      const orderChartInstance = echarts.init(orderChart.value)
      
      if (chartsData) {
        const orderData = chartsData.orderTrend
        
        orderChartInstance.setOption({
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            data: orderData.months
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: orderData.counts,
              type: 'line',
              smooth: true
            }
          ]
        })
        
        // 用户分布图表
        const userChartInstance = echarts.init(userChart.value)
        const userData = chartsData.userDistribution
        
        userChartInstance.setOption({
          tooltip: {
            trigger: 'item'
          },
          legend: {
            top: '5%',
            left: 'center'
          },
          series: [
            {
              name: '用户类型',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '18',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: userData
            }
          ]
        })
      } else {
        // 显示错误状态
        orderChartInstance.setOption({
          title: {
            text: '获取数据失败',
            left: 'center',
            top: 'center'
          }
        })
        
        const userChartInstance = echarts.init(userChart.value)
        userChartInstance.setOption({
          title: {
            text: '获取数据失败',
            left: 'center',
            top: 'center'
          }
        })
      }
    }
    
    // 审核操作对话框
    const auditDialogVisible = ref(false)
    const currentAuditId = ref(null)
    const auditForm = reactive({
      result: 'approve', // approve 或 reject
      reason: ''
    })
    const auditFormRef = ref(null)
    
    const handleReview = (row) => {
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
            // 刷新待审核内容列表
            getPendingContent()
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
            // 刷新待审核内容列表
            getPendingContent()
            auditDialogVisible.value = false
          } else {
            console.error('拒绝审核失败:', response.data.message)
          }
        }
      } catch (error) {
        console.error('审核操作失败:', error)
      }
    }
    
    const formatTime = (time) => {
      if (!time) return '-'
      // 处理数组格式的时间 [year, month, day, hour, minute, second]
      if (Array.isArray(time) && time.length >= 6) {
        const [year, month, day, hour, minute, second] = time
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
    
    onMounted(() => {
      getDashboardStats()
      getPendingContent()
      getChartsData()
    })
    
    return {
      userCount,
      photographerCount,
      orderCount,
      totalAmount,
      loading,
      orderChart,
      userChart,
      pendingContent,
      // 审核操作对话框
      auditDialogVisible,
      auditForm,
      auditFormRef,
      currentAuditId,
      handleReview,
      handleAuditSubmit,
      formatTime
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.dashboard-container h2 {
  margin-bottom: 20px;
  color: #303133;
}

.stats-card {
  height: 120px;
}

.stats-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.stats-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 8px;
}

.stats-label {
  font-size: 14px;
  color: #606266;
}

.chart-card {
  height: 300px;
}

.chart-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 16px;
  color: #303133;
}

.chart {
  width: 100%;
  height: calc(100% - 40px);
}

.table-card {
  margin-top: 20px;
}

.table-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 16px;
  color: #303133;
}
</style>
