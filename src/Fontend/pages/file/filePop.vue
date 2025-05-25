<template>
	<view v-if="isVisible" class="file-pop-modal">
		<view class="file-pop-overlay" @click="closeModal"></view>
		
		<!-- 重命名文件 -->
		<view v-if="popType === 'rename'" class="file-pop-content">
			<view class="pop-container">
				<view class="pop-title">重命名文件</view>
				<view class="pop-content">
					<view class="input-group">
						<text class="input-label">文件名</text>
						<input class="pop-input" v-model="inputValue" placeholder="请输入新文件名" />
					</view>
				</view>
				<view class="pop-footer">
					<button class="pop-btn pop-btn-cancel" @click="closeModal">取消</button>
					<button class="pop-btn pop-btn-confirm" @click="handleConfirm">确认</button>
				</view>
			</view>
		</view>
		
		<!-- 新建文件夹 -->
		<view v-if="popType === 'newFolder'" class="file-pop-content">
			<view class="pop-container">
				<view class="pop-title">新建文件夹</view>
				<view class="pop-content">
					<view class="input-group">
						<text class="input-label">文件夹名</text>
						<input class="pop-input" v-model="inputValue" placeholder="请输入文件夹名" />
					</view>
				</view>
				<view class="pop-footer">
					<button class="pop-btn pop-btn-cancel" @click="closeModal">取消</button>
					<button class="pop-btn pop-btn-confirm" @click="handleConfirm">确认</button>
				</view>
			</view>
		</view>
		
		<!-- 上传文件 -->
		<view v-if="popType === 'upload'" class="file-pop-content">
			<view class="pop-container">
				<view class="pop-title">上传文件</view>
				<view class="pop-content">
					<!-- #ifdef WEB -->
					<view class="upload-area" @click="selectFile">
						<l-icon name="upload" size="80rpx" color="#999"></l-icon>
						<text class="upload-text">点击选择文件</text>
					</view>
					<view v-if="uploadInfo.fileName" class="upload-info">
						<text class="file-name">{{uploadInfo.fileName}}</text>
						<view class="progress-container">
							<view class="progress-bar">
								<view class="progress-inner" :style="{width: uploadInfo.progress + '%'}"></view>
							</view>
							<text class="progress-text">{{uploadInfo.progress}}%</text>
						</view>
						<text class="status-text" v-if="uploadInfo.status">
							{{ uploadInfo.status === 'checking' ? '文件校验中...' : 
							   uploadInfo.status === 'uploading' ? '文件上传中...' : 
							   uploadInfo.status === 'verifying' ? '等待服务端校验...' : 
							   uploadInfo.status === 'success' ? '上传成功' : '' }}
						</text>
					</view>
					<!-- #endif -->
					<!-- #ifdef APP -->
					<text>截至2025年4月20日，Uniapp 仍未提供可靠的APP端的分块上传方案</text>
					<u-gap></u-gap>
					<text>尝试在APP端直接切割文件，使用uni request上传后不明原因导致切片整合的文件损坏</text>
					<u-gap></u-gap>
					<text>尝试了使用外挂webview的方式实现，但过于臃肿索性关闭了APP端的上传功能,后续将考虑开发原生插件实现</text>
					<u-gap />
					<text style="color: red;">上传文件请使用网页端</text>
					<!-- #endif -->
				</view>
				<view class="pop-footer">
					<button class="pop-btn pop-btn-cancel" @click="closeModal">取消</button>
					<!-- #ifdef WEB -->
					<button class="pop-btn pop-btn-confirm" :disabled="!uploadInfo.fileName" @click="handleConfirm">上传</button>
					<!-- #endif -->
				</view>
			</view>
		</view>
		
		<!-- 分享文件 -->
		<view v-if="popType === 'share'" class="file-pop-content">
			<view class="pop-container">
				<view class="pop-title">分享文件</view>
				<view class="pop-content">
					<view v-if="!shareInfo.link" class="share-setup">
						<view class="file-info">
							<text class="file-info-label">文件名：</text>
							<text class="file-info-value">{{fileInfo?.name || '未选择文件'}}</text>
						</view>
						<view class="input-group">
							<text class="input-label">提取密码</text>
							<input class="pop-input" v-model="shareInfo.password" placeholder="留空则无需密码" />
						</view>
					</view>
					<view v-else class="share-result">
						<view class="share-item">
							<text class="share-label">分享链接：</text>
							<view class="share-value-container">
								<text class="share-value">{{shareInfo.link}}</text>
								<button class="copy-btn" @click="copyText(shareInfo.link)">复制</button>
							</view>
						</view>
						<view v-if="shareInfo.password" class="share-item">
							<text class="share-label">提取密码：</text>
							<view class="share-value-container">
								<text class="share-value">{{shareInfo.password}}</text>
								<button class="copy-btn" @click="copyText(shareInfo.password)">复制</button>
							</view>
						</view>
					</view>
				</view>
				<view class="pop-footer">
					<u-button @click="closeModal" class="pop-btn pop-btn-cancel">关闭</u-button>
					<u-button  class="pop-btn pop-btn-confirm" v-if="!shareInfo.link" @click="handleConfirm" type="primary">生成链接</u-button>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import { ref, defineProps, defineEmits, computed, getCurrentInstance, onMounted } from 'vue';
	import { useUserStore } from '@/stores/modules/user';
	import { sha256 } from 'js-sha256';
	const {proxy} = getCurrentInstance()
	const userStore = useUserStore()
	const props = defineProps({
		type: {
			type: String,
			default: 'rename'
		},
		fileInfo: {
			type: Object,
			default: () => null
		},
		virtualPath: {
			type: String,
			default: '/'
		}
	});
	
	const emit = defineEmits(['confirm', 'cancel']);
	const isVisible = ref(false);
	const popType = ref(props.type);
	const inputValue = ref('');
	
	// 上传信息
	const uploadInfo = ref({
		fileName: '',
		fileSize: 0,
		filePath: '',
		progress: 0,
		sha256: '',
		status: '',
		fileId: ''
	});
	
	// 分享信息
	const shareInfo = ref({
		link: '',
		password: ''
	});
	
	
	// 打开弹窗
	function openModal(type, file = null) {
		popType.value = type || props.type;
		inputValue.value = '';
		uploadInfo.value = { fileName: '', fileSize: 0, filePath: '', progress: 0 };
		shareInfo.value = { link: '', password: '' };
		
		// 如果是重命名操作且有文件信息，则预填充文件名
		if (type === 'rename' && file && file.name) {
			inputValue.value = file.name;
		}
		isVisible.value = true;
	}
	
	// 关闭弹窗
	function closeModal() {
		isVisible.value = false;
		emit('cancel');
	}
	
	// 处理确认操作
	function handleConfirm() {
		switch (popType.value) {
			case 'rename':
				// 重命名操作
				proxy.$http.updateFileInfo(props.fileInfo.id, {
					fileId: props.fileInfo.id,
					newFileName: inputValue.value,
					newVirtualPath: props.virtualPath
				}).then(res => {
					if (res.isSuccess) {
						emit('confirm', {
							type: 'rename',
							data: {
								fileInfo: res.data,
								newName: inputValue.value
							}
						});
						uni.showToast({
							title: '重命名成功',
							icon: 'success'
						});
						closeModal();
					}
				}).catch(err => {
					uni.showToast({
						title: '重命名失败',
						icon: 'none'
					});
				});
				break;
				
			case 'newFolder':
				// 新建文件夹操作
				proxy.$http.createFolder({
					virtualPath: props.virtualPath,
					folderName: inputValue.value
				}).then(res => {
					if (res.isSuccess) {
						emit('confirm', {
							type: 'newFolder',
							data: {
								path: props.virtualPath,
								folderName: inputValue.value
							}
						});
						uni.showToast({
							title: '文件夹创建成功',
							icon: 'success'
						});
						closeModal()
					}
				}).catch(err => {
					uni.showToast({
						title: '文件夹创建失败',
						icon: 'none'
					});
				});
				break;
				
			case 'upload':
				// 上传文件操作
				if (!uploadInfo.value.fileName) {
					uni.showToast({
						title: '请先选择文件',
						icon: 'none'
					});
					return;
				}
				
				// 上传流程
				uploadInfo.value.status = 'checking';
				calculateSHA256().then(() => {
					return preUploadFile();
				}).then(() => {
					return uploadFile();
				}).then(() => {
					return confirmUploadFile();
				}).then(() => {
					return checkFileStatus();
				}).catch(err => {
					console.error('上传过程出错:', err);
					uni.showToast({
						title: '上传失败: ' + (err.message || '未知错误'),
						icon: 'none'
					});
				});
				
				// 通知父组件上传开始
				emit('confirm', {
					type: 'upload',
					data: {
						path: props.virtualPath,
						fileInfo: uploadInfo.value
					}
				});
				break;
				
			case 'share':
				// 分享文件操作
				if (!shareInfo.value.link) {
					proxy.$http.createShare({
						fileId: props.fileInfo.id,
						nickName: userStore.name,
						needPassword: !!shareInfo.value.password,
						password: shareInfo.value.password || ''
					}).then(res => {
						if (res.isSuccess) {
							shareInfo.value.link = `${window.location.origin}/#/pages/share/share?id=${res.data.id}`;
							emit('confirm', {
								type: 'share',
								data: {
									fileInfo: props.fileInfo,
									password: shareInfo.value.password,
									link: shareInfo.value.link
								}
							});
						}
					}).catch(err => {
						uni.showToast({
							title: '分享失败',
							icon: 'none'
						});
					});
				}
				break;
		}
	}
	
	// 选择文件
	function selectFile() {
		// 重置上传信息
		uploadInfo.value = {
			fileName: '',
			fileSize: 0,
			filePath: '',
			progress: 0,
			sha256: '',
			status: '',
			fileId: ''
		};
		
		// #ifdef WEB
		uni.chooseFile({
			count: 1,
			extension: ['*'],
			success: (res) => {
				if (res.tempFiles && res.tempFiles.length > 0) {
					const file = res.tempFiles[0];
					uploadInfo.value.fileName = file.name;
					uploadInfo.value.fileSize = file.size;
					uploadInfo.value.filePath = file;
					uploadInfo.value.progress = 0;
				}
			}
		});
		// #endif
	}
	
	// 计算SHA256哈希值
	function calculateSHA256() {
		return new Promise((resolve, reject) => {
			if (!uploadInfo.value.fileName) {
				return reject(new Error('未选择文件'));
			}
			
			uploadInfo.value.status = 'checking';
			uploadInfo.value.progress = 0;
			
			// 创建SHA256哈希对象
			const hash = sha256.create();
			
			// #ifdef WEB
			// Web端文件读取
			const file = uploadInfo.value.filePath; 
			const fileSize = uploadInfo.value.fileSize;
			const webChunkSize = 2 * 1024 * 1024; // 2MB
			let offset = 0;
			
			const fileReader = new FileReader();
			
			fileReader.onload = function(e) {
				const data = new Uint8Array(e.target.result);
				hash.update(data);
				
				offset += e.target.result.byteLength;
				uploadInfo.value.progress = Math.floor((offset / fileSize) * 100);
				
				if (offset < fileSize) {
					// 继续读取下一块
					readNextChunk();
				} else {
					// 完成
					uploadInfo.value.sha256 = hash.hex();
					console.log('SHA256计算完成:', uploadInfo.value.sha256);
					resolve();
				}
			};
			
			fileReader.onerror = function(e) {
				console.error('文件读取错误:', e);
				reject(new Error('文件读取错误'));
			};
			
			function readNextChunk() {
				const slice = file.slice(offset, offset + webChunkSize);
				fileReader.readAsArrayBuffer(slice);
			}
			
			// 开始读取第一块
			readNextChunk();
			// #endif
		});
	}
	
	// 预上传文件
	function preUploadFile() {
		return new Promise((resolve, reject) => {
			const preUploadData = {
				virtualPath: props.virtualPath,
				fileName: uploadInfo.value.fileName,
				fileSize: uploadInfo.value.fileSize,
				sha256: uploadInfo.value.sha256
			};
			
			console.log('预上传请求数据:', preUploadData);
			
			proxy.$http.getPreUploadUrl(preUploadData).then(res => {
				if (res.isSuccess) {
					console.log('预上传成功:', res.data);
					uploadInfo.value.fileId = res.data.fileId;
					// 保存上传URL
					uploadInfo.value.uploadUrl = res.data.uploadUrl;
					resolve(res.data);
				} else {
					reject(new Error(res.message || '预上传失败'));
				}
			}).catch(err => {
				console.error('预上传请求错误:', err);
				reject(err);
			});
		});
	}
	
	// 上传文件
	function uploadFile() {
		return new Promise((resolve, reject) => {
			if (!uploadInfo.value.uploadUrl) {
				return reject(new Error('未获取上传链接'));
			}
			
			uploadInfo.value.status = 'uploading';
			uploadInfo.value.progress = 0;
			
			// #ifdef WEB
			// Web端上传
			const file = uploadInfo.value.filePath; 
			const xhr = new XMLHttpRequest();
			
			// 监听上传进度
			xhr.upload.onprogress = function(e) {
				if (e.lengthComputable) {
					uploadInfo.value.progress = Math.floor((e.loaded / e.total) * 100);
				}
			};
			
			xhr.onload = function() {
				if (xhr.status >= 200 && xhr.status < 300) {
					resolve();
				} else {
					reject(new Error('上传失败: ' + xhr.status));
				}
			};
			
			xhr.onerror = function() {
				reject(new Error('网络错误'));
			};
			
			xhr.open('PUT', uploadInfo.value.uploadUrl, true);
			xhr.send(file);
			// #endif
		});
	}
	
	// 确认上传
	function confirmUploadFile() {
		return new Promise((resolve, reject) => {
			if (!uploadInfo.value.fileId) {
				return reject(new Error('未获取文件ID'));
			}
			
			uploadInfo.value.status = 'verifying';
			
			proxy.$http.confirmUpload(uploadInfo.value.fileId).then(res => {
				if (res.isSuccess) {
					console.log('确认上传成功');
					resolve();
				} else {
					reject(new Error(res.message || '确认上传失败'));
				}
			}).catch(err => {
				console.error('确认上传请求错误:', err);
				reject(err);
			});
		});
	}
	
	// 检查文件状态
	function checkFileStatus() {
		return new Promise((resolve, reject) => {
			if (!uploadInfo.value.fileId) {
				return reject(new Error('未获取文件ID'));
			}
			
			const checkStatus = () => {
				proxy.$http.checkUploadStatus(uploadInfo.value.fileId).then(res => {
					if (res.isSuccess) {
						console.log('文件状态:', res.data);
						
						if (res.data.status === 'VERIFIED') {
							// 文件验证成功
							uploadInfo.value.status = 'success';
							uni.showToast({
								title: '文件上传成功',
								icon: 'success'
							});
							setTimeout(() => {
								closeModal();
							}, 1000);
							resolve(res.data);
						} else if (res.data.status === 'FAILED') {
							reject(new Error('文件验证失败'));
						} else {
							setTimeout(checkStatus, 1000);
						}
					} else {
						reject(new Error(res.message || '检查文件状态失败'));
					}
				}).catch(err => {
					console.error('检查文件状态错误:', err);
					reject(err);
				});
			};
			
			checkStatus();
		});
	}
	
	// 复制文本
	function copyText(text) {
		uni.setClipboardData({
			data: text,
			success: () => {
				uni.showToast({
					title: '复制成功',
					icon: 'success'
				});
			}
		});
	}
	
	// 对外暴露方法
	defineExpose({
		openModal,
		closeModal
	});
