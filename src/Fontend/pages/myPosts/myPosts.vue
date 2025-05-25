<template>
	<cd-page header>
		<!-- #ifdef APP -->
		<u-navbar back-text="返回" title="文章管理" height="50"></u-navbar>
		<!-- #endif -->
		<view class="post-list-container">
			<!-- 文章列表 -->
			<view class="post-list">
				<view 
					v-for="(article, index) in articleList" 
					:key="index"
					class="article-item card-border card-border-hover"
				>
					<view class="article-header">
						<view class="article-title" @click="onArticleClick(article)">{{article.title}}</view>
						<view class="article-status" :class="getStatusClass(article.status)">
							{{getStatusText(article.status)}}
						</view>
					</view>
					<view class="article-summary" @click="onArticleClick(article)">{{article.summary}}</view>
					<view class="article-footer">
						<view class="article-time">{{formatDate(article.createTime)}}</view>
						<view class="article-actions">
							<button class="delete-btn" @click="showDeletePopup(article)">删除</button>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 删除确认弹窗 -->
		<u-popup v-model="showDeleteConfirm" mode="center" width="600rpx" border-radius="16">
			<view class="delete-popup-content">
				<view class="delete-popup-title">删除确认</view>
				<view class="delete-popup-message">确定要删除这篇文章吗？此操作不可撤销。</view>
				<view class="delete-popup-buttons">
					<u-button @click="cancelDelete">取消</u-button>
					<u-button @click="handleDelete" type="primary">确定</u-button>
<!-- 					<view class="cancel-btn" >取消</view>
					<view class="confirm-btn" @click="handleDelete">确定</view> -->
				</view>
			</view>
		</u-popup>
	</cd-page>
</template>

<script setup>
	import { ref, onMounted, getCurrentInstance } from 'vue';
	
	const { proxy } = getCurrentInstance();
	const articleList = ref([]);
	const showDeleteConfirm = ref(false);
	const currentArticle = ref(null);
	
	// 获取文章列表
	const fetchArticleList = async () => {
		try {
			const res = await proxy.$http.getUserPostList();
			if (res && res.isSuccess) {
				articleList.value = res.data || [];
			}
		} catch (error) {
			console.error('获取文章列表失败', error);
			uni.showToast({
				title: '获取文章列表失败',
				icon: 'none'
			});
		}
	};
	
	// 获取状态样式类
	const getStatusClass = (status) => {
		switch (status) {
			case 0: return 'status-pending';
			case 1: return 'status-normal';
			case 2: return 'status-off';
			default: return '';
		}
	};
	
	// 获取状态文字
	const getStatusText = (status) => {
		switch (status) {
			case 0: return '审核中';
			case 1: return '正常';
			case 2: return '下架';
			default: return '未知';
		}
	};
	
	// 格式化日期
	const formatDate = (dateString) => {
		if (!dateString) return '';
		const date = new Date(dateString);
		return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
	};
	
	// 文章点击
	const onArticleClick = (article) => {
		uni.navigateTo({
			url: `/pages/postDetail/postDetail?id=${article.id}`
		});
	};
	
	// 显示删除弹窗
	const showDeletePopup = (article) => {
		currentArticle.value = article;
		showDeleteConfirm.value = true;
	};
	
	// 取消删除
	const cancelDelete = () => {
		showDeleteConfirm.value = false;
		currentArticle.value = null;
	};
	
	// 执行删除
	const handleDelete = async () => {
		if (!currentArticle.value) return;
		
		try {
			const res = await proxy.$http.deletePost(currentArticle.value.id);
			if (res && res.isSuccess) {
				uni.showToast({
					title: '删除成功',
					icon: 'success'
				});
				// 重新获取列表或从当前列表中移除
				articleList.value = articleList.value.filter(item => item.id !== currentArticle.value.id);
			} else {
				uni.showToast({
					title: res?.message || '删除失败',
					icon: 'none'
				});
			}
		} catch (error) {
			console.error('删除文章失败', error);
			uni.showToast({
				title: '删除失败，请稍后重试',
				icon: 'none'
			});
		} finally {
			showDeleteConfirm.value = false;
			currentArticle.value = null;
		}
	};
	
	onMounted(() => {
		fetchArticleList();
	});
