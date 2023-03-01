<!--
* @Author: zhanghaowei
* @Date: 2023年2月28日14:43:51
* @FilePath: 
* @Description: 时段禁用
-->

<template>
	<app-body>
		<app-logo color="#353535" text="时段禁止" ></app-logo>
		<view class="ui-title">
			<view class="ui-img">
				<view class="ui-img-content">
					<view class="ui-img-title">禁用运行中</view>
					<view class="ui-img-text">禁用时间拒绝所有电话和短信，但可查看时间和定位</view>
				</view>
			</view>
		</view>
		<view class="ui-cell">
			<view class="ui-cell-content">
				<view class="ui-cell-style">
					<view class="ui-cell-title">禁用时段</view>
					<view><u-icon name="arrow-right"></u-icon></view>
				</view>
				<view class="ui-cell-style">
					<view class="ui-cell-font">
						<view>2023/02/21 00:00 至 </view>
						<view>2023/02/23 23:59</view>
					</view>
					<u-switch @change="handleSwitch" v-model="flag" activeValue="1" inactiveValue="0"
						activeColor="#FEAE43" inactiveColor="rgb(138, 138, 138)" size="20"> >
					</u-switch>
				</view>
			</view>
		</view>
		<view class="ui-btn"><button class="default" @click="toJump">新增时间段</button></view>
	</app-body>
</template>

<script>
	export default {
		data() {
			return {
				flag: '0', //0关闭，1开启
			};
		},
		mounted() {
			
		},
		methods: {
			handleSwitch() {
				// PostUpdPushMsg({
				// 	flag: this.flag
				// }).then(res => {
				// 	uni.$u.toast(res.msg)
				// })
			},
			toJump(){
				uni.navigateTo({
					url:'/pages/new/new-interval'
				})
			}
		}
	};
</script>

<style lang="scss">
	.ui-title{
		margin-top: 54rpx;
		.ui-img{
			margin-top: 54rpx;
			width: 686rpx;
			height: 206rpx;
			margin: 0 auto;
			background-image: url('@/static/images/prohibit-moving.png');
			background-size: 100% 100%;
			.ui-img-content{
				display: flex;
				align-items: flex-start;
				flex-direction: column;
				padding: 20rpx;
			}
			.ui-img-title{
				font-size: 50rpx;
				font-weight: bold;
				color: #fff;
			}
			.ui-img-text{
				margin-top: 10rpx;
				color: #fff;
				font-weight: 400;
				display: flex;
				flex-wrap: wrap;
				line-height: 45rpx;
			}
		}
	}
	.ui-cell {
		margin-top: 50rpx;
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
		width: 100%;
		position: fixed;
		bottom: 0;
		left: 0;
	
		button {
			border-radius: 0rpx !important;
		}
	}
</style>
