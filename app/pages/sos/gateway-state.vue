<!-- 修改设备名称 -->
<template>
	<app-body :bg="false">
		<view class="app-main">
			<!-- <view class="tips">
				<view class="tips-text">选择不同的定位模式，定位频率不同</view>
				<view class="tips-text">高频率定位，定位点更多，运动轨迹更细致</view>
			</view> -->
			<view class="pattern">
				<u-radio-group v-model="patternVal" iconPlacement="right">
					<u-radio v-for="(item, index) in patternList" :key="index" :name="item.type" activeColor="#fb7105"
						@change="radioChange">
						<view class="pattern-item">
							<view class="pattern-item_name">{{item.name}}</view>
							<view class="pattern-item_info">{{item.info}}</view>
							<view v-if="item.type === '1' && patternVal === '1'" style="margin-top: 10rpx;">
								<u-checkbox-group v-model="checked" @change="checkboxChange">
									<u-checkbox  activeColor="red" name="门磁和红外是否静音" label="门磁和红外是否静音"></u-checkbox>
								</u-checkbox-group>
							</view>
						</view>
					</u-radio>
				</u-radio-group>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		setGatewayState
	} from '@/common/http/api.js';

	import {
		mapState,
	} from 'vuex';
	export default {
		data() {
			return {
				checked: [],
				patternList: [{
					name: '蜂鸣模式',
					type: '1',
					info: '网关收到报警事件，主动播放警报声音'
				}, {
					name: '静音模式',
					type: '0',
					info: '网关收到报警事件，不播放警报声音'
				}],
				patternVal: ''
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, )
		},
		onLoad(options) {
			this.patternVal = options.type
			console.log(options.type)
		},
		methods: {
			checkboxChange(e) {
				this.checked = e
				this.set()
			},
			radioChange(e) {
				this.patternVal = e
				this.set()
			},
			set() {
				setGatewayState({
					deviceId: this.deviceInfo.deviceId,
					state: String(this.patternVal) === '0' ? '00' : '02',
					doorMagneticSilence: this.checked.length && this.patternVal === '1' ? '1' : '0'
				}).then(res => {
					this.$store.dispatch('updateDevacesInfo');
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

	// .tips {
	// 	padding: 20rpx 0;
	// 	border-bottom: 1px solid #b6b6b4;

	// 	&-text {
	// 		color: #7d7d7d;
	// 		font-size: 28rpx;
	// 	}
	// }

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