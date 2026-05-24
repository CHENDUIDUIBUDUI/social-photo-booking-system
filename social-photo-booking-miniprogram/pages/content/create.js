// 发布约拍需求页面
Page({
  data: {
    // 拍摄类型
    shootingTypes: [
      { label: '个人写真', value: 'personal' },
      { label: '情侣写真', value: 'couple' },
      { label: '婚纱摄影', value: 'wedding' },
      { label: '儿童摄影', value: 'children' },
      { label: '商业拍摄', value: 'commercial' },
      { label: '其他', value: 'other' }
    ],
    selectedShootingType: '',
    
    // 拍摄风格
    styles: [
      { label: '清新自然', value: 'natural' },
      { label: '复古文艺', value: 'vintage' },
      { label: '时尚潮流', value: 'fashion' },
      { label: '极简主义', value: 'minimalist' },
      { label: '创意个性', value: 'creative' },
      { label: '韩式风格', value: 'korean' },
      { label: '日式风格', value: 'japanese' },
      { label: '欧美风格', value: 'western' }
    ],
    selectedStyles: [],
    
    // 表单数据
    selectedDate: '',
    location: '',
    budget: '',
    description: '',
    images: [],
    
    // 表单验证
    formErrors: {}
  },
  
  onLoad: function(options) {
    // 页面加载
  },
  
  // 检查风格是否被选中
  isStyleSelected: function(styleValue) {
    const selectedStyles = this.data.selectedStyles;
    for (let i = 0; i < selectedStyles.length; i++) {
      if (selectedStyles[i] === styleValue) {
        return true;
      }
    }
    return false;
  },
  
  // 返回上一页
  navigateBack: function() {
    wx.navigateBack({ delta: 1 });
  },
  
  // 选择拍摄类型
  selectShootingType: function(e) {
    const value = e.currentTarget.dataset.value;
    this.setData({
      selectedShootingType: value
    });
  },
  
  // 切换拍摄风格
  toggleStyle: function(e) {
    
    const value = e.currentTarget.dataset.value;
    
    const selectedStyles = this.data.selectedStyles;
    
    
    if (selectedStyles.includes(value)) {
      // 取消选择
      const newStyles = selectedStyles.filter(item => item !== value);
      
      this.setData({
        selectedStyles: newStyles
      });
    } else {
      // 选择
      const newStyles = [...selectedStyles, value];
      
      this.setData({
        selectedStyles: newStyles
      });
    }
  },
  
  // 输入拍摄时间
  bindDateInput: function(e) {
    this.setData({
      selectedDate: e.detail.value
    });
  },
  
  // 显示日期选择器
  showDatePicker: function() {
    // 由于模拟器环境限制，使用简单的日期选择方式
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth() + 1;
    const day = now.getDate();
    const defaultDate = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
    
    wx.showModal({
      title: '选择拍摄时间',
      content: `请输入拍摄日期，格式：YYYY-MM-DD\n\n默认日期：${defaultDate}`,
      placeholderText: '请输入日期',
      success: (res) => {
        if (res.confirm) {
          // 这里可以添加日期选择逻辑
          // 由于模拟器限制，暂时使用默认日期
          this.setData({
            selectedDate: defaultDate
          });
        }
      }
    });
  },
  
  // 输入拍摄地点
  bindLocationInput: function(e) {
    this.setData({
      location: e.detail.value
    });
  },
  
  // 输入预算金额
  bindBudgetInput: function(e) {
    this.setData({
      budget: e.detail.value
    });
  },
  
  // 输入需求详情
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
    
    if (!this.data.selectedShootingType) {
      errors.shootingType = '请选择拍摄类型';
    }
    
    if (this.data.selectedStyles.length === 0) {
      errors.styles = '请至少选择一种拍摄风格';
    }
    
    if (!this.data.selectedDate) {
      errors.date = '请选择拍摄时间';
    }
    
    if (!this.data.location) {
      errors.location = '请输入拍摄地点';
    }
    
    if (!this.data.budget) {
      errors.budget = '请输入预算金额';
    }
    
    if (!this.data.description) {
      errors.description = '请输入需求详情';
    }
    
    this.setData({
      formErrors: errors
    });
    
    return Object.keys(errors).length === 0;
  },
  
  // 提交需求
  submitContent: function() {
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
      title: '发布中..'
    });
    
    // 模拟发布请求
    setTimeout(() => {
      // 构建发布数据
      const requestData = {
        shootingType: this.data.selectedShootingType,
        styles: this.data.selectedStyles,
        date: this.data.selectedDate,
        location: this.data.location,
        budget: this.data.budget,
        description: this.data.description,
        images: this.data.images
      };
      
      
      
      // 模拟成功响应
      wx.hideLoading();
      wx.showToast({
        title: '发布成功',
        icon: 'success'
      });
      
      // 跳转到内容列表页
      setTimeout(() => {
        wx.navigateTo({
          url: '/pages/content/list'
        });
      }, 1500);
    }, 1500);
  }
});

