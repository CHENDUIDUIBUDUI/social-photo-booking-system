// edit.js
Page({
  data: {
    userInfo: {
      id: 1,
      nickname: '用户1',
      avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=user&size=150',
      gender: 2,
      age: '',
      phone: '',
      bio: '',
      occupation: '',
      skills: []
    },
    newSkill: '',
    loading: false,
    showVerifyCode: false,
    verifyCode: '',
    countdown: 0,
    timer: null
  },
  
  onLoad() {
    this.loadUserInfo();
  },
  
  onUnload() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
  
  loadUserInfo() {
    const userInfo = getApp().getUserInfo();
    if (userInfo) {
      this.setData({ userInfo });
    } else {
      // 模拟用户数据
      const mockUserInfo = {
        id: 1,
        nickname: '用户1',
        avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=user&size=150',
        gender: 2,
        age: '',
        phone: '',
        bio: '',
        occupation: '',
        skills: []
      };
      this.setData({ userInfo: mockUserInfo });
    }
  },
  
  navigateBack() {
    wx.navigateBack();
  },
  
  chooseAvatar() {
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const avatarUrl = res.tempFilePaths[0];
        this.setData({
          'userInfo.avatar': avatarUrl
        });
      }
    });
  },
  
  bindNicknameInput(e) {
    this.setData({
      'userInfo.nickname': e.detail.value
    });
  },
  
  selectGender(e) {
    const gender = parseInt(e.currentTarget.dataset.gender);
    this.setData({
      'userInfo.gender': gender
    });
  },
  
  bindAgeInput(e) {
    this.setData({
      'userInfo.age': e.detail.value
    });
  },
  
  bindPhoneInput(e) {
    const phone = e.detail.value;
    this.setData({
      'userInfo.phone': phone
    });
  },
  
  bindVerifyCodeInput(e) {
    this.setData({
      verifyCode: e.detail.value
    });
  },
  
  bindBioInput(e) {
    this.setData({
      'userInfo.bio': e.detail.value
    });
  },
  
  bindOccupationInput(e) {
    this.setData({
      'userInfo.occupation': e.detail.value
    });
  },
  
  bindNewSkillInput(e) {
    this.setData({
      newSkill: e.detail.value
    });
  },
  
  addSkill() {
    const newSkill = this.data.newSkill.trim();
    if (newSkill) {
      const skills = this.data.userInfo.skills;
      if (!skills.includes(newSkill)) {
        skills.push(newSkill);
        this.setData({
          'userInfo.skills': skills,
          newSkill: ''
        });
      }
    }
  },
  
  deleteSkill(e) {
    const index = e.currentTarget.dataset.index;
    const skills = this.data.userInfo.skills;
    skills.splice(index, 1);
    this.setData({
      'userInfo.skills': skills
    });
  },
  
  // 发送验证码
  sendVerifyCode() {
    const phone = this.data.userInfo.phone;
    if (!/^1[3-9]\d{9}$/.test(phone)) {
      wx.showToast({
        title: '请输入正确的手机号码',
        icon: 'none'
      });
      return;
    }
    
    // 发送验证码
    wx.request({
      url: `${getApp().globalData.baseUrl}/auth/send-code`,
      method: 'POST',
      data: {
        phone: phone
      },
      timeout: 30000,
      success: (res) => {
        if (res.data.code === 200) {
          // 开始倒计时
          this.startCountdown();
          this.setData({ showVerifyCode: true });
          wx.showToast({
            title: '验证码已发送',
            icon: 'success'
          });
        } else {
          wx.showToast({
            title: res.data.message || '发送验证码失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        console.error('发送验证码失败:', err);
        wx.showToast({
          title: '网络错误，请重试',
          icon: 'none'
        });
      }
    });
  },
  
  // 开始倒计时
  startCountdown() {
    let countdown = 60;
    this.setData({ countdown });
    
    this.timer = setInterval(() => {
      countdown--;
      if (countdown <= 0) {
        clearInterval(this.timer);
        this.setData({ countdown: 0 });
      } else {
        this.setData({ countdown });
      }
    }, 1000);
  },
  
  // 绑定手机号
  bindPhone() {
    const phone = this.data.userInfo.phone;
    const verifyCode = this.data.verifyCode;
    const userId = this.data.userInfo.id;
    
    if (!/^1[3-9]\d{9}$/.test(phone)) {
      wx.showToast({ title: '请输入正确的手机号码', icon: 'none' });
      return;
    }
    
    if (!verifyCode || verifyCode.length !== 6) {
      wx.showToast({ title: '请输入6位验证码', icon: 'none' });
      return;
    }
    
    this.setData({ loading: true });
    
    wx.request({
      url: `${getApp().globalData.baseUrl}/auth/bind-phone`,
      method: 'POST',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token'),
        'Content-Type': 'application/json'
      },
      data: {
        userId: userId,
        phone: phone,
        code: verifyCode
      },
      timeout: 30000,
      success: (res) => {
        this.setData({ loading: false });
        
        if (res.data.code === 200) {
          wx.showToast({
            title: '手机号绑定成功',
            icon: 'success',
            duration: 1500
          });
        } else {
          wx.showToast({
            title: res.data.message || '绑定失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        this.setData({ loading: false });
        console.error('绑定手机号失败', err);
        wx.showToast({
          title: '网络错误，请重试',
          icon: 'none'
        });
      }
    });
  },
  
  saveUserInfo() {
    const phone = this.data.userInfo.phone;
    const verifyCode = this.data.verifyCode;
    
    // 如果填写了手机号，需要绑定
    if (phone && verifyCode) {
      this.bindPhone();
      return;
    }
    
    this.setData({ loading: true });
    
    // 模拟保存用户信息
    setTimeout(() => {
      getApp().setUserInfo(this.data.userInfo);
      wx.showToast({
        title: '保存成功',
        icon: 'success',
        duration: 1500,
        success: () => {
          this.setData({ loading: false });
          setTimeout(() => {
            wx.navigateBack();
          }, 1500);
        }
      });
    }, 1000);
  }
});

