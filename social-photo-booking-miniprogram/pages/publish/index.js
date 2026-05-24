// publish/index.js
Page({
  data: {
    // 发布页面数据
  },
  
  onLoad() {
    // 页面加载逻辑
  },
  
  // 发布作品
  publishWork() {
    wx.navigateTo({
      url: '/pages/work/create'
    });
  },
  
  // 发布需求
  publishRequest() {
    wx.navigateTo({
      url: '/pages/content/create'
    });
  }
});
