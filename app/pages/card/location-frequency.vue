<!-- 修改设备名称 -->
<template>
	<app-body :bg="false">
		<view class="app-main">
			<view class="tips">
				<view class="tips-text">选择不同的定位模式，定位频率不同</view>
				<view class="tips-text">高频率定位，定位点更多，运动轨迹更细致</view>
			</view>
			<view class="pattern">
				<u-radio-group v-model="patternVal" iconPlacement="right">
					<u-radio v-for="(item, index) in patternList" :key="index" :name="item.type" activeColor="#fb7105"
						@change="radioChange">
						<view class="pattern-item">
							<view class="pattern-item_name">{{item.name}}</view>
							<view class="pattern-item_info">{{item.info}}</view>
						</view>
					</u-radio>
				</u-radio-group>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		getLocationFrequency,
		setLocationFrequency,
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
			getPattern() {
				getLocationFrequency({
					deviceId: this.deviceInfo.deviceId,
				}).then(res => {
					this.patternVal = res.data.frequency
				})
			},
			radioChange(e) {
				this.patternVal = e
				setLocationFrequency({
					deviceId: this.deviceInfo.deviceId,
					frequency: String(e)
				}).then(res => {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					});
				})
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