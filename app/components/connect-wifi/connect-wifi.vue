<!--
* @Author: 连接wifi
* @Date: 2023-01-28 16:38:01
* @FilePath: /components/connect-wifi/connect-wifi.vue
* @Description: In User Settings Edit
-->

<template>
	<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close">
		<view class="wd-add ui-change">
			<u-text prefixIcon="setting" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
				size="30rpx" text="选择网络"></u-text>
			<view class="ui-add-box">
				<!-- <view class="ui-list">
					<view v-for="item in list" :key="item.key" @tap="handleSelect(item)" class="ui-list-item"
						:class="{'active': item.active}">
						<u-icon name="wifi" size="40rpx"></u-icon>
						<text class="text">设备1 device-01</text>
						<image v-show="item.active" class="tick" src="../../static/images/tick.png" />
					</view>
				</view> -->
				<view class="ui-pwd">
					<u-text size="28rpx" prefixIcon="lock" iconStyle="font-size: 36rpx" text="密码">
					</u-text>
					<u--input v-model="pwd" type="password" placeholder="请输入wifi密码" border="bottom" clearable>
					</u--input>
				</view>
			</view>
			<view class="wd-btn-gloup">
				<button @click="add">返回</button>
				<button class="wd-sms" @click="add">确定</button>
			</view>
		</view>
	</u-popup>
</template>

<script>
	export default {
		props: {
			list: Array
		},
		data() {
			return {
				show: true,
				pwd: ''
			};
		},
		methods: {
			close() {
				this.show = false;
			},

			open() {
				this.show = true;
			},

			/**
			 * 选择wifi
			 */
			handleSelect() {

			}
		}
	}
</script>

<style lang="scss">
	.wd-add {
		width: 582rpx;
		min-height: 500rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 53rpx 31rpx;

		&.ui-change {
			// .ui-add-box {
			// 	border-bottom: 1px solid #e4e4e4;
			// }
		}

		&>view {
			margin-top: 52rpx;

			&.ui-add-box {
				padding: 10rpx 20rpx;

				&>* {
					margin-top: 30rpx;
				}
			}
		}

		.wd-btn-gloup {
			text-align: center;
			margin-top: 70rpx;

			button {
				width: 237rpx;
				height: 71rpx;
				font-size: 28rpx;
				color: #ffffff;
			}
		}

		.ui-list {
			margin-top: 20rpx;
			padding: 20rpx 60rpx 20rpx 20rpx;
			display: flex;
			flex-direction: column;
			width: 100%;

			.ui-list-item {
				// margin-left: 80rpx;
				padding: 18rpx 34rpx;
				border-radius: 40rpx;
				display: flex;
				align-items: center;
				justify-content: space-between;

				.text {
					flex: 1;
					font-size: 36rpx;
					margin-left: 20rpx;
					color: #000000;
					font-weight: 550;
				}

				.device {
					width: 50rpx;
					height: 50rpx;
				}

				.tick {
					width: 35rpx;
					height: 30rpx;
				}
			}
		}
	}

	.ui-pwd {
		margin-bottom: 80rpx;
	}
</style>