</script>

<style scoped>
/* 弹窗基础样式 */
.file-pop-modal {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	z-index: 9999;
	display: flex;
	justify-content: center;
	align-items: center;
}

.file-pop-overlay {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: rgba(0, 0, 0, 0.5);
	z-index: 1;
}

.file-pop-content {
	position: relative;
	z-index: 2;
	background-color: #fff;
	border-radius: 12rpx;
	width: 80%;
	max-width: 600rpx;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.15);
	animation: popIn 0.3s ease-out forwards;
}

@keyframes popIn {
	from {
		opacity: 0;
		transform: scale(0.9);
	}
	to {
		opacity: 1;
		transform: scale(1);
	}
}

.pop-container {
	padding: 30rpx;
	width: 100%;
	box-sizing: border-box;
}

.pop-title {
	font-size: 36rpx;
	font-weight: bold;
	margin-bottom: 30rpx;
	text-align: center;
}

.pop-content {
	margin-bottom: 30rpx;
}

.input-group {
	margin-bottom: 20rpx;
}

.input-label {
	display: block;
	margin-bottom: 10rpx;
	font-size: 28rpx;
	color: #666;
}

.pop-input {
	width: 100%;
	height: 80rpx;
	border: 1px solid #ddd;
	border-radius: 8rpx;
	padding: 0 20rpx;
	font-size: 28rpx;
	box-sizing: border-box;
}

