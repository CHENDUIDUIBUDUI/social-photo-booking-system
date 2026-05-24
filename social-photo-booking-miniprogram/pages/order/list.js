// list.js
Page({
  data: {
    // 订单状态标签
    statusTabs: [
      { label: '全部', value: 'all' },
      { label: '待付款', value: '0' },
      { label: '已支付定金', value: '1' },
      { label: '进行中', value: '2' },
      { label: '拍摄完成', value: '3' },
      { label: '已完成', value: '4' },
      { label: '已取消', value: '5' }
    ],
    activeStatus: 'all',
    orderList: [],
    type: 'user', // user: 我的订单, photographer: 我的预约
    loading: false
  },
  
  onLoad(options) {
    if (options.type) {
      this.setData({ type: options.type });
    }
    this.loadOrderList();
  },

  onShow() {
    this.loadOrderList();
  },
  
  // 加载订单列表
  loadOrderList() {
    const token = wx.getStorageSync('token');
    if (!token) {
      return;
    }
    
    this.setData({ loading: true });
    
    // 实际API调用
    const userId = wx.getStorageSync('userId');
    let url = `${getApp().globalData.baseUrl}/api/order/user`;
    let data = {
      userId: userId
    };
    
    if (this.data.activeStatus !== 'all') {
      data.status = parseInt(this.data.activeStatus);
    }
    
    if (this.data.type === 'photographer') {
      // 摄影师查询订单需要先获取摄影师ID
      const token = wx.getStorageSync('token');
      wx.request({
        url: `${getApp().globalData.baseUrl}/api/photographer/user/${userId}`,
        method: 'GET',
        header: { 'Authorization': 'Bearer ' + token },
        success: (res) => {
          if (res.data && res.data.code === 200 && res.data.data) {
            const photographerId = res.data.data.id;
            this.loadPhotographerOrders(photographerId);
          } else {
            this.setData({ loading: false });
          }
        },
        fail: () => {
          this.setData({ loading: false });
        }
      });
      return;
    }
    
    wx.request({
      url: url,
      method: 'GET',
      header: { 'Authorization': 'Bearer ' + token },
      data: data,
      timeout: 30000,
      success: (res) => {
        console.log('加载订单列表成功，响应:', res);
        if (res.data && res.data.code === 200 && res.data.data) {
          // 后端直接返回数组，需要兼容处理
          const orderList = Array.isArray(res.data.data) ? res.data.data : (res.data.data.list || []);
          // 转换订单数据格式
          const formattedOrders = orderList.map(order => ({
            id: order.id,
            orderId: order.orderNo,
            status: order.status,
            statusText: this.getStatusText(order.status),
            statusBgColor: 'linear-gradient(135deg, #A8E6CF 0%, #DDA0DD 100%)',
            title: '摄影套餐', // 从订单数据中获取标题
            photographerName: '摄影师', // 从订单数据中获取摄影师名称
            price: order.totalAmount,
            imageUrl: 'https://api.dicebear.com/7.x/personas/svg?seed=photography&size=200',
            createTime: this.formatDateTime(order.createTime)
          }));
          this.setData({ 
            orderList: formattedOrders,
            loading: false 
          });
        } else {
          this.setData({ loading: false });
        }
      },
      fail: (err) => {
        console.error('加载订单列表失败:', err);
        this.setData({ loading: false });
      }
    });
  },
  
  // 加载摄影师订单
  loadPhotographerOrders(photographerId) {
    const token = wx.getStorageSync('token');
    let data = {
      photographerId: photographerId
    };
    
    if (this.data.activeStatus !== 'all') {
      data.status = parseInt(this.data.activeStatus);
    }
    
    wx.request({
      url: `${getApp().globalData.baseUrl}/api/order/photographer`,
      method: 'GET',
      header: { 'Authorization': 'Bearer ' + token },
      data: data,
      timeout: 30000,
      success: (res) => {
        console.log('加载摄影师订单列表成功，响应:', res);
        if (res.data && res.data.code === 200 && res.data.data) {
          const orderList = Array.isArray(res.data.data) ? res.data.data : (res.data.data.list || []);
          const formattedOrders = orderList.map(order => ({
            id: order.id,
            orderId: order.orderNo,
            status: order.status,
            statusText: this.getStatusText(order.status),
            statusBgColor: 'linear-gradient(135deg, #A8E6CF 0%, #DDA0DD 100%)',
            title: '摄影套餐',
            photographerName: '摄影师',
            price: order.totalAmount,
            imageUrl: 'https://api.dicebear.com/7.x/personas/svg?seed=photography&size=200',
            createTime: order.createTime
          }));
          this.setData({ 
            orderList: formattedOrders,
            loading: false 
          });
        } else {
          this.setData({ loading: false });
        }
      },
      fail: (err) => {
        console.error('加载摄影师订单列表失败:', err);
        this.setData({ loading: false });
      }
    });
  },
  
  // 获取订单状态文本
  getStatusText(status) {
    switch (status) {
      case 0:
        return '待付款';
      case 1:
        return '已支付定金';
      case 2:
        return '进行中';
      case 3:
        return '拍摄完成';
      case 4:
        return '已完成';
      case 5:
        return '已取消';
      default:
        return '未知状态';
    }
  },
  
  // 格式化日期时间
  formatDateTime(dateTime) {
    if (!dateTime) return '';
    // 将ISO 8601格式转换为YYYY-MM-DD HH:MM格式
    const date = new Date(dateTime);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}`;
  },
  
  // 切换订单状态
  switchStatus(e) {
    const status = e.currentTarget.dataset.value;
    this.setData({ activeStatus: status });
    this.loadOrderList();
  },
  
  // 返回上一页
  navigateBack() {
    wx.navigateBack();
  },
  
  // 跳转到订单详情
  navigateToDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/order/detail?id=${id}&type=${this.data.type}`
    });
  },
  
  // 取消订单
  cancelOrder(e) {
    const id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '取消订单',
      content: '确定要取消这个订单吗？',
      success: (res) => {
        if (res.confirm) {
          // 调用后端API取消订单
          const token = wx.getStorageSync('token');
          wx.request({
            url: `${getApp().globalData.baseUrl}/api/order/status`,
            method: 'PUT',
            header: { 'Authorization': 'Bearer ' + token, 'content-type': 'application/json' },
            data: { id, status: 5 }, // 5表示已取消
            timeout: 30000,
            success: (res) => {
              if (res.data && res.data.code === 200) {
                wx.showToast({ title: '订单已取消', icon: 'success' });
                this.loadOrderList();
              } else {
                wx.showToast({ title: res.data?.message || '取消失败', icon: 'none' });
              }
            },
            fail: (err) => {
              console.error('取消订单失败:', err);
              wx.showToast({ title: '取消失败，请重试', icon: 'none' });
            }
          });
        }
      }
    });
  },
  
  // 立即付款
  payOrder(e) {
    const id = e.currentTarget.dataset.id;
    wx.showToast({ title: '跳转到支付页面', icon: 'success' });
  },
  
  // 确认订单（摄影师确认）
  confirmOrder(e) {
    const id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '确认订单',
      content: '确定要确认这个订单吗？',
      success: (res) => {
        if (res.confirm) {
          // 模拟确认订单
          setTimeout(() => {
            wx.showToast({ title: '订单已确认', icon: 'success' });
            this.loadOrderList();
          }, 1000);
          
          // 调用后端API确认订单
          const token = wx.getStorageSync('token');
          wx.request({
            url: `${getApp().globalData.baseUrl}/api/order/status`,
            method: 'PUT',
            header: { 'Authorization': 'Bearer ' + token, 'content-type': 'application/json' },
            data: { id, status: 2 }, // 进行中
            timeout: 30000,
            success: (res) => {
              if (res.data && res.data.code === 200) {
                wx.showToast({ title: '订单已确认', icon: 'success' });
                this.loadOrderList();
              }
            },
            fail: (err) => {
              console.error('确认订单失败:', err);
              wx.showToast({ title: '确认失败，请重试', icon: 'none' });
            }
          });
        }
      }
    });
  },
  
  // 确认拍摄完成
  confirmShoot(e) {
    const id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '确认拍摄完成',
      content: '确定拍摄已完成吗？',
      success: (res) => {
        if (res.confirm) {
          // 模拟确认拍摄完成
          setTimeout(() => {
            wx.showToast({ title: '拍摄已完成', icon: 'success' });
            this.loadOrderList();
          }, 1000);
          
          // 调用后端API确认拍摄完成
          const token = wx.getStorageSync('token');
          wx.request({
            url: `${getApp().globalData.baseUrl}/api/order/status`,
            method: 'PUT',
            header: { 'Authorization': 'Bearer ' + token, 'content-type': 'application/json' },
            data: { id, status: 3 }, // 拍摄完成
            timeout: 30000,
            success: (res) => {
              if (res.data && res.data.code === 200) {
                wx.showToast({ title: '拍摄已完成', icon: 'success' });
                this.loadOrderList();
              }
            },
            fail: (err) => {
              console.error('确认拍摄失败:', err);
              wx.showToast({ title: '确认失败，请重试', icon: 'none' });
            }
          });
        }
      }
    });
  },
  
  // 支付尾款
  payBalance(e) {
    const id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '支付尾款',
      content: '确定要支付尾款吗？',
      success: (res) => {
        if (res.confirm) {
          // 模拟支付尾款
          setTimeout(() => {
            wx.showToast({ title: '支付成功', icon: 'success' });
            this.loadOrderList();
          }, 1000);
          
          // 调用后端API支付尾款
          const token = wx.getStorageSync('token');
          wx.request({
            url: `${getApp().globalData.baseUrl}/api/order/status`,
            method: 'PUT',
            header: { 'Authorization': 'Bearer ' + token, 'content-type': 'application/json' },
            data: { id, status: 4 }, // 已完成
            timeout: 30000,
            success: (res) => {
              if (res.data && res.data.code === 200) {
                wx.showToast({ title: '支付成功', icon: 'success' });
                this.loadOrderList();
              }
            },
            fail: (err) => {
              console.error('支付尾款失败:', err);
              wx.showToast({ title: '支付失败，请重试', icon: 'none' });
            }
          });
        }
      }
    });
  },
  
  // 评价订单
  reviewOrder(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/order/review?id=${id}`
    });
  }
});