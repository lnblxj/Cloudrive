<template>
	<view class="settings-container card-border">
		<!-- 引入弹窗组件，使用v-model绑定方式 -->
		<my-pop ref="myPopRef"></my-pop>
		<!-- 个人信息卡片 -->
		<view class="settings-card">
			<view class="card-header">
				<u-text text="个人信息" size="30" bold></u-text>
			</view>
			<view class="card-content">
				<view class="info-list">
					<view class="info-item">
						<text class="info-label">用户名</text>
						<text class="info-value">{{ userInfo.nickname }}</text>
					</view>
					<view class="info-item">
						<text class="info-label">邮箱</text>
						<text class="info-value">{{ userInfo.email }}</text>
					</view>
					<view class="info-item">
						<text class="info-label">账户状态</text>
						<text class="info-value">{{ userInfo.status }}</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 折叠面板 -->
		<u-collapse>
			<!-- 设置选项卡片 -->
			<u-collapse-item title="设置" name="settings">
				<view class="card-content">
					<view class="button-item">
						<u-button type="primary" @click="openInfoPopup"><text>修改信息</text></u-button>
					</view>
					<view class="button-item">
						<u-button type="primary" @click="openPasswordPopup"><text>修改密码</text></u-button>
					</view>
					<view class="button-item">
						<u-button plain
							:custom-style="{color: '#ff0000', borderColor: '#ff0000', borderWidth: '2rpx'}"
							@click="handleLogout"><text>退出登录</text></u-button>
					</view>
				</view>
			</u-collapse-item>

			<!-- 帮助与支持卡片 -->
			<u-collapse-item title="帮助与支持" name="help">
				<view>
					<scroll-view scroll-y="true" class="faq-scroll">
						<view class="faq-list">
							<view class="faq-item">
								<view class="faq-question">
									<u-text text="我无法上传文件" size="30"></u-text>
								</view>
								<view class="faq-answer">
									<text>请检查可用容量是否充足，手机端由于技术原因暂时关闭上传功能</text>
								</view>
							</view>
							<view class="faq-item">
								<view class="faq-question">
									<u-text text="忘记密码了怎么办?" size="30"></u-text>
								</view>
								<view class="faq-answer">
									<text>使用登录界面的重置密码功能进行密码重置</text>
								</view>
							</view>
							<view class="faq-item">
								<view class="faq-question">
									<u-text text="如何修改个人信息?" size="30"></u-text>
								</view>
								<view class="faq-answer">
									<text>点击"修改信息"按钮即可修改个人信息</text>
								</view>
							</view>
							<view class="faq-item">
								<view class="faq-question">
									<u-text text="山的那边是什么?" size="30"></u-text>
								</view>
								<view class="faq-answer">
									<text>山的那边是海呀</text>
								</view>
							</view>
						</view>
					</scroll-view>
				</view>
			</u-collapse-item>

			<!-- 关于卡片 -->
			<u-collapse-item title="关于" name="about">
				<view class="card-content about-content">
					<view class="about-logo">
						<u-image :src="loginImage" width="200" height="200" shape="circle"></u-image>
					</view>
					<view class="about-title">
						<u-text text="Cloudrive" size="24" bold></u-text>
					</view>
					<view class="about-info">
						<view class="info-row">
							<u-text text="By lnblxj"></u-text>
						</view>
						<view class="info-row">
							<cd-router-link to="https://github.com/lnblxj" text="https://github.com/lnblxj" color="#4f65f1"></cd-router-link>
						</view>
						<view class="info-row website-link">
							<cd-router-link to="https://sboxm.top" text="https://sboxm.top" color="#4f65f1"></cd-router-link>
						</view>
					</view>
				</view>
			</u-collapse-item>
		</u-collapse>
	</view>
</template>

<script setup>
	import staticConfig from '@/config/static_config.uts';
	const loginImage = staticConfig.logo;
	import { onMounted, getCurrentInstance, ref, nextTick } from 'vue';
	import { useUserStore } from '@/stores/modules/user';
	import myPop from './myPop.vue';
	import api from '@/request/api.js';
	import { cloudriveConfig } from '@/config/global_config.ts';

	const { proxy } = getCurrentInstance();
	const myPopRef = ref(null);
	const userStore = useUserStore();
	
	// 用户信息响应式数据
	const userInfo = ref({
		nickname: '',
		email: '',
		status: ''
	})
	
	// 从状态管理中获取用户信息
	const getUserInfo = () => {
		userInfo.value = {
			nickname: userStore.name,
			email: userStore.email,
			status: userStore.status === '0' ? '正常' : '禁用'
		}
	}
	
	// 打开修改信息弹窗
	const openInfoPopup = () => {
		nextTick(() => {
			if (myPopRef.value) {
				myPopRef.value.openInfoPopup();
			} else {
				console.error('myPopRef未初始化');
			}
		});
	};
	
	// 打开修改密码弹窗
	const openPasswordPopup = () => {
		nextTick(() => {
			if (myPopRef.value) {
				myPopRef.value.openPasswordPopup();
			} else {
				console.error('myPopRef未初始化');
			}
		});
	};
	
	// 处理退出登录
	const handleLogout = async () => {
		try {
			const res = await api.logout();
			if (res.isSuccess || res.code == 401) {
				userStore.clearUserInfo();
				uni.switchTab({
					url: cloudriveConfig.index.url
				});
			}
		} catch (error) {
			console.error('退出登录失败:', error);
		}
	};

	defineExpose({
		openInfoPopup,
		openPasswordPopup
	});
	
	onMounted(() => {
		getUserInfo()
	})
</script>

<style scoped>
	@import '@/common/style.scss';
	.settings-container {
		background-color: #fff;
		width: 100%;
		height: 100%;
		box-sizing: border-box;
		overflow-y: auto;
		padding: 10px;
		border-radius: 20rpx;
	}

	.settings-card {
		background-color: #ffffff;
		margin-bottom: 20rpx;
		overflow: hidden;
		width: 100%;
	}

	.card-header {
		padding: 20rpx;
		border-bottom: 2rpx solid #f5f5f5;
	}

	.card-content {
		padding: 20rpx;
	}

	.info-list {
		margin-bottom: 20rpx;
	}

	.info-item {
		display: flex;
		flex-direction: column;
		padding: 16rpx 0;
		border-bottom: 2rpx solid #f5f5f5;
	}

	.info-label {
		font-size: 28rpx;
		color: #666;
		margin-bottom: 8rpx;
	}

	.info-value {
		font-size: 32rpx;
		color: #333;
	}

	.button-group {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}

	.faq-scroll {
		height: 600rpx;
	}

	.faq-list {
		padding: 10rpx 0;
	}

	.faq-item {
		margin-bottom: 20rpx;
		padding: 16rpx;
		background-color: #f8f8f8;
		border-radius: 8rpx;
	}

	.faq-question {
		margin-bottom: 10rpx;
	}

	.faq-answer {
		color: #666;
		font-size: 28rpx;
	}

	.about-content {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 30rpx 0;
		gap: 20rpx;
	}

	.about-logo {
		margin-bottom: 10rpx;
	}

	.about-title {
		margin-bottom: 20rpx;
	}

	.about-info {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 10rpx;
	}

	.info-row {
		display: flex;
		align-items: center;
		gap: 10rpx;
	}

	.website-link {
		display: flex;
		align-items: center;
	}
	.button-item{
		margin: 5rpx;
	}
	@media screen and (min-width: 768px) {
		.settings-container {
			margin: 0 auto;
		}

		.button-group {
			flex-direction: row;
			justify-content: flex-start;
		}
	}
</style>