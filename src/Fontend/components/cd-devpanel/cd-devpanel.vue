<template>
	<!-- #ifdef WEB -->
	<view class="dev-panel" :style="panelStyle" @touchstart="onTouchStart" @touchmove="onTouchMove"
		@touchend="onTouchEnd" @mousedown="onMouseDown" @mousemove="onMouseMove" @mouseup="onMouseUp">
		<text style="color: chartreuse;">Dev Panel</text>
		<u-gap></u-gap>
		<!-- 用户信息 -->
		<view v-if="userStore.isLoggedIn" class="user-info">
			<text class="stat-item">昵称: {{ userStore.name }}</text><br/>
			<text class="stat-item">ID: {{ userStore.id }}</text><br/>
			<text class="stat-item">邮箱: {{ userStore.email }}</text>
		</view>
		<u-gap></u-gap>
		<!-- 系统资源监控 -->
		<view class="performance-stats">
			<text class="stat-item">FPS: {{ fps }}</text>
			<text class="stat-item">Memory: {{ memoryUsage }}</text>
			<text class="stat-item">CPU: {{ cpuUsage }}</text>
		</view>
		<u-gap></u-gap>
		<cd-router-link text="login" to="/pages/auth/login/login"></cd-router-link>
<!-- 		<cd-router-link text="test" to="/pages/test/test"></cd-router-link> -->
		<cd-router-link text="editor" to="/pages/editor/editor"></cd-router-link>
		<cd-router-link text="post" to="/pages/post/post"></cd-router-link>
		<cd-router-link text="RFpage" to="/pages/auth/RFpage/RFpage"></cd-router-link>
		<cd-router-link text="file" to="/pages/file/file"></cd-router-link>
		<cd-router-link text="my" to="/pages/my/my"></cd-router-link>
	</view>
	<!-- #endif -->
	<!-- #ifdef APP -->
	<view class="dev-panel">
		<text style="color: chartreuse;">Dev Panel</text>
		<cd-router-link text="login" to="/pages/auth/login/login"></cd-router-link>
<!-- 		<cd-router-link text="test" to="/pages/test/test"></cd-router-link> -->
		<cd-router-link text="editor" to="/pages/editor/editor"></cd-router-link>
		<cd-router-link text="post" to="/pages/post/post"></cd-router-link>
		<cd-router-link text="RFpage" to="/pages/auth/RFpage/RFpage"></cd-router-link>
		<cd-router-link text="file" to="/pages/file/file"></cd-router-link>
		<cd-router-link text="my" to="/pages/my/my"></cd-router-link>
		<cd-router-link text="codeScanner" to="/pages/auth/codeScanner/codeScanner"></cd-router-link>
	</view>
	<!-- #endif -->

</template>

