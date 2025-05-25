<template>
	<cd-page header>
		<!-- #ifdef APP -->
		<u-navbar back-text="返回" title="用户协议" height="50"></u-navbar>
		<!-- #endif -->
		<view class="container">
			<view class="section">
				<text class="title">用户协议</text>
				<text class="update-date">最后更新日期：2025年3月4日</text>
				
				<view class="chapter">
					<text class="chapter-title">1. 服务内容</text>
					<text class="chapter-content">本协议适用于由cloudrive提供的网盘存储、文件分享、博客文章发布及相关服务（以下简称"服务"）。服务包括但不限于：</text>
					<text class="list-item">- 文件上传、下载、分享及管理</text>
					<text class="list-item">- 用户注册、登录及账户管理</text>
					<text class="list-item">- 博客文章发布、评论及资源广场功能</text>
					<text class="list-item">- 管理员后台管理功能</text>
				</view>

				<view class="chapter">
					<text class="chapter-title">2. 账户责任</text>
					<text class="sub-chapter-title">2.1 注册要求</text>
					<text class="chapter-content">用户需通过邮箱验证完成注册，禁止冒用他人信息。扫码登录功能需通过官方授权渠道使用。</text>
					
					<text class="sub-chapter-title">2.2 账户安全</text>
					<text class="chapter-content">您需妥善保管账户凭证，因密码泄露导致的损失由用户自行承担。</text>
				</view>

				<view class="chapter">
					<text class="chapter-title">3. 使用规则</text>
					<text class="sub-chapter-title">3.1 禁止内容</text>
					<text class="chapter-content">不得上传或分享以下内容：</text>
					<text class="list-item">- 违反法律法规的文件</text>
					<text class="list-item">- 病毒、木马等恶意程序</text>
					<text class="list-item">- 侵犯知识产权的材料</text>
					<text class="list-item">- 反动、色情等违规信息</text>

					<text class="sub-chapter-title">3.2 文件管理</text>
					<text class="list-item">- 私密链接分享功能需自行承担链接泄露风险</text>
					<text class="list-item">- 系统自动清理超过180天未激活的分享链接</text>
				</view>

				<view class="chapter">
					<text class="chapter-title">4. 服务变更与终止</text>
					<text class="chapter-content">我们保留因系统升级调整功能的权利。出现以下情况时可能终止服务：</text>
					<text class="list-item">- 用户发布三次以上违规内容</text>
					<text class="list-item">- 利用系统漏洞进行非法操作</text>
					<text class="list-item">- 超过12个月未登录的僵尸账户</text>
				</view>

				<view class="chapter">
					<text class="chapter-title">5. 免责声明</text>
					<text class="chapter-content">不对以下情况承担责任：</text>
					<text class="list-item">- 因Sentinel流量控制导致的临时服务降级</text>
					<text class="list-item">- 用户自行分享文件引发的版权纠纷</text>
					<text class="list-item">- 不可抗力导致的超过72小时服务中断</text>
				</view>
			</view>

			<view class="section">
				<text class="title">隐私政策</text>
				<text class="update-date">最后更新日期：2025年12月26日</text>

				<view class="chapter">
					<text class="chapter-title">1. 数据收集</text>
					<text class="sub-chapter-title">1.1 直接收集</text>
					<text class="list-item">- 认证数据：邮箱、加密存储的密码、扫码登录令牌（Redis临时存储）</text>
					<text class="list-item">- 用户资料：昵称、头像（通过文件服务存储）、联系方式</text>
					<text class="list-item">- 文件元数据：文件名、大小、分享设置、访问日志</text>

					<text class="sub-chapter-title">1.2 自动收集</text>
					<text class="list-item">- 设备信息：IP地址、浏览器类型、操作日志</text>
					<text class="list-item">- 服务日志：Nacos生成的微服务调用记录（保留30天）</text>
				</view>

				<view class="chapter">
					<text class="chapter-title">2. 数据使用</text>
					<text class="list-item">- 通过Resend发送账户相关邮件（注册确认、密码重置）</text>
					<text class="list-item">- 采用MyBatis-Plus管理MySQL数据库中的结构化数据</text>
					<text class="list-item">- OpenFeign服务间通信仅传输必要业务数据</text>
				</view>

				<view class="chapter">
					<text class="chapter-title">3. 数据共享</text>
					<text class="sub-chapter-title">3.1 第三方服务</text>
					<text class="list-item">- Minio对象存储：所有用户文件的物理存储</text>
					<text class="list-item">- 阿里云Sentinel：微服务流量监控数据</text>
					<text class="list-item">- Resend邮件服务：收发账户相关邮件</text>

					<text class="sub-chapter-title">3.2 法律要求</text>
					<text class="chapter-content">在收到合法司法要求时，可能披露管理员服务中的审计日志。</text>
				</view>

				<view class="chapter">
					<text class="chapter-title">4. 数据安全</text>
					<text class="list-item">- 文件传输使用HTTPS加密</text>
					<text class="list-item">- 敏感操作需二次验证（如密码修改）</text>
					<text class="list-item">- 定期进行安全渗透测试</text>
				</view>

				<view class="chapter">
					<text class="chapter-title">5. 用户权利</text>
					<text class="sub-chapter-title">5.1 访问与更正</text>
					<text class="chapter-content">通过用户服务API可随时下载个人数据副本，修改资料信息。</text>

					<text class="sub-chapter-title">5.2 数据删除</text>
					<text class="list-item">- 删除账户将同步清除认证服务和用户服务数据</text>
					<text class="list-item">- 博客评论保留匿名化处理（替换用户名）</text>
					<text class="list-item">- 文件服务数据需手动清空后注销</text>
				</view>

				<view class="chapter">
					<text class="chapter-title">6. 国际传输</text>
					<text class="chapter-content">数据主要存储在境内数据中心，Minio存储集群可能涉及跨境备份。</text>
				</view>

				<view class="chapter">
					<text class="chapter-title">7. 政策更新</text>
					<text class="chapter-content">重大变更将通过博客服务发布公告，持续使用视为接受新条款。</text>
				</view>
			</view>
		</view>
	</cd-page>
</template>

<style>
.container {
	padding: 20px;
}

.section {
	margin-bottom: 40px;
}

.title {
	font-size: 24px;
	font-weight: bold;
	margin-bottom: 10px;
	display: block;
}

.update-date {
	color: #666;
	font-size: 14px;
	margin-bottom: 20px;
	display: block;
}

.chapter {
	margin-bottom: 30px;
}

.chapter-title {
	font-size: 20px;
	font-weight: bold;
	margin-bottom: 15px;
	display: block;
}

.sub-chapter-title {
	font-size: 18px;
	font-weight: bold;
	margin: 15px 0 10px;
	display: block;
}

.chapter-content {
	font-size: 16px;
	line-height: 1.6;
	margin-bottom: 10px;
	display: block;
}

.list-item {
	font-size: 16px;
	line-height: 1.6;
	margin: 5px 0;
	padding-left: 20px;
	display: block;
}

/* 移动端适配 */
@media screen and (max-width: 768px) {
	.container {
		padding: 15px;
	}

	.title {
		font-size: 20px;
	}

	.chapter-title {
		font-size: 18px;
	}

	.sub-chapter-title {
		font-size: 16px;
	}

	.chapter-content,
	.list-item {
		font-size: 14px;
	}
}
</style>