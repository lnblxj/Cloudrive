import {cloudriveConfig} from '@/config/global_config.ts'
import { useUserStore } from '@/stores/modules/user.js'

export default class Request {
    http(param) {
        let url = param.url;
        let method = param.method;
        let header = param.header || {};
        let data = Object.assign(param.data || {});
        let hideLoading = param.hideLoading || false;
        let requestUrl = cloudriveConfig.BASE_URL.url + url;
        
        // 先设置默认的content-type
        let defaultHeader = {
            'content-type': "application/json"
        };
        
        // 合并自定义header
        header = Object.assign({}, defaultHeader, header);
        
        // 获取用户token并添加到请求头
        const userStore = useUserStore();
        if (userStore.token) {
            header['Authorization'] = `Bearer ${userStore.token}`;
        }
		if (!hideLoading) {
		    uni.showLoading()
		}
		return new Promise((resolve, reject) => {
		    uni.request({
		        url: requestUrl,
		        data: data,
		        method: method,
		        header: header,
		        responseType: header.responseType || 'text',
		        success: (res) => {
		            if (!hideLoading) {
		                uni.hideLoading();
		            }
		            // 如果是二进制数据，直接返回
		            if (header.responseType === 'arraybuffer') {
		                resolve(res.data);
		                return true;
		            }
		            // 判断请求是否成功
		            if(res.data.isSuccess){
		                resolve(res.data)
		                return true
		            } else {
		                // 处理通用错误
		                switch(res.data.code) {
		                    case 504:
		                        // token失效，跳转到登录页
		                        uni.navigateTo({
		                            url: '/pages/login/login'
		                        })
		                        return false
		                    case 517:
		                        // 二维码未被扫描，静默处理
		                        return false
		                    default:
		                        // 其他业务错误交给调用者处理
		                        resolve(res.data)
		                        return true
		                }
		            }
		        },
		        fail: (err) => {
		            if (!hideLoading) {
		                uni.hideLoading();
		            }
		            return false;
		        },
		    })
		})
    }
}
