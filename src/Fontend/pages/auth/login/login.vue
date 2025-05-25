<template>
	<cd-page>
		<!-- #ifdef WEB -->
		<view class="login-container">
			<u-row justify="center" align="center">
				<u-col span="7.4">
					<view class="logo-container">
						<u-image :src="loginImage" mode="aspectFit" width="600rpx" height="600rpx"></u-image>
						<u-text size="60px" align="center" bold text="Cloudrive" font="Microsoft YaHei"></u-text>
					</view>
				</u-col>
				<u-col span="4.6">
					<view class="card-container">
						<u-card padding="0" margin="0" class="card-border">
							<template v-slot:body>
								<view style="width: 100%;">
									<u-subsection 
									:list="tabData" 
									v-model="loginMode"
									></u-subsection>
								</view>
								<auth-form v-if="loginMode == 1"></auth-form>
								<view class="QRcode" v-else>
									<view class="qrcode-wrapper" @click="handleQRCodeClick">
										<l-qrcode :value="QRCode"></l-qrcode>
										<view class="qrcode-mask" v-if="isQRCodeExpired">
											<u-text text="二维码已过期\n点击重新获取" color="#000" align="center"></u-text>
										</view>
									</view>
									<u-gap></u-gap>
									<u-row justify="center">
										<u-text text="使用Cloudrive手机端扫码登录"></u-text>
									</u-row>
								</view>
							</template>
						</u-card>
					</view>
				</u-col>
			</u-row>
		</view>
		<!-- #endif -->

		<!-- #ifdef APP -->
		<view class="mobile-login-container">
			<view class="mobile-logo-container">
				<view class="mobile-logo-image">
					<u-image :src="loginImage" mode="aspectFit"></u-image>
				</view>
				<u-text size="40px" align="center" bold text="Cloudrive" font="Microsoft YaHei"></u-text>
			</view>
			<view class="mobile-card-container">
				<auth-form></auth-form>
			</view>
		</view>
		<!-- #endif -->
	</cd-page>
</template>

<script lang="ts" setup>
	import { ref, reactive, getCurrentInstance, onMounted, onUnmounted, watch } from 'vue';
	import staticConfig from '@/config/static_config.uts';
	import authForm from './authForm.vue';
	import { useUserStore } from '@/stores/modules/user.js';
	
	const loginImage = staticConfig.logo;
	const { proxy }: any = getCurrentInstance();
	const userStore = useUserStore();

	onMounted(()=>{
		// #ifdef WEB
		// #endif
	})

	onUnmounted(() => {
		// #ifdef WEB
		clearQRCodeTimer()
		// #endif
	})
	
	// #ifdef WEB
	const dynnamicBg = false
	const loginMode = ref(1);
	const QRCode = ref("none")
	const qrCodeTimer = ref<NodeJS.Timeout | null>(null);
	const isQRCodeExpired = ref(false);
	const tabData = reactive([
		{
			name: '扫码登录'
		},
		{
			name: '账号登录'
		}
	]);

	watch(loginMode, (newVal) => {
		if (newVal === 0) {
			getQRCode()
		} else if (newVal === 1) {
			clearQRCodeTimer()
			isQRCodeExpired.value = false
		}
	})

	const getQRCode = () => {
		if (QRCode.value !== "none" && !isQRCodeExpired.value) {
			startQRCodeCheck()
			return
		}
		
		proxy.$u.throttle(async () => {
			try{
				const res = await proxy.$http.loginQRCode();
				QRCode.value = res.data.content
				isQRCodeExpired.value = false
				startQRCodeCheck()
			}catch (e){
				QRCode.value = "none"
				console.error(e)
			}
		},1000,true)
	}

	const handleQRCodeClick = () => {
		if (isQRCodeExpired.value) {
			getQRCode()
		}
	}

	const startQRCodeCheck = () => {
		clearQRCodeTimer()
		qrCodeTimer.value = setInterval(async () => {
			proxy.$u.debounce(async () => {
				try {
					const res = await proxy.$http.loginQRCodeCheck(QRCode.value)
					if (res.isSuccess) {
						// 登录成功
						clearQRCodeTimer()
						userStore.setUserInfo({
							token: res.data.token,
							id: res.data.infoVo.id,
							name: res.data.infoVo.nickname,
							email: res.data.infoVo.email
						})
						uni.showToast({
							title: '登录成功',
							icon: 'none'
						});
						uni.switchTab({
							url: "/pages/index/index"
						})
					} else if (res.code === 516) {
						// 二维码过期
						clearQRCodeTimer()
						isQRCodeExpired.value = true
						QRCode.value = "none"
					}
					// code 517 未扫描，继续轮询
				} catch (e) {
					console.error(e)
					clearQRCodeTimer()
					proxy.$u.toast('二维码检查失败，请重试')
				}
			}, 500)
		}, 2000)
	}

	const clearQRCodeTimer = () => {
		if (qrCodeTimer.value) {
			clearInterval(qrCodeTimer.value)
			qrCodeTimer.value = null
		}
	}
	
	// #endif
</script>

<style scoped>
	/* #ifdef WEB */
	.login-container {
		width: 80%;
		margin: 0 auto;
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
	}

	.logo-container {
		width: 100%;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 20px;
	}

	.card-container {
		display: flex;
		align-items: center;
		justify-content: center;
		height: 100%;
	}

	.u-card {
		width: 100%;
		max-width: 400px;
		min-height: 400px;
		padding: 30px;
	}

	.u-tabs {
		margin-bottom: 20px;
		width: 100%;
	}

	.QRcode {
		width: 100%;
		height: 100%;
		display: flex;
		margin-top: 30px;
		flex-direction: column;
		align-items: center;
	}

	.qrcode-wrapper {
		position: relative;
		cursor: pointer;
	}

	.qrcode-mask {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background-color: rgba(255, 255, 255, 0.85);
		display: flex;
		justify-content: center;
		align-items: center;
		backdrop-filter: blur(2px);
	}

	.qrcode-mask .u-text {
		font-size: 14px;
		color: #666;
		font-weight: 500;
	}
	/* #endif */

	/* #ifdef APP */
	.mobile-login-container {
		width: 100%;
		height: auto;
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 60px 10px;
	}

	.mobile-logo-container {
		width: 100%;
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-bottom: 60px;
	}

	.mobile-logo-image {
		width: 100px;
		height: 100px;
		margin-bottom: 30px;
	}

	.mobile-card-container {
		width: 100%;
		max-width: 700rpx;
		padding: 20px;
	}
	/* #endif */
</style>