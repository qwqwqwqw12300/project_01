<!--
* @Author: zhanch
* @Date: 2022-12-30 17:14:05
* @FilePath: /pages/myself/device-manage.vue
* @Description: 设备管理
-->

<template>
	<view id="deviceManage">
		<app-body>
			<!-- <view class="ui-logo">设备管理</view> -->
			<app-logo text="设备管理"></app-logo>
			<view class="ui-menu">
				<u-grid col="2">
					<u-grid-item v-for="(item, index) in list" :key="index">
						<view class="ui-menu-item" :border="false">
							<u-icon :customStyle="{ paddingTop: 20 + 'rpx' }" name="/static/images/myself/device.png"
								size="80rpx"></u-icon>
							<!-- <text class="grid-text">{{ baseListItem.title }}</text> -->
							<u-text class="grid-text" @click="edit" suffixIcon="edit-pen-fill"
								iconStyle="font-size: 36rpx" align="center" :text="item.familyName"></u-text>
							<text class="grid-text">{{ item.roomName }}</text>
							<view class="ui-wifi active">
								<u-icon :customStyle="{ paddingTop: 20 + 'rpx' }" name="wifi" size="40rpx"
									color="#0dab1c"></u-icon>
							</view>
							<u-icon @click="onDelete" class="ui-close active" name="close-circle-fill" size="40rpx">
							</u-icon>
						</view>
						<view class="ui-btn"><button class="wd-sms" @click="unbinding">解绑</button></view>
					</u-grid-item>
				</u-grid>
			</view>
			<view class="ui-add-btn"><button @click="add">创建设备</button></view>
			<!-- 绑定房间 -->
			<u-popup :closeable="true" :round="10" :show="bindRoomShow" mode="center" @close="close">
				<view class="wd-add">
					<u-text prefixIcon="plus-circle" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }"
						color="#ea942f" size="30rpx" text="绑定设备"></u-text>
					<view class="ui-add-box">
						<u-text size="28rpx" prefixIcon="home" iconStyle="font-size: 36rpx" text="选择家庭"></u-text>
						<view class="ui-select">
							<uni-data-select v-model="value" :clear="false" :localdata="range"></uni-data-select>
						</view>
					</view>
					<view class="ui-add-box">
						<u-text size="28rpx" prefixIcon="home-fill" iconStyle="font-size: 36rpx" text="选择房间"></u-text>
						<view class="ui-select">
							<uni-data-select v-model="value" :clear="false" :localdata="range"></uni-data-select>
						</view>
					</view>
					<view class="wd-btn-gloup"><button @click="close">确定</button></view>
				</view>
			</u-popup>
			<!-- /绑定房间 -->
			<!-- 修改名称 -->
			<u-popup :closeable="true" :round="10" :show="isEditShow" mode="center" @close="eidtClose">
				<view class="wd-add ui-change">
					<u-text prefixIcon="edit-pen" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
						size="30rpx" text="修改名称"></u-text>
					<view class="ui-add-box">
						<u-text size="28rpx" prefixIcon="home" iconStyle="font-size: 36rpx" text="设备名称"></u-text>
						<u--input placeholder="请输入设备名称" border="bottom" clearable></u--input>
					</view>
					<view class="wd-btn-gloup"><button @click="eidtClose">确定</button></view>
				</view>
			</u-popup>
			<!-- /修改名称 -->
		</app-body>
	</view>
</template>

<script>
	import {
		PostDeviceList,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				isEditShow: false,
				/**绑定房间**/
				bindRoomShow: false,
				value: 0,
				range: [{
					value: 0,
					text: '房间一'
				}, {
					value: 1,
					text: '房间二'
				}, {
					value: 2,
					text: '房间三'
				}]
			};
		},
		computed: {
			...mapState({
				/**所有家庭列表**/
				list: state => state.getDeviceList
			}),
		},
		methods: {
			/**
			 * 绑定
			 */
			bind() {
				this.bindRoomShow = true;
			},


			/**
			 * 关闭弹窗
			 */
			close() {
				this.bindRoomShow = false;
			},

			/**
			 * 编辑浮层打开
			 */
			edit() {
				this.isEditShow = true;
			},

			/**
			 * 编辑浮层关闭
			 */
			eidtClose() {
				this.isEditShow = false;
			},

			/**
			 * 删除
			 */
			onDelete() {
				uni.showModal({
					title: '提示',
					content: '是否确认删除设备',
					success: res => {
						if (res.confirm) {
							console.log('用户点击确定');
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},

			/**
			 * 解绑
			 */
			unbinding() {
				uni.showModal({
					title: '提示',
					content: '是否和房间解除绑定',
					success: res => {
						if (res.confirm) {
							console.log('用户点击确定');
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},

			/**
			 * 绑定房间
			 */
			bindRoom() {
				this.bindRoomShow = true;
			},
			handleInit() {
				PostDeviceList({

				}).then(res => {
					this.list = res.rows
				})
			}
		},
		mounted() {
			this.handleInit()
		}
	};
</script>

<style lang="scss">
	.ui-menu {
		margin: 70rpx 0;
		padding: 0 78rpx;
		min-height: 600rpx;

		.ui-menu-item {
			position: relative;
			margin-top: 58rpx;
			padding: 20rpx 0;
			display: flex;
			box-sizing: border-box;
			align-items: center;
			justify-content: center;
			flex-direction: column;
			font-size: 27rpx;
			color: #414141;
			height: 258rpx;
			width: 258rpx;
			border-radius: 10rpx;
			filter: drop-shadow(7.824rpx 10.382rpx 8rpx rgba(7, 5, 5, 0.08));
			background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);
			text-align: center;

			&>* {
				flex: 1;

				&:nth-child(1) {
					flex: 2;
				}
			}

			text {
				display: inline-flex;
				width: 70%;
				height: 40rpx;
				line-height: 60rpx;
				align-items: center;
				justify-content: center;
			}

			.grid-text {
				&:nth-child(3) {
					font-size: 24rpx;
				}
			}

			.ui-wifi {
				position: absolute;
				bottom: 10rpx;
				right: 10rpx;
				z-index: 10;
			}

			.ui-close {
				position: absolute;
				top: -10rpx;
				right: -10rpx;
			}
		}

		.ui-btn {
			margin-top: 10rpx;
			text-align: center;
			width: 100%;

			button {
				height: 60rpx;
				width: 258rpx;
				line-height: 60rpx;
				border-radius: 10px;
				font-size: 30rpx;
			}
		}
	}

	.ui-add-btn {
		text-align: center;

		button {
			width: 276rpx;
			height: 74rpx;
			font-size: 30rpx;
			line-height: 74rpx;
			border-radius: 60rpx;
		}
	}

	.ui-logo {
		height: 69rpx;
		// width: 366rpx;
		font-size: 69rpx;
		color: #fff;
		margin: 20rpx 72rpx;
		display: inline-block;
		font-weight: bold;
	}

	.wd-add {
		width: 582rpx;
		height: 606rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 53rpx 31rpx;

		&.ui-change {
			height: 400rpx;

			.ui-add-box {
				border-bottom: 1px solid #e4e4e4;
			}
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
	}
</style>
