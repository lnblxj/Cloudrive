<template>
	<cd-page>
		<!-- #ifdef APP -->
		<u-navbar title="文件" immersive :is-back="false" height="50"></u-navbar>
		<!-- #endif -->
		<view class="file-control-menu-container card-border">
			<view class="file-control-menu">
				<view class="file-control-button left-group" v-if="currentPath !== '/'" @click="goBack">
					<l-icon name="arrow-left-circle" size="50rpx"></l-icon>
				</view>
				<view class="file-control-button left-group" v-else></view>
				<view class="right-group">
					<view class="file-control-button" @click="handleUploadClick">
						<l-icon name="upload" size="50rpx"></l-icon>
					</view>
					<view class="file-control-button" @click="handleNewFolderClick">
						<l-icon name="folder-add" size="50rpx"></l-icon>
					</view>
				</view>
			</view>
		</view>
		<view class="file-container card-border">
			<FileList
				:path="currentPath"
				:files="fileData"
				@file-click="handleFileClick" 
				@folder-click="handleFolderClick"
				@file-action="handleFileAction"
			></fileList>
			<fileMenu
				ref="fileMenuRef"
				:fileType="currentFileType"
				:position="menuPosition"
				@menu-action="handleMenuAction"
			/>
			<filePop
				ref="filePopRef"
				:type="popType"
				:fileInfo="currentFile"
				:virtualPath="currentPath"
				@confirm="handlePopConfirm"
				@cancel="handlePopCancel"
			/>
		</view>
		<view :style="tabbarConfig.containerStyle">
			<u-tabbar 
			:list="tabbarData" 
			height="100" 
			:content-style="tabbarConfig.style"
			:bg-color="tabbarConfig.props.bgColor"
			></u-tabbar>
		</view>
	</cd-page>
</template>

