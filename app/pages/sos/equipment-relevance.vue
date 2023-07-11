<!-- 修改设备名称 -->
<template>
	<app-body :bg="false">
		<view class="app-main">
			<view class="list" v-for="(item, index) in list" :key="index">
				<!-- 名称、类型、imei -->
				<view class="list-title">
					<view class="list-title-left">
						<!-- 类型 -->
						<view class="list-title-left-type">{{getType(item.accessoryType, 0)}}</view>
						<!-- 名称、imei -->
						<view class="list-title-left-name">
							<view class="list-title-left-name-text">{{item.name}}</view>
							<view class="list-title-left-name-imei">{{item.imei}}</view>
						</view>
					</view>
				</view>
				<!-- 配件ID、配件编号、设备型号 -->
				<view class="list-info">
					<view class="list-info-id">
						<view class="list-info-id-name">配件id</view>
						<view class="list-info-id-num">{{item.accessoryId}}</view>
					</view>
					<view class="list-info-no">
						<view class="list-info-no-name">配件编号</view>
						<view class="list-info-no-num">{{item.accessoryNo}}</view>
					</view>
					<view class="list-info-type">
						<view class="list-info-type-name">设备类型</view>
						<view class="list-info-type-text">{{getType(item.accessoryType, 1)}}</view>
					</view>
				</view>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		getGatewaySubDevice,
		delAccessoryDevice
	} from '@/common/http/api.js';

	import {
		mapState,
	} from 'vuex';
	export default {
		data() {
			return {
				list: []
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, ),
			getType() {
				return function(type, num) {
					return {
						'01': !num ? '红外' : '红外报警器',
						'02': !num ? '门磁' : '门磁报警器',
						'03': !num ? '烟雾' : '烟雾报警器',
						'04': !num ? '燃气' : '燃气报警器',
						'05': !num ? '一氧' : '一氧化碳报警器 ',
						'06': !num ? '遥控' : '遥控器',
						'07': !num ? '紧急' : '紧急按钮',
						'08': !num ? '水侵' : '水侵报警器',
						'09': !num ? '声光' : '声光警号',
					}[type]
				}
			}
		},
		created() {
			this.getList()
		},
		methods: {
			getList() {
				console.log(this.deviceInfo, 'deviceInfodeviceInfo')
				getGatewaySubDevice({
					deviceId: this.deviceInfo.deviceId,
				}).then(res => {
					this.list = res.data
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.app-main {
		width: 100%;
		padding-top: 28rpx;
		// padding-left: 30rpx;
		box-sizing: border-box;
	}
	.list {
		padding: 28rpx 28rpx 0 28rpx;
		background-color: #ffffff;
		margin-bottom: 20rpx;
		&-title {
			&-left {
				height: 100%;
				display: flex;
				align-items: center;
				&-type {
					padding: 24rpx;
					border: 1px solid #f2f2f2;
					border-radius: 50%;
					font-size: 28rpx;
					color: #e6ba33;
				}
				&-name {
					height: 100%;
					margin-left: 20rpx;
					display: flex;
					flex-direction: column;
					justify-content: space-between;
					font-size: 28rpx;
					&-text {
						padding-bottom: 10rpx;
					}
					&-imei {
						color: #dddbda;
					}
				}
			}
		}
		&-info {
			width: 100%;
			border-top: 1px solid #f2f2f2;
			margin-top: 20rpx;
			padding-top: 20rpx;
			padding-bottom: 28rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			// overflow-x: scroll;
			// white-space: nowrap;
			>view {
				margin-left: 40rpx;
				&:first-child {
					margin-left: 0;
				}
			}
			// justify-content: space-between;
			&-id, &-no, &-type {
				text-align: center;
				font-size: 30rpx;
				&-name {
					color: #cfcfcf;
				}
				&-num {
					
				}
			}
			&-no {
				&-name {
					
				}
				&-num {
					
				}
			}
		}
	}
</style>