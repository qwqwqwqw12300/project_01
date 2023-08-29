<!-- 修改设备名称 -->
<template>
	<app-body :bg="false">
		<input type="text" style="background-color: pink; height: 60px;" v-model="patternVal">
		<button @click="save">保存</button>
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
				})
				// this.getPattern()
			}
		}
	}
</script>

<style lang="scss" scoped>
	.app-main {
		width: calc(100% - 20rpx);
		padding-top: 28rpx;
		padding-left: 20rpx;
		box-sizing: border-box;
	}

	.tips {
		padding: 20rpx 0;
		border-bottom: 1px solid #b6b6b4;

		&-text {
			color: #7d7d7d;
			font-size: 28rpx;
		}
	}

	.pattern {
		width: 100%;

		&-item {
			display: flex;
			flex-wrap: wrap;
			margin-bottom: 10rpx;
			padding: 20rpx 0;

			&_name {
				width: 100%;
				padding-bottom: 4rpx;
			}

			&_info {
				color: #b6b6b4;
				font-size: 24rpx;
			}
		}

		/deep/ .u-radio-group--row {
			flex-wrap: wrap;
		}

		/deep/ .u-radio-label--right {
			width: 100%;
			border-bottom: 1px solid #b6b6b4;
		}
	}

	/deep/ .u-navbar__content__right__text {
		color: #fb7105;
		font-size: 34rpx;
	}

	/deep/ .u-radio-label--right {
		display: flex;
	}
</style>