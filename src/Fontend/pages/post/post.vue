<template>
	<cd-page header>
		<!-- #ifdef APP -->
		<u-navbar back-text="返回" :title="postTitle" height="50"></u-navbar>
		<!-- #endif -->
		<scroll-view scroll-y="true" class="scroll-container">
			<view class="main-container">
				<view class="swiper-container">
					<swiper class="swiper" circular :indicator-dots="true" :interval="3000" :duration="500"
						@change="onSwiperChange">
						<swiper-item v-for="(image, index) in images" :key="index" v-if="images">
							<image :src="image" mode="aspectFill" class="swiper-image" @click="previewImage">
							</image>
						</swiper-item>
					</swiper>
				</view>
				<view class="title-container">
					<u-text selectable :text="postTitle" bold :size="titleSize"></u-text>
				</view>
				<view class="header-container">
					<view class="tag-container">
						<u-tag class="tag-spacing" :text="topTagText" :bgColor="topTagColor" color="#fff"
							border-color="#fff"></u-tag>
						<u-tag class="tag-spacing" v-for="(item, index) in tags" :key="index"
							:text="item"></u-tag>
					</view>
					<view class="report-btn" @click="handleReportClick">
						<u-icon name="warning"></u-icon>
						<text>举报</text>
					</view>
				</view>
				<view class="content-container">
					<u-text selectable :text="postContent" :size="contentSize" v-if="!isMarkdown"></u-text>
					<zero-markdown-view v-if="isMarkdown" theme-color="#000"
						:markdown="postContent"></zero-markdown-view>
				</view>
				<view class="share-card" v-if="postData && postData.shareLink">
					<cd-share-card :shareURL="postData.shareLink" :encryption="postData.needPassword"
						:passWord="postData.password"></cd-share-card>
				</view>
				<view class="comment-container">
					<cd-comment :post-id="props.id"></cd-comment>
				</view>
			</view>
		</scroll-view>
	</cd-page>
</template>

<script setup>
	import {
		ref,
		computed,
		watch,
		onMounted,
		getCurrentInstance
	} from 'vue';
	const { proxy } =getCurrentInstance()
	//平台样式
	// #ifdef WEB
	const titleSize = ref('30px')
	const contentSize = ref('20px')
	const markdownHeight = ref('auto')
	// #endif

	// #ifdef APP
	const titleSize = ref('23px')
	const contentSize = ref('18px')
	const markdownHeight = ref('auto')
	// #endif

	// 文章数据
	const postTitle = ref('')
	const postContent = ref('')
	const topTag = ref('')
	const tags = ref([])
	const isMarkdown = ref(false)
	const images = ref([])
	const postData = ref(null)

	// 获取文章详情
	const getPostDetail = async (id) => {
		try {
			const res = await proxy.$http.getPostDetail(id)
			if (res.isSuccess) {
				postData.value = res.data
				postTitle.value = res.data.title
				postContent.value = res.data.content
				topTag.value = res.data.topTag || ''
				tags.value = res.data.tags ? JSON.parse(res.data.tags) : []
				isMarkdown.value = res.data.isMarkdown === 1
				// 设置轮播图
				if (res.data.imageUrls) {
					try {
						const imageUrlsArray = JSON.parse(res.data.imageUrls);
						images.value = imageUrlsArray;
					} catch (error) {
						console.error('解析imageUrls失败', error);
						images.value = [
							'/static/editor/aliyunpan.png',
							'/static/editor/baiduwangpan.png',
							'/static/editor/cloudrive.png'
						];
					}
				} else {
					// 默认图片
					images.value = [
						'/static/editor/aliyunpan.png',
						'/static/editor/baiduwangpan.png',
						'/static/editor/cloudrive.png'
					];
				}
			} else {
				uni.showToast({
					title: res.message || '获取文章失败',
					icon: 'none'
				})
			}
		} catch (error) {
			console.error('获取文章详情失败', error)
			uni.showToast({
				title: '获取文章失败，请稍后重试',
				icon: 'none'
			})
		}
	}

	const props = defineProps({
		id: {
			type: [String, Number],
			default: ''
		}
	});

	onMounted(() => {
		const postId = props.id;
		if (postId != '') {
			getPostDetail(postId);
		} else {
			uni.showToast({
				title: '获取文章ID失败，请稍后重试',
				icon: 'none'
			})
		}
	})



	//计算相关
	const currentIndex = ref(0)

	function onSwiperChange(e) {
		currentIndex.value = e.detail.current
	}

	function previewImage() {
		uni.previewImage({
			urls: images.value,
			current: currentIndex.value
		});
	}

	const topTagText = computed(() => {
		switch (topTag.value) {
			case 'selected':
				return '精选';
			case 'popular':
				return '热门';
			default:
				return '资源';
		}
	});

	const topTagColor = computed(() => {
		switch (topTag.value) {
			case 'selected':
				return '#f07b3f';
			case 'popular':
				return '#ea5455';
			default:
				return '#2d4059';
		}
	});

	// 根据内容计算Markdown高度
	const calculateMarkdownHeight = () => {
		let baseHeight = 200;
		const contentLength = postContent.value.length;
		// #ifdef WEB
		const additionalHeight = Math.ceil(contentLength / 50) * 50;
		// #endif
		// #ifdef APP
		const additionalHeight = Math.ceil(contentLength / 20) * 50;
		// #endif
		const calculatedHeight = Math.max(baseHeight, baseHeight + additionalHeight);
		return calculatedHeight + 'px';
	};

	// 监听内容变化，动态更新高度
	watch(postContent, () => {
		if (isMarkdown.value) {
			markdownHeight.value = calculateMarkdownHeight();
		}
	}, {
		immediate: true
	});
	
	const handleReportClick = () =>{
		let reportUrl = '/pages/post/report?id=' + props.id
		uni.navigateTo({
			url: reportUrl
		})
	}
