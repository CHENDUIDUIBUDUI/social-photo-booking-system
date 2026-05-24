// feedback.js
Page({
  data: {
    feedbackTypes: [
      { label: '功能建议', value: 'suggestion' },
      { label: '问题反馈', value: 'bug' },
      { label: '其他', value: 'other' }
    ],
    feedbackType: 'suggestion',
    feedbackContent: '',
    contactInfo: '',
    images: [],
    loading: false
  },
  
  onLoad() {
    
  },
  
  navigateBack() {
    wx.navigateBack();
  },
  
  selectType(e) {
    const value = e.currentTarget.dataset.value;
    this.setData({ feedbackType: value });
  },
  
  bindFeedbackContentInput(e) {
    this.setData({ feedbackContent: e.detail.value });
  },
  
  bindContactInfoInput(e) {
    this.setData({ contactInfo: e.detail.value });
  },
  
  chooseImage() {
    const maxCount = 3 - this.data.images.length;
    wx.chooseImage({
      count: maxCount,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const images = this.data.images;
        images.push(...res.tempFilePaths);
        this.setData({ images });
      }
    });
  },
  
  deleteImage(e) {
    const index = e.currentTarget.dataset.index;
    const images = this.data.images;
    images.splice(index, 1);
    this.setData({ images });
  },
  
  submitFeedback() {
    // 验证表单
    if (!this.data.feedbackContent) {
      wx.showToast({ title: '请输入反馈内容', icon: 'none' });
      return;
    }
    
    this.setData({ loading: true });
    
    // 模拟提交反馈
    setTimeout(() => {
      this.setData({ loading: false });
      wx.showToast({
        title: '提交成功',
        icon: 'success',
        duration: 1500,
        success: () => {
          setTimeout(() => {
            wx.navigateBack();
          }, 1500);
        }
      });
    }, 1000);
  }
});
