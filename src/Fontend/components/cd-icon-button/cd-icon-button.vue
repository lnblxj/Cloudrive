<template>
	<view class="cd-icon-button-container"
    :style="containerStyle"
    @mouseover="handleMouseOver"
    @mouseleave="handleMouseLeave"
	@click="handleButtonClick">
		<view v-if="vertical">
			<u-row justify="center">
				<u-icon :name="iconName" :color="iconColor" :size="iconSize"></u-icon>
			</u-row>
			<u-row>
				<u-text :text="text" :color="textColor" :size="textSize" align="center"></u-text>
			</u-row>
		</view>
		<view v-else>
			<u-row>
				<u-icon :name="iconName" :color="iconColor" :size="iconSize"></u-icon>
				<u-text :text="text" :color="textColor" :size="textSize" align="center"></u-text>
			</u-row>
		</view>
	</view>
</template>

<script setup>
	/**
	 * @name cd-icon-button
	 * @description 带图标按钮
	 * 
	 * @property {String} text    String | 按钮文本内容
	 * @property {String} textColor    String | 按钮文本颜色
	 * @property {String} textSize    String | 按钮文本大小
	 * @property {String} iconName      String | 图标名字
	 * @property {String} iconSize    String | 图标大小
	 * @property {String} iconColor    String | 图标颜色
	 * @property {Boolean} vertical Boolean | 垂直布局
	 * @property {String} bgColor    String | 按钮背景颜色
	 * @property {String} borderRadius    String | 按钮圆角大小
	 * @property {Boolean} hover      Boolean |  hover 效果
	 * @property {String} hoverColor  String | 按钮hover背景颜色
	 * @property {String} aniTime     String | 按钮hover背景颜色切换时长
	 * @property {String} to            String | 按钮路由目标地址
	 * @property {String} routerType     String | 按钮路由类型
	 */

	const props = defineProps({
		text: {
			type: String,
			default: ''
		},
		textColor: {
			type: String,
			default: '#000'
		},
		textSize: {
			type: String,
			default: '15px'
		},
		iconName: {
			type: String,
			default: 'user-smile-fill'
		},
		iconSize: {
			type: String,
			default: '10px'
		},
		iconColor: {
			type: String,
			default: '#000'
		},
		vertical: {
			type: Boolean,
			default: false
		},
		bgColor: {
			type: String,
			default: '#55aaff'
		},
		borderRadius: {
			type: String,
			default: '10px'
		},
		hover: {
			type: Boolean,
			default: false
		},
		hoverColor: {
			type: String,
			default: '#0000ff'
		},
		aniTime: {
			type: String,
			default: '0.5'
		},
		to: {
			type: String,
			default: ''
		},
		routerType: {
			type: String,
			default: 'navigateTo'
		}
	})
	
	import { ref, computed } from 'vue'
	
	const isHovering = ref(false)
	
	const handleMouseOver = () => {
	  if (props.hover) isHovering.value = true
	}
	
	const handleMouseLeave = () => {
	  if (props.hover) isHovering.value = false
	}
	
	const containerStyle = computed(() => ({
	  backgroundColor: isHovering.value ? props.hoverColor : props.bgColor,
	  borderRadius: props.borderRadius,
	  transition: props.hover ? `background-color ${props.aniTime}s ease` : 'none',
	  padding: '8px 12px',
	  cursor: 'pointer'
	}))
	
	const handleButtonClick = () => {
		if(props.to == '') return
		console.log('called')
		switch(props.routerType){
			case 'navigateTo':
			uni.navigateTo({
				url: props.to
			});
			break;
			case 'switchTab':
			uni.switchTab({
				url: props.to
			});
			break;
			case 'navigateBack':
			uni.navigateBack();
			break;
		}
	}
</script>

<style>

</style>