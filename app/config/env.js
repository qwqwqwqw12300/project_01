/*
 * @Author: zhanch
 * @Date: 2022-12-13 10:33:13
 * @FilePath: /config/env.js
 * @Description: 环境变量配置
 */
export const env = {
	/**开发模式**/
	mode: 'dev',
	/**服务器地址**/
	// basePath: 'https://67c805463f.zicp.fun',
	// basePath: 'http://218.66.48.228:8003',
	basePath: 'http://192.168.1.109:8081',
	//basePath: 'http://192.168.132.204:8060',
	/**版本更新地址**/
	appUpdateUrl: '',
	/**协议地址**/
	privacy: {},
	/**crisp客服Id**/
	crispWebsoteId: 'efe4eab0-cdef-4d3b-977f-3d862e79dd32',
	/**包名**/
	package: 'uni.UNI9B9BD07',
	/**加密公钥**/
	publicKey: 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDF4xuwZNWhVaQZguqxLBKhc2S1jKiJOzJS0YczxLPTH9l2Fa+zxZ/AzYci95KG9kFbvZEVyuFhR5UTsORc+ONsqTg/zx2+vJ7kI8h2/qtWNV6vH9JmgMVHl5CEpikXhZjOPutNmzcRKfzDbYwp274f6iDUi9GttlYR68qPBij8AQIDAQAB',
	/**设备服务相关**/
	device: {
		email: 'elinker@elinker.cc',
		password: '1234567890',
		url: 'https://api.walabot-home.cn/authenticate'
	},
	// 极光sdk
	pushKey: '3c3f6b5746d37ea030455d83'
}
