import Request from './index.js'
import { apiConfig } from '@/config/global_config.ts'

let request = new Request().http

let userPerfix = apiConfig.user.perfix
let authPerfix = apiConfig.auth.perfix
let filePerfix = apiConfig.file.perfix
let blogPerfix = apiConfig.blog.perfix

function get(url, data, hideLoading, header) {
  return request({
    url: url,
    method: 'GET',
    data: data,
    header: header,
    hideLoading: hideLoading,
  })
}
 
function post(url, data, hideLoading, header) {
  return request({
    url: url,
    method: 'POST',
    data: data,
    header: header,
    hideLoading: hideLoading,
  })
}

function put(url, data, hideLoading, header) {
  return request({
    url: url,
    method: 'PUT',
    data: data,
    header: header,
    hideLoading: hideLoading,
  })
}

function del(url, data, hideLoading, header) {
  return request({
    url: url,
    method: 'DELETE',
    data: data,
    header: header,
    hideLoading: hideLoading,
  })
}

let api = {
  // AuthService
  login: data => post(authPerfix + '/auth/login', data, true),
  loginQRCode: data => get(authPerfix + '/auth/qrcode',data,true),
  loginQRCodeConfirm: data => post(authPerfix + '/auth/qrcode/confirm', data, true),
  loginQRCodeCheck: token => get(authPerfix + '/auth/qrcode/status', { token }, true),
  getVerifyCode: data => post(authPerfix + '/auth/verifyCode', data, true, {
    'Accept': 'image/png',
    'responseType': 'arraybuffer'
  }),
  logout: () => post(authPerfix + '/auth/logout',{},false),
  
  // BlogService
  getPostList: (pageNum = 1, pageSize = 6, categoryId = 0) => get(blogPerfix + '/post/list', { pageNum, pageSize, categoryId }, true),
  getPostDetail: id => get(blogPerfix + `/post/${id}`, {}, true),
  getUserPostList: () => get(blogPerfix + '/post/user/list', {}, true),
  deletePost: id => del(blogPerfix + `/post/${id}`, {}, false),
  addComment: data => post(blogPerfix + '/comment', data, false),
  updateComment: data => put(blogPerfix + '/comment', data, false),
  deleteComment: id => del(blogPerfix + '/comment', { id }, false),
  getCommentList: articleId => get(blogPerfix + `/comment/list/${articleId}`, {}, true),
  search: data => post(blogPerfix + '/search/articles', data, true),
  createNewPost: data => post(blogPerfix + '/post/create', data, true),
  getPostCount: () => get(blogPerfix + '/post/count', true),
  reportPost: data => post(blogPerfix + '/report', data, true),
  // UserService
  // 用户注册
  sendRegisterVerifyCode: email => post(userPerfix + `/register/code/${email}`,{},false),
  checkUserName: nickName => get(userPerfix+ `/register/check/${nickName}`,{},true),
  register: data => post(userPerfix + '/register',data,false),
  // 找回密码
  sendResetPasswordCode: email => post(userPerfix + `/reset/code/${email}`, {}, false),
  resetPassword: data => post(userPerfix + '/reset/password', data, false),
  // 用户信息
  getUserInfo: () => get(userPerfix + '/user/userinfo', {}, true),
  getCapacityInfo: userId => get(userPerfix + '/capacity/info', {userId},true),
  // 用户信息修改
  updateAvatar: data => post(userPerfix + '/user/avatar', data, false, {
    'Content-Type': 'multipart/form-data'
  }),
  updateNickName: data => post(userPerfix + '/user/nickname', data, true),
  updatePassWord: data => post(userPerfix + '/user/password', data, true),
  
  
  //FileService
  // 图床
  uploadImage: data => post(filePerfix + '/imagebed/images', data, false, {
    'Content-Type': 'multipart/form-data'
  }),
  deleteImage: id => del(filePerfix + `/imagebed/images/${id}`, {}, true),
  // 文件上传
  getPreUploadUrl: data => post(filePerfix + '/files/preUpload', data, false),
  confirmUpload: fileId => post(filePerfix + `/files/confirmUpload/${fileId}`, {}, false),
  checkUploadStatus: fileId => get(filePerfix + `/files/status/${fileId}`, {}, true),
  // 文件下载
  downloadFile: fileId => get(filePerfix + `/files/download/${fileId}`, {}, true),
  // 文件夹操作
  createFolder: data => post(filePerfix + '/files/folder', data, false),
  getAllFiles: () => get(filePerfix + '/files/all', {}, true),
  getFolderContent: virtualPath => get(filePerfix + '/files/folder', {virtualPath}, true),
  deleteFolder: folderId => del(filePerfix + `/files/folder/${folderId}`, {}, false),
  // 文件操作
  deleteFile: fileId => del(filePerfix + `/files/${fileId}`, {}, false),
  updateFileInfo: (fileId, data) => put(filePerfix + `/files/${fileId}`, data, false),
  // 分享
  createShare: data => post(filePerfix + '/shares', data, false),
  getShareFileInfo: shareId => get(filePerfix + `/shares/${shareId}`, {}, true),
  getShareDownloadUrl: data => post(filePerfix + '/shares/verify', data, false),
  
}
 
export default api;
