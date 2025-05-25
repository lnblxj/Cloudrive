<template>
	<view>
		<canvas canvas-id="dynamicBgCanvas" id="dynamicBgCanvas" :willReadFrequently="true"></canvas>
		<view class="glass-overlay">
			<slot></slot>
		</view>
	</view>
</template>

<script lang="ts">
	import { defineComponent, onMounted, onUnmounted } from 'vue'
	interface Shape {
		initialX : number;        // 初始 x 坐标
		initialY : number;        // 初始 y 坐标
		width : number;           // 宽度
		height : number;          // 高度
		amplitudeX : number;      // x 方向振幅
		amplitudeY : number;      // y 方向振幅
		frequencyX : number;      // x 方向频率 (周期/秒)
		frequencyY : number;      // y 方向频率
		phaseX : number;          // x 方向相位偏移
		phaseY : number;          // y 方向相位偏移
		baseHue : number;         // 基础色调 (HSL 颜色中的 H)
	}

	export default defineComponent({
		setup() {
			let animationId : NodeJS.Timeout
			let canvasContext : UniApp.CanvasContext | null = null
			let canvasWidth = 0
			let canvasHeight = 0

			onMounted(() => {

				canvasContext = uni.createCanvasContext('dynamicBgCanvas');
				if (!canvasContext) return;

				// 获取设备信息以设置Canvas尺寸
				const info = uni.getSystemInfoSync();
				canvasWidth = info.windowWidth;
				canvasHeight = info.windowHeight;
				const shapes : Shape[] = [
					{
						initialX: canvasWidth / 2 - 50,
						initialY: 100,
						width: 400,
						height: 400,
						amplitudeX: 250,
						amplitudeY: 0,
						frequencyX: 0.01,
						frequencyY: 0,
						phaseX: 0,
						phaseY: 0,
						baseHue: 0
					},
					{
						initialX: canvasWidth / 2 - 50,
						initialY: canvasHeight - 550,
						width: 400,
						height: 400,
						amplitudeX: 0,
						amplitudeY: 500, 
						frequencyX: 0,
						frequencyY: 0.01,
						phaseX: 0,
						phaseY: 0,
						baseHue: 120
					},
					{
						initialX: 0,
						initialY: 0,
						width: 400,
						height: 400,
						amplitudeX: 100,
						amplitudeY: 100,
						frequencyX: 0.01,
						frequencyY: 0.01, 
						phaseX: 0,
						phaseY: Math.PI / 2,
						baseHue: 240 
					}
				];

				// 记录动画开始时间
				const startTime = performance.now();

				// 动画函数
				// HSL转RGB的辅助函数
				function hslToRgb(h: number, s: number, l: number): string {
					h = h % 360;
					s = s / 100;
					l = l / 100;

					let c = (1 - Math.abs(2 * l - 1)) * s;
					let x = c * (1 - Math.abs((h / 60) % 2 - 1));
					let m = l - c/2;
					let r = 0, g = 0, b = 0;

					if (0 <= h && h < 60) {
						r = c; g = x; b = 0;
					} else if (60 <= h && h < 120) {
						r = x; g = c; b = 0;
					} else if (120 <= h && h < 180) {
						r = 0; g = c; b = x;
					} else if (180 <= h && h < 240) {
						r = 0; g = x; b = c;
					} else if (240 <= h && h < 300) {
						r = x; g = 0; b = c;
					} else if (300 <= h && h < 360) {
						r = c; g = 0; b = x;
					}

					r = Math.round((r + m) * 255);
					g = Math.round((g + m) * 255);
					b = Math.round((b + m) * 255);

					return `rgb(${r},${g},${b})`;
				}

				function animate(timestamp : number) {
					const time = (timestamp - startTime) / 1000;

					if (!canvasContext) return;
					canvasContext.clearRect(0, 0, canvasWidth, canvasHeight);

					// 遍历每个形状，更新位置和颜色并绘制
					shapes.forEach(shape => {
						const x = shape.initialX + shape.amplitudeX * Math.sin(2 * Math.PI * shape.frequencyX * time + shape.phaseX);
						const y = shape.initialY + shape.amplitudeY * Math.sin(2 * Math.PI * shape.frequencyY * time + shape.phaseY);

						const hue = (shape.baseHue + time * 30) % 360;

						const color = hslToRgb(hue, 100, 50);
						canvasContext.setFillStyle(color);
						canvasContext.fillRect(x, y, shape.width, shape.height);
					});
					canvasContext.draw(false);

					animationId = setTimeout(() => {
						animate(performance.now());
					}, 16);
				}

				// 启动动画
				animationId = setTimeout(() => {
					animate(performance.now());
				}, 16);
			});

			// 组件卸载时取消动画
			onUnmounted(() => {
				clearTimeout(animationId);
			});
			return {};
		}
	});
</script>

<style scoped>
	canvas {
		width: 100%;
		height: 100vh;
		z-index: 0;
	}

	.glass-overlay {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		backdrop-filter: blur(160px) saturate(160%);
		background: linear-gradient(45deg,
				rgba(255, 255, 255, 0.1),
				rgba(255, 255, 255, 0.05)) content-box;
		z-index: 1;
	}
</style>