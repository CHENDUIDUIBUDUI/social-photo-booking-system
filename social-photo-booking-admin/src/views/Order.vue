<template>
  <div class="order-container">
    <h2>订单管理</h2>
    
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="摄影师ID">
          <el-input v-model="searchForm.photographerId" placeholder="请输入摄影师ID" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态">
            <el-option label="全部" value="" />
            <el-option label="待支付定金" value="0" />
            <el-option label="定金已支付" value="1" />
            <el-option label="拍摄完成" value="2" />
            <el-option label="已支付尾款" value="3" />
            <el-option label="已完成" value="4" />
            <el-option label="已取消" value="5" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card class="table-card">
      <el-table :data="orderList" style="width: 100%" v-loading="loading">
        <el-table-column prop="order_no" label="订单号" width="180" />
        <el-table-column prop="user_id" label="用户ID" width="100" />
        <el-table-column prop="photographer_id" label="摄影师ID" width="120" />
        <el-table-column prop="total_amount" label="总金额" width="100">
          <template #default="scope">
            ¥{{scope.row.total_amount}}
          </template>
        </el-table-column>
        <el-table-column label="拍摄时间" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.shoot_time) }}
          </template>
        </el-table-column>
        <el-table-column prop="location" label="拍摄地点" width="150" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag size="small" :type="getStatusType(scope.row.status)">
              {{getStatusText(scope.row.status)}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.create_time) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="success" size="small" @click="handleEdit(scope.row)">修改</el-button>
            <el-button v-if="adminRole <= 2" type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
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
    
    <!-- 查看订单对话框 -->
    <el-dialog v-model="viewDialogVisible" title="订单详情" width="80%">
      <div class="view-content">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card>
              <template #header>
                <div class="card-header">
                  <span>订单信息</span>
                </div>
              </template>
              <div class="view-item">
                <span class="view-label">订单号：</span>
                <span class="view-value">{{viewOrder.orderNo}}</span>
              </div>
              <div class="view-item">
                <span class="view-label">用户ID：</span>
                <span class="view-value">{{viewOrder.userId}}</span>
              </div>
              <div class="view-item">
                <span class="view-label">摄影师ID：</span>
                <span class="view-value">{{viewOrder.photographerId}}</span>
              </div>
              <div class="view-item">
                <span class="view-label">总金额：</span>
                <span class="view-value">¥{{viewOrder.totalAmount}}</span>
              </div>
              <div class="view-item">
                <span class="view-label">定金：</span>
                <span class="view-value">¥{{viewOrder.deposit_amount}}</span>
              </div>
              <div class="view-item">
                <span class="view-label">尾款：</span>
                <span class="view-value">¥{{viewOrder.paid_balance}}</span>
              </div>
              <div class="view-item">
                <span class="view-label">拍摄时间：</span>
                <span class="view-value">{{formatDateTime(viewOrder.shootTime)}}</span>
              </div>
              <div class="view-item">
                <span class="view-label">拍摄地点：</span>
                <span class="view-value">{{viewOrder.location}}</span>
              </div>
              <div class="view-item">
                <span class="view-label">状态：</span>
                <el-tag size="small" :type="getStatusType(viewOrder.status)">
                  {{getStatusText(viewOrder.status)}}
                </el-tag>
              </div>
              <div class="view-item">
                <span class="view-label">备注：</span>
                <span class="view-value">{{viewOrder.notes}}</span>
              </div>
            </el-card>
          </el-col>
          <el-col :span="18">
            <el-card>
              <template #header>
                <div class="card-header">
                  <span>支付记录</span>
                </div>
              </template>
              <el-table :data="paymentList" style="width: 100%">
                <el-table-column prop="id" label="ID" width="80" />
                <el-table-column prop="type" label="类型" width="100">
                  <template #default="scope">
                    <el-tag size="small" :type="scope.row.type === 0 ? 'primary' : 'success'">
                      {{scope.row.type === 0 ? '定金' : '尾款'}}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="amount" label="金额" width="100">
                  <template #default="scope">
                    ¥{{scope.row.amount}}
                  </template>
                </el-table-column>
                <el-table-column prop="paymentMethod" label="支付方式" width="120" />
                <el-table-column prop="transactionId" label="交易流水号" />
                <el-table-column prop="status" label="状态" width="100">
                  <template #default="scope">
                    <el-tag size="small" :type="scope.row.status === 1 ? 'success' : 'danger'">
                      {{scope.row.status === 1 ? '支付成功' : '支付失败'}}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="支付时间" width="180">
                  <template #default="scope">
                    {{ formatDateTime(scope.row.createTime) }}
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
            
            <el-card style="margin-top: 20px;">
              <template #header>
                <div class="card-header">
                  <span>评价信息</span>
                </div>
              </template>
              <div class="view-item">
                <span class="view-label">用户评价：</span>
                <span class="view-value">{{viewOrder.userComment}}</span>
              </div>
              <div class="view-item">
                <span class="view-label">用户评分：</span>
                <el-rate v-model="viewOrder.userRating" disabled />
              </div>
              <div class="view-item">
                <span class="view-label">摄影师评价：</span>
                <span class="view-value">{{viewOrder.photographerComment}}</span>
              </div>
              <div class="view-item">
                <span class="view-label">摄影师评分：</span>
                <el-rate v-model="viewOrder.photographerRating" disabled />
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
    
    <!-- 修改订单对话框 -->
    <el-dialog v-model="editDialogVisible" title="修改订单" width="60%">
      <div class="edit-content">
        <el-form :model="editOrder" label-width="120px">
          <el-form-item label="订单号">
            <el-input v-model="editOrder.orderNo" placeholder="请输入订单号" disabled />
          </el-form-item>
          <el-form-item label="用户ID">
            <el-input v-model="editOrder.userId" placeholder="请输入用户ID" disabled />
          </el-form-item>
          <el-form-item label="摄影师ID">
            <el-input v-model="editOrder.photographerId" placeholder="请输入摄影师ID" disabled />
          </el-form-item>
          <el-form-item label="总金额">
            <el-input v-model="editOrder.totalAmount" type="number" placeholder="请输入总金额" disabled />
          </el-form-item>
          <el-form-item label="定金">
            <el-input v-model="editOrder.deposit_amount" type="number" placeholder="请输入定金" disabled />
          </el-form-item>
          <el-form-item label="尾款">
            <el-input v-model="editOrder.paid_balance" type="number" placeholder="请输入尾款" disabled />
          </el-form-item>
          <el-form-item label="拍摄时间">
            <el-input v-model="editOrder.shootTime" placeholder="请输入拍摄时间" />
          </el-form-item>
          <el-form-item label="拍摄地点">
            <el-input v-model="editOrder.location" placeholder="请输入拍摄地点" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="editOrder.status" placeholder="请选择状态">
              <el-option label="待支付定金" value="0" />
              <el-option label="定金已支付" value="1" />
              <el-option label="拍摄完成" value="2" />
              <el-option label="已支付尾款" value="3" />
              <el-option label="已完成" value="4" />
              <el-option label="已取消" value="5" />
            </el-select>
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="editOrder.notes" type="textarea" placeholder="请输入备注" />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleEditSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'Order',
  setup() {
    const searchForm = reactive({
      orderNo: '',
      userId: '',
      photographerId: '',
      status: ''
    })
    
    const orderList = ref([])
    const currentPage = ref(1)
    const total = ref(0)
    const loading = ref(false)
    
    const viewDialogVisible = ref(false)
    const viewOrder = reactive({
      orderNo: '',
      userId: '',
      photographerId: '',
      totalAmount: '',
      deposit_amount: '',
      paid_balance: '',
      shootTime: '',
      location: '',
      status: '',
      notes: '',
      userRating: 5,
      userComment: '',
      photographerRating: 5,
      photographerComment: ''
    })
    
    const editDialogVisible = ref(false)
    const editOrder = reactive({
      id: '',
      orderNo: '',
      userId: '',
      photographerId: '',
      totalAmount: '',
      deposit_amount: '',
      paid_balance: '',
      shootTime: '',
      location: '',
      status: '',
      notes: ''
    })
    
    const paymentList = ref([])
    const adminRole = ref(3) // 默认普通管理员
    
    // 从localStorage 中获取管理员角色
    const getAdminRole = () => {
      const adminRoleValue = localStorage.getItem('adminRole')
      if (adminRoleValue) {
        try {
          adminRole.value = parseInt(adminRoleValue) || 3
        } catch (error) {
          console.error('解析管理员角色失败', error)
        }
      }
    }
    
    // 组件初始化时获取管理员角色
    getAdminRole()
    
    const getStatusText = (status) => {
      switch (status) {
        case 0:
          return '待支付定金'
        case 1:
          return '定金已支付'
        case 2:
          return '拍摄完成'
        case 3:
          return '已支付尾款'
        case 4:
          return '已完成'
        case 5:
          return '已取消'
        default:
          return '待支付定金'
      }
    }
    
    const getStatusType = (status) => {
      switch (status) {
        case 0:
          return 'warning'
        case 1:
          return 'info'
        case 2:
          return 'primary'
        case 3:
          return 'success'
        case 4:
          return 'success'
        case 5:
          return 'danger'
        default:
          return 'warning'
      }
    }
    
    const formatDateTime = (dateTime) => {
      if (!dateTime) return ''
      
      // 尝试解析日期时间
      let date
      if (Array.isArray(dateTime)) {
        // 处理数组格式的时间：[2026,5,27,16,0]
        if (dateTime.length >= 5) {
          date = new Date(
            dateTime[0],
            dateTime[1] - 1,  // 月份从0开始
            dateTime[2],
            dateTime[3] || 0,
            dateTime[4] || 0,
            dateTime[5] || 0
          )
        } else {
          return dateTime.toString()
        }
      } else if (typeof dateTime === 'string') {
        // 处理不同格式的日期字符串
        if (dateTime.includes(' ')) {
          // 格式：2026-04-15 10:30:00
          const parts = dateTime.split(' ')
          const dateParts = parts[0].split('-')
          const timeParts = parts[1].split(':')
          date = new Date(
            parseInt(dateParts[0]),
            parseInt(dateParts[1]) - 1,
            parseInt(dateParts[2]),
            parseInt(timeParts[0]),
            parseInt(timeParts[1]),
            parseInt(timeParts[2])
          )
        } else if (dateTime.includes('T')) {
          // 格式：2026-04-15T10:30:00Z
          date = new Date(dateTime)
        } else {
          // 其他格式
          date = new Date(dateTime)
        }
      } else {
        date = new Date(dateTime)
      }
      
      // 检查日期是否有效
      if (isNaN(date.getTime())) {
        return dateTime
      }
      
      const year = date.getFullYear()
      const month = (date.getMonth() + 1).toString().padStart(2, '0')
      const day = date.getDate().toString().padStart(2, '0')
      const hours = date.getHours().toString().padStart(2, '0')
      const minutes = date.getMinutes().toString().padStart(2, '0')
      const seconds = date.getSeconds().toString().padStart(2, '0')
      
      return `${year}.${month}.${day} ${hours}:${minutes}:${seconds}`
    }
    
    const getOrders = async () => {
      loading.value = true
      try {
        const response = await axios.get('http://localhost:8086/admin/api/order/admin/list', {
          params: {
            pageNum: currentPage.value,
            pageSize: 10,
            orderNo: searchForm.orderNo,
            customerId: searchForm.userId,
            photographerId: searchForm.photographerId,
            status: searchForm.status
          }
        })
        
        if (response.data.code === 200) {
          orderList.value = response.data.data.list
          total.value = response.data.data.total
        } else {
          console.error('获取订单列表失败:', response.data.message)
        }
      } catch (error) {
        console.error('获取订单列表失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    const handleSearch = () => {
      currentPage.value = 1
      getOrders()
    }
    
    const resetSearch = () => {
      searchForm.orderNo = ''
      searchForm.userId = ''
      searchForm.photographerId = ''
      searchForm.status = ''
      currentPage.value = 1
      getOrders()
    }
    
    const handleView = async (row) => {
      loading.value = true
      try {
        const response = await axios.get(`http://localhost:8086/admin/api/order/admin/detail/${row.id}`)
        if (response.data.code === 200) {
          const order = response.data.data.order
          viewOrder.orderNo = order.order_no || ''
              viewOrder.userId = order.user_id || ''
              viewOrder.photographerId = order.photographer_id || ''
              viewOrder.totalAmount = order.total_amount || 0
              viewOrder.deposit_amount = order.deposit_amount || 0
              viewOrder.paid_balance = order.paid_balance || 0
              viewOrder.shootTime = order.shoot_time || ''
              viewOrder.location = order.location || ''
              viewOrder.status = getStatusText(order.status || 0)
              viewOrder.notes = order.notes || ''
          viewOrder.userRating = order.user_rating || 0
          viewOrder.userComment = order.user_comment || ''
          viewOrder.photographerRating = order.photographer_rating || 0
          viewOrder.photographerComment = order.photographer_comment || ''
          
          // 暂时注释掉支付记录，因为后端还没有实现这个接口
          // const paymentResponse = await axios.get(`http://localhost:8086/admin/api/order/payment/list/${order.id}`)
          // if (paymentResponse.data.code === 200) {
          //   paymentList.value = paymentResponse.data.data
          // } else {
          //   paymentList.value = []
          // }
          
          // 模拟支付记录数据
          paymentList.value = []
          
          // 生成定金支付记录
          if (viewOrder.deposit > 0) {
            paymentList.value.push({
              id: 1,
              type: 0,
              amount: viewOrder.deposit,
              paymentMethod: '微信支付',
              transactionId: 'wx1234567890',
              status: 1,
              createTime: order.create_time || new Date().toISOString()
            })
          }
          
          // 生成尾款支付记录
          if (viewOrder.balance > 0) {
            paymentList.value.push({
              id: 2,
              type: 1,
              amount: viewOrder.balance,
              paymentMethod: '微信支付',
              transactionId: 'wx0987654321',
              status: 1,
              createTime: order.create_time || new Date().toISOString()
            })
          }
          
          // 当定金和尾款都为 0 时，添加未支付记录
          if (viewOrder.deposit === 0 && viewOrder.balance === 0) {
            paymentList.value.push({
              id: 1,
              type: 0,
              amount: 0,
              paymentMethod: '',
              transactionId: '',
              status: 0,
              createTime: ''
            })
          }
          
          viewDialogVisible.value = true
        } else {
          console.error('获取订单详情失败:', response.data.message)
          alert('获取订单详情失败: ' + response.data.message)
        }
      } catch (error) {
        console.error('获取订单详情失败:', error)
        alert('获取订单详情失败: ' + (error.message || '网络错误'))
      } finally {
        loading.value = false
      }
    }
    
    const handleCurrentChange = (current) => {
      currentPage.value = current
      getOrders()
    }
    
    const handleEdit = async (row) => {
      loading.value = true
      try {
        const response = await axios.get(`http://localhost:8086/admin/api/order/admin/detail/${row.id}`)
        if (response.data.code === 200) {
          const order = response.data.data.order
          editOrder.id = order.id
              editOrder.orderNo = order.order_no || ''
              editOrder.userId = order.user_id || ''
              editOrder.photographerId = order.photographer_id || ''
              editOrder.totalAmount = order.total_amount || 0
              editOrder.deposit_amount = order.deposit_amount || 0
              editOrder.paid_balance = order.paid_balance || 0
              editOrder.shootTime = order.shoot_time || ''
              editOrder.location = order.location || ''
              editOrder.status = order.status || 0
              editOrder.notes = order.notes || ''
          
          editDialogVisible.value = true
        } else {
          console.error('获取订单详情失败:', response.data.message)
          alert('获取订单详情失败: ' + response.data.message)
        }
      } catch (error) {
        console.error('获取订单详情失败:', error)
        alert('获取订单详情失败: ' + (error.message || '网络错误'))
      } finally {
        loading.value = false
      }
    }
    
    const handleDelete = async (row) => {
      // 实现删除订单功能
      if (confirm('确定要删除这个订单吗？')) {
        loading.value = true
        try {
          const response = await axios.post('http://localhost:8086/admin/api/order/admin/delete', {
            id: row.id
          })
          
          if (response.data.code === 200) {
            getOrders() // 刷新订单列表
            alert('删除订单成功')
          } else {
            console.error('删除订单失败:', response.data.message)
            alert('删除订单失败: ' + response.data.message)
          }
        } catch (error) {
          console.error('删除订单失败:', error)
          alert('删除订单失败: ' + (error.message || '网络错误'))
        } finally {
          loading.value = false
        }
      }
    }
    
    const handleEditSubmit = async () => {
      loading.value = true
      try {
        const response = await axios.post('http://localhost:8086/admin/api/order/admin/update', {
              id: editOrder.id,
              order_no: editOrder.orderNo,
              user_id: editOrder.userId,
              photographer_id: editOrder.photographerId,
              total_amount: editOrder.totalAmount,
              deposit_amount: editOrder.deposit_amount,
              paid_balance: editOrder.paid_balance,
              shoot_time: editOrder.shootTime,
              location: editOrder.location,
              status: editOrder.status,
              notes: editOrder.notes
            })
        
        if (response.data.code === 200) {
          editDialogVisible.value = false
          getOrders() // 刷新订单列表
          alert('修改订单成功')
        } else {
          console.error('修改订单失败:', response.data.message)
          alert('修改订单失败: ' + response.data.message)
        }
      } catch (error) {
        console.error('修改订单失败:', error)
        alert('修改订单失败: ' + (error.message || '网络错误'))
      } finally {
        loading.value = false
      }
    }
    
    // 组件初始化时获取订单列表
    onMounted(() => {
      getOrders()
    })
    
    return {
      searchForm,
      orderList,
      currentPage,
      total,
      loading,
      viewDialogVisible,
      viewOrder,
      editDialogVisible,
      editOrder,
      paymentList,
      adminRole,
      getStatusText,
      getStatusType,
      formatDateTime,
      handleSearch,
      resetSearch,
      handleView,
      handleEdit,
      handleEditSubmit,
      handleDelete,
      handleCurrentChange
    }
  }
}
</script>

<style scoped>
.order-container {
  padding: 20px;
}

.order-container h2 {
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

.card-header {
  font-size: 16px;
  font-weight: bold;
}

.view-item {
  margin-bottom: 15px;
}

.view-label {
  font-weight: bold;
  margin-right: 10px;
}
</style>