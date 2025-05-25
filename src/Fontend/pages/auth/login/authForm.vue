<template>
	<u-gap></u-gap>
	<u-form :model="formData" ref="form" :border-bottom="false">
		<l-transition :visible="!showVerifyCode" name="fade-up" appear>
			<view v-if="!showVerifyCode">
				<u-form-item prop="email">
					<u-input 
						v-model="formData.email" 
						placeholder="邮箱" 
						type="text"
						maxlength="50"
						border
						:borderColor="$u.color['primary']"
					></u-input>
				</u-form-item>
				<u-form-item prop="password">
					<u-input 
						v-model="formData.password" 
						placeholder="密码" 
						type="password"
						maxlength="20"
						border
						:borderColor="$u.color['primary']"
					></u-input>
				</u-form-item>
				<u-form-item prop="agree">
					<u-checkbox v-model="formData.agree" >已阅读并同意：</u-checkbox>
					<cd-router-link 
					text="用户协议与隐私政策" 
					color="#00aaff" 
					to="/pages/usePrivacy/usePrivacy" 
					target="_blank"></cd-router-link>
				</u-form-item>
			</view>
		</l-transition>
		<l-transition :visible="showVerifyCode" name="fade-up" appear>
			<view v-if="showVerifyCode" class="verify-content">
				<image class="verify-image" :src="verifyCodeImage" @click="refreshVerifyCode"></image>
				<u-form-item prop="verifyCode">
					<u-input 
						v-model="formData.verifyCode" 
						placeholder="请输入验证码" 
						type="text"
						maxlength="6"
						border
						:borderColor="$u.color['primary']"
					></u-input>
				</u-form-item>
			</view>
		</l-transition>
		<view class="submit-btn">
			<u-button type="primary" @click="submitForm">{{ showVerifyCode ? '确认' : '登录' }}</u-button>
		</view>
	</u-form>
	<u-gap></u-gap>
	<view class="links-container" v-if="!showVerifyCode">
		<cd-router-link text="注册账号" color="#666" to="/pages/auth/RFpage/RFpage?mode=register"></cd-router-link>
		<cd-router-link text="找回密码" color="#666" to="/pages/auth/RFpage/RFpage?mode=forget"></cd-router-link>
	</view>
</template>

<script setup>
	import { ref, reactive, onMounted, getCurrentInstance } from 'vue';
	import { useUserStore } from '@/stores/modules/user';
	const { proxy } = getCurrentInstance();
	// 数据
	const formData = reactive({
		email: '',
		password: '',
		agree: false,
		verifyCode: ''
	});
	
	// 控制是否显示验证码界面
	const showVerifyCode = ref(false);
	const verifyCodeImage = ref('');

	const form = ref(null);

	const formRules = {
		email:[{
			required: true,
			message: '请输入邮箱',
			trigger: ['change', 'blur']
		},
		{
			type: 'email',
			message: '请输入正确的邮箱',
			trigger: ['change', 'blur']
		},
		{
			// 使用自定义验证函数结合uView内置的email验证
			validator: (rule, value, callback) => {
				return uni.$u.test.email(value);
			},
			message: '邮箱格式不正确',
			trigger: ['change', 'blur']
		}],
		password: [{
			required: true,
			message: '请输入密码',
			trigger: ['change', 'blur']
		}],
		agree: [{
			validator: (rule, value, callback) => {
				return formData.agree === true;
			},
			message: '请阅读并同意用户协议与隐私政策',
			trigger: ['change']
		}],
		verifyCode: [{
			required: true,
			message: '请输入验证码',
			trigger: ['change', 'blur']
		}]
	}
	const getVerifyCode = async (isRefresh = false) => {
	  try {
	    const res = await proxy.$http.getVerifyCode({
	      email: formData.email,
	      password: formData.password,
	      verifyCode: ''
	    });
	    if(res instanceof ArrayBuffer) {
	      const base64 = uni.arrayBufferToBase64(res);
	      verifyCodeImage.value = 'data:image/png;base64,' + base64;
	      if (!isRefresh) {
	        showVerifyCode.value = true;
	      }
	    }
	  } catch (e) {
	    console.error(e);
	    uni.$u.toast('获取验证码失败');
	  }
	};
	// 刷新验证码
	const refreshVerifyCode = () => {
		getVerifyCode(true);
	};
	
	// 返回登录界面
	const backToLogin = () => {
		showVerifyCode.value = false;
		formData.verifyCode = '';
	};

	// 确认验证码
	const confirmVerifyCode = async () => {
		if (!formData.verifyCode) {
			uni.$u.toast('请输入验证码');
			return;
		}
		try {
			const res = await proxy.$http.login({
				email: formData.email,
				password: formData.password,
				verifyCode: formData.verifyCode
			});
			if (res.isSuccess) {
				uni.$u.toast('登录成功');
				// 存储用户信息和token
				const userStore = useUserStore();
				userStore.setUserInfo({
					token: res.data.token,
					email: res.data.infoVo.email,
					name: res.data.infoVo.nickname,
					id: res.data.infoVo.id
				});
				uni.switchTab({
					url: '/pages/index/index'
				});
			} else {
				uni.showModal({
					title: '提示',
					content: res.message || '验证码错误',
					showCancel: false,
					success: () => {
						backToLogin();
					}
				});
			}
		} catch (e) {
			console.error(e);
			uni.showModal({
				title: '提示',
				content: '登录失败',
				showCancel: false,
				success: () => {
					backToLogin();
				}
			});
		}
	};

	// 提交表单
	const submitForm = () => {
		if (showVerifyCode.value) {
			// 如果是验证码界面，则提交验证码
			confirmVerifyCode();
		} else {
			// 否则验证表单并获取验证码
			form.value.validate(valid => {
				if (valid) {
					getVerifyCode();
				} else {
					console.log('表单验证失败');
				}
			});
		}
	}

	onMounted(() => {
		// 设置表单验证规则
		form.value.setRules(formRules);
	});
</script>

<style lang="scss">
.submit-btn {
	margin-top: 30rpx;
}

.links-container {
	display: flex;
	justify-content: flex-end;
	width: 100%;
	margin-top: 10rpx;
}

.verify-content {
	padding: 20rpx;
	display: flex;
	flex-direction: column;
	align-items: center;
}

.verify-image {
	width: 300rpx;
	height: 120rpx;
	margin-bottom: 20rpx;
	cursor: pointer;
}

.verify-tip {
	margin-top: 10rpx;
	text-align: right;
	width: 100%;
}

.verify-tip-text {
	color: #2979ff;
	font-size: 24rpx;
}

.modal-footer {
	display: flex;
	justify-content: flex-end;
	padding: 20rpx;
}

:deep(.u-form-item) {
	position: relative;
	margin-bottom: 30rpx;
	
	.u-form-item__message {
		position: absolute;
		bottom: -25rpx;
		left: 0;
		z-index: 10;
	}
}
</style>