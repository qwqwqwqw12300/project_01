<!--
* @Author: 未绑定设备设置
* @Date: 2023-02-10 10:16:28
* @FilePath: /components/unbind-edit/unbind-edit.vue
* @Description: In User Settings Edit
-->


<template>
	<view>
		<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close">
			<view class="wd-add-unbind ui-change">
				<u-text prefixIcon="edit-pen" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
					size="30rpx" text="修改设备"></u-text>
				<view class="ui-add-box">
					<view class="ui-input">
						<u-text size="28rpx" prefixIcon="home" iconStyle="font-size: 36rpx" text="设备名称"></u-text>
						<u-input placeholder="请输入设备名称" :maxlength="6" v-model="editFrom.deviceName" border="bottom"
							clearable>
						</u-input>
					</view>
					<view class="ui-radio">
						<u-text size="28rpx" prefixIcon="map" iconStyle="font-size: 36rpx" text="设备位置"></u-text>
						<u-radio-group v-model="editFrom.location" placement="row">
							<u-radio :customStyle="{margin: '20rpx'}" v-for="item of locationList" :key="item"
								activeColor="#1aa208" :name="item" :label="item"></u-radio>
						</u-radio-group>
					</view>
				</view>
				<view class="wd-btn-group">
					<button class="cancel" @click="close">取消</button>
					<button class="default" @click="editSubmit">确定</button>
				</view>
			</view>
		</u-popup>
	</view>

</template>

<script>
	import {
		PosteditDevice
	} from '../../common/http/api';

	export default {
		data() {
			return {
				/**是否开启 none edit more**/
				show: false,
				editFrom: {
					deviceName: '',
					location: ''
				},
				locationList: [
					'壁挂',
					'顶挂',
				],
			}
		},
		methods: {
			/**
			 * 修改完成
			 */
			editSubmit() {
				if (!this.editFrom.deviceName) return uni.$u.toast('设备名字不能为空');
				PosteditDevice(this.editFrom).then(res => {
					uni.$u.toast('修改成功');
					setTimeout(() => {
						this.$emit('confirm');
						this.close();
					}, 1000);

				})
			},

			/**
			 * 开启
			 */
			open(form) {
				console.log('开启');
				this.editFrom = {
					deviceId: form.deviceId,
					deviceName: form.name,
					location: form.location
				};
				this.show = true;
			},

			close() {
				this.show = false;
			}
		}
	}
</script>

<style lang="scss">
	.wd-add-unbind {
		width: 582rpx;
		min-height: 200;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 53rpx 31rpx;

		.ui-input {
			position: relative;

			&::after {
				bottom: 10rpx;
				left: 20rpx;
				content: '';
				width: 500rpx;
				height: 1rpx;
				background: #e4e4e4;
				position: absolute;
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

		.ui-slider {
			width: 100%;
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: space-between;

			text {
				font-size: 26rpx;
				color: #999;
			}

			&>* {
				:nth-child(1) {
					width: 320rpx;
				}
			}
		}

		.wd-btn-group {
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

	.ui-setting {
		width: 100%;
		text-align: center;
		padding: 60rpx 0;

		.wd-btn-group {
			margin-top: 50rpx;
		}

		>view {
			padding: 35rpx;
			box-sizing: border-box;
			display: inline-block;
			width: 608rpx;
			min-height: 645rpx;
			border-radius: 10rpx;
		}

		.ui-date-list {
			margin-top: 40rpx;
			width: 100%;
			display: inline-flex;
			justify-content: space-between;
			align-items: center;
			font-size: 28rpx;
			color: rgb(96, 98, 102);
		}

		.ui-margin {
			margin-bottom: 40rpx;
		}

		.ui-date {
			position: relative;
			height: 60rpx;
			width: 40%;
			text-align: left;
			border-radius: 10rpx;
			background-color: #dcdcdc;
			display: flex;
		}

		.ui-date {
			align-items: center;
			justify-content: space-between;
			padding: 0 8rpx 0 20rpx;
		}

		.ui-icon {
			position: absolute;
			right: 10rpx;
			top: 50%;
			transform: translateY(-50%);
			pointer-events: none;
		}

		.ui-timing {
			display: flex;
			flex-direction: row;
			justify-content: space-between;

			.ui-timing-switch {
				font-size: 24rpx;
				display: flex;
				flex-direction: row;
				align-items: center;

				text {
					margin-left: 10rpx;
				}

				&:nth-child(1) {
					margin-bottom: 15rpx;
				}

			}

			.ui-timing-pos {
				// width: 50%;

				// &:nth-child(2) {
				// 	width: 200rpx;
				// }

				.ui-timing-active {
					display: flex;
					justify-content: space-evenly;
					align-items: center;
					margin-left: 30rpx;
					font-size: 30rpx;
					line-height: 50rpx;
					height: 50rpx;
					width: 230rpx;
					// border: 1rpx solid #e2e2e2;
					color: #606266;
					padding: 0 10rpx;
					box-sizing: border-box;

					text {
						display: inline-block;
						margin-right: 30rpx;
						// width: 160rpx;
					}

					&:nth-child(2) {
						margin-top: 44rpx;
					}
				}
			}
		}

		.ui-setting-btn {
			margin-top: 60rpx;

			button {
				font-size: 25rpx;
				width: 200rpx;
			}
		}

		.ui-switch {
			margin-top: 20rpx;
			font-size: 28rpx;
			display: flex;
			flex-direction: row;
			align-items: center;

			text {
				margin-left: 20rpx;
				color: #515151;
			}
		}

		.ui-zone {
			margin-top: 15rpx;
			text-align: left;
			color: #2979FF;
			text-decoration: underline;
		}

		.ui-btn-group {
			display: flex;

			button {
				width: 135rpx;
				height: 70rpx;
				border-radius: 35rpx;
				font-size: 28rpx;

				&:nth-child(2) {
					width: 206rpx;
					height: 70rpx;

				}
			}
		}
	}
</style>
