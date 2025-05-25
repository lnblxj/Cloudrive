<template>
	<view class="square-card-container card-border card-border-hover" @click="navigateToPost">
		<!-- #ifdef WEB -->
		<u-row gutter="0" style="cursor: pointer;">
			<u-col span="3">
				<view class="line-container img-hover-box">
					<!-- 					<u-image class="img-hover" width="100%" height="200px" :src="cover" border-radius="0" mode="scaleToFill" :fade="true"
						duration="450"></u-image> -->
					<image class="img-hover" :src="cover" mode="scaleToFill" style="width: 100%; height: 200px;">
					</image>
				</view>
			</u-col>
			<u-col span="9">
				<view class="line-container">
					<view style="height: 150px;">
						<u-row>
							<view class="info-container">
								<u-text :text="topTitle" font="PingFang SC" size="20px" :lines="2" bold></u-text>
							</view>
						</u-row>
						<u-row>
							<view class="info-container">
								<u-text :text="subTitle" :lines="2" overflow="clip" size="16px" color="#666"></u-text>
							</view>
						</u-row>
					</view>
					<view class="info-container">
						<u-row>
							<u-col :span="6">
								<u-row>
									<u-tag class="tag-spacing" :text="topTagText" :bgColor="topTagColor" color="#fff"
										border-color="#fff"></u-tag>
									<u-tag class="tag-spacing" v-for="(item, index) in tags" :key="index"
										:text="item"></u-tag>
								</u-row>
							</u-col>
							<u-col :span="6">
								<u-row justify="right" style="width: 100%;">
									<u-col :span="3">
										<u-row>
											<u-icon name="eye" size="50"></u-icon>
											<u-text :text="viewCount"></u-text>
										</u-row>
									</u-col>
									<u-col :span="3">
										<u-row>
											<u-icon name="chat" size="50"></u-icon>
											<u-text :text="commCount"></u-text>
										</u-row>
									</u-col>
								</u-row>
							</u-col>
						</u-row>
					</view>
				</view>
			</u-col>
		</u-row>
		<!-- #endif -->

		<!-- #ifdef APP -->
		<view class="mobile-card-image">
			<u-image width="100%" height="300rpx" :src="cover" border-radius="0" mode="scaleToFill" :fade="true"
				duration="450"></u-image>
		</view>
		<view class="info-container">
			<u-row>
				<u-text :text="topTagText" size="15px"></u-text>
			</u-row>
			<u-row>
				<view class="title-container">
					<u-text :text="topTitle" font="PingFang SC" size="18px" :lines="2" bold
						overflow="ellipsis"></u-text>
				</view>
			</u-row>
			<u-row class="tag-and-stats-container">
				<view class="tag-container">
					<u-tag class="tag-spacing" v-for="(item, index) in tags" :key="index" :text="item"
						size="15px"></u-tag>
				</view>
				<view class="stats-container">
					<view class="stat-item">
						<u-icon name="eye" size="40"></u-icon>
						<u-text :text="viewCount" size="30rpx"></u-text>
					</view>
					<view class="stat-item">
						<u-icon name="chat" size="40"></u-icon>
						<u-text :text="commCount" size="30rpx"></u-text>
					</view>
				</view>
			</u-row>
		</view>
		<!-- #endif -->
	</view>
</template>

<script setup>
	import {
		computed,
		defineProps,
		defineEmits
	} from 'vue';

	/**
	 * @name square-card
	 * @description 资源广场卡片
	 * 
	 * @property {String} topTitle    String | 内容主标题
	 * @property {String} subTitle    String | 内容副标题
	 * @property {String} cover       String | 封面
	 * @property {String} viewCount   String | 浏览量
	 * @property {String} commCount   String | 评论量
	 * @property {String} topTag      String | 顶级标签
	 * @property {String[]} tags      String[] | 次级标签
	 * @property {String} id          String | ID
	 */

	const props = defineProps({
		topTitle: {
			type: String,
			default: '标题文字'
		},
		subTitle: {
			type: String,
			default: '副标题文字'
		},
		cover: {
			type: String,
			default: ''
		},
		viewCount: {
			type: String,
			default: '0'
		},
		commCount: {
			type: String,
			default: '0'
		},
		topTag: {
			type: String,
			default: ''
		},
		tags: {
			type: Array,
			default: []
		},
		id: {
			type: String,
			default: ''
		}
	});

	const topTagText = computed(() => {
		switch (props.topTag) {
			case 'selected':
				return '精选';
			case 'popular':
				return '热门';
			default:
				return '资源';
		}
	});

	const topTagColor = computed(() => {
		switch (props.topTag) {
			case 'selected':
				return '#f07b3f';
			case 'popular':
				return '#ea5455';
			default:
				return '#2d4059';
		}
	});
	// 跳转到文章详情页
	const navigateToPost = () => {
		uni.navigateTo({
			url: `/pages/post/post?id=${props.id}`
		});
	};
</script>

<style lang="scss" scoped>
	.square-card-container {
		margin-bottom: 20rpx;
		border-radius: 16rpx;
		overflow: hidden;
		background-color: #fff;
	}

	.tag-spacing {
		margin-right: 10px;
	}

	/*#ifdef WEB */
	.info-container {
		margin-top: 5px;
		width: 100%;
		padding: 5px 10px;
	}

	.line-container {
		height: 200px;
		width: 100%;
	}

	/* #endif */

	/*#ifdef APP */
	.title-container {
		margin: 5px 0 5px 0;
		height: 50px;
		width: 100%;
	}

	.info-container {
		margin: 16px 12px;
	}

	.mobile-card-image {
		margin: 0;
		padding: 0;
		width: 100%;
	}

	.tag-and-stats-container {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 10rpx 0;
	}

	.stats-container {
		display: flex;
		align-items: center;
		justify-content: flex-end; /* 确保统计数据右对齐 */
		margin-left: auto; /* 添加此行以确保右对齐 */
	}

	.stat-item {
		display: flex;
		align-items: center;
		margin-left: 20rpx;
	}
	/* #endif */
</style>