<template>
	<cd-page>
		<!-- <cd-devpanel></cd-devpanel> -->
		<scroll-view scroll-y="true" direction="vertical" class="page-container" :show-scrollbar="false">
			<!-- #ifdef WEB -->
			<u-row>
				<u-col :span='9.5'>
					<view class="topbar-container">
						<cd-dynamic-bg style="z-index: 0;">
							<view class="search-container">
								<u-text text="Cloudrive" align="center" size="120rpx"></u-text>
								<u-gap></u-gap>
								<u-search placeholder="搜索" :showCancel="false" borderColor="#9a9a9a"
									animation v-model="searchKeyword" @search="handleSearch"></u-search>
							</view>
						</cd-dynamic-bg>
					</view>
				</u-col>
				<u-col :span='2.5'>
					<view class="topbar-container">
						<user-info-card :container-style="userInfoContainerStyle"></user-info-card>
					</view>
				</u-col>
			</u-row>
			<view class="atricle">
				<view class="card-list">
					<squareCardList></squareCardList>
				</view>
				<view class="aside">
					<view class="aside-one">
						<u-row>
							<u-col :span="6">
								<view class="aside-one-text-box">
									<u-count-to :endVal="popularCount"></u-count-to>
								</view>
								<view class="aside-one-text-box">
									<u-text text="热门" align="center" size="15px"></u-text>
								</view>
							</u-col>
							<u-col :span="6">
								<view class="aside-one-text-box">
									<u-count-to :endVal="selectedCount"></u-count-to>
								</view>
								<view class="aside-one-text-box">
									<u-text text="精选" align="center" size="15px"></u-text>
								</view>
							</u-col>
						</u-row>
					</view>
					<view class="aside-two">
						<view style="width: 60%; margin: 0 auto;">
							<view v-if="!userStore.isLoggedIn">
							<cd-icon-button
							text="登录" 
							icon-name="man-add" 
							text-color="#fff" 
							icon-color="#fff"
							icon-size="45" 
							bg-color="#2aae67" 
							hover hover-color="#ffb115"
							to="/pages/auth/login/login"
							></cd-icon-button>
							</view>
							<view v-else>
								<cd-icon-button 
								text="创建分享文章" 
								icon-name="plus-circle" 
								text-color="#fff" 
								icon-color="#fff"
								icon-size="45" 
								:bg-color="$u.color['primary']" 
								hover hover-color="#ffb115"
								to='/pages/editor/editor'
								></cd-icon-button>
								<u-gap />
								<cd-icon-button 
								text="文章管理" 
								icon-name="list" 
								text-color="#fff" 
								icon-color="#fff"
								icon-size="45" 
								bg-color="#fc664e" 
								hover hover-color="#ffb115"
								to='/pages/myPosts/myPosts'></cd-icon-button>
							</view>
						</view>
					</view>
				</view>
			</view>
			<!-- #endif -->
			<!-- #ifdef APP -->
			<u-navbar title="广场" immersive :is-back="false" height="50">
				<template v-slot:right>
					<view class="nav-btn-group">
						<view class="nav-btn">
							<u-icon name="plus-circle" size="40" @click="handleNavBtnClick('left')"></u-icon>
						</view>
						<view class="nav-btn">
							<u-icon name="search" size="40" @click="handleNavBtnClick('mid')"></u-icon>
						</view>
						<view class="nav-btn">
							<u-icon name="list" size="40" @click="handleNavBtnClick('right')"></u-icon>
						</view>
					</view>
				</template>
			</u-navbar>
			<squareCardList></squareCardList>
			<!-- #endif -->
		</scroll-view>
	</cd-page>
	<view :style="tabbarConfig.containerStyle">
		<u-tabbar 
		:list="tabbarData" 
		height="100" 
		:content-style="tabbarConfig.style"
		:bg-color="tabbarConfig.props.bgColor"
		></u-tabbar>
	</view>
</template>

<script setup>
	import { ref, onMounted, onUnmounted, nextTick, getCurrentInstance } from 'vue';
	import staticConfig from '@/config/static_config.uts';
	import {tabbarData, tabbarConfig} from '@/common/tabbar.js';
	import squareCardList from './SquareCardList.vue';
	import { useUserStore } from '@/stores/modules/user';
	const userStore = useUserStore()
	const { proxy } = getCurrentInstance()
	// #ifdef WEB
	const userInfoContainerStyle = {
		backgroundColor: '#485fef',
		height: '100vh',
		paddingTop: '20px'
	}
	const logo = staticConfig.logo;

	
	// 数据
	const iconButtonText = ref('创建新分享')
	const popularCount = ref(0)
	const selectedCount = ref(0)
	const searchKeyword = ref('')
	
	const getPostCount = async () =>{
		const res = await proxy.$http.getPostCount()
		if (res.isSuccess) {
			popularCount.value = res.data.popularCount
			selectedCount.value = res.data.selectedCount
		}
	}

	function handleSearch() {
		if (!searchKeyword.value.trim()) {
			return
		}
		uni.navigateTo({
			url: `/pages/search/search?q=${searchKeyword.value}`
		})
	}
	// #endif
	
	// #ifdef APP
	const handleNavBtnClick = (name)=>{
		let target = ''
		switch(name){
			case 'left':
			target = '/pages/editor/editor'
			break
			case 'right':
			target = '/pages/myPosts/myPosts'
			break
			case 'mid':
			target = '/pages/search/search'
			break
		}
		uni.navigateTo({
			url: target
		})
	}
	// #endif

	
	onMounted(()=>{
		// #ifdef WEB
			getPostCount()
		// #endif
	})
</script>

<style scoped>
	/* #ifdef WEB */
	.topbar-container {
		height: 250px;
		width: 100%;
		border-radius: 10px;
		position: relative;
		overflow: hidden;
	}
	
	.card-container {
		border-radius: 15px;
		position: relative;
		overflow: hidden;
	}
	
	.search-container {
		text-align: center;
		margin: 0 auto;
		width: 80%;
		top: 10%;
		padding-top: 2%;
		padding-bottom: 2%;
	}
	
	.atricle {
		width: 100%;
		display: flex;
		flex-direction: row;
		margin-top: 10px;
	}
	
	.card-list {
		width: 78.8%;
		flex-shrink: 0;
		margin-right: 10px;
	}
	
	.aside {
		width: 20%;
		height: 230px;
		flex-shrink: 0;
	}
	
	.aside-one,
	.aside-two {
		width: 100%;
		border-radius: 10px;
		padding: 10px 0px 10px 0px;
		background-color: #fff;
	}
	
	.aside-one-text-box {
		text-align: center;
		width: 100%;
	}
	
	.aside-two {
		margin-top: 10px;
		padding: 10px;
	}
	
	.card-container,
	.topbar-container,
	.aside-one,
	.aside-two {
		border: 1px solid #e3e8f7;
	}
	/* #endif */
	/* #ifdef APP */
	.page-container{
		width: 100%;
	}
	.nav-btn-group{
		display: flex;
		align-items: center;
		margin-right: 30rpx;
	}
	
	.nav-btn{
		margin-left: 20rpx;
	}
	/* #endif */
</style>