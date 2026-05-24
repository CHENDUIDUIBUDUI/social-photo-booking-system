// about.js
Page({
  data: {
    version: '1.0.0'
  },
  
  onLoad() {
    this.loadAppInfo();
  },
  
  loadAppInfo() {
    // 模拟加载应用信息
    // 实际项目中，这里应该从后端获取应用信息
  },
  
  navigateBack() {
    wx.navigateBack();
  },
  
  navigateToPrivacy() {
    wx.navigateTo({
      url: '/pages/agreement/privacy'
    });
  },
  
  navigateToService() {
    wx.navigateTo({
      url: '/pages/agreement/service'
    });
  }
});
