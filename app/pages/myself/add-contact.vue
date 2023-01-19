<template>
	<app-body>
		<view class="ui-title">
			<text>添加联系人电话</text>
		</view>
		<view class="ui-form">
			<view class="ui-form-item">
				<u-text prefixIcon="/static/images/order.png" iconStyle="font-size: 32rpx" text="选择优先级" color="#444"
					size="28rpx"></u-text>
				<view class="ui-input">
					<uni-data-select v-model="form.orderNum" :clear="false" :localdata="range"></uni-data-select>
				</view>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="account" iconStyle="font-size: 40rpx" text="联系人姓名" color="#444" size="28rpx">
				</u-text>
				<view class="ui-input">
					<u-input v-model="form.phoneName" placeholder="输入紧急联系人名字" :border="'none'" fontSize="28rpx"
						clearable>
					</u-input>
				</view>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="/static/images/tel.png" :iconStyle="{width: '30rpx',height:'40rpx'}" text="手机号码"
					color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u-input v-model="form.phone" placeholder="输入紧急联系手机号码" :border="'none'" fontSize="28rpx" clearable>
					</u-input>
				</view>
			</view>
			<view class="ui-btn">
				<button @click="handleSubmit">提交</button>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		PostAddContacts,
	} from '@/common/http/api.js';
	export default {
		data() {
			const range = [{
				value: '1',
				text: '第一紧急联系人'
			}, {
				value: '2',
				text: '第二紧急联系人'
			}, {
				value: '3',
				text: '第三紧急联系人'
			}]
			return {
				form: {
					orderNum: '',
					phone: '',
					phoneName: '',
				},
				range,

			}
		},
		methods: {
			handleSubmit() {
				const {
					orderNum,
					phone,
					phoneName,
				} = this.form
				if (!orderNum) {
					return uni.$u.toast('请选择紧急联系人')
				}
				if (!phoneName) {
					return uni.$u.toast('请填写联系人姓名')
				}
				if (!uni.$u.test.mobile(phone)) {
					return uni.$u.toast('请填写正确的手机号码')
				}
				PostAddContacts({
					...this.form
				}).then(res => {
					uni.$u.toast(res.msg)
					setTimeout(() => {
						uni.navigateBack()
					}, 500)
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	::v-deep {
		.uni-select {
			border: none;
		}
	}

	.ui-title {
		margin-top: 10rpx;
		padding-left: 40rpx;

		text {
			font-size: 72rpx;
			font-weight: bold;
			color: #ffffff;
		}
	}

	.ui-form {
		margin-top: 356rpx;
		padding: 0 80rpx;
		padding-bottom: 120rpx;

		.ui-input {
			margin: 34rpx 0 60rpx 0;
			border-bottom: 1px solid #e2e2e2;
		}

		.ui-bot {
			display: flex;
			align-items: center;
			justify-content: space-between;
			font-size: 26rpx;
			color: #484848;
		}

		.ui-mini {
			width: 160rpx;
			// height: 60rpx;
		}

		.wd-btn-gloup {
			margin: 120rpx 0;
		}

		.ui-select {
			width: 506rpx;
			border-radius: 20rpx;
			// background-color: #fff;
		}

		.ui-btn {
			margin-top: 100rpx;
		}
	}
</style>