<script setup>
	// #ifdef WEB
	import {
		ref,
		computed,
		onMounted,
		onUnmounted
	} from 'vue';
	import { useUserStore } from '@/stores/modules/user';

	const position = ref({
		x: 10,
		y: 10
	});
	const startPosition = ref({
		x: 0,
		y: 0
	});
	const isDragging = ref(false);

	// 获取用户状态
	const userStore = useUserStore();

	// 性能监控数据
	const fps = ref(0);
	const memoryUsage = ref('0 MB');
	const cpuUsage = ref('0%');

	// FPS计算相关变量
	let frameCount = 0;
	let lastFrameTime = 0;
	let fpsUpdateInterval = 1000; // 每秒更新一次FPS


	const panelStyle = computed(() => {
		return {
			position: 'fixed',
			left: position.value.x + 'px',
			top: position.value.y + 'px',
			width: '120px',
			backgroundColor: 'black',
			padding: '10px',
			zIndex: 999,
			border: 'red solid',
			cursor: isDragging.value ? 'grabbing' : 'grab'
		}
	});

	// 触摸事件处理
	function onTouchStart(event) {
		const touch = event.touches[0];
		startPosition.value = {
			x: touch.clientX - position.value.x,
			y: touch.clientY - position.value.y
		};
		isDragging.value = true;
	}

	function onTouchMove(event) {
		if (!isDragging.value) return;
		const touch = event.touches[0];
		position.value = {
			x: touch.clientX - startPosition.value.x,
			y: touch.clientY - startPosition.value.y
		};
	}

	function onTouchEnd() {
		isDragging.value = false;
		autoSnap();
	}

	// 鼠标事件处理
	function onMouseDown(event) {
		event.preventDefault();
		startPosition.value = {
			x: event.clientX - position.value.x,
			y: event.clientY - position.value.y
		};
		isDragging.value = true;
	}

	function onMouseMove(event) {
		if (!isDragging.value) return;
		event.preventDefault();
		position.value = {
			x: event.clientX - startPosition.value.x,
			y: event.clientY - startPosition.value.y
		};
	}

	function onMouseUp() {
		isDragging.value = false;
		autoSnap();
	}

	// 自动贴边
	function autoSnap() {
		const windowWidth = window.innerWidth;
		const panelWidth = 120;

		if (position.value.x < windowWidth / 2) {
			position.value.x = 0; // 贴左边
		} else {
			position.value.x = windowWidth - panelWidth; // 贴右边
		}
	}

	// 全局鼠标事件监听
	function onGlobalMouseMove(event) {
		onMouseMove(event);
	}

	function onGlobalMouseUp() {
		if (isDragging.value) {
			onMouseUp();
		}
	}

	// 计算FPS
	function calculateFPS(timestamp) {
		if (!lastFrameTime) {
			lastFrameTime = timestamp;
		}

		frameCount++;
		const elapsed = timestamp - lastFrameTime;

		if (elapsed >= fpsUpdateInterval) {
			fps.value = Math.round((frameCount * 1000) / elapsed);
			frameCount = 0;
			lastFrameTime = timestamp;
		}

		requestAnimationFrame(calculateFPS);
	}

	// 获取内存使用情况
	function getMemoryUsage() {
		if (window.performance && window.performance.memory) {
			const memoryInfo = window.performance.memory;
			const usedHeapSize = memoryInfo.usedJSHeapSize;
			const totalHeapSize = memoryInfo.totalJSHeapSize;

			// 转换为MB并保留2位小数
			const usedHeapSizeMB = (usedHeapSize / (1024 * 1024)).toFixed(2);
			const totalHeapSizeMB = (totalHeapSize / (1024 * 1024)).toFixed(2);

			memoryUsage.value = `${usedHeapSizeMB} / ${totalHeapSizeMB} MB`;
		} else {
			memoryUsage.value = '不可用';
		}
	}


	function simulateCpuUsage() {

		const randomUsage = Math.floor(Math.random() * 30) + 10;
		cpuUsage.value = `${randomUsage}%`;
	}

	let performanceMonitorInterval;

	function startPerformanceMonitoring() {
		requestAnimationFrame(calculateFPS);

		performanceMonitorInterval = setInterval(() => {
			getMemoryUsage();
			simulateCpuUsage();
		}, 1000);
	}

	onMounted(() => {

		window.addEventListener('mousemove', onGlobalMouseMove);
		window.addEventListener('mouseup', onGlobalMouseUp);

		startPerformanceMonitoring();
	});

	onUnmounted(() => {
		window.removeEventListener('mousemove', onGlobalMouseMove);
		window.removeEventListener('mouseup', onGlobalMouseUp);

		clearInterval(performanceMonitorInterval);
	});
	// #endif
</script>

<style>
	.dev-panel {
		user-select: none;
		touch-action: none;
		z-index: 999;
	}

	/* #ifdef APP */
	.dev-panel {
		position: fixed;
		left: 10px;
		top: 10px;
		width: 120px;
		padding: 10px;
		border: red solid;
		background-color: black;
	}

	/* #endif */
	.performance-stats {
		display: flex;
		flex-direction: column;
		gap: 5px;
	}

	.stat-item {
		color: #ffff00;
		font-size: 12px;
	}
</style>