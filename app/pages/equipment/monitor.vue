<!--
* @Author: zhanghaowei
* @Date: 2023年2月28日14:43:51
* @FilePath: 
* @Description: 远程监听
-->

<template>
	<app-body>
		<app-logo color="#353535" text="远程监听" ></app-logo>
		<CardTitle :title="'远程监听'" :content="'您可以实时获取设备环境声音'" :backGroundImg="'/static/images/long-range-monitor.png'"></CardTitle>
		<view class="ui-btn"><button class="default" @click="handleStart">开始监听</button></view>
		
	</app-body>
</template>

<script>
	import {
		PostRemoteMonitor
	} from '@/common/http/api.js';
	import CardTitle from '@/components/card-title/card-title.vue';
	import {
		mapState,
	} from 'vuex';
	export default {
		components:{
			CardTitle
		},
		computed: {
			...mapState({
				/**所有家庭列表**/
				deviceInfo: state => state.deviceInfo
			}),
		
		},
		data() {
			return {
				flag: '0', //0关闭，1开启
			};
		},
		mounted() {
			
		},
		methods: {
			handleStart(){
				PostRemoteMonitor({
					deviceId:this.deviceInfo.deviceId, 
					requestCall:"1"
				}).then(res=>{
					console.log(res,'res')
					uni.$u.toast(res.msg)
				})
			}
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
