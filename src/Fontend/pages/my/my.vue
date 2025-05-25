<template>
	<cd-page>
		<!-- #ifdef WEB -->
		<u-row>
			<view class="user-info-container">
				<user-info-card :avatar-contain-style="{marginTop: '2%'}"
					:container-style="{height: '200px', backgroundColor: '#4f65f1', borderRadius: '15rpx'}"
					:name-contain-style="{marginTop: '1%'}"></user-info-card>
			</view>
		</u-row>
		<view class="cards">
			<u-row style="width: 100%;">
				<u-col span="8">
					<view class="left-card">
						<scroll-view scroll-y="true">
							<mySettings></mySettings>
						</scroll-view>
					</view>
				</u-col>
				<u-col span="4">
					<view class="right-card card-border">
						<l-liquid v-model:current="modelVale" :percent="target" size="200px" outline>
							<text>{{modelVale}}%</text>
						</l-liquid>
						<text class="capacity-text">{{ formatFileSize(used) }}/{{ formatFileSize(total) }}</text>
					</view>
				</u-col>
			</u-row>
		</view>
		<!-- #endif -->
		<!-- #ifdef APP -->
		<u-navbar title="我的" immersive :is-back="false" height="50">
			<template v-slot:right>
				<view class="nav-btn-group">
					<view class="nav-btn">
						<u-icon name="scan" size="40" @click="handleScanClick"></u-icon>
					</view>
				</view>
			</template>
		</u-navbar>
		<scroll-view scroll-y="true" style="height: auto;">
			<u-row class="row-card">
				<view class="user-info-container">
					<user-info-card :avatar-contain-style="{marginTop: '2%'}"
						:container-style="{height: '200px', backgroundColor: '#4f65f1', borderRadius: '15rpx'}"
						:name-contain-style="{marginTop: '1%'}"></user-info-card>
				</view>
			</u-row>
			<u-row class="row-card">
				<view class="capacity-card">
					<u-row>
						<text>容量信息</text>
					</u-row>
					<u-row>
						<u-col :span="8">
							<u-line-progress active-color="#2979ff" :percent="percent"></u-line-progress>
						</u-col>
						<u-col :span="4">
							<text class="capacity-text">{{ formatFileSize(used) }}/{{ formatFileSize(total) }}</text>
						</u-col>
					</u-row>
				</view>
			</u-row>
			<u-row class="row-card">
				<mySettings></mySettings>
			</u-row>
		</scroll-view>
		<!-- #endif -->
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
	import mySettings from './mySettings.vue'
	import {ref, onMounted, getCurrentInstance} from 'vue';
	import {tabbarData, tabbarConfig} from '@/common/tabbar.js';
	import { useUserStore } from '@/stores/modules/user';
	const userStore = useUserStore()
	const {proxy} = getCurrentInstance()
	// 数据
	const target = ref(50)
	const modelVale = ref(0)
	const used = ref(500)
	const total = ref(1024)
	const percent = ref(50)
	
	// 格式化文件大小函数
	const formatFileSize = (size) => {
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
	
	const loadCapacityInfo = async () =>{
		try {
			const res = await proxy.$http.getCapacityInfo(userStore.id)
			if (!res || !res.isSuccess || !res.data) {
				throw new Error(res?.message || '获取容量信息失败')
			}
			const data = res.data
			total.value = data.totalCapacity
			used.value = data.usedCapacity
			percent.value = data.usageRate
			target.value = data.usageRate
			modelVale.value = data.usageRate
		} catch (error) {
			uni.showToast({
				title: error.message || '网络请求异常',
				icon: 'none'
			})
			console.error('获取容量信息失败:', error)
		}
	}
	
	const init = ()=>{
		loadCapacityInfo()
	}
	
	// #ifdef APP
	const handleScanClick = () => {
		uni.navigateTo({
			url: '/pages/auth/codeScanner/codeScanner'
		})
	}
	// #endif
	
	onMounted(()=>{
		init()
	})
</script>

<style scoped>
	@import '@/common/style.scss';
	.user-info-container {
		width: 100%;
		height: 200px;
		border-radius: 15px;
	}

	.cards {
		display: flex;
		flex-direction: row;
		margin-top: 10px;
	}

	.right-card,
	.left-card {
		flex-shrink: 0;
		height: 500px;
	}

	.left-card {
		width: 100%;
		overflow: hidden;
	}

	.left-card scroll-view {
		height: 100%;
	}

	.right-card {
		width: 100%;
		background-color: #fff;
		border-radius: 10px;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}

	.user-info-container,
	.right-card {
		border: 1px solid #e3e8f7;
	}

	.capacity-text {
		font-size: 60rpx;
	}

	/* #ifdef APP */
	.row-card {
		width: 100%;
		margin-bottom: 10rpx;
	}

	.capacity-card {
		border: 1px solid #e3e8f7;
		background-color: #fff;
		width: 100%;
		border-radius: 15rpx;
		padding: 10rpx;
	}

	.capacity-text {
		font-size: 30rpx;
	}
	.nav-btn-group{
		display: flex;
		align-items: center;
		margin-right: 30rpx;
	}
	
	.nav-btn{
		margin-left: 20rpx;
	}

	scroll-view {
		height: 100vh;
	}
	/* #endif */
</style>