Page({
  data: {
    loadingProgress: 0
  },

  onLoad() {
    this.startLoading();
  },

  startLoading() {
    let progress = 0;
    const timer = setInterval(() => {
      progress += 3.33;
      if (progress >= 100) {
        progress = 100;
        clearInterval(timer);
        this.navigateToNextPage();
      }
      this.setData({ loadingProgress: progress });
    }, 100);
  },

  navigateToNextPage() {
    const token = wx.getStorageSync('token');
    const userInfo = wx.getStorageSync('userInfo');
    
    if (token && userInfo) {
      wx.switchTab({
        url: '/pages/index/index'
      });
    } else {
      wx.redirectTo({
        url: '/pages/login/index'
      });
    }
  }
});
