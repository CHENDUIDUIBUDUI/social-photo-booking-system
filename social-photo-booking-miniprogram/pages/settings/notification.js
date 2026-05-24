// notification.js
Page({
  data: {
    notification: {
      newMessage: true,
      comment: true,
      like: true,
      follow: true,
      newOrder: true,
      orderStatus: true,
      system: true,
      activity: false
    }
  },
  
  onLoad() {
    this.loadNotificationSettings();
  },
  
  loadNotificationSettings() {
    // 模拟加载通知设置
    // 实际项目中，这里应该从后端获取通知设置
  },
  
  navigateBack() {
    wx.navigateBack();
  },
  
  toggleNewMessage(e) {
    this.setData({
      'notification.newMessage': e.detail.value
    });
    this.saveNotificationSettings();
  },
  
  toggleComment(e) {
    this.setData({
      'notification.comment': e.detail.value
    });
    this.saveNotificationSettings();
  },
  
  toggleLike(e) {
    this.setData({
      'notification.like': e.detail.value
    });
    this.saveNotificationSettings();
  },
  
  toggleFollow(e) {
    this.setData({
      'notification.follow': e.detail.value
    });
    this.saveNotificationSettings();
  },
  
  toggleNewOrder(e) {
    this.setData({
      'notification.newOrder': e.detail.value
    });
    this.saveNotificationSettings();
  },
  
  toggleOrderStatus(e) {
    this.setData({
      'notification.orderStatus': e.detail.value
    });
    this.saveNotificationSettings();
  },
  
  toggleSystem(e) {
    this.setData({
      'notification.system': e.detail.value
    });
    this.saveNotificationSettings();
  },
  
  toggleActivity(e) {
    this.setData({
      'notification.activity': e.detail.value
    });
    this.saveNotificationSettings();
  },
  
  saveNotificationSettings() {
    // 模拟保存通知设置
    // 实际项目中，这里应该将通知设置保存到后�?    wx.showToast({ title: '设置已保�?, icon: 'success' });
  }
});
