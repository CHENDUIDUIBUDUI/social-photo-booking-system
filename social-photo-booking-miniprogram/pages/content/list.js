// list.js
Page({
  data: {
    contentList: [],
    type: '0', // 0: 作品, 1: 需�?    loading: false
  },
  
  onLoad(options) {
    if (options.type) {
      this.setData({ type: options.type });
    }
    this.loadContentList();
  },
  
  loadContentList() {
    const token = wx.getStorageSync('token');
    if (!token) {
      return;
    }
    
    this.setData({ loading: true });
    
    wx.request({
      url: `${getApp().globalData.baseUrl}/content/my-list`,
      method: 'GET',
      header: { 'Authorization': 'Bearer ' + token },
      data: {
        page: 1,
        pageSize: 20
      },
      timeout: 30000,
      success: (res) => {
        
        if (res.data && res.data.code === 200 && res.data.data) {
          const contentList = res.data.data.list || [];
          this.setData({ 
            contentList,
            loading: false 
          });
        } else {
          this.setData({ loading: false });
        }
      },
      fail: (err) => {
        console.error('加载内容列表失败:', err);
        this.setData({ loading: false });
      }
    });
  },
  
  navigateBack() {
    wx.navigateBack();
  },
  
  navigateToDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/content/detail?id=${id}`
    });
  },
  
  createContent() {
    wx.navigateTo({
      url: `/pages/content/create?type=${this.data.type}`
    });
  }
});
