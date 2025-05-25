<template>
	<cd-page header :padding-top="0">
		<!-- #ifdef APP -->
		<u-navbar back-text="返回" title="创建分享文章" height="50"></u-navbar>
		<!-- #endif -->
		<view class="editor-container card-border">
			<view class="editor-form">
				<view class="form-row">
					<u-upload :action="uploadLink" max-count="3" :header="uploadHeader" @on-success="onUploadSuccess"
						@on-remove="onRemoveImage" ref="uUpload"></u-upload>
				</view>
				<view class="form-row">
					<uni-data-select v-model="category" :localdata="categoryData" placeholder="请选择分类"
						class="item-border">
					</uni-data-select>
				</view>
				<view class="form-row">
					<u-input v-model="title" placeholder="标题" type="text" maxlength="50" border
						:borderColor="$u.color['primary']"></u-input>
				</view>
				<view class="form-row">
					<view class="border-primary" style="border-radius: 10rpx; overflow: hidden;">
						<u-input placeholder="正文" v-model="content" type="textarea" border height="250" auto-height
							maxlength="800" />
					</view>
				</view>
				<view class="form-row">
					<view class="border-primary" style="border-radius: 10rpx;">
						<view class="flex-container">
							<view class="flex-item input-column">
								<view class="input-row">
									<u-input v-model="shareLink" placeholder="链接地址" border></u-input>
								</view>
								<view class="input-row">
									<u-input v-model="extractPassword" placeholder="提取密码,无需密码请留空" border></u-input>
								</view>
							</view>
							<view class="flex-item platform-column">
								<view class="platform-row">
									<u-image width="60px" height="60px" :src="currentPanIcon"></u-image>
								</view>
								<view class="platform-row">
									<u-text :text="linkText" :color="linkTextColor" class="platform-text" align="center"
										size="20"></u-text>
								</view>
							</view>
						</view>
					</view>
				</view>
				<view class="form-row">
					<view class="form-row">
						<view class="tag-list">
							<u-tag v-for="(tag, index) in tags" :key="index" :text="tag" @click="removeTag(index)"
								class="tag-item" :borderColor="$u.color['primary']"></u-tag>
						</view>
						<view class="tag-input-container" v-if="tags.length < 3">
							<view class="input-wrapper">
								<u-input v-model="customTag" placeholder="输入标签" border
									:borderColor="$u.color['primary']" maxlength="3"></u-input>
							</view>
							<view class="button-wrapper">
								<u-button @click="addTag" type="primary" plain>+</u-button>
							</view>
						</view>
						<view v-else class="tag-limit-tip">
							<u-text text="最多添加3个标签" color="#999" size="24rpx"></u-text>
						</view>
					</view>
				</view>
			</view>
			<view class="form-row">
				<u-button type="primary" @click="publishPost">发布分享</u-button>
			</view>
		</view>
	</cd-page>
</template>