.pop-footer {
	display: flex;
	justify-content: space-between;
}

.pop-btn {
	width: 45%;
	height: 80rpx;
	border-radius: 8rpx;
	font-size: 28rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}

.pop-btn-cancel {
	background-color: #f5f5f5;
	color: #666;
}

.pop-btn-confirm {
	background-color: #007aff;
	color: #fff;
}

.pop-btn-confirm[disabled] {
	background-color: #cccccc;
	color: #ffffff;
	opacity: 0.6;
}

/* 上传区域样式 */
.upload-area {
	width: 100%;
	height: 200rpx;
	border: 2px dashed #ddd;
	border-radius: 8rpx;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	margin-bottom: 20rpx;
	cursor: pointer;
}

.upload-text {
	margin-top: 10rpx;
	font-size: 28rpx;
	color: #999;
}

.upload-info {
	margin-top: 20rpx;
}

.file-name {
	font-size: 28rpx;
	margin-bottom: 10rpx;
	display: block;
	word-break: break-all;
}

.progress-container {
	display: flex;
	align-items: center;
}

.progress-bar {
	flex: 1;
	height: 10rpx;
	background-color: #f0f0f0;
	border-radius: 10rpx;
	overflow: hidden;
	margin-right: 10rpx;
}

.progress-inner {
	height: 100%;
	background-color: #007aff;
	border-radius: 10rpx;
	transition: width 0.3s;
}

