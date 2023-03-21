<template>
	<app-body :bg="false" :bodyStyle="{backgroundColor:'#FFF'}">
		<app-logo text="设备信息"></app-logo>
		<view class="ui-scan">
			<view class="scan-box">
				<view class="box-bg" @click="handleScan">
					
				</view>
			</view>
		</view>
		<view class="ui-cell">
			<view class="cell-box">
				<u-cell-group>
					<u-cell title="定位频率"  arrow-direction="right"
						isLink titleStyle="font-size: 15px;color: #303133;" >
						<view slot="value" class="u-slot-value">
							133300001111
						</view>
					</u-cell>
					<u-cell title="IMEI号" titleStyle="font-size: 15px;color: #303133;" >
						<view slot="value" class="u-slot-value">
							89283492342
						</view>
					</u-cell>
					<u-cell title="固件版本"  titleStyle="font-size: 15px;color: #303133;" >
						<view slot="value" class="u-slot-value">
							3.5.1
						</view>
					</u-cell>
				</u-cell-group>
			</view>
		</view>
		<view class="ui-tips">
			<text>升级路径：</text>
			<text style="margin-top: 15rpx;">
				在手表-主菜单-设置-设备信息页点击【软件更新】按钮，如检测到有新的固件，手表会自动升级，升级前请确保手机电量不低于30%
			</text>
		</view>
	</app-body>

</template>

<script>
	import {
		mapState,
	} from 'vuex';
	import {
		PostCareCardUnBind
	} from '@/common/http/api';
	export default {
		data() {
			return {
				
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, ),
		},
		mounted() {},
		methods: {
			handleSwitch(){
				
			},
			
		}
	}
</script>

<style lang="scss" scoped>
	::v-deep {
		.u-cell .u-line {
			margin: 0px !important;
		}
	}
	.ui-scan {
		margin-top: 80rpx;
		padding: 0 32rpx;
		box-sizing: border-box;
	
		.scan-box {
			width: 360rpx;
			height: 360rpx;
			margin: 0 auto;
			background: #F2F2F2;
			border-radius: 16px;
			box-sizing: border-box;
			padding: 20rpx;
			
			.box-bg {
				width: 100%;
				height: 100%;
				background-image: url('@/static/images/QRcode.svg');
				background-size: 100% 100%;
				display: flex;
				align-items: center;
				justify-content: center;
			}
		}
	}
	.ui-cell {
		margin-top: 64rpx;
		padding: 0 32rpx;

		.cell-box {
			background-color: #fff;
			border-radius: 16rpx;
		}
	}
	.ui-tips{
		width:686rpx;
		padding: 32rpx 32rpx;
		// margin-top: 32rpx;
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: flex-start;
		font-size: 26rpx;
		color: #888888;
		letter-spacing: 0;
		text-align: left;
		line-height: 40rpx;
		font-weight: 400;
	}

</style>
