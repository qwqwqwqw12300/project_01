<!--
* @Author: zhanch
* @Date: 2022-12-25 23:25:20
* @FilePath: /pages/customer-service/customer-service.vue
* @Description: 客服
-->

<template>
	<view>
		<view :crispWebsoteId="crispWebsoteId" :change:crispWebsoteId="service.loadData"></view>
		<u-loading-page loading-text="正在为您连接客服,请稍后..." :loading="true"></u-loading-page>
	</view>
</template>

<script>
	import {
		env
	} from "@/config/env.js";
	export default {
		mounted() {
			this.crispWebsoteId = env.crispWebsoteId;
		},
		data() {
			return {
				crispWebsoteId: "",
			};
		},
	};
</script>

<script module="service" lang="renderjs">
	import {
		Crisp
	} from "crisp-sdk-web";
	export default {
		mounted() {

		},
		methods: {
			loadData(crispWebsoteId) {
				if (!crispWebsoteId) return
				Crisp.configure(crispWebsoteId, {
					lockMaximized: true // 锁定页面不关闭
				});
				Crisp.user.setNickname("test1"); // 设置昵称
				Crisp.session.setData({ // 其他携带的数据
					user_id: "123456"
				});
				$crisp.push(["safe", true])
				Crisp.chat.open();
			}
		}
	}
</script>

<style lang="scss"></style>