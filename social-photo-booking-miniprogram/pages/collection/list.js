// list.js
Page({
  data: {
    collectionList: [],
    loading: false
  },
  
  onLoad() {
    this.loadCollectionList();
  },
  
  loadCollectionList() {
    const token = wx.getStorageSync('token');
    if (!token) {
      return;
    }
    
    this.setData({ loading: true });
    
    wx.request({
      url: `${getApp().globalData.baseUrl}/collection/list`,
      method: 'GET',
      header: { 'Authorization': 'Bearer ' + token },
      data: {
        page: 1,
        pageSize: 20
      },
      timeout: 30000,
      success: (res) => {
        
        if (res.data && res.data.code === 200 && res.data.data) {
          const collectionList = res.data.data.list || [];
          this.setData({ 
            collectionList,
            loading: false 
          });
        } else {
          this.setData({ loading: false });
        }
      },
      fail: (err) => {
        console.error('加载收藏列表失败:', err);
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
  
  cancelCollection(e) {
    const id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '取消收藏',
      content: '确定要取消收藏这个作品吗？',
      success: (res) => {
        if (res.confirm) {
          // 调用后端API取消收藏
          const token = wx.getStorageSync('token');
          wx.request({
            url: `${getApp().globalData.baseUrl}/collection/cancel`,
            method: 'POST',
            header: { 'Authorization': 'Bearer ' + token },
            data: { contentId: id },
            timeout: 30000,
            success: (res) => {
              if (res.data && res.data.code === 200) {
                const collectionList = this.data.collectionList;
                const index = collectionList.findIndex(item => item.id === id);
                if (index !== -1) {
                  collectionList.splice(index, 1);
                  this.setData({ collectionList });
                  wx.showToast({ title: '已取消收藏', icon: 'success' });
                }
              }
            },
            fail: (err) => {
              console.error('取消收藏失败:', err);
              wx.showToast({ title: '取消失败，请重试', icon: 'none' });
            }
          });
        }
      }
    });
  }
});