.progress-text {
	font-size: 24rpx;
	color: #666;
	width: 60rpx;
	text-align: right;
}

.status-text {
	display: block;
	margin-top: 10rpx;
	font-size: 26rpx;
	color: #007aff;
	text-align: center;
}

/* 分享结果样式 */
.share-result {
	padding: 20rpx;
	background-color: #f8f8f8;
	border-radius: 8rpx;
}

.share-item {
	margin-bottom: 20rpx;
}

.share-label {
	font-size: 28rpx;
	color: #666;
	margin-bottom: 10rpx;
	display: block;
}

.share-value-container {
	display: flex;
	align-items: center;
}

.share-value {
	flex: 1;
	font-size: 28rpx;
	word-break: break-all;
	padding: 10rpx;
	background-color: #fff;
	border: 1px solid #eee;
	border-radius: 4rpx;
}

.copy-btn {
	margin-left: 20rpx;
	padding: 10rpx 20rpx;
	background-color: #f0f0f0;
	border-radius: 4rpx;
	font-size: 24rpx;
}

.file-info {
	margin-bottom: 20rpx;
	padding: 20rpx;
	background-color: #f8f8f8;
	border-radius: 8rpx;
}

.file-info-label {
	font-size: 28rpx;
	color: #666;
}

.file-info-value {
	font-size: 28rpx;
	font-weight: bold;
}
</style>
