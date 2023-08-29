<template>
	<app-body :bg="false" title="心率报警设置">
		<view class="ui-form">
			<view class="u-slot-value">
				<text>心率报警</text>
				<u-switch v-model="open" space="2" size="20" activeColor="#FEAE43"
					inactiveColor="rgb(230, 230, 230)"></u-switch>
			</view>
			<view class="u-slot-value" @click="showtime">
				<text>设置心率最高值</text>
				<view class="arrow" style="border: 1px solid #ccc;">
					<u-input v-model="highRate" border="false" />
				</view>
			</view>
			<view class="u-slot-value" @click="showtime">
				<text>设置心率最低值</text>
				<view class="arrow" style="border: 1px solid #ccc;">
					<u-input v-model="lowRate" border="false" />
				</view>
			</view>
			<view class="u-slot-value" @click="showtime">
				<text>设置异常次数开始报警</text>
				<view class="arrow" style="border: 1px solid #ccc;">
					<u-input v-model="errorTime" border="false" />
				</view>
			</view>
			<view class="u-slot-value" @click="showtime">
				<text>设置报警间隔</text>
				<view class="arrow" style="border: 1px solid #ccc;">
					<u-input v-model="callTime" border="false" /> <text>分钟</text>
				</view>
			</view>
		</view>

		<view class="ui-button">
			<button @click="send" class="default">
				保存
			</button>
		</view>
	</app-body>
</template>

<script>
	import {
		mapState,
	} from 'vuex';
	import {
		watchheartReteCall,
		GetWatchInfo
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				open: true,
				highRate: 130,
				lowRate: 50,
				errorTime: 3,
				callTime: 10
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, )
		},
		onLoad(options) {

		},
		methods: {
			send() {
				watchheartReteCall({
					deviceId: this.deviceInfo.deviceId,
					open: this.open,
					high: this.highRate,
					low: this.lowRate,
					threshold: this.errorTime,
					alarmInterval: this.callTime
				}).then((res) => {
					uni.$u.toast(res.msg)
				})
			},
			getSetting() {
				GetWatchInfo({
					deviceId: this.deviceInfo.deviceId
				}).then((res) => {
					// this.open=res.aiWeiIntelligentWatchSettings
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-logo {
		margin-top: 20rpx;
		background: #ffffff;
		padding-bottom: 120rpx;
	}

	.ui-form {
		padding: 30rpx;
		background: #ffffff;

		.u-slot-value {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-bottom: 20px;
		}

		.arrow {
			display: flex;
			align-items: center;
		}
	}

	.ui-button {
		width: calc(100% - 64rpx);
		padding: 50rpx 32rpx;
		position: fixed;
		left: 0;
		bottom: 10px;

	}
</style>