// 作品详情页
const app = getApp()

Page({
  data: {
    workId: null,
    work: {
      id: 1,
      title: '夏日清新人像',
      images: [
        'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800',
        'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=800',
        'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800'
      ],
      publishTime: '2024-03-15',
      shootParams: ['Canon EOS R5', 'RF 85mm f/1.2', 'f/1.8 ISO200 1/200s'],
      likeCount: 328,
      collectCount: 156,
      commentCount: 42,
      photographer: {
        id: 1,
        name: '摄影师小明',
        avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200',
        description: '专注人像摄影5年，擅长日系清新风格',
        isVerified: true,
        isFollowing: false
      }
    },
    comments: [
      {
        id: 1,
        nickname: '小美',
        avatar: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200',
        content: '拍得真好！色调太美了，请问是用什么滤镜调色的？',
        time: '2小时前',
        likeCount: 12,
        isLiked: false
      },
      {
        id: 2,
        nickname: '摄影爱好者',
        avatar: 'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=200',
        content: '构图很棒，光线运用得恰到好处，学习了！',
        time: '5小时前',
        likeCount: 8,
        isLiked: true
      },
      {
        id: 3,
        nickname: '阳光女孩',
        avatar: 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200',
        content: '想约拍这种风格的，怎么联系您？',
        time: '昨天',
        likeCount: 3,
        isLiked: false
      }
    ],
    currentImageIndex: 0,
    isLiked: false,
    isCollected: false,
    isScrolled: false,
    isInputFocus: false,
    commentText: '',
    replyTo: null,
    showMorePopup: false,
    // 分页相关
    page: 1,
    pageSize: 10,
    hasMoreComments: true,
    isLoadingComments: false,
    // 排序相关
    sortType: 'latest', // latest: 最新, hottest: 最热
    // 表情相关
    showEmojiPicker: false,
    emojis: ['😀', '😃', '😄', '😁', '😆', '😅', '😂', '🤣', '😊', '😇', '🙂', '🙃', '😉', '😌', '😍', '🥰', '😘', '😗', '😙', '😚', '😋', '😛', '😝', '😜', '🤪', '🤨', '🧐', '🤓', '😎', '🤩', '🥳', '😏', '😒', '😞', '😔', '😟', '😕', '🙁', '☹️', '😣', '😖', '😫', '😩', '🥺', '😢', '😠', '😡', '🤬', '😳', '🥵', '🥶', '😱', '😨', '😰', '😥', '😓', '🤗', '🤔', '🤭', '🤫', '🤥', '😶', '😐', '😑', '😬', '🙄', '😯', '😦', '😧', '😮', '😲', '🥱', '😴', '🤤', '😪', '😵', '🤐', '🥴', '🤢', '🤮', '🤧', '😷', '🤒', '🤕'],
    // 评论字数限制
    maxCommentLength: 200
  },

  onLoad(options) {
    // 获取作品ID
    const workId = options.id || options.workId
    if (workId) {
      this.setData({ workId })
      this.loadWorkDetail(workId)
      this.loadComments(workId)
    }
    
    // 检查用户是否已点赞/收藏
    this.checkUserInteraction()
  },

  onShow() {
    // 页面显示时刷新数据
    if (this.data.workId) {
      // 只刷新评论和互动状态，避免重复加载作品详情和摄影师信息
      this.loadComments(this.data.workId)
      this.checkUserInteraction()
    }
  },

  onPageScroll(e) {
    // 监听滚动，控制导航栏样式
    const isScrolled = e.scrollTop > 100
    if (isScrolled !== this.data.isScrolled) {
      this.setData({ isScrolled })
    }
  },

  // 加载作品详情
  loadWorkDetail(workId) {
    console.log('加载作品详情，workId:', workId)
    const userInfo = app.globalData.userInfo;
    const token = app.globalData.token || wx.getStorageSync('token')
    // 调用API获取作品详情
    wx.request({
      url: `${app.globalData.baseUrl}/api/content/info`,
      method: 'GET',
      data: { id: workId },
      header: {
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        console.log('获取作品详情成功，响应', res)
        // 后端返回标准化响应格式，从data字段获取作品数据
        // 将coverImage转换为images数组，以符合前端期望的数据结构
        const work = res.data.data
        console.log('作品数据:', work)
        if (work) {
          work.images = [work.coverImage]
          console.log('转换后的images:', work.images)
          
          // 添加publishTime字段，使用createTime
          work.publishTime = work.createTime ? work.createTime.substring(0, 10) : ''
          
          // 添加shootParams字段，暂时使用空数组
          work.shootParams = []
          
          // 处理标签数据，后端返回的tagList是标签名称数组
          work.tags = work.tagList || []
          
          // 获取评论数
          wx.request({
            url: `${app.globalData.baseUrl}/api/comment/count`,
            method: 'GET',
            data: { contentId: workId },
            header: {
              'Authorization': token ? `Bearer ${token}` : ''
            },
            success: (commentRes) => {
              if (commentRes.data && commentRes.data.code === 200 && commentRes.data.data) {
                work.commentCount = commentRes.data.data.count;
              } else {
                work.commentCount = 0;
              }
              
              // 获取点赞数
              wx.request({
                url: `${app.globalData.baseUrl}/api/like/content/status`,
                method: 'GET',
                data: {
                  userId: userInfo?.id,
                  contentId: workId
                },
                header: {
                  'Authorization': token ? `Bearer ${token}` : ''
                },
                success: (likeRes) => {
                  if (likeRes.data && likeRes.data.code === 200 && likeRes.data.data) {
                    work.likeCount = likeRes.data.data.likeCount;
                  } else {
                    work.likeCount = 0;
                  }
                  
                  // 获取收藏数
                  wx.request({
                    url: `${app.globalData.baseUrl}/api/collection/count`,
                    method: 'GET',
                    data: { contentId: workId },
                    header: {
                      'Authorization': token ? `Bearer ${token}` : ''
                    },
                    success: (collectRes) => {
                      if (collectRes.data && collectRes.data.code === 200 && collectRes.data.data) {
                        work.collectCount = collectRes.data.data.count;
                      } else {
                        work.collectCount = 0;
                      }
                      
                      this.setData({ work });
                      console.log('设置work数据成功');
                      
                      // 根据userId获取摄影师信息
                      this.loadPhotographerInfo(work.userId);
                    }
                  });
                }
              });
            }
          });
        }
      },
      fail: (err) => {
        console.error('获取作品详情失败:', err)
      }
    })
  },

  // 加载评论列表
  loadComments(workId, isLoadMore = false) {
    const userInfo = app.globalData.userInfo;
    const userId = userInfo?.id;
    const token = app.globalData.token || wx.getStorageSync('token');
    
    if (isLoadMore && (!this.data.hasMoreComments || this.data.isLoadingComments)) {
      return;
    }
    
    const page = isLoadMore ? this.data.page + 1 : 1;
    
    this.setData({ isLoadingComments: true });
    
    // 调用API获取评论列表
    wx.request({
      url: `${app.globalData.baseUrl}/api/comment/list`,
      method: 'GET',
      data: {
        contentId: workId,
        userId: userId,
        page: page,
        pageSize: this.data.pageSize,
        sort: this.data.sortType === 'hottest' ? 'like' : 'time'
      },
      header: {
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        if (res.data && res.data.code === 200 && res.data.data) {
          const newComments = res.data.data.map(comment => ({
            id: comment.id,
            nickname: comment.nickname,
            avatar: comment.avatar,
            content: comment.content,
            time: this.formatTime(comment.createTime),
            likeCount: comment.likeCount,
            isLiked: comment.isLiked,
            isOwner: comment.userId === userInfo?.id,
            replies: comment.replies ? comment.replies.map(reply => ({
              replyId: reply.id,
              replyNickname: reply.nickname,
              replyContent: reply.content,
              replyTime: this.formatTime(reply.createTime)
            })) : []
          }));
          
          const comments = isLoadMore ? [...this.data.comments, ...newComments] : newComments;
          const hasMoreComments = newComments.length === this.data.pageSize;
          
          this.setData({
            comments,
            page,
            hasMoreComments,
            isLoadingComments: false
          });
        } else {
          this.setData({
            hasMoreComments: false,
            isLoadingComments: false
          });
        }
      },
      fail: (err) => {
        console.error('获取评论列表失败:', err);
        this.setData({ isLoadingComments: false });
      }
    });
  },
  
  // 格式化时间
  formatTime(timeStr) {
    if (!timeStr) return '';
    const now = new Date();
    const commentTime = new Date(timeStr);
    const diff = now - commentTime;
    
    const minutes = Math.floor(diff / 60000);
    if (minutes < 1) return '刚刚';
    
    const hours = Math.floor(diff / 3600000);
    if (hours < 1) return `${minutes}分钟前`;
    
    const days = Math.floor(diff / 86400000);
    if (days < 1) return `${hours}小时前`;
    
    if (days < 7) return `${days}天前`;
    
    return commentTime.toLocaleDateString('zh-CN');
  },

  // 根据用户ID获取摄影师信息
  loadPhotographerInfo(userId) {
    if (!userId) {
      console.log('用户ID为空，无法获取摄影师信息')
      return
    }
    
    console.log('根据用户ID获取摄影师信息，userId:', userId)
    const token = app.globalData.token || wx.getStorageSync('token');
    
    wx.request({
      url: `${app.globalData.baseUrl}/api/photographer/user`,
      method: 'GET',
      data: { userId: userId },
      header: {
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        console.log('获取摄影师信息成功', res)
        if (res.data && res.data.code === 200 && res.data.data) {
          const photographer = res.data.data
          // 更新作品数据中的摄影师信息
          const work = this.data.work
          work.photographer = {
            id: photographer.id,  // 使用摄影师ID
            userId: userId,       // 保存用户ID
            name: photographer.name || '摄影师',
            avatar: photographer.avatar || 'https://api.dicebear.com/7.x/personas/svg?seed=photographer&size=100',
            description: photographer.bio || '专注摄影多年',
            isVerified: photographer.certified === 1,
            isFollowing: false
          }
          this.setData({ work })
          console.log('摄影师信息设置成功', work.photographer)
        } else {
          // 如果获取失败，使用默认数据，不设置id字段
          const work = this.data.work
          work.photographer = {
            // 不设置id字段，这样goToUserProfile会跳转到普通用户个人主页
            userId: userId,
            name: '用户',
            avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=user&size=100',
            description: '普通用户',
            isVerified: false,
            isFollowing: false
          }
          this.setData({ work })
        }
      },
      fail: (err) => {
        console.error('获取摄影师信息失败', err)
        // 使用默认数据，不设置id字段
        const work = this.data.work
        work.photographer = {
          // 不设置id字段，这样goToUserProfile会跳转到普通用户个人主页
          userId: userId,
          name: '用户',
          avatar: 'https://api.dicebear.com/7.x/personas/svg?seed=user&size=100',
          description: '普通用户',
          isVerified: false,
          isFollowing: false
        }
        this.setData({ work })
      }
    })
  },

  // 检查用户互动状态
  checkUserInteraction() {
    const userInfo = app.globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      return;
    }
    
    const token = app.globalData.token || wx.getStorageSync('token');
    
    // 检查是否已点赞
    wx.request({
      url: `${app.globalData.baseUrl}/api/like/content/status`,
      method: 'GET',
      data: {
        userId: userInfo.id,
        contentId: this.data.workId
      },
      header: {
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        if (res.data && res.data.code === 200 && res.data.data) {
          this.setData({
            isLiked: res.data.data.isLiked || false,
            'work.likeCount': res.data.data.likeCount || 0
          });
        }
      }
    });
    
    // 检查是否已收藏
    wx.request({
      url: `${app.globalData.baseUrl}/api/collection/status`,
      method: 'GET',
      data: {
        userId: userInfo.id,
        contentId: this.data.workId
      },
      header: {
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        if (res.data && res.data.code === 200 && res.data.data) {
          this.setData({
            isCollected: res.data.data.isCollected || false,
            'work.collectCount': this.data.work.collectCount || 0
          });
        }
      }
    });
  },

  // 图片切换
  onImageChange(e) {
    this.setData({
      currentImageIndex: e.detail.current
    })
  },

  // 预览图片
  previewImage(e) {
    const index = e.currentTarget.dataset.index
    wx.previewImage({
      current: this.data.work.images[index],
      urls: this.data.work.images
    })
  },

  // 返回上一页
  goBack() {
    wx.navigateBack()
  },

  // 跳转到用户主页（摄影师或普通用户）
  goToUserProfile() {
    const photographer = this.data.work.photographer
    if (!photographer) {
      wx.showToast({
        title: '用户信息加载中',
        icon: 'none'
      })
      return
    }
    
    // 优先使用摄影师ID
    if (photographer.id) {
      wx.navigateTo({
        url: `/pages/photographer/detail?id=${photographer.id}`
      })
    } else if (photographer.userId) {
      // 如果有userId但没有id，尝试重新加载摄影师信息
      wx.showToast({
            title: '加载用户信息..',
            icon: 'loading'
          })
      
      // 重新调用API获取摄影师信息
      wx.request({
        url: `${app.globalData.baseUrl}/api/photographer/user`,
        method: 'GET',
        data: { userId: photographer.userId },
        success: (res) => {
          wx.hideToast()
          if (res.data && res.data.code === 200 && res.data.data) {
            const photographerData = res.data.data
            // 更新摄影师信息
            const work = this.data.work
            work.photographer = {
              id: photographerData.id,
              userId: photographer.userId,
              name: photographerData.name || '摄影师',
              avatar: photographerData.avatar || 'https://api.dicebear.com/7.x/personas/svg?seed=photographer&size=100',
              description: photographerData.bio || '专注摄影多年',
              isVerified: photographerData.certified === 1,
              isFollowing: false
            }
            this.setData({ work })
            
            // 使用获取到的摄影师ID跳转
            wx.navigateTo({
              url: `/pages/photographer/detail?id=${photographerData.id}`
            })
          } else {
            // API返回错误，跳转到普通用户个人主页
            wx.navigateTo({
              url: `/pages/profile/index?userId=${photographer.userId}`
            })
          }
        },
        fail: (err) => {
          wx.hideToast()
          console.error('获取用户信息失败', err)
          // 跳转到普通用户个人主页
          wx.navigateTo({
            url: `/pages/profile/index?userId=${photographer.userId}`
          })
        }
      })
    } else {
      wx.showToast({
        title: '无法获取用户信息',
        icon: 'none'
      })
    }
  },

  // 切换关注状态
  toggleFollow() {
    const photographer = this.data.work.photographer
    const newStatus = !photographer.isFollowing
    
    // 调用API关注/取消关注
    // wx.request({
    //   url: `${app.globalData.baseUrl}/user/follow`,
    //   method: 'POST',
    //   data: {
    //     photographerId: photographer.id,
    //     action: newStatus ? 'follow' : 'unfollow'
    //   },
    //   success: (res) => {
    //     if (res.data.code === 200) {
    //       this.setData({
    //         'work.photographer.isFollowing': newStatus
    //       })
    //     }
    //   }
    // })
    
    // 模拟操作
    this.setData({
      'work.photographer.isFollowing': newStatus
    })
    
    wx.showToast({
      title: newStatus ? '关注成功' : '已取消关注',
      icon: 'none'
    })
  },

  // 切换点赞状态
  toggleLike() {
    const userInfo = app.globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }
    
    const newStatus = !this.data.isLiked;
    const token = app.globalData.token || wx.getStorageSync('token');
    
    // 调用API点赞/取消点赞
    wx.request({
      url: `${app.globalData.baseUrl}/api/like/content`,
      method: 'POST',
      data: {
        userId: userInfo.id,
        contentId: this.data.workId,
        liked: newStatus
      },
      header: {
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        if (res.data && res.data.code === 200 && res.data.data) {
          this.setData({
            isLiked: newStatus,
            'work.likeCount': res.data.data.likeCount
          });
          wx.showToast({
            title: newStatus ? '点赞成功' : '已取消点赞',
            icon: 'none'
          });
        } else {
          wx.showToast({
            title: res.data?.message || '操作失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        console.error('点赞操作失败:', err);
        wx.showToast({
          title: '网络错误，请重试',
          icon: 'none'
        });
      }
    });
  },

  // 切换收藏状态
  toggleCollect() {
    const userInfo = app.globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }
    
    const newStatus = !this.data.isCollected;
    const token = app.globalData.token || wx.getStorageSync('token');
    
    // 调用API收藏/取消收藏
    wx.request({
      url: `${app.globalData.baseUrl}/api/collection/toggle`,
      method: 'POST',
      data: {
        userId: userInfo.id,
        contentId: this.data.workId,
        collected: newStatus
      },
      header: {
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        if (res.data && res.data.code === 200 && res.data.data) {
          this.setData({
            isCollected: newStatus,
            'work.collectCount': res.data.data.collectionCount
          });
          wx.showToast({
            title: newStatus ? '收藏成功' : '已取消收藏',
            icon: 'none'
          });
        } else {
          wx.showToast({
            title: res.data?.message || '操作失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        console.error('收藏操作失败:', err);
        wx.showToast({
          title: '网络错误，请重试',
          icon: 'none'
        });
      }
    });
  },

  // 分享
  handleShare() {
    // 显示分享菜单
    wx.showShareMenu({
      withShareTicket: true,
      menus: ['shareAppMessage', 'shareTimeline']
    })
  },

  // 举报
  handleReport() {
    wx.showActionSheet({
      itemList: ['色情低俗', '政治敏感', '广告骚扰', '侵权投诉', '其他'],
      success: (res) => {
        const reasonIndex = res.tapIndex;
        const reasons = ['色情低俗', '政治敏感', '广告骚扰', '侵权投诉', '其他'];
        const reason = reasons[reasonIndex];
        
        wx.showModal({
          title: '确认举报',
          content: '确定要举报该作品吗？',
          success: (modalRes) => {
            if (modalRes.confirm) {
              // 获取用户信息
              const userInfo = app.globalData.userInfo;
              if (!userInfo || !userInfo.id) {
                wx.showToast({
                  title: '请先登录',
                  icon: 'none'
                });
                return;
              }
              
              const token = app.globalData.token || wx.getStorageSync('token');
              
              // 调用举报API
              wx.request({
                url: `${app.globalData.baseUrl}/api/report/create`,
                method: 'POST',
                data: {
                  reporterId: userInfo.id,
                  targetId: this.data.workId,
                  type: 1, // 1-内容
                  reason: reason
                },
                header: {
                  'Authorization': token ? `Bearer ${token}` : ''
                },
                success: (res) => {
                  if (res.data && res.data.code === 200) {
                    wx.showToast({
                      title: '举报已提交',
                      icon: 'success'
                    });
                  } else {
                    wx.showToast({
                      title: res.data?.message || '举报失败，请重试',
                      icon: 'none'
                    });
                  }
                },
                fail: (err) => {
                  console.error('举报失败:', err);
                  wx.showToast({
                    title: '网络错误，请重试',
                    icon: 'none'
                  });
                }
              });
            }
          }
        });
      }
    });
  },

  // 显示更多选项
  showMoreOptions() {
    this.setData({ showMorePopup: true })
  },

  // 隐藏更多选项
  hideMorePopup() {
    this.setData({ showMorePopup: false })
  },

  // 阻止冒泡
  preventBubble() {
    // 什么都不做，阻止事件冒泡
  },

  // 分享给好友
  shareToFriend() {
    this.hideMorePopup()
    this.handleShare()
  },

  // 复制链接
  copyLink() {
    const link = `https://your-domain.com/work/${this.data.workId}`
    wx.setClipboardData({
      data: link,
      success: () => {
        wx.showToast({
          title: '链接已复制',
          icon: 'success'
        })
        this.hideMorePopup()
      }
    })
  },

  // 输入框获得焦点
  onInputFocus() {
    this.setData({ isInputFocus: true })
  },

  // 输入框失去焦点
  onInputBlur() {
    this.setData({ isInputFocus: false })
  },

  // 评论输入
  onCommentInput(e) {
    console.log('输入事件触发:', e);
    let commentText = e.detail.value;
    console.log('输入内容:', commentText);
    if (commentText.length > this.data.maxCommentLength) {
      commentText = commentText.substring(0, this.data.maxCommentLength);
    }
    this.setData({
      commentText
    }, () => {
      console.log('数据更新成功:', this.data.commentText);
    })
  },

  // 回复评论
  replyComment(e) {
    const { id, nickname } = e.currentTarget.dataset
    this.setData({
      replyTo: nickname,
      isInputFocus: true
    })
  },

  // 点赞评论
  likeComment(e) {
    const userInfo = app.globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }
    
    const commentId = e.currentTarget.dataset.id;
    const comment = this.data.comments.find(c => c.id === commentId);
    if (!comment) return;
    
    const newStatus = !comment.isLiked;
    const token = app.globalData.token || wx.getStorageSync('token');
    
    // 调用API点赞/取消点赞评论
    wx.request({
      url: `${app.globalData.baseUrl}/api/like/comment`,
      method: 'POST',
      data: {
        userId: userInfo.id,
        commentId: commentId,
        liked: newStatus
      },
      header: {
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        if (res.data && res.data.code === 200 && res.data.data) {
          const comments = this.data.comments.map(c => {
            if (c.id === commentId) {
              return {
                ...c,
                isLiked: newStatus,
                likeCount: res.data.data.likeCount
              }
            }
            return c
          });
          this.setData({ comments });
        } else {
          wx.showToast({
            title: res.data?.message || '操作失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        console.error('评论点赞操作失败:', err);
        wx.showToast({
          title: '网络错误，请重试',
          icon: 'none'
        });
      }
    });
  },

  // 发送评论
  sendComment() {
    const userInfo = app.globalData.userInfo;
    if (!userInfo || !userInfo.id) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }
    
    const { commentText, replyTo } = this.data
    
    if (!commentText.trim()) {
      wx.showToast({
        title: '请输入评论内容',
        icon: 'none'
      })
      return
    }
    
    const token = app.globalData.token || wx.getStorageSync('token');
    
    // 调用API发送评论
    wx.request({
      url: `${app.globalData.baseUrl}/api/comment/add`,
      method: 'POST',
      data: {
        userId: userInfo.id,
        contentId: this.data.workId,
        content: replyTo ? `回复 ${replyTo}：${commentText}` : commentText
      },
      header: {
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        if (res.data && res.data.code === 200) {
          // 重新加载评论列表
          this.loadComments(this.data.workId);
          this.setData({
            commentText: '',
            replyTo: null
          });
          wx.showToast({
            title: '评论成功',
            icon: 'success'
          });
        } else {
          wx.showToast({
            title: res.data?.message || '评论失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        console.error('发送评论失败:', err);
        wx.showToast({
          title: '网络错误，请重试',
          icon: 'none'
        });
      }
    });
  },

  // 切换表情选择器
  toggleEmojiPicker() {
    this.setData({
      showEmojiPicker: !this.data.showEmojiPicker
    });
  },
  
  // 选择表情
  selectEmoji(e) {
    const emoji = e.currentTarget.dataset.emoji;
    this.setData({
      commentText: this.data.commentText + emoji,
      showEmojiPicker: false
    });
  },
  
  // 加载更多评论
  loadMoreComments() {
    if (this.data.workId) {
      this.loadComments(this.data.workId, true);
    }
  },
  
  // 切换评论排序类型
  changeSortType(e) {
    const sortType = e.currentTarget.dataset.type;
    if (sortType !== this.data.sortType) {
      this.setData({
        sortType,
        page: 1,
        hasMoreComments: true
      });
      if (this.data.workId) {
        this.loadComments(this.data.workId);
      }
    }
  },
  
  // 删除评论
  deleteComment(e) {
    const commentId = e.currentTarget.dataset.id;
    const userInfo = app.globalData.userInfo;
    
    if (!userInfo || !userInfo.id) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }
    
    wx.showModal({
      title: '确认删除',
      content: '确定要删除这条评论吗？',
      success: (res) => {
        if (res.confirm) {
          const token = app.globalData.token || wx.getStorageSync('token');
          
          // 调用API删除评论
          wx.request({
            url: `${app.globalData.baseUrl}/api/comment/delete`,
            method: 'POST',
            data: {
              id: commentId,
              userId: userInfo.id
            },
            header: {
              'Authorization': token ? `Bearer ${token}` : ''
            },
            success: (res) => {
              if (res.data && res.data.code === 200) {
                // 重新加载评论列表
                this.loadComments(this.data.workId);
                // 更新评论数
                this.setData({
                  'work.commentCount': this.data.work.commentCount - 1
                });
                wx.showToast({
                  title: '删除成功',
                  icon: 'success'
                });
              } else {
                wx.showToast({
                  title: res.data?.message || '删除失败',
                  icon: 'none'
                });
              }
            },
            fail: (err) => {
              console.error('删除评论失败:', err);
              wx.showToast({
                title: '网络错误，请重试',
                icon: 'none'
              });
            }
          });
        }
      }
    });
  },
  
  // 举报评论
  reportComment(e) {
    const commentId = e.currentTarget.dataset.id;
    const userInfo = app.globalData.userInfo;
    
    if (!userInfo || !userInfo.id) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }
    
    wx.showActionSheet({
      itemList: ['色情低俗', '政治敏感', '广告骚扰', '侵权投诉', '其他'],
      success: (res) => {
        const reasonIndex = res.tapIndex;
        const reasons = ['色情低俗', '政治敏感', '广告骚扰', '侵权投诉', '其他'];
        const reason = reasons[reasonIndex];
        
        wx.showModal({
          title: '确认举报',
          content: '确定要举报这条评论吗？',
          success: (modalRes) => {
            if (modalRes.confirm) {
              const token = app.globalData.token || wx.getStorageSync('token');
              
              // 调用举报API
              wx.request({
                url: `${app.globalData.baseUrl}/api/report/create`,
                method: 'POST',
                data: {
                  reporterId: userInfo.id,
                  targetId: commentId,
                  type: 2, // 2-评论
                  reason: reason
                },
                header: {
                  'Authorization': token ? `Bearer ${token}` : ''
                },
                success: (res) => {
                  if (res.data && res.data.code === 200) {
                    wx.showToast({
                      title: '举报已提交',
                      icon: 'success'
                    });
                  } else {
                    wx.showToast({
                      title: res.data?.message || '举报失败，请重试',
                      icon: 'none'
                    });
                  }
                },
                fail: (err) => {
                  console.error('举报失败:', err);
                  wx.showToast({
                    title: '网络错误，请重试',
                    icon: 'none'
                  });
                }
              });
            }
          }
        });
      }
    });
  },

  // 用户点击右上角分享
  onShareAppMessage() {
    return {
      title: this.data.work.title,
      path: `/pages/content/detail?id=${this.data.workId}`,
      imageUrl: this.data.work.images[0]
    }
  },

  // 用户点击右上角分享到朋友圈
  onShareTimeline() {
    return {
      title: this.data.work.title,
      query: `id=${this.data.workId}`,
      imageUrl: this.data.work.images[0]
    }
  }
})

