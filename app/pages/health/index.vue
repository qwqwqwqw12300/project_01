<!--
* @Author: zhanghaowei
* @Date: 2023年2月27日15:59:21
* @FilePath: /pages/health/index
* @Description: 健康首页
-->

<template>
	<view>
		<image src="@/static/images/health-img.jpg" mode="widthFix" class="ui-img"></image>
	</view>
</template>

<script>
	export default {
		data() {
			return {};
		},
		mounted() {

		},
		methods: {

		}
	};
</script>

<style lang="scss">
	.ui-img {
		width: 100%;
		height: calc(100vh - var(--window-bottom) - var(--status-bar-height));

	}
</style>
