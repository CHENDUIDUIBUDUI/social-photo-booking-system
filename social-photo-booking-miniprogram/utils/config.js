// utils/config.js
// 小程序后端服务地址配置 —— 支持一键切换环境
// 使用方式：修改下方 ENV 常量即可切换 dev / lan / prod

// ====================  在此选择环境  ====================
// dev: 仅微信开发者工具模拟器可用（后端跑在本机 127.0.0.1）
// lan: 真机同 WiFi 调试（需填写你电脑当前的局域网 IP）
// prod: 外网/异地可访问（填写你已备案、已配置 HTTPS 的域名）
const ENV = 'prod';
// ========================================================

const ENV_CONFIG = {
  dev: {
    // 开发者工具模拟器：后端跑在本机
    baseUrl: 'http://127.0.0.1:8081',
    adminUrl: 'http://127.0.0.1:8086'
  },
  lan: {
    // 真机同 WiFi：换成你电脑的局域网 IP（cmd 里 ipconfig 查看 IPv4）
    baseUrl: 'http://192.168.1.16:8081',
    adminUrl: 'http://192.168.1.16:8086'
  },
  prod: {
    // 生产/外网：必须用 已备案域名 + HTTPS，
    // 并在「微信公众平台 → 开发 → 开发管理 → 开发设置 → 服务器域名」
    // 中把该域名加到「request 合法域名」里。
    // 例：
    baseUrl: 'https://lethargic-annex-footpath.ngrok-free.dev',
    // 8086 管理后台需另开一个 ngrok 隧道，拿到地址后替换下面这行；
    // 若暂时不用摄影师入驻申请功能，可保持不变（仅那一个接口会失败）
    adminUrl: 'https://lethargic-annex-footpath.ngrok-free.dev'
  }
};

const current = ENV_CONFIG[ENV] || ENV_CONFIG.dev;

module.exports = {
  ENV,
  baseUrl: current.baseUrl,
  adminUrl: current.adminUrl,
  // 兼容 getApp().globalData.baseUrl 的写法
  getBaseUrl() {
    return current.baseUrl;
  },
  getAdminUrl() {
    return current.adminUrl;
  }
};
