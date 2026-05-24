// list.js
Page({
  data: {
    // 标签栏状态
    activeTab: 'system',
    
    // 未读消息数
    unreadCount: {
      system: 1,
      order: 0
    },
    
    // 系统通知列表
    systemList: [
      {
        id: 1,
        content: '您的账户已成功认证，现在可以开始接单了',
        time: '2026-03-29',
        unread: true
      },
      {
        id: 2,
        content: '平台新功能上线：现在可以直接在消息中发送图片了',
        time: '2026-03-28',
        unread: false
      },
      {
        id: 3,
        content: '您的个人资料已更新成功',
        time: '2026-03-27',
        unread: false
      }
    ],
    
    // 订单通知列表
    orderList: [
      {
        id: 1,
        title: '订单已确认',
        content: '您的订单 #10001 已被摄影师确认，拍摄时间为2026-04-01 14:00',
        time: '2026-03-28',
        unread: false
      },
      {
        id: 2,
        title: '订单已完成',
        content: '您的订单 #10000 已完成，感谢您的使用',
        time: '2026-03-25',
        unread: false
      }
    ],
    
    loading: false
  },
  
  onLoad() {
    this.loadMessageData();
  },
  
  onShow() {
    this.loadMessageData();
  },
  
  // 加载消息数据
  loadMessageData() {
    const token = wx.getStorageSync('token');
    if (!token) {
      return;
    }
    
    this.setData({ loading: true });
    
    // 模拟加载消息数据
    setTimeout(() => {
      this.setData({ loading: false });
    }, 1000);
    
    // 实际API调用（暂时注释，使用模拟数据）
    /*
    wx.request({
      url: `${getApp().globalData.baseUrl}/message/list`,
      method: 'GET',
      header: { 'Authorization': 'Bearer ' + token },
      data: {
        page: 1,
        pageSize: 20
      },
      timeout: 30000,
      success: (res) => {
        
        if (res.data && res.data.code === 200 && res.data.data) {
          // 处理API返回的数据
          this.setData({ 
            loading: false 
          });
        } else {
          this.setData({ loading: false });
        }
      },
      fail: (err) => {
        console.error('加载消息列表失败:', err);
        this.setData({ loading: false });
      }
    });
    */
  },
  
  // 切换标签
  switchTab(e) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({ activeTab: tab });
    
    // 切换标签时重置未读消息数
    if (tab === 'system') {
      this.setData({ 'unreadCount.system': 0 });
      // 标记所有系统通知为已读
      const updatedSystemList = this.data.systemList.map(item => ({
        ...item,
        unread: false
      }));
      this.setData({ systemList: updatedSystemList });
    } else if (tab === 'order') {
      this.setData({ 'unreadCount.order': 0 });
      // 标记所有订单通知为已读
      const updatedOrderList = this.data.orderList.map(item => ({
        ...item,
        unread: false
      }));
      this.setData({ orderList: updatedOrderList });
    }
  }
});
