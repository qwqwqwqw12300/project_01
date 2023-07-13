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
							<!-- <view class="pattern-item_info">{{item.info}}</view> -->
							<view class="pattern-item_info" v-if="item.type === '0' || item.type === '1'">
								1、发生预警事件时，SOS智能网关会发出蜂鸣声；<br />
								2、发生预警事件时，SOS智能网关依次拨打您设置的紧急电话；（有人接听即停止拨打，最多拨打3轮）；<br />
								3、发生预警事件时，您将收到APP或短信广播通知。
							</view>
							<view class="pattern-item_info" v-if="item.type === '2' || item.type === '3'">
								1、红外及门磁子设备发生预警事件时，SOS智能网关不发出蜂鸣声，且不拨打您设置的紧急电话；<br />
								2、其它设备发生预警事件时，SOS智能网关不发出蜂鸣声，并依次拨打您设置的紧急电话；（有人接听即停止拨打，最多拨打3轮）；<br />
								3、所有设备发生预警事件时，您将收到APP或短信广播通知。
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
		setGatewayDeployment
	} from '@/common/http/api.js';

	import {
		mapState,
	} from 'vuex';
	export default {
		data() {
			return {
				patternList: [{
					name: '蜂鸣模式',
					type: '1'
				}, {
					name: '静音模式',
					type: '0'
				}, {
					name: '居家蜂鸣模式',
					type: '2'
				}, {
					name: '居家静音模式',
					type: '3'
				}, ],
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
			console.log(this.deviceInfo, 'deviceInfo')
		},
		methods: {
			radioChange(e) {
				this.patternVal = e
				this.set()
			},
			set() {
				setGatewayDeployment({
					deviceId: this.deviceInfo.deviceId,
					deploymentState: this.patternVal
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