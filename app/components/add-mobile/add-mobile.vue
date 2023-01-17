<template>
	<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close">
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
			<view class="ui-btn"><button @click="handleSubmit" class="wd-sms">提交</button></view>
		</view>
	</u-popup>
</template>

<script>
	import {
		PostAddDevicePhone,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				show: false,
				form: {
					phoneName: '',
					phone: '',
					type: '0',
				},
				list: [],
				deviceId: '',
				typeDict: {
					0: '设置为SOS',
					1: '设置为按钮1',
					2: '设置为按钮2',
					3: '设置为按钮3',
					P: '暂保存、不设置',
				}
			}
		},
		methods: {
			close() {
				this.form = {}
				this.deviceId = ''
				this.list = []
				this.show = false;
			},
			open(options) {
				this.deviceId = options.deviceId
				this.list = options.list
				this.show = true;
			},
			handleSubmit() {
				const {
					phone,
					phoneName
				} = this.form
				if (!phoneName) {
					uni.$u.toast('请填写名称')
				}
				if (!phone) {
					return uni.$u.toast('请填写手机号码')
				}
				const data = {
					deviceId: this.deviceId,
					list: [...this.list, {
						...this.form
					}]
				}
				PostAddDevicePhone(data).then(res => {
					uni.$u.toast(res.msg)
					this.close()
					setTimeout(() => {
						this.$emit('update')
					}, 500);
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	::v-deep {
		.u-radio {
			margin-bottom: 16rpx;
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

		.ui-btn {
			text-align: center;
			margin-top: 70rpx;

			button {
				width: 237rpx;
				height: 71rpx;
				font-size: 28rpx;
				color: #ffffff;
			}

			;

		}
	}
</style>