</script>

<style scoped>
	.main-container{
		background-color: #fff;
	}
	.scroll-container {
		width: 100%;
		height: auto;
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
	}
	
	.swiper {
		width: 100%;
		height: 100%;
	}

	.swiper-image {
		width: 100%;
		height: 100%;
	}

	.tag-spacing {
		margin-right: 10px;
	}

	.header-container {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin: 0px 0px 5px 0px;
		padding: 20rpx;
	}

	.tag-container {
		display: flex;
		align-items: center;
	}

	.report-btn {
		display: flex;
		align-items: center;
		gap: 4px;
		/* #ifdef WEB */
		cursor: pointer;
		/* #endif */
	}

	.report-btn text{
		font-size: 30rpx;
		color: #666;
	}

	.comment-container,
	.share-card {
		width: 100%;
	}

	/* #ifdef WEB */
	.scroll-container{
		margin-top: 120rpx;
	}
	.main-container {
		width: 60%;
		margin: 30rpx auto;
		border-radius: 30rpx;
		height: auto;
		overflow: hidden;
		border: 1px solid #e3e8f7;
	}

	.swiper-container {
		height: 300px;
	}

	.title-container {
		margin: 10px;
	}

	.content-container, .comment-container, .share-card {
		margin: 20px 0px 20px 0px;
		padding: 0 30rpx 0 30rpx;
	}
	
	.content-container{
		min-height: 600rpx;
	}
	.share-card{
		margin-top: 50rpx;
		margin-bottom: 100rpx;
	}
	.post-card{
		margin-top: 20rpx;
		border-radius: 20rpx;
	}
	/* #endif */
	/* #ifdef APP */
	.scroll-container{
		margin-top: 105rpx;
	}
	.main-container {
		width: 100%;
	}

	.swiper-container {
		height: 250px;
	}

	.title-container {
		margin: 10px;
	}

	.content-container {
		margin: 20px 0px 20px 0px;
	}

	/* #endif */
</style>