<template>
	<cd-page>
		<!-- 顶部导航栏 -->
		<u-navbar title="扫码登录" height="50"></u-navbar>

		<!-- 扫码区域 -->
		<view class="scan-area">
			<!-- 扫码按钮 -->
			<view class="scan-button" @click="startScan">
				<u-icon name="scan" color="#ffffff" size="60rpx"></u-icon>
				<text class="button-text">点击扫码</text>
			</view>
			<u-gap />
			<!-- 安全提示 -->
			<view class="tips-area">
				<u-icon name="info-circle" color="#ff9900" size="32rpx"></u-icon>
				<text class="tips-text">请勿扫描未知来源的二维码</text>
			</view>
		</view>
	</cd-page>
</template>

<script setup>
	import {ref, getCurrentInstance} from 'vue';
	const {proxy} = getCurrentInstance()
	// 开始扫码
	const startScan = () => {
		uni.scanCode({
			onlyFromCamera: true,
			scanType: ['qrCode'],
			success: (res) => {
				if (res.result.startsWith('http://') || res.result.startsWith('https://')) {
					uni.showToast({
						title: '无效的二维码内容',
						icon: 'none'
					});
					return;
				}
				submitScanResult(res.result);
			},
			fail: () => {
				uni.showToast({
					title: '扫码失败，请重试',
					icon: 'none'
				});
			}
		});
	};

	// 提交扫描结果
	const submitScanResult = async (result) => {
		uni.showLoading({
			title: '验证中...'
		});
		const data = {
			token: result
		}
		try{
			const res = await proxy.$http.loginQRCodeConfirm(data)
			uni.hideLoading();
			if(res.isSuccess){
				uni.showToast({
					title: '登录成功',
					icon: 'success'
				});
				setTimeout(() => {
					uni.switchTab({
						url: '/pages/my/my'
					})
				}, 1500);
			}else{
				uni.showToast({
					title: res.message || '登录失败',
					icon: 'none'
				});
			}
		}catch (error) {
			uni.hideLoading();
			uni.showToast({
				title: '网络错误，请重试',
				icon: 'none'
			});
		}
	};

	// 模拟API调用（实际项目中应替换为真实API）
	const simulateApiCall = (result) => {
		return new Promise((resolve) => {
			setTimeout(() => {
				resolve({
					success: true,
					message: '登录成功'
				});
			}, 1000);
		});
	};
</script>

<style>
	.scan-area {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: 70vh;
		gap: 40rpx;
	}

	.scan-button {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		width: 200rpx;
		height: 200rpx;
		background-color: #2979ff;
		border-radius: 50%;
		box-shadow: 0 4rpx 12rpx rgba(41, 121, 255, 0.4);
	}

	.button-text {
		margin-top: 20rpx;
		color: #ffffff;
		font-size: 28rpx;
	}

	.tips-area {
		display: flex;
		align-items: center;
		gap: 12rpx;
		padding: 20rpx 30rpx;
		background-color: rgba(255, 153, 0, 0.1);
		border-radius: 12rpx;
	}

	.tips-text {
		color: #ff9900;
		font-size: 28rpx;
	}
</style>