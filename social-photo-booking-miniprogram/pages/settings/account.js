// account.js
Page({
  data: {
    userInfo: {
      id: 1,
      nickname: '用户1',
      avatar: 'https://via.placeholder.com/150',
      phone: '13800138000',
      email: 'user@example.com'
    }
  },
  
  onLoad() {
    this.loadUserInfo();
  },
  
  loadUserInfo() {
    const userInfo = getApp().getUserInfo();
    if (userInfo) {
      this.setData({ userInfo });
    }
  },
  
  navigateBack() {
    wx.navigateBack();
  },
  
  navigateToPhone() {
    wx.navigateTo({
      url: '/pages/settings/phone'
    });
  },
  
  navigateToEmail() {
    wx.navigateTo({
      url: '/pages/settings/email'
    });
  },
  
  navigateToPassword() {
    wx.navigateTo({
      url: '/pages/settings/password'
    });
  },
  
  navigateToLoginDevice() {
    wx.navigateTo({
      url: '/pages/settings/login-device'
    });
  },
  
  navigateToDeleteAccount() {
    wx.navigateTo({
      url: '/pages/settings/delete-account'
    });
  }
});
