<template>
	<view class="ui-title">
		<view class="ui-img" :style="{backgroundImage:'url('+ backGroundImg +')'}">
			<view class="ui-img-content">
				<view class="ui-img-title">{{title}}</view>
				<view class="ui-img-text">{{content}}</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props:{
			title:{
				type:String,
				default:()=>''
			},
			content:{
				type:String,
				default:()=>''
			},
			backGroundImg:{
				type:String,
				default:()=>''
			},
		},
		data() {
			return {
				
			};
		},
		mounted() {
			
		},
		methods: {
			
		}
	};
</script>

<style lang="less">
	.ui-title{
		margin-top: 54rpx;
		.ui-img{
			margin-top: 54rpx;
			width: 686rpx;
			height: 206rpx;
			margin: 0 auto;
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
				margin-top: 20rpx;
				color: #fff;
				font-weight: 400;
				display: flex;
				flex-wrap: wrap;
				line-height: 45rpx;
			}
		}
	}
</style>