/*
 * @Author: zhanch
 * @Date: 2022-12-13 10:33:13
 * @FilePath: /config/env.js
 * @Description: 环境变量配置
 */
export const env = {
	/**应用模式 dev-开发模式 prod-生产模式**/
	mode: 'prod',
	/**服务器地址**/
	// http://www.agetech.cc:7766
	basePath: 'http://www.agetech.cc',
	// basePath: 'http://192.168.132.204:7766',
	/**日志地址**/
	log: '/api/logging',
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
		base: 'https://api.walabot-home.cn',
		email: 'elinker@elinker.cc',
		password: '1234567890',
		url: 'https://api.walabot-home.cn/authenticate',
		par: 'https://api.walabot-home.cn/pairing',
		ntpUrl: "ntp.aliyun.com",
	},
	// 极光sdk
	pushKey: '3c3f6b5746d37ea030455d83'
}
