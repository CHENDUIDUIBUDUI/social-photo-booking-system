// utils/request.js
// 统一 wx.request 封装：自动拼接 baseUrl / 注入 token / 统一错误提示
//
// 用法：
//   const request = require('../../utils/request.js');
//   1) 访问小程序后端（默认）：
//      request('/api/photographer/list', { method: 'GET', data: {...} })
//   2) 访问管理后台后端（端口8086，如摄影师入驻申请）：
//      request('/admin/api/photographer/application/submit', { useAdmin: true, method: 'POST', data: {...} })

const config = require('./config.js');

function request(url, options = {}) {
  const {
    method = 'GET',
    data = {},
    header = {},
    useAdmin = false,   // true 则走 8086 管理后台
    showLoading = false,
    loadingText = '加载中...',
    noAuth = false      // 部分接口无需 token（登录/注册）
  } = options;

  if (showLoading) {
    wx.showLoading({ title: loadingText, mask: true });
  }

  const base = useAdmin ? config.adminUrl : config.baseUrl;
  const fullUrl = /^https?:\/\//.test(url) ? url : base + url;

  // 注入 token + ngrok 免费版防拦截 header
  const finalHeader = Object.assign({ 'Content-Type': 'application/json' }, header);
  // ngrok 免费版会拦截缺少此 header 的请求并返回警告页 HTML，导致业务拿不到 JSON
  finalHeader['ngrok-skip-browser-warning'] = '1';
  if (!noAuth) {
    try {
      const token = wx.getStorageSync('token') || (getApp() && getApp().globalData.token);
      if (token) finalHeader.Authorization = 'Bearer ' + token;
    } catch (e) {}
  }

  return new Promise((resolve, reject) => {
    wx.request({
      url: fullUrl,
      method,
      data,
      header: finalHeader,
      timeout: 15000,
      success(res) {
        if (showLoading) wx.hideLoading();
        // HTTP 层 2xx
        if (res.statusCode >= 200 && res.statusCode < 300) {
          resolve(res.data);
        } else if (res.statusCode === 401) {
          wx.removeStorageSync('token');
          wx.removeStorageSync('userInfo');
          if (getApp()) {
            getApp().globalData.token = null;
            getApp().globalData.userInfo = null;
          }
          wx.showToast({ title: '请先登录', icon: 'none' });
          reject(res);
        } else {
          wx.showToast({
            title: (res.data && res.data.message) || ('请求失败 ' + res.statusCode),
            icon: 'none'
          });
          reject(res);
        }
      },
      fail(err) {
        if (showLoading) wx.hideLoading();
        console.error('[request fail]', fullUrl, err);
        let msg = '网络错误';
        if (err && err.errMsg) {
          if (err.errMsg.includes('timeout')) msg = '请求超时，请稍后重试';
          else if (err.errMsg.includes('fail')) {
            // 把常见原因提示给用户，方便排查
            msg = '无法连接服务器，请检查：\n1) 后端是否启动\n2) ENV 选择是否正确\n3) 是否已配置合法域名';
          }
        }
        wx.showToast({ title: msg, icon: 'none', duration: 2500 });
        reject(err);
      }
    });
  });
}

// 便捷方法
['GET', 'POST', 'PUT', 'DELETE'].forEach(m => {
  request[m.toLowerCase()] = function (url, data, extra = {}) {
    return request(url, Object.assign({ method: m, data }, extra));
  };
});

module.exports = request;
