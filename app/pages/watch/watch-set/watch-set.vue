<template>
	<app-body :bg="false" :bodyStyle="{backgroundColor:'#FFF'}">
		<view class="ui-title">
			<view class="ui-title-left">设备配置</view>
			<view class="ui-title-right">
				<u-icon name="/static/images/restart@3x.png" color="black" size="36rpx"></u-icon>
				<text style="margin-left: 11rpx;">重启设备</text>
			</view>
		</view>
		<view class="ui-cell">
			<view class="cell-box">
				<u-cell-group>
					<!-- <u-cell v-for="(item,index) in cellList" :key="index" size="large" :title="item.title" :value="item.value"
						@tap="handleJump(item.url)" :isLink="true" titleStyle="font-size: 15px;color: #303133;">
					</u-cell> -->
					<u-cell :title="item.title" v-for="(item,index) in cellList" :key="index" arrow-direction="right"
						isLink titleStyle="font-size: 15px;color: #303133;" @tap="handleJump(item.url)">
						
						<view slot="value" class="u-slot-value">
							<text>{{item.value}}</text>
							<u-switch space="2" v-model="item.type" activeValue="1" inactiveValue="0" v-if="item.type" @click.native.stop="handleSwitch"
								size="20" activeColor="#FEAE43" inactiveColor="rgb(230, 230, 230)"></u-switch>
						</view>
					</u-cell>
				</u-cell-group>
			</view>
		</view>
		<view class="ui-button">
			<button @click="unBind">
				解绑
			</button>
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
				cellList: [{
					title: '常用联系人',
					url: '/pages/card/tel-books',
				}, {
					title: '一键同步',
					url: '/pages/card/kinship-number'
				}, {
					title: '自动定位',
					value:'30分钟',
					url: '/pages/card/enclosure/enclosure?type=set'
				}, {
					title: '跌倒检测',
					type:'1',
					url: '/pages/card/guard'
				}, {
					title: '活动设定',
					url: '/pages/card/time-interval'
				}, {
					title: '地理围栏',
					url: '/pages/card/monitor'
				}, {
					title: '设备信息',
					url: '/pages/card/monitor'
				}]
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, ),
		},
		mounted() {},
		methods: {
			handleJump(url) {
				uni.navigateTo({
					url
				})
			},
			handleSwitch(){
				
			},
			unBind() {
				const {
					humanId,
					deviceId: deviceId
				} = this.deviceInfo
				PostCareCardUnBind({
					humanId,
					deviceId
				}).then(res => {
					uni.$u.toast(res.msg)
					setTimeout(() => {
						uni.switchTab({
							url: '/pages/index/index'
						})
					}, 1000)
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	::v-deep {
		.u-cell .u-line {
			margin: 0px !important;
		}
	}
	.ui-title{
		margin-top: 40rpx;
		width:100%;
		display: flex;
		align-items: center;
		justify-content: space-between;
		.ui-title-left{
			margin-left: 32rpx;
			font-size: 50rpx;
			color: #353535;
			letter-spacing: 0;
			text-align: left;
			line-height: 50rpx;
			font-weight: 600;
		}
		.ui-title-right{
			margin-right: 32rpx;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}
	.ui-detail {
		margin-top: 52rpx;
		padding: 0 32rpx;

		.detail-box {
			background-color: #fff;
			border-radius: 16rpx;
			padding: 32rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.device-img {
				width: 150rpx;
				height: 150rpx;
			}

			.device-info {
				flex: 1;
				align-self: flex-start !important;
				padding-left: 10rpx;
				padding-top: 10rpx;

				.title {
					color: #353535;
					font-size: 34rpx;
					font-weight: 500;
					padding-left: 4rpx;
					display: flex;
					align-items: center;

					.name {
						max-width: 240rpx;
						white-space: nowrap;
						text-overflow: ellipsis;
						overflow: hidden;
					}

					.line {
						margin-left: 30rpx;
						color: #fff;
						font-size: 22rpx;
						padding: 6rpx 12rpx;
						// background-image: linear-gradient(90deg, #1EC862 0%, #13B98F 100%);
						border-radius: 4px;
					}
				}

				.status {
					// height: 60rpx;
					margin-top: 4rpx;
					display: flex;
					align-items: center;

					text {
						font-size: 26rpx;
						color: #888888;
					}
				}
			}

			.device-set {
				width: 180rpx;
				display: flex;
				color: #FEAE43;
				font-size: 30rpx;
				font-weight: 400;
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

	.ui-button {
		margin-top: 104rpx;
		padding: 0 32rpx;

		button {
			color: #FF4645;
			font-size: 32rpx;
			background: #FFFFFF;
			border: 1px solid rgba(233,86,86,1);
			border-radius: 44rpx;
		}
	}

	.online {
		background-image: linear-gradient(90deg, #1EC862 0%, #13B98F 100%);
	}

	.offline {
		background-image: linear-gradient(90deg, #ff4800 0%, #FF7E00 100%);
	}
</style>
