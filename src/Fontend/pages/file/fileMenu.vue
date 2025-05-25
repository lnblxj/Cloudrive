<template>
	<!-- PC端右键菜单 -->
	<view v-if="showContextMenu && !isAppPlatform" class="context-menu" :style="menuPosition" @click.stop>
		<view class="menu-item" v-for="(item, index) in menuItems" :key="index" @click="handleMenuClick(item.action)">
			{{ item.label }}
		</view>
	</view>

	<!-- 移动端ActionSheet -->
	<l-action-sheet
		v-if="isAppPlatform"
		v-model="showActionSheet"
		:list="menuItems"
		:description="tips.text"
		@select="handleActionSheetSelect"
		cancel-text="取消"
		:rowCol="[4]"
		@close="closeMenu"
	/>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';

const props = defineProps({
	fileType: {
		type: String,
		default: 'file'
	},
	position: {
		type: Object,
		default: () => ({ x: 0, y: 0 })
	}
});

// 当前操作的文件
const currentFile = ref(null);
const emit = defineEmits(['menu-action', 'close']);

// 判断是否为APP平台
const isAppPlatform = computed(() => {
	// #ifdef APP
	return true;
	// #endif
	// #ifndef APP
	return false;
	// #endif
});

// 菜单显示状态
const showContextMenu = ref(false);
const showActionSheet = ref(false);

// 菜单位置样式
const menuPosition = computed(() => ({
	left: props.position.x + 'px',
	top: props.position.y + 'px'
}));

// 菜单提示信息
const tips = {
	text: props.fileType === 'folder' ? '文件夹操作' : '文件操作',
	color: '#909399',
	fontSize: 28
};

// 根据文件类型获取菜单项
const menuItems = computed(() => {
	const items = [];
	if (props.fileType === 'folder') {
		items.push(
			{ label: '重命名', action: 'rename', icon: 'edit', color: '#000000' },
			{ label: '删除', action: 'delete', icon: 'delete', color: '#fa3534' }
		);
	} else {
		items.push(
			{ label: '下载', action: 'download', icon: 'download', color: '#000000' },
			{ label: '重命名', action: 'rename', icon: 'edit', color: '#000000' },
			{ label: '分享', action: 'share', icon: 'share', color: '#000000' },
			{ label: '删除', action: 'delete', icon: 'delete', color: '#fa3534' }
		);
	}
	return items;
});

// 显示菜单
function showMenu() {
	if (isAppPlatform.value) {
		showActionSheet.value = true;
	} else {
		showContextMenu.value = true;
	}
}

// 关闭菜单
function closeMenu() {
	showContextMenu.value = false;
	showActionSheet.value = false;
	emit('close');
}

// 处理PC端菜单点击
function handleMenuClick(action) {
	emit('menu-action', action);
	closeMenu();
}

// 处理移动端ActionSheet选择
function handleActionSheetSelect(index) {
	const item = menuItems.value[index];
	if (item && !item.disabled) {
		emit('menu-action', item.action);
		closeMenu();
	}
}

// 处理全局点击事件
function handleGlobalClick(event) {
	if (!showContextMenu.value || isAppPlatform.value) return;
	
	// 获取菜单元素
	const menuElement = document.querySelector('.context-menu');
	if (!menuElement || !menuElement.contains(event.target)) {
		closeMenu();
	}
}

onMounted(() => {
	if (!isAppPlatform.value) {
		document.addEventListener('click', handleGlobalClick);
	}
});

onUnmounted(() => {
	if (!isAppPlatform.value) {
		document.removeEventListener('click', handleGlobalClick);
	}
});

defineExpose({
	showMenu,
	closeMenu
});
</script>

<style scoped>
.context-menu {
	position: fixed;
	background-color: #ffffff;
	border-radius: 4px;
	box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
	padding: 5px 0;
	z-index: 1000;
}

.menu-item {
	padding: 8px 16px;
	cursor: pointer;
	font-size: 14px;
	color: #333;
}

.menu-item:hover {
	background-color: #f5f7fa;
}
</style>
