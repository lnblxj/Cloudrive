<template>
	<view :style="containerStyle">
		<u-row justify="center">
			<view :style="avatarContainStyle">
				<image class="avatar" :src="avatar" mode="aspectFill" />
			</view>
		</u-row>
		<u-row justify="center">
			<view :style="nameContainStyle">
				<u-text :text="nickName" color="#fff" bold size="40rpx"></u-text>
			</view>
		</u-row>
	</view>
</template>
<script setup>
	import { ref, onMounted, getCurrentInstance } from 'vue'
	import { useUserStore } from '../../stores/modules/user';

	/**
	 * @name user-info-card
	 * @description 用户信息卡片
	 * 
	 * @property {Object} avatarContainStyle Object | 头像容器样式
	 * @property {Object} nameContainStyle Object |  用户名容器样式
	 * @property {Object} containerStyle Object | 父容器样式
	 */
	const props = defineProps({
	    avatarContainStyle: {
	        type: Object,
	        default: () => ({})
	    },
		nameContainStyle: {
		    type: Object,
		    default: () => ({})
		},
		containerStyle:{
			type: Object,
			default: () => ({})
		}
	})
	const { proxy } = getCurrentInstance();
	const userStore = useUserStore()

	// 数据
	const nickName = ref('')
	const avatar = ref('')

	const getUserInfo = async () => {
		try {
			if (!userStore.isLoggedIn) {
				nickName.value = "未登录"
				avatar.value = '/static/icon/avatar.png'
				return
			}
			const res = await proxy.$http.getUserInfo()
			if (res.isSuccess === true) {

				userStore.setUserInfo(res.data)
				nickName.value = res.data.nickname
				avatar.value = res.data.avatar
				// 触发store的更新
				userStore.$patch((state) => {
					state.name = res.data.nickname
					state.avatar = res.data.avatar
					state.email = res.data.email
					state.status = res.data.status
				})
			}
		} catch (error) {
			console.error('获取用户信息失败:', error)
		}
	}

	onMounted(() => {
		getUserInfo()
	})
</script>

<style scoped>
	.avatar {
		width: 100px;
		height: 100px;
		border-radius: 50%;
		border: 4px solid #ffffff;
		transition: transform 0.5s linear;
	}

	.avatar:hover {
		animation: rotate 0.2s linear;
	}

	@keyframes rotate {
		from {
			transform: rotate(0deg);
		}

		to {
			transform: rotate(360deg);
		}
	}
</style>