<script setup>
	import { ref, onMounted, getCurrentInstance } from 'vue';
	import FileList from './fileList.vue';
	import fileMenu from './fileMenu.vue';
	import filePop from './filePop.vue';
	import {tabbarData, tabbarConfig} from '@/common/tabbar.js';
	
	const {proxy} = getCurrentInstance()

	//数据
	const currentPath = ref('/');
	const fileData = ref([]);
	const pathHistory = ref([]);
	const isLoading = ref(false);

	// 获取文件列表
	async function loadFileList(path = '/') {
		isLoading.value = true;
		try {
			const res = await proxy.$http.getFolderContent(path);
			if (res && res.isSuccess && res.data) {
				// 转换API返回的数据格式为组件需要的格式
				fileData.value = res.data.map(item => ({
					id: item.id,
					type: item.isFolder ? 'folder' : (item.fileType || 'unknown'),
					name: item.fileName,
					size: item.isFolder ? '-' : formatFileSize(item.fileSize),
					date: formatDate(item.updatedAt),
					virtualPath: item.virtualPath,
					isFolder: item.isFolder,
					originalData: item // 保存原始数据，以备后用
				}));
			} else {
				console.error('获取文件列表失败:', res);
				uni.showToast({
					title: '获取文件列表失败',
					icon: 'none'
				});
			}
		} catch (error) {
			console.error('获取文件列表异常:', error);
			uni.showToast({
				title: '网络异常，请稍后重试',
				icon: 'none'
			});
		} finally {
			isLoading.value = false;
		}
	}

	// 格式化文件大小
	function formatFileSize(size) {
		if (!size) return '0B';
		
		const units = ['B', 'KB', 'MB', 'GB', 'TB'];
		let index = 0;
		let fileSize = size;
		
		while (fileSize >= 1024 && index < units.length - 1) {
			fileSize /= 1024;
			index++;
		}
		
		return fileSize.toFixed(2) + units[index];
	}

	// 格式化日期
	function formatDate(dateStr) {
		if (!dateStr) return '';
		
		const date = new Date(dateStr);
		return date.toISOString().split('T')[0];
	}

	// 处理文件点击事件
	function handleFileClick(file) {
		console.log('文件点击:', file);
		if (file.isFolder) {
			enterFolder(file);
		} else {
			// 处理文件点击
		}
	}
	
	// 处理文件夹点击事件
	function handleFolderClick(folder) {
		console.log('文件夹点击:', folder);
		enterFolder(folder);
	}
	
	// 进入文件夹
	function enterFolder(folder) {
		pathHistory.value.push(currentPath.value);
		currentPath.value = folder.virtualPath + folder.name + '/';
		loadFileList(currentPath.value);
	}
	
	// 返回上一级
	function goBack() {
		if (pathHistory.value.length > 0) {
			const previousPath = pathHistory.value.pop();
			currentPath.value = previousPath;
			loadFileList(previousPath);
		}
	}
	
	// 组件挂载时加载初始文件列表
	onMounted(() => {
		loadFileList(currentPath.value);
	})
	
	// 菜单相关状态
	const fileMenuRef = ref(null);
	const filePopRef = ref(null);
	const currentFileType = ref('file');
	const menuPosition = ref({ x: 0, y: 0 });
	const popType = ref('rename');
	const currentFile = ref(null);

	// 处理文件操作事件（点击/长按/右键菜单）
	function handleFileAction(actionData) {
		console.log('文件操作:', actionData);
		if (actionData.actionType === 'click') {
			// console.log('点击文件:', actionData.file);
		} else if (actionData.actionType === 'longpress' || actionData.actionType === 'contextmenu') {
			console.log('文件长按');
			currentFileType.value = actionData.file.isFolder ? 'folder' : 'file';
			// 设置菜单位置
			if (actionData.actionType === 'contextmenu' && actionData.event) {
				menuPosition.value = {
					x: actionData.event.clientX,
					y: actionData.event.clientY
				};
			}
			
			// 设置当前操作的文件
			if (fileMenuRef.value) {
				fileMenuRef.value.currentFile = actionData.file;
			}
			
			// 显示菜单
			fileMenuRef.value?.showMenu();
		}
		}
	// 处理菜单操作
	function handleMenuAction(action) {
		console.log('菜单操作:', action);
		// 保存当前操作的文件
		currentFile.value = fileMenuRef.value?.currentFile;
		switch (action) {
			case 'download':
				// 下载
				if (currentFileType.value !== 'folder') {
					const fileId = fileMenuRef.value?.currentFile?.id;
					if (fileId) {
						proxy.$http.downloadFile(fileId).then(res => {
							if (res && res.isSuccess && res.data) {
								const downloadUrl = res.data;

								// #ifdef APP
								plus.runtime.openURL(downloadUrl);
								// #endif
								// #ifdef WEB
								window.location.href = downloadUrl;
								// #endif
							} else {
								uni.showToast({
									title: '下载链接获取失败',
									icon: 'none'
								});
							}
						}).catch(error => {
							console.error('文件下载异常:', error);
							uni.showToast({
								title: '网络异常，请稍后重试',
								icon: 'none'
							});
						});
					}
				}
				break;
			case 'rename':
				// 打开重命名弹窗
				popType.value = 'rename';
				filePopRef.value?.openModal('rename', currentFile.value);
				break;
			case 'share':
				// 打开分享弹窗
				popType.value = 'share';
				filePopRef.value?.openModal('share', currentFile.value);
				break;
			case 'delete':
				// 实现删除逻辑
				if (currentFileType.value === 'folder') {
					const folderId = fileMenuRef.value?.currentFile?.id;
					if (folderId) {
						proxy.$http.deleteFolder(folderId).then(() => {
							// 删除成功后重新加载当前目录
							loadFileList(currentPath.value);
						});
					}
				} else {
					const fileId = fileMenuRef.value?.currentFile?.id;
					if (fileId) {
						proxy.$http.deleteFile(fileId).then(() => {
							// 删除成功后重新加载当前目录
							loadFileList(currentPath.value);
						});
					}
				}
				break;
		}
	}
	
	// 处理上传按钮点击
	function handleUploadClick() {
		popType.value = 'upload';
		filePopRef.value?.openModal('upload');
	}
	
	// 处理新建文件夹按钮点击
	function handleNewFolderClick() {
		popType.value = 'newFolder';
		filePopRef.value?.openModal('newFolder');
	}
	
	// 处理弹窗确认
	function handlePopConfirm(data) {
		console.log('弹窗确认:', data);
		switch (data.type) {
			case 'rename':
				break;
				
			case 'newFolder':
				break;
				
			case 'upload':
				break;
				
			case 'share':
				break;
		}
	}
	
	// 处理弹窗取消
	function handlePopCancel() {
		console.log('弹窗取消');
	}
</script>

<style scoped>
.file-container{
    background-color: #fff;
    border-radius: 20rpx;
}

/* #ifdef WEB */
.file-container{
    margin-bottom: 200rpx;
}
/* #endif */

.file-control-menu-container {
    width: 100%;
    display: flex;
    justify-content: space-between;
    padding: 10px;
    border-radius: 10px;
    background-color: #fff;
    margin-bottom: 10px;
}

.file-control-menu {
    display: flex;
    width: 100%;
    justify-content: space-between;
    align-items: center;
}

.right-group {
    display: flex;
    gap: 10px;
}

.file-control-button {
    padding: 8px;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    justify-content: center;
}

/* PC端hover效果 */
/* #ifndef APP-PLUS */
.file-control-button:hover {
    background-color: #f5f5f5;
    transform: scale(1.05);
}
/* #endif */

/* 移动端active效果 */
/* #ifdef APP-PLUS */
.file-control-button:active {
    background-color: #f0f0f0;
    transform: scale(0.95);
}
/* #endif */
</style>
