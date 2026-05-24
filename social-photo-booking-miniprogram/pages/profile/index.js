// 个人中心页面
Page({
  data: {
    userInfo: {
      id: 1,
      nickname: '用户1',
      avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=user&size=150',
      role: 1, // 1: 摄影师
      creditScore: 95,
      worksCount: 12,
      fansCount: 89,
      followCount: 45,
      likeCount: 234
    }
  },
  
  onLoad(options) {
    this.setData({
      userId: options.userId || null
    });
    this.loadUserInfo();
  },
  
  loadUserInfo() {
    const userId = this.data.userId;
    const token = wx.getStorageSync('token');

    if (userId) {
      // 从后端获取指定用户的信息
      wx.request({
        url: `${getApp().globalData.baseUrl}/api/user/info`,
        method: 'GET',
        data: { id: userId },
        success: (res) => {
          if (res.data && res.data.code === 200 && res.data.data) {
            const userData = res.data.data;
            // 适配不同字段
            const adaptedUserInfo = {
              id: userData.id || 1,
              nickname: userData.nickName || userData.nickname || '用户',
              avatar: userData.avatarUrl || userData.avatar || 'https://api.dicebear.com/7.x/personas/svg?seed=user&size=150',
              role: userData.role || 0,
              creditScore: userData.creditScore || 90,
              worksCount: userData.worksCount || 0,
              fansCount: userData.fansCount || 0,
              followCount: userData.followCount || 0,
              likeCount: userData.likeCount || 0
            };
            this.setData({ userInfo: adaptedUserInfo });
          } else {
            // API返回错误，使用模拟数据
            const mockUserInfo = {
              id: userId,
              nickname: '用户' + userId,
              avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=user' + userId + '&size=150',
              role: 0, // 0: 普通用户
              creditScore: 90,
              worksCount: 0,
              fansCount: 0,
              followCount: 0,
              likeCount: 0
            };
            this.setData({ userInfo: mockUserInfo });
          }
        },
        fail: (err) => {
          console.error('获取用户信息失败', err);
          // 使用模拟数据
          const mockUserInfo = {
            id: userId,
            nickname: '用户' + userId,
            avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=user' + userId + '&size=150',
            role: 0, // 0: 普通用户
            creditScore: 90,
            worksCount: 0,
            fansCount: 0,
            followCount: 0,
            likeCount: 0
          };
          this.setData({ userInfo: mockUserInfo });
        }
      });
    } else {
      // 使用当前登录用户的信息 - 从服务器获取最新数据
      if (token) {
        const cachedUserInfo = wx.getStorageSync('userInfo');
        if (cachedUserInfo && cachedUserInfo.id) {
          // 先显示缓存数据，然后从服务器获取最新
          const adaptedUserInfo = {
            id: cachedUserInfo.id || 1,
            nickname: cachedUserInfo.nickName || cachedUserInfo.nickname || '用户',
            avatar: cachedUserInfo.avatarUrl || cachedUserInfo.avatar || 'https://api.dicebear.com/7.x/personas/svg?seed=user&size=150',
            role: cachedUserInfo.role || 0,
            creditScore: cachedUserInfo.creditScore || 90,
            worksCount: cachedUserInfo.worksCount || 0,
            fansCount: cachedUserInfo.fansCount || 0,
            followCount: cachedUserInfo.followCount || 0,
            likeCount: cachedUserInfo.likeCount || 0
          };
          this.setData({ userInfo: adaptedUserInfo });

          // 从服务器获取最新用户信息
          wx.request({
            url: `${getApp().globalData.baseUrl}/api/user/info`,
            method: 'GET',
            data: { id: cachedUserInfo.id },
            header: { 'Authorization': 'Bearer ' + token },
            success: (res) => {
              if (res.data && res.data.code === 200 && res.data.data) {
                const userData = res.data.data;
                const latestUserInfo = {
                  id: userData.id || cachedUserInfo.id,
                  nickname: userData.nickName || userData.nickname || '用户',
                  avatar: userData.avatarUrl || userData.avatar || cachedUserInfo.avatarUrl || 'https://api.dicebear.com/7.x/personas/svg?seed=user&size=150',
                  role: userData.role || cachedUserInfo.role || 0,
                  creditScore: userData.creditScore || 90,
                  worksCount: userData.worksCount || 0,
                  fansCount: userData.fansCount || 0,
                  followCount: userData.followCount || 0,
                  likeCount: userData.likeCount || 0
                };
                this.setData({ userInfo: latestUserInfo });
                wx.setStorageSync('userInfo', latestUserInfo);
                getApp().setUserInfo(latestUserInfo);
              }
            }
          });
        } else {
          // 没有缓存，显示模拟数据
          const mockUserInfo = {
            id: 1,
            nickname: '用户',
            avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=user&size=150',
            role: 0,
            creditScore: 90,
            worksCount: 0,
            fansCount: 0,
            followCount: 0,
            likeCount: 0
          };
          this.setData({ userInfo: mockUserInfo });
        }
      } else {
        // 未登录，显示模拟数据
        const mockUserInfo = {
          id: 1,
          nickname: '用户',
          avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=user&size=150',
          role: 0,
          creditScore: 90,
          worksCount: 0,
          fansCount: 0,
          followCount: 0,
          likeCount: 0
        };
        this.setData({ userInfo: mockUserInfo });
      }
    }
  },
  
  getRoleText(role) {
    switch (role) {
      case 0:
        return '普通用户';
      case 1:
        return '摄影师';
      case 2:
        return '模特';
      default:
        return '普通用户';
    }
  },
  
  navigateToEdit() {
    wx.navigateTo({
      url: '/pages/profile/edit'
    });
  },
  
  navigateToMyOrders() {
    wx.navigateTo({
      url: '/pages/order/list?type=user'
    });
  },
  
  navigateToMyCollections() {
    wx.navigateTo({
      url: '/pages/collection/list'
    });
  },
  
  navigateToMyEvaluations() {
    wx.showToast({ title: '跳转到我的评价页面', icon: 'success' });
  },
  
  navigateToRealAuth() {
    wx.showToast({ title: '跳转到实名认证页面', icon: 'success' });
  },
  
  navigateToCreatorCenter() {
    wx.showToast({ title: '跳转到创作者中心', icon: 'success' });
  },
  
  // 跳转到套餐管理页面
  navigateToPackageManagement() {
    wx.navigateTo({
      url: '/pages/package/list'
    });
  },
  
  navigateToSettings() {
    wx.navigateTo({
      url: '/pages/settings/index'
    });
  },
  
  navigateToHelp() {
    wx.showToast({ title: '跳转到帮助与客服页面', icon: 'success' });
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
  },
  
  // 申请摄影师
  applyPhotographer() {
    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }

    // 检查是否已经是摄影师
    if (userInfo.role === 1) {
      wx.showToast({
        title: '您已经是摄影师了',
        icon: 'none'
      });
      return;
    }

    // 跳转到摄影师申请页面
    wx.navigateTo({
      url: '/pages/profile/apply-photographer'
    });
  },
  
  // 申请模特
  applyModel() {
    wx.showModal({
      title: '申请模特',
      content: '确定要申请成为模特吗？申请后需要等待审核',
      success: (res) => {
        if (res.confirm) {
          this.updateUserRole(2);
        }
      }
    });
  },
  
  // 更新用户角色
  updateUserRole(role) {
    const token = wx.getStorageSync('token');
    if (!token) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    
    wx.request({
      url: `${getApp().globalData.baseUrl}/api/auth/user/role`,
      method: 'POST',
      header: {
        'Authorization': 'Bearer ' + token
      },
      data: {
        role: role
      },
      timeout: 30000,
      success: (res) => {
        if (res.data.code === 200) {
          // 更新本地存储的用户信息
          const userInfo = wx.getStorageSync('userInfo');
          userInfo.role = role;
          wx.setStorageSync('userInfo', userInfo);
          getApp().setUserInfo(userInfo);
          
          // 更新页面数据
          this.setData({ userInfo });
          
          wx.showToast({ 
            title: role === 1 ? '摄影师申请成功，等待审核' : '模特申请成功，等待审核', 
            icon: 'success' 
          });
        } else {
          wx.showToast({ 
            title: res.data.message || '申请失败，请重试', 
            icon: 'none' 
          });
        }
      },
      fail: (err) => {
        console.error('更新角色失败:', err);
        wx.showToast({ 
          title: '网络错误，请重试', 
          icon: 'none' 
        });
      }
    });
  }
});

