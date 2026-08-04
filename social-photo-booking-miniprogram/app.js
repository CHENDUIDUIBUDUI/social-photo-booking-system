// app.js
const envConfig = require('./utils/config.js');

App({
  globalData: {
    userInfo: null,
    token: null,
    // 统一从 utils/config.js 读取，改 ENV 即可一键切换环境
    baseUrl: envConfig.baseUrl,
    adminUrl: envConfig.adminUrl,
    env: envConfig.ENV
  },

  onLaunch() {
    // 控制台打印当前环境，开发调试时一眼知道连的是哪
    console.log(`[app] 当前环境：${envConfig.ENV}  baseUrl=${envConfig.baseUrl}  adminUrl=${envConfig.adminUrl}`);
    this.checkLoginStatus();
  },

  checkLoginStatus() {
    const token = wx.getStorageSync('token');
    const userInfo = wx.getStorageSync('userInfo');

    if (token && userInfo) {
      this.globalData.token = token;
      this.globalData.userInfo = userInfo;
      this.loginWithToken(token);
    }
  },

  loginWithToken(token) {
    const request = require('./utils/request.js');
    request('/api/auth/login/token', {
      method: 'POST',
      data: { token },
      noAuth: true
    }).then(res => {
      console.log('使用 token 登录成功，响应:', res);
      if (res && res.data && res.data.user) {
        this.globalData.userInfo = res.data.user;
        wx.setStorageSync('userInfo', res.data.user);
      }
    }).catch(() => {
      wx.removeStorageSync('token');
      wx.removeStorageSync('userInfo');
      this.globalData.token = null;
      this.globalData.userInfo = null;
    });
  },

  login(openid) {
    const request = require('./utils/request.js');
    return request('/api/auth/login', {
      method: 'POST',
      data: { openid },
      noAuth: true
    }).then(res => {
      console.log('登录成功，响应:', res);
      if (res && res.data && res.data.user) {
        this.globalData.userInfo = res.data.user;
        wx.setStorageSync('userInfo', res.data.user);
        if (res.data.token) {
          this.globalData.token = res.data.token;
          wx.setStorageSync('token', res.data.token);
        }
      }
      return res;
    });
  },

  register(userInfo) {
    const request = require('./utils/request.js');
    return request('/api/auth/login', {
      method: 'POST',
      data: { code: 'test', userInfo },
      noAuth: true
    }).then(res => {
      console.log('注册成功，响应:', res);
      if (res && res.data && res.data.user) {
        this.globalData.userInfo = res.data.user;
        wx.setStorageSync('userInfo', res.data.user);
        if (res.data.token) {
          this.globalData.token = res.data.token;
          wx.setStorageSync('token', res.data.token);
        }
        return res.data.user;
      }
      return Promise.reject('注册失败');
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
