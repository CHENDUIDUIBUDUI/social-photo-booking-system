// detail.js
const app = getApp();

Page({
  data: {
    order: null,
    loading: true,
    deposit: null
  },
  
  onLoad(options) {
    const id = options.id;
    this.loadOrderDetail(id);
  },
  
  loadOrderDetail(id) {
    const token = wx.getStorageSync('token');
    if (!token) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    
    wx.showLoading({ title: '加载中..' });
    wx.request({
      url: app.globalData.baseUrl + '/api/order/info',
      method: 'GET',
      data: { id: id },
      header: {
        'Authorization': 'Bearer ' + token
      },
      success: (res) => {
        wx.hideLoading();
        if (res.data && res.data.code === 200 && res.data.data) {
          const order = res.data.data;
          // 转换订单状态
          const statusInfo = this.getStatusInfo(order.status);
          
          // 构建订单数据结构
            const orderData = {
                id: order.id,
                orderId: order.orderNo,
                status: order.status,
                statusText: statusInfo.text,
                statusColor: statusInfo.color,
                statusDesc: statusInfo.desc,
                createTime: this.formatDateTime(order.createTime),
                completeTime: this.formatDateTime(order.updateTime),
                service: {
                    title: '摄影服务',
                    price: order.totalAmount,
                    description: '',
                    imageUrl: 'https://api.dicebear.com/7.x/personas/svg?seed=photography&size=300'
                },
                contact: {
                    name: order.contactName || '',
                    phone: order.contactPhone || '',
                    location: order.location || '',
                    time: order.shootTime ? this.formatDateTime(order.shootTime) : ''
                },
                remark: order.notes || '',
                deposit: order.deposit || (order.totalAmount * 0.3).toFixed(2),
                paidDeposit: order.paidDeposit || 0
            };
          
          this.setData({ 
            order: orderData, 
            loading: false,
            deposit: orderData.deposit
          });
        } else {
          wx.showToast({ title: '获取订单详情失败', icon: 'none' });
          this.setData({ loading: false });
        }
      },
      fail: (err) => {
        wx.hideLoading();
        wx.showToast({ title: '网络错误', icon: 'none' });
        this.setData({ loading: false });
        console.error('获取订单详情失败:', err);
      }
    });
  },
  
  getStatusInfo(status) {
    switch (status) {
      case 0:
        return { text: '待付款', color: '#E6A23C', desc: '订单等待支付定金' };
      case 1:
        return { text: '已支付定金', color: '#E6A23C', desc: '定金已支付，等待摄影师确认' };
      case 2:
        return { text: '进行中', color: '#409EFF', desc: '订单已确认，等待拍摄' };
      case 3:
        return { text: '拍摄完成', color: '#67C23A', desc: '拍摄已完成，等待支付尾款' };
      case 4:
        return { text: '已完成', color: '#67C23A', desc: '订单已完成' };
      case 5:
        return { text: '已取消', color: '#909399', desc: '订单已取消' };
      default:
        return { text: '未知状态', color: '#909399', desc: '订单状态未知' };
    }
  },
  
  formatDateTime(dateTime) {
    // 格式化日期时�?    if (!dateTime) return '';
    // 将ISO 8601格式转换�?YYYY-MM-DD HH:MM 格式
    const date = new Date(dateTime);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}`;
  },
  
  navigateBack() {
    wx.navigateBack();
  },
  
  cancelOrder() {
    wx.showModal({
      title: '取消订单',
      content: '确定要取消这个订单吗？',
      success: (res) => {
        if (res.confirm) {
          // 模拟取消订单
          this.setData({
            'order.status': 4,
            'order.statusText': '已取消',
            'order.statusColor': '#909399',
            'order.statusDesc': '订单已取消'
          });
          wx.showToast({ title: '订单已取消', icon: 'success' });
        }
      }
    });
  },
  
  confirmOrder() {
    wx.showModal({
      title: '确认完成',
      content: '确定要确认完成这个订单吗？',
      success: (res) => {
        if (res.confirm) {
          // 模拟确认完成
          this.setData({
            'order.status': 3,
            'order.statusText': '已完成',
            'order.statusColor': '#67c23a',
            'order.statusDesc': '订单已完成',
            'order.completeTime': '2026-03-27 18:00:00'
          });
          wx.showToast({ title: '订单已完成', icon: 'success' });
        }
      }
    });
  },
  
  payDeposit() {
    const deposit = this.data.deposit;
    const orderId = this.data.order.id;
    if (!deposit || !orderId) return;
    
    // 模拟微信支付弹窗
    wx.showModal({
      title: '微信支付',
      content: `请支付定金 ¥${deposit}`,
      confirmText: '立即支付',
      cancelText: '取消',
      success: (payRes) => {
        if (payRes.confirm) {
          // 模拟支付过程
          wx.showLoading({ title: '支付中...' });
          
          setTimeout(() => {
            wx.hideLoading();
            wx.showToast({
              title: '支付成功',
              icon: 'success'
            });
            
            // 更新订单状态和支付信息
            this.setData({
              'order.status': 1, // 已支付定金
              'order.statusText': '已支付定金',
              'order.statusColor': '#E6A23C',
              'order.statusDesc': '定金已支付，等待摄影师确认',
              'order.paidDeposit': deposit
            });
            
            // 调用后端 API 更新订单状态
            const token = wx.getStorageSync('token');
            if (token) {
              wx.request({
                url: app.globalData.baseUrl + '/api/order/status',
                method: 'PUT',
                header: {
                  'Authorization': 'Bearer ' + token,
                  'content-type': 'application/json'
                },
                data: {
                  id: orderId,
                  status: 1 // 已支付定金
                },
                success: (res) => {
                  console.log('更新订单状态成功', res);
                },
                fail: (err) => {
                  console.error('更新订单状态失败', err);
                }
              });
            }
          }, 1500);
        }
      }
    });
  }
});
