import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => {
    const userData = uni.getStorageSync('userData')
    return userData || {
    token: '',
    email: '',
    name: '',
    id: '',
    avatar: '',
    capacity: 0,
    capacityUsed: 0,
    redirectUrl: '',
    status: '0'
  }},

  getters: {
    isLoggedIn: (state) => !!state.token,
    getCapacityUsage: (state) => {
      if (!state.capacity) return 0
      return (state.capacityUsed / state.capacity) * 100
    }
  },

  actions: {
    setUserInfo(userInfo) {
      this.token = userInfo.token || this.token
      this.email = userInfo.email || this.email
      this.name = userInfo.name || this.name
      this.id = userInfo.id || this.id
      this.avatar = userInfo.avatar || this.avatar
      this.capacity = userInfo.capacity || this.capacity
      this.capacityUsed = userInfo.capacityUsed || this.capacityUsed
      this.status = userInfo.status || this.status
      
      // 保存到本地存储
      uni.setStorageSync('userData', {
        token: this.token,
        email: this.email,
        name: this.name,
        id: this.id,
        avatar: this.avatar,
        capacity: this.capacity,
        capacityUsed: this.capacityUsed,
        redirectUrl: this.redirectUrl,
        status: this.status
      })
    },

    clearUserInfo() {
      this.token = ''
      this.email = ''
      this.name = ''
      this.id = ''
      this.avatar = ''
      this.capacity = 0
      this.capacityUsed = 0
      this.redirectUrl = ''
      this.status = '0'
      
      // 清除本地存储
      uni.removeStorageSync('userData')
    },

    setRedirectUrl(url) {
      this.redirectUrl = url
      
      // 更新本地存储
      uni.setStorageSync('userData', {
        token: this.token,
        email: this.email,
        name: this.name,
        id: this.id,
        avatar: this.avatar,
        capacity: this.capacity,
        capacityUsed: this.capacityUsed,
        redirectUrl: this.redirectUrl,
        status: this.status
      })
    },

    getRedirectUrl() {
      const url = this.redirectUrl
      this.redirectUrl = ''
      
      // 更新本地存储中的redirectUrl
      uni.setStorageSync('userData', {
        token: this.token,
        email: this.email,
        name: this.name,
        id: this.id,
        avatar: this.avatar,
        capacity: this.capacity,
        capacityUsed: this.capacityUsed,
        redirectUrl: this.redirectUrl,
        status: this.status
      })
      return url
    }
  }
})