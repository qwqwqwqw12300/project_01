<!--
* @Author: zhanch
* @Date: 2022-12-27 15:53:29
* @FilePath: /pages/index/index.vue
* @Description: 应用首页
-->

<template>	
	<app-body>
		<view class="ui-scan active">
			<u-icon name="scan" color="#fff" size="28"></u-icon>
		</view>
		<text class="wd-logo">
			新大陆米家
		</text>
		<view class="ui-group">
			<view class="ui-title">
				<view>
					<text>我的家庭1</text>
					<text>共两个设备</text>
					<text>在线两个设备</text>
				</view>
				<u-text prefixIcon="share-square" size="28rpx" :align="'right'" :block="false" color="#fff" :iconStyle="{
					fontSize: '44rpx',
					color: '#fff'
				}" text="分享"></u-text>
			</view>
			<view class="ui-device">
				<view class="ui-list">
					<view class="ui-list-box active">
						<image src="../../static/images/device.png"></image>
						<text>xx设备名称</text>
						<text>位置</text>
						<view class="ui-list-static">
							<u-icon name="wifi" color="#0dab1c" size="28"></u-icon>
						</view>
						<u-badge :offset="[-9, 0]" :value="9" absolute></u-badge>
					</view>
				</view>
				<view class="ui-list">
					<view class="ui-list-box active">
						<image src="../../static/images/device.png"></image>
						<text>xx设备名称</text>
						<text>位置</text>
						<view class="ui-list-static">
						<u-icon name="wifi-off" color="#ff4800" size="28"></u-icon>
						</view>
					</view>
				</view>
				<view class="ui-list">
					<view class="ui-list-box active">
						<image src="../../static/images/device.png"></image>
						<text>xx设备名称</text>
						<text>位置</text>
						<view class="ui-list-static">
						<u-icon name="wifi-off" color="#ff4800" size="28"></u-icon>
						</view>
					</view>
				</view>
			</view>
			
		</view>
		
		<view class="ui-group">
			<view class="ui-title">
				<view>
					<text>我的家庭1</text>
					<text>共两个设备</text>
					<text>在线两个设备</text>
				</view>
				<u-text prefixIcon="share-square" size="28rpx" :align="'right'" :block="false" color="#fff" :iconStyle="{
					fontSize: '44rpx',
					color: '#fff'
				}" text="分享"></u-text>
			</view>
			<view class="ui-device">
				<view class="ui-list">
					<view class="ui-list-box active">
						<image src="../../static/images/device.png"></image>
						<text>xx设备名称</text>
						<text>位置</text>
						<view class="ui-list-static">
							<u-icon name="wifi" color="#0dab1c" size="28"></u-icon>
						</view>
						<u-badge :offset="[-9, 0]" :value="9" absolute></u-badge>
					</view>
				</view>
				<view class="ui-list">
					<view class="ui-list-box active">
						<image src="../../static/images/device.png"></image>
						<text>xx设备名称</text>
						<text>位置</text>
						<view class="ui-list-static">
						<u-icon name="wifi-off" color="#ff4800" size="28"></u-icon>
						</view>
					</view>
				</view>
				<view class="ui-list">
					<view class="ui-list-box active">
						<image src="../../static/images/device.png"></image>
						<text>xx设备名称</text>
						<text>位置</text>
						<view class="ui-list-static">
						<u-icon name="wifi-off" color="#ff4800" size="28"></u-icon>
						</view>
					</view>
				</view>
			</view>
			
		</view>
	</app-body>

</template>
<script>
	export default {
		data() {
			return {
			}
		},
		onLoad() {},
		methods: {
			
		}
	}
</script>

<style lang="scss">
	.ui-scan {
		position: absolute;
		right: 24rpx;
		top: 80rpx;
		
		
	}
	.ui-group {
		margin-top: 56rpx;
		width: 100%;
		display: inline-flex;
		flex-direction: column;
		align-items: center;
		.ui-title {
			box-sizing: border-box;
			padding: 0 32rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			height: 80rpx;
			width: calc(100% - 74rpx);
			border-radius: 40px;
			overflow: hidden;
			color: #fff;
			background-color: rgba(0, 0, 0, 0.4);
			font-size: 24rpx;
			text {
				margin-right: 10rpx;
			}
		}
		.ui-device {
			display: inline-block;
			width: calc(100% - 128rpx);
			margin-top: 74rpx;
			&
			.ui-list {
				margin-bottom: 50rpx;
				float: left;
				height: 258rpx;
				width: 50%;
				text-align: center;
				.ui-list-box {
					position: relative;
					height: 100%;
					width: 258rpx;
					display: inline-flex;
					align-items: center;
					justify-content: center;
					flex-direction: column;
					border-radius: 10px;
					filter: drop-shadow(7.824px 10.382px 8px rgba(7,5,5,0.08));
					background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);
					image {
						height: 78rpx;
						width: 80rpx;
					}
					text {
						display: inline-block;
						margin-top: 15rpx;
						font-size: 22rpx;
						&:nth-child(2) {
							font-size: 30rpx;
						}
					}
					.ui-list-static {
						position: absolute;
						bottom: 10rpx;
						right: 12rpx;
					}
				}
			
			}
		}
	}
</style>
