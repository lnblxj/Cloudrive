<template>
	<!-- #ifdef APP -->
	<u-navbar :title="mode? '创建账号' : '重置密码'" height="50"></u-navbar>
	<!-- #endif -->
	<image :src="bg" class="bg-image" mode="aspectFill"></image>
	<view class="card-container">
		<u-card margin="20rpx" padding="20rpx" :border="true" :border-radius="10" class="card-border">
			<template v-slot:body>
				<view class="form-container">
					<u-row>
						<image src="/static/auth/logo-light-mask.png" mode="aspectFit" class="logo"></image>
						<text class="title-text">Cloudrive</text>
					</u-row>
					<u-gap height="20"></u-gap>
					<text class="subtitle-text">{{mode?'创建账号':'找回密码'}}</text>
					<l-transition :visible="show" :name="animiMode" appear>
						<view class="input-container">
							<view v-if="step === 1">
								<u-gap height="20"></u-gap>
								<text class="verify-text">输入邮箱</text>
								<u-input v-model="email" placeholder="请输入邮箱" type="text" maxlength="50" border
									borderColor="#2979ff"></u-input>
							</view>
							<view v-if="step === 2">
								<u-gap height="20"></u-gap>
								<text class="verify-text">输入验证码</text>
								<l-code-input v-model="verifyCode" borderColor="#2979ff" bgColor="#eaeaea"
									radius="10rpx" :mask="false"></l-code-input>
								<view class="resend-code">
									<u-verification-code 
									ref="uCodeResend" 
									:seconds="60" 
									@change="codeChange"
									start-text="获取验证码"
									end-text="获取验证码"
									keep-running unique-key="rfpage-code"></u-verification-code>
									<u-button size="mini" type="primary" @click="sendVerifyCode">{{ codeText }}</u-button>
								</view>
							</view>
							<view v-if="step === 3">
								<u-gap height="20"></u-gap>
								<view v-if="mode">
									<text class="verify-text">设置您的账户信息</text>
								<u-input v-model="nickName" placeholder="设置您的昵称" maxlength="20"
									border borderColor="#2979ff" @blur="checkNicknameOnBlur"></u-input>
								<u-gap height="20"></u-gap>
								</view>
								<text v-else class="verify-text">设置您的新密码</text>
								<u-input v-model="passWord" placeholder="请输入密码" type="password" maxlength="20" border
									borderColor="#2979ff"></u-input>
								<u-gap height="20"></u-gap>
								<u-input v-model="confPassWord" placeholder="请确认密码" type="password" maxlength="20"
									border borderColor="#2979ff"></u-input>
							</view>
						</view>
					</l-transition>
					<view class="error-message" v-if="errorMsg">{{errorMsg}}</view>
					<u-button @click="nextStep" type="primary" :loading="btnDisabled">{{buttonText}}</u-button>
				</view>
			</template>
		</u-card>
	</view>
</template>

