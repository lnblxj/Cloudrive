<template>
	<view class="share-card card-border card-border-hover">
		<u-row justify="center">
			<!-- #ifdef WEB -->
			<u-col :span="1">
				<u-image width="60px" height="60px" :src="currentPanIcon"></u-image>
			</u-col>
			<u-col :span="2">
				<u-text :text="linkText"></u-text>
			</u-col>
			<!-- #endif -->
			<u-button type="primary" v-if="!showLink"
				@click="()=>{showLink = true}">获取分享链接</u-button>
			<u-col :span="9" v-if="showLink">
				<u-text text="链接:"></u-text>
				<view class="share-link">
					<u-link :href="shareURL" :text="shareURL"></u-link>
				</view>
				<u-text text="提取密码:" v-if="encryption"></u-text>
				<view class="share-password" v-if="encryption">
					<u-text selectable :text="passWord"></u-text>
				</view>
			</u-col>
		</u-row>
	</view>
</template>

<script setup>
	import { platformMap } from '@/common/platformMap.ts'
	import {ref} from 'vue'
	/**
	 * @name cd-share-card
	 * @description 分享链接卡片
	 * 
	 * @property {String} shareURL    String | 分享链接
	 * @property {Boolean} encryption String | 需要密码
	 * @property {String} passWord    String | 提取密码
	 */
	defineOptions({
		name: 'cd-comment'
	})
	const props = defineProps({
		shareURL: {
			type: String,
			default: 'https://sboxm.top'
		},
		encryption: {
			type: Boolean,
			default: false
		},
		passWord: {
			type: String,
			default: ''
		}

	})

	const showLink = ref(false)
	const currentPanIcon = ref('')
	const linkText = ref('')
	//计算相关
	for (const [domain, info] of Object.entries(platformMap)) {
		const urlMatch = props.shareURL.match(/(?:https?:\/\/)?(?:[^@\/\n]+@)?(?:www\.)?([^:\/?\n]+)/i)
		if (urlMatch && urlMatch[1] && urlMatch[1].includes(domain) && info) {
			currentPanIcon.value = info.icon
			linkText.value = info.name
			break
		}
	}
</script>

<style scoped>
	.share-card {
		border-radius: 10px;
		padding: 5px;
		width: 100%;
		margin: 20px 0 20px 0;
	}

	.share-link,
	.share-password {
		border: #bcbcbc solid 1px;
		border-radius: 8px;
		padding: 5px 8px 5px 8px;
		margin-top: 3px;
		width: 100%;
	}
</style>