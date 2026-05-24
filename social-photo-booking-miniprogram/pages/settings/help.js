// help.js
Page({
  data: {
    searchKeyword: '',
    faqList: [
      {
        id: 1,
        question: '如何发布作品？',
        answer: '进入"我的"页面，点击"我的作品"，然后点击"发布作品"按钮，填写相关信息并上传图片即可发布作品。',
        expanded: false
      },
      {
        id: 2,
        question: '如何发布需求？',
        answer: '进入"我的"页面，点击"我的需求"，然后点击"发布需求"按钮，填写相关信息并上传图片即可发布需求。',
        expanded: false
      },
      {
        id: 3,
        question: '如何约拍？',
        answer: '浏览首页的作品列表，找到心仪的作品，点击进入详情页面，然后点击"约拍"按钮，填写相关信息并提交即可。',
        expanded: false
      },
      {
        id: 4,
        question: '如何支付？',
        answer: '目前支持微信支付，在确认订单后，会跳转到支付页面，完成支付即可。',
        expanded: false
      },
      {
        id: 5,
        question: '如何取消订单？',
        answer: '进入"我的订单"页面，找到要取消的订单，点击"取消订单"按钮，确认后即可取消订单。',
        expanded: false
      }
    ]
  },

  onLoad() {
    this.loadFaqList();
  },
  
  loadFaqList() {
    // 模拟加载常见问题列表
    // 实际项目中，这里应该从后端获取常见问题列表
  },
  
  navigateBack() {
    wx.navigateBack();
  },
  
  bindSearchInput(e) {
    this.setData({ searchKeyword: e.detail.value });
    this.searchFaq();
  },
  
  searchFaq() {
    const keyword = this.data.searchKeyword.toLowerCase();
    const faqList = this.data.faqList.map(item => {
      if (keyword && !item.question.toLowerCase().includes(keyword)) {
        item.expanded = false;
      }
      return item;
    });
    this.setData({ faqList });
  },
  
  toggleFaq(e) {
    const id = e.currentTarget.dataset.id;
    const faqList = this.data.faqList.map(item => {
      if (item.id === id) {
        item.expanded = !item.expanded;
      } else {
        item.expanded = false;
      }
      return item;
    });
    this.setData({ faqList });
  },
  
  contactOnline() {
    wx.showToast({ title: '正在连接在线客服...', icon: 'loading' });
    // 实际项目中，这里应该跳转到在线客服页面
  },
  
  contactPhone() {
    wx.showModal({
      title: '联系客服',
      content: '客服电话�?00-123-4567',
      confirmText: '拨打',
      success: (res) => {
        if (res.confirm) {
          wx.makePhoneCall({
            phoneNumber: '400-123-4567'
          });
        }
      }
    });
  }
});
