<template>
	<cd-page header>
		<!-- #ifdef APP -->
		<u-navbar back-text="返回" title="举报" height="50"></u-navbar>
		<!-- #endif -->
		<view class="report-container card-border">
			<u-form :model="formData" ref="reportForm" label-position="top">
				<!-- 举报类型 -->
				<u-form-item label="举报类型" prop="reportType" :required="true">
					<u-radio-group v-model="formData.reportType">
						<u-radio v-for="(item, index) in reportTypes" :key="index" :name="item.value">{{ item.label }}</u-radio>
					</u-radio-group>
				</u-form-item>

				<!-- 举报内容 -->
				<u-form-item label="举报内容" prop="reportContent" :required="true">
					<u-input placeholder="举报原因" v-model="formData.reportContent" type="textarea" border height="250" auto-height
						maxlength="300" />
				</u-form-item>

				<!-- 证据材料 -->
				<u-form-item label="证据材料" prop="evidence">
					<view class="evidence-links">
						<view v-for="(link, index) in evidenceLinks" :key="index" class="evidence-link-item">
							<u-input v-model="evidenceLinks[index]" placeholder="请输入证据链接" :border="true"></u-input>
							<u-icon name="close" size="20" @click="removeLink(index)" class="remove-link-btn"></u-icon>
						</view>
						<u-button size="mini" type="primary"text="添加链接" @click="addLink"
							:disabled="evidenceLinks.length >= 5" class="add-link-btn"><u-icon name="plus"></u-icon></u-button>
					</view>
				</u-form-item>

				<!-- 提交按钮 -->
				<view class="submit-btn-wrapper">
					<u-button type="primary" @click="submitReport">提交举报</u-button>
				</view>
			</u-form>
		</view>
	</cd-page>
</template>

<script setup>
	import {
		ref,
		reactive,
		onMounted,
		getCurrentInstance
	} from 'vue';

	const {proxy} = getCurrentInstance();

	const props = defineProps({
		id: {
			type: [String, Number],
			default: ''
		}
	});

	// 表单数据
	const formData = reactive({
		blogId: '',
		reportType: 0,
		reportContent: '',
		evidence: ''
	});

	// 表单验证规则
	const rules = {
		reportContent: [{
			required: true,
			message: '请填写举报内容',
			trigger: ['blur', 'change']
		}]
	};

	onMounted(() => {
		if (props.id) {
			formData.blogId = props.id;
			proxy.$refs.reportForm.setRules(rules);
		}
	});
	// 举报类型选项
	const reportTypes = [{
			label: '垃圾广告',
			value: 0
		},
		{
			label: '违法犯罪',
			value: 1
		},
		{
			label: '侵犯权益',
			value: 2
		},
		{
			label: '其他原因',
			value: 3
		}
	];

	// 证据链接列表
	const evidenceLinks = ref(['']);

	// 添加链接
	const addLink = () => {
		if (evidenceLinks.value.length < 5) {
			evidenceLinks.value.push('');
		} else {
			proxy.$u.toast('最多添加5条证据链接');
		}
	};

	// 删除链接
	const removeLink = (index) => {
		evidenceLinks.value.splice(index, 1);
		// 确保至少有一个输入框
		if (evidenceLinks.value.length === 0) {
			evidenceLinks.value.push('');
		}
	};

	// 提交举报
	const submitReport = async () => {
		// 表单验证
		if (!formData.blogId) {
			return proxy.$u.toast('缺少文章ID');
		}
		
		// 使用表单验证
		const valid = await proxy.$refs.reportForm.validate();
		if (!valid) {
			return;
		}

		let evidenceData = [];
		const validLinks = evidenceLinks.value.filter(link => link.trim() !== '');
		if (validLinks.length > 0) {
			evidenceData = validLinks.map(link => {
				return {
					url: link,
					name: '链接证据'
				};
			});
		}
		formData.evidence = evidenceData.length > 0 ? JSON.stringify(evidenceData) : '';

		try {
			const res = await proxy.$http.reportPost(formData);
			if (res.isSuccess) {
				proxy.$u.toast('举报成功');
				setTimeout(() => {
					uni.navigateBack()
				}, 1500);
			} else {
				proxy.$u.toast(res.message || '举报失败');
			}
		} catch (error) {
			console.error('举报提交失败', error);
			proxy.$u.toast('网络错误，请稍后重试');
		}
	};
</script>

<style>
	.report-container {
		padding: 20px;
		width: 100%;
		box-sizing: border-box;
		background-color: #fff;
		border-radius: 20rpx;
	}

	.tips {
		font-size: 12px;
		color: #909399;
		margin-top: 5px;
	}

	.submit-btn-wrapper {
		margin-top: 30px;
		display: flex;
		justify-content: center;
	}

	/* 证据链接样式 */
	.evidence-links {
		width: 100%;
	}

	.evidence-link-item {
		display: flex;
		align-items: center;
		margin-bottom: 10px;
	}

	.remove-link-btn {
		margin-left: 10px;
		color: #fa3534;
	}

	.add-link-btn {
		margin-top: 5px;
	}

	/* 适配Web端 */
	/* #ifdef WEB */
	.report-container {
		max-width: 800px;
		margin: 0 auto;
	}

	/* #endif */
</style>