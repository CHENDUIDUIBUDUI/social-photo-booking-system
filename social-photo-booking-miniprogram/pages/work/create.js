// 发布作品页面
Page({
  data: {
    // 作品分类
    categories: [
      { label: '人像摄影', value: 'portrait' },
      { label: '风景摄影', value: 'landscape' },
      { label: '建筑摄影', value: 'architecture' },
      { label: '街拍', value: 'street' },
      { label: '商业摄影', value: 'commercial' },
      { label: '其他', value: 'other' }
    ],
    selectedCategory: '',
    
    // 拍摄风格
    styleList: [
      { label: '清新自然', value: 'natural', selected: false },
      { label: '复古文艺', value: 'vintage', selected: false },
      { label: '时尚潮流', value: 'fashion', selected: false },
      { label: '极简主义', value: 'minimalist', selected: false },
      { label: '创意个性', value: 'creative', selected: false },
      { label: '韩式风格', value: 'korean', selected: false },
      { label: '日式风格', value: 'japanese', selected: false },
      { label: '欧美风格', value: 'western', selected: false }
    ],
    
    // 表单数据
    title: '',
    location: '',
    camera: '',
    lens: '',
    aperture: '',
    shutter: '',
    iso: '',
    description: '',
    images: [],
    
    // 表单验证
    formErrors: {}
  },
  
  onLoad: function(options) {
    // 页面加载
  },
  

  
  // 返回上一页
  navigateBack: function() {
    wx.navigateBack({ delta: 1 });
  },
  
  // 选择作品分类
  selectCategory: function(e) {
    const value = e.currentTarget.dataset.value;
    this.setData({
      selectedCategory: value
    });
  },
  
  // 切换拍摄风格
  toggleStyle: function(e) {
    const value = e.currentTarget.dataset.value;
    const styleList = this.data.styleList;
    
    const newStyleList = styleList.map(item => {
      if (item.value === value) {
        return { ...item, selected: !item.selected };
      }
      return item;
    });
    
    this.setData({
      styleList: newStyleList
    });
  },
  
  // 获取选中的风格值数组
  getSelectedStyles: function() {
    return this.data.styleList
      .filter(item => item.selected)
      .map(item => item.value);
  },
  
  // 输入作品标题
  bindTitleInput: function(e) {
    this.setData({
      title: e.detail.value
    });
  },
  
  // 输入拍摄地点
  bindLocationInput: function(e) {
    this.setData({
      location: e.detail.value
    });
  },
  
  // 输入相机型号
  bindCameraInput: function(e) {
    this.setData({
      camera: e.detail.value
    });
  },
  
  // 输入镜头型号
  bindLensInput: function(e) {
    this.setData({
      lens: e.detail.value
    });
  },
  
  // 输入光圈
  bindApertureInput: function(e) {
    this.setData({
      aperture: e.detail.value
    });
  },
  
  // 输入快门
  bindShutterInput: function(e) {
    this.setData({
      shutter: e.detail.value
    });
  },
  
  // 输入ISO
  bindIsoInput: function(e) {
    this.setData({
      iso: e.detail.value
    });
  },
  
  // 输入作品描述
  bindDescriptionInput: function(e) {
    this.setData({
      description: e.detail.value
    });
  },
  
  // 选择图片
  chooseImage: function() {
    const maxCount = 9 - this.data.images.length;
    
    wx.chooseImage({
      count: maxCount,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        this.setData({
          images: [...this.data.images, ...res.tempFilePaths]
        });
      }
    });
  },
  
  // 删除图片
  deleteImage: function(e) {
    const index = e.currentTarget.dataset.index;
    const images = this.data.images;
    images.splice(index, 1);
    
    this.setData({
      images: images
    });
  },
  
  // 表单验证
  validateForm: function() {
    const errors = {};
    
    if (!this.data.title) {
      errors.title = '请输入作品标题';
    }
    
    if (!this.data.selectedCategory) {
      errors.category = '请选择作品分类';
    }
    
    if (this.getSelectedStyles().length === 0) {
      errors.styles = '请至少选择一种拍摄风格';
    }
    
    if (!this.data.location) {
      errors.location = '请输入拍摄地点';
    }
    
    if (!this.data.description) {
      errors.description = '请输入作品描述';
    }
    
    if (this.data.images.length === 0) {
      errors.images = '请至少上传一张作品图片';
    }
    
    this.setData({
      formErrors: errors
    });
    
    return Object.keys(errors).length === 0;
  },
  
  // 上传单个图片
  uploadImage: function(tempFilePath) {
    return new Promise((resolve, reject) => {
      const token = wx.getStorageSync('token');
      wx.uploadFile({
        url: `${getApp().globalData.baseUrl}/api/upload/image`,
        filePath: tempFilePath,
        name: 'file',
        header: {
          'Authorization': 'Bearer ' + token
        },
        success: (res) => {
          try {
            const result = JSON.parse(res.data);
            if (result.code === 200) {
              resolve(result.data.url);
            } else {
              reject(new Error(result.message || '上传失败'));
            }
          } catch (error) {
            reject(new Error('上传失败'));
          }
        },
        fail: (err) => {
          reject(err);
        }
      });
    });
  },

  // 批量上传图片
  uploadImages: function(images) {
    var uploadedUrls = [];
    var index = 0;
    var that = this;  // 保存this引用
    
    function uploadNext() {
      return new Promise(function(resolve, reject) {
        if (index >= images.length) {
          resolve(uploadedUrls);
          return;
        }
        
        that.uploadImage(images[index]).then(function(url) {  // 使用that代替this
          uploadedUrls.push(url);
          index++;
          uploadNext().then(resolve).catch(reject);
        }).catch(reject);
      });
    }
    
    return uploadNext();
  },

  // 提交作品
  submitWork: function() {
    // 表单验证
    if (!this.validateForm()) {
      wx.showToast({
        title: '请完善所有必填项',
        icon: 'none'
      });
      return;
    }
    
    // 显示加载状态
    wx.showLoading({ 
      title: '上传图片中..' 
    });
    
    // 上传图片
    this.uploadImages(this.data.images).then(function(uploadedImages) {
      // 构建发布数据
      var workData = {
        title: this.data.title,
        location: this.data.location,
        description: this.data.description,
        coverImage: uploadedImages[0]
      };
      
      // 调用后端API发布作品
      var token = wx.getStorageSync('token');
      return new Promise(function(resolve) {
        wx.request({
          url: getApp().globalData.baseUrl + '/api/content/create',
          method: 'POST',
          header: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
          },
          data: {
            title: workData.title,
            location: workData.location,
            description: workData.description,
            coverImage: workData.coverImage,
            type: 0
          },
          timeout: 30000,
          success: resolve,
          fail: resolve
        });
      });
    }.bind(this)).then(function(res) {
      wx.hideLoading();
      if (res.data && res.data.code === 200) {
        wx.showToast({
          title: '发布成功',
          icon: 'success'
        });
        
        // 跳转到作品详情页
        setTimeout(() => {
          wx.navigateTo({
            url: '/pages/content/detail?id=' + res.data.data.id
          });
        }, 1500);
      } else {
        wx.showToast({
          title: res.data.message || '发布失败',
          icon: 'none'
        });
      }
    }).catch(function(error) {
      wx.hideLoading();
      console.error('发布作品失败:', error);
      wx.showToast({
        title: '发布失败，请重试',
        icon: 'none'
      });
    });
  }
});
