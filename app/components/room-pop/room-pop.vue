<!--
* @Author: zhanch
* @Date: 2022-12-29 17:47:02
* @FilePath: /components/add-room/add-room.vue
* @Description: 房间设置
-->

<template>
	<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close">
		<view class="ui-add">
			<u-text :prefixIcon="mode === 'add' ? 'plus-circle' : 'edit-pen-fill'"
				:iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f" size="30rpx"
				:text="mode === 'add' ? '新建房间' : '修改房间'"></u-text>
			<view class="ui-add-box">
				<view class="ui-input">
					<u-text size="28rpx" prefixIcon="home" iconStyle="font-size: 40rpx" text="房间名称"></u-text>
					<u-input maxlength="6" class="ui-room-name" v-model="form.name" placeholder="请输入房间名称"
						border="bottom" clearable>
					</u-input>
				</view>
				<!-- <view v-if="mode === 'edit'">
					<u-text size="28rpx" prefixIcon="plus-circle" iconStyle="font-size: 40rpx" text="绑定雷达波">
					</u-text>
					<view class="ui-select">
						<uni-data-select v-model="deviceId" :clear="false" :localdata="devices"></uni-data-select>
					</view>
				</view> -->
			</view>
			<view class="ui-btn"><button @click="next" class="wd-sms">{{ subTitle }}</button></view>
		</view>
	</u-popup>
</template>

<script>
	import {
		PostAddRoom,
		PostEditRoom,
	} from '@/common/http/api.js';
	export default {
		props: {
			/**模式add-添加家庭 edit-修改家庭**/
			mode: {
				default: 'add'
			},
			subTitle: {
				default: '提交'
			}
		},
		data() {
			return {
				show: false,
				familyId: '',
				form: {
					name: '', //房间名称
					familyId: '', // 家庭id
					roomId: '' // 房间id
				},
				/**要绑定的雷达波设备**/
				deviceId: ''
			}
		},
		computed: {
			devices: function() {
				return this.$store.getters.filterDevice({
					roomId: 0
				}).map(ele => ({
					text: ele.name,
					value: ele.deviceId
				}))
			}
		},
		methods: {
			close() {
				this.form = {}
				this.show = false;
			},
			open(obj = {}) {
				this.show = true;
				this.form = {
					...obj
				};
			},
			next() {
				const {
					name,
				} = this.form;
				if (!name) {
					return uni.$u.toast('请完善房间信息');
				}
				const handle = this.mode === 'add' ? PostAddRoom({
					...this.form
				}) : PostEditRoom({
					...this.form
				});
				handle.then(res => {
					uni.$u.toast('修改成功')
					this.close();
					setTimeout(() => {
						this.$emit('update', res.data && res.data.roomId)
					}, 500);
				})
			},
		}
	};
</script>

<style lang="scss">
	.ui-add {
		width: 582rpx;
		min-height: 300rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		border: 1px solid #ffcb3d;
		padding: 53rpx 31rpx;

		.ui-select {
			width: 500rpx;
			margin: 20rpx 0 0 20rpx;
			border-radius: 20rpx;
			background-color: #e4e4e4;
		}

		&>view {
			margin-top: 52rpx;

			.ui-input {
				position: relative;

				&::after {
					bottom: 10rpx;
					left: 40rpx;
					content: '';
					width: 500rpx;
					height: 1rpx;
					background: #e4e4e4;
					position: absolute;
				}

			}

			&.ui-add-box {
				margin-top: 20rpx;
				padding: 10rpx 20rpx;


				// border-bottom: 1px solid #e4e4e4;

				&>* {
					margin-top: 20rpx;
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

		.ui-room-name {
			margin-left: 20rpx;
		}

		.ui-btn {
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
