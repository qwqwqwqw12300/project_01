<template>
	<view>
		<monitor-echarts ref="echartsRef"></monitor-echarts>
		<view class="ui-tip">
			<view class="ui-item">
				<view class="span1"></view>
				<text>监控区域</text>
			</view>
			<view class="ui-item">
				<view class="span2"></view>
				<text>子区域</text>
			</view>
			<view class="ui-item">
				<u-icon name="/static/images/person.png" color="#fff" size="50rpx"></u-icon>
				<text>人员</text>
			</view>
			<view class="ui-item">
				<u-icon name="/static/images/person-down.png" color="#fff" size="44rpx"></u-icon>
				<text>跌倒</text>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		GetStartDevice,
		GetEndDevice,
	} from '@/common/http/api';
	export default {
		props: {
			deviceInfo: {
				type: Object,
				default: () => {},
			}
		},
		data() {
			return {
				messageList: [],
			}
		},
		watch: {
			deviceInfo: {
				handler(val) {
					if (val.deviceId) {
						this.handleStart()
					}
				},
				immediate: true,
				deep: true,
			}
		},
		onUnload() {
			this.handleEnd()
		},
		mounted() {

		},
		methods: {
			onRefresh() {

			},
			close() {

			},
			handleStart() {
				GetStartDevice({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					this.$refs.echartsRef.handleOnload(uni.$u.deepClone(this.deviceInfo))
				})
			},
			handleEnd() {
				GetEndDevice({
					deviceId: this.deviceInfo.deviceId
				})
			},
		}
	}
</script>

<style lang="scss">
	.ui-tip {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0rpx 70rpx;
		margin-top: -30rpx;
	}

	.ui-item {
		display: flex;
		flex-direction: row;
		align-items: center;

		.span1 {
			width: 60rpx;
			height: 40rpx;
			border-radius: 10rpx;
			border: 1px solid gray;
			background-color: #ffffff;
			margin-right: 10rpx;
		}

		.span2 {
			width: 60rpx;
			height: 40rpx;
			border-radius: 10rpx;
			border: 1px solid gray;
			background-color: rgba(215, 215, 215, 0.7);
			margin-right: 14rpx;
		}

		text {
			font-size: 24rpx;
		}
	}
</style>
