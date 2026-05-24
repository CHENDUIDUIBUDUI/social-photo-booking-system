// review.js
Page({
  data: {
    orderId: null,
    order: null,
    rating: 5, // 默认5星
    comment: '',
    submitting: false,
    stars: [1, 2, 3, 4, 5]
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ orderId: options.id });
      this.loadOrderInfo(options.id);
    }
  },

  // 加载订单信息
  loadOrderInfo(orderId) {
    const token = wx.getStorageSync('token');
    wx.request({
      url: `${getApp().globalData.baseUrl}/api/order/info`,
      method: 'GET',
      header: { 'Authorization': 'Bearer ' + token },
      data: { id: orderId },
      success: (res) => {
        if (res.data && res.data.code === 200 && res.data.data) {
          this.setData({ order: res.data.data });
        }
      }
    });
  },

  // 获取评分文字
  getRatingText(rating) {
    switch (rating) {
      case 1:
        return '非常不满意';
      case 2:
        return '不满意';
      case 3:
        return '一般';
      case 4:
        return '满意';
      case 5:
        return '非常满意';
      default:
        return '';
    }
  },

  // 选择评分
  selectRating(e) {
    const rating = parseInt(e.currentTarget.dataset.rating);
    this.setData({ rating });
  },

  // 输入评论
  inputComment(e) {
    this.setData({ comment: e.detail.value });
  },

  // 提交评价
  submitReview() {
    const { orderId, rating, comment } = this.data;
    
    if (!orderId) {
      wx.showToast({ title: '订单信息错误', icon: 'none' });
      return;
    }

    if (this.data.submitting) {
      return;
    }

    this.setData({ submitting: true });

    const token = wx.getStorageSync('token');
    wx.request({
      url: `${getApp().globalData.baseUrl}/api/order/rating`,
      method: 'PUT',
      header: { 'Authorization': 'Bearer ' + token, 'content-type': 'application/x-www-form-urlencoded' },
      data: {
        id: orderId,
        userRating: rating,
        userComment: comment || '用户未填写评价',
        photographerRating: null,
        photographerComment: null
      },
      success: (res) => {
        this.setData({ submitting: false });
        if (res.data && res.data.code === 200) {
          wx.showToast({ title: '评价成功', icon: 'success' });
          setTimeout(() => {
            wx.navigateBack();
          }, 1500);
        } else {
          wx.showToast({ title: res.data?.message || '评价失败', icon: 'none' });
        }
      },
      fail: () => {
        this.setData({ submitting: false });
        wx.showToast({ title: '评价失败，请重试', icon: 'none' });
      }
    });
  }
});