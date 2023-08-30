<template>
	<app-body :bg="false" title="血氧报警设置">
		<view class="ui-form">
			<view class="u-slot-value">
				<text>血氧报警</text>
				<u-switch v-model="open" space="2" size="20" activeColor="#FEAE43"
					inactiveColor="rgb(230, 230, 230)"></u-switch>
			</view>
			<view class="u-slot-value">
				<text>设置血氧最低值</text>
				<view class="arrow" style="border: 1px solid #ccc;">
					<input class="input" type="number" v-model="lowOxygen">
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
		watchbloodOxygen,
		GetWatchInfo
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				open: '',
				lowOxygen: 0
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, )
		},
		created() {
			this.getSetting()
		},
		methods: {
			send() {
				watchbloodOxygen({
					deviceId: this.deviceInfo.deviceId,
					open: this.open,
					low: this.lowOxygen * 1
				}).then((res) => {
					uni.$u.toast(res.msg)
				})
			},
			getSetting() {
				GetWatchInfo({
					deviceId: this.deviceInfo.deviceId
				}).then((res) => {
					this.open = res.data.aiWeiIntelligentWatchSettings.spoAlarmVO.open
					this.lowOxygen = res.data.aiWeiIntelligentWatchSettings.spoAlarmVO.low
				})
			},

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

			// width: 60px;
			.input {
				width: 60px;
			}
		}
	}

	.ui-button {
		width: calc(100% - 64rpx);
		padding: 50rpx 32rpx;
		position: fixed;
		left: 0;
		bottom: 10px;

	}

	/deep/ .uni-input-wrapper {
		text-align: center;
	}

	/deep/ .minute {
		width: 32px;
	}
</style>