const app = getApp();

Page({
  data: {
    formData: {
      realName: '',
      idCard: '',
      phone: '',
      region: '',
      bio: '',
      idCardFront: '',
      idCardBack: '',
      styles: [],
      works: [],
      price: '',
      agreed: false
    },
    styleOptions: ['人像', '风景', '街拍', '纪实', '商业', '婚礼', '儿童', '时尚', '产品', '证件照'],
    submitting: false
  },

  onLoad() {
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      this.setData({
        'formData.phone': userInfo.phone || ''
      });
    }
  },

  onRealNameInput(e) {
    this.setData({
      'formData.realName': e.detail.value
    });
  },

  onIdCardInput(e) {
    this.setData({
      'formData.idCard': e.detail.value
    });
  },

  onPhoneInput(e) {
    this.setData({
      'formData.phone': e.detail.value
    });
  },

  onRegionInput(e) {
    this.setData({
      'formData.region': e.detail.value
    });
  },

  onBioInput(e) {
    this.setData({
      'formData.bio': e.detail.value
    });
  },

  chooseIdCardFront() {
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePath = res.tempFilePaths[0];
        this.uploadImage(tempFilePath, 'idCardFront');
      }
    });
  },

  chooseIdCardBack() {
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePath = res.tempFilePaths[0];
        this.uploadImage(tempFilePath, 'idCardBack');
      }
    });
  },

  chooseWork() {
    wx.chooseImage({
      count: 9 - this.data.formData.works.length,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePaths = res.tempFilePaths;
        tempFilePaths.forEach(filePath => {
          this.uploadImage(filePath, 'works');
        });
      }
    });
  },

  uploadImage(filePath, type) {
    wx.showLoading({
      title: '上传中...'
    });

    wx.uploadFile({
      url: app.globalData.baseUrl + '/api/upload/image',
      filePath: filePath,
      name: 'file',
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token')
      },
      success: (res) => {
        wx.hideLoading();
        const data = JSON.parse(res.data);
        if (data.code === 200) {
          const relativeUrl = data.data.url;
          const imageUrl = app.globalData.baseUrl + relativeUrl;
          if (type === 'works') {
            const works = [...this.data.formData.works, imageUrl];
            this.setData({
              'formData.works': works
            });
          } else {
            this.setData({
              [`formData.${type}`]: imageUrl
            });
          }
          wx.showToast({
            title: '上传成功',
            icon: 'success'
          });
        } else {
          wx.showToast({
            title: data.message || '上传失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        wx.hideLoading();
        wx.showToast({
          title: '上传失败',
          icon: 'none'
        });
      }
    });
  },

  deleteWork(e) {
    const index = e.currentTarget.dataset.index;
    const works = [...this.data.formData.works];
    works.splice(index, 1);
    this.setData({
      'formData.works': works
    });
  },

  toggleStyle(e) {
    const style = e.currentTarget.dataset.style;
    const styles = [...this.data.formData.styles];
    const index = styles.indexOf(style);
    
    if (index > -1) {
      styles.splice(index, 1);
    } else {
      styles.push(style);
    }
    
    this.setData({
      'formData.styles': styles
    });
  },

  onPriceInput(e) {
    this.setData({
      'formData.price': e.detail.value
    });
  },

  toggleAgreement() {
    this.setData({
      'formData.agreed': !this.data.formData.agreed
    });
  },

  validateForm() {
    const { formData } = this.data;

    if (!formData.realName || formData.realName.trim() === '') {
      wx.showToast({
        title: '请输入真实姓名',
        icon: 'none'
      });
      return false;
    }

    if (!formData.idCard || formData.idCard.trim() === '') {
      wx.showToast({
        title: '请输入身份证号',
        icon: 'none'
      });
      return false;
    }

    const idCardRegex = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    if (!idCardRegex.test(formData.idCard)) {
      wx.showToast({
        title: '身份证号格式不正确',
        icon: 'none'
      });
      return false;
    }

    if (!formData.phone || formData.phone.trim() === '') {
      wx.showToast({
        title: '请输入手机号码',
        icon: 'none'
      });
      return false;
    }

    const phoneRegex = /^1[3-9]\d{9}$/;
    if (!phoneRegex.test(formData.phone)) {
      wx.showToast({
        title: '手机号码格式不正确',
        icon: 'none'
      });
      return false;
    }

    if (!formData.region || formData.region.trim() === '') {
      wx.showToast({
        title: '请输入所在城市',
        icon: 'none'
      });
      return false;
    }

    if (!formData.bio || formData.bio.trim() === '') {
      wx.showToast({
        title: '请输入个人简介',
        icon: 'none'
      });
      return false;
    }

    if (!formData.idCardFront) {
      wx.showToast({
        title: '请上传身份证正面',
        icon: 'none'
      });
      return false;
    }

    if (!formData.idCardBack) {
      wx.showToast({
        title: '请上传身份证反面',
        icon: 'none'
      });
      return false;
    }

    if (formData.styles.length === 0) {
      wx.showToast({
        title: '请至少选择一个擅长风格',
        icon: 'none'
      });
      return false;
    }

    if (formData.works.length < 3) {
      wx.showToast({
        title: '请至少上传3张作品照片',
        icon: 'none'
      });
      return false;
    }

    if (!formData.price || formData.price.trim() === '') {
      wx.showToast({
        title: '请输入起拍价格',
        icon: 'none'
      });
      return false;
    }

    if (!formData.agreed) {
      wx.showToast({
        title: '请阅读并同意入驻协议',
        icon: 'none'
      });
      return false;
    }

    return true;
  },

  submitApplication() {
    if (!this.validateForm()) {
      return;
    }

    const token = wx.getStorageSync('token');
    const userInfo = wx.getStorageSync('userInfo');

    if (!token || !userInfo) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }

    this.setData({
      submitting: true
    });

    const requestData = {
      userId: userInfo.id,
      realName: this.data.formData.realName,
      idNumber: this.data.formData.idCard,
      phone: this.data.formData.phone,
      styles: this.data.formData.styles.join(','),
      portfolio: JSON.stringify(this.data.formData.works)
    };

    wx.request({
      url: 'http://127.0.0.1:8086/admin/api/photographer/application/submit',
      method: 'POST',
      data: requestData,
      header: {
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
      },
      success: (res) => {
        this.setData({
          submitting: false
        });

        if (res.data.code === 200) {
          wx.showModal({
            title: '申请成功',
            content: '您的摄影师申请已提交，我们会在1-3个工作日内完成审核，请耐心等待',
            showCancel: false,
            success: () => {
              wx.navigateBack();
            }
          });
        } else {
          wx.showToast({
            title: res.data.message || '提交失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        this.setData({
          submitting: false
        });
        wx.showToast({
          title: '网络错误，请重试',
          icon: 'none'
        });
      }
    });
  }
});