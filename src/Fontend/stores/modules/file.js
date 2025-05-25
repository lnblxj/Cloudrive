import { defineStore } from 'pinia'

export const useFileStore = defineStore('file', {
  state: () => ({
    fileData: uni.getStorageSync('fileData') || []
  }),

  getters: {
    getFileList: (state) => state.fileData,
    getFileById: (state) => (id) => state.fileData.find(file => file.id === id)
  },

  actions: {
    setFileData(files) {
      this.fileData = files
      uni.setStorageSync('fileData', this.fileData)
    },

    updateFile(file) {
      const index = this.fileData.findIndex(f => f.id === file.id)
      if (index !== -1) {
        this.fileData[index] = { ...this.fileData[index], ...file }
        uni.setStorageSync('fileData', this.fileData)
      }
    },

    addFile(file) {
      this.fileData.push(file)
      uni.setStorageSync('fileData', this.fileData)
    },

    removeFile(fileId) {
      const index = this.fileData.findIndex(f => f.id === fileId)
      if (index !== -1) {
        this.fileData.splice(index, 1)
        uni.setStorageSync('fileData', this.fileData)
      }
    },

    clearFileData() {
      this.fileData = []
      uni.setStorageSync('fileData', this.fileData)
    }
  }
})