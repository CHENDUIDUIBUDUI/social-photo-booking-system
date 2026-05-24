<template>
  <div class="system-container">
    <h2>系统配置</h2>
    

    
    <el-card class="config-card" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span>信用规则配置</span>
        </div>
      </template>
      <el-form :model="creditRules" label-width="120px">
        <el-form-item label="初始信用分">
          <el-input v-model.number="creditRules.initialScore" type="number" />
        </el-form-item>
        <el-form-item label="按时赴约加分">
          <el-input v-model.number="creditRules.onTimeScore" type="number" />
        </el-form-item>
        <el-form-item label="完成订单加分">
          <el-input v-model.number="creditRules.completeScore" type="number" />
        </el-form-item>
        <el-form-item label="未按时赴约扣分">
          <el-input v-model.number="creditRules.lateScore" type="number" />
        </el-form-item>
        <el-form-item label="取消订单扣分">
          <el-input v-model.number="creditRules.cancelScore" type="number" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSaveCreditRules">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    

  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'System',
  setup() {
    const creditRules = reactive({
      initialScore: 100,
      onTimeScore: 5,
      completeScore: 10,
      lateScore: -10,
      cancelScore: -15
    })
    
    const getCreditRules = async () => {
      try {
        const response = await axios.get('http://localhost:8086/admin/credit/rules')
        if (response.data.code === 200) {
          const rules = response.data.data
          creditRules.initialScore = rules.initialScore
          creditRules.onTimeScore = rules.onTimeScore
          creditRules.completeScore = rules.completeScore
          creditRules.lateScore = rules.lateScore
          creditRules.cancelScore = rules.cancelScore
        } else {
          console.error('获取信用规则失败:', response.data.message)
        }
      } catch (error) {
        console.error('获取信用规则失败:', error)
      }
    }
    
    const handleSaveCreditRules = () => {
      // 处理保存信用规则逻辑
      console.log('保存信用规则:', creditRules)
    }
    
    // 组件初始化时获取数据
    onMounted(() => {
      getCreditRules()
    })
    
    return {
      creditRules,
      handleSaveCreditRules
    }
  }
}
</script>

<style scoped>
.system-container {
  padding: 20px;
}

.system-container h2 {
  margin-bottom: 20px;
  color: #303133;
}

.config-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
}
</style>
