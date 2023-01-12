<template>
	<app-body>
		<app-logo text="消息推送设置"></app-logo>
		<view class="ui-form">
			<view class="ui-form-item">
				<u-icon name="email" size="37"></u-icon>
				<text class="text">消息推送</text>
				<u-switch @change="handleSwitch" v-model="flag" activeValue="1" inactiveValue="0" activeColor="#5ac725" inactiveColor="rgb(138, 138, 138)"
					size="24"> >
				</u-switch>
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
				flag: false, //0关闭，1开启
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
					console.log(res)
				})
			}
		},
		mounted() {
			this.handleInit()
		}
	}
</script>

<style lang="scss" scoped>
	.ui-form {
		margin-top: 256rpx;
		padding: 0 60rpx;

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
