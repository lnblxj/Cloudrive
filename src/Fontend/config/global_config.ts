export const cloudriveConfig = {
	login: {
		url: '/pages/auth/login/login'
	},
	index: {
		url: '/pages/index/index'
	},
	error: {
		url: '/pages/error/404' // 404 Not Found 错误页面路径
	},
	BASE_URL: {
		url: 'http://localhost:15000' // 全局请求地址
	},
	IPV4_PROBE: {
		url: 'https://4.ipw.cn' // ipv4 探针
	},
	IPV6_PROBE: {
		url: 'https://6.ipw.cn' // ipv6 探针
	},
	DNS: {
		ip: '8.8.8.8'  // DNS测试
	},
	SHOW_HTTP_LOG: {
		status: false  // 显示接口请求日志
	}
}

export const apiConfig = {
	perfix:{
		enabled: true
	},
	auth: {
		perfix: '/auth-service'
	},
	user: {
		perfix: '/user-service'
	},
	blog: {
		perfix: '/blog-service'
	},
	file: {
		perfix: '/file-service'
	}
}

export default { cloudriveConfig, apiConfig }