<template>
	<view class="tabs-container card-border card-border-hover">
		<u-tabs :list="categoryList" is-scroll v-model="currentCategory" @change="changeCategory"></u-tabs>
	</view>
	<view class="card-list">
		<square-card v-for="(item, index) in displayList" :key="index" 
			:cover="item.thumbnail"
			:topTitle="item.title"
			:subTitle="item.summary" 
			:topTag="item.topTag"
			:tags="JSON.parse(item.tags)" 
			:viewCount="item.viewCount.toString()" 
			:commCount="item.commentCount.toString()"
			:id="String(item.id)">
		</square-card>
		<u-divider v-if="!hasMore" bg-color="#F7F9FE">没有更多了</u-divider>
		<u-loadmore v-else :status="loadStatus" @loadmore="loadMore"></u-loadmore>
	</view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import squareCard from './SquareCard.vue'
import api from '@/request/api.js'

// 分类列表数据
const categoryList = ref([{
		name: '全部',
		value: 0
	},
	{
		name: '文档',
		value: 1
	},
	{
		name: '视频',
		value: 2
	},
	{
		name: '电影',
		value: 3
	},
	{
		name: '软件',
		value: 4
	},
	{
		name: '课程',
		value: 5
	},
	{
		name: '音乐',
		value: 6
	},
	{
		name: '书籍',
		value: 7
	},
	{
		name: '图片',
		value: 8
	}
]);

// 当前选中的分类
const currentCategory = ref(0);

// 文章列表数据
const postList = ref([]);
const currentPage = ref(1);
const pageSize = ref(6);
const total = ref(0);
const hasMore = ref(true);
const loadStatus = ref('loading');

// 获取文章列表
const getPostList = async () => {
	try {
		const response = await api.getPostList(currentPage.value, pageSize.value, currentCategory.value);
		if (response.isSuccess) {
			const { records, total: totalCount, current, pages } = response.data;
			postList.value = [...postList.value, ...records];
			total.value = totalCount;
			hasMore.value = current < pages;
			loadStatus.value = hasMore.value ? 'loadmore' : 'nomore';
		}
	} catch (error) {
		console.error('获取文章列表失败:', error);
		loadStatus.value = 'loadmore';
	}
};

// 加载更多
const loadMore = () => {
	if (hasMore.value) {
		currentPage.value++;
		loadStatus.value = 'loading';
		getPostList();
	}
};

// 分类切换事件处理
const changeCategory = (index) => {
	currentCategory.value = index;
	postList.value = [];
	currentPage.value = 1;
	hasMore.value = true;
	loadStatus.value = 'loading';
	getPostList();
};

// 计算属性：根据当前分类过滤显示的列表
const displayList = computed(() => postList.value);
// 初始化加载
onMounted(() => {
	getPostList();
});
</script>

<style scoped>
.tabs-container {
	margin-bottom: 20rpx;
	width: 100%;
	overflow: hidden;
	border-radius: 20rpx;
}

.card-list {
	padding: 0 10rpx;
}
</style>