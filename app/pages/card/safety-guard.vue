<!-- 修改设备名称 -->
<template>
	<app-body :bg="false">
		<view class="ui-logo">
			<app-logo color="#353535" text="安全守护"></app-logo>
		</view>
		<view class="app-main">
			<view class="tips">
				设置好家和学校的位置，当宝贝离开或到达时，您将收到通知(过小的安全区提醒，不在宝贝身边也能实时知道安全状态。域容易导致误报，请慎重选择，建议调整为200米以上)
			</view>
			<view class="guard">
				<view class="guard-item" v-for="(item, index) in guardList" :key="index">
					<image class="guard-item-img" :src="item.img" mode=""></image>
					<view class="guard-item-content">
						<image class="guard-item-content-icon" :src="item.icon" mode=""></image>
						<view class="guard-item-content-info">
							<view class="guard-item-content-info-name">{{ item.name }}方圆{{ item.radius }}米</view>
							<view class="guard-item-content-info-location">{{ item.location }}</view>
						</view>
						<u-switch v-model="item.switch" activeColor="#FEAE43" inactiveColor="rgb(230, 230, 230)"></u-switch>
					</view>
				</view>
			</view>
		</view>
		<view class="app-bottom">
			<button class="app-bottom-btn" @tap="toEnclosure">添加新的区域守护</button>
		</view>
	</app-body>
</template>

<script>
	// import {} from '@/common/http/api.js';

	import {
		mapState,
	} from 'vuex';
	export default {
		data() {
			return {
				guardList: [{
					// img: require('@/static/images/bluetooth.png'),
					img: 'https://img2.baidu.com/it/u=2582324909,1978308042&fm=253&fmt=auto&app=138&f=JPEG?w=812&h=500',
					icon: require('@/static/images/add-home.png'),
					name: '家',
					radius: '200',
					location: '福州仓山区鹿玲路192号东方明珠大厦',
					switch: true
				}, {
					// img: require('@/static/images/bluetooth.png'),
					img: 'https://img2.baidu.com/it/u=2582324909,1978308042&fm=253&fmt=auto&app=138&f=JPEG?w=812&h=500',
					icon: require('@/static/images/add-home.png'),
					name: '家',
					radius: '200',
					location: '福州仓山区鹿玲路192号东方明珠大厦',
					switch: true
				}, {
					// img: require('@/static/images/bluetooth.png'),
					img: 'https://img2.baidu.com/it/u=2582324909,1978308042&fm=253&fmt=auto&app=138&f=JPEG?w=812&h=500',
					icon: require('@/static/images/add-home.png'),
					name: '家',
					radius: '200',
					location: '福州仓山区鹿玲路192号东方明珠大厦',
					switch: true
				}]
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, )
		},
		methods: {
			toEnclosure() {
				uni.navigateTo({
					url: '/pages/card/enclosure/enclosure?type=set'
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-logo {
		background-color: #ffffff;
		padding-bottom: 34rpx;
	}

	.app-main {
		padding: 20rpx 32rpx;
		padding-bottom: 266rpx;
	}

	.tips {
		text-align: justify;
		font-size: 26rpx;
		color: #888888;
		font-weight: 400;
	}

	.guard {
		&-item {
			&-img {
				width: 100%;
				height: 300rpx;
				display: block;
				padding-top: 20rpx;
			}

			&-content {
				display: flex;
				justify-content: space-between;
				align-items: center;
				padding: 26rpx 32rpx 32rpx 32rpx;
				background-color: #ffffff;
				&-icon {
					width: 64rpx;
					height: 64rpx;
				}

				&-info {
					padding-left: 20rpx;
					&-name {
						width: 100%;
						font-size: 32rpx;
						color: #353535;
						font-weight: 400;
						padding-bottom: 10rpx;
					}

					&-location {
						font-size: 26rpx;
						color: #888888;
						font-weight: 400;
					}
				}
			}
		}
	}
	.app-bottom {
		position: fixed;
		bottom: 0;
		padding-top: 20rpx;
		padding-bottom: 54rpx;
		z-index: 9;
		width: 100%;
		height: 88rpx;
		background-color: #f2f2f2;
		&-btn {
			width: 686rpx;
			background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
			border-radius: 44rpx;
			font-size: 36rpx;
			color: #FFFFFF;
			text-align: center;
			font-weight: 400;
		}
	}
</style>