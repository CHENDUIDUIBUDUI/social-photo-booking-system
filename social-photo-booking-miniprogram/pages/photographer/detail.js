// 摄影师详情页逻辑
const app = getApp();

Page({
  data: {
    loading: true,
    photographer: {
      id: 0,
      name: '',
      avatar: '',
      cover: '',
      certified: false,
      orders: 0,
      rating: 0,
      price: 0,
      region: '',
      styles: [],
      works: [],
      bio: '',
      followers: 0,
      collected: false
    },
    activeTab: 'works',
    leftWorks: [],
    rightWorks: [],
    packages: [],
    reviews: [
      {
        avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=photographer1&size=100',
        name: '用户A',
        time: '2024-01-15',
        rating: 5,
        content: '摄影师技术很好，拍摄效果超出预期！非常满意！'
      },
      {
        avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=photographer2&size=100',
        name: '用户B',
        time: '2024-01-10',
        rating: 4,
        content: '拍摄过程很愉快，摄影师很专业，推荐！'
      },
      {
        avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=photographer3&size=100',
        name: '用户C',
        time: '2024-01-05',
        rating: 5,
        content: '照片质量很高，后期处理也很棒，下次还会再来！'
      }
    ]
  },
  
  onLoad(options) {
    const id = options.id;
    const userId = options.userId;
    if (id) {
      this.loadPhotographerDetail(id, null);
    } else if (userId) {
      this.loadPhotographerDetail(null, userId);
    } else {
      wx.showToast({
        title: '参数错误',
        icon: 'none',
        duration: 1500
      });
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    }
  },
  
  // 加载摄影师详情
  loadPhotographerDetail: function(id, userId) {
    this.setData({ loading: true });
    
    // 构建请求参数
    const requestData = {};
    if (id) {
      requestData.id = id;
    } else if (userId) {
      requestData.userId = userId;
    }
    
    wx.request({
      url: app.globalData.baseUrl + '/api/photographer/info',
      method: 'GET',
      data: requestData,
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token')
      },
      success: (res) => {
        if (res.data.code === 200) {
          const photographerData = res.data.data;
          let works = [];
          try {
            if (photographerData.works && typeof photographerData.works === 'string') {
              const parsed = JSON.parse(photographerData.works);
              if (Array.isArray(parsed)) {
                works = parsed;
              }
            }
          } catch (e) {
            console.error('解析works数据失败:', e);
            works = [];
          }
          
          // 将作品分成左右两列
          const leftWorks = [];
          const rightWorks = [];
          if (Array.isArray(works)) {
            works.forEach((work, index) => {
              if (index % 2 === 0) {
                leftWorks.push(work);
              } else {
                rightWorks.push(work);
              }
            });
          }
          
          this.setData({
            photographer: {
              id: photographerData.id,
              name: photographerData.name || '摄影师',
              avatar: photographerData.avatar || 'https://api.dicebear.com/7.x/personas/svg?seed=avatar&size=200',
              cover: photographerData.coverImage || 'https://api.dicebear.com/7.x/personas/svg?seed=cover&size=750',
              certified: photographerData.certified === 1,
              orders: photographerData.orders || 0,
              rating: photographerData.rating ? Math.round(photographerData.rating) : 0,
              price: photographerData.price || 0,
              region: photographerData.region || '',
              styles: photographerData.styles ? photographerData.styles.split(',') : [],
              works: works,
              bio: photographerData.bio || '暂无简介',
              followers: photographerData.followers || 0,
              collected: false
            },
            leftWorks: leftWorks,
            rightWorks: rightWorks
          });
          
          // 加载摄影师的套餐列表
          this.loadPackages(photographerData.id);
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
        console.error('加载摄影师详情失败', err);
        wx.showToast({
          title: '网络错误，请稍后重试',
          icon: 'none',
          duration: 2000
        });
        this.setData({ loading: false });
      }
    });
  },
  
  // 加载摄影师的套餐列表
  loadPackages(photographerId) {
    wx.request({
      url: app.globalData.baseUrl + '/api/package/photographer',
      method: 'GET',
      data: {
        photographerId: photographerId
      },
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token')
      },
      success: (res) => {
        if (res.data.code === 200) {
          // 处理套餐数据，将features JSON字符串转换为数组
          const packages = (res.data.data || []).map(pack => {
            let featuresArray = [];
            if (pack.features) {
              try {
                // 尝试解析JSON格式的features
                featuresArray = JSON.parse(pack.features);
              } catch (e) {
                // 如果解析失败，尝试按逗号分割
                featuresArray = pack.features.split(',');
              }
            }
            return {
              ...pack,
              featuresArray: featuresArray
            };
          });
          
          this.setData({
            packages: packages,
            loading: false
          });
        } else {
          wx.showToast({
            title: '获取套餐列表失败',
            icon: 'none',
            duration: 2000
          });
          this.setData({ loading: false });
        }
      },
      fail: (err) => {
        console.error('加载套餐列表失败:', err);
        wx.showToast({
          title: '网络错误，请稍后重试',
          icon: 'none',
          duration: 2000
        });
        this.setData({ loading: false });
      }
    });
  },
  
  // 切换标签
  switchTab(e) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({ activeTab: tab });
  },
  
  // 返回上一页
  goBack() {
    wx.navigateBack();
  },
  
  // 收藏/取消收藏
  toggleCollect() {
    const photographer = { ...this.data.photographer };
    photographer.collected = !photographer.collected;
    
    this.setData({ photographer });
    
    wx.showToast({
      title: photographer.collected ? '收藏成功' : '取消收藏',
      icon: 'success',
      duration: 1000
    });
  },
  
  // 预览图片
  previewImage(e) {
    const url = e.currentTarget.dataset.url;
    const works = [...this.data.leftWorks, ...this.data.rightWorks];
    
    wx.previewImage({
      current: url,
      urls: works
    });
  },
  
  // 立即预约
  bookNow() {
    wx.showModal({
      title: '预约摄影师',
      content: '确定要预约这位摄影师吗？',
      success: (res) => {
        if (res.confirm) {
          wx.navigateTo({
            url: '/pages/order/create?photographerId=' + this.data.photographer.id,
            success: function(res) {
              
            },
            fail: function(res) {
              console.error('跳转到预约页面失败', res);
              wx.showToast({
                title: '跳转失败，请稍后重试',
                icon: 'none',
                duration: 1500
              });
            }
          });
        }
      }
    });
  },
  
  // 图片加载失败处理
  handleImageError(e) {
    const index = e.currentTarget.dataset.index;
    const url = e.currentTarget.dataset.url;
    const photographer = { ...this.data.photographer };

    if (e.currentTarget.dataset.type === 'cover') {
      photographer.cover = 'https://api.dicebear.com/7.x/personas/svg?seed=cover&size=750';
    } else if (e.currentTarget.dataset.type === 'avatar') {
      photographer.avatar = 'https://api.dicebear.com/7.x/personas/svg?seed=avatar&size=200';
    } else if (url && photographer.works) {
      const originalIndex = photographer.works.findIndex(work => work === url);
      if (originalIndex !== -1) {
        photographer.works[originalIndex] = 'https://api.dicebear.com/7.x/personas/svg?seed=placeholder&size=300';
      }
    }

    this.setData({ photographer });
  }
});
