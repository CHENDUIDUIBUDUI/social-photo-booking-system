// index.js
Page({
  data: {
    cacheSize: '0.00MB',
    version: '1.0.0'
  },
  
  onLoad() {
    this.getCacheSize();
  },
  
  getCacheSize() {
    // 模拟获取缓存大小
    this.setData({ cacheSize: '0.00MB' });
  },
  
  navigateBack() {
    wx.navigateBack();
  },
  
  navigateToAccountSettings() {
    wx.navigateTo({
      url: '/pages/settings/account'
    });
  },
  
  navigateToPrivacySettings() {
    wx.navigateTo({
      url: '/pages/settings/privacy'
    });
  },
  
  navigateToNotificationSettings() {
    wx.navigateTo({
      url: '/pages/settings/notification'
    });
  },
  
  clearCache() {
    wx.showModal({
      title: '清除缓存',
      content: '确定要清除所有缓存吗？',
      success: (res) => {
        if (res.confirm) {
          // 模拟清除缓存
          this.setData({ cacheSize: '0.00MB' });
          wx.showToast({ title: '缓存已清理', icon: 'success' });
        }
      }
    });
  },
  
  checkUpdate() {
    // 模拟检查更�?    wx.showToast({ title: '当前已是最新版�?, icon: 'success' });
  },
  
  navigateToAbout() {
    wx.navigateTo({
      url: '/pages/settings/about'
    });
  },
  
  navigateToFeedback() {
    wx.navigateTo({
      url: '/pages/settings/feedback'
    });
  },
  
  navigateToHelp() {
    wx.navigateTo({
      url: '/pages/settings/help'
    });
  },
  
  handleLogout() {
    wx.showModal({
      title: '退出登录',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          getApp().setToken(null);
          getApp().setUserInfo(null);
          wx.removeStorageSync('token');
          wx.removeStorageSync('userInfo');
          wx.navigateTo({
            url: '/pages/login/index'
          });
        }
      }
    });
  }
});
