<template>
	<view class="cd-page-container" :style="containerStyle">
		<!-- #ifdef WEB -->
		<view class="header-container" v-if="props.header" @click="handleHeaderClick">
			<view class="logo-container">
				<image :src="logo" mode="aspectFit"></image>
				<text>Cloudrive</text>
			</view>
			<view class="page-title">
				
			</view>
		</view>
		<!-- #endif -->
		<view class="cd-page-inner" :style="innerStyle">
			<slot></slot>
		</view>
		<!-- #ifdef WEB -->
		<view class="footer-container" v-if="props.footer">
			<view class="footer-content">
				<text class="copyright">© 2025 Cloudrive. All rights reserved.</text>
			</view>
		</view>
		<!-- #endif -->
	</view>
</template>

<script setup>
import { computed, ref } from 'vue';
import static_config from '@/config/static_config';
const logo = static_config.logo
const props = defineProps({
	padding: {
		type: Number,
		default: 10
	},
	paddingTop: {
		type: Number,
		default: 110
	},
	minHeight: {
		type: [String, Number],
		default: '100vh'
	},
	backgroundColor: {
		type: String,
		default: '#F7F9FE'
	},
	header: {
		type: Boolean,
		default: false
	},
	footer: {
		type: Boolean,
		default: false
	}
});

const containerStyle = computed(() => ({
	backgroundColor: props.backgroundColor,
	minHeight: typeof props.minHeight === 'number' ? `${props.minHeight}rpx` : props.minHeight
}));

const innerStyle = computed(() => ({
	padding: props.padding + 'rpx',
	paddingTop: props.paddingTop + 'rpx'
}));

const handleHeaderClick = ()=>{
	uni.switchTab({
		url: '/pages/index/index'
	})
}

// const modalRef = ref(null);

// const showModal = (config, confirmCallback, cancelCallback) => {
// 	if (modalRef.value) {
// 		modalRef.value.showModal(config, confirmCallback, cancelCallback);
// 	}
// };

// const closeModal = () => {
// 	if (modalRef.value) {
// 		modalRef.value.closeModal();
// 	}
// };

// defineExpose({
// 	showModal,
// 	closeModal
// });
</script>

<style scoped>
.cd-page-container {
	width: 100%;
	height: auto;
	position: relative;
}

.cd-page-inner {
	box-sizing: border-box;
}

.header-container{
	width: 100%;
}

.header-container image {
	width: 80rpx;
	height: 60rpx;
}

.header-container text {
	font-size: 48rpx;
	font-weight: bold;
}

/* #ifdef WEB */
.cd-page-inner {

	width: 90%;
	margin: 0 auto;
}

.logo-container{
	display: flex;
	align-items: center;
	gap: 20rpx;
	padding: 30rpx 40rpx;
	cursor: pointer;
}

.footer-container {
	padding: 20rpx 0;
	background-color: #f7f7f9;
	border-top: 1px solid #e3e8f7;
	margin-top: 30rpx;
	height: 150rpx;
}

.footer-content {
	display: flex;
	justify-content: center;
	align-items: center;
	width: 90%;
	margin: 0 auto;
}

.copyright {
	font-size: 24rpx;
	color: #666;
}

.terms {
	font-size: 24rpx;
	color: #666;
	cursor: pointer;
}

.terms:hover {
	text-decoration: underline;
}
/* #endif */

/* #ifdef APP */
.cd-page-inner {
	padding-top: 84px !important;
	width: 100%;
}
/* #endif */
</style>
