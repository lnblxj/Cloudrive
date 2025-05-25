<template>
	<view class="file-list-container">
		<!-- 顶部路径和操作栏 -->
		<view class="path-container">
			<view class="path-info">
				<u-icon name="wenjianleixing-biaozhuntu-wenjianjia" custom-prefix="custom-icon" size="20px" color="#4f65f1"></u-icon>
				<text class="path-text">{{currentPath || '根目录'}}</text>
			</view>
			<view class="view-actions">
				<u-icon 
					:name="listMode ? 'list' : 'grid'" 
					size="24px" 
					color="#666"
					@click="toggleViewMode"
				></u-icon>
			</view>
		</view>
		
		<!-- 排序控制区域 -->
		<view class="sort-container">
			<view class="sort-item" @click="changeSortType('name')">
				<text class="sort-text" :class="{active: sortType === 'name'}">名称</text>
				<u-icon v-if="sortType === 'name'" :name="sortOrder === 'asc' ? 'arrow-up' : 'arrow-down'" size="16px" :color="'#4f65f1'"></u-icon>
			</view>
			<view class="sort-item" @click="changeSortType('size')">
				<text class="sort-text" :class="{active: sortType === 'size'}">大小</text>
				<u-icon v-if="sortType === 'size'" :name="sortOrder === 'asc' ? 'arrow-up' : 'arrow-down'" size="16px" :color="'#4f65f1'"></u-icon>
			</view>
			<view class="sort-item" @click="changeSortType('date')">
				<text class="sort-text" :class="{active: sortType === 'date'}">日期</text>
				<u-icon v-if="sortType === 'date'" :name="sortOrder === 'asc' ? 'arrow-up' : 'arrow-down'" size="16px" :color="'#4f65f1'"></u-icon>
			</view>
		</view>
		
		<!-- 文件列表 -->
		<view class="file-list" :class="{'grid-view': !listMode}">
			<template v-if="fileList && fileList.length > 0">
				<view 
					v-for="(item, index) in fileList" 
					:key="index"
					@click="handleFileClick(item)"
					@contextmenu.prevent="handleContextMenu($event, item)"
					@touchstart="handleTouchStart($event, item)"
					@touchend="handleTouchEnd"
					@touchmove="handleTouchMove"
					class="file-item-wrapper"
				>
					<cd-file-list-item
						:grid="!listMode"
						:file-type="item.type"
						:file-name="item.name"
						:file-size="item.size"
						:date-time="item.date"
					></cd-file-list-item>
				</view>
			</template>
			<view v-else class="empty-list">
				<u-icon name="inbox-fill" size="48px" color="#ccc"></u-icon>
				<text class="empty-text">暂无文件</text>
			</view>
		</view>
	</view>
</template>

<script setup>
	import { ref, computed } from 'vue';
	const props = defineProps({
		path: {
			type: String,
			default: ''
		},
		files: {
			type: Array,
			default: () => []
		}
	});
	
	// 定义事件
	const emit = defineEmits(['file-click', 'folder-click', 'file-action']);
	
	// 视图模式（列表/网格）
	const listMode = ref(true);
	
	// 排序相关状态
	const sortType = ref('name');
	const sortOrder = ref('asc');
	
	// 当前路径
	const currentPath = computed(() => props.path);
	
	// 文件列表数据
	const fileList = computed(() => {
		let files = [];
		if (props.files && props.files.length > 0) {
			files = [...props.files];
		}
		const folders = files.filter(file => file.type === 'folder');
		const nonFolders = files.filter(file => file.type !== 'folder');
		
		// 根据排序类型和顺序对非文件夹项目进行排序
		nonFolders.sort((a, b) => {
			let result = 0;
			
			if (sortType.value === 'name') {
				result = a.name.localeCompare(b.name);
			} else if (sortType.value === 'date') {
				result = new Date(a.date) - new Date(b.date);
			} else if (sortType.value === 'size') {
				const sizeA = parseFileSize(a.size);
				const sizeB = parseFileSize(b.size);
				result = sizeA - sizeB;
			}
			
			// 根据排序顺序调整结果
			return sortOrder.value === 'asc' ? result : -result;
		});
		
		// 文件夹按名称排序
		folders.sort((a, b) => {
			const result = a.name.localeCompare(b.name);
			return sortOrder.value === 'asc' ? result : -result;
		});
		return [...folders, ...nonFolders];
	});
	
	// 切换视图模式
	function toggleViewMode() {
		listMode.value = !listMode.value;
	}
	
	// 处理文件点击事件
	function handleFileClick(file) {
		emit('file-action', {
			file: file,
			actionType: 'click'
		});
		if (file.type === 'folder') {
			emit('folder-click', file);
		} else {
			emit('file-click', file);
		}
	}
	
	// 解析文件大小字符串为数字（字节）
	function parseFileSize(sizeStr) {
		if (!sizeStr || sizeStr === '-') return 0;
		
		const units = {
			'B': 1,
			'KB': 1024,
			'MB': 1024 * 1024,
			'GB': 1024 * 1024 * 1024,
			'TB': 1024 * 1024 * 1024 * 1024
		};
		
		const regex = /(\d+(\.\d+)?)\s*(B|KB|MB|GB|TB)/i;
		const match = sizeStr.match(regex);
		
		if (match) {
			const size = parseFloat(match[1]);
			const unit = match[3].toUpperCase();
			return size * units[unit];
		}
		
		return 0;
	}
	
	// 切换排序类型和顺序
	function changeSortType(type) {
		if (sortType.value === type) {
			sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
		} else {
			sortType.value = type;
			sortOrder.value = 'asc';
		}
	}
	
	// 长按相关变量
	const touchTimer = ref(null);
	const touchStartTime = ref(0);
	const longPressThreshold = 500;
	const touchMoved = ref(false);
	const currentTouchItem = ref(null);
	
	// 处理触摸开始事件
	function handleTouchStart(event, item) {
		touchStartTime.value = Date.now();
		currentTouchItem.value = item;
		touchMoved.value = false;
		touchTimer.value = setTimeout(() => {
			if (!touchMoved.value) {
				emit('file-action', {
					file: item,
					actionType: 'longpress',
					event: event
				});
			}
		}, longPressThreshold);
	}
	
	// 处理触摸结束事件
	function handleTouchEnd() {
		if (touchTimer.value) {
			clearTimeout(touchTimer.value);
			touchTimer.value = null;
		}
		currentTouchItem.value = null;
	}
	
	// 处理触摸移动事件
	function handleTouchMove() {
		touchMoved.value = true;
		if (touchTimer.value) {
			clearTimeout(touchTimer.value);
			touchTimer.value = null;
		}
	}
	
	// 处理右键菜单事件
	function handleContextMenu(event, item) {
		event.preventDefault();
		const platform = uni.getSystemInfoSync().platform;
		if (platform !== 'ios' && platform !== 'android') {
			emit('file-action', {
				file: item,
				actionType: 'contextmenu',
				event: event
			});
		}
	}
