<!--
* @Author: zhanch
* @Date: 2022-12-25 23:25:20
* @FilePath: /pages/customer-service/customer-service.vue
* @Description: 客服
-->

<template>
	<u-loading-page loading-text="正在为您连接客服,请稍后..." :loading="true"></u-loading-page>
</template>

<script>
export default {
	data() {
		return {};
	},
	mounted() {
	}
};
</script>

<script module="service" lang="renderjs">
import { Crisp } from "crisp-sdk-web";
import { env } from "@/config/env.js";
export default {
	mounted() {
		Crisp.configure(env.crispWebsoteId, {
			  lockMaximized: true
		});
		Crisp.user.setNickname("test1");
		Crisp.session.setData({
		  user_id : "123456"
		});
		Crisp.chat.open();
	},
	methods: {

	}
}
</script>

<style lang="scss"></style>
