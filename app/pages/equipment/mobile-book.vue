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
			<view class="ui-nav-slot active" slot="right" @click="handleAdd">
				<u-text prefixIcon="plus" color="#fff" :iconStyle="{
					fontSize: '40rpx',
					color: '#fff'
				}" text="新增"></u-text>
			</view>
		</u-navbar>
		<view id="mobileBook">
			<view class="ui-books">
				<view class="ui-item" v-for="(item,index) in list" :key="index">
					<view class="ui-icon">{{index+1 || 'S'}}</view>
					<view class="ui-msg">
						<text>{{ item.phoneName }}</text>
						<text>{{item.phone}}</text>
					</view>
					<button @click="handleSet(item,index)">设置</button>
				</view>
			</view>
		</view>
		<u-popup :closeable="true" :round="10" :show="isEditShow" mode="center" @close="close">
			<view class="wd-add">
				<view>
					<u-text size="28rpx" prefixIcon="man-add-fill" iconStyle="font-size: 45rpx" text="姓名"></u-text>
					<u--input v-model="form.phoneName" placeholder="请输入姓名" border="bottom" clearable></u--input>
				</view>
				<view>
					<u-text size="28rpx" prefixIcon="phone-fill" iconStyle="font-size: 36rpx" text="手机号码"></u-text>
					<u--input v-model="form.phone" placeholder="请输入手机号码" border="bottom" clearable></u--input>
				</view>
				<view class="ui-check">
					<u-radio-group v-model="form.type" placement="column">
						<u-radio v-for=" (name,value) in typeDict" :key="value" :name="value" shape="circle"
							labelSize="24rpx" size="30rpx" activeColor="#fdbc2b" labelColor="#fdbc2b" :label="name">
						</u-radio>
					</u-radio-group>
				</view>
				<view class="wd-btn-gloup">
					<button @click="onSubmit">提交</button>
					<!-- 		<button @click="onDelete">删除</button> -->
				</view>
			</view>
		</u-popup>
		<add-mobile @update="handleInit" ref="addmobile" />
	</app-body>


</template>

<script>
	import {
		PostDevicePhoneList,
		// PostAddDevicePhone,
		PostSetDevicePhone,
		PostSetSosDevicePhone,
	} from '@/common/http/api.js';
	import { mapState } from 'vuex';
	export default {
		data() {
			return {
				isEditShow: false,
				index: 0,
				list: [],
				form: {
					phoneName: '',
					phone: '',
					type: '0',
				},
				typeDict: {
					0: '设置为SOS',
					1: '设置为按钮1',
					2: '设置为按钮2',
					3: '设置为按钮3',
					P: '暂保存、不设置',
				}
			};
		},
		comments: {
			...mapState({
				deviceId: state => state.deviceInfo.deviceId
			})
		},
		methods: {
			/**
			 * 关闭浮层
			 */
			close() {
				this.form = {}
				this.isEditShow = false;
			},
			/**
			 * 打开浮层
			 */
			handleSet(item, index) {
				this.form = {
					...item
				}
				this.index = index
				this.isEditShow = true;
			},
			handleAdd() {
				this.$refs.addmobile.open({
					deviceId: this.deviceId,
					list: this.list,
				});
			},
			handleInit() {
				PostDevicePhoneList({
					deviceId: this.deviceId,
				}).then(res => {
					this.list = res.rows
				})
			},

			onSubmit() {
				const {
					phone,
					phoneName,
					type,
				} = this.form
				if (!phoneName) {
					uni.$u.toast('请填写名称')
				}
				if (!phone) {
					return uni.$u.toast('请填写手机号码')
				}
				const InterList = type === 0 ? PostSetSosDevicePhone : PostSetDevicePhone
				const listArr = uni.$u.deepClone(this.list)
				listArr[this.index] = this.form
				InterList({
					deviceId: this.deviceId,
					list: listArr
				}).then(res => {
					uni.$u.toast(res.msg)
					this.close()
					setTimeout(() => {
						this.handleInit()
					}, 500);
				})
			}

		},
		mounted() {
			this.handleInit()
		}
	}
</script>

<style lang="scss">
	::v-deep {
		.u-radio {
			margin-bottom: 16rpx;
		}
	}

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
		height: 800rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 53rpx 31rpx;
		border: 1px solid #ffcb3d;

		&>view {
			margin-top: 30rpx;
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
