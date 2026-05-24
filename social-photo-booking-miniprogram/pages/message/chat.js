// chat.js
Page({
  data: {
    chatUser: {
      id: null,
      name: '',
      avatar: ''
    },
    messages: [],
    inputText: '',
    toView: '',
    loading: false
  },
  
  onLoad(options) {
    const userId = options.userId;
    const userName = options.userName;
    const userAvatar = options.userAvatar;
    
    this.setData({
      chatUser: {
        id: userId,
        name: userName,
        avatar: userAvatar
      }
    });
    
    this.loadMessages(userId);
  },
  
  loadMessages(userId) {
    const token = wx.getStorageSync('token');
    if (!token) {
      return;
    }
    
    this.setData({ loading: true });
    
    wx.request({
      url: `${getApp().globalData.baseUrl}/message/chat`,
      method: 'GET',
      header: { 'Authorization': 'Bearer ' + token },
      data: {
        userId: userId,
        page: 1,
        pageSize: 50
      },
      timeout: 30000,
      success: (res) => {
        
        if (res.data && res.data.code === 200 && res.data.data) {
          const messageList = res.data.data.list || [];
          const messages = messageList.map(msg => ({
            id: msg.id,
            content: msg.content,
            time: this.formatTime(msg.createTime),
            isSelf: msg.senderId === getApp().getUserInfo().id,
            avatar: msg.senderId === getApp().getUserInfo().id ? getApp().getUserInfo().avatar : this.data.chatUser.avatar
          }));
          
          this.setData({ 
            messages,
            loading: false 
          });
          
          this.scrollToBottom();
        } else {
          this.setData({ loading: false });
        }
      },
      fail: (err) => {
        console.error('加载消息失败:', err);
        this.setData({ loading: false });
      }
    });
  },
  
  navigateBack() {
    wx.navigateBack();
  },
  
  bindInputInput(e) {
    this.setData({ inputText: e.detail.value });
  },
  
  sendMessage() {
    const inputText = this.data.inputText.trim();
    if (!inputText) {
      return;
    }
    
    const token = wx.getStorageSync('token');
    if (!token) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    
    const messages = this.data.messages;
    const newMessage = {
      id: Date.now(),
      content: inputText,
      time: this.getCurrentTime(),
      isSelf: true,
      avatar: getApp().getUserInfo().avatar
    };
    
    messages.push(newMessage);
    this.setData({ 
      messages, 
      inputText: '' 
    });
    
    this.scrollToBottom();
    
    // 发送消息到后端
    wx.request({
      url: `${getApp().globalData.baseUrl}/message/send`,
      method: 'POST',
      header: { 'Authorization': 'Bearer ' + token },
      data: {
        receiverId: this.data.chatUser.id,
        content: inputText
      },
      timeout: 30000,
      success: (res) => {
        
      },
      fail: (err) => {
        console.error('发送消息失败', err);
        wx.showToast({ title: '发送失败，请重试', icon: 'none' });
      }
    });
  },
  
  getCurrentTime() {
    const now = new Date();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
  },
  
  formatTime(timeStr) {
    if (!timeStr) return '';
    const time = new Date(timeStr);
    const hours = String(time.getHours()).padStart(2, '0');
    const minutes = String(time.getMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
  },
  
  scrollToBottom() {
    const messages = this.data.messages;
    if (messages.length > 0) {
      this.setData({ toView: 'msg-' + messages[messages.length - 1].id });
    }
  }
});
