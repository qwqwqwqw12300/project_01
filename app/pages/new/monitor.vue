<!--
* @Author: zhanghaowei
* @Date: 2023年2月28日14:43:51
* @FilePath: 
* @Description: 时段禁用
-->

<template>
	<app-body>
		<app-logo color="#353535" text="远程监听" ></app-logo>
		<CardTitle :title="'远程监听'" :content="'您可以实时获取设备环境声音'" :backGroundImg="'/static/images/long-range-monitor.png'"></CardTitle>
		<view class="ui-btn"><button class="default" >开始监听</button></view>
		
	</app-body>
</template>

<script>
	import CardTitle from '@/components/card-title/card-title.vue';
	export default {
		components:{
			CardTitle
		},
		data() {
			return {
				flag: '0', //0关闭，1开启
			};
		},
		mounted() {
			
		},
		methods: {
			
		}
	};
</script>

<style lang="scss">
	
	.ui-btn {
		width: 686rpx;
		margin: 0 auto;
		margin-top: 104rpx;
		button {
			border-radius: 44rpx !important;
		}
	}
</style>