</script>

<style scoped>
	.post-list-container {
		width: 100%;
		padding: 0 20rpx;
	}
	
	.search-area {
		max-width: 850rpx;
		margin: 0 auto 40rpx;
		padding: 20rpx 0;
	}
	
	.post-list {
		max-width: 1060rpx;
		margin: 0 auto;
	}
	
	.article-item {
		background-color: #ffffff;
		padding: 30rpx;
		border-radius: 16rpx;
		margin-bottom: 30rpx;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
		transition: box-shadow 0.3s;
	}
	
	.article-item:hover {
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
	}
	
	.article-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 16rpx;
	}
	
	.article-title {
		font-size: 36rpx;
		font-weight: 600;
		color: #333333;
		line-height: 1.4;
		flex: 1;
		cursor: pointer;
	}
	
	.article-status {
		font-size: 24rpx;
		padding: 4rpx 16rpx;
		border-radius: 100rpx;
		white-space: nowrap;
		margin-left: 20rpx;
	}
	
	.status-pending {
		background-color: rgba(255, 152, 0, 0.1);
		color: #ff9800;
	}
	
	.status-normal {
		background-color: rgba(76, 175, 80, 0.1);
		color: #4caf50;
	}
	
	.status-off {
		background-color: rgba(244, 67, 54, 0.1);
		color: #f44336;
	}
	
	.article-summary {
		font-size: 28rpx;
		color: #666666;
		line-height: 1.5;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 3;
		overflow: hidden;
		cursor: pointer;
		margin-bottom: 20rpx;
	}
	
	.article-footer {
		display: flex;
		justify-content: space-between;
		align-items: center;
		font-size: 24rpx;
		color: #999;
		margin-top: 10rpx;
	}
	
	.delete-btn {
		font-size: 24rpx;
		color: #f44336;
		background: none;
		border: 1px solid #f44336;
		border-radius: 100rpx;
		padding: 4rpx 20rpx;
		line-height: 1.5;
		cursor: pointer;
	}
	
	.delete-btn:hover {
		background-color: rgba(244, 67, 54, 0.1);
	}
	
	/* 删除弹窗样式 */
	.delete-popup-content {
		padding: 40rpx 30rpx;
		text-align: center;
	}
	
	.delete-popup-title {
		font-size: 32rpx;
		font-weight: 600;
		color: #333;
		margin-bottom: 20rpx;
	}
	
	.delete-popup-message {
		font-size: 28rpx;
		color: #666;
		margin-bottom: 40rpx;
		padding: 0 20rpx;
	}
	
	.delete-popup-buttons {
		display: flex;
		justify-content: space-between;
		margin-top: 5rpx;
		padding-top: 5rpx;
	}
	
	.cancel-btn, .confirm-btn {
		flex: 1;
		padding: 20rpx 0;
		font-size: 28rpx;
		text-align: center;
		cursor: pointer;
	}
	
	.cancel-btn {
		color: #666;
		border-right: 1px solid #eee;
	}
	
	.confirm-btn {
		color: #f44336;
		font-weight: 500;
	}
	
	/* 响应式适配 */
	@media screen and (max-width: 768px) {
		.post-list-container {
			padding: 0 10rpx;
		}
		
		.search-area {
			width: 90%;
		}
		
		.post-list {
			width: 95%;
		}
		
		.article-title {
			font-size: 32rpx;
		}
		
		.article-summary {
			font-size: 26rpx;
		}
		
		.article-item {
			padding: 20rpx;
			margin-bottom: 20rpx;
		}
		
		.article-status {
			font-size: 22rpx;
			padding: 2rpx 12rpx;
		}
		
		.delete-btn {
			font-size: 22rpx;
			padding: 2rpx 16rpx;
		}
	}
</style>