<script setup>
	import {
		platformMap
	} from '@/common/platformMap.ts'
	import {
		ref,
		watch,
		onMounted,
		getCurrentInstance
	} from 'vue'
	import {
		apiConfig,
		cloudriveConfig
	} from '@/config/global_config.ts'
	import {
		useUserStore
	} from '@/stores/modules/user'
	import api from '@/request/api.js'
	const {
		proxy
	} = getCurrentInstance()
	const userStore = useUserStore();
	const categoryData = [{
			value: 0,
			text: '选择分类'
		},
		{
			value: 1,
			text: '文档'
		},
		{
			value: 2,
			text: '视频'
		},
		{
			value: 3,
			text: '电影'
		},
		{
			value: 4,
			text: '软件'
		},
		{
			value: 5,
			text: '课程'
		},
		{
			value: 6,
			text: '音乐'
		},
		{
			value: 7,
			text: '书籍'
		},
		{
			value: 8,
			text: '图片'
		}
	]
	// 数据
	const title = ref('')
	const content = ref('')
	const uploadLink = ref(cloudriveConfig.BASE_URL.url + apiConfig.file.perfix + "/imagebed/images")
	const uploadHeader = ref({})
	const encryption = ref(false)
	const extractPassword = ref('')
	const tags = ref([])
	const customTag = ref('')
	const shareLink = ref('')
	const currentPanIcon = ref('/static/editor/blanck.png')
	const linkText = ref('请输入分享链接')
	const linkTextColor = ref('#000')
	const lickCheckedPass = ref(false)
	const category = ref(0)
	// 存储上传图片信息
	const uploadedImages = ref([])
	// 监听链接变化
	watch([shareLink, extractPassword], ([newShareLink, newExtractPassword]) => {
		// 处理分享链接变化
		if (!newShareLink) {
			currentPanIcon.value = '/static/editor/blanck.png'
			linkText.value = '请输入分享链接'
			linkTextColor.value = '#000'
			return
		}
		// 处理提取密码变化
		encryption.value = !!newExtractPassword

		lickCheckedPass.value = false
		for (const [domain, info] of Object.entries(platformMap)) {
			const urlMatch = newShareLink.match(/(?:https?:\/\/)?(?:[^@\/\n]+@)?(?:www\.)?([^:\/?\n]+)/i)
			if (urlMatch && urlMatch[1] && urlMatch[1].includes(domain) && info) {
				currentPanIcon.value = info.icon
				linkText.value = info.name
				linkTextColor.value = '#000'
				lickCheckedPass.value = true
				break
			}
		}

		if (!lickCheckedPass.value) {
			currentPanIcon.value = ''
			linkText.value = '不支持的平台'
			linkTextColor.value = '#ff0000'
		}
	})

	// 添加标签
	const addTag = () => {
		if (customTag.value.trim()) {
			tags.value.push(customTag.value.trim())
			customTag.value = ''
		}
	}

	// 删除标签
	const removeTag = (index) => {
		tags.value.splice(index, 1)
	}

	const init = () => {
		uploadHeader.value = {
			'Authorization': `Bearer ${userStore.token}`
		}
	}

	onMounted(() => {
		init()
	})

	// 图片上传成功处理
	const onUploadSuccess = (data, index, lists) => {
		if (data && data.isSuccess && data.data) {
			// 保存图片信息
			uploadedImages.value.push({
				id: data.data.id,
				imageUrl: data.data.imageUrl
			})
		}
	}

	// 图片删除处理
	const onRemoveImage = (index, lists) => {
		// 获取被删除的图片信息
		const removedImage = uploadedImages.value[index]
		if (removedImage && removedImage.id) {
			// 调用删除接口
			api.deleteImage(removedImage.id).then(res => {
				if (res && res.isSuccess) {
					// 从本地列表中删除
					uploadedImages.value.splice(index, 1)
				}
			})
		}
	}

	// 发布文章
	const publishPost = () => {
		// 表单验证
		if (!title.value.trim()) {
			proxy.$u.toast('请输入标题')
			return
		}
		if (!content.value.trim()) {
			proxy.$u.toast('请输入正文内容')
			return
		}
		if (category.value === 0) {
			proxy.$u.toast('请选择分类')
			return
		}
		if (!shareLink.value.trim()) {
			proxy.$u.toast('请输入分享链接')
			return
		}
		if (!lickCheckedPass.value) {
			proxy.$u.toast('不支持的网盘平台')
			return
		}
		
		// 构建请求数据
		const postData = {
			title: title.value,
			content: content.value,
			summary: content.value.startsWith('#') ? content.value.replace(/^#+\s*/gm, '').substring(0, 100) : content.value.substring(0, 100), // 如果是Markdown则去除标签后取前100字符
			categoryId: category.value,
			thumbnail: uploadedImages.value.length > 0 ? uploadedImages.value[0].imageUrl : '', // 缩略图为第一张图片
			isMarkdown: content.value.startsWith('#') ? 1 : 0, // 判断是否为Markdown格式
			imageUrls: JSON.stringify(uploadedImages.value.map(img => img.imageUrl)), // 图片URL数组
			shareLink: shareLink.value,
			needPassword: !!extractPassword.value, // 是否需要密码
			password: extractPassword.value || '', // 提取密码
			tags: JSON.stringify(tags.value) // 标签数组
		}
		
		// 调用创建文章API
		proxy.$u.toast('发布中...')
		api.createNewPost(postData).then(res => {
			if (res && res.isSuccess) {
				proxy.$u.toast('发布成功')
				// 清空表单
				title.value = ''
				content.value = ''
				category.value = 0
				shareLink.value = ''
				extractPassword.value = ''
				tags.value = []
				// 清空图片
				if (proxy.$refs.uUpload) {
					proxy.$refs.uUpload.clear()
				}
				uploadedImages.value = []
				// 跳转到首页或文章列表
				uni.switchTab({
					url: '/pages/index/index'
				})
			} else {
				proxy.$u.toast(res?.message || '发布失败')
			}
		}).catch(err => {
			console.error('发布失败', err)
			proxy.$u.toast('发布失败，请稍后重试')
		})
	}
</script>

<style scoped>
	.editor-form {
		padding: 10rpx;
	}

	.form-row {
		width: 100%;
		margin-bottom: 10rpx;
		padding: 0 10rpx 0 10rpx;
	}

	/* #ifdef WEB */
	.editor-container {
		width: 60%;
		margin: 0 auto;
		background-color: #fff;
		border-radius: 20rpx;
		padding: 20rpx 10rpx 30rpx 10rpx;
		overflow: hidden;
	}

	/* #endif */
	/* #ifdef APP */
	.editor-container {
		width: 100%;
		margin: 0 auto;
		background-color: #fff;
	}

	/* #endif */
	.flex-container {
		display: flex;
		gap: 30rpx;
		width: 100%;
		padding: 20rpx;
	}

	.flex-item {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}

	.input-column {
		flex: 2;
	}

	.platform-column {
		flex: 1;
		align-items: center;
		justify-content: center;
	}

	.input-row,
	.platform-row {
		width: 100%;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.platform-row {
		text-align: center;
	}

	.platform-text {
		margin-top: 5rpx;
	}

	.tag-list {
		display: flex;
		flex-wrap: wrap;
		gap: 10rpx;
		margin-bottom: 10rpx;
		height: 50rpx;
	}

	.tag-item {
		margin-right: 10rpx;
	}

	.tag-input-container {
		display: flex;
		align-items: flex-start;
		gap: 20rpx;
		width: 100%;
	}

	.input-wrapper {
		max-width: 180rpx;
		min-width: 160rpx;
	}

	.button-wrapper {
		max-width: 180rpx;
		min-width: 160rpx;
		margin-left: 10rpx;
	}

	.tag-input {
		width: 100%;
	}

	.tag-limit-tip {
		margin-top: 10rpx;
		text-align: center;
	}
</style>