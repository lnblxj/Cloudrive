<template>
	<cd-page header>
		<!-- #ifdef APP -->
		<u-navbar back-text="返回" title="搜索" height="50"></u-navbar>
		<!-- #endif -->
		<view class="search-container">
			<!-- 搜索区域 -->
			<view class="search-area">
				<u-search
					placeholder="搜索文章..."
					v-model="searchKeyword"
					:show-action="true"
					action-text="搜索"
					shape="round"
					bg-color="#ffffff"
					@search="onSearch"
					@custom="onSearch"
					:border-color="$u.color['primary']"
				></u-search>
			</view>
			
			<!-- 搜索结果列表 -->
			<view class="search-results" v-if="showResult">
				<view 
					v-for="(article, index) in articleList" 
					:key="index"
					class="article-item card-border card-border-hover"
					@click="onArticleClick(article)"
				>
					<view class="article-title">{{article.title}}</view>
					<view class="article-summary">{{article.summary}}</view>
				</view>
				<u-divider v-if="!hasMore" bg-color="#F7F9FE">没有更多了</u-divider>
				<u-loadmore v-else :status="loadStatus" @loadmore="loadMore"></u-loadmore>
			</view>
		</view>
	</cd-page>
</template>

<script setup>
	import { ref, onMounted, getCurrentInstance} from 'vue';
	const {proxy} = getCurrentInstance()
	// 搜索关键词
	const searchKeyword = ref('');
	const articleList = ref([]);
	const pageNum = ref(1);
	const pageSize = ref(10);
	const hasMore = ref(true);
	const loadStatus = ref('loadmore');
	const showResult= ref(false)
	// 从props获取搜索参数
	const props = defineProps({
		q: {
			type: String,
			default: ''
		}
	});
	
	// 初始化搜索
	const initSearch = async () => {
		const q = props.q;
		if (q) {
			searchKeyword.value = q;
			await doSearch();
		}else{
			showResult.value = false
		}
	};
	
	// 执行搜索
	const doSearch = async () => {
		showResult.value = true
		loadStatus.value = 'loading';
		try {
			const res = await proxy.$http.search({
				keyword: searchKeyword.value,
				pageNum: pageNum.value,
				pageSize: pageSize.value
			});
			if (res.isSuccess) {
				const data = res.data;
				if (pageNum.value === 1) {
					articleList.value = data.records;
				} else {
					articleList.value = [...articleList.value, ...data.records];
				}
				hasMore.value = data.current < data.pages;
				loadStatus.value = hasMore.value ? 'loadmore' : 'nomore';
			}
		} catch (e) {
			console.error(e);
			loadStatus.value = 'loadmore';
		}
	};
	
	// 搜索方法
	const onSearch = () => {
		pageNum.value = 1;
		doSearch();
	};
	
	// 加载更多
	const loadMore = () => {
		if (hasMore.value) {
			pageNum.value++;
			doSearch();
		}
	};
	
	// 初始化
	onMounted(() => {
		initSearch();
	});
	
	// 点击文章项
	const onArticleClick = (article) => {
		uni.navigateTo({
			url: `/pages/post/post?id=${article.id}`
		});
	};
</script>

<style scoped>
	.search-container {
		width: 100%;
		padding: 0 20rpx;
	}
	
	.search-area {
		max-width: 850rpx;
		margin: 0 auto 40rpx;
		padding: 20rpx 0;
	}
	
	.search-results {
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
		cursor: pointer;
	}
	
	.article-item:hover {
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
	}
	
	.article-title {
		font-size: 36rpx;
		font-weight: 600;
		color: #333333;
		margin-bottom: 16rpx;
		line-height: 1.4;
	}
	
	.article-summary {
		font-size: 28rpx;
		color: #666666;
		line-height: 1.5;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 3;
		overflow: hidden;
	}
	
	/* 响应式适配 */
	@media screen and (max-width: 768px) {
		.search-container {
			padding: 0 10rpx;
		}
		
		.search-area {
			width: 90%;
		}
		
		.search-results {
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
	}
</style>
