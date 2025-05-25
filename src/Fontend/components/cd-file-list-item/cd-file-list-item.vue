<template>
	<view class="file-item" :class="{'grid-mode': grid, 'list-mode': !grid}">
		<view class="file-icon">
			<u-image :src="fileIcon" class="icon-image" mode="aspectFill" @error="handleImageError" :key="imageKey" width="40" height="40"></u-image>
		</view>
		<view class="file-info" v-if="!grid">
			<view class="file-name">{{fileName}}</view> 
			<view class="file-meta">
				<text class="file-size">{{fileSize}}</text>
				<text>{{dateTime}}</text>
			</view>
		</view>
		<view class="file-name-grid" v-else>{{fileName}}</view>
	</view>
</template>

<script setup>
	/**
	 * @name cd-file-list-item
	 * @description 文件项组件，用于显示文件列表中的单个文件项，支持列表模式和宫格模式
	 * 
	 * @property {Boolean} grid - 是否使用宫格模式显示，默认为false（列表模式）
	 * @property {String} fileType - 文件类型，用于确定显示的图标，默认为'doc'
	 * @property {String} fileName - 文件名称
	 * @property {String} fileSize - 文件大小，如'5.8MB'
	 * @property {String} dateTime - 文件日期时间，如'2023-12-19'
	 */
	const props = defineProps({
		grid: {
			type: Boolean,
			default: false
		},
		fileType: {
			type: String,
			default: 'doc'
		},
		fileName: {
			type: String,
			default: ''
		},
		fileSize: {
			type: String,
			default: ''
		},
		dateTime: {
			type: String,
			default: ''
		}
	})

	import { standardIcon, thumbnail } from '@/common/fileIconMap.ts'
	import { ref } from 'vue'
	const imageKey = ref(0)
	
	import { computed } from 'vue'

	const fileIcon = computed(() => {
		let type = props.fileType?.toLowerCase() || 'unknown'
		const iconMap = props.grid ? standardIcon : thumbnail
		try{
			return iconMap[type] || iconMap['unknown']
		}catch(err){
			return iconMap['unknown']
		}
	})

	
	// 处理图片加载错误
	function handleImageError() {
		// 通过改变key强制重新加载图片
		imageKey.value++
		// 最多重试3次
		if (imageKey.value > 3) {
			console.error('图标加载失败:', fileIcon.value)
		}
	}
</script>

<style>
.file-item {
	padding: 8px;
	box-sizing: border-box;
	transition: all 0.3s ease;
}

.grid-mode {
	padding: 2px;
	margin-bottom: 0;
}

.list-mode {
	display: flex;
	flex-direction: row;
	align-items: center;
}

.list-mode:hover {
	background-color: #f5f5f5;
	box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
	transform: translateY(-2px);
}

.grid-mode {
	display: flex;
	flex-direction: column;
	align-items: center;
	text-align: center;
	width: 100px;
	height: auto;
	margin: 4px;
	transition: all 0.3s ease;
}

.grid-mode:hover {
	background-color: #f5f5f5;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
	transform: scale(1.02);
}
.file-icon {
	margin-right: 12px;
	width: 48px;
	height: 48px;
	display: flex;
	align-items: center;
	justify-content: center;
	background-color: transparent;
	overflow: visible;
}

.grid-mode .file-icon {
	margin-right: 0;
	margin-bottom: 8px;
	width: 56px;
	height: 56px;
}

.icon-image {
	width: 48px !important;
	height: 48px !important;
	display: block;
}

.grid-mode .icon-image {
	width: 56px !important;
	height: 56px !important;
	display: block;
}

.file-name {
	font-size: 18px;
	color: #333;
	margin-bottom: 6px;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.file-name-grid {
	font-size: 14px;
	color: #333;
	width: 100%;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	text-align: center;
	padding: 0 4px;
}

.file-meta {
	display: flex;
	flex-direction: row;
	align-items: center;
	font-size: 14px;
	color: #999;
}

.file-info {
	flex: 1;
	display: flex;
	flex-direction: column;
	justify-content: center;
}

.file-name {
	font-size: 16px;
	color: #333;
	margin-bottom: 4px;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.file-name-grid {
	font-size: 12px;
	color: #333;
	width: 100%;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	text-align: center;
}

.file-meta {
	display: flex;
	flex-direction: row;
	align-items: center;
	font-size: 12px;
	color: #999;
}

.file-size {
	margin-right: 10px;
}

/* 手机端竖屏适配 */
@media screen and (max-width: 480px) {
	.file-item {
		padding: 8px;
	}
	
	.list-mode {
		width: 100%;
	}
	
	.grid-mode {
		width: 100%;
		height: auto;
		padding: 10px 5px;
		margin: 0;
		margin-bottom: 5px;
	}
	
	.icon-image {
		width: 36px;
		height: 36px;
	}
	
	.grid-mode .icon-image {
		width: 42px;
		height: 42px;
	}
	
	.file-name {
		font-size: 14px;
		max-width: 200px;
	}
	
	.file-name-grid {
		margin-top: 5px;
		width: 100%;
		max-width: 120px;
	}
	
	.file-meta {
		font-size: 11px;
	}
}

/* 小型手机适配 */
@media screen and (max-width: 320px) {
	.file-name {
		max-width: 150px;
	}
	
	.file-name-grid {
		max-width: 100px;
	}
}
</style>