<script setup>
	import staticConfig from '@/config/static_config.uts';
	import {ref,watch,onMounted,onUnmounted, getCurrentInstance} from 'vue';
	import {useTransition} from '@/uni_modules/lime-transition';
	
	const { proxy } = getCurrentInstance();
	const bg = staticConfig.authbg
	const mode = ref(false)
	const props = defineProps({
		mode: {
			type: String,
			default: 'register'
		} 
	})
	
	watch(() => props.mode, (newVal) => {
		mode.value = newVal === 'register'
	}, { immediate: true })
	onMounted(() => {
		animiIn()
	})

	// 数据
	const show = ref(true)
	const animiMode = ref("fade")
	const buttonText = ref("下一步")
	const step = ref(1)

	// 监听步骤变化
	watch(step, (newVal) => {
		if (newVal === 3) {
			buttonText.value = mode.value ? "创建账号" : "重置密码"
		} else {
			buttonText.value = "下一步"
		}
	})

	// 表单数据
	const email = ref("")
	const verifyCode = ref("")
	const passWord = ref("")
	const confPassWord = ref("")
	const nickName = ref("")
	
	// 错误信息
	const errorMsg = ref("")
	// 按钮禁用状态
	const btnDisabled = ref(false)
	// 验证码组件相关
	const uCodeResend = ref(null)
	const codeText = ref("获取验证码")
	
	// 表单验证
	function validateEmail() {
		if (!email.value) {
			errorMsg.value = "请输入邮箱"
			return false
		}
		const emailRegex = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/
		if (!emailRegex.test(email.value)) {
			errorMsg.value = "邮箱格式不正确"
			return false
		}
		return true
	}
	
	function validateVerifyCode() {
		if (!verifyCode.value || verifyCode.value.length !== 6) {
			errorMsg.value = "请输入6位验证码"
			return false
		}
		return true
	}
	
	function validatePassword() {
		if (!passWord.value) {
			errorMsg.value = "请输入密码"
			return false
		}
		if (passWord.value.length < 8) {
			errorMsg.value = "密码长度不能少于8位"
			return false
		}
		if (passWord.value !== confPassWord.value) {
			errorMsg.value = "两次输入的密码不一致"
			return false
		}
		return true
	}
	
	function validateNickname() {
		if (!nickName.value) {
			errorMsg.value = "请输入昵称"
			return false
		}
		return true
	}
	
	// 验证码相关函数
	// 验证码文字变化
	function codeChange(text) {
		codeText.value = text
	}
	
	function sendVerifyCode() {
		if (!uCodeResend.value?.canGetCode && uCodeResend.value != null) {
			uni.showToast({
				title: "请稍后再试",
				icon: "none"
			})
			return
		}
		
		if (!validateEmail()) return
		btnDisabled.value = true
		errorMsg.value = ""
		proxy.$u.throttle(async()=>{
			try {
				let res
				if (mode.value) {
					// 注册验证码
					res = await proxy.$http.sendRegisterVerifyCode(email.value.trim())
				} else {
					// 重置密码验证码
					res = await proxy.$http.sendResetPasswordCode(email.value.trim())
				}
				if (res.isSuccess) {
					uni.showToast({
						title: "验证码已发送",
						icon: "success"
					})
					// 开始倒计时
					uCodeResend.value.start()
				} else {
					errorMsg.value = res.message || "发送验证码失败"
					if (res.code === 505) {
						// 邮箱已存在，显示确认对话框
						if (mode.value) {
							uni.showModal({
								title: '提示',
								content: '该邮箱已注册，是否前往登录？',
								success: function(res) {
									if (res.confirm) {
										uni.navigateTo({
											url: "/pages/auth/login/login"
										})
									}
								}
							})
						} else {
							errorMsg.value = "邮箱已存在"
						}
					}
				}
			} catch (error) {
				errorMsg.value = "网络错误，请稍后重试"
				console.error(error)
			} finally {
				btnDisabled.value = false
			}
		}, 500, true)
	}
	

	
	// 检查昵称是否存在
	async function checkNickname() {
		if (!nickName.value) return false
		
		try {
			const res = await proxy.$http.checkUserName(nickName.value.trim())
			if (!res.isSuccess && res.code === 501) {
				errorMsg.value = "用户名已存在"
				return false
			}
			return true
		} catch (error) {
			errorMsg.value = "网络错误，请稍后重试"
			console.error(error)
			return false
		}
	}
	
	// 注册账号
	async function register() {
		if (!validatePassword()) return
		if (!validateNickname()) return
		
		btnDisabled.value = true
		errorMsg.value = ""
		
		try {
			// 检查昵称是否存在
			const nicknameValid = await checkNickname()
			if (!nicknameValid) {
				btnDisabled.value = false
				return
			}
			
			const data = {
				email: email.value.trim(),
				verifyCode: verifyCode.value,
				nickname: nickName.value.trim(),
				password: passWord.value
			}
			
			const res = await proxy.$http.register(data)
			
			if (res.isSuccess) {
				uni.showToast({
					title: "注册成功",
					icon: "success"
				})
				setTimeout(() => {
					uni.navigateTo({
						url: "/pages/auth/login/login"
					})
				}, 1500)
			} else {
				errorMsg.value = res.message || "注册失败"
				if (res.code === 514) {
					errorMsg.value = "验证码已过期，请重新获取"
					step.value = 1
				}
			}
		} catch (error) {
			errorMsg.value = "网络错误，请稍后重试"
			console.error(error)
		} finally {
			btnDisabled.value = false
		}
	}
	
	// 重置密码
	async function resetPassword() {
		if (!validatePassword()) return
		
		btnDisabled.value = true
		errorMsg.value = ""
		
		try {
			const data = {
				email: email.value.trim(),
				verifyCode: verifyCode.value,
				newPassword: passWord.value
			}
			
			const res = await proxy.$http.resetPassword(data)
			
			if (res.isSuccess) {
				uni.showToast({
					title: "密码重置成功",
					icon: "success"
				})
				setTimeout(() => {
					uni.navigateTo({
						url: "/pages/auth/login/login"
					})
				}, 1500)
			} else {
				errorMsg.value = res.message || "重置密码失败"
				if (res.code === 514) {
					errorMsg.value = "验证码已过期，请重新获取"
					step.value = 1
				}
			}
		} catch (error) {
			errorMsg.value = "网络错误，请稍后重试"
			console.error(error)
		} finally {
			btnDisabled.value = false
		}
	}
	
	const nextStep = ()=>{
		if (btnDisabled.value) return
		
		errorMsg.value = ""
		
		if (step.value === 1) {
			// 第一步：只验证邮箱，不发送验证码
			if (validateEmail()) {
				animiOut()
				setTimeout(() => {
					step.value = 2
					animiIn()
				}, 100)
			}
		} else if (step.value === 2) {
			// 第二步：验证验证码
			if (validateVerifyCode()) {
				animiOut()
				setTimeout(() => {
					step.value = 3
					animiIn()
				}, 100)
			}
		} else if (step.value === 3) {
			// 第三步：完成注册或重置密码
			if (mode.value) {
				register()
			} else {
				resetPassword()
			}
		}
	} 
	// 昵称输入框失去焦点时检查昵称是否存在
	async function checkNicknameOnBlur() {
		if (nickName.value && nickName.value.trim() !== '') {
			await checkNickname()
		}
	}
	
	function nextIn() {
		if (step.value < 3) {
			step.value += 1
		} else {
			step.value = 1
		}
		animiIn()
	}

	function animiIn() {
		animiMode.value = "fade-right"
		show.value = true
	}

	function animiOut() {
		animiMode.value = "fade-left"
		show.value = false
	}
	
	// 组件卸载时处理
	onUnmounted(() => {
		// 无需手动清除定时器，验证码组件会自行处理
	})
</script>

<style scoped>
	.bg-image {
		position: fixed;
		width: 100%;
		height: 100%;
		z-index: -1;
	}

	.card-container {
		position: relative;
		width: 100%;
		height: 100vh;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.form-container {
		width: 700rpx;
		height: 700rpx;
		padding: 20rpx;
	}

	@media screen and (min-width: 768px) {
		.card-container {
			u-card {
				width: 500px;
			}
		}
	}

	@media screen and (max-width: 767px) {
		.card-container {
			u-card {
				width: 90%;
			}
		}
	}
	.logo {
		width: 35px;
		height: 35px;
		margin-right: 10rpx;
	}

	.input-container {
		height: 360rpx;
		margin-bottom: 20rpx;
	}

	.title-text {
		font-size: 25px;
		font-family: 'PingFang SC';
		font-weight: bold;
	}

	.subtitle-text {
		font-size: 20px;
		margin-bottom: 20rpx;
	}

	.verify-text {
		font-size: 16px;
		margin-bottom: 20rpx;
		display: block;
	}
	
	.error-message {
		color: #fa3534;
		font-size: 14px;
		margin: 10rpx 0;
		text-align: center;
	}
	
	.resend-code {
		margin-top: 20rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}
</style>