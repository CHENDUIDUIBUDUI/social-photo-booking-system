// list.js
Page({
  data: {
    followList: [],
    loading: false
  },
  
  onLoad() {
    this.loadFollowList();
  },
  
  loadFollowList() {
    const token = wx.getStorageSync('token');
    if (!token) {
      return;
    }
    
    this.setData({ loading: true });
    
    wx.request({
      url: `${getApp().globalData.baseUrl}/follow/list`,
      method: 'GET',
      header: { 'Authorization': 'Bearer ' + token },
      data: {
        page: 1,
        pageSize: 20
      },
      timeout: 30000,
      success: (res) => {
        
        if (res.data && res.data.code === 200 && res.data.data) {
          const followList = res.data.data.list || [];
          this.setData({ 
            followList,
            loading: false 
          });
        } else {
          this.setData({ loading: false });
        }
      },
      fail: (err) => {
        console.error('加载关注列表失败:', err);
        this.setData({ loading: false });
      }
    });
  },
  
  navigateBack() {
    wx.navigateBack();
  },
  
  unfollow(e) {
    const id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '取消关注',
      content: '确定要取消关注这个用户吗？',
      success: (res) => {
        if (res.confirm) {
          // 调用后端API取消关注
          const token = wx.getStorageSync('token');
          wx.request({
            url: `${getApp().globalData.baseUrl}/follow/cancel`,
            method: 'POST',
            header: { 'Authorization': 'Bearer ' + token },
            data: { followUserId: id },
            timeout: 30000,
            success: (res) => {
              if (res.data && res.data.code === 200) {
                const followList = this.data.followList;
                const index = followList.findIndex(item => item.id === id);
                if (index !== -1) {
                  followList.splice(index, 1);
                  this.setData({ followList });
                  wx.showToast({ title: '已取消关注', icon: 'success' });
                }
              }
            },
            fail: (err) => {
              console.error('取消关注失败:', err);
              wx.showToast({ title: '取消失败，请重试', icon: 'none' });
            }
          });
        }
      }
    });
  }
});
