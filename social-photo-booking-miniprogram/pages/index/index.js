// index.js
Page({
  data: {
    // 分类数据
    categories: [],
    activeCategory: 'all',
    
    // 轮播图数据
    bannerList: [],
    
    // 热门摄影师数据
    photographers: [],
    
    // 推荐作品数据
    leftWorksList: [],
    rightWorksList: [],
    hasMore: true,
    page: 1,
    pageSize: 10,
    
    // 位置信息
    currentLocation: '上海',
    latitude: null,
    longitude: null,
    
    // 加载状�?    loading: false,
    refreshing: false
  },
  
  onLoad() {
    this.loadInitialData();
  },
  
  // 加载初始数据
  loadInitialData() {
    
    this.setData({ loading: true });
    
    // 并行加载数据
    const promises = [
      this.loadCategories(),
      this.loadBannerList(),
      this.loadHotPhotographers(),
      this.getLocation()
    ];
    
    Promise.all(promises).then(() => {
      // 所有数据加载完成后，先重置加载状态，再加载作品列表
      
      this.setData({ loading: false });
      this.loadWorksList();
    }).finally(() => {
      // 确保加载状态被重置
      
      this.setData({ loading: false });
    });
  },
  
  // 获取当前位置
  getLocation() {
    return new Promise((resolve) => {
      wx.getLocation({
        type: 'gcj02',
        success: (res) => {
          const { latitude, longitude } = res;
          this.setData({ latitude, longitude });
          this.getAddressFromLocation(latitude, longitude);
          resolve();
        },
        fail: (err) => {
          console.error('获取位置失败:', err);
          wx.showToast({
            title: '获取位置失败，请检查定位权限',
            icon: 'none'
          });
          resolve();
        }
      });
    });
  },
  
  // 根据经纬度获取地址
  getAddressFromLocation(latitude, longitude) {
    // 使用腾讯地图SDK或百度地图SDK进行逆地理编码
    // 这里使用模拟数据，实际项目中需要调用地图API
    wx.request({
      url: 'https://apis.map.qq.com/ws/geocoder/v1/',
      data: {
        location: `${latitude},${longitude}`,
        key: 'YOUR_TENCENT_MAP_KEY', // 需要替换为实际的腾讯地图key
        get_poi: 0
      },
      success: (res) => {
        if (res.data && res.data.status === 0) {
          const address = res.data.result.address_component;
          const city = address.city || '上海';
          this.setData({ currentLocation: city });
        }
      },
      fail: () => {
        // 如果API调用失败，使用默认位置
        
      }
    });
  },
  
  // 选择位置
  chooseLocation() {
    wx.chooseLocation({
      success: (res) => {
        const { name, address, latitude, longitude } = res;
        this.setData({
          currentLocation: name || address || '上海',
          latitude,
          longitude
        });
      },
      fail: (err) => {
        // 用户取消选择，不显示错误
        if (err.errMsg && err.errMsg.includes('cancel')) {
          
          return;
        }
        // 其他错误才显示提示
        console.error('选择位置失败:', err);
        wx.showToast({
          title: '选择位置失败',
          icon: 'none'
        });
      }
    });
  },
  
  // 加载分类数据
  loadCategories() {
    return new Promise((resolve) => {
      wx.request({
        url: `${getApp().globalData.baseUrl}/api/category/list`,
        method: 'GET',
        timeout: 10000,
        success: (res) => {
          
          if (res.data && res.data.code === 200 && res.data.data) {
            // 将后端返回的标签数据转换为前端需要的格式
            // 后端返回的是 [{id: 1, name: '人像', type: 0, status: 1}, ...]
            const tagList = res.data.data;
            const categories = [{ id: 'all', name: '全部' }];
            
            // 只添加风格标签（type=0）作为首页分类
            tagList.forEach(tag => {
              if (tag.type === 0) {
                categories.push({
                  id: tag.id.toString(),
                  name: tag.name
                });
              }
            });
            
            this.setData({ 
              categories,
              activeCategoryName: '全部'
            });
          } else {
            // 如果API调用失败，使用默认分类（与数据库标签一致）
            this.setData({
              categories: [
                { id: 'all', name: '全部' },
                { id: '9', name: '日系' },
                { id: '10', name: '韩系' },
                { id: '5', name: '时尚' },
                { id: '1', name: '人像' },
                { id: '2', name: '风景' },
                { id: '3', name: '婚礼' },
                { id: '4', name: '商业' },
                { id: '6', name: '写真' },
                { id: '7', name: '街拍' },
                { id: '8', name: '产品' },
                { id: '11', name: '欧美' },
                { id: '12', name: '古风' }
              ],
              activeCategoryName: '全部'
            });
          }
          resolve();
        },
        fail: (err) => {
          console.error('加载分类数据失败:', err);
          // 使用默认分类（与数据库标签一致）
          this.setData({
            categories: [
              { id: 'all', name: '全部' },
              { id: '9', name: '日系' },
              { id: '10', name: '韩系' },
              { id: '5', name: '时尚' },
              { id: '1', name: '人像' },
              { id: '2', name: '风景' },
              { id: '3', name: '婚礼' },
              { id: '4', name: '商业' },
              { id: '6', name: '写真' },
              { id: '7', name: '街拍' },
              { id: '8', name: '产品' },
              { id: '11', name: '欧美' },
              { id: '12', name: '古风' }
            ]
          });
          resolve();
        }
      });
    });
  },
  
  // 加载轮播图数据
  loadBannerList() {
    return new Promise((resolve) => {
      wx.request({
        url: `${getApp().globalData.baseUrl}/api/banner/list`,
        method: 'GET',
        timeout: 10000,
        success: (res) => {
          
          if (res.data && res.data.code === 200 && res.data.data) {
            this.setData({ bannerList: res.data.data });
          } else {
            // 如果API调用失败，使用默认轮播图
            this.setData({
              bannerList: [
                { id: 1, imageUrl: 'https://picsum.photos/750/300?random=1' },
                { id: 2, imageUrl: 'https://picsum.photos/750/300?random=2' },
                { id: 3, imageUrl: 'https://picsum.photos/750/300?random=3' }
              ]
            });
          }
          resolve();
        },
        fail: (err) => {
          console.error('加载轮播图数据失败', err);
          // 使用默认轮播图
          this.setData({
              bannerList: [
                { id: 1, imageUrl: 'https://picsum.photos/750/300?random=4' },
                { id: 2, imageUrl: 'https://picsum.photos/750/300?random=5' },
                { id: 3, imageUrl: 'https://picsum.photos/750/300?random=6' }
              ]
          });
          resolve();
        }
      });
    });
  },
  
  // 加载热门摄影师数据
  loadHotPhotographers() {
    return new Promise((resolve) => {
      wx.request({
        url: `${getApp().globalData.baseUrl}/api/photographer/hot`,
        method: 'GET',
        data: {
          limit: 5
        },
        timeout: 10000,
        success: (res) => {
          
          if (res.data && res.data.code === 200 && res.data.data) {
            // 处理后端返回的数据
            const processedPhotographers = res.data.data.map(item => ({
              id: item.id,
              name: item.name,
              avatar: item.avatar
            }));
            this.setData({ photographers: processedPhotographers });
          } else {
            // 如果API调用失败，使用默认数据
            this.setData({
              photographers: [
                { id: 1, name: '摄影师A', avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=photographer1' },
                { id: 2, name: '摄影师B', avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=photographer2' },
                { id: 3, name: '摄影师C', avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=photographer3' },
                { id: 4, name: '摄影师D', avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=photographer4' },
                { id: 5, name: '摄影师E', avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=photographer5' }
              ]
            });
          }
          resolve();
        },
        fail: (err) => {
          console.error('加载热门摄影师数据失败', err);
          // 使用默认数据
          this.setData({
            photographers: [
              { id: 1, name: '摄影师A', avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=photographer6' },
              { id: 2, name: '摄影师B', avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=photographer7' },
              { id: 3, name: '摄影师C', avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=photographer8' },
              { id: 4, name: '摄影师D', avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=photographer9' },
              { id: 5, name: '摄影师E', avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=photographer10' }
            ]
          });
          resolve();
        }
      });
    });
  },
  

  
  // 切换分类
  switchCategory(e) {
    const categoryId = e.currentTarget.dataset.id;
    const categoryName = e.currentTarget.dataset.name;
    this.setData({
      activeCategory: categoryId,
      activeCategoryName: categoryName,
      leftWorksList: [],
      rightWorksList: [],
      page: 1,
      hasMore: true
    });
    this.loadWorksList();
  },
  
  // 加载作品列表
  loadWorksList() {
    if (this.data.loading || !this.data.hasMore) return;
    
    this.setData({ loading: true });
    
    const { page, pageSize, activeCategory, activeCategoryName } = this.data;
    const token = wx.getStorageSync('token');
    
    // 确定要传递的标签参数
    let tags = null;
    if (activeCategory !== 'all' && activeCategoryName) {
      tags = activeCategoryName;
    }
    
    
    console.log('开始加载作品列表，请求地址:', `${getApp().globalData.baseUrl}/api/content/list`);
    
    
    // 构建请求参数，当tags为null时不传递该参数
    const requestData = {
      page,
      pageSize,
      status: 1
    };
    if (tags !== null && tags !== undefined) {
      requestData.tags = tags;
    }
    
    wx.request({
      url: `${getApp().globalData.baseUrl}/api/content/list`,
      method: 'POST',
      header: token ? { 'Authorization': 'Bearer ' + token } : {},
      data: requestData,
      timeout: 10000,
      success: (res) => {
        
        if (res.data && res.data.code === 200 && res.data.data) {
          const data = res.data.data;
          const worksList = data.list || data || [];
          
          
          
          if (worksList.length === 0) {
            
            this.setData({ 
              hasMore: false,
              loading: false
            });
            return;
          }
          
          // 创建新的数组，避免引用问题
          const leftWorksList = [];
          const rightWorksList = [];
          
          // 将作品分配到左右两列
          worksList.forEach((item, index) => {
            
            // 处理后端返回的数据，使用coverImage字段作为图片URL
            // 使用item.id来生成唯一的随机图片
            item.imageUrl = item.coverImage || `https://api.dicebear.com/7.x/personas/svg?seed=work${item.id}`;
            
            // 模拟数据，实际项目中应该从API获取
            item.authorAvatar = item.authorAvatar || `https://api.dicebear.com/7.x/personas/svg?seed=author${item.id}`;
            item.likes = item.likes || (item.likeCount || Math.floor(Math.random() * 100));
            item.author = item.name || '摄影师';
            
            if (index % 2 === 0) {
              leftWorksList.push(item);
            } else {
              rightWorksList.push(item);
            }
          });
          
          
          
          
          // 判断是否有更多数据：如果本次返回的数据量小于pageSize，说明已经没有更多数据了
          const hasMore = worksList.length === pageSize;
          
          this.setData({
            leftWorksList: [...this.data.leftWorksList, ...leftWorksList],
            rightWorksList: [...this.data.rightWorksList, ...rightWorksList],
            page: page + 1,
            hasMore: hasMore,
            loading: false
          }, () => {
            
          });
        } else {
          
          this.setData({ 
            hasMore: false,
            loading: false
          });
        }
      },
      fail: (err) => {
        console.error('加载作品失败:', err);
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
        // 确保加载状态被重置
        this.setData({ loading: false });
      },
      complete: () => {
        this.setData({ loading: false });
      }
    });
  },
  
  // 图片加载失败处理
  onImageError(e) {
    const id = e.currentTarget.dataset.id;
    
    // 找到对应的作品并更新图片URL
    const { leftWorksList, rightWorksList } = this.data;
    let found = false;
    
    // 在左侧列表中查找
    for (let i = 0; i < leftWorksList.length; i++) {
      if (leftWorksList[i].id === id) {
        leftWorksList[i].imageUrl = '/assets/images/default-avatar.png';
        found = true;
        break;
      }
    }
    
    // 在右侧列表中查找
    if (!found) {
      for (let i = 0; i < rightWorksList.length; i++) {
        if (rightWorksList[i].id === id) {
          rightWorksList[i].imageUrl = '/assets/images/default-avatar.png';
          break;
        }
      }
    }
    
    this.setData({ leftWorksList, rightWorksList });
  },

  // 跳转到作品详情
  navigateToDetail: function(e) {
    const id = e.currentTarget.dataset.id;
    // 显示加载动画，减少视觉闪烁
    wx.showLoading({
      title: '加载中...',
      mask: true
    });
    setTimeout(function() {
      wx.navigateTo({
        url: `/pages/content/detail?id=${id}`,
        success: function() {
          wx.hideLoading();
        },
        fail: function() {
          wx.hideLoading();
        }
      });
    }, 100);
  },
  
  // 跳转到摄影师详情
  navigateToPhotographer: function(e) {
    const id = e.currentTarget.dataset.id;
    // 显示加载动画，减少视觉闪烁
    wx.showLoading({
      title: '加载中...',
      mask: true
    });
    setTimeout(function() {
      wx.navigateTo({
        url: `/pages/photographer/detail?id=${id}`,
        success: function() {
          wx.hideLoading();
        },
        fail: function() {
          wx.hideLoading();
        }
      });
    }, 100);
  },
  

  
  // 跳转到搜索页面
  navigateToSearch: function() {
    // 显示加载动画，减少视觉闪烁
    wx.showLoading({
      title: '加载中...',
      mask: true
    });
    setTimeout(function() {
      wx.navigateTo({
        url: '/pages/search/index',
        success: function() {
          wx.hideLoading();
        },
        fail: function() {
          wx.hideLoading();
        }
      });
    }, 100);
  },
  
  // 跳转到消息页面
  navigateToMessage: function() {
    // 显示加载动画，减少视觉闪烁
    wx.showLoading({
      title: '加载中...',
      mask: true
    });
    setTimeout(() => {
      wx.switchTab({
        url: '/pages/message/list',
        success: function() {
          wx.hideLoading();
        },
        fail: function() {
          wx.hideLoading();
        }
      });
    }, 100);
  },
  
  // 加载更多
  onReachBottom() {
    if (this.data.hasMore) {
      this.loadWorksList();
    }
  },
  
  // 下拉刷新
  onPullDownRefresh() {
    this.setData({
      leftWorksList: [],
      rightWorksList: [],
      page: 1,
      hasMore: true
    });
    this.loadWorksList();
    wx.stopPullDownRefresh();
  }
});

