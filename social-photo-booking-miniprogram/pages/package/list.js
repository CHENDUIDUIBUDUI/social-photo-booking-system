// 套餐管理页面
const app = getApp();

Page({
  data: {
    packages: [],
    photographerId: null
  },
  
  onLoad() {
    this.loadPhotographerInfo();
  },
  
  // 加载摄影师信息
  loadPhotographerInfo() {
    const token = wx.getStorageSync('token');
    const userInfo = wx.getStorageSync('userInfo');
    
    
    
    
    if (!token || !userInfo) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    
    const requestUrl = app.globalData.baseUrl + '/api/photographer/user';
    
    
    
    wx.request({
      url: requestUrl,
      method: 'GET',
      data: {
        userId: userInfo.id
      },
      header: {
        'Authorization': 'Bearer ' + token
      },
      success: (res) => {
        
        if (res.data.code === 200) {
          
          this.setData({
            photographerId: res.data.data.id
          });
          // 获取到photographerId后再加载套餐列表
          this.loadPackages(res.data.data.id);
        } else if (res.data.code === 404) {
          
          wx.showModal({
            title: '提示',
            content: '您还不是摄影师，请先申请成为摄影师后再管理套餐',
            confirmText: '去申请',
            cancelText: '取消',
            success: (modalRes) => {
              if (modalRes.confirm) {
                // 跳转到摄影师申请页面
                wx.navigateTo({
                  url: '/pages/profile/apply-photographer'
                });
              }
            }
          });
        } else {
          
          wx.showToast({ title: '获取摄影师信息失败 ' + (res.data.message || '未知错误'), icon: 'none' });
        }
      },
      fail: (err) => {
        console.error('获取摄影师信息网络错误', err);
        wx.showToast({ title: '网络错误: ' + err.errMsg, icon: 'none' });
      }
    });
  },
  
  // 加载套餐列表
  loadPackages(photographerId) {
    wx.showLoading({ title: '加载�?..' });
    
    const token = wx.getStorageSync('token');
    
    if (!token) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    
    wx.request({
      url: app.globalData.baseUrl + '/api/package/list',
      method: 'GET',
      data: {
        photographerId: photographerId
      },
      header: {
        'Authorization': 'Bearer ' + token
      },
      success: (res) => {
        if (res.data.code === 200) {
          const packages = res.data.data || [];
          // 处理features数组
          packages.forEach(pack => {
            try {
              pack.featuresArray = JSON.parse(pack.features);
            } catch (e) {
              pack.featuresArray = [];
            }
          });
          this.setData({ packages });
        } else {
          wx.showToast({ title: res.data.message || '加载失败', icon: 'none' });
        }
      },
      fail: (err) => {
        console.error('加载套餐失败:', err);
        wx.showToast({ title: '网络错误', icon: 'none' });
      },
      complete: () => {
        wx.hideLoading();
      }
    });
  },
  
  // 返回上一页
  goBack() {
    wx.navigateBack();
  },
  
  // 跳转到创建套餐页面
  navigateToCreate() {
    wx.navigateTo({
      url: '/pages/package/create'
    });
  },
  
  // 跳转到编辑套餐页面
  navigateToEdit(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/package/edit?id=${id}`
    });
  },
  
  // 删除套餐
  deletePackage(e) {
    const id = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '确认删除',
      content: '确定要删除这个套餐吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '删除中..' });
          
          const token = wx.getStorageSync('token');
          
          wx.request({
            url: app.globalData.baseUrl + '/api/package/delete',
            method: 'DELETE',
            data: {
              id: id
            },
            header: {
              'Authorization': 'Bearer ' + token
            },
            success: (res) => {
              if (res.data.code === 200) {
                wx.showToast({ title: '删除成功', icon: 'success' });
                // 重新加载套餐列表
                const photographerId = this.data.photographerId;
                if (photographerId) {
                  this.loadPackages(photographerId);
                }
              } else {
                wx.showToast({ title: res.data.message || '删除失败', icon: 'none' });
              }
            },
            fail: (err) => {
              console.error('删除套餐失败:', err);
              wx.showToast({ title: '网络错误', icon: 'none' });
            },
            complete: () => {
              wx.hideLoading();
            }
          });
        }
      }
    });
  }
});
