// 登录页面逻辑
Page({
  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {
      avatarUrl: '',
      nickName: ''
    },
    loading: false,
    errorMessage: '',
    isGettingUserInfo: false // 防止重复调用 getUserProfile
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 页面加载时不再自动获取用户信息，需要用户点击头像
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
    // 清除倒计时
    if (this.timer) {
      clearInterval(this.timer);
    }
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
   * 获取用户信息
   */
  getUserProfile: function () {
    // 防止重复调用
    if (this.data.isGettingUserInfo) {
      return
    }
    
    this.setData({ isGettingUserInfo: true })
    
    wx.getUserProfile({
      desc: '用于完善用户资料',
      success: (res) => {
        console.log('获取用户信息成功:', res.userInfo)
        this.setData({
          userInfo: {
            avatarUrl: res.userInfo.avatarUrl,
            nickName: res.userInfo.nickName
          },
          isGettingUserInfo: false
        })
      },
      fail: (err) => {
        console.log('获取用户信息失败:', err)
        wx.showToast({
          title: '获取用户信息失败，请重试',
          icon: 'none'
        })
        this.setData({ isGettingUserInfo: false })
      }
    })
  },

  /**
   * 获取用户信息回调
   */
  onGetUserProfile: function (e) {
    console.log('获取用户信息回调:', e.detail)
    
    if (e.detail.errMsg === 'getUserInfo:ok') {
      const userInfo = e.detail.userInfo
      console.log('用户信息:', userInfo)
      
      this.setData({
        userInfo: {
          avatarUrl: userInfo.avatarUrl,
          nickName: userInfo.nickName
        }
      })
    } else {
      console.log('获取用户信息失败:', e.detail.errMsg)
      wx.showToast({
        title: '获取用户信息失败，请重试',
        icon: 'none'
      })
    }
  },

  /**
   * 点击头像获取用户信息
   */
  getUserInfo: function () {
    console.log('用户点击头像，准备获取用户信息')
    this.getUserProfile()
  },

  /**
   * 微信登录
   */
  wechatLogin: function () {
    // 先获取微信登录凭证
    wx.login({
      success: (loginRes) => {
        if (loginRes.code) {
          
          // 检查是否已经获取了用户信息
          if (!this.data.userInfo.avatarUrl || !this.data.userInfo.nickName) {
            // 如果没有获取用户信息，提示用户点击头像授权
            wx.showModal({
              title: '授权提示',
              content: '请点击上方头像获取用户信息后再登录',
              showCancel: false
            });
          } else {
            // 如果已经获取了用户信息，直接发送登录请求
            this.setData({
              loading: true,
              errorMessage: ''
            });
            this.requestWechatLogin(loginRes.code, this.data.userInfo);
          }
        } else {
          
          this.setData({
            loading: false,
            errorMessage: '登录失败，请重试'
          });
        }
      },
      fail: (err) => {
        
        this.setData({
          loading: false,
          errorMessage: '登录失败，请重试'
        });
      }
    });
  },

  /**
   * 调用微信登录接口
   */
  requestWechatLogin: function (code, userInfo) {
    const app = getApp();
    console.log('开始调用微信登录接口，code:', code)
    console.log('用户信息:', userInfo)
    console.log('请求地址:', `${app.globalData.baseUrl}/api/auth/login`)
    
    // 使用全局配置的baseUrl
    const loginUrl = `${app.globalData.baseUrl}/api/auth/login`;
    
    
    wx.request({
      url: loginUrl,
      method: 'POST',
      data: {
        code: code,
        userInfo: userInfo
      },
      timeout: 30000, // 增加超时时间
      success: (res) => {
        console.log('登录接口响应:', res)
        this.handleLoginResponse(res)
      },
      fail: (err) => {
        console.log('请求登录失败:', err)
        if (err.errMsg.includes('request:fail')) {
            if (err.errMsg.includes('timeout')) {
              this.setData({
                loading: false,
                errorMessage: '网络请求超时，请检查网络连接'
              })
            } else {
              this.setData({
                loading: false,
                errorMessage: '网络请求失败，请检查网络连接或在开发者工具中开启不校验域名选项'
              })
            }
          } else {
            this.setData({
              loading: false,
              errorMessage: '网络错误，请重试'
            })
          }
      }
    })
  },

  /**
   * 处理登录响应
   */
  handleLoginResponse: function (res) {
    if (res.data.code === 200) {
      // 登录成功，存储token和用户信息
      const token = res.data.data.token
      const user = res.data.data.user
      
      // 合并微信用户信息到用户数据中
      const existingUserInfo = wx.getStorageSync('userInfo') || {};
      const userInfo = {
        ...user,
        avatarUrl: this.data.userInfo.avatarUrl || user.avatarUrl || user.avatar,
        nickName: this.data.userInfo.nickName || user.nickName || user.nickname,
        role: existingUserInfo.role || user.role // 保留之前的角色信息
      }
      
      wx.setStorageSync('token', token)
      wx.setStorageSync('userInfo', userInfo)
      wx.setStorageSync('userId', user.id) // 存储userId
      
      // 更新全局数据
      getApp().setToken(token)
      getApp().setUserInfo(userInfo)
      
      // 无论用户是否有角色，都直接跳转到欢迎页面
      // 角色选择将在个人中心进行
      wx.redirectTo({
        url: '/pages/welcome/welcome'
      })
    } else {
      this.setData({
        loading: false,
        errorMessage: res.data.message || '登录失败，请重试'
      })
    }
  }
})

