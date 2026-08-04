// 发现页- 摄影师列表逻辑
const app = getApp();

Page({
  data: {
    // 摄影师列表数据
    photographers: [],
    
    // 原始摄影师数据（用于筛选）
    originalPhotographers: [],
    
    // 筛选面板状态
    showFilter: false,
    
    // 筛选选项
    styleOptions: ['人像', '风景', '街拍', '纪实', '商业', '婚礼', '儿童', '时尚', '产品', '证件照'],
    regionOptions: ['全部', '北京', '上海', '广州', '深圳', '杭州', '成都', '武汉', '西安', '南京', '重庆', '天津', '苏州', '郑州', '长沙', '青岛', '宁波', '东莞', '无锡', '厦门', '福州', '济南', '大连', '昆明', '合肥', '佛山', '哈尔滨', '南昌', '贵阳', '南宁', '太原', '石家庄', '乌鲁木齐', '兰州', '西宁', '银川', '拉萨'],

    sortOptions: [
      { label: '推荐', value: 'recommend' },
      { label: '销量最高', value: 'sales' },
      { label: '好评率', value: 'rating' },
      { label: '最新入驻', value: 'newest' }
    ],
    
    // 价格区间
    priceRange: {
      min: 0,
      max: 2000
    },
    
    // 选中的筛选条件
    selectedFilters: {
      styles: [],
      region: '全部',
      sort: 'recommend'
    },
    
    // 加载状态
    loading: false,
    
    // 筛选栏激活状态
    activeFilter: '',
    
    // 搜索关键词
    searchKeyword: ''
  },
  
  onLoad() {
    this.loadPhotographers();
  },
  
  // 加载摄影师列表
  loadPhotographers() {
    this.setData({ loading: true });
    
    wx.request({
      url: app.globalData.baseUrl + '/api/photographer/list',
      method: 'POST',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token')
      },
      data: {},
      success: (res) => {
        if (res.data.code === 200) {
          const photographers = res.data.data.map(photographer => {
            let works = [];
            try {
              works = photographer.works ? JSON.parse(photographer.works).slice(0, 2) : [];
            } catch (e) {
              console.error('解析作品数据失败:', e);
              works = [];
            }
            return {
              id: photographer.id,
              name: photographer.name || '摄影师',
              avatar: photographer.avatar || 'https://api.dicebear.com/7.x/personas/svg?seed=avatar&size=200',
              certified: photographer.certified === 1,
              orders: photographer.orders || 0,
              rating: photographer.rating ? Math.round(photographer.rating) : 0,
              styles: photographer.styles ? photographer.styles.split(',') : [],
              works: works,
              followed: false,
              region: photographer.region || '',
              price: photographer.price || 0
            };
          });
          
          this.setData({
            photographers: photographers,
            originalPhotographers: photographers,
            loading: false
          });
        } else {
          wx.showToast({
            title: res.data.message || '加载失败',
            icon: 'none',
            duration: 2000
          });
          this.setData({ loading: false });
        }
      },
      fail: (err) => {
        console.error('加载摄影师列表失败', err);
        wx.showToast({
          title: '网络错误，请稍后重试',
          icon: 'none',
          duration: 2000
        });
        this.setData({ loading: false });
      }
    });
  },
  
  // 显示筛选面板
  showFilterPanel(e) {
    const filterType = e.currentTarget.dataset.type || '';
    this.setData({ 
      showFilter: true,
      activeFilter: filterType
    });
  },
  
  // 关闭筛选面板
  closeFilterPanel() {
    this.setData({ showFilter: false });
  },
  
  // 切换标签选择
  toggleTag(e) {
    const tag = e.currentTarget.dataset.tag;
    const styles = [...(this.data.selectedFilters.styles || [])];
    
    if (styles.indexOf(tag) !== -1) {
      // 移除标签
      const index = styles.indexOf(tag);
      styles.splice(index, 1);
    } else {
      // 添加标签
      styles.push(tag);
    }
    
    this.setData({
      'selectedFilters.styles': styles
    });
    
  },
  
  // 检查风格是否被选中
  isStyleSelected(style) {
    const styles = this.data.selectedFilters.styles || [];
    return styles.indexOf(style) !== -1;
  },
  
  // 价格变化
  priceChange(e) {
    this.setData({
      'priceRange.max': e.detail.value
    });
  },
  
  // 选择区域
  selectRegion(e) {
    const region = e.currentTarget.dataset.region;
    this.setData({
      'selectedFilters.region': region
    });
  },
  
  // 选择排序
  selectSort(e) {
    const sort = e.currentTarget.dataset.sort;
    this.setData({
      'selectedFilters.sort': sort
    });
  },
  
  // 确认筛选
  confirmFilter() {
    this.setData({ showFilter: false, loading: true });
    
    // 调用后端API进行筛选
    this.loadPhotographersWithFilters();
  },
  
  // 带筛选条件加载摄影师列表
  loadPhotographersWithFilters() {
    const { styles, region, sort } = this.data.selectedFilters;
    const { max: maxPrice } = this.data.priceRange;
    
    wx.request({
      url: app.globalData.baseUrl + '/api/photographer/list',
      method: 'POST',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token')
      },
      data: {
        region: region === '全部' ? '' : region,
        style: styles.length > 0 ? styles.join(',') : '',
        maxPrice: maxPrice
      },
      success: (res) => {
        if (res.data.code === 200) {
          const photographers = res.data.data.map(photographer => {
            let works = [];
            try {
              works = photographer.works ? JSON.parse(photographer.works).slice(0, 2) : [];
            } catch (e) {
              console.error('解析作品数据失败:', e);
              works = [];
            }
            return {
              id: photographer.id,
              name: photographer.name || '摄影师',
              avatar: photographer.avatar || 'https://api.dicebear.com/7.x/personas/svg?seed=avatar&size=200',
              certified: photographer.certified === 1,
              orders: photographer.orders || 0,
              rating: photographer.rating ? Math.round(photographer.rating) : 0,
              styles: photographer.styles ? photographer.styles.split(',') : [],
              works: works,
              followed: false,
              region: photographer.region || '',
              price: photographer.price || 0
            };
          });
          
          // 按排序方式排序
          let sortedPhotographers = [...photographers];
          switch (sort) {
            case 'sales':
              sortedPhotographers.sort((a, b) => b.orders - a.orders);
              break;
            case 'rating':
              sortedPhotographers.sort((a, b) => b.rating - a.rating);
              break;
            case 'newest':
              sortedPhotographers.sort((a, b) => b.id - a.id);
              break;
            default:
              // 推荐排序（综合考虑订单量和好评率）
              sortedPhotographers.sort((a, b) => {
                const scoreA = a.orders * 0.7 + a.rating * 0.3;
                const scoreB = b.orders * 0.7 + b.rating * 0.3;
                return scoreB - scoreA;
              });
          }
          
          this.setData({
            photographers: sortedPhotographers,
            loading: false
          });
          
          // 显示筛选结果
          wx.showToast({
            title: '筛选成功',
            icon: 'success',
            duration: 1000
          });
        } else {
          wx.showToast({
            title: res.data.message || '加载失败',
            icon: 'none',
            duration: 2000
          });
          this.setData({ loading: false });
        }
      },
      fail: (err) => {
        console.error('加载摄影师列表失败', err);
        wx.showToast({
          title: '网络错误，请稍后重试',
          icon: 'none',
          duration: 2000
        });
        this.setData({ loading: false });
      }
    });
  },
  
  // 关注/取消关注
  toggleFollow(e) {
    const id = e.currentTarget.dataset.id;
    const photographers = [...this.data.photographers];
    
    photographers.forEach((photographer) => {
      if (photographer.id === id) {
        photographer.followed = !photographer.followed;
      }
    });
    
    this.setData({ photographers });
    
    // 这里可以调用API更新关注状态
    wx.showToast({
      title: photographers.find(p => p.id === id).followed ? '关注成功' : '取消关注',
      icon: 'success',
      duration: 1000
    });
  },
  
  // 图片加载失败处理
  handleImageError(e) {
    const index = e.currentTarget.dataset.index;
    const workIndex = e.currentTarget.dataset.workIndex;
    const photographers = [...this.data.photographers];
    
    if (workIndex !== undefined) {
      // 作品图片加载失败
      photographers[index].works[workIndex] = 'https://api.dicebear.com/7.x/personas/svg?seed=placeholder&size=300';
    } else {
      // 头像加载失败
      photographers[index].avatar = 'https://api.dicebear.com/7.x/personas/svg?seed=avatar&size=200';
    }
    
    this.setData({ photographers });
  },
  
  // 图片加载完成处理
  handleImageLoad(e) {
    
  },
  
  // 下拉刷新
  onPullDownRefresh() {
    this.loadPhotographers();
    setTimeout(() => {
      wx.stopPullDownRefresh();
    }, 1000);
  },
  
  // 触底加载
  onReachBottom() {
    // 这里可以实现分页加载
    
  },
  
  // 跳转到摄影师详情页
  goToPhotographerDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/photographer/detail?id=${id}`,
      success: function(res) {
        
      },
      fail: function(res) {
        console.error('跳转到摄影师详情页失败', res);
        wx.showToast({
          title: '跳转失败，请稍后重试',
          icon: 'none',
          duration: 1500
        });
      }
    });
  },

  // 搜索输入
  onSearchInput(e) {
    this.setData({
      searchKeyword: e.detail.value
    });
  },

  // 确认搜索
  onSearchConfirm() {
    const keyword = this.data.searchKeyword.trim();
    if (!keyword) {
      // 如果搜索词为空，显示全部
      this.setData({
        photographers: this.data.originalPhotographers
      });
      return;
    }
    
    this.performSearch(keyword);
  },

  // 执行搜索
  performSearch(keyword) {
    this.setData({ loading: true });
    
    const lowerKeyword = keyword.toLowerCase();
    
    // 在本地数据中搜索
    const filteredPhotographers = this.data.originalPhotographers.filter(photographer => {
      // 搜索摄影师名�?      const nameMatch = photographer.name && photographer.name.toLowerCase().includes(lowerKeyword);
      
      // 搜索风格
      const styleMatch = photographer.styles && photographer.styles.some(style => 
        style.toLowerCase().includes(lowerKeyword)
      );
      
      // 搜索地区
      const regionMatch = photographer.region && photographer.region.toLowerCase().includes(lowerKeyword);
      
      return nameMatch || styleMatch || regionMatch;
    });
    
    this.setData({
      photographers: filteredPhotographers,
      loading: false
    });
    
    // 显示搜索结果
    if (filteredPhotographers.length === 0) {
      wx.showToast({
        title: '未找到相关摄影师',
        icon: 'none',
        duration: 1500
      });
    } else {
      wx.showToast({
        title: `找到 ${filteredPhotographers.length} 位摄影师`,
        icon: 'success',
        duration: 1500
      });
    }
  },

  // 清空搜索
  clearSearch() {
    this.setData({
      searchKeyword: '',
      photographers: this.data.originalPhotographers
    });
  }
});