</script>

<style scoped>
	.file-list-container {
		width: 100%;
		height: 100%;
		display: flex;
		flex-direction: column;
	}
	
	.path-container {
		width: 100%;
		padding: 10px 15px;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		border-bottom: 1px solid #eee;
	}
	.path-info {
		margin-left: 8px;
		display: flex;
		flex-direction: row;
		align-items: center;
		flex: 1;
		overflow: hidden;
	}
	
	.path-text {
		margin-left: 8px;
		font-size: 16px;
		color: #333;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	
	.sort-container {
		width: 100%;
		padding: 8px 15px;
		display: flex;
		flex-direction: row;
		align-items: center;
		border-bottom: 1px solid #eee;
		background-color: #f9f9f9;
		flex-wrap: wrap;
	}
	
	.sort-item {
		display: flex;
		flex-direction: row;
		align-items: center;
		margin-right: 20px;
		padding: 4px 8px;
		cursor: pointer;
	}
	
	.sort-text {
		font-size: 14px;
		color: #666;
		margin-right: 4px;
	}
	
	.sort-text.active {
		color: #4f65f1;
		font-weight: bold;
	}
	
	.file-list {
		flex: 1;
		padding: 10px;
		overflow-y: auto;
	}
	
	.grid-view {
		display: grid;
		grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
		gap: 12px;
		padding: 12px;
		width: 100%;
		grid-auto-rows: min-content;
	}
	
	.file-item-wrapper {
		cursor: pointer;
	}
	
	.grid-view .file-item-wrapper {
		width: 100%;
		min-height: 100px;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 8px;
		background-color: #fff;
		border-radius: 8px;
		transition: all 0.3s ease;
	}
	
	.grid-view .file-item-wrapper:hover {
		box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
		transform: translateY(-2px);
	}
	
	.empty-list {
		width: 100%;
		height: 100%;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		padding: 40px 0;
	}
	
	.empty-text {
		margin-top: 10px;
		color: #999;
		font-size: 14px;
	}
	
	/* 手机端竖屏适配 */
	@media screen and (max-width: 480px) {
		.path-container {
			padding: 8px 12px;
		}
		
		.path-text {
			font-size: 14px;
			max-width: 200px;
		}
		
		.sort-container {
			padding: 5px 12px;
			justify-content: space-between;
		}
		
		.sort-item {
			margin-right: 10px;
			padding: 3px 5px;
			margin-bottom: 2px;
		}
		
		.sort-text {
			font-size: 12px;
		}
		
		.file-list {
			padding: 8px;
		}
		
		.grid-view {
			grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
			gap: 8px;
			padding: 8px;
		}
	}
	
	/* 小型手机适配 */
	@media screen and (max-width: 320px) {
		.path-text {
			max-width: 150px;
		}
		
		.grid-view {
			grid-template-columns: repeat(auto-fill, minmax(60px, 1fr));
			gap: 6px;
			padding: 6px;
		}
	}
</style>