<template>
	<view>
 		<!-- 修改信息弹窗 -->
		<u-popup v-model="showInfoPopup" mode="center" border-radius="14" width="700rpx" @close="closeInfoPopup">
			<view class="popup-content">
				<view class="popup-header">
					<u-text text="修改信息" size="32" bold></u-text>
					<u-icon name="close" size="28" @click="closeInfoPopup"></u-icon>
				</view>
				<view class="popup-body">
					<!-- 头像修改 -->
					<view class="avatar-section">
						<view class="u-avatar-wrap">
							<image class="u-avatar-demo" :src="avatar" mode="aspectFill"></image>
						</view>
						<!-- 根据状态显示不同的按钮 -->
						<view v-if="!showAvatarButtons" class="avatar-buttons">
							<u-button size="mini" @tap="chooseAvatar">更换头像</u-button>
						</view>
						<view v-else class="avatar-buttons">
							<u-button size="mini" @tap="chooseOtherAvatar" style="margin-right: 20rpx;">选择其它图片</u-button>
							<u-button size="mini" type="primary" @tap="confirmAvatar">确认更换头像</u-button>
						</view>
					</view>
					<!-- 昵称修改 -->
					<view class="nickname-section">
						<u-form :model="userInfoForm" ref="userInfoFormRef">
							<u-form-item label="昵称" prop="nickname">
								<u-input v-model="userInfoForm.nickname" placeholder="请输入新昵称"></u-input>
							</u-form-item>
						</u-form>
					</view>
				</view>
				<view class="popup-footer">
					<u-button type="primary" @click="updateUserInfo">保存</u-button>
				</view>
			</view>
		</u-popup>

		<!-- 修改密码弹窗 -->
		<u-popup v-model="showPasswordPopup" mode="center" border-radius="14" width="650rpx" @close="closePasswordPopup">
			<view class="popup-content">
				<view class="popup-header">
					<u-text text="修改密码" size="32" bold></u-text>
					<u-icon name="close" size="28" @click="closePasswordPopup"></u-icon>
				</view>
				<view class="popup-body">
					<u-form :model="passwordForm" ref="passwordFormRef" label-position="top">
						<u-form-item label="原密码" prop="oldPassword">
							<u-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码"></u-input>
						</u-form-item>
						<u-form-item label="新密码" prop="newPassword">
							<u-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码"></u-input>
						</u-form-item>
						<u-form-item label="确认密码" prop="confirmPassword">
							<u-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码"></u-input>
						</u-form-item>
					</u-form>
				</view>
				<view class="popup-footer">
					<u-button type="primary" @click="updatePassword">保存</u-button>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script setup>
	import { ref, reactive, onMounted, getCurrentInstance, onBeforeUnmount, watch } from 'vue';
	import { useUserStore } from '@/stores/modules/user';
	import { apiConfig, cloudriveConfig } from '@/config/global_config.ts'
	const props = defineProps({
		showInfoPopup: {
			type: Boolean,
			default: false
		},
		showPasswordPopup: {
			type: Boolean,
			default: false
		}
	});

	const { proxy } = getCurrentInstance();
	const userStore = useUserStore();

	// 弹窗显示状态
	const showInfoPopup = ref(props.showInfoPopup);
	const showPasswordPopup = ref(props.showPasswordPopup);

	watch(() => props.showInfoPopup, (newVal) => {
		showInfoPopup.value = newVal;
	});
	
	watch(() => props.showPasswordPopup, (newVal) => {
		showPasswordPopup.value = newVal;
	});

	// 用户信息表单
	const userInfoForm = reactive({
		nickname: ''
	});

	// 密码表单
	const passwordForm = reactive({
		oldPassword: '',
		newPassword: '',
		confirmPassword: ''
	});

	// 头像
	const avatar = ref('');

	// 表单引用
	const userInfoFormRef = ref(null);
	const passwordFormRef = ref(null);

	// 控制头像操作按钮的显示
	const showAvatarButtons = ref(false);
	
	// 选择其他图片
	const chooseOtherAvatar = () => {
		chooseAvatar();
	};
	
	// 确认更换头像
	const confirmAvatar = () => {
		uploadAvatar(avatar.value);
	};

	// 打开修改信息弹窗
	const openInfoPopup = () => {
		getUserInfo();
		showInfoPopup.value = true;
		emit('update:showInfoPopup', true);
	};

	const emit = defineEmits(['update:showInfoPopup', 'update:showPasswordPopup']);

	// 关闭修改信息弹窗
	const closeInfoPopup = () => {
		showInfoPopup.value = false;
		emit('update:showInfoPopup', false);
		userInfoForm.nickname = '';
		showAvatarButtons.value = false;
	};

	// 打开修改密码弹窗
	const openPasswordPopup = () => {
		showPasswordPopup.value = true;
		emit('update:showPasswordPopup', true);
	};

	// 关闭修改密码弹窗
	const closePasswordPopup = () => {
		showPasswordPopup.value = false;
		emit('update:showPasswordPopup', false);
		passwordForm.oldPassword = '';
		passwordForm.newPassword = '';
		passwordForm.confirmPassword = '';
	};

	// 获取用户信息
	const getUserInfo = async () => {
		try {
			const res = await proxy.$http.getUserInfo();
			if (res.code === 200) {
				userInfoForm.nickname = res.data.nickname;
				avatar.value = res.data.avatar || '/static/default-avatar.png';
			}
		} catch (error) {
			console.error('获取用户信息失败:', error);
			proxy.$u.toast('获取用户信息失败');
		}
	};

	// 更新用户信息
	const updateUserInfo = async () => {
		if (!userInfoForm.nickname) {
			return proxy.$u.toast('昵称不能为空');
		}

		try {
			const data = {
				nickname: userInfoForm.nickname
			};
			const res = await proxy.$http.updateNickName(data);
			if (res.isSuccess) {
				proxy.$u.toast('昵称修改成功');
				userStore.setUserInfo({
					name: userInfoForm.nickname
				});
				closeInfoPopup();
				return;
			}
			
			// 如果头像已更改，则上传头像
			if (avatar.value && !avatar.value.includes('/static/default-avatar.png') && !avatar.value.startsWith('http')) {
				await uploadAvatar(avatar.value);
			} else {
				proxy.$u.toast('信息修改成功');
				closeInfoPopup();
			}
			
			userStore.setUserInfo({
				name: userInfoForm.nickname
			});
		} catch (error) {
			console.error('修改用户信息失败:', error);
			proxy.$u.toast('修改用户信息失败');
		}
	};

	// 更新密码
	const updatePassword = async () => {
		if (!passwordForm.oldPassword) {
			return proxy.$u.toast('请输入原密码');
		}
		if (!passwordForm.newPassword) {
			return proxy.$u.toast('请输入新密码');
		}
		if (passwordForm.newPassword !== passwordForm.confirmPassword) {
			return proxy.$u.toast('两次输入的密码不一致');
		}

		try {
			const data = {
				oldPassword: passwordForm.oldPassword,
				newPassword: passwordForm.newPassword
			};
			const res = await proxy.$http.updatePassWord(data);
			if (res.isSuccess) {
				proxy.$u.toast('密码修改成功');
				closePasswordPopup();
			} else {
				proxy.$u.toast(res.message || '修改失败');
			}
		} catch (error) {
			console.error('修改密码失败:', error);
			proxy.$u.toast('修改密码失败');
		}
	};

	// 选择头像
	const chooseAvatar = () => {
		uni.chooseImage({
			count: 1,
			sizeType: ['compressed'],
			sourceType: ['album', 'camera'], 
			success: (res) => {
				const tempFilePath = res.tempFilePaths[0];
				avatar.value = tempFilePath;
				showAvatarButtons.value = true;
			},
			fail: (err) => {
				console.error('选择图片失败:', err);
				proxy.$u.toast('选择图片失败');
			}
		});
	};

	// 上传头像
	const uploadAvatar = async (filePath) => {
		try {
			// 显示上传中提示
			uni.showLoading({
				title: '上传中...'
			});
			let upLoadUrl = cloudriveConfig.BASE_URL.url + apiConfig.user.perfix + '/user/avatar'
			uni.uploadFile({
				url: upLoadUrl,
				filePath: filePath,
				name: 'file',
				header: {
					'Authorization': 'Bearer ' + userStore.token
				},
				success: (res) => {
					uni.hideLoading();
					
					const data = JSON.parse(res.data);
					if (data.isSuccess == 'true') {
						proxy.$u.toast('头像修改成功');
						closeInfoPopup();
					} else {
						proxy.$u.toast(data.message || '上传失败');
					}
				},
				fail: (err) => {
					uni.hideLoading();
					console.error('上传头像失败:', err);
					proxy.$u.toast('上传头像失败');
				}
			});
		} catch (error) {
			uni.hideLoading();
			console.error('上传头像失败:', error);
			proxy.$u.toast('上传头像失败');
		}
	};

	defineExpose({
		openInfoPopup,
		openPasswordPopup,
		showInfoPopup,
		showPasswordPopup
	});
</script>

<style lang="scss" scoped>
	.popup-content {
		padding: 30rpx;
	}

	.popup-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 30rpx;
	}

	.popup-body {
		margin-bottom: 30rpx;
	}

	.avatar-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-bottom: 30rpx;
	}
	
	.avatar-buttons {
		display: flex;
		flex-direction: row;
		justify-content: center;
		margin-top: 20rpx;
	}

	.u-avatar-wrap {
		margin-bottom: 20rpx;
		overflow: hidden;
		text-align: center;
	}

	.u-avatar-demo {
		width: 150rpx;
		height: 150rpx;
		border-radius: 100rpx;
	}

	.nickname-section {
		margin-top: 20rpx;
	}

	.popup-footer {
		display: flex;
		justify-content: center;
	}
</style>
