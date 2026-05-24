const app = getApp();

Page({
  data: {
    photographerId: null,
    photographer: null,
    loading: true,
    submitting: false,
    
    date: '',
    time: '',
    location: '',
    contactName: '',
    contactPhone: '',
    notes: '',
    minDate: '',
    
    packages: [],
    selectedPackage: null,
    deposit: null,
    balance: null
  },

  onLoad(options) {
    const photographerId = options.photographerId;
    if (photographerId) {
      this.setData({ photographerId: photographerId });
      this.loadPhotographerInfo(photographerId);
    }
    
    const today = new Date();
    const minDate = this.formatDate(today);
    this.setData({ minDate: minDate });
  },

  formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  },

  loadPhotographerInfo(id) {
    this.setData({ loading: true });
    
    wx.request({
      url: app.globalData.baseUrl + '/api/photographer/info',
      method: 'GET',
      data: { id: id },
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token')
      },
      success: (res) => {
        if (res.data.code === 200) {
          this.setData({
            photographer: res.data.data
          });
          // 获取摄影师的套餐列表
          this.loadPackages(id);
        } else {
          wx.showToast({
            title: '获取摄影师信息失败',
            icon: 'none'
          });
          setTimeout(() => {
            wx.navigateBack();
          }, 1500);
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络错误',
          icon: 'none'
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
      data: { photographerId: photographerId },
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token')
      },
      success: (res) => {
        if (res.data.code === 200) {
          this.setData({
            packages: res.data.data || [],
            loading: false
          });
        } else {
          wx.showToast({
            title: '获取套餐列表失败',
            icon: 'none'
          });
          this.setData({ loading: false });
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络错误',
          icon: 'none'
        });
        this.setData({ loading: false });
      }
    });
  },

  onDateChange(e) {
    this.setData({ date: e.detail.value });
  },

  onTimeChange(e) {
    this.setData({ time: e.detail.value });
  },

  onLocationInput(e) {
    this.setData({ location: e.detail.value });
  },

  onNotesInput(e) {
    this.setData({ notes: e.detail.value });
  },

  onContactNameInput(e) {
    this.setData({ contactName: e.detail.value });
  },

  onContactPhoneInput(e) {
    this.setData({ contactPhone: e.detail.value });
  },

  selectPackage(e) {
    const packageId = e.currentTarget.dataset.id;
    const selectedPackage = this.data.packages.find(p => p.id === packageId);
    
    // 计算定金和尾�?
    if (selectedPackage) {
      const deposit = (selectedPackage.price * 0.3).toFixed(2);
      const balance = (selectedPackage.price * 0.7).toFixed(2);
      this.setData({
        selectedPackage: selectedPackage,
        deposit: deposit,
        balance: balance
      });
    } else {
      this.setData({ selectedPackage: null, deposit: null, balance: null });
    }
  },

  submitOrder() {
    const { photographerId, date, time, location, contactName, contactPhone, notes, selectedPackage } = this.data;
    
    if (!selectedPackage) {
      wx.showToast({
        title: '请选择套餐',
        icon: 'none'
      });
      return;
    }
    
    if (!date) {
      wx.showToast({
        title: '请选择拍摄日期',
        icon: 'none'
      });
      return;
    }
    
    if (!time) {
      wx.showToast({
        title: '请选择拍摄时间',
        icon: 'none'
      });
      return;
    }
    
    if (!location.trim()) {
      wx.showToast({
        title: '请输入拍摄地点',
        icon: 'none'
      });
      return;
    }
    
    if (!contactName.trim()) {
      wx.showToast({
        title: '请输入联系人姓名',
        icon: 'none'
      });
      return;
    }
    
    if (!contactPhone.trim()) {
      wx.showToast({
        title: '请输入联系电话',
        icon: 'none'
      });
      return;
    }
    
    // 简单的手机号验�?
    const phoneRegex = /^1[3-9]\d{9}$/;
    if (!phoneRegex.test(contactPhone)) {
      wx.showToast({
        title: '请输入正确的手机号',
        icon: 'none'
      });
      return;
    }
    
    const shootTime = `${date} ${time}:00`;
    
    this.setData({ submitting: true });
    
    const userId = wx.getStorageSync('userId');
    if (!userId) {
      this.setData({ submitting: false });
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      setTimeout(() => {
        wx.navigateTo({
          url: '/pages/login/login'
        });
      }, 1500);
      return;
    }
    
    wx.request({
      url: app.globalData.baseUrl + '/api/order/create',
      method: 'POST',
      data: {
        userId: userId,
        photographerId: photographerId,
        contentId: 0, // 默认值，因为用户是通过选择套餐创建订单的
        totalAmount: selectedPackage.price,
        deposit: parseFloat(this.data.deposit),
        paidDeposit: parseFloat(this.data.deposit),
        balance: parseFloat(this.data.balance),
        location: location,
        shootTime: shootTime,
        contactName: contactName,
        contactPhone: contactPhone,
        notes: notes,
        status: 0
      },
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
        'content-type': 'application/json'
      },
      success: (res) => {
        this.setData({ submitting: false });
        if (res.data && res.data.code === 200) {
          // 预约成功后，提醒用户要付定金
          wx.showToast({
            title: '预约成功，请注意支付定金',
            icon: 'success'
          });
          
          setTimeout(() => {
            // 直接跳转到订单详情页
            wx.redirectTo({
              url: '/pages/order/detail?id=' + res.data.data.id
            });
          }, 1000);
        } else {
          wx.showToast({
            title: res.data?.message || '预约失败，请重试',
            icon: 'none'
          });
        }
      },
      fail: () => {
        this.setData({ submitting: false });
        wx.showToast({
          title: '网络错误',
          icon: 'none'
        });
      }
    });
  }
});

