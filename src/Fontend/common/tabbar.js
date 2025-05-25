export const tabbarData = [{
		iconPath: "/static/tabbar/start.png",
		selectedIconPath: "/static/tabbar/start_selected.png",
		text: '广场',
		pagePath: "/pages/index/index"
	},
	{
		iconPath: "/static/tabbar/file.png",
		selectedIconPath: "/static/tabbar/file_selected.png",
		text: '文件',
		pagePath: "/pages/file/file"
	},
	{
		iconPath: "/static/tabbar/user.png",
		selectedIconPath: "/static/tabbar/user_selected.png",
		text: '我的',
		pagePath: "/pages/my/my"
	},
]

export const tabbarConfig = {
	// #ifdef APP
	style: {
		alignItems: 'center',
		position: 'fixed',
		bottom: '0',
		left: '0',
		width: '100%',
		zIndex: '998'
	},
	props: {
		bgColor: '#fff'
	},
	containerStyle:{
		with: '100%',
		height: '100rpx'
	}
	// #endif
	
	// #ifdef WEB
	style: {
		alignItems: 'center',
		position: 'fixed',
		top: '0',
		left: '50%',
		transform: 'translateX(-50%)',
		width: '35%',
		zIndex: '998'
	},
	props: {
		bgColor: '#F7F9FE'
	},
	containerStyle: {
		alignItems: 'center',
		position: 'fixed',
		top: '0',
		width: '100%',
		height: '100rpx',
		backgroundColor: '#F7F9FE',
		border: 'red soild',
		zIndex: '998'
	}
	// #endif
}