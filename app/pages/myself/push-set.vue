<template>
	<app-body :bg="false">
		<view class="ui-body">
			<app-logo text="消息推送设置"></app-logo>
			<view class="ui-form">
				<!-- 	<view class="ui-form-item">
					<u-icon name="email" size="37"></u-icon>
					<text class="text">消息推送</text>
					<u-switch @change="handleSwitch" v-model="flag" activeValue="1" inactiveValue="0" activeColor="#5ac725" inactiveColor="rgb(138, 138, 138)"
						size="24"> >
					</u-switch>
				</view> -->
				<u-cell-group>
					<u-cell icon="email" :iconStyle="{fontSize: '60rpx'}" :titleStyle="{fontSize: '36rpx'}"
						title="消息推送">
						<view slot="right-icon">
							<u-switch @change="handleSwitch" v-model="flag" activeValue="1" inactiveValue="0"
								activeColor="#FEAE43" inactiveColor="rgb(138, 138, 138)" size="20"> >
							</u-switch>
						</view>

					</u-cell>
				</u-cell-group>
			</view>
		</view>

	</app-body>
</template>

<script>
	import {
		PostUpdPushMsg,
		PostGetPushMsgState,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				flag: '0', //0关闭，1开启
			}
		},
		methods: {
			handleSwitch() {
				PostUpdPushMsg({
					flag: this.flag
				}).then(res => {
					uni.$u.toast(res.msg)
				})
			},
			handleInit() {
				PostGetPushMsgState({}).then(res => {
					this.flag = res.data.state + ''
				})
			}
		},
		mounted() {
			this.handleInit()
		}
	}
</script>

<style lang="scss" scoped>
	.ui-body {
		margin-top: 20rpx;
		background: #fff;
		// height: 600rpx;
	}

	.ui-form {
		margin-top: 120rpx;
		padding: 0 32rpx;

		.ui-form-item {
			padding: 30rpx;
			background-color: #f5f5f5;
			border-radius: 10rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			box-shadow: 15rpx 15rpx 20rpx #c8c5c0;

			.text {
				flex: 1;
				margin-left: 10rpx;
				font-size: 33rpx;
			}
		}
	}
</style>
