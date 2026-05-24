// 搜索页面
Page({
  data: {
    keyword: '',
    history: [],
    hotKeywords: ['人像摄影', '风景摄影', '婚纱摄影', '儿童摄影', '商业摄影'],
    isSearching: false,
    hasResults: false,
    photographers: [],
    works: []
  },
  
  onLoad: function(options) {
    // 加载历史搜索记录
    this.loadHistory();
  },
  
  onKeywordInput: function(e) {
    this.setData({
      keyword: e.detail.value
    });
  },
  
  clearKeyword: function() {
    this.setData({
      keyword: ''
    });
  },
  
  search: function() {
    const keyword = this.data.keyword.trim();
    if (!keyword) return;
    
    // 保存到历史记录
    this.saveToHistory(keyword);
    
    // 显示搜索中状态
    this.setData({
      isSearching: true,
      hasResults: false
    });
    
    // 调用真实API搜索摄影师和作品
    const promises = [
      this.searchPhotographers(keyword),
      this.searchWorks(keyword)
    ];
    
    Promise.all(promises).then(([photographers, works]) => {
      this.setData({
        isSearching: false,
        hasResults: true,
        photographers: photographers,
        works: works
      });
    }).catch((err) => {
      console.error('搜索失败:', err);
      this.setData({
        isSearching: false,
        hasResults: true,
        photographers: [],
        works: []
      });
    });
  },
  
  // 搜索摄影师
  searchPhotographers: function(keyword) {
    return new Promise((resolve) => {
      wx.request({
        url: `${getApp().globalData.baseUrl}/api/photographer/search`,
        method: 'GET',
        data: {
          keyword: keyword,
          page: 1,
          pageSize: 10
        },
        timeout: 10000,
        success: (res) => {
          if (res.data && res.data.code === 200 && res.data.data) {
            const photographers = res.data.data.map(item => ({
              id: item.id,
              name: item.name,
              avatar: item.avatar || 'https://api.dicebear.com/7.x/personas/svg?seed=photographer',
              specialty: item.specialty || ''
            }));
            resolve(photographers);
          } else {
            resolve([]);
          }
        },
        fail: () => {
          resolve([]);
        }
      });
    });
  },
  
  // 搜索作品
  searchWorks: function(keyword) {
    return new Promise((resolve) => {
      wx.request({
        url: `${getApp().globalData.baseUrl}/api/content/search`,
        method: 'GET',
        data: {
          keyword: keyword,
          page: 1,
          pageSize: 10,
          status: 1
        },
        timeout: 10000,
        success: (res) => {
          if (res.data && res.data.code === 200 && res.data.data) {
            const works = res.data.data.map(item => ({
              id: item.id,
              title: item.title || '无标题',
              imageUrl: item.coverImage || `https://api.dicebear.com/7.x/personas/svg?seed=work${item.id}`
            }));
            resolve(works);
          } else {
            resolve([]);
          }
        },
        fail: () => {
          resolve([]);
        }
      });
    });
  },
  
  loadHistory: function() {
    const history = wx.getStorageSync('searchHistory') || [];
    this.setData({ history });
  },
  
  saveToHistory: function(keyword) {
    let history = wx.getStorageSync('searchHistory') || [];
    // 去重并限制历史记录数�?    history = history.filter(item => item !== keyword);
    history.unshift(keyword);
    if (history.length > 10) {
      history = history.slice(0, 10);
    }
    wx.setStorageSync('searchHistory', history);
    this.setData({ history });
  },
  
  clearHistory: function() {
    wx.removeStorageSync('searchHistory');
    this.setData({ history: [] });
  },
  
  selectHistory: function(e) {
    const keyword = e.currentTarget.dataset.keyword;
    this.setData({ keyword });
    this.search();
  },
  
  selectHotKeyword: function(e) {
    const keyword = e.currentTarget.dataset.keyword;
    this.setData({ keyword });
    this.search();
  },
  
  navigateToPhotographer: function(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/photographer/detail?id=${id}`
    });
  },
  
  navigateToWork: function(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/content/detail?id=${id}`
    });
  },
  
  navigateBack: function() {
    wx.navigateBack();
  }
});
