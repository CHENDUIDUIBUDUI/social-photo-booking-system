// 套餐编辑页面
const app = getApp();

Page({
  data: {
    packageId: '',
    formData: {
      id: '',
      name: '',
      price: '',
      description: '',
      features: [''],
      status: 1
    }
  },
  
  onLoad(options) {
    const id = options.id;
    if (id) {
      this.setData({ packageId: id });
      this.loadPackageDetail(id);
    }
  },
  
  // 加载套餐详情
  loadPackageDetail(id) {
    wx.showLoading({ title: '加载�?..' });
    
    const token = wx.getStorageSync('token');
    
    wx.request({
      url: app.globalData.baseUrl + '/api/package/info',
      method: 'GET',
      data: {
        id: id
      },
      header: {
        'Authorization': 'Bearer ' + token
      },
      success: (res) => {
        if (res.data.code === 200) {
          const pack = res.data.data;
          try {
            pack.features = JSON.parse(pack.features);
          } catch (e) {
            pack.features = [];
          }
          this.setData({
            formData: {
              id: pack.id,
              name: pack.name,
              price: pack.price,
              description: pack.description,
              features: pack.features.length > 0 ? pack.features : [''],
              status: pack.status
            }
          });
        } else {
          wx.showToast({ title: res.data.message || '加载失败', icon: 'none' });
        }
      },
      fail: (err) => {
        console.error('加载套餐详情失败:', err);
        wx.showToast({ title: '网络错误', icon: 'none' });
      },
      complete: () => {
        wx.hideLoading();
      }
    });
  },
  
  // 绑定输入事件
  bindInput(e) {
    const field = e.currentTarget.dataset.field;
    const value = e.detail.value;
    
    this.setData({
      [`formData.${field}`]: value
    });
  },
  
  // 绑定服务输入事件
  bindFeatureInput(e) {
    const index = e.currentTarget.dataset.index;
    const value = e.detail.value;
    
    const features = [...this.data.formData.features];
    features[index] = value;
    
    this.setData({
      'formData.features': features
    });
  },
  
  // 添加服务
  addFeature() {
    const features = [...this.data.formData.features, ''];
    this.setData({
      'formData.features': features
    });
  },
  
  // 删除服务
  removeFeature(e) {
    const index = e.currentTarget.dataset.index;
    const features = [...this.data.formData.features];
    features.splice(index, 1);
    
    this.setData({
      'formData.features': features
    });
  },
  
  // 绑定状态切换
  bindStatusChange(e) {
    this.setData({
      'formData.status': e.detail.value ? 1 : 0
    });
  },
  
  // 提交表单
  submitForm() {
    const { id, name, price, description, features, status } = this.data.formData;
    
    // 验证表单
    if (!name) {
      wx.showToast({ title: '请输入套餐名称', icon: 'none' });
      return;
    }
    
    if (!price || parseFloat(price) <= 0) {
      wx.showToast({ title: '请输入有效的套餐价格', icon: 'none' });
      return;
    }
    
    if (!description) {
      wx.showToast({ title: '请输入套餐描述', icon: 'none' });
      return;
    }
    
    // 过滤空服务    const validFeatures = features.filter(feature => feature.trim());
    if (validFeatures.length === 0) {
      wx.showToast({ title: '请至少添加一项服务', icon: 'none' });
      return;
    }
    
    wx.showLoading({ title: '保存�?..' });
    
    const token = wx.getStorageSync('token');
    
    wx.request({
      url: app.globalData.baseUrl + '/api/package/update',
      method: 'PUT',
      data: {
        id: id,
        name: name,
        price: parseFloat(price),
        description: description,
        features: JSON.stringify(validFeatures),
        status: status
      },
      header: {
        'Authorization': 'Bearer ' + token
      },
      success: (res) => {
        if (res.data.code === 200) {
          wx.showToast({ title: '更新成功', icon: 'success' });
          // 跳转到套餐列表页面
          wx.redirectTo({
            url: '/pages/package/list'
          });
        } else {
          wx.showToast({ title: res.data.message || '更新失败', icon: 'none' });
        }
      },
      fail: (err) => {
        console.error('更新套餐失败:', err);
        wx.showToast({ title: '网络错误', icon: 'none' });
      },
      complete: () => {
        wx.hideLoading();
      }
    });
  },
  
  // 返回上一页
  goBack() {
    wx.navigateBack();
  }
});
