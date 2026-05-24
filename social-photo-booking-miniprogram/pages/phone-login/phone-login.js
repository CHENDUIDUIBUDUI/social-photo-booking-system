// 手机号登录页面逻辑
Page({
  /**
   * 页面的初始数据
   */
  data: {
    phoneNumber: '',
    password: '',
    loading: false,
    errorMessage: '',
    showPassword: false
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
   * 绑定手机号码输入
   */
  bindPhoneInput: function (e) {
    this.setData({
      phoneNumber: e.detail.value
    })
  },

  /**
   * 绑定密码输入
   */
  bindPasswordInput: function (e) {
    this.setData({
      password: e.detail.value
    })
  },

  /**
   * 切换密码可见性
   */
  togglePasswordVisibility: function () {
    this.setData({
      showPassword: !this.data.showPassword
    })
  },

  /**
   * 处理输入框回车事件
   */
  handleEnterKey: function (e) {
    // 当在密码输入框按回车时，触发登录
    if (e.detail.keyCode === 13) {
      this.phoneLogin()
    }
  },

  /**
   * 手机号登录
   */
  phoneLogin: function () {
    const phoneNumber = this.data.phoneNumber
    const password = this.data.password

    // 验证表单
    if (!phoneNumber) {
      this.setData({ errorMessage: '请输入手机号码' })
      return
    }
    if (!/^1[3-9]\d{9}$/.test(phoneNumber)) {
      this.setData({ errorMessage: '请输入正确的手机号码' })
      return
    }
    if (!password) {
      this.setData({ errorMessage: '请输入密码' })
      return
    }
    if (password.length < 6) {
      this.setData({ errorMessage: '密码长度至少6位' })
      return
    }

    this.setData({
      loading: true,
      errorMessage: ''
    })

    // 调用后端登录接口
    this.requestPhoneLogin(phoneNumber, password)
  },

  /**
   * 调用手机号登录接口
   */
  requestPhoneLogin: function (phone, password) {
    const app = getApp();
    console.log('开始调用手机号码登录接口，phone:', phone, 'password:', password)
    console.log('请求地址:', `${app.globalData.baseUrl}/api/auth/login/phone`)
    
    // 使用全局配置的baseUrl
    const loginUrl = `${app.globalData.baseUrl}/api/auth/login/phone`;
    
    
    wx.request({
      url: loginUrl,
      method: 'POST',
      data: {
        phone: phone,
        password: password
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
      
      const userInfo = {
        ...user,
        avatarUrl: user.avatarUrl || user.avatar,
        nickName: user.nickName || user.nickname
      }
      
      wx.setStorageSync('token', token)
      wx.setStorageSync('userInfo', userInfo)
      
      // 更新全局数据
      getApp().setToken(token)
      getApp().setUserInfo(userInfo)
      
      // 检查用户是否已有角色
      if ((user.role !== undefined && user.role !== null && user.role !== 0) || (user.roleId !== undefined && user.roleId !== null && user.roleId !== 0)) {
        // 用户已有角色，直接跳转到欢迎页面
        wx.redirectTo({
          url: '/pages/welcome/welcome'
        })
      } else {
        // 用户没有角色，跳转到角色选择页面
        wx.redirectTo({
          url: '/pages/role/select'
        })
      }
    } else {
      this.setData({
        loading: false,
        errorMessage: res.data.message || '登录失败，请重试'
      })
    }
  }
});