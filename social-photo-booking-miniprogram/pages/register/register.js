// 注册页面逻辑
Page({
  /**
   * 页面的初始数据
   */
  data: {
    phoneNumber: '',
    password: '',
    confirmPassword: '',
    nickname: '',
    selectedTags: [],
    loading: false,
    errorMessage: '',
    showPassword: false,
    showConfirmPassword: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      showPassword: false,
      showConfirmPassword: false
    })
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
    const value = e.detail.value
    this.setData({
      password: value,
      showPassword: false
    })
  },

  /**
   * 绑定确认密码输入
   */
  bindConfirmPasswordInput: function (e) {
    const value = e.detail.value
    this.setData({
      confirmPassword: value,
      showConfirmPassword: false
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
   * 切换确认密码可见性
   */
  toggleConfirmPasswordVisibility: function () {
    this.setData({
      showConfirmPassword: !this.data.showConfirmPassword
    })
  },

  /**
   * 绑定昵称输入
   */
  bindNicknameInput: function (e) {
    this.setData({
      nickname: e.detail.value
    })
  },



  /**
   * 切换兴趣标签
   */
  toggleTag: function (e) {
    const tag = e.currentTarget.dataset.tag
    let selectedTags = this.data.selectedTags
    
    if (selectedTags.includes(tag)) {
      selectedTags = selectedTags.filter(t => t !== tag)
    } else {
      selectedTags.push(tag)
    }
    
    this.setData({ selectedTags })
  },

  /**
   * 注册
   */
  register: function () {
    const phoneNumber = this.data.phoneNumber
    const password = this.data.password
    const nickname = this.data.nickname
    const selectedTags = this.data.selectedTags

    // 验证表单
    if (!phoneNumber) {
      this.setData({ errorMessage: '请输入手机号' })
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
    if (!nickname) {
      this.setData({ errorMessage: '请输入昵称' })
      return
    }
    if (nickname.length < 2) {
      this.setData({ errorMessage: '昵称长度至少2位' })
      return
    }

    this.setData({
      loading: true,
      errorMessage: ''
    })

    // 调用后端注册接口
    this.requestRegister(phoneNumber, password, nickname, selectedTags)
  },

  /**
   * 调用后端注册接口
   */
  requestRegister: function (phone, password, nickname, tags) {
    console.log('开始调用注册接口，phone:', phone, 'password:', password, 'nickname:', nickname, 'tags:', tags)
    console.log('请求地址:', 'http://127.0.0.1:8081/api/auth/register')
    
    // 直接硬编码请求地址，确保使用正确的URL
    const registerUrl = 'http://127.0.0.1:8081/api/auth/register';
    
    
    wx.request({
      url: registerUrl,
      method: 'POST',
      data: {
        phone: phone,
        password: password,
        nickname: nickname,
        tags: tags
      },
      timeout: 30000, // 增加超时时间
      success: (res) => {
        console.log('注册接口响应:', res)
        if (res.data.code === 200) {
          // 注册成功，跳转到登录页面
          wx.showToast({
            title: '注册成功，请登录',
            icon: 'success'
          })
          setTimeout(() => {
            wx.redirectTo({
              url: '/pages/login/index'
            })
          }, 1500)
        } else {
          this.setData({
            loading: false,
            errorMessage: res.data.message || '注册失败，请重试'
          })
        }
      },
      fail: (err) => {
        console.log('请求注册失败:', err)
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
  }
})

