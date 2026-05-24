// app.js
App({
  globalData: {
    userInfo: null,
    token: null,
    baseUrl: 'http://127.0.0.1:8081' // 后端服务运行在 8081 端口
  },
  
  onLaunch() {
    // 小程序启动时执行
    this.checkLoginStatus();
  },
  
  checkLoginStatus() {
    // 检查登录状态
    const token = wx.getStorageSync('token');
    const userInfo = wx.getStorageSync('userInfo');
    
    if (token && userInfo) {
      this.globalData.token = token;
      this.globalData.userInfo = userInfo;
      // 如果存在登录态标识，直接使用该标识进行登录
      this.loginWithToken(token);
    }
  },
  
  loginWithToken(token) {
    // 使用 token 进行登录
    console.log('使用 token 进行登录，token:', token);
    console.log('请求地址:', `${this.globalData.baseUrl}/api/auth/login/token`);
    
    wx.request({
      url: `${this.globalData.baseUrl}/api/auth/login/token`,
      method: 'POST',
      data: { token },
      header: {
        'Content-Type': 'application/json'
      },
      timeout: 20000,
      success: (res) => {
        console.log('使用 token 登录成功，响应:', res);
        if (res.data && res.data.data && res.data.data.user) {
          this.globalData.userInfo = res.data.data.user;
          wx.setStorageSync('userInfo', res.data.data.user);
        }
      },
      fail: (err) => {
        console.error('使用 token 登录失败:', err);
        if (err.errMsg.includes('request:fail')) {
          if (err.errMsg.includes('timeout')) {
            console.error('网络请求超时，请检查网络连接');
            wx.showToast({ title: '网络请求超时，请检查网络连接', icon: 'none' });
          } else {
            console.error('网络请求失败，请检查网络连接或在开发者工具中开启不校验域名选项');
            wx.showToast({ title: '网络请求失败，请检查网络连接或在开发者工具中开启不校验域名选项', icon: 'none' });
          }
        } else {
          console.error('网络错误，请重试');
          wx.showToast({ title: '网络错误，请重试', icon: 'none' });
        }
        // 如果使用 token 登录失败，清除本地存储的登录态标识
        wx.removeStorageSync('token');
        wx.removeStorageSync('userInfo');
        this.globalData.token = null;
        this.globalData.userInfo = null;
      }
    });
  },
  
  login(openid) {
    // 登录逻辑
    console.log('开始登录，请求地址:', `${this.globalData.baseUrl}/api/auth/login`);
    console.log('请求参数:', { openid });
    
    wx.request({
      url: `${this.globalData.baseUrl}/api/auth/login`,
      method: 'POST',
      data: { openid },
      timeout: 20000,
      success: (res) => {
        console.log('登录成功，响应:', res);
        if (res.data && res.data.data && res.data.data.user) {
          this.globalData.userInfo = res.data.data.user;
          wx.setStorageSync('userInfo', res.data.data.user);
        }
      },
      fail: (err) => {
        console.error('登录失败:', err);
        if (err.errMsg.includes('request:fail')) {
          if (err.errMsg.includes('timeout')) {
            console.error('网络请求超时，请检查网络连接');
            wx.showToast({ title: '网络请求超时，请检查网络连接', icon: 'none' });
          } else {
            console.error('网络请求失败，请检查网络连接或在开发者工具中开启不校验域名选项');
            wx.showToast({ title: '网络请求失败，请检查网络连接或在开发者工具中开启不校验域名选项', icon: 'none' });
          }
        } else {
          console.error('网络错误，请重试');
          wx.showToast({ title: '网络错误，请重试', icon: 'none' });
        }
      }
    });
  },
  
  register(userInfo) {
    // 注册逻辑
    return new Promise((resolve, reject) => {
      console.log('开始注册，请求地址:', `${this.globalData.baseUrl}/api/auth/login`);
      console.log('请求参数:', { code: 'test', userInfo });
      
      wx.request({
        url: `${this.globalData.baseUrl}/api/auth/login`,
        method: 'POST',
        data: { code: 'test', userInfo },
        timeout: 20000,
        success: (res) => {
          console.log('注册成功，响应:', res);
          if (res.data && res.data.data && res.data.data.user) {
            this.globalData.userInfo = res.data.data.user;
            wx.setStorageSync('userInfo', res.data.data.user);
            resolve(res.data.data.user);
          } else {
            reject('注册失败');
          }
        },
        fail: (err) => {
          console.error('注册失败:', err);
          if (err.errMsg.includes('request:fail')) {
            if (err.errMsg.includes('timeout')) {
              console.error('网络请求超时，请检查网络连接');
              wx.showToast({ title: '网络请求超时，请检查网络连接', icon: 'none' });
            } else {
              console.error('网络请求失败，请检查网络连接或在开发者工具中开启不校验域名选项');
              wx.showToast({ title: '网络请求失败，请检查网络连接或在开发者工具中开启不校验域名选项', icon: 'none' });
            }
          } else {
            console.error('网络错误，请重试');
            wx.showToast({ title: '网络错误，请重试', icon: 'none' });
          }
          reject(err);
        }
      });
    });
  },
  
  getToken() {
    return this.globalData.token;
  },
  
  setToken(token) {
    this.globalData.token = token;
    wx.setStorageSync('token', token);
  },
  
  getUserInfo() {
    return this.globalData.userInfo;
  },
  
  setUserInfo(userInfo) {
    this.globalData.userInfo = userInfo;
    wx.setStorageSync('userInfo', userInfo);
  }
});
