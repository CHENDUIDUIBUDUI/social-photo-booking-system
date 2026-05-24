// privacy.js
Page({
  data: {
    privacy: {
      publicProfile: true,
      showWorks: true,
      allowMessage: false,
      allowFollow: true
    }
  },
  
  onLoad() {
    this.loadPrivacySettings();
  },
  
  loadPrivacySettings() {
    // 模拟加载隐私设置
    // 实际项目中，这里应该从后端获取隐私设置
  }, 
  
  navigateBack() {
    wx.navigateBack();
  },
  
  togglePublicProfile(e) {
    this.setData({
      'privacy.publicProfile': e.detail.value
    });
    this.savePrivacySettings();
  },
  
  toggleShowWorks(e) {
    this.setData({
      'privacy.showWorks': e.detail.value
    });
    this.savePrivacySettings();
  },
  
  toggleAllowMessage(e) {
    this.setData({
      'privacy.allowMessage': e.detail.value
    });
    this.savePrivacySettings();
  },
  
  toggleAllowFollow(e) {
    this.setData({
      'privacy.allowFollow': e.detail.value
    });
    this.savePrivacySettings();
  },
  
  savePrivacySettings() {
    // 模拟保存隐私设置
    // 实际项目中，这里应该将隐私设置保存到后端
    wx.showToast({ title: '设置已保存', icon: 'success' });
  },
  
  navigateToBlacklist() {
    wx.navigateTo({
      url: '/pages/settings/blacklist'
    });
  }
});
