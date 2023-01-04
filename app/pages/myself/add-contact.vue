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
					<uni-data-select v-model="value" :clear="false" :localdata="range"></uni-data-select>
				</view>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="/static/images/tel.png" :iconStyle="{width: '30rpx',height:'40rpx'}" text="手机号码"
					color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u-input placeholder="输入紧急联系手机号码" :border="'none'" :password="true" fontSize="28rpx" clearable>
					</u-input>
				</view>
			</view>
		</view>
	</app-body>
</template>

<script>
	export default {
		data() {
			return {
				value: '',
				range: [{
					value: 0,
					text: '设备一'
				}, {
					value: 1,
					text: '设备二'
				}, {
					value: 2,
					text: '设备三'
				}]
			}
		},
		methods: {

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
	}
</style>
