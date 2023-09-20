<!--
* @Author: zhanch
* @Date: 2022-12-13 13:54:19
* @FilePath: /App.vue
* @Description: 应用入口
-->

// 包名 uni.UNI9B9BD07
// Android 应用签名: 22:5A:89:92:79:D0:68:41:45:0D:FE:9A:DE:94:73:81:5C:2A:8C:11

<script>
	// import {
	// 	push
	// // } from '@/common/sdk/push.js';
	// import {
	// 	isApp
	// } from '@/common/utils/util.js';

	export default {
		// onLaunch: function() {
		// 	if (isApp()) {
		// 		push.init(); // 推送服务初始化
		// 	}
		// },
		onShow: function() {},
		onHide: function() {},
		mounted() {
			// #ifdef APP-PLUS
			plus.runtime.setBadgeNumber(0);
			// #endif
		}
	}
</script>

<style>
	/*每个页面公共css */
</style>