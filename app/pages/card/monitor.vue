<!--
* @Description: 远程监听
-->

<template>
	<app-body title="安全守护">
		<!-- <app-logo color="#353535" text="远程监听"></app-logo> -->
		<CardTitle :title="'远程监听'" :content="'您可以实时获取设备环境声音'" :backGroundImg="'/static/images/long-range-monitor.png'">
		</CardTitle>
		<view class="ui-btn">
			<button :disabled="disabled" :class="disabled ? '' : 'default'" @click="handleStart">开始监听</button>
		</view>
		<view class="ui-content" v-if="disabled">
			<view class="ui-time">
				<text>{{second}}</text>
			</view>
			<view class="ui-font">设备回拨中，请等待…</view>
		</view>
	</app-body>
</template>

<script>
	import {
		PostRemoteMonitor
	} from '@/common/http/api.js';
	import CardTitle from '@/components/card-title/card-title.vue';
	import {
		mapState,
	} from 'vuex';
	export default {
		components: {
			CardTitle
		},
		computed: {
			...mapState({
				/**所有家庭列表**/
				deviceInfo: state => state.deviceInfo
			}),

		},
		data() {
			return {
				flag: '0', //0关闭，1开启
				second: 60,
				disabled: false, //倒计时开关
				countDown: ''
			};
		},
		onHide() {
			clearInterval(this.countDown)
			uni.navigateBack()
		},
		onBackPress() {
			clearInterval(this.countDown)
		},
		destroyed() {
			clearInterval(this.countDown)
		},
		methods: {
			handleStart() {
				console.log('开始倒计时......')
				if (this.disabled) return
				this.disabled = true
				PostRemoteMonitor({
					deviceId: this.deviceInfo.deviceId,
					requestCall: "1"
				}).then(res => {
					uni.$u.toast(res.msg)
					if (this.second == 60) {
						this.countDown = setInterval(() => {
							this.second--
							if (this.second == 0) {
								this.disabled = false
								this.second = 60
								clearInterval(this.countDown)
							}
						}, 1000)
					}
				})
			}
		}
	};
</script>

<style lang="scss">
	.ui-btn {
		width: 686rpx;
		margin: 0 auto;
		margin-top: 104rpx;

		button {
			color: #FFFFFF !important;
			background-color: #D4D4D4 !important;
			border-radius: 44rpx !important;
		}
	}

	.ui-content {
		width: 100%;
		margin-top: 104rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.ui-time {
		width: 144rpx;
		height: 144rpx;
		border-radius: 100%;
		background-color: #76BAFF;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 64rpx;
		color: #FFFFFF;
		line-height: 64rpx;
		font-weight: 500;
	}

	.ui-font {
		margin-top: 32rpx;
		font-size: 28rpx;
		color: #353535;
		line-height: 28rpx;
		font-weight: 500;
	}
</style>