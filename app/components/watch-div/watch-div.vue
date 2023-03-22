<template>
	<view class="wd-content">
		<view class="wd-content-left">{{text}}</view>
		<view class="wd-content-right">{{content}}</view>
	</view>
</template>

<script>
	export default {
		props: {
			text: {
				default: ''
			},
			content:{
				default: ''
			}
		},
		data() {
			return {
				
			}
		},
		methods: {
			
		}
	}
</script>

<style lang="scss">
	.wd-content{
		width: 90%;
		height: 128rpx;
		margin: 0 auto;
		background-color: #FFFFFF;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		.wd-content-left{
			margin-left:20rpx;
			font-size: 34rpx;
			color: #353535;
			letter-spacing: 0;
			font-weight: 400;
		}
		.wd-content-right{
			margin-right:20rpx;
			font-size: 56rpx;
			color: #353535;
			letter-spacing: 0;
			font-weight: 700;
		}
	}
</style>