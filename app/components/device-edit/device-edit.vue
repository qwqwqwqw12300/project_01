<!--
* @Author: zhanch
* @Date: 2023-01-31 15:18:07
* @FilePath: /components/device-edit/device-edit.vue
* @Description: 设备信息修改
-->

<template>
	<u-popup :closeable="true" :round="10" :show="isEditShow" mode="center" @close="close">
		<view class="wd-add ui-change">
			<u-text prefixIcon="edit-pen" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
				size="30rpx" text="修改名称"></u-text>
			<view class="ui-add-box">
				<view>
					<u-text size="28rpx" prefixIcon="home" iconStyle="font-size: 36rpx" text="设备名称"></u-text>
					<u-input placeholder="请输入设备名称" :maxlength="6" v-model="editFrom.deviceName" border="bottom"
						clearable>
					</u-input>
				</view>
				<view>
					<u-text size="28rpx" prefixIcon="setting" iconStyle="font-size: 36rpx" text="房间高度"></u-text>
					<view class="ui-slider">
						<u-slider v-model="editFrom.roomHeight" activeColor="#eeaa3d" blockColor="#eeaa3d"
							inactiveColor="#c0c4cc" />
						<text>{{$u.priceFormat(editFrom.roomHeight/10, 2)}}米</text>
					</view>
				</view>
				<view>
					<u-text size="28rpx" prefixIcon="setting" iconStyle="font-size: 36rpx" text="房间长度"></u-text>
					<view class="ui-slider">
						<u-slider v-model="editFrom.roomLength" activeColor="#eeaa3d" blockColor="#eeaa3d"
							inactiveColor="#c0c4cc" />
						<text>{{$u.priceFormat(editFrom.roomLength/10, 2)}}米</text>
					</view>
				</view>
				<view>
					<u-text size="28rpx" prefixIcon="setting" iconStyle="font-size: 36rpx" text="房间左长度">
					</u-text>
					<view class="ui-slider">
						<u-slider v-model="editFrom.roomRight" activeColor="#eeaa3d" blockColor="#eeaa3d"
							inactiveColor="#c0c4cc" />
						<text>{{$u.priceFormat(editFrom.roomRight/10, 2)}}米</text>
					</view>
				</view>
				<view>
					<u-text size="28rpx" prefixIcon="setting" iconStyle="font-size: 36rpx" text="房间右长度">
					</u-text>
					<view class="ui-slider">
						<u-slider v-model="editFrom.roomLeft" activeColor="#eeaa3d" blockColor="#eeaa3d"
							inactiveColor="#c0c4cc" />
						<text>{{$u.priceFormat(editFrom.roomLeft/10, 2)}}米</text>
					</view>
				</view>
			</view>
			<view class="wd-btn-gloup"><button @click="editSubmit">确定</button></view>
		</view>
	</u-popup>
</template>

<script>
	const INIT_FORM = {
		deviceId: '',
		deviceName: '',
		deviceType: '',
		deviceNo: '',
		roomLeft: 0,
		roomHeight: 0,
		roomRight: 0,
		roomLength: 0
	};
	export default {
		data() {
			return {
				/**是否开启**/
				isEditShow: false,
				editFrom: {
					...INIT_FORM
				},
			}
		},
		methods: {
			/**
			 * 修改完成
			 */
			editSubmit() {
				this.$emit('comfirm', this.editFrom);
			},

			/**
			 * 开启
			 */
			open(form) {
				this.editFrom = {
					...form
				};
				this.isEditShow = true;
			},

			close() {
				this.isEditShow = false;
				this.editFrom = {
					...INIT_FORM
				};
			}

		}
	}
</script>

<style lang="scss">
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
