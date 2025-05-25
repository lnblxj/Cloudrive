<template>
	<view>
		<text 
			class="cd-router-link" 
			:class="{'cd-router-link--disabled': disabled}"
			:style="{color: color, fontSize: size, fontFamily: font}"
			@click="onLinkClick()"
			@mouseover="isHover = true"
			@mouseleave="isHover = false"
		>
			{{text}}
		</text>
	</view>
</template>

<script setup>
	import { ref } from 'vue'
	/**
	 * @name cd-router-link
	 * @description 声明式路由链接
	 * 
	 * @property {String} text    String | 链接文本内容
	 * @property {String} color    String | 链接文本颜色
	 * @property {String} size    String | 链接文本大小
	 * @property {String} to      String | 路由地址
	 * @property {String} font    String | 字体名称
	 * @property {String} target  String | 链接打开方式，可选值：_self（当前页面打开）、_blank（新标签页打开），仅在PC端生效
	 */
	defineOptions({
		name: 'cd-router-link'
	})
	const props = defineProps({
		to: {
			type: String,
			default: ''
		},
		color: {
			type: String,
			default: '#00ff00'
		},
		text: {
			type: String,
			default: '链接文字'
		},
		size: {
			type: String,
			default: '16px'
		},
		font: {
			type: String,
			default: 'PingFang SC'
		},
		disabled: {
			type: Boolean,
			default: false
		},
		hoverColor: {
			type: String,
			default: ''
		},
		target: {
			type: String,
			default: '_self'
		}
	})

	const isHover = ref(false)

function onLinkClick() {
	if(props.disabled) return
	
	if(!props.to) {
		console.error('cd-router-link: to prop is required')
		return
	}
	
	// #ifdef WEB
	if(props.target === '_blank') {
		window.open('/#'+props.to, '_blank')
		return
	}
	// #endif
	
	uni.navigateTo({
		url: props.to,
		success: res => { },
		fail: (err) => {
			console.error('cd-router-link navigation failed:', err)
		},
		complete: () => { }
	})
}
</script>

<style lang="scss">
	.cd-router-link {
		display: inline-block;
		margin-right: 20rpx;
		transition: all 0.3s ease;
		// #ifdef WEB
		cursor: pointer;
		// #endif
	}
	
	.cd-router-link:hover {
		color: v-bind('hoverColor || color');
	}
	
	.cd-router-link--disabled {
		opacity: 0.5;
		cursor: not-allowed !important;
	}
</style>