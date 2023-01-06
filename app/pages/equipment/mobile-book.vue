<!--
* @Author: zhanch
* @Date: 2023-01-06 10:27:32
* @FilePath: /pages/equipment/mobile-book.vue
* @Description: 手表通讯录
-->

<template>
	<app-body :hideTitle="true">
		<u-navbar leftText="返回" :safeAreaInsetTop="false" :autoBack="true" bgColor="transparent" leftIconColor="#fff"
			:fixed="false">
			<view class="ui-nav-slot active" slot="right" @click="open">
				<u-text prefixIcon="plus" color="#fff" :iconStyle="{
					fontSize: '40rpx',
					color: '#fff'
				}" text="新增"></u-text>
			</view>
		</u-navbar>
		<view id="mobileBook">
			<view class="ui-books">
				<view class="ui-item" v-for="item of 3" :key="item">
					<view class="ui-icon">{{item || 'S'}}</view>
					<view class="ui-msg">
						<text>SOS</text>
						<text>13599999999</text>
					</view>
					<button @click="open">设置</button>
				</view>
			</view>
		</view>
		<u-popup :closeable="true" :round="10" :show="isEditShow" mode="center" @close="close">
			<view class="wd-add">
				<view>
					<u-text size="28rpx" prefixIcon="man-add-fill" iconStyle="font-size: 45rpx" text="姓名"></u-text>
					<u--input placeholder="请输入姓名" border="bottom" clearable></u--input>
				</view>
				<view>
					<u-text size="28rpx" prefixIcon="phone-fill" iconStyle="font-size: 36rpx" text="手机号码"></u-text>
					<u--input placeholder="请输入手机号码" border="bottom" clearable></u--input>
				</view>
				<view class="ui-check">
					<u-radio-group>
						<u-radio shape="square" labelSize="24rpx" size="30rpx" activeColor="#fdbc2b"
							labelColor="#fdbc2b" label="设为紧急联系号码">
						</u-radio>
					</u-radio-group>
				</view>
				<view class="wd-btn-gloup">
					<button @click="onSubmit">提交</button>
					<button @click="onDelete">删除</button>
				</view>
			</view>
		</u-popup>
	</app-body>


</template>

<script>
	export default {
		data() {
			return {
				isEditShow: false,
			};
		},
		methods: {
			/**
			 * 关闭浮层
			 */
			close() {
				this.isEditShow = false;
			},
			/**
			 * 打开浮层
			 */
			open() {
				this.isEditShow = true;
			}
		}
	}
</script>

<style lang="scss">
	.ui-books {
		margin-top: 90rpx;
		box-sizing: border-box;
		width: 100%;
		padding: 0 50rpx;

		.ui-item {
			box-sizing: border-box;
			display: flex;
			padding: 0 21rpx;
			align-items: center;
			justify-content: space-between;
			margin-bottom: 10rpx;
			width: 640rpx;
			height: 113rpx;
			border-radius: 10rpx;
			filter: drop-shadow(7.824rpx 10.382rpx 8rpx rgba(7, 5, 5, 0.08));
			background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);

			.ui-msg {
				flex: 2;
				display: flex;
				flex-direction: column;
				padding-left: 30rpx;

				text {
					&:nth-child(1) {
						font-size: 36rpx;
						color: #666666;
					}

					&:nth-child(2) {
						font-size: 24rpx;
						color: #c3c3c3;
					}
				}
			}

			button {
				display: inline-block;
				margin: 0;
				width: 100rpx;
				height: 41rpx;
				padding: 0 10rpx;
				font-size: 24rpx;
				color: #ffffff;
				line-height: 41rpx;
				border-radius: 20rpx;
				background: #f39800;
			}

			.ui-icon {
				text-align: center;
				line-height: 72rpx;
				width: 72rpx;
				height: 72rpx;
				color: #fff;
				border-radius: 10rpx;
			}

			&:nth-child(1) {
				.ui-msg>text {
					&:nth-child(1) {
						color: #ff0101 !important;
					}
				}
			}

			@each $idx,
			$bg in (1: #e5004f, 2:#ec6941, 3:#448aca) {
				&:nth-child(#{$idx}) {
					.ui-icon {
						background-color: $bg;
					}
				}
			}
		}
	}

	.wd-add {
		width: 582rpx;
		height: 606rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 53rpx 31rpx;
		border: 1px solid #ffcb3d;

		&>view {
			margin-top: 52rpx;
			padding: 10rpx 20rpx;
			border-bottom: 1px solid #e4e4e4;

			&:nth-last-of-type(1),
			&:nth-last-of-type(2) {
				padding-left: 0rpx;
				border-bottom: none;
			}
		}

		.ui-check {
			margin-left: 25rpx;
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
	}
</style>
