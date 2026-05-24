// 角色选择页面逻辑
Page({
  /**
   * 页面的初始数据
   */
  data: {
    loading: false,
    errorMessage: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  /**
   * 选择角色
   */
  selectRole: function (e) {
    const roleStr = e.currentTarget.dataset.role
    let role = 0
    switch (roleStr) {
      case 'photographer':
        role = 1
        break
      case 'model':
        role = 2
        break
      case 'user':
        role = 0
        break
      default:
        role = 0
    }
    this.setData({
      loading: true,
      errorMessage: ''
    })

    // 调用后端接口更新用户角色
    this.updateUserRole(role)
  },

  /**
   * 更新用户角色
   */
  updateUserRole: function (role) {
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.redirectTo({
        url: '/pages/login/index'
      })
      return
    }

    console.log('开始更新用户角色，请求地址:', `${getApp().globalData.baseUrl}/api/auth/user/role`);
    
    
    
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
          const userInfo = wx.getStorageSync('userInfo')
          userInfo.role = role
          wx.setStorageSync('userInfo', userInfo)
          
          // 跳转到欢迎页面
          wx.redirectTo({
            url: '/pages/welcome/welcome'
          })
        } else {
          this.setData({
            loading: false,
            errorMessage: res.data.message || '更新角色失败，请重试'
          })
        }
      },
      fail: (err) => {
        console.error('更新角色失败:', err);
        if (err.errMsg.includes('request:fail')) {
          if (err.errMsg.includes('timeout')) {
            console.error('网络请求超时，请检查网络连接');
            this.setData({
              loading: false,
              errorMessage: '网络请求超时，请检查网络连接'
            });
          } else {
            console.error('网络请求失败，请检查网络连接或在开发者工具中开启不校验域名选项');
            this.setData({
              loading: false,
              errorMessage: '网络请求失败，请检查网络连接或在开发者工具中开启不校验域名选项'
            });
          }
        } else {
          console.error('网络错误，请重试');
          this.setData({
            loading: false,
            errorMessage: '网络错误，请重试'
          });
        }
      }
    })
  }
})
