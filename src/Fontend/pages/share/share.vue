<template>
	<cd-page header>
		<view class="share-container card-border">
			<!-- 文件信息展示区 -->
			<view class="file-info card-border card-border-hover">
				<image :src="fileIcon" mode="aspectFit" class="file-icon"></image>
				<view class="info-content">
					<text class="filename">{{ fileInfo.fileName }}</text>
					<view class="sub-info">
						<text class="size">大小：{{ (fileInfo.fileSize / 1024 / 1024).toFixed(2) }}MB</text>
						<text class="time">分享时间：{{ fileInfo.updateTime }}</text>
						<text class="sharer">分享者：{{ fileInfo.nickName }}</text>
					</view>
				</view>
			</view>

			<!-- 提取码输入区 -->
			<view class="extract-area">
				<u-input v-if="fileInfo.needPassword" v-model="extractCode" type="text" placeholder="请输入提取码"
					:clearable="true" border :border-color="$u.color['primary']"></u-input>
				<u-button type="primary" @click="handleExtract" :loading="loading">提取文件</u-button>
				<text v-if="errorMsg" class="error-msg">{{ errorMsg }}</text>
			</view>
			{{extractCode}}
			<!-- 底部信息 -->
			<view class="footer">
				<text class="copyright">© 2025 Cloudrive. All rights reserved.</text>
				<text class="terms" @click="handleTerms">使用条款</text>
			</view>
		</view>
	</cd-page>
</template>

<script setup>
	import {
		ref,
		onMounted,
		getCurrentInstance,
		computed
	} from 'vue'
	import {
		standardIcon
	} from '@/common/fileIconMap.ts'
	const {
		proxy
	} = getCurrentInstance()

	// 定义props接收参数
	const props = defineProps({
		id: {
			type: [String, Number],
			default: ''
		}
	});

	// 响应式数据
	const extractCode = ref('')
	const loading = ref(false)
	const errorMsg = ref('')
	const fileInfo = ref({
		fileName: '',
		fileSize: 0,
		nickName: '',
		updateTime: '',
		needPassword: false
	})

	// 获取分享文件信息
	const fetchShareInfo = async () => {
		if (props.id == '') {
			uni.switchTab({
				url: '/pages/index/index'
			})
		}
		try {
			const res = await proxy.$http.getShareFileInfo(props.id)
			if (res.isSuccess) {
				fileInfo.value = {
					fileName: res.data.fileName,
					fileSize: res.data.fileSize,
					nickName: res.data.nickName,
					updateTime: res.data.updateTime,
					needPassword: res.data.needPassword
				}
			}
		} catch (error) {
			console.error('获取分享信息失败:', error)
		}
	}

	onMounted(() => {
		fetchShareInfo()
	})

	// 根据文件扩展名获取对应图标
	const fileIcon = computed(() => {
		const ext = fileInfo.value.fileName.split('.').pop()?.toLowerCase() || 'unknown'
		return standardIcon[ext] || standardIcon['unknown']
	})

	// 处理提取文件
	const handleExtract = async () => {
		if (fileInfo.value.needPassword) {
			if(extractCode.value === '')
			{
				errorMsg.value = '请输入提取码'
				return
			}
		} else {
			extractCode.value = ''
		}
		loading.value = true
		errorMsg.value = ''

		try {
			const res = await proxy.$http.getShareDownloadUrl({
				shareId: props.id,
				password: extractCode.value
			})

			if (res.isSuccess) {
				// #ifdef APP
				plus.runtime.openURL(res.data)
				// #endif
				// #ifdef WEB
				window.location.href = res.data
				// #endif
			} else {
				errorMsg.value = res.message || '提取文件失败'
			}
		} catch (error) {
			errorMsg.value = '网络错误，请重试'
			console.error('提取文件失败:', error)
		} finally {
			loading.value = false
		}
	}

	// 处理使用条款点击
	const handleTerms = () => {
		uni.navigateTo({
			url: '/pages/usePrivacy/usePrivacy'
		})
	}
</script>

<style scoped>
	.share-container {
		padding: 20px;
		max-width: 1000px;
		margin: 0 auto;
		min-height: 1200rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		background-color: #fff;
		border-radius: 20rpx;
	}

	.header {
		width: 100%;
		display: flex;
		flex-direction: row;
		align-items: center;
		margin-bottom: 40px;
		gap: 10rpx;
	}

	.logo-container {
		width: auto;
		display: flex;
		align-items: center;
		justify-content: flex-start;
	}

	.logo {
		width: 120px;
		height: 60px;
	}

	.title {
		font-size: 24px;
		font-weight: bold;
	}

	.file-info {
		width: 100%;
		padding: 20px;
		border-radius: 10px;
		background-color: #f8f9fa;
		display: flex;
		align-items: center;
		margin-bottom: 30px;
	}

	.file-icon {
		width: 60px;
		height: 60px;
		object-fit: contain;
	}

	.info-content {
		margin-left: 20px;
	}

	.filename {
		font-size: 18px;
		font-weight: bold;
		margin-bottom: 10px;
		display: block;
	}

	.sub-info {
		display: flex;
		gap: 20px;
		color: #666;
		font-size: 14px;
	}

	.extract-area {
		width: 100%;
		max-width: 400px;
		margin: 20px 0;
	}

	.u-input {
		margin-bottom: 15px;
	}

	.error-msg {
		color: #ff4d4f;
		font-size: 14px;
		margin-top: 10px;
		display: block;
		text-align: center;
	}

	.footer {
		margin-top: auto;
		text-align: center;
		color: #999;
		font-size: 14px;
	}

	.terms {
		margin-left: 10px;
		color: #2979ff;
		cursor: pointer;
	}

	/* 响应式适配 */
	@media screen and (max-width: 768px) {
		.share-container {
			padding: 15px;
		}

		.logo {
			width: 100px;
			height: 50px;
		}

		.title {
			font-size: 20px;
		}

		.file-info {
			padding: 15px;
		}

		.filename {
			font-size: 16px;
		}

		.sub-info {
			flex-direction: column;
			gap: 5px;
		}
	}
</style>