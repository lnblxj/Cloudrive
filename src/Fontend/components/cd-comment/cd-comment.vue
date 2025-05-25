<template>
	<view class="comment-title" ref="commentTitleRef">
		<text>评论</text>
	</view>
	<view class="comment-textarea">
		<textarea class="native-textarea" :disabled="!isLogin" v-model="commentText" :placeholder="isLogin?'输入评论内容':'请先登录后发表评论'"></textarea>
		<u-row justify="right">
			<view style="width: 20%; margin: 10px;">
				<u-button @click="newComment" :disabled="!isLogin" type="primary" >评论</u-button>
			</view>
		</u-row>
	</view>
	<view class="comment-list">
		<view v-if="loading" class="loading-container">
			<u-loading mode="circle" size="24"></u-loading>
			<text class="loading-text">加载评论中...</text>
		</view>
		<view v-else-if="commentList.length === 0" class="empty-comment">
			<text>暂无评论，快来发表第一条评论吧</text>
		</view>
		<view v-else>
			<u-row v-for="(item, index) in commentList" :key="item.id" style="width: 100%;" align="top" class="comment-item">
				<u-col :span="2" text-align="right">
					<u-avatar :src="item.avatar || 'http://cdnjson.com/images/2024/04/17/1924a1db1b45681b1e52e5d9e7c8c49e.jpg'"
						mode="circle" size="100"></u-avatar>
				</u-col>
				<u-col :span="10">
					<view>
						<u-row>
							<u-text :text="item.nickName || '用户昵称'" bold size="13px"></u-text>
						</u-row>
						<u-row>
							<u-text :text="formatDate(item.createTime)" color="#9f9f9f" size="12px"></u-text>
						</u-row>
						<u-row style="width: 100%;">
							<view class="comment-content">
								<u-text :text="item.content" overflow="clip"></u-text>
							</view>
						</u-row>
					</view>
				</u-col>
			</u-row>
		</view>
	</view>
</template>

<script setup>
	/**
	 * @name cd-comment
	 * @description 评论区
	 * 
	 * @property {String} postId    String | 文章ID
	 */
	import {ref, onMounted, getCurrentInstance, nextTick} from 'vue'
	import { useUserStore } from '@/stores/modules/user'
	defineOptions({
		name: 'cd-comment'
	})
	const {proxy} = getCurrentInstance()
	const userStore = useUserStore()
	const props = defineProps({
		postId: {
			type: String,
			default: ''
		}
	})
	//数据
	const isLogin = ref(false)
	const commentText = ref('')
	const token = ref('')
	const commentList = ref([])
	const loading = ref(false)
	const commentTitleRef = ref(null)
	const isCommentLoaded = ref(false)
	
	// 初始化
	const init = () => {
		isLogin.value = userStore.isLoggedIn
		token.value = userStore.token
		// 设置懒加载观察器
		setupIntersectionObserver()
	}
	
	// 设置交叉观察器实现懒加载
	const setupIntersectionObserver = () => {
		nextTick(() => {
			try {
				const observer = uni.createIntersectionObserver(proxy);

				observer.relativeToViewport().observe('.comment-title', (res) => {
					if (res.intersectionRatio > 0 && !isCommentLoaded.value) {
						console.log('评论区进入视图，开始加载评论');
						getCommentList();
						isCommentLoaded.value = true;
						observer.disconnect();
					}
				});
			} catch (error) {
				console.error('创建交叉观察器失败:', error);
				if (!isCommentLoaded.value) {
					getCommentList();
					isCommentLoaded.value = true;
				}
			}
		});
	}
	
	// 获取评论列表
	const getCommentList = async () => {
		if (!props.postId) return
		
		try {
			loading.value = true
			const res = await proxy.$http.getCommentList(props.postId)
			if (res.isSuccess) {
				commentList.value = res.data || []
			} else {
				uni.showToast({
					title: res.message || '获取评论失败',
					icon: 'none'
				})
			}
		} catch (error) {
			console.error('获取评论列表失败', error)
			uni.showToast({
				title: '获取评论失败，请稍后重试',
				icon: 'none'
			})
		} finally {
			loading.value = false
		}
	}
	
	// 提交新评论
	const newComment = async () => {
		if (!isLogin.value) {
			uni.showToast({
				title: '请先登录后再评论',
				icon: 'none'
			})
			return
		}
		
		if (!commentText.value.trim()) {
			uni.showToast({
				title: '评论内容不能为空',
				icon: 'none'
			})
			return
		}
		
		if (!props.postId) {
			uni.showToast({
				title: '文章ID不能为空',
				icon: 'none'
			})
			return
		}
		
		try {
			const data = {
				articleId: props.postId,
				content: commentText.value.trim()
			}
			
			const res = await proxy.$http.addComment(data)
			if (res.isSuccess) {
				uni.showToast({
					title: '评论成功',
					icon: 'success'
				})
				commentText.value = ''
				// 重新获取评论列表
				getCommentList()
			} else {
				uni.showToast({
					title: res.message || '评论失败',
					icon: 'none'
				})
			}
		} catch (error) {
			console.error('提交评论失败', error)
			uni.showToast({
				title: '评论失败，请稍后重试',
				icon: 'none'
			})
		}
	}
	
	// 格式化日期
	const formatDate = (dateString) => {
		if (!dateString) return ''
		
		try {
			const date = new Date(dateString)
			return date.toLocaleString('zh-CN', {
				year: 'numeric',
				month: '2-digit',
				day: '2-digit',
				hour: '2-digit',
				minute: '2-digit',
				second: '2-digit'
			})
		} catch (error) {
			console.error('日期格式化失败', error)
			return dateString
		}
	}
	
	onMounted(()=>{
		init()
	})
</script>

<style scoped>
	.comment-title{
		width: 100%;
		font-size: 18px;
		font-weight: bold;
		color: #333;
		margin-bottom: 15px;
	}
	
	.comment-textarea {
		width: 100%;
		min-height: 100px;
	}

	.comment-list {
		width: 100%;
		height: auto;
		margin-top: 20px;
	}

	.comment-content {
		margin: 15px 0px 10px 0px;
		width: 100%;
	}

	.native-textarea {
		width: 100%;
		min-height: 100px;
		padding: 10px;
		box-sizing: border-box;
		border: 1px solid #dcdfe6;
		border-radius: 4px;
		font-size: 14px;
		line-height: 1.5;
	}

	.native-textarea:focus {
		border-color: #000;
		outline: none;
	}

	.native-textarea:disabled {
		background-color: #f5f7fa;
		cursor: not-allowed;
	}
	
	.loading-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 20px 0;
	}
	
	.loading-text {
		margin-top: 10px;
		color: #909399;
		font-size: 14px;
	}
	
	.empty-comment {
		text-align: center;
		padding: 30px 0;
		color: #909399;
		font-size: 14px;
	}
	
	.comment-item {
		margin-bottom: 20px;
		padding-bottom: 15px;
		border-bottom: 1px solid #f0f0f0;
	}
	
	.comment-item:last-child {
		border-bottom: none;
	}
</style>