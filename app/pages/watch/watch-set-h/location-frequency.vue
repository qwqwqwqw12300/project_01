<!-- 修改设备名称 -->
<template>
	<app-body :bg="false" title="定位频率">
		<view class="ui-form">
			<view class="u-slot-value">
				<text>设置定位频率</text>
				<view class="arrow" style="border: 1px solid #ccc;">
					<input class="input" type="number" v-model="patternVal">
				</view>
			</view>

		</view>

		<view class="ui-button">
			<button @click="save" class="default">
				保存
			</button>
		</view>
	</app-body>
</template>

<script>
	import {
		GetWatchInfo,
		watchHDataFreq,
	} from '@/common/http/api.js';

	import {
		mapState,
	} from 'vuex';
	export default {
		data() {
			return {
				patternList: [{
						name: '追踪模式',
						type: '3',
						info: '最快3分钟定位一次，电量消耗较大'
					},
					{
						name: '普通模式',
						type: '10',
						info: '最快10分钟定位一次，电量消耗不大 '
					}
				],
				patternVal: '',
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, )
		},
		created() {
			this.getPattern()
		},
		methods: {
			// getPattern() {
			// 	getLocationFrequency({
			// 		deviceId: this.deviceInfo.deviceId,
			// 	}).then(res => {
			// 		this.patternVal = res.data.frequency
			// 	})
			// },
			// radioChange(e) {
			// 	this.patternVal = e
			// 	setLocationFrequency({
			// 		deviceId: this.deviceInfo.deviceId,
			// 		frequency: String(e)
			// 	}).then(res => {
			// 		uni.showToast({
			// 			title: res.msg,
			// 			icon: 'none'
			// 		});
			// 	})
			// }
			getPattern() {
				GetWatchInfo({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					// 		this.patternVal = res.data.frequency
					// console.log(res);
					this.patternVal = res.data.aiWeiIntelligentWatchSettings.autoLocate.gpsIntervalTime
				})
			},
			save() {
				watchHDataFreq({
					deviceId: this.deviceInfo.deviceId,
					gpsAutoCheck: true,
					gpsIntervalTime: this.patternVal * 1
				}).then((res) => {
					uni.$u.toast(res.msg)
				})
				// this.getPattern()
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