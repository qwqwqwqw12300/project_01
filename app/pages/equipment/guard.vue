<!--
* @Author: zhanghaowei
* @Date: 2023年3月1日14:43:51
* @FilePath: 
* @Description: 位置守护
-->

<template>
	<app-body>
		<app-logo color="#353535" text="位置守护" ></app-logo>
		<CardTitle :title="'到达提醒'" :content="'迟到APP有报警'" :backGroundImg="'/static/images/arrive-guard.png'"></CardTitle>
		<view style="margin-top: 50rpx;">
			<view class="ui-cell" v-for="(item,index) in list" :key="index" @click="toJump('/pages/equipment/info-guard')">
				<view class="ui-cell-content">
					<view class="ui-cell-style">
						<view class="ui-cell-title">{{item.jobName}}</view>
						<view><u-icon name="arrow-right"></u-icon></view>
					</view>
					<view class="ui-cell-style">
						<view class="ui-cell-font">
							<view>{{item.firstDate}} 至 </view>
							<view>{{item.lastDate}}</view>
						</view>
						<u-switch @change="handleSwitch" v-model="flag" activeValue="1" inactiveValue="0"
							activeColor="#FEAE43" inactiveColor="rgb(138, 138, 138)" size="20"> >
						</u-switch>
					</view>
				</view>
			</view>
		</view>
		<view class="ui-btn"><button class="default"  @click="toJump('/pages/equipment/new-guard')">新增守护位置</button></view>
	</app-body>
</template>

<script>
	import CardTitle from '@/components/card-title/card-title.vue';
	import {
		GetLocationGuardList
	} from '@/common/http/api';
	export default {
		components:{
			CardTitle
		},
		data() {
			return {
				flag: '0', //0关闭，1开启
				list:[
					{title:'舞蹈课'},
					{title:'公交站'}
				]
			};
		},
		mounted() {
			
		},
		methods: {
			initData(){
				GetLocationGuardList({
					deviceNo:'867977060000248'
				}).then(res=>{
					console.log(res,'res')
					this.list = res.data
				})
			},
			handleSwitch() {
				
			},
			toJump(url){
				uni.navigateTo({
					url:url
				})
			}
		},
		onShow() {
			this.initData()
		}
	};
</script>

<style lang="scss">
	.ui-cell {
		width: 100%;
		height: 240rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: #fff;
		.ui-cell-content{
			width: 91.4%;
			height: 72%;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			.ui-cell-style{
				display: flex;
				align-items: center;
				justify-content: space-between;
				.ui-cell-font{
					display: flex;
					align-items: flex-start;
					flex-direction: column;
					font-size: 26rpx;
					color: #888888;
					letter-spacing: 0;
					line-height: 40rpx;
					font-weight: 400;
				}
				.ui-cell-title{
					font-size: 34rpx;
					color: #353535;
					line-height: 34rpx;
					font-weight: 400;
				}
			}
		}
	}
	.ui-btn {
		width:100%;
		position: fixed;
		bottom: 0;
		left: 0;
		button {
			border-radius: 0rpx !important;
		}
	}
</style>
